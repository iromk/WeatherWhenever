package pro.xite.dev.weatherwhenever;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ForecastActivity extends AppCompatActivity {

    private boolean messageSent = false;

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

    @Override
    public void finish() {
        Intent result = new Intent();
        result.putExtra("friends_response", "Friend will be prepared too");
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
        Log.d("TRACER", "Tell the friend clicked");
        messageSent = true;
        Toast.makeText(this, "Message to the friend has been sent", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("friends_response", "Bob is ready too");
        setIntent(intent);
    }
}
