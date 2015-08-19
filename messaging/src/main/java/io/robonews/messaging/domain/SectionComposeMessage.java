/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.messaging.domain;

import java.util.Date;

/**
 * Created by rosty on 06/08/15.
 */
public class SectionComposeMessage {

    private int sectionId;

    private Date timestampFrom;

    private Date timestampTo;

    public SectionComposeMessage() {
    }

    public SectionComposeMessage(int sectionId, Date timestampFrom, Date timestampTo) {
        this.sectionId = sectionId;
        this.timestampFrom = timestampFrom;
        this.timestampTo = timestampTo;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public Date getTimestampFrom() {
        return timestampFrom;
    }

    public void setTimestampFrom(Date timestampFrom) {
        this.timestampFrom = timestampFrom;
    }

    public Date getTimestampTo() {
        return timestampTo;
    }

    public void setTimestampTo(Date timestampTo) {
        this.timestampTo = timestampTo;
    }
}
