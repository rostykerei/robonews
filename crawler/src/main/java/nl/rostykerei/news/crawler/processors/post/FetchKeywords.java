package nl.rostykerei.news.crawler.processors.post;

import java.util.Set;
import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.dao.TagDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.domain.Tag;
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

    @Override
    public Story postProcess(Story story) {
        String text = story.getTitle() + ". " + story.getDescription();

        Set<NamedEntity> namedEntities = namedEntityRecognizerService.getNamedEntities(text);

        for (NamedEntity namedEntity : namedEntities) {
            try {
                Tag tag = tagDao.findOrCreateNamedEntity(namedEntity.getName(), Tag.Type.valueOf(namedEntity.getType().toString()));
                story.getTags().add(tag);
            }
            catch (Exception e) {

            }
        }

        try {
            storyDao.update(story);
        }
        catch (Exception e) {
            System.out.println("\n\n\n\n\n");
            System.out.println("ERRRRRRRR: " + text);
            System.out.println("\n\n\n\n\n");
        }


        return story;
    }
}
