package com.example.competition1.pestprediction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.competition1.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<PestsOnCropDTO> pestsOnCropList;       //선택한 달에 조심해야하는 병해충 목록
    private SparseBooleanArray selectedItems;                //작물 ListView에서 선택한 작물의 위치정보를 기억
    private int previousPosition;                            //직전에 선택한 작물의 위치정보

    private Context mContext;

    public Adapter(Context mContext){
        pestsOnCropList = new ArrayList<>();
        selectedItems = new SparseBooleanArray();
        previousPosition = -1;

        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_pest_information_list, parent, false);    //작물에 대한 병해충 정보 리스트 한칸
        return new ViewHolder(view, mContext);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ViewHolder viewHolder = (ViewHolder)holder;

        viewHolder.onBind(pestsOnCropList.get(position), position, selectedItems);    //예측결과뷰에 작물명, 해당 병충해 정보 저장


        viewHolder.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {

                if (selectedItems.get(position)) {
                    selectedItems.delete(position);          //이미 클릭한 작물 클릭 시 -> 클릭한 작물에 대해 펼쳐진 정보를 접음
                }
                else {
                    selectedItems.delete(previousPosition);  //직전에 클릭한 작물정보에 대해 펼쳐진 정보를 접음
                    selectedItems.put(position, true);       // 클릭한 작물의 위치 저장
                }

                if (previousPosition != -1) {
                    notifyItemChanged(previousPosition); //해당 위치의 변화를 알림
                }

                notifyItemChanged(position);

                previousPosition = position;             //선택한 작물 위치 저장
            }
        });
    }

    @Override
    public int getItemCount() {

        return pestsOnCropList.size();
    }

    public void addToPestsOnCropList(PestsOnCropDTO pestsOnCropDTO){

        pestsOnCropList.add(pestsOnCropDTO);
    }
}
