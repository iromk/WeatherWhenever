package pro.xite.dev.weatherwhenever.data;

import android.app.Service;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import pro.xite.dev.weatherwhenever.Helpers;
import pro.xite.dev.weatherwhenever.manage.IDataProviderListener;
import pro.xite.dev.weatherwhenever.manage.LeakSafeHandler;

/**
 * Created by Roman Syrchin on 4/7/18.
 */
abstract public class WebJsonProvider extends Service implements IDataProvider {

    static final int WHAT_CODE_FOR_REQUEST_QUEUE = 22;
    static final int WHAT_CODE_FOR_DELAYED_REQUEST = 5;

    final private static String TAG_TRACER = "WJP";
    private static final String TAG_TRACER_SERVICE = "LOG/WJP/SERVICE";

    static final String KEY_QUEUE_CRITERIA_ARRAY = "ARGH";
    private ServiceHandler serviceHandler;
    private long delayedRequestTimeout;
    private IDataProviderListener listener;

    public void setListener(IDataProviderListener listener) {
        this.listener = listener;
    }

    abstract protected Class<?> getTargetClass();

    abstract protected URL getRequestUrl(String... criteria);

    protected String loadData(URL url) {

        try {

            Log.d(Helpers.getMethodName(), url.toString());

            HttpResponseCache cache = HttpResponseCache.getInstalled();
//            Log.d("CACHE", String.format("requests %d", cache.getRequestCount()));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                rawData.append(nextLine).append('\n');
            }
            reader.close();

            return rawData.toString();

        } catch (IOException e) {
            return null;
        }
    }

    private void request(final String... criteria) {
        final Handler callbackHandler = new LeakSafeHandler(listener);
        new Thread() {
            public void run() {
                Log.d(TAG_TRACER, Helpers.getMethodName());
                String rawString = loadData(getRequestUrl(criteria));
                Serializable response = (Serializable) new Gson().fromJson(rawString, getTargetClass());
                Log.d(TAG_TRACER, "Data loaded");
                if (response != null) {
                    Message msgObj = callbackHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(IDataProviderListener.KEY_SERIALIZABLE, response);
                    msgObj.setData(bundle);
                    callbackHandler.sendMessage(msgObj);
                }
            }
        }.start();
    }

    @Override
    public void asyncRequest(IDataProviderListener listener, String... criteria) {
        setListener(listener);
        request(criteria);
    }

    public void delayedRequest(String... criteria) {
        serviceHandler.removeMessages(WHAT_CODE_FOR_DELAYED_REQUEST);

        serviceHandler.sendMessageDelayed(
                prepareMessage(WHAT_CODE_FOR_DELAYED_REQUEST, criteria),
                delayedRequestTimeout);
    }

    public void queuedRequest(String... criteria) {
        serviceHandler.sendMessage(prepareMessage(WHAT_CODE_FOR_REQUEST_QUEUE, criteria));
    }

    private Message prepareMessage(int what, String... content) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_QUEUE_CRITERIA_ARRAY, content);

        Message msg = serviceHandler.obtainMessage();
        msg.what = what;
        msg.setData(bundle);
        return msg;
    }

    public void setDelayedRequestTimeout(long millis) {
        delayedRequestTimeout = millis;
    }

    private class ServiceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d(TAG_TRACER_SERVICE, String.format("handleMessage: got a message \"%d\"", msg.what));
            Bundle bundle = msg.getData();
            String[] criteria = bundle.getStringArray(KEY_QUEUE_CRITERIA_ARRAY);
            super.handleMessage(msg);
            request(criteria);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
