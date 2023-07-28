package com.dimitri.meteoapp.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dimitri.meteoapp.R;
import com.dimitri.meteoapp.adapters.FavoriteAdapter;


import com.dimitri.meteoapp.adapters.FavoriteAdapterGson;
import com.dimitri.meteoapp.databinding.ActivityFavoriteBinding;
import com.dimitri.meteoapp.models.City;
import com.dimitri.meteoapp.models.CityGson;
import com.dimitri.meteoapp.utils.API;
import com.dimitri.meteoapp.utils.Util;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;

import okhttp3.Request;


public class FavoriteActivity extends AppCompatActivity {

    private ActivityFavoriteBinding binding;

    private Context mContext;

    private FavoriteAdapter mAdapter;
    private FavoriteAdapterGson mAdapterGson;

    private Gson mGson;
    public ArrayList<String> mCitiesName = new ArrayList<>();
    ArrayList<CityGson> mCitiesGson = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext =this;

        mGson = new Gson();
        Log.d("TAG", "FavoriteActivity: onCreate()");

        String msg = getIntent().getStringExtra(Util.KEY_MESSAGE);

        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.msg.setText(msg);
        setSupportActionBar(binding.toolbar);
        binding.toolbarLayout.setTitle(getTitle());

        String appid = "01897e497239c8aff78d9b8538fb24ea";
        Log.d("citiesName", mCitiesName.toString());
        mCitiesGson = Util.initFavoriteCities(mContext);

        binding.fab.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v = LayoutInflater.from(mContext).inflate(R.layout.modal_add_city, null);
            final EditText editTextCity = (EditText) v.findViewById(R.id.search_city);
            builder.setView(v);
            builder.setMessage("Search a city by name");
            builder.setPositiveButton("OK", (d, i) -> {
                String cityEntered = editTextCity.getText().toString();
                Log.d("TAG", "cityEntered: " + cityEntered);
                String url = "https://api.openweathermap.org/data/2.5/weather?q=" + cityEntered + "&appid=" + appid + "&units=metric&lang=fr";
                Request request = new Request.Builder().url(url).build();
                API.callApi(request, this::updateUi);
            });
            builder.create().show();
        });
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        RecyclerView mRecyclerView = binding.favoriteInclude.myRecyclerView;
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapterGson = new FavoriteAdapterGson(mContext, mCitiesGson);
        mRecyclerView.setAdapter(mAdapterGson);
    }

    private Boolean updateUi(String json) {
        CityGson city = mGson.fromJson(json, CityGson.class);
        Log.d("citylogged", city.toString());
        runOnUiThread(() ->{
            //CityGson city = mGson.fromJson(json, CityGson.class);
            Log.d("citylogged", city.toString());
            Toast.makeText(mContext, "kjbjihvgf", Toast.LENGTH_SHORT).show();
            if (city == null){
                Toast.makeText(mContext, "Not found", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "City added in BDD", Toast.LENGTH_SHORT).show();
                ArrayList<CityGson> cities = Util.initFavoriteCities(mContext);
                cities.add(city);
                Util.saveFavouriteCities(mContext, cities);
                mCitiesGson.add(city);
                mAdapterGson.notifyDataSetChanged();
            }
        });
        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d("TAG", "FavoriteActivity: onDestroy()");
    }

}