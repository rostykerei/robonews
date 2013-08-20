package nl.rostykerei.news.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tag_ambiguous", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class TagAmbiguous {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private long id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "effort")
    private int effort;

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

    public int getEffort() {
        return effort;
    }

    public void setEffort(int effort) {
        this.effort = effort;
    }
}
