package com.PastPest.competition1.pestprediction;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.PastPest.competition1.R;
import com.PastPest.competition1.utility.Constants;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> pestsOnCropList;  //선택한 작물의 병해충 정보 리스트 데이터
    private ArrayList<Integer> alertLevelList;  //각 작물의 경보 단계

    public ItemAdapter(Context mContext, ArrayList<String> pestsOnCropList, ArrayList<Integer> alertLevelList) {
        this.mContext = mContext;
        this.pestsOnCropList = pestsOnCropList;
        this.alertLevelList = alertLevelList;
    }

    @Override
    public int getCount() {
        return pestsOnCropList.size();
    }

    @Override
    public Object getItem(int position) {
        return pestsOnCropList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void setAlertLevel(TextView alertLevelDisplay, int alertLevel){
        GradientDrawable greenCircle = (GradientDrawable)
                ContextCompat.getDrawable(mContext, R.drawable.bg_green_circle);      //경보단계-예보의 배경으로 사용한 drawable
        GradientDrawable yellowCircle = (GradientDrawable)
                ContextCompat.getDrawable(mContext, R.drawable.bg_yellow_circle);     //경보단계-주의의 배경으로 사용한 drawable
        GradientDrawable redCircle = (GradientDrawable)
                ContextCompat.getDrawable(mContext, R.drawable.bg_red_circle);        //경보단계-경보의 배경으로 사용한 drawable

        switch (alertLevel){              //경보단계에 맞게 세팅
            case Constants.LOW_LEVEL:
                alertLevelDisplay.setBackground(greenCircle);
                break;
            case Constants.MEDIUM_LEVEL:
                alertLevelDisplay.setBackground(yellowCircle);
                break;
            case Constants.HIGH_LEVEL:
                alertLevelDisplay.setBackground(redCircle);
                break;
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //병해충 이름 한칸에 해당하는 레이아웃을 view객체로 만들기 위한 작업으로 LayoutInflatert사용
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_pest_information, parent, false);
        }

        TextView pest = (TextView) convertView.findViewById(R.id.tx_pest);     //병해충 이름
        pest.setText(pestsOnCropList.get(position));                //화면에 데이터 표시

        TextView alertLevelDisplay = (TextView) convertView.findViewById(R.id.tx_alert_level);  //경보단계를 보여주는 동그라미
        int alertLevel = alertLevelList.get(position);

        setAlertLevel(alertLevelDisplay, alertLevel);

        return convertView;
    }
}
