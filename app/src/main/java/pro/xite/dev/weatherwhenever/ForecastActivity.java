package pro.xite.dev.weatherwhenever;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import pro.xite.dev.weatherwhenever.owm.OWMActualWeatherProvider;
import pro.xite.dev.weatherwhenever.owm.OWMNearestForecast;
import pro.xite.dev.weatherwhenever.owm.OWMNearestForecastProvider;
import pro.xite.dev.weatherwhenever.owm.OWMWeather;

public class ForecastActivity extends AppCompatActivity implements ViewUpdatable {

    public static final String TAG_LIFECYCLE = "LIFETIME";
    public static final String TAG_TRACER = "TRACER";
    public static final String FORECAST_OBJECT = "forecastObject";

    private boolean messageSent = false;
    private Forecast reliableForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Intent intent = getIntent();
        reliableForecast = (Forecast) intent.getSerializableExtra(FORECAST_OBJECT);
        // TODO savedInstance
        new OWMActualWeatherProvider().request("Cairo", this);
        new OWMNearestForecastProvider().request("Cairo", this);

        logMe();
    }

    @Override
    public <T> void updateViews(T owm) {
        if(owm instanceof OWMWeather) {
            OWMWeather owmWeather = (OWMWeather) owm;
            setCityText(owmWeather.toString());
            setForecastText(String.format("%s, %s", owmWeather.getTemp(), owmWeather.getPressure()));
        }

        if(owm instanceof OWMNearestForecast) {
            OWMNearestForecast owmNearestForecast = (OWMNearestForecast) owm;
            setTextDailyTip(owmNearestForecast.toString());
        }
    }

    private void logMe() {
        Log.i(TAG_LIFECYCLE, Helpers.getMethodName(2));
    }

    @Override
    protected void onStart() {
        logMe();
        super.onStart();
    }

    @Override
    protected void onResume() {
        logMe();
        super.onResume();
    }

    @Override
    protected void onPause() {
        logMe();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        logMe();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        logMe();
        super.onStop();
    }

    @Override
    protected void onRestart() {
        logMe();
        super.onRestart();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        logMe();
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void finish() {
        Intent result = new Intent();
        result.putExtra(MainActivity.FRIENDS_RESPONSE, "Friend will be prepared too");
        if(messageSent)
            setResult(RESULT_OK, result);
        else
            setResult(RESULT_CANCELED, result);
        super.finish();
    }

    private void setCityText(String text) {
        TextView tv = findViewById(R.id.selected_city_name);
        tv.setText(text);
    }

    private void setTextDailyTip(String text) {
        TextView tv = findViewById(R.id.textview_tipoftheday);
        tv.setText(text);
    }

    private void setForecastText(String text) {
        TextView tv = findViewById(R.id.textview_forecast);
        tv.setText(text);
    }

    public void onClick(View view) {
        Log.d(TAG_TRACER, "Tell the friend clicked");
        messageSent = true;
        Toast.makeText(this, "Message to the friend has been sent", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_SEND);
//        Intent intent = new Intent(Intent.ACTION_QUICK_VIEW); // test of unavailable activity
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, reliableForecast.getTip());
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.d(TAG_TRACER, "Don't panic. That activity is still in development.");
        }
    }
}
