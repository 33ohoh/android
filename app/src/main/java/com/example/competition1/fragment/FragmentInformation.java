package com.example.competition1.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;

import com.example.competition1.R;
import com.example.competition1.information.SectorSelectionActivity;
import com.example.competition1.report.CropData;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;


public class FragmentInformation extends Fragment {

    String selectedCrop="";
    ArrayList<CropData> datas =new ArrayList<CropData>();
    GridLayout gridLayout;
    LinearLayout emptyLayout1;
    LinearLayout emptyLayout2;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view =  inflater.inflate(R.layout.fragment_information, container, false);

        AppCompatButton selectButton=(AppCompatButton) view.findViewById(R.id.cropSaveButton);
        selectButton.setText("병충해 조회");
        TextView thirdText=(TextView) view.findViewById(R.id.third_Text);
        SpannableString text=new SpannableString("병충해 조회 버튼을 눌러주세요.");
        text.setSpan(new ForegroundColorSpan(Color.parseColor("#04CF5C")),0,3, Spanned.SPAN_INTERMEDIATE);
        thirdText.setText(text);
        gridLayout=(GridLayout)view.findViewById(R.id.cropGrid);
        emptyLayout1=(LinearLayout)view.findViewById(R.id.emptylayout1);
        emptyLayout2=(LinearLayout)view.findViewById(R.id.emptylayout2);
        loadViews();
        for(int i=0;i<20;i++){
            datas.get(i).button.setOnClickListener(new cropInformationListener(datas.get(i)));
        }

        AppCompatButton saveButton=(AppCompatButton) view.findViewById(R.id.cropSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedCrop.equals(""))
                    Toast.makeText(getActivity().getApplicationContext(), "임산물이 선택되지 않았습니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SectorSelectionActivity.class);
                    intent.putExtra("cropName", selectedCrop);
                    startActivity(intent);
                }
            }
        });
        TextInputEditText searchText=(TextInputEditText) view.findViewById(R.id.crop_search);
        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = searchText.getText().toString();
                search(text);
            }
        });
        return view;

    }
    private void search(String text){
        gridLayout.removeAllViews();
        if (text.length() == 0) {
            for(int i = 0;i < datas.size(); i++)
                gridLayout.addView(datas.get(i).layout);
        }
        else
        {
            int count=0;
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < datas.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (datas.get(i).textView.getText().toString().contains(text))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    gridLayout.addView(datas.get(i).layout);
                    count++;
                }
            }
            if(count==2)
                gridLayout.addView(emptyLayout1);
            else if(count==1){
                gridLayout.addView(emptyLayout1);
                gridLayout.addView(emptyLayout2);
            }
        }
    }

    private void loadViews(){
        for(int i=0;i<20;i++)
            datas.add(new CropData());
        datas.get(0).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button1);
        datas.get(0).textView=(TextView) view.findViewById(R.id.text1);
        datas.get(1).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button2);
        datas.get(1).textView=(TextView) view.findViewById(R.id.text2);
        datas.get(2).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button3);
        datas.get(2).textView=(TextView) view.findViewById(R.id.text3);
        datas.get(3).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button4);
        datas.get(3).textView=(TextView) view.findViewById(R.id.text4);
        datas.get(4).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button5);
        datas.get(4).textView=(TextView) view.findViewById(R.id.text5);
        datas.get(5).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button6);
        datas.get(5).textView=(TextView) view.findViewById(R.id.text6);
        datas.get(6).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button7);
        datas.get(6).textView=(TextView) view.findViewById(R.id.text7);
        datas.get(7).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button8);
        datas.get(7).textView=(TextView) view.findViewById(R.id.text8);
        datas.get(8).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button9);
        datas.get(8).textView=(TextView) view.findViewById(R.id.text9);
        datas.get(9).button=(com.google.android.material.card.MaterialCardView)view.findViewById(R.id.button10);
        datas.get(9).textView=(TextView) view.findViewById(R.id.text10);
        datas.get(10).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button11);
        datas.get(10).textView=(TextView) view.findViewById(R.id.text11);
        datas.get(11).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button12);
        datas.get(11).textView=(TextView) view.findViewById(R.id.text12);
        datas.get(12).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button13);
        datas.get(12).textView=(TextView) view.findViewById(R.id.text13);
        datas.get(13).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button14);
        datas.get(13).textView=(TextView) view.findViewById(R.id.text14);
        datas.get(14).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button15);
        datas.get(14).textView=(TextView) view.findViewById(R.id.text15);
        datas.get(15).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button16);
        datas.get(15).textView=(TextView) view.findViewById(R.id.text16);
        datas.get(16).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button17);
        datas.get(16).textView=(TextView) view.findViewById(R.id.text17);
        datas.get(17).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button18);
        datas.get(17).textView=(TextView) view.findViewById(R.id.text18);
        datas.get(18).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button19);
        datas.get(18).textView=(TextView) view.findViewById(R.id.text19);
        datas.get(19).button=(com.google.android.material.card.MaterialCardView) view.findViewById(R.id.button20);
        datas.get(19).textView=(TextView) view.findViewById(R.id.text20);
        datas.get(0).layout=(LinearLayout)view.findViewById(R.id.layout1);
        datas.get(1).layout=(LinearLayout)view.findViewById(R.id.layout2);
        datas.get(2).layout=(LinearLayout)view.findViewById(R.id.layout3);
        datas.get(3).layout=(LinearLayout)view.findViewById(R.id.layout4);
        datas.get(4).layout=(LinearLayout)view.findViewById(R.id.layout5);
        datas.get(5).layout=(LinearLayout)view.findViewById(R.id.layout6);
        datas.get(6).layout=(LinearLayout)view.findViewById(R.id.layout7);
        datas.get(7).layout=(LinearLayout)view.findViewById(R.id.layout8);
        datas.get(8).layout=(LinearLayout)view.findViewById(R.id.layout9);
        datas.get(9).layout=(LinearLayout)view.findViewById(R.id.layout10);
        datas.get(10).layout=(LinearLayout)view.findViewById(R.id.layout11);
        datas.get(11).layout=(LinearLayout)view.findViewById(R.id.layout12);
        datas.get(12).layout=(LinearLayout)view.findViewById(R.id.layout13);
        datas.get(13).layout=(LinearLayout)view.findViewById(R.id.layout14);
        datas.get(14).layout=(LinearLayout)view.findViewById(R.id.layout15);
        datas.get(15).layout=(LinearLayout)view.findViewById(R.id.layout16);
        datas.get(16).layout=(LinearLayout)view.findViewById(R.id.layout17);
        datas.get(17).layout=(LinearLayout)view.findViewById(R.id.layout18);
        datas.get(18).layout=(LinearLayout)view.findViewById(R.id.layout19);
        datas.get(19).layout=(LinearLayout)view.findViewById(R.id.layout20);

    }

    public class cropInformationListener implements View.OnClickListener {
        CropData data;

        public cropInformationListener(CropData data) {
            this.data=data;
        }

        @Override
        public void onClick(View view) {
            if(data.selected==false) {
                for(int i=0;i<datas.size();i++){
                    if(datas.get(i).selected==true){
                        datas.get(i).button.setStrokeColor(Color.parseColor("#ffffff"));
                        datas.get(i).selected=false;
                    }
                }
                data.selected=true;
                data.button.setStrokeColor(Color.parseColor("#04CF5C"));
                selectedCrop =data.textView.getText().toString();
            }
            else {
                data.selected=false;
                data.button.setStrokeColor(Color.parseColor("#ffffff"));
                selectedCrop ="";
            }
        }
    }
}