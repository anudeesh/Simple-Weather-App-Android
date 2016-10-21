package com.example.anudeesh.hw6;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Anudeesh on 10/18/2016.
 */
public class CitiesTable {
    static final String TABLENAME = "cities";
    static final String COLUMN_city = "city";
    static final String COLUMN_country = "country";
    static final String COLUMN_temp = "temperature";
    static final String COLUMN_fav = "favorite";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE "+ TABLENAME +"(");
        sb.append(COLUMN_city +" TEXT, ");
        sb.append(COLUMN_country +" TEXT NOT NULL, ");
        sb.append(COLUMN_temp +" TEXT NOT NULL, ");
        sb.append(COLUMN_fav +" TEXT NOT NULL, ");
        sb.append("PRIMARY KEY ("+COLUMN_city+","+COLUMN_country+"));");

        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLENAME);
        CitiesTable.onCreate(db);
    }
}
