package com.dimitri.meteoapp.models;

import org.json.JSONException;
import org.json.JSONObject;

public class City {


    public String mName;
    public int mWeatherResIconGrey;
    public String mTemperature;
    public String mDescription;
    public int mIdCity;
    public double mlatitude;
    public double mLongitude;
    public int mWeatherResIconWhite;
    public City(String name, String desc, String temp, int resWeatherIcon) {
        mName = name;
        mDescription = desc;
        mTemperature = temp;
        mWeatherResIconGrey = resWeatherIcon;

    }

    public City(String stringJson) throws JSONException {
        JSONObject json = new JSONObject(stringJson);
        mName = json.getString("name");
        mDescription =json.getJSONArray("weather").getJSONObject(0).getString("weather");
        //mTemperature = temp;
    }

}