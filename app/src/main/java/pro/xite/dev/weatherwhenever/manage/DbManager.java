package pro.xite.dev.weatherwhenever.manage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Wherever;

/**
 * Created by Roman Syrchin on 4/2/18.
 */
public class DbManager {

    private static final String TAG_CLASS_NAME = "TAG_CLASS_NAME";
    private static final String TAG_OBJECT_CONTENT = "TAG_OBJECT_CONTENT";

    private static final String CIPHER = "Ave, Imperator, morituri te salutant";

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
            if (!city.toString().equals(deserialize(decrypt(encrypt(serialize(city)))).toString())) throw new AssertionError();
        } else { // add
            addData(city, weather, forecast);
            removeObsoleteRows();
            Log.d(LOG_TAG, "updateData: data added");
        }
    }

    private void removeObsoleteRows() { // actually only the first row
        Cursor cursor = database.query(
                DatabaseHelper.TABLE_WEATHER_INFO,
                new String[] { DatabaseHelper.COLUMN_ID },
                null, null, null, null, null);
        if(cursor.getCount() > RecentCitiesList.MAX_RECENT_CITIES) {
            cursor.moveToFirst();
            final long beyondLastOne = cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            database.delete(
                    DatabaseHelper.TABLE_WEATHER_INFO,
                    DatabaseHelper.COLUMN_ID + "=?",
                    new String[]{String.valueOf(beyondLastOne)}
            );
        }
        cursor.close();
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

    public RecentCitiesList loadRecentCitiesList() {
        final RecentCitiesList citiesList = new RecentCitiesList();

        final String[] cityNameColumn = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_CITY_NAME,
                DatabaseHelper.COLUMN_CITY,
                DatabaseHelper.COLUMN_WEATHER,
                DatabaseHelper.COLUMN_FORECAST,
            };

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_WEATHER_INFO, // from
                cityNameColumn, // columns
                null,
                null,null, null, null );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Wherever city = deserialize(decrypt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY))));
            Weather weather = deserialize(decrypt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_WEATHER))));
            Whenever forecast = deserialize(decrypt(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FORECAST))));
            citiesList.addUnique(city, weather, forecast);
            cursor.moveToNext();
        }

        cursor.close();

        return citiesList;
    }

    /**          * ∆
     * Maaagic..  \O_
     * @param object
     * @param <T>
     * @return
     */
    private <T> String serialize(T object) {
        final JsonElement objectJsonTree = new Gson().toJsonTree(object);
        final JsonObject jsonMeta = new JsonObject();
        jsonMeta.addProperty(TAG_CLASS_NAME, object.getClass().getName());
        jsonMeta.add(TAG_OBJECT_CONTENT, objectJsonTree);
        return jsonMeta.toString();
    }

    /**
     * Dispell magic :)
     * @param jsonString
     * @param <T>
     * @return
     */
    private <T> T deserialize(String jsonString) {
        final JsonParser parser = new JsonParser();
        final JsonObject jsonObject = parser.parse(jsonString).getAsJsonObject();
        String className = jsonObject.get(TAG_CLASS_NAME).getAsJsonPrimitive().getAsString();
        Class klass = getClassByString(className);
        return (T) new Gson().fromJson(jsonObject.get(TAG_OBJECT_CONTENT), klass);
    }

    /**
     *
     * @param plain
     * @return
     */
    private String encrypt(String plain) {
        final int half = plain.length() / 2 + plain.length() % 2;
        final String half0 = plain.substring(0, half);
        final String half1 = plain.substring(half);
        final String shuffled = half1 + half0;

        final char[] encrypted = shuffled.toCharArray();
        final char[] key = CIPHER.toCharArray();
        final int cipherLength = key.length;

        for(int i = 0; i < encrypted.length; i++)
            encrypted[i] += key[i % cipherLength];
        return String.copyValueOf(encrypted);
    }

    /**
     *
     */
    private String decrypt(String encrypted) {
        final char[] plain = encrypted.toCharArray();
        final char[] key = CIPHER.toCharArray();
        final int cipherLength = key.length;

        for(int i = 0; i < plain.length; i++)
            plain[i] -= key[i % cipherLength];

        final String plainString = String.copyValueOf(plain);
        final int half = plainString.length() / 2;
        final String half0 = plainString.substring(0, half);
        final String half1 = plainString.substring(half);
        return half1 + half0;
    }

    private Class getClassByString(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e("InterfaceAdapter", e.getLocalizedMessage());
            throw new JsonParseException(e.getMessage());
        }
    }


}
