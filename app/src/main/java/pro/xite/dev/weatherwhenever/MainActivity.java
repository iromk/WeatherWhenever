package pro.xite.dev.weatherwhenever;

import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.data.owm.OwmActualWeatherProvider;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecastProvider;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;
import pro.xite.dev.weatherwhenever.manage.DbManager;
import pro.xite.dev.weatherwhenever.manage.PrefsManager;
import pro.xite.dev.weatherwhenever.manage.RecentCitiesList;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, DataProviderListener {

    public static final String FRIENDS_RESPONSE = "friends_response";
    public static final String TAG_TRACER = "TRACER";
    private static final int PROMPT_AFTER_3_SEC = 3000;
    private static final int PROMPT_INSTANT = 0;

    private NavigationView navigationView;
    private EditText editTextCity;
    private TextView textViewDescription;
    private TextView textViewTemperature;
    private DrawerLayout drawerLayout;

    private PrefsManager prefsManager;
    private DbManager dbManager;
    private RecentCitiesList recentCitiesList;

    private Weather weather;
    private Whenever whenever;
    private Wherever wherever;

    final private boolean USE_DATABASE = true;

    MaterialTapTargetPrompt mFabPrompt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        textViewDescription = findViewById(R.id.textview_wheather_now);
        textViewTemperature = findViewById(R.id.textview_temp);
        editTextCity = findViewById(R.id.edittext_cityname);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        View fragmentContainer = findViewById(R.id.fragment_top);


        try {
            File httpCacheDir = new File(getCacheDir(), "http");
            long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (IOException e) {
            Log.i(TAG_TRACER, "HTTP response cache installation failed:" + e);
        }

        dbManager = new DbManager(this);
        dbManager.open();

        prefsManager = new PrefsManager(getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE));

        if(USE_DATABASE)
            recentCitiesList = dbManager.loadRecentCitiesList();
        else
            recentCitiesList = prefsManager.loadRecentCitiesList();

        if (recentCitiesList == null)
            recentCitiesList = new RecentCitiesList();

        if(recentCitiesList.getCounter() > 0) {
            Menu menu = navigationView.getMenu();
            for (Wherever city : recentCitiesList.getCities()) {
                menu.add(city.getName());
            }

            wherever = recentCitiesList.getLatestCity();
            weather = recentCitiesList.getLatestWeather();
            whenever = recentCitiesList.getLatestForecast();
            updateViews();
        }

        initDrawer();

        // TODO if (favorites == 0)
        promptUseSearchCity(PROMPT_AFTER_3_SEC);
    }

    private void promptUseSearchCity(int delayMillis) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                new MaterialTapTargetPrompt.Builder(MainActivity.this)
                        .setPrimaryText(R.string.search_city_prompt_title)
                        .setSecondaryText(R.string.search_city_prompt_description)
                        .setAnimationInterpolator(new FastOutSlowInInterpolator())
                        .setMaxTextWidth(R.dimen.tap_target_menu_max_width)
                        .setIcon(R.drawable.ic_search)
                        .setTarget(R.id.add_city)
                        .show();
            }
        }, delayMillis);

    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Requesting owm", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Action", vol)
                        .show();
                promptUseSearchCity(PROMPT_INSTANT); // for test only

//                loadFragment(R.id.fragment_top, AddCity.newInstance("", ""), true);
//                addCityAndLoadWeather();
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadFragment(int containerID, Fragment fragment, boolean isPutToBackstack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(containerID, fragment);
        if (isPutToBackstack) transaction.addToBackStack(null);
        transaction.commit();
    }

    private void addCityAndLoadWeather() {
        final String city = editTextCity.getText().toString();
        whenever = null;
        weather = null;
        wherever = null;
        new OwmActualWeatherProvider().request(city, this);
        new OwmNearestForecastProvider().request(city, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG_TRACER, "onClick: not implemented");
    }

    @Override
    protected void onPause() {
        Log.d(TAG_TRACER, "onPause: not implemented");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbManager.close();
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
    public void onSerializedDataReceived(Serializable data) {
        Log.d(TAG_TRACER, Helpers.getMethodName());
        if(data instanceof Wherever) {
            wherever = (Wherever) data;
            Log.d(TAG_TRACER, String.format("Got new Wherever %s of %s", wherever.getName(), wherever.getCountryCode()));
        } else if(data instanceof Weather) {
            weather = (Weather) data;
            Log.d(TAG_TRACER, String.format("Got new Weather t==%d", (int) weather.getTemperature()));
        } else if (data instanceof Whenever) {
            whenever = (Whenever) data;
            Log.d(TAG_TRACER, String.format("Got new Whenever %d", (int) whenever.getLatestForecast().getTemperature()));
        }
        updateViews();
        tryToSavePreferences();
        tryToUpdateDb();
    }

    private void updateViews() {
        String weatherDescription = "";
        if(weather != null) {
            textViewTemperature.setText(String.valueOf((int) weather.getTemperature()));
            weatherDescription = weather.toString();
        }
        if(wherever != null) {
            weatherDescription = String.format("%s of %s:\n%s", wherever.getName(), wherever.getCountryCode(), weatherDescription);
        }
        textViewDescription.setText(weatherDescription);
    }

    private void tryToSavePreferences() {
        if(wherever != null && weather != null && whenever != null) {
            recentCitiesList.add(wherever, weather, whenever);
            prefsManager.savePrefs(recentCitiesList);

            Menu menu = navigationView.getMenu();
            menu.add(wherever.getName());
        }
    }

    private void tryToUpdateDb() {
        if(wherever != null && weather != null && whenever != null) {
            dbManager.updateData(wherever, weather, whenever);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(this, FindCity.class);
//        intent.putExtra("w", reliableForecast);
        startActivityForResult(intent, 1);
        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //int id = item.getItemId();
        final CharSequence cityName = item.getTitle();
        wherever = recentCitiesList.getCity(cityName);
        weather = recentCitiesList.getWeatherForCity(cityName);
        whenever = recentCitiesList.getForecastForCity(cityName);
        drawerLayout.closeDrawer(GravityCompat.START);
        updateViews();
        return true;
    }
}
