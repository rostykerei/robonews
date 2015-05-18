/**
 * Robonews.io
 *
 * Copyright (c) 2013-2015 Rosty Kerei.
 * All rights reserved.
 */
package io.robonews.console.dto;

import io.robonews.domain.Topic;

import javax.validation.constraints.Size;

public class TopicDto {

    private int id;

    @Size(min = 1, max = 255)
    private String name;

    private int level;

    private int leftIndex;

    private int rightIndex;

    private boolean priority;

    public static TopicDto fromTopic(Topic topic) {
        TopicDto dto = new TopicDto();
        dto.setId(topic.getId());
        dto.setName(topic.getName());
        dto.setPriority(topic.isPriority());
        dto.setLevel(topic.getLevel());
        dto.setLeftIndex(topic.getLeftIndex());
        dto.setRightIndex(topic.getRightIndex());

        return dto;
    }

    public Topic updateTopic(Topic topic) {
        topic.setId(getId());
        topic.setName(getName());
        topic.setLevel(getLevel());
        topic.setPriority(isPriority());
        topic.setLeftIndex(getLeftIndex());
        topic.setRightIndex(getRightIndex());

        return topic;
    }

    public Topic toTopic() {
        return updateTopic(new Topic());
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

    public boolean isPriority() {
        return priority;
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }
}
