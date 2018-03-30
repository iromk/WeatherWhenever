package pro.xite.dev.weatherwhenever;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pro.xite.dev.weatherwhenever.owm.OWMActualWeatherProvider;
import pro.xite.dev.weatherwhenever.owm.OWMWeather;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, Spinner.OnItemSelectedListener, ViewUpdatable {

    public static final String LATEST_FORECAST_KEY = "latest_forecast";
    public static final String FRIENDS_RESPONSE = "friends_response";
    public static final String TAG_TRACER = "TRACER";

    private Spinner spinnerCityList;
    private Forecast reliableForecast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
//        spinnerCityList = findViewById(R.id.spinner_city_list);
//        spinnerCityList.setOnItemSelectedListener(this);
        ForecastProvider.create(this);
        initDrawer();
//        loadSelectionFromPreferences();

//        if(savedInstanceState != null) {
//            reliableForecast = (Forecast) savedInstanceState.getSerializable(LATEST_FORECAST_KEY);
//            setForecastText(reliableForecast.getWeather());
//        }
    }

    private void initDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LATEST_FORECAST_KEY, reliableForecast);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.button_get_forecast) {
            Log.d(TAG_TRACER, "The button has been clicked");
            EditText editText = findViewById(R.id.edittext_cityname);
            new OWMActualWeatherProvider().request(editText.getText().toString(), this);
//            reliableForecast = ForecastProvider.makeReliableForecast(getSelectedCityName());
//            setForecastText(reliableForecast.getWeather());
//            Intent intent = new Intent(this, ForecastActivity.class);
//            intent.putExtra(ForecastActivity.FORECAST_OBJECT, reliableForecast);
//            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onPause() {
//        saveSelectionInPreferences();
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            String response = data.getStringExtra(FRIENDS_RESPONSE);
            Toast.makeText(this, response, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Friend wouldn't know", Toast.LENGTH_LONG).show();
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

    // TODO move to external class
    private void saveSelectionInPreferences() {
        final Context context = this;
        final SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.selected_city_index_prefs_key), getSelectedCityId());
        editor.apply(); // tip! writes in a background
        //editor.commit(); // tip! writes data immediately
    }

    // TODO move to external class
    private void loadSelectionFromPreferences() {
//        final Context context = this;
//        final SharedPreferences sharedPref = context.getSharedPreferences(
//                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//        final int default_city_index = 0; //getResources().getStringArray(R.array.city_list)[0];
//        final int last_city_index = sharedPref.getInt(getString(R.string.selected_city_index_prefs_key), default_city_index);
//        spinnerCityList.setSelection(last_city_index,true);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG_TRACER, spinnerCityList.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setForecastText(String text) {
        TextView tv = findViewById(R.id.textview_forecast);
        tv.setText(text);
    }

    @Override
    public <T> void updateViews(T owm) {
        Log.d(TAG_TRACER, Helpers.getMethodName());
        TextView textView = findViewById(R.id.textview_wheather_now);
        if(owm instanceof OWMWeather) {
            textView.setText(owm.toString());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
