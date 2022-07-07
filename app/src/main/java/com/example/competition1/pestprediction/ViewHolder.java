package com.example.competition1.pestprediction;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.provider.SyncStateContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;
import com.example.competition1.utility.Constants;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView cropName;              //레이아웃 요소
    private LinearLayout fullLayout;        //전체 레이아웃
    private ListView pestsOnCropListView;
    private OnViewHolderItemClickListener onViewHolderItemClickListener;
    private String cro;

    private ItemAdapter itemAdapter;
    private ArrayList<String> pestsOnCropList;  //선택한 작물의 병해충 정보 리스트 데이터
    private ArrayList<Integer> alertLevelList;  //각 작물의 경보 단계

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        cropName = itemView.findViewById(R.id.tx_crop_name);      //작물 이름

        fullLayout = itemView.findViewById(R.id.ll_pest_information);                //전체 레이아웃
        pestsOnCropListView = fullLayout.findViewById(R.id.lv_pest_information);     //해충정보를 담을 리스트뷰

        fullLayout.setOnClickListener(new View.OnClickListener() {            //작물이름 클릭시 병해충 정보를 보여줌
            @Override
            public void onClick(View view) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    private void setPestsAndAlertLevel(String alertLevel, int numberOfAlertLevel){
        if(alertLevel.equals("")) return;             //

        int index;
        String[] pests = alertLevel.split(",");

        for(index = 0; index < pests.length; index++){
            pestsOnCropList.add(pests[index]);        //병해충명 저장
            alertLevelList.add(numberOfAlertLevel);   //경보단계 저장
        }
    }

    private void setPestsOnCrop(PestsOnCropDTO pestsOnCropDTO){
        pestsOnCropList = new ArrayList<>();      //해당 작물의 병해충 리스트
        alertLevelList = new ArrayList<>();       //각 병해충의 경보 단계

        //경보단계부터 보이도록 경보 -> 주의보 -> 예보 순으로 저장
        setPestsAndAlertLevel(pestsOnCropDTO.getHighLevel(), Constants.HIGH_LEVEL);
        setPestsAndAlertLevel(pestsOnCropDTO.getMediumLevel(), Constants.MEDIUM_LEVEL);
        setPestsAndAlertLevel(pestsOnCropDTO.getLowLevel(), Constants.LOW_LEVEL);
    }

    public void onBind(PestsOnCropDTO pestsOnCropDTO, int position, SparseBooleanArray selectedItems, Context mContext){
        cropName.setText(pestsOnCropDTO.getCropName());
        cro = cropName.getText()+"";

        setPestsOnCrop(pestsOnCropDTO);                                  //해당 작물의 해충정보 세팅
        itemAdapter = new ItemAdapter(mContext, pestsOnCropList, alertLevelList);
        pestsOnCropListView.setAdapter(itemAdapter);

        changeVisibility(selectedItems.get(position));
    }

    private int getListViewHeight(ListView list) {   //리스트뷰의 높이를 구함

        ListAdapter adapter = list.getAdapter();

        int listviewHeight = 0;

        list.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        listviewHeight = list.getMeasuredHeight() * adapter.getCount() + (adapter.getCount() * list.getDividerHeight());

        return listviewHeight;
    }

    private void changeVisibility(final boolean isExpanded) {
        ValueAnimator valueAnimator;
        int list_height;

        //각 작물별 병해충 정보를 담은 리스트 뷰의 높이를 구함
        list_height = getListViewHeight(pestsOnCropListView);

        //작물별 병해충 정보를 담은 리스트 뷰의 높이가 다르기 때문에 각각의 뷰 높이를 구해 적용해줌
        valueAnimator = isExpanded ? ValueAnimator.ofInt(0, list_height) : ValueAnimator.ofInt(list_height, 0);
        valueAnimator.setDuration(500);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pestsOnCropListView.getLayoutParams().height = (int)animation.getAnimatedValue();
                pestsOnCropListView.requestLayout();
                pestsOnCropListView.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });

        valueAnimator.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}
