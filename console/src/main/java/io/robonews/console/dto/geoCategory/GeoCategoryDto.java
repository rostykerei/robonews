package io.robonews.console.dto.geoCategory;

import io.robonews.domain.Area;

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

    public static GeoCategoryDto fromGeoCategory(Area area) {
        GeoCategoryDto dto = new GeoCategoryDto();
        dto.setId(area.getId());
        dto.setName(area.getName());
        dto.setLevel(area.getLevel());
        dto.setLeftIndex(area.getLeftIndex());
        dto.setRightIndex(area.getRightIndex());

        return dto;
    }

    public Area updateGeoCategory(Area area) {
        area.setId(getId());
        area.setName(getName());
        area.setLevel(getLevel());
        area.setLeftIndex(getLeftIndex());
        area.setRightIndex(getRightIndex());

        return area;
    }

    public Area toGeoCategory() {
        return updateGeoCategory(new Area());
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
