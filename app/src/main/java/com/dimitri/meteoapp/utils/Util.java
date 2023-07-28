package com.dimitri.meteoapp.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dimitri.meteoapp.R;

import com.dimitri.meteoapp.models.CityGson;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Util {

    private static final String PREFS_NAME = "prefs";
    private static final String PREFS_FAVORITE_CITIES = "favorite_cities";

    public static final String KEY_MESSAGE = "key_message";


    /*
     * Méthode qui initialise l'icon blanc présent dans la MainActivity
     * */
    public static int setWeatherIcon(int actualId, long sunrise, long sunset) {

        int id = actualId / 100;
        int icon = R.drawable.weather_sunny_white;

        if (actualId == 800) {
            long currentTime = new Date().getTime();

            if (currentTime >= sunrise && currentTime < sunset) {
                icon = R.drawable.weather_sunny_white;
            } else {
                icon = R.drawable.weather_clear_night_white;
            }
        } else {
            switch (id) {
                case 2:
                    icon = R.drawable.weather_thunder_white;
                    break;
                case 3:
                    icon = R.drawable.weather_drizzle_white;
                    break;
                case 7:
                    icon = R.drawable.weather_foggy_white;
                    break;
                case 8:
                    icon = R.drawable.weather_cloudy_white;
                    break;
                case 6:
                    icon = R.drawable.weather_snowy_white;
                    break;
                case 5:
                    icon = R.drawable.weather_rainy_white;
                    break;
            }
        }

        return icon;
    }

    /*
     * Méthode qui initialise l'icon gris présent dans le forecast et dans la liste des favoris.
     * */
    public static int setWeatherIcon(int actualId) {

        int id = actualId / 100;
        int icon = R.drawable.weather_sunny_grey;

        if (actualId != 800) {
            switch (id) {
                case 2:
                    icon = R.drawable.weather_thunder_grey;
                    break;
                case 3:
                    icon = R.drawable.weather_drizzle_grey;
                    break;
                case 7:
                    icon = R.drawable.weather_foggy_grey;
                    break;
                case 8:
                    icon = R.drawable.weather_cloudy_grey;
                    break;
                case 6:
                    icon = R.drawable.weather_snowy_grey;
                    break;
                case 5:
                    icon = R.drawable.weather_rainy_grey;
                    break;
            }
        }

        return icon;
    }

    public static boolean isActiveNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static ArrayList<CityGson> initFavoriteCities(Context context) {
        ArrayList<CityGson> cities = new ArrayList<>();
        SharedPreferences preferences = context.getSharedPreferences(Util.PREFS_NAME, Context.MODE_PRIVATE);
        try {
            JSONArray jsonArray = new JSONArray(preferences.getString(Util.PREFS_FAVORITE_CITIES,
                    ""));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectCity = new JSONObject(jsonArray.getString(i));
                Gson mGson;
                mGson = new Gson();
                CityGson city = mGson.fromJson(jsonObjectCity.toString(), CityGson.class);
                cities.add(city);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;
    }
    public static void saveFavouriteCities(Context context, ArrayList<CityGson> cities) {
        Gson mGson;
        mGson = new Gson();
        JSONArray jsonArrayCities = new JSONArray();
        for (int i = 0; i < cities.size(); i++) {
                jsonArrayCities.put(mGson.toJson(cities.get(i)));
        }
        SharedPreferences preferences = context.getSharedPreferences(
                Util.PREFS_NAME,
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(
                Util.PREFS_FAVORITE_CITIES,
                jsonArrayCities.toString()
        ).apply();

    }

    public static String capitalize(String s) {
        if (s == null) return null;
        if (s.length() == 1) {
            return s.toUpperCase();
        }
        if (s.length() > 1) {
            return s.substring(0, 1).toUpperCase() + s.substring(1);
        }
        return "";
    }

    public static void deleteCity(Context context, ArrayList<CityGson> newCityArray){
        saveFavouriteCities(context, newCityArray);
    }
}
