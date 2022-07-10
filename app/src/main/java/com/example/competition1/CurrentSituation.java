package com.example.competition1;


public class CurrentSituation {

    private String imageUrl;
    private String cropName;
    private String description;
    private String id;
    private String address;

    public CurrentSituation(String imageUrl, String cropName, String description,String id, String address){
        this.imageUrl = imageUrl;
        this.cropName = cropName;
        this.id = id;
        this.address = address;
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public String getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }

    public String getCropName(){
        return cropName;
    }

    public String getDescription() {
        return description;
    }
}
