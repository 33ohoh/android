package com.example.competition1.fragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.competition1.CurrentSituation;
import com.example.competition1.CurrentSituationAdapter;
import com.example.competition1.LoginActivity;
import com.example.competition1.LoginedId;
import com.example.competition1.Mapdata;
import com.example.competition1.NetworkStatusActivity;
import com.example.competition1.R;
import com.example.competition1.report.LocationSelectActivity;

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
    private ArrayList<CurrentSituation> currentHistoryList;
    private ArrayList<CurrentSituation> searchHistoryList;
    private View view;
    private EditText cropSearch;
    private ArrayList<Mapdata> mapdataList;

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

        com.tistory.webnautes.get_gps_location.GpsTracker gpsTracker;
        double latitude;
        double longitude;

        //현재위치 이동
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
        gpsTracker = new com.tistory.webnautes.get_gps_location.GpsTracker(getActivity().getApplicationContext());

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

            Mapdata obj = mapdataList.get(index);

            MapPOIItem marker = new MapPOIItem();
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(obj.getLatitude(), obj.getLongitude());



            marker.setItemName(obj.getProductName());
            marker.setTag(0);
            marker.setMapPoint(mapPoint);
            marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양
            mapView.addPOIItem(marker);
        }

    }


    public void search(String charText) {
        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        currentHistoryList.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            currentHistoryList.addAll(searchHistoryList);
        }
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < searchHistoryList.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (searchHistoryList.get(i).getCropName().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    currentHistoryList.add(searchHistoryList.get(i));
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

                 mapdataList.add(new Mapdata(productName,details,latitude,longitude,id,address));
            }

        }catch (JSONException e) {
            e.printStackTrace();
        }


    }
    private void setMapdataList(){

        mapdataList= new ArrayList<>();

        JSONObject requestJsonObject = new JSONObject();
        String url = "http://ec2-43-200-8-163.ap-northeast-2.compute.amazonaws.com:3000";
        //인터넷 연결확인
        int status = NetworkStatusActivity.getConnectivityStatus(getActivity().getApplicationContext());
        if (status == NetworkStatusActivity.TYPE_MOBILE || status == NetworkStatusActivity.TYPE_WIFI) {

            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "/declarations/data", requestJsonObject, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {

                        if (response.getBoolean("status")) {
                            persingData(response);
                            //마커 찍기
                            setMarker();
                            setCurrentHistoryList();
                            Toast.makeText(getActivity().getApplicationContext(), "맵데이터 잘받아옴", Toast.LENGTH_SHORT).show();
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


        for(int index = 0 ; index < mapdataList.size() ; index++){

            Mapdata obj = mapdataList.get(index);

            currentHistoryList.add(new CurrentSituation("data:image/jpeg;base64,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", obj.getProductName(), obj.getDetails(),obj.getId(),obj.getAddress()));
        }

        currentHistoryAdapter = new CurrentSituationAdapter( getActivity().getApplicationContext(), currentHistoryList);  //어뎁터
        currentHistoryView.setAdapter(currentHistoryAdapter);


        searchHistoryList = new ArrayList<>();
        searchHistoryList.addAll(currentHistoryList);

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