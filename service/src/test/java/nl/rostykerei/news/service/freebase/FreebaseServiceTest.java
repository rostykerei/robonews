package nl.rostykerei.news.service.freebase;

import nl.rostykerei.news.service.freebase.impl.FreebaseSearchResult;
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
public class FreebaseServiceTest {

    @Autowired
    private FreebaseService freebaseService;

    @Test
    public void testSearchForPerson() throws Exception {
        Assert.assertEquals("Barack Obama", freebaseService.searchForPerson("Obama").getName());
        Assert.assertEquals("Vladimir Putin", freebaseService.searchForPerson("Putin").getName());
    }

    @Test
    public void testSearchForLocation() throws Exception {
        Assert.assertEquals("United States of America", freebaseService.searchForLocation("U.S.").getName());
    }

    @Test
    public void testSearchForOrganization() throws Exception {
        Assert.assertEquals("Transportation Security Administration", freebaseService.searchForOrganization("TSA").getName());
    }

    @Test
    public void testSearchForMiscellaneous() throws Exception {
        Assert.assertEquals("Olympic Games", freebaseService.searchForMiscellaneous("Olympic").getName());
    }
}
