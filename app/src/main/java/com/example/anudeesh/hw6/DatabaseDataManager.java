package com.example.anudeesh.hw6;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * Created by Anudeesh on 10/18/2016.
 */
public class DatabaseDataManager {
    private Context mcontext;
    private DatabaseOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private CitiesDAO cityDAO;

    public DatabaseDataManager(Context mcontext) {
        this.mcontext = mcontext;
        dbOpenHelper = new DatabaseOpenHelper(this.mcontext);
        db = dbOpenHelper.getWritableDatabase();
        cityDAO = new CitiesDAO(db);
    }

    public void close() {
        if(db!=null) {
            db.close();
        }
    }
    public CitiesDAO getCityDAO() {
        return this.cityDAO;
    }
    public long saveCity(Cities city) {
        return this.cityDAO.save(city);
    }
    public boolean updateCity(Cities city) {
        return this.cityDAO.update(city);
    }
    public boolean deleteCity(Cities city) {
        return this.cityDAO.delete(city);
    }
    public Cities getCity(String city, String country) {
        return this.cityDAO.get(city, country);
    }
    public List<Cities> getAllCities() {
        return this.cityDAO.getAll();
    }
}
