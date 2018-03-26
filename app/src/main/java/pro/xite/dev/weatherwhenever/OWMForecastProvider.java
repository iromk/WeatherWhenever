package pro.xite.dev.weatherwhenever;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Provides weather and forecast info from openweathermap.org.
 */
public class OWMForecastProvider {

    private static final String OWM_API_KEY = "62a13a0ac59c620b10c5f3680ec685f0";
    private static final String OWM_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    private static final String KEY = "x-api-key";

    private static final String NEW_LINE = "\n";

    private static final String RESPONSE_JSON_KEY = "cod";
    private static final int HTTP_CODE_200 = 200;

    public static final String TAG_TRACER = "TRACER-OWM";

    static JSONObject getJSONData(String city) {


        try {

            Log.d(TAG_TRACER, "1");
            URL url = new URL(String.format(OWM_API, city, OWM_API_KEY));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Log.d(TAG_TRACER, "2");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable);
            }
            reader.close();

            Log.d(TAG_TRACER, "3");

            JSONObject jsonObject = new JSONObject(rawData.toString());
            if (jsonObject.getInt(RESPONSE_JSON_KEY) != HTTP_CODE_200) {
                return null;
            }
            return jsonObject;
        } catch (Exception e) {
            return null;
        }
    }

}
