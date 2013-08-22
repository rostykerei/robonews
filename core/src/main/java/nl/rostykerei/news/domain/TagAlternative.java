package nl.rostykerei.news.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tag_alternative", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class TagAlternative {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "typeId")
    private int type;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "confidence")
    private float confidence;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Tag.Type getType() {
        return Tag.Type.getType(type);
    }

    public void setType(Tag.Type type) {
        this.type = type.getId();
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
