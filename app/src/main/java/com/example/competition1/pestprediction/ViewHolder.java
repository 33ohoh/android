package com.example.competition1.pestprediction;

import android.animation.ValueAnimator;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;
import com.example.competition1.pestprediction.OnViewHolderItemClickListener;
import com.example.competition1.pestprediction.PestsOnCropDTO;

public class ViewHolder extends RecyclerView.ViewHolder {
    TextView cropName;              //레이아웃 요소
    TextView content;
    LinearLayout fullLayout;        //전체 레이아웃
    LinearLayout detailLayout;      //작물이름 클릭시 나올 레이아웃(해당 작물의 병해충 정보를 보여줌)
    OnViewHolderItemClickListener onViewHolderItemClickListener;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        cropName = itemView.findViewById(R.id.tx_crop_name);      //작물 이름
        content = itemView.findViewById(R.id.tx_content);        //병해충 정보

        fullLayout = itemView.findViewById(R.id.ll_pest_information);         //전체 레이아웃
        detailLayout = itemView.findViewById(R.id.ll_pest_information_list);  //작물의 병해충 정보가 담긴 레이아웃

        fullLayout.setOnClickListener(new View.OnClickListener() {            //작물이름 클릭시 병해충 정보를 보여줌
            @Override
            public void onClick(View view) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    public void onBind(PestsOnCropDTO itemData, int position, SparseBooleanArray selectedItems){
        cropName.setText(itemData.getCropName());
        content.setText(itemData.getContent());
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
