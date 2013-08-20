package nl.rostykerei.news.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(name = "tag", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"typeId", "name"}),
        @UniqueConstraint(columnNames = {"freebase_mid"})})
public class Tag {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "typeId")
    private int type;

    @Column(name = "freebase_mid", nullable = false, length = 255)
    private String freebaseMid;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Version
    @Column(name = "version")
    private long version;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag", targetEntity = TagAlternative.class, cascade = CascadeType.ALL)
    private Set<TagAlternative> tagAlternatives = new HashSet<TagAlternative>();

    public static enum Type {
        PERSON(1),
        LOCATION(2),
        ORGANIZATION(3),
        MISC(4);

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Type getType() {
        return Type.getType(type);
    }

    public void setType(Type type) {
        this.type = type.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFreebaseMid() {
        return freebaseMid;
    }

    public void setFreebaseMid(String freebaseMid) {
        this.freebaseMid = freebaseMid;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Set<TagAlternative> getTagAlternatives() {
        return tagAlternatives;
    }

    public void setTagAlternatives(Set<TagAlternative> tagAlternatives) {
        this.tagAlternatives = tagAlternatives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != tag.id) return false;
        if (type != tag.type) return false;
        //if (version != tag.version) return false;
        if (freebaseMid != null ? !freebaseMid.equalsIgnoreCase(tag.freebaseMid) : tag.freebaseMid != null) return false;
        if (name != null ? !name.equalsIgnoreCase(tag.name) : tag.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + type;
        result = 31 * result + (freebaseMid != null ? freebaseMid.toLowerCase().hashCode() : 0);
        result = 31 * result + (name != null ? name.toLowerCase().hashCode() : 0);
       // result = 31 * result + (int) (version ^ (version >>> 32));
        return result;
    }
}
