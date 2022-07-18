package com.example.wa1;

import android.location.LocationManager;
import android.os.Bundle;

import androidx.collection.ArrayMap;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import vn.thanguit.toastperfect.ToastPerfect;

public class WeatherFragment extends Fragment {

    private View mRootView;
    private RelativeLayout RLHome;
    private ProgressBar PBLoading;
    private ImageView backgroundIV, iconIV;
    private TextView temperatureTV, conditionTV, cityNameTV;

    private RecyclerView weatherRV;
    private ArrayList<WeatherRVModal> mWeatherList;
    private WeatherRVAdapter rvAdapter;
    private String cityName;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_weather, container, false);
        initUI();
        mWeatherList = new ArrayList<>();
        rvAdapter = new WeatherRVAdapter(getContext(), mWeatherList);
        weatherRV.setAdapter(rvAdapter);

        Bundle mReceiveBundle = getArguments();
        if (mReceiveBundle != null) {
            CityName mCityName = (CityName) mReceiveBundle.get("data_key");
            if (mCityName != null) {
                // Process UI layout in here
                PBLoading.setVisibility(View.VISIBLE);
                RLHome.setVisibility(View.GONE);
                getCurrentWeatherInfo(mCityName.getCityName());
                getForecastWeatherInfo(mCityName.getCityName());

            }
        }
        return mRootView;
    }

    public void initUI() {
        RLHome = mRootView.findViewById(R.id.idRLHome);
        PBLoading = mRootView.findViewById(R.id.idPBLoading);
        backgroundIV = mRootView.findViewById(R.id.idIVBack);
        cityNameTV = mRootView.findViewById(R.id.idTVCityName);
        temperatureTV = mRootView.findViewById(R.id.idTVCurrentTemperature);
        iconIV = mRootView.findViewById(R.id.idIVIcon);
        conditionTV = mRootView.findViewById(R.id.idTVCondition);
        weatherRV = mRootView.findViewById(R.id.idRVWeather);
    }

    public void getCurrentWeatherInfo(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityName + "&appid=c71c878f3ebbf0bea3e1b814098c43c5&units=metric";

        cityNameTV.setText(cityName);

        RequestQueue mReqQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    PBLoading.setVisibility(View.GONE);
                    RLHome.setVisibility(View.VISIBLE);

                    try {
                        JSONObject main = response.getJSONObject("main");
                        String temperatureStr = main.getString("temp");
                        temperatureTV.setText(temperatureStr + "°C");

                        JSONObject weather0 = response.getJSONArray("weather").getJSONObject(0);
                        String condition = weather0.getString("main");
                        conditionTV.setText(condition);
                        String iconStr = weather0.getString("icon");
                        String urlIcon = "https://openweathermap.org/img/w/" + iconStr + ".png";
                        Picasso.get().load(urlIcon).into(iconIV);

                        Picasso.get().load("https://images.unsplash.com/photo-1527190997915-67ce3b53cc58?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80").into(backgroundIV);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> ((MainActivity) getActivity()).displayMessage(ToastPerfect.WARNING, "Error response for current weather!")) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> mHeaders = new ArrayMap<>();
                mHeaders.put("X-RapidAPI-Key", "94c7a0e085msh104067b984b31abp1fd6b5jsn62951eceeec3");
                mHeaders.put("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com");
                return mHeaders;
            }
        };
        mReqQueue.add(jsonObjectRequest);
    }

    public void getForecastWeatherInfo(String cityName) {
        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + cityName + "&appid=c71c878f3ebbf0bea3e1b814098c43c5&units=metric&cnt=24";


        RequestQueue mReqQueue = Volley.newRequestQueue(getContext());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    mWeatherList.clear();

                    try {
                        // Forecast
                        JSONArray list = response.getJSONArray("list");

                        for (int i = 0; i < list.length(); i++) {
                            JSONObject hourObject = list.getJSONObject(i);

                            String dateTimeStr = hourObject.getString("dt_txt");
                            String temper = hourObject.getJSONObject("main").getString("temp");
                            // Transfer the Description replace Icon
                            String icon = hourObject.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String urlIcon = "https://openweathermap.org/img/w/" + icon + ".png";
                            String windS = hourObject.getJSONObject("wind").getString("speed");
                            mWeatherList.add(new WeatherRVModal(dateTimeStr, temper, urlIcon, windS));
                        }
                        rvAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> ((MainActivity) getActivity()).displayMessage(ToastPerfect.WARNING, "Error response for forecast weather!")) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> mHeaders = new ArrayMap<>();
                mHeaders.put("X-RapidAPI-Key", "94c7a0e085msh104067b984b31abp1fd6b5jsn62951eceeec3");
                mHeaders.put("X-RapidAPI-Host", "community-open-weather-map.p.rapidapi.com");
                return mHeaders;
            }
        };
        mReqQueue.add(jsonObjectRequest);
    }
}