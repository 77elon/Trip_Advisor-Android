package com.dhnns.navermaptest;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.LocationButtonView;
import com.naver.maps.map.widget.ZoomControlView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Map_Test extends AppCompatActivity implements OnMapReadyCallback {

    static final boolean enabled = true;
    static final boolean disabled = false;
    static final String sort = "distance";

    private NaverMap naverMap;
    private LinearLayoutCompat map_layout;
    private MapView mapView;
    private ZoomControlView zoomControlView;
    private LocationButtonView locationButtonView;
    private UiSettings uiSettings;
    private FusedLocationSource locationSource;
    private LocationOverlay locationOverlay;
    private LatLng curr_latlng, arr_latLng, dest_latLng;
    private CameraPosition cameraPosition;
    private CameraUpdate cameraUpdate;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private Button btn_ret;
    private DrawerLayout drawerLayout;
    //private NavigationView navigationView;
    private LinearLayoutCompat customized_view;
    private Toolbar toolbar;
    //private Geocoder geocoder;

    private AutoCompleteTextView Place_search;
    private Marker curr_marker, arr_marker, dest_marker;

    private LinearLayoutCompat arr_dest_selection;
    private TextView place_name, distance, phone, road_address_name;
    private Button arr_btn, dest_btn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_test);
        //네이버 지도
        mapView = (MapView) findViewById(R.id.map);

        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        this.uiSettings = naverMap.getUiSettings();

        naverMap.setLocale(Locale.KOREAN);

        cameraPosition = naverMap.getCameraPosition();
        //final !!!!
        curr_latlng = cameraPosition.target.toLatLng();

        getLocation(naverMap, locationSource, locationOverlay);

        InitObj();
        InitMarker();
        InitNavbar(customized_view, toolbar);
        InitUserInterface(uiSettings);
        InitInteraction(mapView, Place_search);

        Customized_Btn(naverMap, uiSettings, zoomControlView, locationButtonView);

        //Marker Set
        interactionTest(naverMap, cameraUpdate, arr_marker);

        //item IsSelected
        Search_Interaction(naverMap, Place_search, curr_latlng, arr_marker, dest_marker);

        //Direction Tracking...! Marker Based


        //naverMap.setOnMapClickListener((pointF, latLng) -> Place_search.clearFocus());
        btn_ret.setOnClickListener(v -> finish());

        //How to Set Dark/Light Mode Automation??, NaverMap(basic not support night_mode)

    }

    public void getLocation(@NonNull NaverMap naverMap, @NonNull FusedLocationSource locationSource, @NonNull LocationOverlay locationOverlay) {
        this.naverMap = naverMap;
        this.locationSource = locationSource;
        this.locationOverlay = locationOverlay;

        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

        naverMap.setLocationSource(locationSource);
        naverMap.setMapType(NaverMap.MapType.Basic);
        naverMap.setIndoorEnabled(enabled);

        /*locationOverlay = naverMap.getLocationOverlay();

        locationOverlay.setIcon(LocationOverlay.DEFAULT_ICON);
        locationOverlay.setVisible(true);*/
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
        customized_view = findViewById(R.id.customized_view);
        map_layout = findViewById(R.id.map_layout);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //navigationView = (NavigationView) findViewById(R.id.navigation_view);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Place_search = (AutoCompleteTextView) findViewById(R.id.Place_search);

        arr_dest_selection = findViewById(R.id.nav_interaction);

        place_name = findViewById(R.id.place_name);
        distance = findViewById(R.id.distance);
        phone = findViewById(R.id.phone);
        road_address_name = findViewById(R.id.road_address_name);

        arr_btn = findViewById(R.id.arr_btn);
        dest_btn = findViewById(R.id.dest_btn);
    }

    public void InitMarker()    {
        //Declare Object, Passing Global Variable
        this.curr_marker = new Marker();
        this.arr_marker = new Marker();
        this.dest_marker = new Marker();

        //Color Diff!
        curr_marker.setIconTintColor(Color.GREEN);
        curr_marker.setWidth(Marker.SIZE_AUTO);
        curr_marker.setHeight(Marker.SIZE_AUTO);
        curr_marker.setIconPerspectiveEnabled(true);

        arr_marker.setIconTintColor(Color.RED);
        arr_marker.setWidth(Marker.SIZE_AUTO);
        arr_marker.setHeight(Marker.SIZE_AUTO);
        arr_marker.setIconPerspectiveEnabled(true);

        dest_marker.setIconTintColor(Color.BLUE);
        dest_marker.setWidth(Marker.SIZE_AUTO);
        dest_marker.setHeight(Marker.SIZE_AUTO);
        dest_marker.setIconPerspectiveEnabled(true);
    }

    public void InitNavbar(@NonNull LinearLayoutCompat customized_view, @NonNull Toolbar toolbar) {
        this.customized_view = customized_view;
        this.toolbar = toolbar;

        //set Zoom Ctrl View margin

        //set Included View Gravity
        customized_view.setGravity(Gravity.CENTER_VERTICAL);

        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true); // 왼쪽 상단 버튼 만들기
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24); //왼쪽 상단 버튼 아이콘 지정

    }

    public void InitUserInterface(@NonNull UiSettings uiSettings) {
        this.uiSettings = uiSettings;
        uiSettings.setCompassEnabled(enabled);
        uiSettings.setScaleBarEnabled(enabled);
        uiSettings.setZoomGesturesEnabled(enabled);
        //illegal
        uiSettings.setLogoClickEnabled(disabled);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void InitInteraction(@NonNull MapView mapView, @NonNull AutoCompleteTextView Place_search) {
        this.mapView = mapView;
        this.Place_search = Place_search;
        Place_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus)
                {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });

        mapView.setOnTouchListener((v, event) -> {
            Place_search.clearFocus();
            return false;
        });
    }

    public void Customized_Btn(@NonNull NaverMap naverMap, @NonNull UiSettings uiSettings, @NonNull ZoomControlView zoomControlView, @NonNull LocationButtonView locationButtonView) {
        this.naverMap = naverMap;
        this.uiSettings = uiSettings;
        this.zoomControlView = zoomControlView;
        this.locationButtonView = locationButtonView;

        zoomControlView = (ZoomControlView) findViewById(R.id.zoom_control);
        uiSettings.setZoomControlEnabled(disabled);
        zoomControlView.setMap(naverMap);
        locationButtonView = (LocationButtonView) findViewById(R.id.currentLocationButton);
        uiSettings.setLocationButtonEnabled(disabled);
        locationButtonView.setMap(naverMap);
    }

    public void Search_Process(@NonNull NaverMap naverMap, @NonNull AutoCompleteTextView Place_search, @NonNull LatLng curr_latlng, @NonNull Marker curr_marker, @NonNull Marker arr_marker, @NonNull Marker dest_marker)
    {
        this.naverMap = naverMap;
        this.Place_search = Place_search;

        this.curr_latlng = curr_latlng;
        this.curr_marker = curr_marker;

        this.arr_marker = arr_marker;
        this.dest_marker = dest_marker;


        List<String> Place_List = new ArrayList<>();
        ArrayAdapter<?> adapter = new ArrayAdapter<>(Map_Test.this, android.R.layout.simple_dropdown_item_1line, Place_List);
        Place_search.setAdapter(adapter);

        Kakao_Place.getPlace(Place_search.getText().toString(), Double.toString(curr_latlng.latitude), Double.toString(curr_latlng.longitude), sort, new Kakao_Place.PlaceResponseListener() {
            @Override
            public void onSuccessResponse(Documents documents) {

                for (int i = 0; i < documents.getDocuments().size(); i++) {
                    Place_List.add(documents.getDocuments().get(i).getPlaceName());
                }

                adapter.notifyDataSetChanged();

                Place_search.setOnItemClickListener((parent, view, position, id) -> {
                    Document body = documents.getDocuments().get(position);
                    //documents.getDocuments().get(position)
                    double selected_lat = Double.parseDouble(body.getLatitude());
                    double selected_lng = Double.parseDouble(body.getLongitude());
                    LatLng selected_pos = new LatLng(selected_lat, selected_lng);
                    naverMap.moveCamera(CameraUpdate.scrollTo(selected_pos).animate(CameraAnimation.Easing));

                    curr_marker.setPosition(selected_pos);
                    curr_marker.setMap(naverMap);

                    arr_dest_selection.setVisibility(View.VISIBLE);
                    place_name.setText(body.getPlaceName());
                    distance.setText(Integer.parseInt(body.getDistance()) / 1000 + "km");
                    phone.setText(body.getPhone());
                    road_address_name.setText(body.getRoadAddressName());
                });


                /*
                //if user selected arr btn...(flag based) -> arr_latlng assignment + marker based latlng extract...!
                arr_marker.setPosition(curr_marker.getPosition());
                arr_marker.setMap(naverMap);

                //else user selected dest btn -> dest_latlng assignment
                dest_marker.setPosition(curr_marker.getPosition());
                dest_marker.setMap(naverMap);
                 */
            }

            @Override
            public void onFailResponse() {
                Toast.makeText(Map_Test.this, "정보 수신 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Search_Interaction(@NonNull NaverMap naverMap, @NonNull AutoCompleteTextView Place_search, @NonNull LatLng curr_latlng, @NonNull Marker arr_marker, @NonNull Marker dest_marker) {
        this.naverMap = naverMap;
        this.Place_search = Place_search;

        this.curr_latlng = curr_latlng;

        this.arr_marker = arr_marker;
        this.dest_marker = dest_marker;

        /*//Pressed Enter(Return)
        Place_search.setOnKeyListener((v, keyCode, event) -> {
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
            }
            return false;
        });*/

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
                Search_Process(naverMap, Place_search, curr_latlng, curr_marker, arr_marker, dest_marker);
            }
        });
    }

    //Android Framework 기반 Method 작성 시 전역변수에 대한 데이터 무결성이 보장되지 않는다... 쓰레드 사용 시 잠재적 에러 가능성...
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
                Intent intent = new Intent(this, Json_test.class);
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

    public void interactionTest(@NonNull NaverMap naverMap, @NonNull CameraUpdate cameraUpdate, @NonNull Marker curr_marker) {
        this.naverMap = naverMap;
        this.cameraUpdate = cameraUpdate;
        this.curr_marker = curr_marker;

        naverMap.setOnSymbolClickListener(symbol -> {
            if (!symbol.getCaption().equals("")) {
                //Pop-Up Overlay로 소개해줘도 좋을 듯... 혹은 선택한 것을 기록할 수도 있다...
                naverMap.moveCamera(CameraUpdate.scrollTo(symbol.getPosition().toLatLng()).animate(CameraAnimation.Easing));
                curr_marker.setPosition(symbol.getPosition().toLatLng());
                curr_marker.setMap(naverMap);

                Toast.makeText(this, symbol.getCaption(), Toast.LENGTH_SHORT).show();
                // 이벤트 소비, OnMapClick 이벤트는 발생하지 않음
                return true;
            }

            curr_marker.setMap(null);
            // 이벤트 전파, OnMapClick 이벤트가 발생함
            return false;
        });

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
        cameraPosition = null;
        cameraUpdate = null;

        btn_ret = null;
        drawerLayout = null;
        //navigationView = null;
        customized_view = null;
        toolbar = null;
        //geocoder = null;

        Place_search = null;

        super.onDestroy();
    }
}
