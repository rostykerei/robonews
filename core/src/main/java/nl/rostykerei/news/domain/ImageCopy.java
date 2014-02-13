package nl.rostykerei.news.domain;

import nl.rostykerei.news.util.KeyGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "image_copy",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"uid"})
    })
public class ImageCopy {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @NotNull
    @Column(name = "uid", length = 11, nullable = false, unique = true)
    @GeneratedValue
    private String uid = KeyGenerator.generateKey();

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "imageId", nullable = false)
    private Image image;

    @NotNull
    @Column(name = "typeId")
    private int type;

    @NotNull
    @Column(name = "directory", length = 6, nullable = false)
    private String directory;

    @NotNull
    @Column(name = "seed", nullable = false, unique = true)
    @GeneratedValue
    private int seed = KeyGenerator.generateSeed();

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
    @Column(name = "size", nullable = false)
    private long size;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createdDate", unique = false, nullable = false)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleteAfterDate", unique = false, nullable = false)
    private Date deleteAfterDate;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image.Type getType() {
        return Image.Type.getType(type);
    }

    public void setType(Image.Type type) {
        this.type = type.getId();
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public int getSeed() {
        return seed;
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

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getDeleteAfterDate() {
        return deleteAfterDate;
    }

    public void setDeleteAfterDate(Date deleteAfterDate) {
        this.deleteAfterDate = deleteAfterDate;
    }
}
