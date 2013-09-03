package nl.rostykerei.news.crawler.processors.post;

import java.util.Set;
import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.StoryTag;
import nl.rostykerei.news.domain.Tag;
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
import org.springframework.transaction.annotation.Transactional;

public class FetchKeywords implements StoryPostProcessor {

    @Autowired
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FreebaseService freebaseService;

    private Logger logger = LoggerFactory.getLogger(FetchKeywords.class);


    @Override
    public Story postProcess(Story story) {
        String text = story.getTitle() + ". " + story.getDescription();

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
                continue;
            }
        }

        return story;
    }

    @Transactional
    private Tag getTagByNamedEntity(NamedEntity namedEntity) {

        Tag.Type altType = Tag.Type.valueOf(namedEntity.getType().toString());

        Tag tag = tagDao.findByAlternative(namedEntity.getName(), altType);

        if (tag != null) {
            return tag;
        }

        FreebaseSearchResult freebaseSearchResult = null;

        try {
            freebaseSearchResult = freebaseService.search(namedEntity);
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
            logger.info("Freebase service exception", e);
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
}
