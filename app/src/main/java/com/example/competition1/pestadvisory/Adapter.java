package com.example.competition1.pestadvisory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.competition1.R;
import com.example.competition1.ReportHistory;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<PestAdvisory> pestAdvisoryList;

    public Adapter(Context mContext, ArrayList<PestAdvisory> pestAdvisoryList) {
        this.mContext = mContext;
        this.pestAdvisoryList = pestAdvisoryList;
    }

    @Override
    public int getCount() {
        return pestAdvisoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return pestAdvisoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //해충 방제정보 한칸에 해당하는 레이아웃을 view객체로 만들기 위한 작업으로 LayoutInflatert사용
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_pest_control_period, parent, false);
        }

        TextView pestName = (TextView) convertView.findViewById(R.id.tx_pest_name);             //해충이름
        TextView controlPeriod = (TextView) convertView.findViewById(R.id.tx_control_period);   //방제시기 설명

        pestName.setText(pestAdvisoryList.get(position).getPsetName());              //화면에 데이터 표시
        controlPeriod.setText(pestAdvisoryList.get(position).getControlPeriod());

        return convertView;
    }
}
