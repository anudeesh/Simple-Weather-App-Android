package com.example.anudeesh.hw6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class CityWeatherActivity extends AppCompatActivity {

    RecyclerView recyclerView,recyclerView1;
    RecyclerView.Adapter adapter,adapter1;
    RecyclerView.LayoutManager layoutManager,layoutManager1;
    TextView dailyTitle;
    DatabaseDataManager dm;
    String city_val,country_val,dbtemp,dbfav;
    String tempUnits;
    ArrayList<Weather> listUnits;
    ArrayList<Weather> listUnits1;
    ArrayList<Integer> index;
    ArrayList<String> average;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        dailyTitle = (TextView) findViewById(R.id.textViewDailyTitle);
        dm = new DatabaseDataManager(this);

        city_val = getIntent().getStringExtra("city");
        country_val = getIntent().getStringExtra("country");

        dailyTitle.setText("Daily Forecast for "+city_val+", "+country_val.toUpperCase());
        if(isConnectedOnline()) {
            new GetWeatherAsyncTask(this).execute("http://api.openweathermap.org/data/2.5/forecast?q="+city_val+","+country_val+"&mode=xml&appid=cc64b3fb3cb31424f6be6ae667a06d88");
        } else {
            Toast.makeText(CityWeatherActivity.this,"Cannot access internet",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options1,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Cities city = dm.getCity(city_val,country_val);
        if(item.getItemId() == R.id.settingsCity) {
            Intent intent = new Intent(CityWeatherActivity.this,Settings.class);
            startActivityForResult(intent,2);
        }
        else if(item.getItemId() == R.id.saveCity) {
            if(city == null) {
                dm.saveCity(new Cities(city_val, country_val, dbtemp, "false"));
                Toast.makeText(CityWeatherActivity.this,"City Saved",Toast.LENGTH_SHORT).show();
            } else {
                city.setTemperature(dbtemp);
                dm.updateCity(city);
                Toast.makeText(CityWeatherActivity.this,"City Updated",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            if(resultCode== Activity.RESULT_OK) {
                loadHourlyView(listUnits);
                loaddailyView(listUnits1,average);
                Toast.makeText(CityWeatherActivity.this,"Temperature Unit has been changed to °"+tempUnits,Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        else {
            return false;
        }
    }

    public void onTaskCompleted(ArrayList<Weather> wList) {
        listUnits = wList;
        loadHourlyView(wList);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnits = prefs.getString("PREF_UNIT","C");

        //Log.d("demo",tempUnits);
        ArrayList<Weather> dailyList = new ArrayList<Weather>();
        int count=0;
        index = new ArrayList<Integer>();
        average = new ArrayList<String>();
        while(count<wList.size()) {
            ArrayList<Weather> temp = new ArrayList<Weather>();
            index.add(count);
            String d = wList.get(count).getTime().split("T")[0];
            double t = 0;
            while (count<wList.size()) {
                String d1 = wList.get(count).getTime().split("T")[0];
                if (d.equals(d1)) {
                    temp.add(wList.get(count));
                    t=t+Double.parseDouble(wList.get(count).getTemperature());
                } else {
                    int len = temp.size();
                    double x = t/len;
                    BigDecimal bd = new BigDecimal(x);
                    bd = bd.setScale(2, RoundingMode.HALF_UP);
                    String avg = bd.doubleValue()+"";
                    average.add(avg);
                    Weather w1 = null;
                    if(len%2==0) {
                        w1=temp.get(len/2);
                        /*temp.get(len/2).setTemperature(avg);
                        dailyList.add(temp.get(len/2));*/
                    } else {
                        w1=temp.get((len-1)/2);
                        /*temp.get((len-1)/2).setTemperature(avg);
                        dailyList.add(temp.get((len-1)/2));*/
                    }
                    //w1.setTemperature(avg);
                    dailyList.add(w1);
                    temp.clear();
                    break;
                }
                count++;
            }
        }

        /*for (int i =0;i<dailyList.size();i++) {
            dailyList.get(i).setTemperature(average.get(i));
        }*/

        dbtemp = dailyList.get(0).getTemperature();
        listUnits1=dailyList;
        loaddailyView(dailyList,average);
    }

    public void loadHourlyView(ArrayList<Weather> wList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnits = prefs.getString("PREF_UNIT","C");
        recyclerView = (RecyclerView) findViewById(R.id.viewHourlyWeather);
        adapter = new HourlyRecyclerAdapter(CityWeatherActivity.this,wList,tempUnits);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(CityWeatherActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void loaddailyView(ArrayList<Weather> dList,ArrayList<String> avgs) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnits = prefs.getString("PREF_UNIT","C");
        recyclerView1 = (RecyclerView) findViewById(R.id.viewDailyWeather);
        adapter1 = new DailyRecyclerAdapter(CityWeatherActivity.this,dList,tempUnits,avgs);
        recyclerView1.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(CityWeatherActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setAdapter(adapter1);

        recyclerView1.addOnItemTouchListener(
                new DailyRecyclerAdapter(CityWeatherActivity.this, new DailyRecyclerAdapter.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        recyclerView.smoothScrollToPosition(index.get(position));
                    }
                })
        );
    }
}
