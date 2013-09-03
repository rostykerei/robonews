package nl.rostykerei.news.domain;


import java.io.Serializable;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

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
    }
}