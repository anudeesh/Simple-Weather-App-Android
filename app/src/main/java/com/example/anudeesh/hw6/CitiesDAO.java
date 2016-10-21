package com.example.anudeesh.hw6;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anudeesh on 10/18/2016.
 */
public class CitiesDAO {
    private SQLiteDatabase db;

    public CitiesDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(Cities city) {
        ContentValues values = new ContentValues();
        values.put(CitiesTable.COLUMN_city,city.getCity());
        values.put(CitiesTable.COLUMN_country,city.getCountry());
        values.put(CitiesTable.COLUMN_temp,city.getTemperature());
        values.put(CitiesTable.COLUMN_fav,city.getFavorite());
        return db.insert(CitiesTable.TABLENAME,null,values);
    }

    public boolean update(Cities city) {
        ContentValues values = new ContentValues();
        values.put(CitiesTable.COLUMN_city,city.getCity());
        values.put(CitiesTable.COLUMN_country,city.getCountry());
        values.put(CitiesTable.COLUMN_temp,city.getTemperature());
        values.put(CitiesTable.COLUMN_fav,city.getFavorite());
        return db.update(CitiesTable.TABLENAME,values,CitiesTable.COLUMN_city + "=? AND " +
                CitiesTable.COLUMN_country + "=?",new String[]{city.getCity(),city.getCountry()})>0;
    }

    public boolean delete(Cities city) {
        return db.delete(CitiesTable.TABLENAME,CitiesTable.COLUMN_city + "=? AND " +
                CitiesTable.COLUMN_country + "=?",new String[]{city.getCity(),city.getCountry()})>0;
    }

    public Cities get(String city,String country) {
        Cities c = null;
        Cursor cursor = db.query(true,CitiesTable.TABLENAME,
                new String[]{CitiesTable.COLUMN_city,CitiesTable.COLUMN_country,CitiesTable.COLUMN_temp,CitiesTable.COLUMN_fav},
                CitiesTable.COLUMN_city + "=? AND " + CitiesTable.COLUMN_country + "=?",new String[]{city,country},
                null,null,null,null);
        if(cursor!=null && cursor.moveToFirst()) {
            c = buildCityFromCursor(cursor);
            if(!cursor.isClosed()) {
                cursor.close();
            }
        }
        return c;
    }

    public List<Cities> getAll() {
        List<Cities> list = new ArrayList<Cities>();
        Cursor cursor = db.query(CitiesTable.TABLENAME,
                new String[]{CitiesTable.COLUMN_city,CitiesTable.COLUMN_country,CitiesTable.COLUMN_temp,CitiesTable.COLUMN_fav},
                null,null,null,null,CitiesTable.COLUMN_fav+" DESC");
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                Cities city = buildCityFromCursor(cursor);
                if (city!=null) {
                    list.add(city);
                }
            } while (cursor.moveToNext());
            if(!cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    private Cities buildCityFromCursor(Cursor c) {
        Cities city = null;
        if (c!=null) {
            city = new Cities();
            city.setCity(c.getString(0));
            city.setCountry(c.getString(1));
            city.setTemperature(c.getString(2));
            city.setFavorite(c.getString(3));
        }
        return city;
    }
}
