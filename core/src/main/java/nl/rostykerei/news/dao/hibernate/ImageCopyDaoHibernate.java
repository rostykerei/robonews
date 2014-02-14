/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.dao.hibernate;

import nl.rostykerei.news.dao.ImageCopyDao;
import nl.rostykerei.news.domain.ImageCopy;

public class ImageCopyDaoHibernate extends AbstractDaoHibernate<ImageCopy, Integer> implements ImageCopyDao {

    public ImageCopyDaoHibernate() {
        super(ImageCopy.class);
    }
}
