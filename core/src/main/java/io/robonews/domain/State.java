/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "state", uniqueConstraints = @UniqueConstraint(columnNames = {"countryId", "isoCode"}))
public class State {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Country.class, optional = false)
    @JoinColumn(name = "countryId", referencedColumnName = "id", nullable = false)
    private Country country;

    @NotNull
    @Column(name = "isoCode", unique = true, nullable = false, length = 8)
    private String isoCode;

    @NotNull
    @Column(name = "name", unique = false, nullable = false, length = 255)
    private String name;

    public int getId() {
        return id;
    }

    public Country getCountry() {
        return country;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }
}
