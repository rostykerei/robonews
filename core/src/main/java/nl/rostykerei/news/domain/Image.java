package nl.rostykerei.news.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "image",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"url"}),
        @UniqueConstraint(columnNames = {"sourceChannelId", "size", "crcHash"})
    })
public class Image {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sourceChannelId", referencedColumnName = "id", nullable = false)
    private Channel sourceChannel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "sourceStoryId", referencedColumnName = "id", nullable = false)
    private Story sourceStory;

    @NotNull
    @Column(name = "typeId")
    private int type;

    @NotNull
    @Column(name = "url", unique = true, nullable = false, length = 255)
    private String url;

    @NotNull
    @Column(name = "size", nullable = false)
    private long size;

    @NotNull
    @Column(name = "width", nullable = false)
    private int width;

    @NotNull
    @Column(name = "height", nullable = false)
    private int height;

    @NotNull
    @Column(name = "ratio")
    private float ratio;

    @NotNull
    @Column(name = "crcHash", nullable = false)
    private long crcHash;

    @NotNull
    @Column(name = "pHash", nullable = false)
    private byte[] pHash;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", unique = false, nullable = false)
    private Date createdDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Story getSourceStory() {
        return sourceStory;
    }

    public void setSourceStory(Story sourceStory) {
        this.sourceStory = sourceStory;
    }

    public Channel getSourceChannel() {
        return sourceChannel;
    }

    public void setSourceChannel(Channel sourceChannel) {
        this.sourceChannel = sourceChannel;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
        updateRatio();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        updateRatio();
    }

    public float getRatio() {
        return ratio;
    }

    private void updateRatio() {
        if (this.height > 0 && this.width > 0) {
            this.ratio = (float) this.width / this.height;
        }
        else {
            this.ratio = 0;
        }
    }

    public long getCrcHash() {
        return crcHash;
    }

    public void setCrcHash(long crcHash) {
        this.crcHash = crcHash;
    }

    public byte[] getpHash() {
        return pHash;
    }

    public void setpHash(byte[] pHash) {
        this.pHash = pHash;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public static enum Type {
        JPEG(1),
        PNG(2),
        GIF(3);

        private int id;

        private static final Map<Integer, Type> lookup = new HashMap<Integer, Type>();

        static {
            for (Type item : Type.values()) {
                lookup.put(item.getId(), item);
            }
        }

        private Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Type getType(int id) {
            return lookup.get(id);
        }
    }

    public Type getType() {
        return Type.getType(type);
    }

    public void setType(Type type) {
        this.type = type.getId();
    }

}
