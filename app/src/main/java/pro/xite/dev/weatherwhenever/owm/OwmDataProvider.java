package pro.xite.dev.weatherwhenever.owm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import pro.xite.dev.weatherwhenever.Helpers;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

abstract public class OwmDataProvider {

    /**
     * OWM API definitions
     */
    private static final String OWM_API_KEY = "62a13a0ac59c620b10c5f3680ec685f0";
    private static final String OWM_API_BASE_URL =
            "http://api.openweathermap.org/data/2.5/%s?appid=%s&q=%s%s";
    private static final String OWM_EXTRA_GET_PARAMS = "&units=metric";


    /**
     * OWM Json keys
     */
    private static final String RESPONSE_CODE_JSON_KEY = "cod";

    /**
     * Response codes
     */
    private static final int RESPONSE_CODE_200 = 200;

    private static final String TAG_TRACER = "TRACER/OWMDP";

    public static final String OWM_DATA_KEY = "owmData";

    abstract protected String getApiAction();
    protected String getExtraGetParams() { return OWM_EXTRA_GET_PARAMS; }

    protected <T> T loadData(String city, Class<T> owmObjectClass) {

        try {

            URL url = new URL(String.format(OWM_API_BASE_URL, getApiAction(), OWM_API_KEY, city, getExtraGetParams()));
            Log.d(Helpers.getMethodName(), url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                rawData.append(nextLine);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE_CODE_JSON_KEY) != RESPONSE_CODE_200) {
                Log.d(TAG_TRACER, String.format("Json response code: %d", jsonObject.getInt(RESPONSE_CODE_JSON_KEY)));
                return null;
            }

            return new Gson().fromJson(jsonObject.toString(), owmObjectClass);

        } catch (Exception e) {
            Log.d(TAG_TRACER, Helpers.getMethodName() + " failed:\n  " + e.toString());
            return null;
        }
    }

    public <T extends Serializable> void requestOWM(final String city, final Class<T> d, final Handler handler) {

        Log.d(TAG_TRACER, Helpers.getMethodName());

        new Thread() {
            public void run() {
                Log.d(TAG_TRACER, Helpers.getMethodName());
                T owmData = loadData(city, d);
                Log.d(TAG_TRACER, "Data loaded");
                if(owmData != null) {
                    Message msgObj = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OWM_DATA_KEY, owmData);
                    msgObj.setData(bundle);
                    handler.sendMessage(msgObj);
                }
            }
        }.start();
    }



}
