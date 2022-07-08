package com.example.competition1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CurrentSituationAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<CurrentSituation>currentHistoryList;  //신고내역 리스트 데이터

    public CurrentSituationAdapter(Context mContext, ArrayList<CurrentSituation> currentHistoryList) {
        this.mContext = mContext;
        this.currentHistoryList = currentHistoryList;
    }

    @Override
    public int getCount() {
        return currentHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return currentHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_current_situation_list, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.current_crop_image);       // 작물 사진
        TextView name = (TextView) convertView.findViewById(R.id.current_crop_name);         // 작물 이름
        TextView description = (TextView) convertView.findViewById(R.id.current_crop_description);     //본문 설명

        String url = currentHistoryList.get(position).getImageUrl(); //이미지 url로 넣기
        Glide.with(mContext).load(url).into(imageView);
        name.setText(currentHistoryList.get(position).getCropName());    //이름 넣기
        description.setText(currentHistoryList.get(position).getDescription());   //설명 넣기

        return convertView;
    }
}
