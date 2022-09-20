package com.dhnns.navermaptest;

import static java.lang.System.out;

import android.Manifest;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
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
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class Map_Test extends AppCompatActivity implements OnMapReadyCallback {

    static final boolean enabled = true;
    static final boolean disabled = false;

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
    private ArrayList<LatLng> naverRouteList = new ArrayList<LatLng>();

    private EditText dest_search;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_test);

        InitObj();
        InitNavbar();

        btn_ret.setOnClickListener(v -> finish());
        //네이버 지도
        mapView = (MapView) findViewById(R.id.map);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
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
        dest_search = (EditText) findViewById(R.id.dest_search);

    }

    public void InitNavbar() {
        //set Zoom Ctrl View margin

        //set Included View Gravity
        customized_view.setGravity(Gravity.CENTER_VERTICAL);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //왼쪽 상단 버튼 아이콘 지정

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        out.println(item);
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                //out.println("Execute1");
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
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
                //Pop-Up으로 소개해줘도 좋을 듯... 혹은 선택한 것을 기록할 수도 있다...
                naverMap.moveCamera(cameraUpdate.scrollTo(symbol.getPosition().toLatLng()).animate(CameraAnimation.Easing));
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
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        uiSettings = naverMap.getUiSettings();

        InitUserInterface(uiSettings);
        Customized_Btn(naverMap, uiSettings);
        interactionTest(naverMap, cameraUpdate);

        //Set Dark/Light Mode How to Automation??, NaverMap(basic not support night_mode)

        //if naver's locate button IsPressed

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        cameraPosition = naverMap.getCameraPosition();

        naverMap.setLocationSource(locationSource);
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setIndoorEnabled(enabled);
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
