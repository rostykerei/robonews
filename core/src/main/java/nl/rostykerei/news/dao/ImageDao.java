/**
 * Robonews.io
 *
 * Copyright (c) 2013-2014 Rosty Kerei.
 * All rights reserved.
 */
package nl.rostykerei.news.dao;

import nl.rostykerei.news.domain.Image;

public interface ImageDao extends AbstractDao<Image, Integer> {

    Image getByUrl(String url);

    Image getByIndex(int channelId, long size, long crc32);

}
