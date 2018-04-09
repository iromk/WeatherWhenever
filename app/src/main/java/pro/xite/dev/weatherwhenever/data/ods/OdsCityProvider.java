package pro.xite.dev.weatherwhenever.data.ods;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.URL;
import java.util.Locale;

import pro.xite.dev.weatherwhenever.Helpers;
import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.WebJsonDataProvider;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecast;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;
import pro.xite.dev.weatherwhenever.manage.LeakSafeHandler;

/**
 * Created by Roman Syrchin on 4/8/18.
 */
public class OdsCityProvider extends WebJsonDataProvider {

    /**
     * OpenDataSoft API definitions
     */
    private static final String ODS_RECORD_SEARCH_API_BASE_URL =
            "https://data.opendatasoft.com/api/records/1.0/search/";

    private static final String GET_PARAMS_TEMPLATE = "?dataset=%s&rows=%d&%s";
    private static final String GET_CRITERIA_TEMPLATE = "&q=%s";

    private static final String ODS_DATASET_ID = "geonames-all-cities-with-a-population-1000@public";
    private static final int ODS_RESULT_ROWS_LIMIT = 8;
    private static final String ODS_EXTRA_GET_PARAMS = "&facet=country";

    /**
     * Response codes
     */
    private static final int RESPONSE_CODE_200 = 200;

    private static final String TAG_TRACER = "TRACER/ODS";
    private static final String TAG_TRACER_SERVICE = "ODS/SERVICE";
    private DataProviderListener listener;

    @Override
    protected URL getRequestUrl(String... criteria) {
        final StringBuilder getRequestUrl = new StringBuilder(ODS_RECORD_SEARCH_API_BASE_URL);
        getRequestUrl.append(String.format(Locale.getDefault(), GET_PARAMS_TEMPLATE,
                            ODS_DATASET_ID,
                            ODS_RESULT_ROWS_LIMIT,
                            ODS_EXTRA_GET_PARAMS));

        if(criteria.length > 0) {
            getRequestUrl.append(String.format(GET_CRITERIA_TEMPLATE, criteria[0]));
        } else {
            Log.d(TAG_TRACER, "getRequestUrl: no criteria were specified, result will be unpredictable. You warned :P");
        }

        try {
            return new URL(getRequestUrl.toString());

        } catch (Exception e) {
            Log.d(TAG_TRACER, String.format("getRequestUrl: malformed url string [%s]", getRequestUrl.toString()));
            return null;
        }
    }

    public static void request(String city, DataProviderListener activity) {
        getInstance().request(city, new LeakSafeHandler<>(activity));
    }

    public void setListener(DataProviderListener listener) {
        this.listener = listener;
    }

    public void suggest(String city) {
        Log.d(TAG_TRACER, Helpers.getMethodName());
        request(city, new LeakSafeHandler<>(listener));
    }

    private static WebJsonDataProvider getInstance() {
        return new OdsCityProvider(); // it could be more sophisticated when needed.
    }

    private final IBinder binder = new DataProviderBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
        return binder;
    }

    public class DataProviderBinder extends Binder {
        public OdsCityProvider getDataProviderService() {
            Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
            return OdsCityProvider.this;
        }
    }

}
