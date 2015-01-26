/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "story_tag", uniqueConstraints = @UniqueConstraint(columnNames = {"storyId", "tagId"}))
public class StoryTag {

    private StoryTagPK pk = new StoryTagPK();

    public StoryTag() {

    }

    public StoryTag(Story story, Tag tag) {
        this.pk = new StoryTagPK();
        this.pk.setStoryId(story.getId());
        this.pk.setTagId(tag.getId());
    }

    @EmbeddedId
    public StoryTagPK getPk() {
        return pk;
    }

    public void setPk(StoryTagPK pk) {
        this.pk = pk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryTag storyTag = (StoryTag) o;

        if (pk != null ? !pk.equals(storyTag.pk) : storyTag.pk != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pk != null ? pk.hashCode() : 0;
    }

    @Embeddable
    class StoryTagPK implements Serializable {

        private long storyId;
        private long tagId;

        @Column(name = "storyId")
        long getStoryId() {
            return storyId;
        }

        void setStoryId(long storyId) {
            this.storyId = storyId;
        }

        @Column(name = "tagId")
        long getTagId() {
            return tagId;
        }

        void setTagId(long tagId) {
            this.tagId = tagId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StoryTagPK that = (StoryTagPK) o;

            if (storyId != that.storyId) return false;
            if (tagId != that.tagId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (storyId ^ (storyId >>> 32));
            result = 31 * result + (int) (tagId ^ (tagId >>> 32));
            return result;
        }
    }
}