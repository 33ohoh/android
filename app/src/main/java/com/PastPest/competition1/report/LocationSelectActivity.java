package com.PastPest.competition1.report;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.PastPest.competition1.R;
import com.PastPest.competition1.kakaoMap.AddressApiActivity;
import com.PastPest.competition1.kakaoMap.NetworkStatus;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
//import com.example.mechacat.databinding.ActivityMainBinding;
public class LocationSelectActivity extends AppCompatActivity implements MapView.CurrentLocationEventListener, MapView.MapViewEventListener{
    double latitude;
    double longitude;
    TextView addressView;
    EditText searchText;
    MapView mapView;
    MapPoint MARKER_POINT;
    MapPOIItem marker = new MapPOIItem();
    private com.PastPest.competition1.kakaoMap.GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        Intent intent=getIntent();
        longitude=intent.getDoubleExtra("longitude",127.075032);
        latitude=intent.getDoubleExtra("latitude",37.5495538);
        String loadAddress=intent.getStringExtra("loadAddress");
        String detailAddress=intent.getStringExtra("detailAddress");
        MARKER_POINT=MapPoint.mapPointWithGeoCoord(latitude, longitude);
        mapView = new MapView(this);
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(MARKER_POINT);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양
        mapView.addPOIItem(marker);

        mapView.setMapViewEventListener(this);
        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);



        addressView=(TextView) findViewById(R.id.address_text);
        addressView.setText(loadAddress);
        EditText editText=(EditText) findViewById(R.id.detail_address);
        editText.setText(detailAddress);


        AppCompatButton saveBtn=(AppCompatButton) findViewById(R.id.locationSaveButton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent locationIntent=new Intent();
                locationIntent.putExtra("loadAddress",addressView.getText().toString());
                locationIntent.putExtra("detailAddress",editText.getText().toString());
                locationIntent.putExtra("latitude",latitude);
                locationIntent.putExtra("longitude",longitude);
                setResult(RESULT_OK,locationIntent);
                finish();
            }
        });

        searchText=(EditText) findViewById(R.id.locationSearchText);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int status = NetworkStatus.getConnectivityStatus(getApplicationContext());
                if(status == NetworkStatus.TYPE_MOBILE || status == NetworkStatus.TYPE_WIFI) {
                    Intent searchIntent=new Intent(getApplicationContext(), AddressApiActivity.class);
                    overridePendingTransition(0,0);
                    startActivityForResult(searchIntent,1000);
                }else {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AppCompatButton currentLocationButton=(AppCompatButton) findViewById(R.id.currentLocationButton);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLocationServicesStatus()) {
                    showDialogForLocationServiceSetting();
                }else {
                    checkRunTimePermission();
                }
                gpsTracker = new com.PastPest.competition1.kakaoMap.GpsTracker(getApplicationContext());

                latitude = gpsTracker.getLatitude();
                longitude = gpsTracker.getLongitude();

                String address = getCurrentAddress(latitude, longitude);
                addressView.setText(address);
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                MARKER_POINT=mapView.getMapCenterPoint();
                marker.setMapPoint(MARKER_POINT);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode==1000){
            if(resultCode==RESULT_OK){
                String data=intent.getExtras().getString("data");
                if(data!=null){
                    Log.i("test","data:"+data);
                    String adress[]=data.split(",");
                    //searchText.setText(adress[1].trim());
                    addressView.setText(adress[1].trim());
                    final Geocoder geocoder = new Geocoder(getApplicationContext());
                    List<Address> list=new List<Address>() {
                        @Override
                        public int size() {
                            return 0;
                        }

                        @Override
                        public boolean isEmpty() {
                            return false;
                        }

                        @Override
                        public boolean contains(@Nullable Object o) {
                            return false;
                        }

                        @NonNull
                        @Override
                        public Iterator<Address> iterator() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public Object[] toArray() {
                            return new Object[0];
                        }

                        @NonNull
                        @Override
                        public <T> T[] toArray(@NonNull T[] ts) {
                            return null;
                        }

                        @Override
                        public boolean add(Address address) {
                            return false;
                        }

                        @Override
                        public boolean remove(@Nullable Object o) {
                            return false;
                        }

                        @Override
                        public boolean containsAll(@NonNull Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public boolean addAll(@NonNull Collection<? extends Address> collection) {
                            return false;
                        }

                        @Override
                        public boolean addAll(int i, @NonNull Collection<? extends Address> collection) {
                            return false;
                        }

                        @Override
                        public boolean removeAll(@NonNull Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public boolean retainAll(@NonNull Collection<?> collection) {
                            return false;
                        }

                        @Override
                        public void clear() {

                        }

                        @Override
                        public Address get(int i) {
                            return null;
                        }

                        @Override
                        public Address set(int i, Address address) {
                            return null;
                        }

                        @Override
                        public void add(int i, Address address) {

                        }

                        @Override
                        public Address remove(int i) {
                            return null;
                        }

                        @Override
                        public int indexOf(@Nullable Object o) {
                            return 0;
                        }

                        @Override
                        public int lastIndexOf(@Nullable Object o) {
                            return 0;
                        }

                        @NonNull
                        @Override
                        public ListIterator<Address> listIterator() {
                            return null;
                        }

                        @NonNull
                        @Override
                        public ListIterator<Address> listIterator(int i) {
                            return null;
                        }

                        @NonNull
                        @Override
                        public List<Address> subList(int i, int i1) {
                            return null;
                        }
                    };
                    String str = adress[1];
                    try {
                        list = geocoder.getFromLocationName(str,10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (list != null) {
                        String city = "";
                        String country = "";
                        if (list.size() == 0) {
                            Toast.makeText(getApplicationContext(), "올바른 주소를 입력해주세요. ", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Address address = list.get(0);
                            latitude = address.getLatitude();
                            longitude = address.getLongitude();
                            mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                            MARKER_POINT=mapView.getMapCenterPoint();
                            marker.setMapPoint(MARKER_POINT);
                        }
                    }
                }
            }
        }
        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }

    }
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
        MapPoint.GeoCoordinate mapPointGeo = mapPoint.getMapPointGeoCoord();
        //Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
        MARKER_POINT=mapView.getMapCenterPoint();
        marker.setMapPoint(MARKER_POINT);
        latitude=MARKER_POINT.getMapPointGeoCoord().latitude;
        longitude=MARKER_POINT.getMapPointGeoCoord().longitude;
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
        MARKER_POINT=mapView.getMapCenterPoint();
        marker.setMapPoint(MARKER_POINT);
        latitude=MARKER_POINT.getMapPointGeoCoord().latitude;
        longitude=MARKER_POINT.getMapPointGeoCoord().longitude;
        addressView.setText(getCurrentAddress(latitude,longitude));
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
        MARKER_POINT=mapView.getMapCenterPoint();
        marker.setMapPoint(MARKER_POINT);
        latitude=MARKER_POINT.getMapPointGeoCoord().latitude;
        longitude=MARKER_POINT.getMapPointGeoCoord().longitude;
    }
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(getApplicationContext(), "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }
    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(LocationSelectActivity.this);
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



    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}

