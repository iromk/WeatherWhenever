package pro.xite.dev.weatherwhenever.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Roman Syrchin on 4/2/18.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "weatherwhenever.db";
    static final String WEATHER_INFO = "weather_info";
    private static final int DATABASE_VERSION = 1;

    // db columns
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY_NAME = "city_name";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_WEATHER = "weather";
    public static final String COLUMN_FORECAST = "forecast";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE notes (" +
                " " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                " " + COLUMN_CITY_NAME + " TEXT," +
                " " + COLUMN_CITY + " TEXT," +
                " " + COLUMN_WEATHER + " TEXT," +
                " " + COLUMN_FORECAST + " TEXT" +
                ");"
        );
    }

    /** just recreate the database */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WEATHER_INFO);
        onCreate(db);
    }
}
