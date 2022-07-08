package com.example.competition1;


public class CurrentSituation {

    private String imageUrl;
    private String cropName;
    private String description;

    public CurrentSituation(String imageUrl, String cropName, String description){
        this.imageUrl = imageUrl;
        this.cropName = cropName;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCropName(){
        return cropName;
    }

    public String getDescription() {
        return description;
    }
}
