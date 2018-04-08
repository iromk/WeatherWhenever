package pro.xite.dev.weatherwhenever.data.ods;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

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

    @Override
    protected URL getRequestUrl(String... criteria) {
        final StringBuilder getRequestUrl = new StringBuilder(ODS_RECORD_SEARCH_API_BASE_URL);
        getRequestUrl.append(String.format(Locale.getDefault(), GET_PARAMS_TEMPLATE,
                            ODS_DATASET_ID,
                            ODS_RESULT_ROWS_LIMIT,
                            getExtraGetParams()));

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

    protected String getExtraGetParams() {
        return ODS_EXTRA_GET_PARAMS;
    }

    public static void request(final String criteria, @NonNull final Handler callbackHandler) {
        final OdsCityProvider ods = OdsCityProvider.getInstance();
        new Thread() {
            public void run() {
                Log.d(TAG_TRACER, Helpers.getMethodName());
                JSONObject jsonObject = ods.loadData(criteria);
                Log.d(TAG_TRACER, "Data loaded");
                if(jsonObject != null) {
                    Message msgObj = callbackHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Weather.DATA_KEY, jsonObject.toString());
                    msgObj.setData(bundle);
                    callbackHandler.sendMessage(msgObj);
                }
            }
        }.start();
    }
    public static void request(String city, DataProviderListener activity) {
        request(city, new LeakSafeHandler<>(activity));
    }
    private static OdsCityProvider getInstance() {
        return new OdsCityProvider(); // it could be more sophisticated if needed.
    }
}
