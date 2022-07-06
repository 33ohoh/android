package com.example.competition1.pestprediction;

public class PestsOnCropDTO {
    String cropName;
    String pests;

    public PestsOnCropDTO(String cropName, String pests){
        this.cropName = cropName;
        this.pests = pests;
    }

    public String getCropName(){
        return cropName;
    }

    public String getPests(){
        return pests;
    }
}
