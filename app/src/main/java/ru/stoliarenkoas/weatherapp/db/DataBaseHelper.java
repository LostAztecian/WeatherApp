package ru.stoliarenkoas.weatherapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "weather.db";
    private static final int DB_VERSION = 1;

    public static final String TABLE_CITIES = "cities";


    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final TableColumns[] columns = TableColumns.values();
        StringBuilder sb = new StringBuilder();
        sb.append(columns[0].columnName);
        sb.append(" INTEGER PRIMARY KEY, ");
        for (int i = 1; i < columns.length-1; i++) {
            sb.append(columns[i].columnName);
            sb. append(" TEXT, ");
        }
        sb.append(columns[columns.length-1].columnName);
        sb.append(" TEXT");
        final String request = String.format("CREATE TABLE %s (%s);", TABLE_CITIES, sb.toString());
        db.execSQL(request);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //You won't need to migrate if u never release .jpg
    }

}
