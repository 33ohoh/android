package com.PastPest.competition1.ReportHistory;

import java.io.Serializable;

public class ReportHistory implements Serializable {
    private String id;
    private String title;
    private String date;
    private String address;
    private String cropName;
    private String symptom;
    private String pestName;
    private String imageUrl;
    private String details;

    public ReportHistory(String id, String title, String date, String address, String cropName,
                         String symptom, String pestName, String imageUrl, String details){
        this.id = id;
        this.title = title;
        this.date = date;
        this.address = address;
        this.cropName = cropName;
        this.symptom = symptom;
        this.pestName = pestName;
        this.imageUrl = imageUrl;
        this.details = details;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getDate(){
        return date;
    }

    public String getSymptom() {
        return symptom;
    }

    public String getCropName() {
        return cropName;
    }

    public String getAddress() {
        return address;
    }

    public String getDetails() {
        return details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPestName() {
        return pestName;
    }

}
