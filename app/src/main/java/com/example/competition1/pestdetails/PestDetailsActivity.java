package com.example.competition1.pestdetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.competition1.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PestDetailsActivity extends AppCompatActivity {

    private String imageUrl;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_details);

        Intent intent = getIntent();
        PestDetails pestDetails = (PestDetails)intent.getSerializableExtra("details");

        ImageView imageView = findViewById(R.id.imageView);
        TextView pestName = findViewById(R.id.tx_pest_name);
        TextView symptom = findViewById(R.id.tx_symptom);
        TextView controlMethod = findViewById(R.id.tx_pest_control_method);

        pestName.setText(String.format("%s(%s)",                         //병해충명
                pestDetails.getPsetName(), pestDetails.getCropName()));

        if(pestDetails.getSymptom().equals("")){
            findViewById(R.id.ll_symptom).setVisibility(View.GONE);
        }
        else{
            symptom.setText(pestDetails.getSymptom());                       //증상
        }

        controlMethod.setText(pestDetails.getControlMethod());           //방제방법

        imageUrl = pestDetails.getImage();

        Glide.with(this).load(imageUrl).into(imageView);
    }

}
