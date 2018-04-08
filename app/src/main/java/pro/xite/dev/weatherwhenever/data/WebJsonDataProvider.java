package pro.xite.dev.weatherwhenever.data;

import android.net.http.HttpResponseCache;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pro.xite.dev.weatherwhenever.Helpers;

/**
 * Created by Roman Syrchin on 4/7/18.
 */
public class WebJsonDataProvider {


    protected JSONObject loadData(String... criteria) {

        try {

            URL url = getRequestUrl(criteria);

            Log.d(Helpers.getMethodName(), url.toString());

            HttpResponseCache cache = HttpResponseCache.getInstalled();
            Log.d("CAHCE", String.format("requests %d", cache.getRequestCount()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                rawData.append(nextLine);
            }
            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());

            return jsonObject;

        } catch (Exception e) {
            return null;
        }
     }

     protected URL getRequestUrl(String... criteria) {
         return null;
     }

}
