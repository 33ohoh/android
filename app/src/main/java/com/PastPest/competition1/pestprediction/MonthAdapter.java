package com.PastPest.competition1.pestprediction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.PastPest.competition1.R;

import java.util.ArrayList;

public class MonthAdapter extends ArrayAdapter<String> {

    private Context context;
    private ArrayList<String> items;

    public MonthAdapter(@NonNull Context context, int resource, ArrayList<String> items) {

        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = vi.inflate(R.layout.item_month, null);

        String month = items.get(position);
        TextView txMonth = (TextView) view.findViewById(R.id.month);
        txMonth.setText(month);

        return view;
    }
}
