package com.dimitri.meteoapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dimitri.meteoapp.R;
import com.dimitri.meteoapp.databinding.ActivityMainBinding;
import com.dimitri.meteoapp.models.City;
import com.dimitri.meteoapp.models.CityGson;
import com.dimitri.meteoapp.utils.API;
import com.dimitri.meteoapp.utils.Util;

import java.io.IOException;
import java.util.function.Function;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GPS = 0;
    private Context mcontext;

    private LocationManager mLocationManager;

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("Location", location.toString());
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            Log.d("Location", ""  + lat + lon);
            Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=01897e497239c8aff78d9b8538fb24ea&units=metric&lang=fr").build();
            API.callApi(request, MainActivity.this::updateUi);
            binding.mainLayout.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.INVISIBLE);
            mLocationManager.removeUpdates(this);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //LocationListener.super.onStatusChanged(provider, status, extras);
        }
    };
    private ActivityMainBinding binding;

    private OkHttpClient mOkHttpClient;



    private Gson mGson;

    private City mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mOkHttpClient = new OkHttpClient();
        mGson = new Gson();
        mcontext = this;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // Test connectivity
        String lWelcomeMessage;
        if (Util.isActiveNetwork(mcontext)) {
            lWelcomeMessage = "Oui je suis connecté à internet";
            binding.cityName.setText(R.string.city_name);
            Log.d("TAG", "MainActivity: onCreate()");
        } else {
            updateViewNoConnection();
            lWelcomeMessage = "Non je ne suis pas connecté à internet";
        }
        Toast.makeText(this, lWelcomeMessage, Toast.LENGTH_SHORT).show();

        // Permissions GeoLocation
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] permissions = new String[] {
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            };
            ActivityCompat.requestPermissions(this, permissions , REQUEST_CODE_GPS);
        }else {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000,
                    10, mLocationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_GPS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(this , "OK", Toast.LENGTH_SHORT).show();
                } else {
                    // Permission Denied
                    Toast.makeText(this , "NOK", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    private Boolean updateUi(String Json) {
        runOnUiThread(() ->{
            CityGson city = mGson.fromJson(Json, CityGson.class);
            Log.d("TAG", "Meteo de " + city.getName());
            binding.cityName.setText(city.getName());
            binding.cityDesc.setText(city.getWeather().get(0).getDescription());
            binding.cityTemp.setText(city.getMain().getTemp().toString());
            binding.cityIcon.setImageResource(
                    Util.setWeatherIcon(
                            city.getWeather().get(0).getId(),
                            city.getSys().getSunrise(),
                            city.getSys().getSunset()
                    ));
        });
        return true;

    }

    public void updateViewNoConnection() {
        binding.currentTemperature.setVisibility(View.INVISIBLE);
        binding.buttontoFavorite.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("TAG", "MainActivity: onDestroy()");
    }


    public void onClickButton1(View v){
        Intent intent = new Intent(this, FavoriteActivity.class);
        String msg = binding.msgToActivity.getText().toString();
        intent.putExtra(Util.KEY_MESSAGE, msg);
        startActivity(intent);
    }


}
