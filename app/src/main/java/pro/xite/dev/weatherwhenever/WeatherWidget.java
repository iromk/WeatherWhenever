package pro.xite.dev.weatherwhenever;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;

import java.util.Arrays;

import pro.xite.dev.weatherwhenever.data.Weather;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherWidgetConfigureActivity WeatherWidgetConfigureActivity}
 */
public class WeatherWidget extends AppWidgetProvider {

    private static String TAG = "LOG/WIDGET";
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    int temp=-274;

    void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

//        CharSequence widgetText = WeatherWidgetConfigureActivity.loadTitlePref(context, appWidgetId);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fragment_one_day_weather);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
        // Instruct the widget manager to update the widget
        Log.d(TAG, "updateAppWidget: " + temp);
        views.setTextViewText(R.id.textview_temp, String.valueOf(temp));
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Log.d(TAG, "onUpdate: " + temp);
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
        Log.d(TAG, "onReceive: !!");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String action = intent.getAction();
        if(action != null) {
            temp = intent.getIntExtra("TEMP", -11);
            Weather weather = (Weather) intent.getSerializableExtra("WWW");
            int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
            Log.e(TAG, "onReceive: " + Arrays.toString(ids));
            Log.d(TAG, "onReceive: "+temp);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fragment_one_day_weather);
//            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget_layout);
            Log.d(TAG, "updateAppWidget: " + temp);
//            views.setTextViewText(R.id.appwidget_text, String.valueOf(temp));
            views.setTextViewText(R.id.textview_temp, String.valueOf(temp));
            views.setImageViewResource(R.id.imageview_weather_icon,
                    Helpers.getResIdByName("owm_"+weather.getIconId(), R.drawable.class));
            appWidgetManager.updateAppWidget(ids[0], views);
//            FragmentTransaction fragmentTransaction = ((Activity)context).getFragmentManager().beginTransaction();
//            fragmentTransaction.addToBackStack(null);
//            OneDayWeatherFragment frag = OneDayWeatherFragment.newInstance(OneDayWeatherFragment.SIZE_S);
//            frag.setTemp(temp);
//            fragmentTransaction.replace(R.id.widget_frame, frag);
//            fragmentTransaction.commit();
//            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.appwidget_text);
        }
//        String temp = intent.getStringExtra("TEMP");
//        Log.d(TAG, temp);
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

