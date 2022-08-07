package com.PastPest.competition1.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.PastPest.competition1.pestprediction.MonthAdapter;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.PastPest.competition1.NetworkStatusActivity;
import com.PastPest.competition1.R;
import com.PastPest.competition1.pestprediction.Adapter;
import com.PastPest.competition1.pestprediction.PestsOnCropDTO;
import com.PastPest.competition1.utility.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class FragmentPredict extends Fragment {

    private View view;
    private Adapter adapter;
    private ArrayList<PestsOnCropDTO> foodResourcesList;
    private ArrayList<PestsOnCropDTO> vegetableList;
    private ArrayList<PestsOnCropDTO> fruitTreeList;
    private View foodResourcesView;
    private View vegetableView;
    private View fruitTreeView;
    private View preView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_predict, container, false);

        foodResourcesView = view.findViewById(R.id.food_resources);
        vegetableView = view.findViewById(R.id.vegetable);
        fruitTreeView = view.findViewById(R.id.fruit_tree);

        GridView gridViewMonth = view.findViewById(R.id.gridview_month);

        ArrayList<String> monthList = new ArrayList<>();
        for(int month = 1; month<=12; month++){
            monthList.add(month + "월");
        }

        MonthAdapter monthAdapter = new MonthAdapter(
                getActivity().getApplicationContext(),
                R.layout.item_month,
                monthList);

        gridViewMonth.setAdapter(monthAdapter);

        int height = getListViewHeight(gridViewMonth);

        ViewGroup.LayoutParams layoutParams = gridViewMonth.getLayoutParams();
        layoutParams.height = height;
        gridViewMonth.setLayoutParams(layoutParams);

        LinearLayout resultLayout = view.findViewById(R.id.ll_prediction_result);   //조회 결과를 표시할 레이아웃
        resultLayout.setVisibility(View.VISIBLE);

        getPestListFromServer(getCurrentMonth());    //작물별 병해충 정보를 어뎁터에 담음

        gridViewMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String month = Integer.toString(position + 1);
                Toast.makeText(getActivity().getApplicationContext(),
                        month + "월의 병해충 예보를 조회합니다", Toast.LENGTH_SHORT).show();

                Drawable selectedMonth = ContextCompat.getDrawable(
                        getActivity().getApplicationContext(), R.drawable.button_background);  //선택한 작물의의 배경
                Drawable unselectedMonth = ContextCompat.getDrawable(
                        getActivity().getApplicationContext(), R.drawable.bg_month_button);           //선택하지 않은 작물의 배경

                view.setBackground(selectedMonth);

                if(preView != null){
                    preView.setBackground(unselectedMonth);
                }
                preView = view;

                //작물별 병해충 정보를 어뎁터에 담음
                getPestListFromServer(Integer.parseInt(month));
            }
        });

        return view;
    }

    private int getListViewHeight(GridView list) {   //리스트뷰의 높이를 구함

        ListAdapter adapter = list.getAdapter();

        int listviewHeight = 0;

        list.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

        listviewHeight = list.getMeasuredHeight()*3 + 30;

        return listviewHeight;
    }

    private void getPestListFromServer(int month){

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getActivity().getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {
            try {
                requestJsonObject.put("month", month);

                RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constants.SERVER_URL + "/monthlypests/month", requestJsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());

                            if (response.getBoolean("status")) {
                                setPestList(jsonObject);   //서버에서 가져온 작물, 각 병해충 정보 데이터 저장
                            } else {   //로그인 실패

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }

    }

    private void setPestList(JSONObject jsonObject){
        foodResourcesList = new ArrayList<>();
        fruitTreeList = new ArrayList<>();
        vegetableList = new ArrayList<>();

        String cropName, cropType, lowLevel, mediumLevel, highLevel;

        try {
            JSONArray jsonArray = jsonObject.getJSONArray("pests");

            for(int index = 0; index < jsonArray.length(); index++){
                JSONObject obj = jsonArray.getJSONObject(index);

                cropType = obj.getString("crop_type");
                cropName = obj.getString("crop_name");
                lowLevel = obj.getString("low_level").equals("null") ? "" : obj.getString("low_level");
                mediumLevel = obj.getString("medium_level").equals("null") ? "" : obj.getString("medium_level");
                highLevel = obj.getString("high_level").equals("null") ? "" : obj.getString("high_level");

                switch (cropType){
                    case Constants.FOOD_RESOURCES:
                        foodResourcesList.add(new PestsOnCropDTO(
                                cropType, cropName, highLevel, mediumLevel, lowLevel));
                        break;
                    case Constants.FRUIT_TREE:
                        fruitTreeList.add(new PestsOnCropDTO(
                                cropType, cropName, highLevel, mediumLevel, lowLevel));
                        break;
                    case Constants.VEGETABLE:
                        vegetableList.add(new PestsOnCropDTO(
                                cropType, cropName, highLevel, mediumLevel, lowLevel));
                        break;
                }
            }

            setCropList();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setCropList(){

        ((TextView)foodResourcesView.findViewById(R.id.tx_crop_type)).setText(Constants.FOOD_RESOURCES);
        ((ImageView)foodResourcesView.findViewById(R.id.crop_icon)).setImageResource(R.drawable.ic_rice);
        setPestInformationView(foodResourcesView.findViewById(R.id.rv_prediction_result));           //병해충 정보를 나타낼 recyclerView
        addToAddapter(foodResourcesList);

        ((TextView)vegetableView.findViewById(R.id.tx_crop_type)).setText(Constants.VEGETABLE);
        ((ImageView)vegetableView.findViewById(R.id.crop_icon)).setImageResource(R.drawable.ic_cabbage);
        setPestInformationView(vegetableView.findViewById(R.id.rv_prediction_result));           //병해충 정보를 나타낼 recyclerView
        addToAddapter(vegetableList);

        ((TextView)fruitTreeView.findViewById(R.id.tx_crop_type)).setText(Constants.FRUIT_TREE);
        ((ImageView)fruitTreeView.findViewById(R.id.crop_icon)).setImageResource(R.drawable.ic_apple);
        setPestInformationView(fruitTreeView.findViewById(R.id.rv_prediction_result));           //병해충 정보를 나타낼 recyclerView
        addToAddapter(fruitTreeList);

        setNoResultMessage(foodResourcesList, (LinearLayout) foodResourcesView.findViewById(R.id.ll_no_result), Constants.FOOD_RESOURCES);
        setNoResultMessage(vegetableList, (LinearLayout) vegetableView.findViewById(R.id.ll_no_result), Constants.VEGETABLE);
        setNoResultMessage(fruitTreeList, (LinearLayout) fruitTreeView.findViewById(R.id.ll_no_result), Constants.FRUIT_TREE);
    }

    private void setNoResultMessage(ArrayList<PestsOnCropDTO> pestList, LinearLayout noResultMessage, String cropType){

        if(pestList.size() == 0){
            ((TextView)noResultMessage.findViewById(R.id.tx_crop_type_with_no_result)).setText(cropType);
            noResultMessage.setVisibility(View.VISIBLE);
        }
        else{
            noResultMessage.setVisibility(View.GONE);
        }
    }

    private int getCurrentMonth(){

        Date currentTime;
        SimpleDateFormat monthFormat;
        String currentMonth;
        int initialMonth;

        currentTime = Calendar.getInstance().getTime();                 //현재 월을 구함
        monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        currentMonth = monthFormat.format(currentTime);

        initialMonth = Integer.parseInt(currentMonth) - 1;

        return initialMonth;
    }

    private void setPestInformationView(RecyclerView recyclerView){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter(getActivity().getApplicationContext());  //각 작물에 대한 병해충 리스트가 보여지는 뷰);
        recyclerView.setAdapter(adapter);
    }

    private void addToAddapter(ArrayList<PestsOnCropDTO> pestList){
        int length = pestList.size();

        for(int index = 0; index < length; index++){
            adapter.addToPestsOnCropList(pestList.get(index));
        }
    }
}