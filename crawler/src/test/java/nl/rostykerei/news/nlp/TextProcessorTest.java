package nl.rostykerei.news.nlp;

import nl.rostykerei.news.domain.NamedEntityType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@ContextConfiguration({ "classpath:crawlerContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class TextProcessorTest {

    @Autowired
    private TextProcessor textProcessor;

    @Test
    public void testGetNamedEntities() {

        Set<TextNamedEntity> expected = new HashSet<TextNamedEntity>();
        expected.add(new TextNamedEntity("Joe", NamedEntityType.PERSON));
        expected.add(new TextNamedEntity("John", NamedEntityType.PERSON));
        expected.add(new TextNamedEntity("David Smith", NamedEntityType.PERSON));
        expected.add(new TextNamedEntity("San Francisco", NamedEntityType.LOCATION));
        expected.add(new TextNamedEntity("US", NamedEntityType.LOCATION));

        Assert.assertEquals(expected, textProcessor.getNamedEntities("Joe, John and David Smith were walking in San Francisco, US with John"));
    }

    @Test
    public void testGetNamedEntities2() {
        Set<TextNamedEntity> expected = new HashSet<TextNamedEntity>();
        expected.add(new TextNamedEntity("Chris Hallam", NamedEntityType.PERSON));
        expected.add(new TextNamedEntity("Paralympic", NamedEntityType.MISC));
//        Assert.assertEquals(expected, textProcessor.getNamedEntities("Paralympic pioneer Chris Hallam dies. Tributes are paid to Chris Hallam, a Paralympic pioneer and one of the most charismatic characters in disabled sport, who has died."));


        Assert.assertEquals(expected, textProcessor.getNamedEntities("Beyonce's pixie is dust as she reveals new bob hairdo. The long and short of it when it comes to Beyoncé and her hair is that you should expect it to continually change. The latest case in point: The singer stepped out in Miami sporting a new blonde bob.Just a week after revealing a short new pixie cut on Instagram, Mrs."));
    }
}
