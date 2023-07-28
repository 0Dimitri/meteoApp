package com.dimitri.meteoapp.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.dimitri.meteoapp.models.City;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.function.Function;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class API {

    public API() {

    }

    public static void callApi(Request request, Function<String, Boolean> test)

    {
        final OkHttpClient mOkHttpClient;
        mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException, RuntimeException {
                if (response.isSuccessful()) {
                    final String stringJson = response.body().string();
                    Log.d("TAG", stringJson);
                    test.apply(stringJson);
                }
            }


        });
    }

}
