package pro.xite.dev.weatherwhenever;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Wherever;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link WeatherWidgetConfigureActivity WeatherWidgetConfigureActivity}
 */
public class WeatherWidget extends AppWidgetProvider {

    private static final int ABSOLUTE_ZERO = -273;
    public static final String KEY_TEMPERATURE = "WIDGET/TEMP";
    public static final String KEY_WEATHER = "WIDGET/Weather";
    public static final String KEY_WHEREVER = "WIDGET/Wherever";
    public static final String UPDATE_WIDGET_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";
    private static String TAG = "LOG/WIDGET";

    private int temp = ABSOLUTE_ZERO;

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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        String action = intent.getAction();
        if(action != null) {
            final int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, WeatherWidget.class));
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.fragment_one_day_weather);
            temp = intent.getIntExtra(KEY_TEMPERATURE, ABSOLUTE_ZERO);
            Weather weather = (Weather) intent.getSerializableExtra(KEY_WEATHER);
            Wherever wherever= (Wherever) intent.getSerializableExtra(KEY_WHEREVER);
            if(weather != null) {
                views.setTextViewText(R.id.textview_temp, Helpers.tempToString(weather.getTemperature()));
                views.setImageViewResource(R.id.imageview_weather_icon,
                        Helpers.getResIdByName("owm_" + weather.getIconId(), R.drawable.class));
            }
            if(wherever != null)
                views.setTextViewText(R.id.textview_notes, wherever.getName());
            appWidgetManager.updateAppWidget(ids[0], views);
//            appWidgetManager.notifyAppWidgetViewDataChanged(ids, R.id.appwidget_text);
        }
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

