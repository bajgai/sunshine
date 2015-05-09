package com.niranjanbajgai.sunshine.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by niranjanbajgai on 2015-05-07.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

    public static final String LOG_TAG = FetchWeatherTask.class.getSimpleName();


    private static final String MODE = "mode";
    private static final String UNITS = "units";
    private static final String DAYS = "cnt";


    public String uriBuilder(String postal_code){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("api.openweathermap.org")
                .appendPath("data")
                .appendPath("2.5")
                .appendPath("forecast")
                .appendPath("daily")
                .appendQueryParameter("q", postal_code)
                .appendQueryParameter(MODE, "json")
                .appendQueryParameter(UNITS, "metric")
                .appendQueryParameter(DAYS, "7").build();
        String url = builder.toString();
        Log.d(LOG_TAG, url);
        return url;

    }
    @Override
    protected Void doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String forecastJsonStr = null;

        try{
            URL url = new URL(uriBuilder(params[0]));

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while((line = reader.readLine())!= null){
                buffer.append(line + "\n");
            }
            if(buffer.length() == 0){
                return null;

            }
            forecastJsonStr = buffer.toString();
        }
        catch (IOException ioe){
            Log.e(LOG_TAG,"Error", ioe);
            return null;
        }
        finally{
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if (reader != null){
                try{
                    reader.close();
                }
                catch (IOException ioe){
                    Log.e(LOG_TAG, "Error closing the stream", ioe);
                }
            }
        }
        return null;

    }
}
