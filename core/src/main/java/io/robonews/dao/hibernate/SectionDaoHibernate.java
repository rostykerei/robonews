/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.dao.hibernate;

import io.robonews.dao.SectionDao;
import io.robonews.domain.Section;

public class SectionDaoHibernate extends AbstractDaoHibernate<Section, Integer> implements SectionDao {

    public SectionDaoHibernate() {
        super(Section.class);
    }
}
