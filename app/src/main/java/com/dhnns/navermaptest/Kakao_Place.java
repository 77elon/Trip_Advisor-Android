package com.dhnns.navermaptest;

import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Kakao_Place {

    //protected Documents documents;

    //Input From Toolbar_EditText
    public void getPlace(String Query, String Latitude, String Longitude, String Sort, PlaceResponseListener placeResponseListener)
    {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        builder.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("Authorization", BuildConfig.KAKAO_REST_API).build();
            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        //입력 데이터, 위치 x, y를 받아오고, interface의 getPlace 호출
        Kakao_Query kakao_query = retrofit.create(Kakao_Query.class);
        if(!Query.equals(""))
        {
            Call<Documents> call = kakao_query.listPlaces(Query, Longitude, Latitude, Sort);

            call.enqueue(new Callback<Documents>() {
                @Override
                public void onResponse(Call<Documents> call, Response<Documents> response) {

                    if (!response.isSuccessful()) {
                        Log.d("Test", "Raw: "+ response.raw());
                        Log.d("Test", "Error Code: " + response.code());
                        return;
                    }
                    else if (response.isSuccessful()) {
                        if (response.body() != null) {
                            for (int i = 0; i < response.body().getDocuments().size(); i++) {
                                Log.i("Test", "[GET] getAddressList : " + response.body().getDocuments().get(i).getPlaceName());
                            }
                        }
                        Log.d("Test", "Raw: "+ response.raw());
                        Log.d("Test", "Body: " + response.body());
                        placeResponseListener.onSuccessResponse(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Documents> call, Throwable t) {
                    Log.w("Test", "통신 실패: " + t.getMessage() );
                    placeResponseListener.onFailResponse();
                }
            });

            //이후 retrofit 기반으로 commit. query 실시.

        }

    }

    public interface PlaceResponseListener{
        void onSuccessResponse(Documents documents);
        void onFailResponse();
    }
}
