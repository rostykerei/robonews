package nl.rostykerei.news.domain;

import org.apache.commons.codec.digest.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "Story", uniqueConstraints = @UniqueConstraint(columnNames = {"channelId", "guidHash"}))
public class Story {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "channelId", referencedColumnName = "id", nullable = false)
    private Channel channel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id", nullable = false)
    private Category category;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "originalFeedId", referencedColumnName = "id", nullable = false)
    private Feed originalFeed;

    @Column(name = "guidHash", unique = false, nullable = false, length = 40)
    private String guidHash;

    @Column(name = "title", unique = false, nullable = false, length = 255)
    private String title;

    @Column(name = "author", unique = false, nullable = true, length = 255)
    private String author;

    @Column(name = "link", unique = false, nullable = false, length = 255)
    private String link;

    @Column(name = "guid", unique = false, nullable = false, length = 255)
    private String guid;

    @Column(name = "isVideo")
    private boolean video;

    @Column(name = "publicationDate", unique = false, nullable = false)
    private Date publicationDate;

    @Column(name = "createdDate", unique = false, nullable = false)
    private Date createdDate;

    @Column(name = "description", unique = false, nullable = false)
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Feed getOriginalFeed() {
        return originalFeed;
    }

    public void setOriginalFeed(Feed originalFeed) {
        this.originalFeed = originalFeed;
    }

    public String getGuidHash() {
        return guidHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
        this.guidHash = DigestUtils.sha1Hex(guid);
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
