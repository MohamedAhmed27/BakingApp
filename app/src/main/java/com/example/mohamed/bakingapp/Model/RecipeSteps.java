package com.example.mohamed.bakingapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Mohamed on 8/14/2018.
 */

public class RecipeSteps implements Serializable {

    int id;
    String shortDescription;
    String description;
    String videoURL;
    String ThumnailUrl;

    public String getThumnailUrl() {
        return ThumnailUrl;
    }

    public void setThumnailUrl(String thumnailUrl) {
        ThumnailUrl = thumnailUrl;
    }

    public RecipeSteps()
    {

    }

    public RecipeSteps(int id, String shortDescription, String description, String videoURL,String ThumnailUrl) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoURL = videoURL;
        this.ThumnailUrl=ThumnailUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }


}
