/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.ImageDao;
import io.robonews.domain.Image;
import org.springframework.transaction.annotation.Transactional;

public class ImageDaoHibernate extends AbstractDaoHibernate<Image, Integer> implements ImageDao {

    public ImageDaoHibernate() {
        super(Image.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Image getByUrl(String url) {
        return (Image) getSession().createQuery("from Image i " +
                "where i.url = :url").
                setString("url", url).
                setMaxResults(1).
                uniqueResult();
    }

    @Override
    @Transactional(readOnly = true)
    public Image getByIndex(int channelId, long size, long crc32) {
        return (Image) getSession().createQuery("from Image i " +
                "where i.sourceChannel.id = :channelId and " +
                "i.size = :size and " +
                "i.crcHash = :crc32").
                setInteger("channelId", channelId).
                setLong("size", size).
                setLong("crc32", crc32).
                setMaxResults(1).
                uniqueResult();
    }

}
