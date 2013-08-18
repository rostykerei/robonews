package nl.rostykerei.news.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(name = "NamedEntity", uniqueConstraints = @UniqueConstraint(columnNames = {"typeId", "name"}))
public class NamedEntity {

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public NamedEntityType getType() {
        return NamedEntityType.getType(type);
    }

    public void setType(NamedEntityType type) {
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
