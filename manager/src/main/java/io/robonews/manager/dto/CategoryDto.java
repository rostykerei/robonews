/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.manager.dto;

import io.robonews.domain.Topic;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryDto {

    public CategoryDto() {
        super();
    }

    public CategoryDto(Topic topic) {
        setId(topic.getId());
        setName(topic.getName());
        setPriority(topic.isPriority());
    }

    private int id;

    @Size(min = 1, max = 255, message = "Name cannot be empty")
    private String name;

    private boolean priority;

    @NotNull
    @Min(value = 1, message = "Parent topic cannot be empty")
    private int parentCategoryId;

    public Topic toCategory(){
        Topic topic = new Topic();
        topic.setId(getId());
        topic.setName(getName());
        topic.setPriority(isPriority());

        return topic;
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

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public int getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(int parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }
}
