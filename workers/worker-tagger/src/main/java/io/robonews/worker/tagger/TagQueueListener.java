/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.tagger;

import io.robonews.dao.StoryDao;
import io.robonews.dao.TagDao;
import io.robonews.domain.Story;
import io.robonews.domain.StoryTag;
import io.robonews.domain.Tag;
import io.robonews.messaging.domain.SolrCreateMessage;
import io.robonews.messaging.domain.TagMessage;
import io.robonews.service.freebase.FreebaseService;
import io.robonews.service.freebase.exception.FreebaseServiceException;
import io.robonews.service.freebase.impl.FreebaseSearchResult;
import io.robonews.service.nlp.NamedEntityRecognizerService;
import io.robonews.service.nlp.impl.NamedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TagQueueListener {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private FreebaseService freebaseService;

    @Autowired
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Autowired
    @Qualifier("solrCreateMessagingTemplate")
    private RabbitTemplate solrCreateMessaging;

    private ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<String, Object>();

    private Logger logger = LoggerFactory.getLogger(TagQueueListener.class);

    public void listen(TagMessage message) {

        Story story = storyDao.getByIdWithTags(message.getStoryId());

        if (story == null) {
            return;
        }

        Set<NamedEntity> namedEntities = namedEntityRecognizerService.getNamedEntities(compileText(story, message));

        for (NamedEntity namedEntity : namedEntities) {

            if (namedEntity == null) {
                continue;
            }

            String namedEntityName = namedEntity.getName();

            if (namedEntityName == null) {
                continue;
            }
            else if (namedEntityName.length() < 2) {
                continue;
            }
            else if (namedEntityName.length() > 64) {
                continue;
            }
            else if (namedEntityName.equals(story.getTitle())) {
                continue;
            }

            try {

                Tag tag;

                locks.putIfAbsent(namedEntityName, new Object());

                synchronized (locks.get(namedEntityName)) {
                    tag = getTagByNamedEntity(namedEntity);
                }

                locks.remove(namedEntityName);

                if ((tag != null) && !story.getTags().contains(tag) && (story.getTags().size() < Story.MAXIMUM_ALLOWED_TAGS)) {
                    story.getTags().add(tag);
                    storyDao.saveStoryTag(new StoryTag(story, tag));
                }
            }
            catch (RuntimeException e) {
                logger.warn("Cannot process named entity", e);
            }
        }

        try {
            SolrCreateMessage solrCreateMessage = new SolrCreateMessage();
            solrCreateMessage.setStoryId(story.getId());

            solrCreateMessaging.convertAndSend(solrCreateMessage);
        }
        catch (RuntimeException e) {
            logger.warn("Cannot send solrCreateMessage: ", e);
        }
    }

    public Tag getTagByNamedEntity(NamedEntity namedEntity) {

        Tag.Type altType = Tag.Type.valueOf(namedEntity.getType().toString());

        Tag tag = tagDao.findByAlternative(namedEntity.getName(), altType);

        if (tag != null) {
            return tag;
        }

        FreebaseSearchResult freebaseSearchResult;

        try {
            switch (namedEntity.getType()) {
                case PERSON:
                    freebaseSearchResult = freebaseService.searchPerson(namedEntity.getName());
                    break;
                case LOCATION:
                    freebaseSearchResult = freebaseService.searchLocation(namedEntity.getName());
                    break;
                case ORGANIZATION:
                    freebaseSearchResult = freebaseService.searchOrganization(namedEntity.getName());
                    break;
                default:
                    freebaseSearchResult = freebaseService.searchMisc(namedEntity.getName());
                    break;
            }
        }
        catch (FreebaseServiceException e) {
            return createAmbiguousTag(namedEntity, altType);
        }
        catch (Exception e) {
            logger.warn("Freebase exception", e);
            return createAmbiguousTag(namedEntity, altType);
        }

        if (freebaseSearchResult == null) {
            logger.warn("Freebase service returned null");
            return createAmbiguousTag(namedEntity, altType);
        }

        tag = tagDao.findByFreebaseMid(freebaseSearchResult.getMid());

        if (tag == null) {
            synchronized (this) {
                try {
                    return tagDao.createTagWithAlternative(freebaseSearchResult.getName(),
                            Tag.Type.valueOf(freebaseSearchResult.getType().toString()),
                            freebaseSearchResult.getMid(),
                            false, namedEntity.getName(), altType, freebaseSearchResult.getScore());
                }
                catch (DataIntegrityViolationException e) {
                    tag = tagDao.findByFreebaseMid(freebaseSearchResult.getMid());

                    if (tag != null) {
                        return tag;
                    }
                    else  {
                        throw e;
                    }
                }
            }
        }
        else {
            try {
                tagDao.createTagAlternative(tag, altType, namedEntity.getName(), freebaseSearchResult.getScore());
            }
            catch (DataIntegrityViolationException e) {
                // Already there ...
            }

            return tag;
        }
    }

    private Tag createAmbiguousTag(NamedEntity namedEntity, Tag.Type type) {
        try {
            return tagDao.createTagWithAlternative(
                    namedEntity.getName(),
                    type,
                    null, true, namedEntity.getName(), type, 0f);
        }
        catch (DataIntegrityViolationException e) {
            Tag tag = tagDao.findByAlternative(namedEntity.getName(), type);

            if (tag != null) {
                return tag;
            }
            else {
                throw e;
            }
        }
    }

    private String compileText(Story story, TagMessage message) {
        StringBuilder textBuffer = new StringBuilder();

        if ( !StringUtils.isEmpty(story.getTitle()) ) {
            textBuffer.append(story.getTitle());
            textBuffer.append(". ");
        }

        if ( !StringUtils.isEmpty(story.getDescription()) ) {
            textBuffer.append(story.getDescription());
            textBuffer.append(". ");
        }

        if (message.getFoundKeywords() != null && message.getFoundKeywords().length > 0) {
            for (String keyword : message.getFoundKeywords()) {
                textBuffer.append(keyword);
                textBuffer.append(", ");
            }
        }

        String text = textBuffer.toString().trim();

        if (text.endsWith(",")) {
            text = text.substring(0, text.length() - 1);
        }

        return text;
    }
}
