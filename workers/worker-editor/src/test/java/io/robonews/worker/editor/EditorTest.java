/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.editor;

import io.robonews.messaging.domain.SectionComposeMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;


@ContextConfiguration({ "classpath:workerEditorContext-test.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class EditorTest {

    @Autowired
    private Editor editor;

    @Test
    public void test() {
        editor.listen(
            new SectionComposeMessage(1, new Date(1438128000000L), new Date(1438214400000L))
        );
    }

}