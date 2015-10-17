package com.housing.typeracer.models;

import java.io.Serializable;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class ListItem implements Serializable {

    private String speed;
    private int drawableId;
    private int progress;

    public String getSpeed() {
        return speed;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getProgress() {
        return progress;
    }

    public ListItem(int drawableId, int progress, String speed) {
        this.speed = speed;
        this.drawableId = drawableId;
        this.progress = progress;
    }

}
