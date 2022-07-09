package com.example.competition1.pestdetails;

import java.io.Serializable;

public class PestDetails implements Serializable {
    private String psetName;
    private String cropName;
    private String image;
    private String symptom;
    private String controlMethod;

    public PestDetails(String psetName, String cropName, String image, String symptom, String controlMethod){
        this.psetName = psetName;
        this.cropName = cropName;
        this.image = image;
        this.symptom = symptom;
        this.controlMethod = controlMethod;
    }

    public String getPsetName() {
        return psetName;
    }

    public String getCropName() {
        return cropName;
    }

    public String getSymptom() {
        return symptom;
    }

    public String getControlMethod() {
        return controlMethod;
    }

    public String getImage() {
        return image;
    }
}
