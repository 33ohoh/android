package com.example.competition1.API;

import android.os.AsyncTask;

import org.w3c.dom.NodeList;

public class DetailAPITask extends AsyncTask<String,Void, NodeList> {
    @Override
    protected NodeList doInBackground(String... string) {
        DetailAPIData data = new DetailAPIData();
        if (string[1].equals("symptom"))
            return data.load_Data("http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC05&sickKey=" + string[0]);
        else//이미지
            return data.load_Data("http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC07&insectKey=" + string[0]);
    }

}
