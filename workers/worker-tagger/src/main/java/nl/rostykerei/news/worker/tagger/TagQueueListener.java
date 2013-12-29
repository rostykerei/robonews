package nl.rostykerei.news.worker.tagger;

import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.messaging.domain.NewStoryMessage;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.exception.AmbiguousResultException;
import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.exception.NotFoundException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.nlp.NamedEntityRecognizerService;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class TagQueueListener {

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Autowired
    private FreebaseService freebaseService;

    private Logger logger = LoggerFactory.getLogger(TagQueueListener.class);

    public void listen(NewStoryMessage message) {

        Story story = storyDao.getById(message.getId());

        if (story == null) {
            return;
        }

        String text = story.getTitle() != null ? story.getTitle() + ". " : "";
        text = text + (story.getDescription() != null ? story.getDescription() : "");

        Set<NamedEntity> namedEntities = namedEntityRecognizerService.getNamedEntities(text);

        for (NamedEntity namedEntity : namedEntities) {
            try {
                Tag tag = getTagByNamedEntity(namedEntity);

                if (tag != null && !story.getTags().contains(tag)) {
                    story.getTags().add(tag);
                    storyDao.saveStoryTag(new StoryTag(story, tag));
                }
            }
            catch (RuntimeException e) {
                logger.warn("Cannot process named entity", e);
                continue;
            }
        }
    }

    //@Transactional ???
    private Tag getTagByNamedEntity(NamedEntity namedEntity) {

        Tag.Type altType = Tag.Type.valueOf(namedEntity.getType().toString());

        Tag tag = tagDao.findByAlternative(namedEntity.getName(), altType);

        if (tag != null) {
            return tag;
        }

        FreebaseSearchResult freebaseSearchResult = null;

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
        catch (NotFoundException e) {
            return tagDao.createTagWithAlternative(
                    namedEntity.getName(),
                    altType,
                    null, true, namedEntity.getName(), altType, 0f);
        }
        catch (AmbiguousResultException e) {
            return tagDao.createTagWithAlternative(
                    namedEntity.getName(),
                    altType,
                    null, true, namedEntity.getName(), altType, 0f);
        }
        catch (FreebaseServiceException e) {
            logger.warn("Freebase service exception", e);
            return null;
        }

        if (freebaseSearchResult == null) {
            return null;
        }

        tag = tagDao.findByFreebaseMind(freebaseSearchResult.getMid());

        if (tag == null) {
            return tagDao.createTagWithAlternative(freebaseSearchResult.getName(),
                    Tag.Type.valueOf(freebaseSearchResult.getType().toString()),
                    freebaseSearchResult.getMid(),
                    false, namedEntity.getName(), altType, freebaseSearchResult.getScore());
        }
        else {
            tagDao.createTagAlternative(tag, altType, namedEntity.getName(), freebaseSearchResult.getScore());
            return tag;
        }
    }

    public void setStoryDao(StoryDao storyDao) {
        this.storyDao = storyDao;
    }

    public void setTagDao(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    public void setNamedEntityRecognizerService(NamedEntityRecognizerService namedEntityRecognizerService) {
        this.namedEntityRecognizerService = namedEntityRecognizerService;
    }

    public void setFreebaseService(FreebaseService freebaseService) {
        this.freebaseService = freebaseService;
    }
}
