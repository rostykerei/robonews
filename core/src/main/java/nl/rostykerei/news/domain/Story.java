package nl.rostykerei.news.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "story",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"channelId", "guidHash"}),
                @UniqueConstraint(columnNames = {"channelId", "contentHash"}),
        })
public class Story {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;


    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    @Column(name = "uid")
    @GeneratedValue
    private UUID uid = UUID.randomUUID();

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
    private byte[] guidHash;

    @Column(name = "contentHash", unique = false, nullable = false, length = 40)
    private byte[] contentHash;

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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "publicationDate", unique = false, nullable = false)
    private Date publicationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", unique = false, nullable = false)
    private Date createdDate;

    @Column(name = "description", unique = false, nullable = false)
    private String description;

    @ManyToMany(cascade = CascadeType.ALL, targetEntity = Tag.class)
    @JoinTable(name = "story_tag",
            uniqueConstraints = @UniqueConstraint(columnNames = {"tagId", "storyId"}),
            joinColumns = {
            @JoinColumn(name = "storyId", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "tagId",
                    nullable = false, updatable = false) })
    private Set<Tag> tags = new HashSet<Tag>();

    public static final int MAXIMUM_ALLOWED_TAGS = 32;

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

    public byte[] getGuidHash() {
        return guidHash;
    }

    public byte[] getContentHash() {
        return contentHash;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateContentHash();
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
        this.guidHash = DigestUtils.sha1(guid);
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
        updateContentHash();
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> namedEntities) {
        this.tags = namedEntities;
    }

    private void updateContentHash() {
        this.contentHash = DigestUtils.sha1(
            new StringBuffer().append(getTitle()).append(getDescription()).toString()
        );
    }

}
