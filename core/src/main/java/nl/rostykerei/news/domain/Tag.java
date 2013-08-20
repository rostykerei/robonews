package nl.rostykerei.news.domain;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(name = "tag", uniqueConstraints = @UniqueConstraint(columnNames = {"typeId", "name"}))
public class Tag {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "typeId")
    private int type;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Version
    @Column(name = "version")
    private long version;

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

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
