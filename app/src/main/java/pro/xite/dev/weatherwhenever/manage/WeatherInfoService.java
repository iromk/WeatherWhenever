package pro.xite.dev.weatherwhenever.manage;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import pro.xite.dev.weatherwhenever.Helpers;
import pro.xite.dev.weatherwhenever.R;
import pro.xite.dev.weatherwhenever.WeatherWidget;
import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.data.owm.OwmActualWeatherProvider;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecastProvider;
import pro.xite.dev.wjdp.IDataProvider;
import pro.xite.dev.wjdp.IDataProviderListener;
import pro.xite.dev.wjdp.WebJsonProvider;

/**
 * Created by Roman Syrchin on 4/16/18.
 */
public class WeatherInfoService extends Service implements IDataProviderListener {

    private static final String TAG = "TRACER/WIS";

    private final IBinder binder = new DataProviderServiceBinder();

    Bundle bundle;
    Intent intent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand: ");
        resetIntent();
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.fragment_one_day_weather);
        rv.setTextViewText(R.id.textview_time, String.format("%tR", Calendar.getInstance()));
        ComponentName componentName = new ComponentName(this, WeatherWidget.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        appWidgetManager.updateAppWidget(componentName, rv);
        return super.onStartCommand(intent, flags, startId);
    }

    private void resetIntent() {
        intent = new Intent();
        final PrefsManager prefs = new PrefsManager(getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE));
        final String cityname = prefs.getLastCity();
        final RecentCitiesList rcList = prefs.loadRecentCitiesList();
        intent.putExtra(Wherever.KEY, rcList.getCity(cityname));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "Handler().postDelayed: started");
                new OwmActualWeatherProvider().asyncRequest(WeatherInfoService.this, cityname);
                new OwmNearestForecastProvider().asyncRequest(WeatherInfoService.this, cityname);
                Log.i(TAG, "Handler().postDelayed: requested provs");
             }
         }, 640_000);
    }

    @Override
    public void onSerializedDataReceived(Serializable data) {
        if (data instanceof Weather) {
            Weather weather = (Weather) data;
            intent.putExtra(WeatherWidget.KEY_WEATHER, (Serializable) weather);
            Log.d(TAG, String.format("new Weather t==%d", (int) weather.getTemperature()));
        } else if (data instanceof Whenever) {
            Whenever whenever = (Whenever) data;
            intent.putExtra("nop", (Serializable) whenever);
            Log.d(TAG, String.format("new forecast %d", (int) whenever.getLatestForecast().getTemperature()));
        }
        if(intent.hasExtra(WeatherWidget.KEY_WEATHER) && intent.hasExtra("nop")) {
            Log.w(TAG, String.format("onSerializedDataReceived: has wherever %b", intent.hasExtra(Wherever.KEY)));
            intent.setClass(this, WeatherWidget.class);
            intent.setAction(WeatherWidget.UPDATE_WIDGET_ACTION);
            Log.i(TAG, "onSerializedDataReceived: intent sent.");
//            LocalBroadcastManager.getInstance(this).
                    sendBroadcast(intent);
                    resetIntent();
        }
    }

    public class DataProviderServiceBinder extends Binder {
        public WeatherInfoService getDataProviderService() {
            Log.d(TAG, Helpers.getMethodName());
            return WeatherInfoService.this;
        }
    }

    private long rate = 66_000;
    private String[] criteria;

    private ArrayList<IDataProviderListener> listeners = new ArrayList<>();

    public void subscribe(IDataProviderListener listener, long rate) {
        listeners.add(listener);
    }

    public void setCriteria(String... criteria) {
        this.criteria = criteria;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
