package pro.xite.dev.weatherwhenever.manage;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.db.DatabaseHelper;

/**
 * Created by Roman Syrchin on 4/2/18.
 */
public class DbManager {

    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DbManager(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
        dbHelper.close();
    }

    public void addData(final Wherever city, final Weather weather, final Whenever forecast) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CITY_NAME, city.getName());
        values.put(DatabaseHelper.COLUMN_CITY, encrypt(serialize(city)));
        values.put(DatabaseHelper.COLUMN_WEATHER, encrypt(serialize(weather)));
        values.put(DatabaseHelper.COLUMN_FORECAST, encrypt(serialize(forecast)));
        database.insert(DatabaseHelper.TABLE_WEATHER_INFO, null, values);
    }

    public void updateData(final Wherever city, final Weather weather, final Whenever forecast) {

    }

    private List<String> getCityList() {
        final List<String> returnValue = new ArrayList<>();

        
        return returnValue;
    }

    private <T> String serialize(T object) {
        return new Gson().toJson(object);
    }

    // TODO encryption
    /** does noting for now */
    private String encrypt(String plain) {
        return plain;
    }


}
