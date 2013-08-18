package nl.rostykerei.news.crawler.processors.post;

import nl.rostykerei.news.crawler.processors.StoryPostProcessor;
import nl.rostykerei.news.dao.NamedEntityDao;
import nl.rostykerei.news.dao.StoryDao;
import nl.rostykerei.news.domain.Story;
import nl.rostykerei.news.nlp.TextNamedEntity;
import nl.rostykerei.news.nlp.TextProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class FetchKeywords implements StoryPostProcessor {

    @Autowired
    private TextProcessor textProcessor;

    @Autowired
    private NamedEntityDao namedEntityDao;

    @Autowired
    private StoryDao storyDao;

    @Override
    public Story postProcess(Story story) {
        String text = story.getTitle() + ". " + story.getDescription();

        Set<TextNamedEntity> textNamedEntities = textProcessor.getNamedEntities(text);

        for (TextNamedEntity textNamedEntity : textNamedEntities) {
            story.getNamedEntities().add( namedEntityDao.findOrCreateNamedEntity(textNamedEntity.getName(), textNamedEntity.getType()) );
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
