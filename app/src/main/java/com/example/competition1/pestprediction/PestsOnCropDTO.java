package com.example.competition1.pestprediction;

public class PestsOnCropDTO {
    String cropName;
    String content;

    public PestsOnCropDTO(String cropName, String content){
        this.cropName = cropName;
        this.content = content;
    }

    public String getCropName(){
        return cropName;
    }

    public String getContent(){
        return content;
    }
}
