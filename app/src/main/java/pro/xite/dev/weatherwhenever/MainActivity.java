package pro.xite.dev.weatherwhenever;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private Spinner spinnerCityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinnerCityList = findViewById(R.id.spinner_city_list);
        spinnerCityList.setOnItemSelectedListener(this);
        ForecastProvider.create(this);

        loadSelectionFromPreferences();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_get_forecast) {
            Log.d("TRACER", "The button has been clicked");
            saveSelectionInPreferences();
            Forecast reliableForecast = ForecastProvider.makeReliableForecast(getSelectedCityName());
            setForecastText(reliableForecast.getWeather());
            Intent intent = new Intent(this, ForecastActivity.class);
            intent.putExtra(Intentional.FORECAST, reliableForecast);
            startActivity(intent);
        }
    }

    private int getSelectedCityId() {
        return spinnerCityList.getSelectedItemPosition();
    }

    private String getSelectedCityName() {
        return getCityByIndex(getSelectedCityId());
    }

    private String getCityByIndex(int i) {
        return getResources().getStringArray(R.array.city_list)[i];
    }

    private void saveSelectionInPreferences() {
        final Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.selected_city_index_prefs_key), getSelectedCityId());
        editor.apply(); // tip! writes in a background
        //editor.commit(); // tip! writes data immediately
    }

    private void loadSelectionFromPreferences() {
        final Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final int default_city_index = 0; //getResources().getStringArray(R.array.city_list)[0];
        final int last_city_index = sharedPref.getInt(getString(R.string.selected_city_index_prefs_key), default_city_index);
        spinnerCityList.setSelection(last_city_index,true);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("TRACER", spinnerCityList.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
