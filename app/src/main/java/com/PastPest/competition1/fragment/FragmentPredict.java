package com.PastPest.competition1.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_predict, container, false);

        foodResourcesView = view.findViewById(R.id.food_resources);
        vegetableView = view.findViewById(R.id.vegetable);
        fruitTreeView = view.findViewById(R.id.fruit_tree);

        setMonthSpinner();                  //?????? ???????????? spinner???

        LinearLayout resultLayout = view.findViewById(R.id.ll_prediction_result);   //?????? ????????? ????????? ????????????
        resultLayout.setVisibility(View.VISIBLE);

        getPestListFromServer(getCurrentMonth());    //????????? ????????? ????????? ???????????? ??????

        Button btnInquiry = view.findViewById(R.id.btn_prediction_result_inquiry);  //?????? ?????? ??????
        btnInquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPestListFromServer(Integer.parseInt(getMonth()));//????????? ????????? ????????? ???????????? ??????
            }
        });
        return view;
    }

    private void getPestListFromServer(int month){

        JSONObject requestJsonObject = new JSONObject();

        //????????? ????????????
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
                                setPestList(jsonObject);   //???????????? ????????? ??????, ??? ????????? ?????? ????????? ??????
                            } else {   //????????? ??????

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //????????? ????????? ?????? ??? ?????? ????????? ????????? ?????? ?????? ????????? ???????????????.
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
            Toast.makeText(getActivity().getApplicationContext(), "????????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show();
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
                        foodResourcesList.add(new PestsOnCropDTO(cropType, cropName, highLevel, mediumLevel, lowLevel));
                        break;
                    case Constants.FRUIT_TREE:
                        fruitTreeList.add(new PestsOnCropDTO(cropType, cropName, highLevel, mediumLevel, lowLevel));
                        break;
                    case Constants.VEGETABLE:
                        vegetableList.add(new PestsOnCropDTO(cropType, cropName, highLevel, mediumLevel, lowLevel));
                        break;
                }
            }

            setCropList();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String getMonth(){
        Spinner month = (Spinner)view.findViewById(R.id.spn_month);
        return month.getSelectedItem().toString();
    }

    private void setCropList(){

        ((TextView)foodResourcesView.findViewById(R.id.tx_crop_type)).setText(Constants.FOOD_RESOURCES);
        ((ImageView)foodResourcesView.findViewById(R.id.crop_icon)).setImageResource(R.drawable.ic_rice);
        setPestInformationView(foodResourcesView.findViewById(R.id.rv_prediction_result));           //????????? ????????? ????????? recyclerView
        addToAddapter(foodResourcesList);

        ((TextView)vegetableView.findViewById(R.id.tx_crop_type)).setText(Constants.VEGETABLE);
        ((ImageView)vegetableView.findViewById(R.id.crop_icon)).setImageResource(R.drawable.ic_cabbage);
        setPestInformationView(vegetableView.findViewById(R.id.rv_prediction_result));           //????????? ????????? ????????? recyclerView
        addToAddapter(vegetableList);

        ((TextView)fruitTreeView.findViewById(R.id.tx_crop_type)).setText(Constants.FRUIT_TREE);
        ((ImageView)fruitTreeView.findViewById(R.id.crop_icon)).setImageResource(R.drawable.ic_apple);
        setPestInformationView(fruitTreeView.findViewById(R.id.rv_prediction_result));           //????????? ????????? ????????? recyclerView
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

        currentTime = Calendar.getInstance().getTime();                 //?????? ?????? ??????
        monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
        currentMonth = monthFormat.format(currentTime);

        initialMonth = Integer.parseInt(currentMonth) - 1;

        return initialMonth;
    }

    private void setMonthSpinner(){
        String[] month;
        Spinner spnMonth;
        int currentMonth;

        month = getResources().getStringArray(R.array.month);   //??????????????? ????????? ??? ??????(1,2,...,12)
        spnMonth = (Spinner)view.findViewById(R.id.spn_month);       //?????? ????????? ?????????

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_dropdown_item, month);

        currentMonth = getCurrentMonth();
        spnMonth.setAdapter(adapter);
        spnMonth.setSelection(currentMonth);   //???????????? ?????? ?????? ??????
    }

    private void setPestInformationView(RecyclerView recyclerView){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new Adapter(getActivity().getApplicationContext());  //??? ????????? ?????? ????????? ???????????? ???????????? ???);
        recyclerView.setAdapter(adapter);
    }

    private void addToAddapter(ArrayList<PestsOnCropDTO> pestList){
        int length = pestList.size();

        for(int index = 0; index < length; index++){
            adapter.addToPestsOnCropList(pestList.get(index));
        }
    }
}