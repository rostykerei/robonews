package nl.rostykerei.news.service.nlp;

import java.util.HashSet;
import java.util.Set;
import nl.rostykerei.news.service.core.NamedEntityType;
import nl.rostykerei.news.service.nlp.impl.NamedEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration({ "classpath:serviceContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
public class NamedEntityRecognizerServiceTest {

    @Autowired
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Test
    public void testGetNamedEntities() throws Exception {
        Set<NamedEntity> expected = new HashSet<NamedEntity>();
        expected.add(new NamedEntity(NamedEntityType.PERSON, "Joe"));
        expected.add(new NamedEntity(NamedEntityType.PERSON, "John"));
        expected.add(new NamedEntity(NamedEntityType.PERSON, "David Smith"));
        expected.add(new NamedEntity(NamedEntityType.LOCATION, "San Francisco"));
        expected.add(new NamedEntity(NamedEntityType.LOCATION, "US"));

        Assert.assertEquals(expected, namedEntityRecognizerService.getNamedEntities("Joe, John and David Smith were walking in San Francisco, US with John"));
    }
}
