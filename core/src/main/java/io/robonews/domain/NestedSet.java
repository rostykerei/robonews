package io.robonews.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Created by rosty on 04/05/15.
 */
@MappedSuperclass
public abstract class NestedSet {

    @Column(name = "level", updatable = false)
    private int level;

    @Column(name = "leftIndex", updatable = false)
    private int leftIndex;

    @Column(name = "rightIndex", updatable = false)
    private int rightIndex;

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
