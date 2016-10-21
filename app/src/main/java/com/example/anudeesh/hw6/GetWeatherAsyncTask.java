package com.example.anudeesh.hw6;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Anudeesh on 10/19/2016.
 */
public class GetWeatherAsyncTask extends AsyncTask<String, Void, ArrayList<Weather>> {
    CityWeatherActivity activity;
    ProgressDialog progressDialog;

    public GetWeatherAsyncTask(CityWeatherActivity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<Weather> doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int statuscode = con.getResponseCode();

            if(statuscode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return WeatherUtil.WeatherPullParser.parseWeatherDetails(in);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Loading Data");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<Weather> weathers) {
        super.onPostExecute(weathers);
        progressDialog.dismiss();
        //Log.d("demo",weathers.get(0).toString());
        activity.onTaskCompleted(weathers);
    }
}
