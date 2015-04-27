package io.robonews.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "country", uniqueConstraints = {
                @UniqueConstraint(columnNames = "isoCode2"),
                @UniqueConstraint(columnNames = "isoCode3")})
public class Country {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "isoCode2", unique = true, nullable = false, length = 2)
    private String isoCode2;

    @NotNull
    @Column(name = "isoCode3", unique = true, nullable = false, length = 3)
    private String isoCode3;

    @NotNull
    @Column(name = "name", unique = false, nullable = false, length = 255)
    private String name;

    public int getId() {
        return id;
    }

    public String getIsoCode2() {
        return isoCode2;
    }

    public String getIsoCode3() {
        return isoCode3;
    }

    public String getName() {
        return name;
    }
}
