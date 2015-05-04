package io.robonews.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * Created by rosty on 04/05/15.
 */
@Entity
@Table(name = "geo_category")
@Inheritance
public class GeoCategory extends NestedSet {
    @Id
    @Column(name = "id")
    @GeneratedValue
    private int id;

    @Column(name = "name")
    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    @OneToMany(mappedBy = "geoCategory")
    private Collection<Feed> feeds;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Feed> getFeeds() {
        return feeds;
    }

    public void setFeeds(Collection<Feed> feeds) {
        this.feeds = feeds;
    }
}
