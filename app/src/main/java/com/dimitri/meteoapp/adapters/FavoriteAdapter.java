package com.dimitri.meteoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.meteoapp.R;

import com.dimitri.meteoapp.models.City;
import com.dimitri.meteoapp.models.CityGson;
import com.dimitri.meteoapp.utils.Util;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {


    private Context mContext;
    private ArrayList<CityGson> mCities;

    public FavoriteAdapter(Context mContext, ArrayList<CityGson> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CityGson city = mCities.get(position);
        holder.mTextViewCityName.setText(city.getName());
        holder.mTextViewCityDesc.setText(city.getWeather().get(0).getDescription());
        holder.mTextViewCityTemp.setText(city.getMain().getTemp().toString());
        holder.mImageViewIcon.setImageResource(Util.setWeatherIcon(city.getWeather().get(0).getId(), city.getSys().getSunrise(), city.getSys().getSunset()));

    }

    @Override
    public int getItemCount() {
        return mCities.size();
    }

    // Classe holder qui contient la vue d’un item
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewCityName;
        public TextView mTextViewCityTemp;
        public TextView mTextViewCityDesc;
        public ImageView mImageViewIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextViewCityName = (TextView) itemView.findViewById(R.id.favorite_city_name);
            this.mTextViewCityTemp = (TextView) itemView.findViewById(R.id.favorite_city_temp);
            this.mTextViewCityDesc = (TextView) itemView.findViewById(R.id.favorite_city_desc);
            this.mImageViewIcon = (ImageView)  itemView.findViewById(R.id.favorite_city_icon);
        }
    }
}