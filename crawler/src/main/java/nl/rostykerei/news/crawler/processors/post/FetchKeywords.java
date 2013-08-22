package nl.rostykerei.news.crawler.processors.post;

import java.util.Set;
import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.exception.AmbiguousResultException;
import nl.rostykerei.news.service.freebase.exception.FreebaseServiceException;
import nl.rostykerei.news.service.freebase.exception.NotFoundException;
import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
import nl.rostykerei.news.service.nlp.NamedEntityRecognizerService;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class FetchKeywords implements StoryPostProcessor {

    @Autowired
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private StoryDao storyDao;

    @Autowired
    private FreebaseService freebaseService;

    @Override
    public Story postProcess(Story story) {
        String text = story.getTitle() + ". " + story.getDescription();

        Set<NamedEntity> namedEntities = namedEntityRecognizerService.getNamedEntities(text);

        for (NamedEntity namedEntity : namedEntities) {
            Tag tag = getTagByNamedEntity(namedEntity);

            if (tag != null) {
                story.getTags().remove(tag);
                story.getTags().add(tag);
            }
        }

        try {
            storyDao.merge(story);
        }
        catch (Exception e) {
            System.out.println("xxxxxxxxxxxxxxxxxx");
        }

        return story;
    }

    private Tag getTagByNamedEntity(NamedEntity namedEntity) {
        Tag.Type tagType = Tag.Type.valueOf(namedEntity.getType().toString());
        Tag tag = tagDao.findByAlternative(namedEntity.getName(), tagType);

        if (tag != null) {
            return tag;
        }

        FreebaseSearchResult freebaseSearchResult = null;

        try {
            switch (namedEntity.getType()) {
                case PERSON:
                    freebaseSearchResult = freebaseService.searchForPerson(namedEntity.getName());
                    break;
                case LOCATION:
                    freebaseSearchResult = freebaseService.searchForLocation(namedEntity.getName());
                    break;
                case ORGANIZATION:
                    freebaseSearchResult = freebaseService.searchForOrganization(namedEntity.getName());
                    break;
                case MISC:
                    freebaseSearchResult = freebaseService.searchForMiscellaneous(namedEntity.getName());
                    break;
            }
        }
        catch (NotFoundException e) {
            return tagDao.createTagWithAlternative(
                    namedEntity.getName(),
                    tagType,
                    null, true, namedEntity.getName(), 0f);
        }
        catch (AmbiguousResultException e) {
            return tagDao.createTagWithAlternative(
                    namedEntity.getName(),
                    tagType,
                    null, true, namedEntity.getName(), 0f);
        }
        catch (FreebaseServiceException e) {
            return null;
        }

        tag = tagDao.findByFreebaseMind(freebaseSearchResult.getMid());

        if (tag == null) {
            return tagDao.createTagWithAlternative(freebaseSearchResult.getName(),
                    tagType,
                    freebaseSearchResult.getMid(),
                    false, namedEntity.getName(), freebaseSearchResult.getScore());
        }
        else {
            tagDao.createTagAlternative(tag, tagType, namedEntity.getName(), freebaseSearchResult.getScore());
            return tag;
        }

    }
}
