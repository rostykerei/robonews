/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
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
        expected.add(new NamedEntity(NamedEntity.Type.PERSON, "Joe"));
        expected.add(new NamedEntity(NamedEntity.Type.PERSON, "John"));
        expected.add(new NamedEntity(NamedEntity.Type.PERSON, "David Smith"));
        expected.add(new NamedEntity(NamedEntity.Type.LOCATION, "San Francisco"));
        expected.add(new NamedEntity(NamedEntity.Type.LOCATION, "US"));

        Assert.assertEquals(expected, namedEntityRecognizerService.getNamedEntities("Joe, John and David Smith were walking in San Francisco, US with John"));

    }

    @Test
    public void test2() {
        Set<NamedEntity> n = namedEntityRecognizerService.getNamedEntities("Youth Football Leagues Hope for Boost From NFL Settlement.");

        System.out.print(n);
    }
}