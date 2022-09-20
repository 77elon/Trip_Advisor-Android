package com.dhnns.navermaptest;

import static java.lang.System.out;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.EditText;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PolylineOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class NaverDirection extends AsyncTask<String, String, String> {

    private NaverMap naverMap;
    private CameraPosition cameraPosition;
    private Geocoder geocoder;
    private ArrayList<LatLng> naverRouteList = new ArrayList<LatLng>();

    private EditText dest_search;

    String str = dest_search.getText().toString();
    List<Address> addressList = null;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(naverRouteList.get(0));
        naverMap.moveCamera(cameraUpdate);

        PolylineOverlay polylineOverlay = new PolylineOverlay();
        polylineOverlay.setCoords(naverRouteList);
        polylineOverlay.setMap(naverMap);

        Marker marker = new Marker();
        marker.setPosition(naverRouteList.get(naverRouteList.size()-1));
        marker.setMap(naverMap);
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            // editText에 입력한 텍스트(주소, 지역, 장소 등)을 지오 코딩을 이용해 변환
            addressList = geocoder.getFromLocationName(
                    str, // 검색키워드
                    10); // 최대 검색 결과 개수
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // 콤마를 기준으로 split
        String []splitStr = addressList.get(0).toString().split(",");
        String address = splitStr[0].substring(splitStr[0].indexOf("\"") + 1,splitStr[0].length() - 2); // 주소

        String latitude = splitStr[10].substring(splitStr[10].indexOf("=") + 1); // 위도
        String longitude = splitStr[12].substring(splitStr[12].indexOf("=") + 1); // 경도

        //검색을 한 위치를 좌표값으로 변환해준다
        //목적지 좌표값 만들어준다
        double destinationLatitude = Double.parseDouble(latitude);
        double destinationLongitude = Double.parseDouble(longitude);
        LatLng destinationPoint = new LatLng(destinationLatitude, destinationLongitude);

        //현재위치 생성
        LatLng startPoint = cameraPosition.target.toLatLng();
        out.println("출발 지점 " + startPoint);
        String temp = "Not Gained";
        try {
            //시작 좌표와 목적지 좌표를 넣어서 URL을 검색한 다음 각 길의 좌표들을 리스트에 담아준다.
            temp = GET(startPoint,destinationPoint);
            return temp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }

    //시작 좌표와 목적지 좌표를 넣어서 URL을 검색한 다음 각 길의 좌표들을 리스트에 담아준다.
    private String GET(LatLng startPoint, LatLng destinationPoint) throws IOException {
        String data2 = "";

        // 요청 url 출발지 -> 도착지
        String startLatitude =String.valueOf(startPoint.latitude);
        String startLongitude =String.valueOf(startPoint.longitude);

        String destinationLatitude =String.valueOf(destinationPoint.latitude);
        String destinationLongitude =String.valueOf(destinationPoint.longitude);


        out.println("출발 경도: " + startLatitude);
        out.println("출발 위도: " + startLongitude);
        out.println("도착 경도: " + destinationLatitude);
        out.println("도착 위도: " + destinationLongitude);

        String naver_url = "https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?start="+startLongitude+","+startLatitude+"&goal="+destinationLongitude+","+destinationLatitude;
        String myUrl = naver_url;

        try {
            URL url = new URL(myUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            //네이버 플랫폼에서 발급받은 키
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", BuildConfig.CLIENT_ID);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", BuildConfig.CLIENT_SECRET);
            conn.setDoInput(true);
            conn.connect();

            //GSON파일을 읽어온다
            String line;
            String result = "";
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer response = new StringBuffer();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            out.println("차량최종결과"+response);
            result = response.toString();
            JSONObject root = new JSONObject(result);
            JSONObject route = root.getJSONObject("route");
            JSONObject traoptimal = (JSONObject) route.getJSONArray("traoptimal").get(0);
            JSONObject summary = traoptimal.getJSONObject("summary");
            JSONArray path = traoptimal.getJSONArray("path");

//                naverDistance = summary.getInt("distance");
//                naverDepartureTime = summary.getString("departureTime");
//                naverTollFare = summary.getInt("tollFare");
//                naverTaxiFare = summary.getInt("taxiFare");
//                naverFuelPriace = summary.getInt("fuelPrice");

            //좌표값을 가지고 와서 뿌려준다
            for (int i = 0 ; i < path.length() ; i++){
                JSONArray pathIndex = (JSONArray) path.get(i);
                out.println("인덱스 값좀보자"+pathIndex);
                naverRouteList.add(new LatLng(pathIndex.getDouble(1),pathIndex.getDouble(0)));

            }

        } catch (NullPointerException | JSONException e) {
            e.printStackTrace();

        }
        return data2;
    }
}



