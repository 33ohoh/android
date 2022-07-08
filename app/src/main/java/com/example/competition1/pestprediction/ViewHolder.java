package com.example.competition1.pestprediction;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    private LinearLayout fullLayout;        //전체 레이아웃(작물이름 + 해충정보 리스트)
    private ListView pestsOnCropListView;   //작물별 해충정보를 담을 리스트뷰
    private OnViewHolderItemClickListener onViewHolderItemClickListener;

    private Context mContext;
    private ItemAdapter itemAdapter;            //작물에 대한 어뎁터
    private ArrayList<String> pestsOnCropList;  //선택한 작물의 병해충 정보 리스트 데이터
    private ArrayList<Integer> alertLevelList;  //병해충에 대한 경보 단계

    public ViewHolder(@NonNull View itemView, Context mContext) {
        super(itemView);

        this.mContext = mContext;
        cropName = itemView.findViewById(R.id.tx_crop_name);      //작물 이름

        fullLayout = itemView.findViewById(R.id.ll_pest_information);                //전체 레이아웃
        pestsOnCropListView = fullLayout.findViewById(R.id.lv_pest_information);     //해충정보를 담을 리스트뷰

        fullLayout.setOnClickListener(new View.OnClickListener() {            //작물이름 클릭시 병해충 정보를 보여줌
            @Override
            public void onClick(View view) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });

        pestsOnCropListView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });
    }

    private void setPestsAndAlertLevel(String alertLevel, int numberOfAlertLevel){
        if(alertLevel.equals("")) return;             //해당 경보단계에 병해충정보가 없을 경우 리스트에 추가하지 않음

        String[] pests = alertLevel.split(","); //해당 경보단계에 대한 병해충 정보가 ','로 묶여있음

        for(int index = 0; index < pests.length; index++){
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

    public void onBind(PestsOnCropDTO pestsOnCropDTO, int position, SparseBooleanArray selectedItems){
        cropName.setText(pestsOnCropDTO.getCropName());

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

    private void setCropNameBackground(boolean isExpanded){

        Drawable selectedCropName =
                ContextCompat.getDrawable(mContext, R.drawable.bg_selected_crop_name);  //선택한 작물의의 배경
        Drawable unselectedCropName =
                ContextCompat.getDrawable(mContext, R.drawable.bg_crop_name);           //선택하지 않은 작물의 배경

        if(isExpanded){    //선택한 작물을 표시하기 위해 배경색을 채움
            fullLayout.findViewById(R.id.ll_crop_name).setBackground(selectedCropName);
        }
        else{
            fullLayout.findViewById(R.id.ll_crop_name).setBackground(unselectedCropName);
        }
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

                setCropNameBackground(isExpanded);   //선택한 작물을 구분하기 위해 배경색을 채워줌
            }
        });

        valueAnimator.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}
