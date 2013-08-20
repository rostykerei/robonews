package nl.rostykerei.news.crawler.processors.post;

import java.util.Set;
import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.Tag;
import nl.rostykerei.news.domain.TagAlternative;
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
                    tagDao.logAmbiguous(namedEntity.getName());
                }
                else {
                    Tag tagByMid = tagDao.findByFreebaseMid(freebaseSearchResult.getMid());

                    TagAlternative tagAlternative = new TagAlternative();
                    tagAlternative.setName(namedEntity.getName());
                    tagAlternative.setConfidence(freebaseSearchResult.getScore());

                    if (tagByMid == null) {
                        Tag newTag = new Tag();
                        newTag.setFreebaseMid(freebaseSearchResult.getMid());
                        newTag.setType(Tag.Type.valueOf(namedEntity.getType().toString()));
                        newTag.setName(freebaseSearchResult.getName());

                        tagAlternative.setTag(newTag);
                        newTag.getTagAlternatives().add(tagAlternative);

                        story.getTags().add(newTag);
                    }
                    else {
                        tagAlternative.setTag(tagByMid);
                        tagByMid.getTagAlternatives().add(tagAlternative);

                        tagDao.update(tagByMid);

                        story.getTags().add(tagByMid);
                    }
                }
            }
            else {
                story.getTags().add(tag);
            }
        }

        try {
            storyDao.update(story);
        }
        catch (Exception e) {
            System.out.println("xxxxxxxxxxxxxxxxxx");
        }

        return story;
    }
}
