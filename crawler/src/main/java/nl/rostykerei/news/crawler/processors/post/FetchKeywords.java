package nl.rostykerei.news.crawler.processors.post;

import java.util.Set;
import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
import nl.rostykerei.news.domain.TagAmbiguous;
import nl.rostykerei.news.service.freebase.FreebaseService;
import nl.rostykerei.news.service.freebase.FreebaseServiceException;
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
            Tag tag = tagDao.findByAlternative(namedEntity.getName());

            if (tag == null) {

                TagAmbiguous tagAmbiguous = tagDao.findTagAmbiguous(namedEntity.getName());

                if (tagAmbiguous == null) {
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
                    catch (FreebaseServiceException e) {
                        // do nothing..
                        continue;
                    }

                    if (freebaseSearchResult == null) {
                        tagDao.createTagAmbiguous(namedEntity.getName());
                        continue;
                    }
                    else {
                        tag = tagDao.findOrCreateTagWithAlternative(freebaseSearchResult.getName(),
                                namedEntity.getName(), freebaseSearchResult.getScore(), freebaseSearchResult.getMid(),
                                Tag.Type.valueOf(namedEntity.getType().toString()));
                    }
                }
                else {
                    tagAmbiguous.incrementEffort();
                    tagDao.saveTagAmbiguous(tagAmbiguous);
                    continue;
                }
            }

            story.getTags().remove(tag);
            story.getTags().add(tag);
        }

        try {
            storyDao.merge(story);
        }
        catch (Exception e) {
            System.out.println("xxxxxxxxxxxxxxxxxx");
        }

        return story;
    }
}
