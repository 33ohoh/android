package com.example.competition1.API;

import android.os.AsyncTask;

import org.w3c.dom.NodeList;

public class pestAPITask extends AsyncTask<String,Void,NodeList> {
    @Override
    protected NodeList doInBackground(String... string) {
        loadAPIData data = new loadAPIData();
        if (string[1].equals("symptom"))
            return data.load_Data("http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC01&serviceType=[AA001:XML,AA002:Ajax]&cropName=" + string[0]);
        else//이미지
            return data.load_Data("http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC03&serviceType=[AA001:XML,AA002:Ajax]&cropName=" + string[0]);
    }

}
