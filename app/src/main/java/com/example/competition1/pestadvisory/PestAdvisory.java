package com.example.competition1.pestadvisory;

public class PestAdvisory {
    private String psetName;
    private String image;
    private String controlPeriod;

    public PestAdvisory(String psetName, String image, String controlPeriod){
        this.psetName = psetName;
        this.image = image;
        this.controlPeriod = controlPeriod;
    }

    public String getPsetName() {
        return psetName;
    }

    public String getImage() {
        return image;
    }

    public String getControlPeriod() {
        return controlPeriod;
    }
}
