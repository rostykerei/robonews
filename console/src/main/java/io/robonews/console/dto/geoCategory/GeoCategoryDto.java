package io.robonews.console.dto.geoCategory;

import io.robonews.domain.GeoCategory;

import javax.validation.constraints.Size;

/**
 * Created by rosty on 04/05/15.
 */
public class GeoCategoryDto {

    private int id;

    @Size(min = 1, max = 255)
    private String name;

    private int level;

    private int leftIndex;

    private int rightIndex;

    public static GeoCategoryDto fromGeoCategory(GeoCategory geoCategory) {
        GeoCategoryDto dto = new GeoCategoryDto();
        dto.setId(geoCategory.getId());
        dto.setName(geoCategory.getName());
        dto.setLevel(geoCategory.getLevel());
        dto.setLeftIndex(geoCategory.getLeftIndex());
        dto.setRightIndex(geoCategory.getRightIndex());

        return dto;
    }

    public GeoCategory updateGeoCategory(GeoCategory geoCategory) {
        geoCategory.setId(getId());
        geoCategory.setName(getName());
        geoCategory.setLevel(getLevel());
        geoCategory.setLeftIndex(getLeftIndex());
        geoCategory.setRightIndex(getRightIndex());

        return geoCategory;
    }

    public GeoCategory toGeoCategory() {
        return updateGeoCategory(new GeoCategory());
    }

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public void setLeftIndex(int leftIndex) {
        this.leftIndex = leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public void setRightIndex(int rightIndex) {
        this.rightIndex = rightIndex;
    }
}
