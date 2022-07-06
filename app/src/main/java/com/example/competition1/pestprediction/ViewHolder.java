package com.example.competition1.pestprediction;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;

import java.util.ArrayList;

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView cropName;              //레이아웃 요소
    private LinearLayout fullLayout;        //전체 레이아웃
    private LinearLayout detailLayout;      //작물이름 클릭시 나올 레이아웃(해당 작물의 병해충 정보를 보여줌)
    private OnViewHolderItemClickListener onViewHolderItemClickListener;

    private ItemAdapter itemAdapter;
    private ArrayList<String> pestsOnCropList;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        cropName = itemView.findViewById(R.id.tx_crop_name);      //작물 이름

        fullLayout = itemView.findViewById(R.id.ll_pest_information);         //전체 레이아웃
        detailLayout = itemView.findViewById(R.id.ll_pest_information_list);  //작물의 병해충 정보가 담긴 레이아웃

        fullLayout.setOnClickListener(new View.OnClickListener() {            //작물이름 클릭시 병해충 정보를 보여줌
            @Override
            public void onClick(View view) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    private void setPestsOnCrop(String pestsOnCrop, int position){
        pestsOnCropList = new ArrayList<>();              //해당 작물의 병해충 리스트
        String[] pests = pestsOnCrop.split(",");

        for(int index = 0; index < pests.length; index++){
            pestsOnCropList.add(pests[index]);
            Log.v("test", pests[index]);
        }
    }

    public void onBind(PestsOnCropDTO pestsOnCropDTO, int position, SparseBooleanArray selectedItems, Context mContext){
        cropName.setText(pestsOnCropDTO.getCropName());

        ListView pestsOnCropListView =
                detailLayout.findViewById(R.id.lv_pest_information);  //해충정보를 담을 리스트뷰

        setPestsOnCrop(pestsOnCropDTO.getPests(), position);      //해당 작물의 해충정보 세팅
        itemAdapter = new ItemAdapter(mContext, pestsOnCropList);
        pestsOnCropListView.setAdapter(itemAdapter);

        changeVisibility(selectedItems.get(position));
    }

    private void changeVisibility(final boolean isExpanded) {
        ValueAnimator valueAnimator;

        valueAnimator = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);
        valueAnimator.setDuration(500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                detailLayout.getLayoutParams().height = (int) animation.getAnimatedValue();  //병해충 정보 높이
                detailLayout.requestLayout();
                detailLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);           //
            }
        });

        valueAnimator.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}
