package com.PastPest.competition1.pestprediction;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.PastPest.competition1.API.PestAPITask;
import com.PastPest.competition1.R;
import com.PastPest.competition1.pestdetails.PestDetails;
import com.PastPest.competition1.pestdetails.PestDetailsActivity;
import com.PastPest.competition1.utility.Constants;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView cropName;              //레이아웃 요소
    private LinearLayout fullLayout;        //전체 레이아웃(작물이름 + 해충정보 리스트)
    private ListView pestsOnCropListView;   //작물별 해충정보를 담을 리스트뷰
    private OnViewHolderItemClickListener onViewHolderItemClickListener;

    private Context mContext;
    private ItemAdapter itemAdapter;            //작물에 대한 어뎁터
    private ArrayList<String> pestsOnCropList;  //선택한 작물의 병해충 정보 리스트 데이터
    private ArrayList<Integer> alertLevelList;  //병해충에 대한 경보 단계

    private String pestName;
    private String sickKey;
    private String tag;

    private String getSymptomName(Element element){
        NodeList nodeList=element.getElementsByTagName("sickNameKor").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }

    private String getPestName(Element element){
        NodeList nodeList=element.getElementsByTagName("insectKorName").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private String getPestImage(Element element){
        NodeList nodeList=element.getElementsByTagName("thumbImg").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
    private String getPestKey(Element element){
        NodeList nodeList=element.getElementsByTagName("insectKey").item(0).getChildNodes();
        Node node=(Node) nodeList.item(0);
        return node.getNodeValue();
    }
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

        pestsOnCropListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                pestName = pestsOnCropList.get(position);
                String url = String.format(getUrlForCode(),cropName.getText(), pestName);

                try{
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .get()
                            .build();
                    final Call call = okHttpClient.newCall(request);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Response response = call.execute();
                                String responseString = response.body().string();
                                sickKey = getValue(responseString, tag);
                                intentDetailPage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    private String getUrlForCode(){
        String url;

        if(pestName.indexOf("병") != -1 || pestName.indexOf("바이러스") != -1){
            url = "http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC01&serviceType=[AA001:XML,AA002:Ajax]&cropName=%s&sickNameKor=%s";
            tag = "sickKey";
        }
        else{
            url = "http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC03&serviceType=[AA001:XML,AA002:Ajax]&cropName=%s&insectKorName=%s";
            tag = "insectKey";
        }

        return url;
    }


    private String getValue(String xml, String tag){
        tag = String.format("<%s>", tag);
        int beginIndex = xml.indexOf(tag) + tag.length();
        int endIndex = xml.indexOf("<", beginIndex);
        return xml.substring(beginIndex, endIndex);
    }

    private String removeHmlTage(String content){
        content = content.replaceAll("&lt;br/&gt;&#xD;", "\n\n");
        content = content.replaceAll("&lt;br/&gt;", "\n\n");
        content = content.replaceAll("&#xD;", "\n\n");

        return content;
    }

    private String getUrlForDetails(){
        String url;

        if(pestName.indexOf("병") != -1 || pestName.indexOf("바이러스") != -1){
            url = String.format("http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC05&sickKey=%s", sickKey);
        }
        else{
            url = String.format("http://ncpms.rda.go.kr/npmsAPI/service?apiKey=2022c62d6340fb3f2718baa5efac9a3c8575&serviceCode=SVC07&insectKey=%s", sickKey);
        }

        return url;
    }

    private String getImageUrlForDisease(String cropName, String pestName){
        PestAPITask task=new PestAPITask();
        NodeList pestList;
        try {
            pestList=task.execute(cropName, "symptom").get();
            for(int i=0 ;i<pestList.getLength(); i++){
                if(getSymptomName((Element) pestList.item(i)).equals(pestName)){
                    return getPestImage((Element) pestList.item(i));
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getImageUrlForPest(String cropName, String pestName){
        PestAPITask task=new PestAPITask();
        NodeList pestList;
        try {
            pestList=task.execute(cropName, "pest").get();
            for(int i=0 ;i<pestList.getLength(); i++){
                if(getPestName((Element) pestList.item(i)).equals(pestName)){
                    return getPestImage((Element) pestList.item(i));
                }
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    private PestDetails getPestDetails(String responseString){
        String image, pest, cropName, symptom, controlMethod;
        PestDetails pestDetails;

        if(pestName.indexOf("병") != -1 || pestName.indexOf("바이러스") != -1){
            pest = removeHmlTage(getValue(responseString, "sickNameKor"));
            cropName = removeHmlTage(getValue(responseString, "cropName"));
            image = getImageUrlForDisease(cropName, pest);
            symptom = removeHmlTage(getValue(responseString, "symptoms"));

            controlMethod = removeHmlTage(getValue(responseString, "preventionMethod"));
            controlMethod += removeHmlTage(getValue(responseString, "biologyPrvnbeMth"));
            controlMethod += removeHmlTage(getValue(responseString, "chemicalPrvnbeMth"));

            pestDetails = new PestDetails(pest, cropName, image, symptom, controlMethod);
        }
        else{
            pest = removeHmlTage(getValue(responseString, "insectSpeciesKor"));
            cropName = removeHmlTage(getValue(responseString, "cropName"));
            image = getImageUrlForPest(cropName, pest);
            symptom = removeHmlTage(getValue(responseString, "damageInfo"));

            if(symptom.equals("")){
                symptom = removeHmlTage(getValue(responseString, "ecologyInfo"));
            }

            controlMethod = removeHmlTage(getValue(responseString, "preventMethod"));
            controlMethod += removeHmlTage(getValue(responseString, "biologyPrvnbeMth"));
            controlMethod += removeHmlTage(getValue(responseString, "chemicalPrvnbeMth"));

            pestDetails = new PestDetails(pest, cropName, image, symptom, controlMethod);
        }

        return pestDetails;
    }

    private void intentDetailPage(){
        String url = getUrlForDetails();

        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            final Call call = okHttpClient.newCall(request);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = call.execute();
                        String responseString = response.body().string();

                        Intent intent = new Intent(mContext, PestDetailsActivity.class);
                        intent.putExtra("details", getPestDetails(responseString));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }catch(Exception e){
            e.printStackTrace();
        }
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
