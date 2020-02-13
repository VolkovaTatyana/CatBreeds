package com.tatyanavolkova.catbreeds.pojo;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageObject {

    @SerializedName("id")
    private String id;

    @SerializedName("url")
    private String url;

    @SerializedName("breeds")
    private List<Object> breeds = null;

    @SerializedName("categories")
    private List<Object> categories = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Object> getBreeds() {
        return breeds;
    }

    public void setBreeds(List<Object> breeds) {
        this.breeds = breeds;
    }

    public List<Object> getCategories() {
        return categories;
    }

    public void setCategories(List<Object> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public String toString() {
        if (id != null && url != null)
            return "id=" + id + " url=" + url;
        else return "null";
    }
}
