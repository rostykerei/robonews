package nl.rostykerei.news.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "story_image", uniqueConstraints = @UniqueConstraint(columnNames = {"storyId", "imageId"}))
public class StoryImage {

    private StoryImagePK pk = new StoryImagePK();

    public StoryImage() {

    }

    public StoryImage(Story story, Image image) {
        this.pk = new StoryImagePK();
        this.pk.setStoryId(story.getId());
        this.pk.setImageId(image.getId());
    }

    @EmbeddedId
    public StoryImagePK getPk() {
        return pk;
    }

    public void setPk(StoryImagePK pk) {
        this.pk = pk;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StoryImage storyImage = (StoryImage) o;

        if (pk != null ? !pk.equals(storyImage.pk) : storyImage.pk != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return pk != null ? pk.hashCode() : 0;
    }

    @Embeddable
    class StoryImagePK implements Serializable {

        private long storyId;
        private long imageId;

        @Column(name = "storyId")
        long getStoryId() {
            return storyId;
        }

        void setStoryId(long storyId) {
            this.storyId = storyId;
        }

        @Column(name = "imageId")
        long getImageId() {
            return imageId;
        }

        void setImageId(long imageId) {
            this.imageId = imageId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            StoryImagePK that = (StoryImagePK) o;

            if (imageId != that.imageId) return false;
            if (storyId != that.storyId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (int) (storyId ^ (storyId >>> 32));
            result = 31 * result + (int) (imageId ^ (imageId >>> 32));
            return result;
        }
    }
}