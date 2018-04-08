package pro.xite.dev.weatherwhenever.data;

import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import pro.xite.dev.weatherwhenever.Helpers;
import pro.xite.dev.weatherwhenever.data.ods.OdsResponse;

/**
 * Created by Roman Syrchin on 4/7/18.
 */
public class WebJsonDataProvider {

    final private static String TAG_TRACER = "WebJsonDataProvider";
    final public static String DATA_KEY = "WebJsonData";


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

    public void request(final String criteria, @NonNull final Handler callbackHandler) {
        new Thread() {
            public void run() {
                Log.d(TAG_TRACER, Helpers.getMethodName());
                JSONObject jsonObject = loadData(criteria);
                OdsResponse odsResponse = new Gson().fromJson(jsonObject.toString(), OdsResponse.class);
                Log.d(TAG_TRACER, "Data loaded");
                if(jsonObject != null) {
                    Message msgObj = callbackHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DATA_KEY, odsResponse);
                    msgObj.setData(bundle);
                    callbackHandler.sendMessage(msgObj);
                }
            }
        }.start();
    }

}
