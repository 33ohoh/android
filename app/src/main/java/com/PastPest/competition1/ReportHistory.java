package com.PastPest.competition1;

import java.io.Serializable;

public class ReportHistory implements Serializable {

    private String id;
    private String address;
    private String cropName;
    private Double latitude;
    private Double longitude;
    private String symptom;
    private String pestName;
    private String imageUrl;
    private String details;
    private String title;
    private String date;
    private String isSolved;


    public ReportHistory(String title, String date, String address, String cropName,
                         String symptom, String pestName, String imageUrl, String details){
        this.title = title;
        this.date = date;
        this.address = address;
        this.cropName = cropName;
        this.symptom = symptom;
        this.pestName = pestName;
        this.imageUrl = imageUrl;
        this.details = details;
    }



    public ReportHistory(String cropName, String details, Double latitude, Double longitude,String id, String address,String title, String date,String symptom,String pestName,String isSolved){
        this.cropName = cropName;
        this.details = details;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.address = address;
        this.title = title;
        this.date = date;
        this.symptom =symptom;
        this.pestName= pestName;
        this.isSolved =isSolved;
    }

    public ReportHistory(String imageUrl, String cropName, String details,String id, String address,String title, String date,String symptom ,String pestName){
        this.imageUrl = imageUrl;
        this.cropName = cropName;
        this.id = id;
        this.address = address;
        this.details = details;
        this.title = title;
        this.date = date;
        this.symptom =symptom;
        this.pestName = pestName;
    }

    public String getIsSolved(){return isSolved;}

    public String getId() {
        return id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {return longitude;}

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
