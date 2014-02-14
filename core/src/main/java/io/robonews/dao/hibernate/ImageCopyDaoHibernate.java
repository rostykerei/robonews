/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.ImageCopyDao;
import io.robonews.domain.ImageCopy;

public class ImageCopyDaoHibernate extends AbstractDaoHibernate<ImageCopy, Integer> implements ImageCopyDao {

    public ImageCopyDaoHibernate() {
        super(ImageCopy.class);
    }
}
