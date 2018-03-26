package pro.xite.dev.weatherwhenever;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Function;

import android.os.Handler;

import com.google.gson.Gson;

/**
 * Provides weather and forecast info from openweathermap.org.
 */
public class OWMForecastProvider {

    /**
     * OWM API definitions
     */
    private static final String OWM_API_KEY = "62a13a0ac59c620b10c5f3680ec685f0";
    private static final String OWM_API = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=%s&appid=%s";


    /**
     * OWM Json keys
     */
    private static final String RESPONSE_JSON_KEY = "cod";

    /**
     * Response codes
     */
    private static final int HTTP_CODE_200 = 200;


    private static final String TAG_TRACER = "TRACER-OWM";


    public static void loadForecast(String city, final Handler handler) {
        Log.d(TAG_TRACER, Helpers.getMethodName());

        final Forecast owmForecast = new Forecast(city, "no data", "do whatever you want");

        new Thread() {
            public void run() {
                Forecast forecast = OWMForecastProvider.getJSONData("London");
                if(forecast != null) {
                    Message msgObj = handler.obtainMessage();
                    Bundle b = new Bundle();
                    b.putSerializable("fo", forecast);
                    msgObj.setData(b);
                    Log.d(TAG_TRACER, "sending the message");
                    handler.sendMessage(msgObj);
                }
            }
        }.start();
    }

    private static Forecast getJSONData(String city) {


        try {

            URL url = new URL(String.format(OWM_API, city, OWM_API_KEY));
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

            Forecast forecast = new Gson().fromJson(jsonObject.toString(), Forecast.class);

            return forecast;
        } catch (Exception e) {
            return null;
        }
    }

}
