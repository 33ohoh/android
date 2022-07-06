package com.example.competition1.pestprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.competition1.R;
import com.example.competition1.ReportHistory;
import com.example.competition1.pestprediction.PestsOnCropDTO;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> pestsOnCropList;  //선택한 작물의 병해충 정보 리스트 데이터

    public ItemAdapter(Context mContext, ArrayList<String> pestsOnCropList) {
        this.mContext = mContext;
        this.pestsOnCropList = pestsOnCropList;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //병해충 이름 한칸에 해당하는 레이아웃을 view객체로 만들기 위한 작업으로 LayoutInflatert사용
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.pest_information_item, parent, false);
        }

        TextView pest = (TextView) convertView.findViewById(R.id.tx_pest);     //병해충 이름
        pest.setText(pestsOnCropList.get(position));                //화면에 데이터 표시

        return convertView;
    }
}
