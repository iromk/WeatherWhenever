package pro.xite.dev.weatherwhenever.data;

import android.app.Service;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import pro.xite.dev.weatherwhenever.Helpers;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;
import pro.xite.dev.weatherwhenever.manage.LeakSafeHandler;

/**
 * Created by Roman Syrchin on 4/7/18.
 */
abstract public class WebJsonProvider extends Service implements IDataProvider {

    final private static String TAG_TRACER = "WebJsonProvider";
    final public static String DATA_KEY = "WebJsonData";
    private static final String TAG_TRACER_SERVICE = "ODS/SERVICE";
    static final int WHAT_CODE_FOR_REQUEST_QUEUE = 5;
    static final String KEY_QUEUE_CRITERIA_ARRAY = "ARGH";
    private ServiceHandler serviceHandler;
    private long requestRate;

    protected boolean isServiceMode() {
        return serviceMode;
    }

    protected void setServiceMode(boolean mode) {
        serviceMode = mode;
    }

    private boolean serviceMode = false;

    abstract protected String getTargetClass();

    private DataProviderListener listener;

    public void setListener(DataProviderListener listener) {
        this.listener = listener;
    }


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

    abstract protected URL getRequestUrl(String... criteria);

    public void request(final String criteria, @NonNull final Handler callbackHandler) {
        new Thread() {
            public void run() {
                Log.d(TAG_TRACER, Helpers.getMethodName());
                JSONObject jsonObject = loadData(criteria);
                Serializable response = null;
                try {
                    response = (Serializable) new Gson().fromJson(jsonObject.toString(), Class.forName(getTargetClass()));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Log.d(TAG_TRACER, "Data loaded");
                if(response != null) {
                    Message msgObj = callbackHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DATA_KEY, response);
                    msgObj.setData(bundle);
                    callbackHandler.sendMessage(msgObj);
                }
            }
        }.start();
    }

    public void queueRequest(String... criteria) {
        serviceHandler.removeMessages(WHAT_CODE_FOR_REQUEST_QUEUE);

        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_QUEUE_CRITERIA_ARRAY, criteria);

        Message msg = serviceHandler.obtainMessage();
        msg.setData(bundle);
        msg.what = WHAT_CODE_FOR_REQUEST_QUEUE;

        serviceHandler.sendMessageDelayed(msg, requestRate);
    }

    public void setRequestRate(long millis) {
        requestRate = millis;
    }

    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG_TRACER_SERVICE, String.format("handleMessage: got a message \"%d\"", msg.what));
            Bundle bundle = msg.getData();
            String[] criteria = bundle.getStringArray(KEY_QUEUE_CRITERIA_ARRAY);
            super.handleMessage(msg);
            request(criteria[0], new LeakSafeHandler<>(listener));
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setServiceMode(true);
        serviceHandler = new ServiceHandler();
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

    private final IBinder binder = new DataProviderServiceBinder();

    public class DataProviderServiceBinder extends Binder {
        public IDataProvider getDataProviderService() {
            Log.d(TAG_TRACER_SERVICE, Helpers.getMethodName());
            return WebJsonProvider.this;
        }
    }




}
