package pro.xite.dev.weatherwhenever;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ForecastActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Intent intent = getIntent();
        Forecast reliableForecast = (Forecast) intent.getSerializableExtra(Intentional.FORECAST);
        setTextDailyTip(reliableForecast.getTip());
        setForecastText(reliableForecast.getWeather());
        setCityText(reliableForecast.getCity());
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


}
