/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.service.clustering;

import java.util.Comparator;


public class DocumentComparator {

    public static Comparator<Document> ID_DESC = (o1, o2) -> o2.getId().compareTo(o1.getId());

}
