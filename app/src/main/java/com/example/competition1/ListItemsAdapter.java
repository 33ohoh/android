package com.example.competition1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListItemsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ReportHistory> reportHistoryList;

    public ListItemsAdapter(Context mContext, ArrayList<ReportHistory> reportHistoryList) {
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
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_item_list, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView status = (TextView) convertView.findViewById(R.id.status);

        title.setText(reportHistoryList.get(position).getTitle());
        date.setText(reportHistoryList.get(position).getDate());
        status.setText(reportHistoryList.get(position).getStatus());

        return convertView;
    }
}
