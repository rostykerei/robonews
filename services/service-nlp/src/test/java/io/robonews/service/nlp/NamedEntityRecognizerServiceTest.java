/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.nlp;

import io.robonews.service.nlp.impl.NamedEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

@ContextConfiguration({ "classpath:serviceNlpContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class NamedEntityRecognizerServiceTest {

    @Autowired
    private NamedEntityRecognizerService namedEntityRecognizerService;

    @Test
    public void testGetNamedEntities() throws Exception {
        Set<NamedEntity> expected = new HashSet<NamedEntity>();

        // Note, tf is not a part of NamedEntity hashCode function

        expected.add(new NamedEntity(NamedEntity.Type.PERSON, "Joe", 0f));
        expected.add(new NamedEntity(NamedEntity.Type.PERSON, "John", 0f));
        expected.add(new NamedEntity(NamedEntity.Type.PERSON, "David Smith", 0f));
        expected.add(new NamedEntity(NamedEntity.Type.LOCATION, "San Francisco", 0f));
        expected.add(new NamedEntity(NamedEntity.Type.LOCATION, "US", 0f));

        Assert.assertEquals(expected, namedEntityRecognizerService.getNamedEntities("Joe, John and David Smith were walking in San Francisco, US with John"));

    }

    @Test
    public void test2() {
        //Set<NamedEntity> n = namedEntityRecognizerService.getNamedEntities("Youth Football Leagues Hope for Boost From NFL Settlement.");
        //Youth/UPPER Football/LOWER Leagues/LOWER Hope/INIT_UPPER for/LOWER Boost/LOWER From/UPPER NFL/O Settlement/LOWER./O
        Set<NamedEntity> n = namedEntityRecognizerService.getNamedEntities("Youth Football Leagues Hope for Boost From NFL Settlement");

        System.out.print(n);
    }


}