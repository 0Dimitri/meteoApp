package com.dimitri.meteoapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dimitri.meteoapp.R;
import com.dimitri.meteoapp.models.CityGson;
import com.dimitri.meteoapp.utils.Util;

import java.util.ArrayList;

public class FavoriteAdapterGson extends RecyclerView.Adapter<FavoriteAdapterGson.ViewHolder> {

    private ArrayList<CityGson> mArrayListCities;
    private Context mContext;

    public FavoriteAdapterGson(Context context, ArrayList<CityGson> cities) {
        mContext = context;
        mArrayListCities = cities;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextViewCity;
        public ImageView mImageViewWeather;
        public TextView mTextViewTemperature;
        public TextView mTextViewDescription;

        public int position;

        public ViewHolder(View view) {
            super(view);

            view.setOnLongClickListener(mOnLongClickListener);
            view.setTag(this);

            mTextViewCity = (TextView) view.findViewById(R.id.favorite_city_name);
            mImageViewWeather = (ImageView) view.findViewById(R.id.favorite_city_icon);
            mTextViewTemperature = (TextView) view.findViewById(R.id.favorite_city_temp);
            mTextViewDescription = (TextView) view.findViewById(R.id.favorite_city_desc);
        }
    }

    @Override
    public FavoriteAdapterGson.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        CityGson city = mArrayListCities.get(position);

        holder.mTextViewCity.setText(city.getName());
        holder.mImageViewWeather.setImageResource(Util.setWeatherIcon(city.getWeather().get(0).getId(), city.getSys().getSunrise(),city.getSys().getSunset()));;
        holder.mTextViewTemperature.setText(city.getMain().getTemp().toString());
        holder.mTextViewDescription.setText(city.getWeather().get(0).getDescription());

        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return mArrayListCities.size();
    }


    private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {

            ViewHolder holder = (ViewHolder) v.getTag();
            final int position = holder.position;

            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setMessage("Supprimer " + holder.mTextViewCity.getText().toString() + " ?");
            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    mArrayListCities.remove(position);
                    notifyDataSetChanged();
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mArrayListCities.size());
                    Util.deleteCity(mContext, mArrayListCities);
                }
            });

            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });

            builder.create().show();
            return false;
        }
    };
}