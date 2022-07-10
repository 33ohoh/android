package com.example.competition1;

public class Mapdata {

    private String productName;
    private String details;
    private Double latitude;
    private Double longitude;
    private String id;
    private String address;
    private String title;
    private String date;

    public Mapdata(String productName, String details, Double latitude, Double longitude,String id, String address,String title, String date){
        this.productName = productName;
        this.details = details;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.address = address;
        this.title = title;
        this.date = date;
    }

    public String getProductName() {
        return productName;
    }

    public String getDetails() {
        return details;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
    public String getId() {
        return id;
    }
    public String getAddress() {
        return address;
    }

    public String getTitle() {
        return title;
    }
    public String getDate() {
        return date;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
