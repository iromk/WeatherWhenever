package pro.xite.dev.weatherwhenever.manage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

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
    final private String LOG_TAG = "DB";

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

    /**
     * Adds new record to the database.
     * Can be initiated if needed only by public method updateData().
     * @param city
     * @param weather
     * @param forecast
     */
    private void addData(final Wherever city, final Weather weather, final Whenever forecast) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CITY_NAME, city.getName());
        values.put(DatabaseHelper.COLUMN_CITY, encrypt(serialize(city)));
        values.put(DatabaseHelper.COLUMN_WEATHER, encrypt(serialize(weather)));
        values.put(DatabaseHelper.COLUMN_FORECAST, encrypt(serialize(forecast)));
        database.insert(DatabaseHelper.TABLE_WEATHER_INFO, null, values);
    }

    /**
     * Updates data if there is already a record for a given city
     * or adds as a new record.
     * @param city
     * @param weather
     * @param forecast
     */
    public void updateData(final Wherever city, final Weather weather, final Whenever forecast) {
        final long rowId = getCityId(city.getName());
        if(rowId > 0) { // true update
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_CITY, encrypt(serialize(city)));
            values.put(DatabaseHelper.COLUMN_WEATHER, encrypt(serialize(weather)));
            values.put(DatabaseHelper.COLUMN_FORECAST, encrypt(serialize(forecast)));
            database.update(DatabaseHelper.TABLE_WEATHER_INFO,
                    values,
                    DatabaseHelper.COLUMN_ID + "=" + rowId,
                    null
                    );
            Log.d(LOG_TAG, "updateData: data updated");
        } else { // add
            addData(city, weather, forecast);
            Log.d(LOG_TAG, "updateData: data added");
        }
    }

    /**
     * Returns _id value for first found city_name or -1 if cityName not present.
     * @param cityName
     * @return record id or -1
     */
    private long getCityId(final String cityName) {

        final String[] cityNameColumn = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_CITY_NAME};
        final String[] args = { cityName };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_WEATHER_INFO, // from
                cityNameColumn, // columns
                DatabaseHelper.COLUMN_CITY_NAME + "=?", args, // city_name=cityName
                null, null, null );

        long rowId = -1;

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            rowId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
        }

        cursor.close();

        return rowId;
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
