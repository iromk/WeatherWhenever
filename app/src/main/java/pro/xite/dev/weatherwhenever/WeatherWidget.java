package pro.xite.dev.weatherwhenever;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Calendar;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.manage.PrefsManager;
import pro.xite.dev.weatherwhenever.manage.RecentCitiesList;
import pro.xite.dev.weatherwhenever.manage.WeatherInfoService;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherWidgetConfigureActivity WeatherWidgetConfigureActivity}
 */
public class WeatherWidget extends AppWidgetProvider {

    public static final String KEY_TEMPERATURE = "WIDGET/TEMP";
    public static final String KEY_WEATHER = "WIDGET/Weather";
    public static final String KEY_WHEREVER = "WIDGET/Wherever";
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.WEATHER_UPDATE";
    private static String TAG = "TRACER/WIDGET";

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                         int appWidgetId) {

        Log.w(TAG, String.format("%s at %tR", Helpers.getMethodName(), Calendar.getInstance()));
        RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.fragment_one_day_weather);
        PrefsManager prefsManager = new PrefsManager(context.getSharedPreferences(
                context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE));
        RecentCitiesList recentCitiesList = prefsManager.loadRecentCitiesList();

        Wherever wherever = null;
        Weather weather = null;
        if (recentCitiesList.getCounter() > 0) {
            wherever = recentCitiesList.getLatestCity();
            weather = recentCitiesList.getLatestWeather();
        }
        updateViews(context, rViews, weather, wherever);
        appWidgetManager.updateAppWidget(appWidgetId, rViews);
    }

    private void updateViews(Context context, RemoteViews rViews, Weather weather, Wherever wherever) {
        rViews.setViewVisibility(R.id.textview_tscale, View.INVISIBLE);
        if (weather != null) {
            Log.w(TAG, "updateViews: t==" + weather.getTemperature());
            rViews.setViewVisibility(R.id.textview_tscale, View.VISIBLE);
            rViews.setTextViewText(R.id.textview_temp, Helpers.tempToString(weather.getTemperature()));
            rViews.setImageViewResource(R.id.imageview_weather_icon,
                    Helpers.getResIdByName("owm_" + weather.getIconId(), R.drawable.class));
        }
        if (wherever != null)
            rViews.setTextViewText(R.id.textview_notes, wherever.getName());
        else
            rViews.setTextViewText(R.id.textview_temp, context.getString(R.string.placeholder_no_data));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        Log.w(TAG, String.format("onUpdate received %1$tR", Calendar.getInstance()));
        Intent serv = new Intent();
        serv.setClass(context, WeatherInfoService.class);
        context.startService(serv);

        super.onUpdate(context, appWidgetManager, appWidgetIds);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            WeatherWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, String.format("onReceive received %1$tR", Calendar.getInstance()));
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String action = intent.getAction();
        if (UPDATE_WIDGET_ACTION.equals(action)) {
            Log.w(TAG, String.format("received action \"%s\" %tR", UPDATE_WIDGET_ACTION, Calendar.getInstance()));
            final int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
            final RemoteViews rViews = new RemoteViews(context.getPackageName(), R.layout.fragment_one_day_weather);
            Weather weather = (Weather) intent.getSerializableExtra(KEY_WEATHER);
            Wherever wherever = (Wherever) intent.getSerializableExtra(Wherever.KEY);
            if(wherever == null) Log.w(TAG, "onReceive: wherever is NULL");
            else Log.w(TAG, "onReceive: wherever name==" + wherever.getName());
            updateViews(context, rViews, weather, wherever);
            appWidgetManager.updateAppWidget(ids, rViews);
//            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.textview_notes);
        }
        super.onReceive(context, intent);

    }


    @Override
    public void onEnabled(Context context) {
        Log.w(TAG, "onEnabled: ");
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

