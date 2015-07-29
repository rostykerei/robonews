/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.worker.editor.publisher;

import io.robonews.worker.editor.editor.impl.EditorResult;

/**
 * Created by rosty on 25/07/15.
 */
public interface Publisher {

    void publish(EditorResult editorResult);

}
