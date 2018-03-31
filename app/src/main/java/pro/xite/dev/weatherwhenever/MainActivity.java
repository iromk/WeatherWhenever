package pro.xite.dev.weatherwhenever;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import pro.xite.dev.weatherwhenever.data.owm.OwmActualWeatherProvider;
import pro.xite.dev.weatherwhenever.data.owm.OwmCity;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecast;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecastProvider;
import pro.xite.dev.weatherwhenever.data.owm.OwmWeather;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, Spinner.OnItemSelectedListener, ViewUpdatable {

    public static final String LATEST_FORECAST_KEY = "latest_forecast";
    public static final String FRIENDS_RESPONSE = "friends_response";
    public static final String TAG_TRACER = "TRACER";

    private Spinner spinnerCityList;
    private Forecast reliableForecast = null;

    private PrefsManager prefsManager;
    private RecentCitiesList recentCitiesList;
    private OwmWeather owmWeather;
    private OwmNearestForecast owmNearestForecast;
    private OwmCity owmCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        prefsManager  = new PrefsManager(getSharedPreferences(
                                            getString(R.string.preference_file_key),
                                            Context.MODE_PRIVATE));
//        recentCitiesList = new RecentCitiesList();
        recentCitiesList = prefsManager.loadRecentCitiesList();
        if(recentCitiesList == null) {
            recentCitiesList = new RecentCitiesList();
        } else {
            NavigationView navView = findViewById(R.id.nav_view);
            Menu menu = navView.getMenu();
            for (OwmCity city : recentCitiesList.getCities()) {
                menu.add(city.getName());
            }

            owmCity = recentCitiesList.getLatestCity();
            owmWeather = recentCitiesList.getLatestWeather();
            owmNearestForecast = recentCitiesList.getLatestForecast();

            TextView textView = findViewById(R.id.textview_wheather_now);
            TextView textViewTemp = findViewById(R.id.textview_temp);
            textViewTemp.setText(String.valueOf(owmWeather.getTemp().intValue()));
            textView.setText(owmWeather.toString());
        }
//            ForecastProvider.create(this);
        initDrawer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void initDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Requesting owm", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Action", vol)
                        .show();
                addCityAndLoadWeather();
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

    private void addCityAndLoadWeather() {
        EditText editText = findViewById(R.id.edittext_cityname);
        final String city = editText.getText().toString();
        owmNearestForecast = null;
        owmWeather = null;
        owmCity = null;
        new OwmActualWeatherProvider().request(city, this);
        new OwmNearestForecastProvider().request(city, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LATEST_FORECAST_KEY, reliableForecast);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG_TRACER, Helpers.getMethodName());
        int id = view.getId();
        if(id == R.id.button_get_forecast) {
            Log.d(TAG_TRACER, "The button has been clicked");
            addCityAndLoadWeather();
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG_TRACER, spinnerCityList.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public <T> void updateViews(T owm) {
        Log.d(TAG_TRACER, Helpers.getMethodName());
        TextView textView = findViewById(R.id.textview_wheather_now);
        TextView textViewTemp = findViewById(R.id.textview_temp);
        if(owm instanceof OwmWeather) {
            owmWeather = (OwmWeather) owm;
            textViewTemp.setText(String.valueOf(owmWeather.getTemp().intValue()));
            textView.setText(owmWeather.toString());
        } else if (owm instanceof OwmNearestForecast) {
            owmNearestForecast = (OwmNearestForecast) owm;
        }
        tryToSavePreferences();
    }

    private void tryToSavePreferences() {
        if(owmWeather != null && owmNearestForecast != null) {
            owmCity = owmWeather.getOWMCity();
            recentCitiesList.add(owmCity, owmWeather, owmNearestForecast);
            prefsManager.savePrefs(recentCitiesList);

            NavigationView navView = findViewById(R.id.nav_view);
            Menu menu = navView.getMenu();
            menu.add(owmCity.getName());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        owmCity = recentCitiesList.getCity(item.getTitle());
        owmWeather = recentCitiesList.getWeatherForCity(item.getTitle());
        owmNearestForecast = recentCitiesList.getForecastForCity(item.getTitle());
        TextView textView = findViewById(R.id.textview_wheather_now);
        TextView textViewTemp = findViewById(R.id.textview_temp);
        textViewTemp.setText(String.valueOf(owmWeather.getTemp().intValue()));
        textView.setText(owmWeather.toString());
        return true;
    }
}
