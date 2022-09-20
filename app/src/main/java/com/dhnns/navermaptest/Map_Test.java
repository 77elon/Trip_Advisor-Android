package com.dhnns.navermaptest;

import android.Manifest;
import android.content.Intent;
import android.graphics.PointF;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.ArrayList;
import java.util.List;

public class Map_Test extends AppCompatActivity implements OnMapReadyCallback {

    static final boolean enabled = true;
    static final boolean disabled = false;
    static final String sort = "distance";

    private NaverMap naverMap;
    private MapView mapView;
    private ZoomControlView zoomControlView;
    private LocationButtonView locationButtonView;
    private UiSettings uiSettings;
    private FusedLocationSource locationSource;
    private LatLng latLng;
    private CameraPosition cameraPosition;
    private CameraUpdate cameraUpdate;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Button btn_ret;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private LinearLayout customized_view;
    private Toolbar toolbar;
    private Geocoder geocoder;

    private AutoCompleteTextView Place_search;
    private Marker marker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_test);
        mapView = (MapView) findViewById(R.id.map);

        //네이버 지도
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

    }
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        uiSettings = naverMap.getUiSettings();

        InitObj();
        InitNavbar();
        InitMarker();
        InitUserInterface(uiSettings);

        Customized_Btn(naverMap, uiSettings);
        interactionTest(naverMap, cameraUpdate);
        Search_Interaction();


        btn_ret.setOnClickListener(v -> finish());

        //Set Dark/Light Mode How to Automation??, NaverMap(basic not support night_mode)

        //if naver's locate button IsPressed

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        cameraPosition = naverMap.getCameraPosition();

        naverMap.setLocationSource(locationSource);
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setIndoorEnabled(enabled);

        latLng = cameraPosition.target.toLatLng();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
                return;
            } else {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void InitObj() {
        btn_ret = (Button) findViewById(R.id.btn_ret);
        customized_view = (LinearLayout) findViewById(R.id.customized_view);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Place_search = (AutoCompleteTextView) findViewById(R.id.Place_search);
    }

    public void InitNavbar() {
        //set Zoom Ctrl View margin

        //set Included View Gravity
        customized_view.setGravity(Gravity.CENTER_VERTICAL);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //왼쪽 상단 버튼 아이콘 지정

    }

    public void InitMarker()
    {
        marker = new Marker();
        marker.setWidth(Marker.SIZE_AUTO);
        marker.setHeight(Marker.SIZE_AUTO);
        marker.setIconPerspectiveEnabled(true);
    }

    public void Search_Process(@NonNull NaverMap naverMap)
    {
        this.naverMap = naverMap;
        List <String> Place_List = new ArrayList<>();
        ArrayAdapter<?> adapter = new ArrayAdapter<String>(Map_Test.this, android.R.layout.simple_dropdown_item_1line, Place_List);
        Place_search.setAdapter(adapter);

        Kakao_Place kakao_place = new Kakao_Place();
        kakao_place.getPlace(Place_search.getText().toString(), Double.toString(latLng.latitude), Double.toString(latLng.longitude), sort, new Kakao_Place.PlaceResponseListener() {
            @Override
            public void onSuccessResponse(Documents documents) {

                for (int i = 0; i < documents.getDocuments().size(); i++) {
                    Place_List.add(documents.getDocuments().get(i).getPlaceName());
                }
                adapter.notifyDataSetChanged();
                Place_search.setOnItemClickListener((parent, view, position, id) -> {

                    double selected_lat = Double.parseDouble(documents.getDocuments().get(position).getLatitude());
                    double selected_lng = Double.parseDouble(documents.getDocuments().get(position).getLongitude());
                    LatLng selected_pos = new LatLng(selected_lat, selected_lng);

                    marker.setPosition(selected_pos);

                    naverMap.moveCamera(cameraUpdate.scrollTo(selected_pos).animate(CameraAnimation.Easing));
                    marker.setMap(naverMap);
                });
            }

            @Override
            public void onFailResponse() {
                Toast.makeText(Map_Test.this, "정보 수신 실패", Toast.LENGTH_SHORT).show();
            }
        });

        //Log.d("Test", "Result: ");

        //Query_result = new ArrayList<Documents>();
        //Query_result = kakao_place.result;

    }

    public void Search_Interaction()
    {
        //Pressed Enter(Return)
        Place_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_ENTER:

                }
                return false;
            }
        });


        //텍스트가 입력되고, 몇 초 후에 출력하기...
        Place_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //real-time Search process
                Search_Process(naverMap);
            }
        });
    }

    public void Marker_Remover()
    {
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                marker.setMap(null);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                //out.println("Execute1");
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
            //JSON 파싱 테스트
            case R.id.test:
            {
                Intent intent = new Intent(this, Map_Test.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //뒤로가기 했을 때
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //out.println("Execute2");
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //out.println("Execute3");
            super.onBackPressed();
        }
    }

    public void InitUserInterface(@NonNull UiSettings uiSettings) {
        this.uiSettings = uiSettings;
        uiSettings.setCompassEnabled(enabled);
        uiSettings.setScaleBarEnabled(enabled);
        uiSettings.setZoomGesturesEnabled(disabled);
        //illegal
        uiSettings.setLogoClickEnabled(disabled);
    }

    public void interactionTest(@NonNull NaverMap naverMap, @NonNull CameraUpdate cameraUpdate) {
        this.naverMap = naverMap;
        this.cameraUpdate = cameraUpdate;

        naverMap.setOnSymbolClickListener(symbol -> {
            if (!symbol.getCaption().equals("")) {
                //Pop-Up Overlay로 소개해줘도 좋을 듯... 혹은 선택한 것을 기록할 수도 있다...
                naverMap.moveCamera(cameraUpdate.scrollTo(symbol.getPosition().toLatLng()).animate(CameraAnimation.Easing));
                marker.setPosition(symbol.getPosition().toLatLng());
                marker.setMap(naverMap);

                Toast.makeText(this, symbol.getCaption(), Toast.LENGTH_SHORT).show();
                // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
                return true;
            }
            // 이벤트 전파, OnMapClick 이벤트가 발생함
            return false;
        });
    }

    public void Customized_Btn(@NonNull NaverMap naverMap, @NonNull UiSettings uiSettings) {
        this.naverMap = naverMap;
        this.uiSettings = uiSettings;

        zoomControlView = (ZoomControlView) findViewById(R.id.zoom_control);
        uiSettings.setZoomControlEnabled(disabled);
        zoomControlView.setMap(naverMap);
        locationButtonView = (LocationButtonView) findViewById(R.id.currentLocationButton);
        uiSettings.setLocationButtonEnabled(disabled);
        locationButtonView.setMap(naverMap);
    }



    @Override
    public void onDestroy() {
        //removeAllView();
        naverMap = null;
        mapView = null;
        zoomControlView = null;
        locationButtonView = null;
        uiSettings = null;
        locationSource = null;
        latLng = null;
        cameraUpdate = null;
        super.onDestroy();
    }
}
