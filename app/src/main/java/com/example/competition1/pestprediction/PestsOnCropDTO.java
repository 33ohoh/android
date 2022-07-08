package com.example.competition1.pestprediction;

public class PestsOnCropDTO {
    String type;
    String cropName;
    String highLevel;
    String mediumLevel;
    String lowLevel;

    public PestsOnCropDTO(String type, String cropName, String highLevel, String mediumLevel, String lowLevel){
        this.type = type;
        this.cropName = cropName;
        this.highLevel = highLevel;
        this.mediumLevel = mediumLevel;
        this.lowLevel = lowLevel;
    }

    public String getType() { return type; }

    public String getCropName(){
        return cropName;
    }

    public String getHighLevel(){
        return highLevel;
    }

    public String getMediumLevel(){
        return mediumLevel;
    }

    public String getLowLevel(){
        return lowLevel;
    }
}
