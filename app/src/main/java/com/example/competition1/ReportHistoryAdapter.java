package com.example.competition1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ReportHistoryAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ReportHistory> reportHistoryList;  //신고내역 리스트 데이터

    public ReportHistoryAdapter(Context mContext, ArrayList<ReportHistory> reportHistoryList) {
        this.mContext = mContext;
        this.reportHistoryList = reportHistoryList;
    }

    @Override
    public int getCount() {
        return reportHistoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return reportHistoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //신고내역 한칸에 해당하는 레이아웃(activity_report_history_list.xml)을 view객체로 만들기 위한 작업으로 LayoutInflatert사용
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_report_history, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);       //피해신고 제목
        TextView date = (TextView) convertView.findViewById(R.id.date);         //신고 날짜
        TextView status = (TextView) convertView.findViewById(R.id.status);     //답변 상태

        title.setText(reportHistoryList.get(position).getTitle());              //화면에 데이터 표시
        date.setText(reportHistoryList.get(position).getDate());
        status.setText(reportHistoryList.get(position).getStatus());

        return convertView;
    }
}
