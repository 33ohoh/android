package com.example.competition1.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.competition1.R;
import com.example.competition1.reportActivity.CropSelectActivity;
import com.example.competition1.reportActivity.LocationSelectActivity;
import com.example.competition1.reportActivity.PestSelectActivity;
import com.example.competition1.reportActivity.SymptomSelectActivity;


public class FragmentDeclaration extends Fragment {

    ImageView selectedImage;
    double latitude = 37.5495538;
    double longitude = 127.075032;
    String loadAdress = "";
    String detailAddress = "";
    String loadAddress = "현재위치";
    String cropName = "";
    String symptomName = "";
    String pestName = "";
    AppCompatButton locationBtn;
    AppCompatButton cropBtn;
    AppCompatButton symptomBtn;
    AppCompatButton pestBtn;
    AppCompatButton imageBtn;
    AppCompatButton reportBtn;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_declaration, container, false);

        EditText editText = (EditText) view.findViewById(R.id.detailText);
        selectedImage = (ImageView) view.findViewById(R.id.selectedImage);
        locationBtn = (AppCompatButton) view.findViewById(R.id.locationButton);
        cropBtn = (AppCompatButton) view.findViewById(R.id.cropButton);
        symptomBtn = (AppCompatButton) view.findViewById(R.id.symptomButton);
        pestBtn = (AppCompatButton) view.findViewById(R.id.pestButton);
        imageBtn = (AppCompatButton) view.findViewById(R.id.imageButton);
        reportBtn = (AppCompatButton) view.findViewById(R.id.reportButton);

        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), LocationSelectActivity.class);
                //intent.putExtra("key","12345".toString());
                intent.putExtra("loadAddress", loadAddress);
                intent.putExtra("detailAddress", detailAddress);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                startActivityForResult(intent, 1);
            }
        });

        cropBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), CropSelectActivity.class);
                intent.putExtra("cropName", cropName);
                startActivityForResult(intent, 2);
            }
        });

        symptomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropName.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "작물이 등록되어있지 않습니다", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SymptomSelectActivity.class);
                    intent.putExtra("selectedSymptom", symptomName);
                    intent.putExtra("cropName", cropName);
                    startActivityForResult(intent, 3);
                }
            }
        });

        pestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cropName.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "작물이 등록되어있지 않습니다", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), PestSelectActivity.class);
                    intent.putExtra("selectedPest", pestName);
                    intent.putExtra("cropName", cropName);
                    startActivityForResult(intent, 4);
                }
            }
        });

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 5);
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //데이터베이스에 결과들 저장해주는 함수 만들기
                if (loadAddress.equals("") || cropName.equals("")) {
                    Toast.makeText(getActivity().getApplicationContext(), "위치정보와 임산물 정보는 필수입니다.", Toast.LENGTH_LONG).show();
                } else {

                    //finish();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==5){
            if(resultCode==Activity.RESULT_OK){
                Glide.with(getActivity().getApplicationContext()).load(data.getData()).override(100,100).into(selectedImage);
                Toast.makeText(getActivity().getApplicationContext(),Glide.with(getActivity().getApplicationContext()).load(data.getData()).toString(),Toast.LENGTH_LONG).show();
                imageBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
        }
        else if(requestCode==1){
            if(resultCode==Activity.RESULT_OK) {
                latitude = data.getDoubleExtra("latitude",latitude);
                longitude = data.getDoubleExtra("longitude",longitude);
                detailAddress = data.getStringExtra("detailAddress");
                loadAddress=data.getStringExtra("loadAddress");
                locationBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==2){
            if(resultCode==Activity.RESULT_OK) {
                String temp=data.getStringExtra("cropName");
                if(temp.equals(""))
                    cropBtn.setBackgroundResource(R.drawable.button_background);
                else
                    cropBtn.setBackgroundResource(R.drawable.seleted_button_background);
                if(!cropName.equals(temp)) {
                    cropName = temp;
                    symptomName="";
                    pestName="";
                    symptomBtn.setBackgroundResource(R.drawable.button_background);
                    pestBtn.setBackgroundResource(R.drawable.button_background);
                }
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==3){
            if(resultCode==Activity.RESULT_OK) {
                symptomName = data.getStringExtra("selectedSymptom");
                if(symptomName.equals(""))
                    symptomBtn.setBackgroundResource(R.drawable.button_background);
                else
                    symptomBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
        else if(requestCode==4){
            if(resultCode==Activity.RESULT_OK) {
                pestName = data.getStringExtra("selectedPest");
                if(pestName.equals(""))
                    pestBtn.setBackgroundResource(R.drawable.button_background);
                else
                    pestBtn.setBackgroundResource(R.drawable.seleted_button_background);
            }
            else if(resultCode== Activity.RESULT_OK){
                return;
            }
        }
    }

}
