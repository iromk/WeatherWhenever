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

import pro.xite.dev.weatherwhenever.Forecast;
import pro.xite.dev.weatherwhenever.Helpers;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

abstract public class OWMDataProvider {

    /**
     * OWM API definitions
     */
    private static final String OWM_API_KEY = "62a13a0ac59c620b10c5f3680ec685f0";
    private static final String OWM_API_BASE =
            "http://api.openweathermap.org/data/2.5/%s?appid=%s&q=%s%s";
    private static final String OWM_EXTRA_GET_PARAMS = "&units=metric";


    /**
     * OWM Json keys
     */
    private static final String RESPONSE_JSON_KEY = "cod";

    /**
     * Response codes
     */
    private static final int HTTP_CODE_200 = 200;

    private static final String TAG_TRACER = "TRACER-OWM<>";

    abstract protected String getApiAction();
    protected String getExtraGetParams() { return OWM_EXTRA_GET_PARAMS; }

    protected <T> T loadJSONData(String city, Class<T> owmObjectClass) {

        try {

            URL url = new URL(String.format(OWM_API_BASE, getApiAction(), OWM_API_KEY, city, getExtraGetParams()));
            Log.d(Helpers.getMethodName(), url.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE_JSON_KEY) != HTTP_CODE_200) {
                return null;
            }

            T owmObject = new Gson().fromJson(jsonObject.toString(), owmObjectClass);

            return owmObject;
        } catch (Exception e) {
            return null;
        }
    }

    public <T extends Serializable> void loadForecast(final String city, final Class<T> d, final Handler handler) {
        Log.d(TAG_TRACER, Helpers.getMethodName());

        final Forecast owmForecast = new Forecast(city, "no data", "do whatever you want");

        new Thread() {
            public void run() {
                T forecast = loadJSONData(city, d);
                if(forecast != null) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putSerializable("fo", forecast);
                    msgObj.setData(b);
                    Log.d(TAG_TRACER, "sending <> the message");
                    handler.sendMessage(msgObj);
                }
            }
        }.start();
    }



}
