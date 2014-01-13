package nl.rostykerei.news.worker.tagger;

import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.messaging.domain.TagMessage;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.nlp.NamedEntityRecognizerService;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
