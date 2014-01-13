package nl.rostykerei.news.messaging.domain;

/**
 * Created with IntelliJ IDEA.
 * User: rosty
 * Date: 1/7/14
 * Time: 7:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageMessage {

    private long storyId;

    private String imageUrl;

    public long getStoryId() {
        return storyId;
    }

    public void setStoryId(long storyId) {
        this.storyId = storyId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
