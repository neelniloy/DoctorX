package com.sarker.hellodoctor.model;

public class SlideInfo {

    public String description;
    public String imageUrl;
    public String imageKey;

    public SlideInfo (){
        //Empty Constructor
    }

    public SlideInfo(String description, String imageUrl, String imageKey) {
        this.description = description;
        this.imageUrl = imageUrl;
        this.imageKey = imageKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
