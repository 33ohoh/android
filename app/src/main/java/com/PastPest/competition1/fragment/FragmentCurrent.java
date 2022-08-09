package com.PastPest.competition1.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.PastPest.competition1.FirstActivity;
import com.PastPest.competition1.MainActivity;
import com.PastPest.competition1.utility.Constants;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.PastPest.competition1.CurrentSituationAdapter;
import com.PastPest.competition1.NetworkStatusActivity;
import com.PastPest.competition1.R;
import com.PastPest.competition1.ReportHistory.ReportHistory;
import com.PastPest.competition1.ReportRecordActivity;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class FragmentCurrent extends Fragment implements View.OnClickListener {


    private ScrollView scrollView = null;
    private MapView mapView;
    private ListView currentHistoryView;
    private CurrentSituationAdapter currentHistoryAdapter;
    private ArrayList<ReportHistory> currentHistoryList;
    private ArrayList<ReportHistory> searchHistoryList;
    private View view;
    private EditText cropSearch;
    private ArrayList<ReportHistory> mapdataList;
    private ArrayList<ReportHistory> secondCurrentHistoryList;
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_current, container, false);

        //카카오맵
        mapView = new MapView(getActivity());
        ViewGroup mapViewContainer = (ViewGroup) view.findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        com.PastPest.competition1.kakaoMap.GpsTracker gpsTracker;
        double latitude;
        double longitude;

        //현재위치 이동
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
        gpsTracker = new com.PastPest.competition1.kakaoMap.GpsTracker(getActivity().getApplicationContext());

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);



        scrollView = view.findViewById(R.id.current_scrollview);
        Button mapButton = view.findViewById(R.id.current_show_map);
        Button tableButton = view.findViewById(R.id.current_show_table);
        cropSearch = view.findViewById(R.id.current_crop_search); // 검색창

        //버튼에 리스너 달기
        mapButton.setOnClickListener(this);
        tableButton.setOnClickListener(this);

        //데이터들 파싱해서 리스트에 넣기
        setMapdataList();


        // 마커 클릭 리스너
        mapView.setPOIItemEventListener(new MapView.POIItemEventListener(){

            @Override
            public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {}

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {}

            @Override
            public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

                Toast.makeText(getActivity(),"이것은 Toast 메시지입니다.", Toast.LENGTH_SHORT).show();
                // ReportHistory reportHistory = new ReportHistory(obj.getTitle(),obj.getDate(),obj.getAddress(),obj.getProductName(),obj.getSymptom(),obj.getPestName(),"",obj.getDetails());
                //Intent intent = new Intent(getActivity().getApplicationContext(), ReportRecordActivity.class);
                //intent.putExtra("reportHistory", reportHistory);
                //startActivity(intent);
            }

            @Override
            public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

            }
        });


        //검색 해당 테이블만 보여주기
        cropSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String text = cropSearch.getText().toString();
                search(text);
            }
        });



        return view;
    }


    private void setMarker(){


        for(int index = 0 ; index < mapdataList.size() ; index++){

            ReportHistory obj = mapdataList.get(index);

            if(obj.getIsSolved().equals("1"))
                continue;

            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(obj.getLatitude(), obj.getLongitude());

            switch (obj.getCropName()){

                case "감":
                    marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
                    break;

                case "당귀":
                    marker.setMarkerType(MapPOIItem.MarkerType.YellowPin); // 기본으로 제공하는 BluePin 마커 모양.
                    break;

                case "감초":
                    marker.setMarkerType(MapPOIItem.MarkerType.RedPin); // 기본으로 제공하는 BluePin 마커 모양.
                    break;
            }

            marker.setItemName(obj.getCropName());
            marker.setTag(1);
            marker.setMapPoint(mapPoint);

            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양
            marker.setCustomImageAutoscale(false); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌.
            marker.setCustomImageAnchor(0.5f, 1.0f); // 마커 이미지중 기준이 되는 위치(앵커포인트) 지정 - 마커 이미지 좌측 상단 기준 x(0.0f ~ 1.0f), y(0.0f ~ 1.0f) 값.

            mapView.addPOIItem(marker);
        }


    }



    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.


        currentHistoryList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            currentHistoryList.addAll(secondCurrentHistoryList);
        }
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < secondCurrentHistoryList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (secondCurrentHistoryList.get(i).getCropName().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    currentHistoryList.add(secondCurrentHistoryList.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        currentHistoryAdapter.notifyDataSetChanged();
    }

    private void persingData(JSONObject jsonObject){

        try {

            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for(int index = 0 ; index < jsonArray.length() ; index++){
                JSONObject obj = jsonArray.getJSONObject(index);

                String productName = obj.getString("product_name");
                String details= obj.getString("details");
                Double latitude= obj.getDouble("latitude");
                Double longitude= obj.getDouble("longitude");
                String id = obj.getString("id");
                String address = obj.getString("street_address");
                String title = obj.getString("title");
                String date = obj.getString("date");
                String symptom = obj.getString("symptom");
                String pestName = obj.getString("pest_name");
                String isSolved = obj.getString("whether_to_solve");
                String imageUrl =obj.getString("image_url");

                mapdataList.add(new ReportHistory(imageUrl,productName,details,latitude,longitude,id,address,title,date,symptom,pestName,isSolved));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setMapdataList(){

        mapdataList= new ArrayList<>();

        JSONObject requestJsonObject = new JSONObject();

        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getActivity().getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Constants.SERVER_URL + "/declarations/data", requestJsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (response.getBoolean("status")) {
                            persingData(response);
                            //마커 찍기
                            setMarker();
                            setCurrentHistoryList();
                        }
                        else{
                            Toast.makeText(getActivity().getApplicationContext(), "맵데이터 서버오류", Toast.LENGTH_SHORT).show();
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

        } else {
            Toast.makeText(getActivity().getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
        }


    }
    private void setCurrentHistoryList() {

        currentHistoryView = (ListView) view.findViewById(R.id.current_listview);  //신고 내역 리스트가 보여지는 뷰
        currentHistoryList = new ArrayList<>();                                  //신고 내역 리스트
        searchHistoryList = new ArrayList<>();

        for(int index = 0 ; index < mapdataList.size() ; index++){

            ReportHistory obj = mapdataList.get(index);

            if(obj.getIsSolved().equals("1"))
                continue;
            searchHistoryList.add(new ReportHistory(obj.getImageUrl(),obj.getCropName(), obj.getDetails(),obj.getId(),obj.getAddress(),obj.getTitle(),obj.getDate(),obj.getSymptom(),obj.getPestName()));
            currentHistoryList.add(new ReportHistory("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAABRFBMVEX///8FOGPkAi5XVlZOTU1TUlK0s7NRUFDu7u6CgYEANWHy8vJ3dnZKSUljYmLkACzh4eFra2vR0NBDQkIAMl9eXV3Nzc3Hx8frACzjACb4+PgALl28vLwAPWjjACKJiIiWlZV6enoAKlvjABvo7vIAJliosr/+9/n84OYrT3TxACriABH97/J6kqj72N+kpKTO2OC3w89qg5tbK1Wbqrq/EjoaRW3F0drlETjxhZjVCTP2tcBddpHtaX70orD3tMH4w8xGZ4bqU2joPVSxwc+Jn7M+W3yeq7twiqI7M11OLlh2IkuGHEeVF0KjEj2wDjqRGUNSJ1M/MlwAF1LJCzVkKFLUAC/tXHfqPmDoIkzweY3xkJ30prPmI0ElM156EUONT2uzVW+sZ4L2AB/PorNVGEpEGU0tHFDJj6NDRW3oRFkwLy/r8uthAAAPB0lEQVR4nO2d6UPayhrGDYJokD0oJIAKQQVUQCm4QAsq1GOtntatavUuvecuvf//95uZJDBJJoFAMqHn5vdJliTzMO+8y8wkzs25uLi4uLi4uLi4uLi4uJAhwFdrtfNNifNas8qzTrfJOvjz3n63X2xkaE6EoTONYr+1X6/xTrdtavjIeatRKOQEUTRNIQgvGS5XKBT3z/mA062cGLb5sdXIMQplamgu12jVq043dSL4erfBMUbqZJEMU2zVnW6uWQLVFlcYR54sssBcVH8h18OetyhubHmyudK7TacbPi7NFmU0+DIK0I5stH6JAVntG5nn22+X95+uPl9f37y7ubn+/er+4PIN6cfC7GuM9DI6+jLU28HV9btjT7KEkAx77m6uP13SlNibXKY+2zGyVsTbZybDfLoRxAmEPUrC8L27z19oaLI03Z3h4Rhp5fAd+PXTHewufcLC59f30GCZ291Z7cZmEedAM9TB9UMpaaBuIDJ5d/UVmuqMdmO9genATObgncew91CSyYdrRrBVpnHutBotgW4BNwLv736M0X0IpdK10I907mLWLLXazeHG303SnD5orA9XbxmKa82WxGpfa6EZ6upwbPtUaEzefclQue4sSWzeYobg5UNpEn2A5I/PbxRXnB1/U2tghuCVR2Og4VQqC0mlwiPEl+7uM0xjVhKcGs6J3qAGCqTly/nDb6ft56Oj5/bjtydPuZwHSnW7MfmJYvqzIbF5q+3Br8clRF728Ozx6MPeluKwnb3vz6dnnqyuyNLN22z0YrWoEZg5OB5YaDjrOX3pbOOP3do+eTkN53U0lu7emKLz7obXetHM13BSllf2vOztGJ5gZ+/otZzFikze/eZ80GAxmdq95EbCqcP2yVhnOWm/YjUmPQfcvrOlP7urCfSZgwexB1Oe5/H0AYSOzGIlXnI9G9s/mk1NB2YYMUqkyk8q12LM1t4zzlaTx5eck2GxSWu8zG/QyYSzPz+YPtte+zCllfhwWXTOobJFbSC8gQLDbR3vacjWyZPWVJPHDg7FXkFjo1cgDqbC380YKMJ2O6/pxtINt2ltu8emllHbaObyBxB4NkkHSnRetRKvC86EjEBLEyje7gQbTT1OIVAYjacaicm/7FvVaFN81EbC3wWB+UfjCD+SnXZZ5VPDybeaNW02Ba+t6b8IoT71OP2pjzwqicnjvgN2ion170qeVHvKHoS8P1RL/Ex+6UabcGe+ljzZb1YIFCSqDDUcvrTkvCZgd7Wj8F0yfGiNQEGiylBLD6STt6q2rD8QGjV+IjoKtcQffyU8EjFd+DmZfZ4w0ON4Livt9Jhs2Gc12Qz19SH70yobhTwqM7js34h2Yl3rSD+VyuaTbSO2laE/+bBi6emNYVvawv6m9GTxVTrKmJH/u8XnN0KbkVJ0MtWx+jLf84pOzBM0057Gzwg596mloxByqhiKP/5h+QX0wM0+Xf14b/2F9hR2mm1bfwUdmphVmJunPRuu9B1VGH7t2HAJLJhgSB/b8gPvKPxp+Lsd18ChnQOm3lIvtlzqBI37lmYURvC3WiO9LE9V9urzDenE1JNNF1Fzrk1oqC8emy7WQUdiyo6xjgEzDKmDbzZdbAdN3vI2+GsMbBezmHZvmyc/QSJG6tSuqyio9jHroX/Y42jmlO40fGjXVRQ0cSu+f9jnyF+Q3K1sfd6EYRO3q+Sf1tW+anZQhR3bLoNQx+18+tekOSPPV5tV4437SMAg4mpwMzQUVZhIYbUONvEL9Fu757qVA1JiZI8mb/jY8NraUID7t/kT1TOFHAM28cON+4VCS+fmhO3XgTclknzzuGBBMWbLU75X5FQDmuG6NZy17jwOzNSK+ebRTcMFC4r+j7mz1LpqfVBjZh/XjS+DoJ86JZCZ8rhgIQxEMwU4u6+zU5rmCphdicOgn7J2sgsPr53BAORMTLvz+5jMVtZI9TSWunc2VEgg947gFTKtsc8QwPqqgUTmQn3AMK2ZamlyXKr4Gw3ozLgLYLgNKgoKu+pD2vJADJ8RqC50FFLjrrazF7idqEo+qo45mgmFNDXerhBs0qc6lXo72yDmE1GoMw6Bqx/n8Kre4YpTNZQH7ZVJjkMdXyqQG2MBjMVsJsZQUNrpdpmkL9WJh9R4zqY3ehDCTlSuam/lBwpJxEPMTJsskR4lUTtZrgOnDK+DaEEkp8HmpXIvGm/Q6ml3iel1onIfu7xcSiQvNYzXTNGoF8fuQXAmxW8lp21Eagt8fTjoxcKF3o3L+Fsy9OAUmY1cPxGpD/E1/pACvgTiNxumbiuli+jRch+SmU4cFbKxt4LWu2MPQfmXQn8neRySmafB3l2h+PmZQrFXlW+zZwN8bZ8ycduzrBBNkeQN/WTm2rDzpWqROaq/3/u4ublZ320Vc7hqd6RCpFCU4yGh+dKAQbhARTLgQQoMeEaEeXmUsuDckXKalF1rByoMnallcEgNJeeledtm1pVsmvH6lig8kazU+s0QeHj9OQibFEoT+2FS64dzo5ypJaCVyrNYARNbA567IDEQc8g+NmnCNEVsHb9GYiAi8XD7J1RIcC8GZj+NDQqHxUVHTEuJlE4SZoqECaGRiQxpVw2pWAHA7GuzGm54A8KWNKtv13YPHNilfGvJDYfhjriASGgRX0K7v9Ri0ImaPbGyyHZIKgzYHfTRuw7f58WclEhdMcDm3JRG78h7SpENhiLVhq0KGeRGJ3GylMhcMIrxbM3U3CLlr5iU5onM0KBgblK3DqY7vNAOTGjCdu2bM2DM2evJFCJdKC4AZwlGe5kA9nk0lsC1hjOSW7CuID4KIZt2jUS6gXThHri538rbjUxgvFg9BTl0DRgu/2bb5HJuFOwWvumhc8is+TZwpGGSGamCj3ZkNjSDLn2IXUhm4yyOrg1DkblAJrvhjUEph2wUUMU8cmBagcXI8PxbbSEWpl4J7e7GYv1Q5NA1D5Bzh/Mdp9RBNi0eioonmeyAsin77Jg4kQtLJebQQTj3nLfg9v6pYXctHIo5xa6j98IgzNpwR5xZeOt6kVMs3u8JdWHq1alIiMJ2LZKY60bQ854JAg87DolSwl5YYqg55UOv2nlP6qnjkCQ1gZ4FdYbqqV4vnpnpQUhP5+Gz41NQ7n/+4AlnZ0mgEPr7UyVwTEb5PIFOOZz96WQqg4FvTbJYL8H1lRuNOq+p7OkseFEFgR43YTcyt6od+oLA/ESP0rIbvqX3LG8jaKar2p5/Us56CE+Ojgu7iX+YsBHaR5R/P8z+dGTSYiwCPVN7g2gud6HeP3UULs+khQ5g633D/xuAwNB9zbPJtx/LEzytjzD8+T6VG6mR5qjupmb/28lZ6mWmO1CCrfaKt/pbvWiGKRQavar2PqKX7LcZC4IGVOv7/Qal3PNFC9qEkFLs7mP/s0zn21mHdDOng2+Cf4NUbFAMlxNg6Eyx29qtn2M6bw48wfTxxLkZp8lheZ6PVJu1ZrNZrUaEFzo31my9f+44X+vayPaHX8G/TMOvaJ4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uLi4uvwiLqysrK6vO/vvKubnYguqNyNLa2tq6mVOIR2Ceu7TqTyQS3kXlm2xF+PaGuUZOQ2ApHlG+sxL0+bxR1dfW/fMYvOI/jYnFhSOWVUIAq0HhS3HVB4Elr88Xt07BKNJ+/5rSjGKCGN+y6mt4hUFJYRAcoatQ1bmBJZ/wpoUSjGFBQ1cVb5lQ6MUrXJVYqYCjvGn5tSiVsMJ1r9C4qMJO8QqDfhUGCuNBCfFn8Uqv4jH4KVmFsQRswrriPZzClQU1CX2F2P4WbNoBhSy4GGgp2olYhVpC+goFTyIBzy6/cEJhJSj+ur5lxBtMrzC6LAM6OrQhvYiSV7jokw3IhwRFrMLFiBoDhYsysajwpWhEfil+SlIhcDPe9QXQvtDQToHC+egKcH6xwZuVUFSFgaeRYeGXfINoFIBEyClMg+sL0paFK/qHnQYVzkPvN3wT/BhjRwuJxWXJly6JH7Gij/UCyyGicBEo81YkhxocBMXYwBX6lgZfNhUPRQJrQAqMiEuwF1nkVyKiEIZjPyv9NWyhvkJ/UEVc/FnwCqFALzzQCw2VtMJIHLQ5Df4Ue3OdRRWCoO5VKvRXNP6G1VXIina9OrcwMFQ2SFQhu+YfOgGYP86vIApD64ChhwUKveoqRAansAL9NDgCeDLRUMXcLb1MxtOsggYk5H8oCXyCT6onpGjBAgZfN6uwAr1VRTwWSpRNREwz7FcYiUpuRnoZGr7Uzbz96zENMMhoFS7EYZiQXsH+9K6RjYdwlPiGmcyCT4wcc0a1hc+rJghFaBSmxTgon56FuZN3jWRtEfEOjQgCnY0PWpKJ6mnej1UIBfqjiOQlWChCt0ZGoahnCa18QSvng2Ck4RUOs2kUL1YhGIS+EGq17LqQhUs/KBGFsYRwkYSy8BV8qz+kq3BhYwkLbLXGShcEgTHF8YH1gZ8iopCNRIPeNeV7kUR8TX8csgE80AxicSF2okY5lw6m1ScYKA5sCNnbfy3Toks6qs5BVqVGjVk9IURA7KwopmNUJ1+MpSuVhZVF+IMEhnWGrehew7zCUUQWoqIbDq1HRn/bfixXmI765ELUG6qM/r7txIJ+7XwpCAEhIxJLekZRiUN9ksrgBqEpcDYG5tux/x4gAuaZ1I4COA9sRBzUIbj5UnicGDgTy8shr5jKkZG4uDGfSPhjo784II2tgUcqhPngvK8CpjIqCb9Yb5BgccM3mN4bD1FhAouBwiUoSnIwYqah19vWMplC3xJYTFITgx/h2x2A+e/A5iOwHl7BfdNqJlPoX8d+FNFbtxArT9/GcLyv+XXPYjGTKsR6CQOFFXAUEiFWvFYHWz2IKfQrJ2MjSKltL4724Z9KocPj0JvWzNUPUR8xmcJAEONLiQREqHBeM1U/ZE2tBcbDELZI3DCIcph4qKlqbEFUqI9fkz6KER9b6fuMcpoEnPlehznNvI9cThMYodCno9B01ibOCM9755eXE/AMXrylW86iUOQbEVxWt2MhbnyAru1V4uJPINUWmhPbBLuqWbZWojGlyIgD0rotXwj5ZYPxJwhVFoSJVELiboXEmpn49EshLeks/ik70MXFxcXFxcXF5f+L/wFH+8UlBDoNvAAAAABJRU5ErkJggg==", obj.getCropName(), obj.getDetails(),obj.getId(),obj.getAddress(),obj.getTitle(),obj.getDate(),obj.getSymptom(),obj.getPestName()));
        }

        currentHistoryAdapter = new CurrentSituationAdapter( getActivity().getApplicationContext(), currentHistoryList);  //어뎁터
        currentHistoryView.setAdapter(currentHistoryAdapter);


        secondCurrentHistoryList = new ArrayList<>();
        secondCurrentHistoryList.addAll(currentHistoryList);


        currentHistoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ReportHistory a = searchHistoryList.get(position);

                ReportHistory reportHistory = new ReportHistory(
                        a.getImageUrl(), a.getCropName(), a.getDetails(), a.getId(),
                        a.getAddress(), a.getTitle(), a.getDate(), a.getSymptom() , a.getPestName());

                Intent intent = new Intent(getActivity().getApplicationContext(), ReportRecordActivity.class);
                intent.putExtra("reportHistory", reportHistory);
                startActivity(intent);


            }
        });


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.current_show_map:
                mapView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.INVISIBLE);
                break;

            case R.id.current_show_table:
                mapView.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                break;
        }
    }






    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("위치 접근 권한");
        builder.setMessage("현재 위치를 찾기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 권한 설정을 수정해주세요.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(getActivity(), "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }
    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(getActivity(), "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(getActivity(), "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(getActivity(), "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }




}