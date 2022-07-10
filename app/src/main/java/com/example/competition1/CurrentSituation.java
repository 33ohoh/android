package com.example.competition1;


public class CurrentSituation {

    private String imageUrl;
    private String cropName;
    private String description;
    private String id;
    private String address;
    private String title;
    private String date;
    private String symptom;
    private String pestName;
    public CurrentSituation(String imageUrl, String cropName, String description,String id, String address,String title, String date,String symptom ,String pestName){
        this.imageUrl = imageUrl;
        this.cropName = cropName;
        this.id = id;
        this.address = address;
        this.description = description;
        this.title = title;
        this.date = date;
        this.symptom =symptom;
        this.pestName = pestName;
    }

    public String getPestName() {
        return pestName;
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
    public String getSymptom() {
        return symptom;
    }
    public String getDescription() {
        return description;
    }
    public String getTitle(){
        return title;
    }
    public String getDate(){
        return date;
    }
}
