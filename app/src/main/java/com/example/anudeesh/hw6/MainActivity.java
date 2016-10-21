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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{
    DatabaseDataManager dm;
    EditText en_city,en_country;
    Button buttonSearch;
    ArrayList<Cities> saveList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    String tempUnits;
    TextView savedLabel,noCitiesText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        savedLabel = (TextView) findViewById(R.id.textViewSaved);
        noCitiesText = (TextView) findViewById(R.id.textViewNoCities);

        dm = new DatabaseDataManager(this);
        en_city = (EditText) findViewById(R.id.editTextCity);
        en_country = (EditText) findViewById(R.id.editTextCountry);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);

        checkData();

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cival = en_city.getText().toString();
                String coval = en_country.getText().toString();

                if(cival.isEmpty() || coval.isEmpty()) {
                    Toast.makeText(MainActivity.this,"Enter both city and country..",Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this,CityWeatherActivity.class);
                    intent.putExtra("city",cival);
                    intent.putExtra("country",coval);
                    startActivity(intent);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        dm.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this,Settings.class);
            startActivityForResult(intent,1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1) {
            if(resultCode== Activity.RESULT_OK) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                tempUnits = prefs.getString("PREF_UNIT","C");
                Toast.makeText(MainActivity.this,"Temperature Unit has been changed to °"+tempUnits,Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        checkData();
    }

    public void loadSavedView(ArrayList<Cities> cList) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tempUnits = prefs.getString("PREF_UNIT","C");
        recyclerView = (RecyclerView) findViewById(R.id.viewSavedCities);
        adapter = new SavedCitiesRecyclerAdapter(MainActivity.this,cList,tempUnits);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MainActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void checkData() {
        saveList = (ArrayList<Cities>) dm.getAllCities();
        if(saveList.size()!=0) {
            savedLabel.setVisibility(View.VISIBLE);
            noCitiesText.setVisibility(View.INVISIBLE);
            loadSavedView(saveList);
        } else {
            savedLabel.setVisibility(View.INVISIBLE);
            noCitiesText.setVisibility(View.VISIBLE);
        }
    }
}
