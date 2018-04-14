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
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import pro.xite.dev.weatherwhenever.data.Weather;
import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.data.owm.OwmActualWeatherProvider;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecastProvider;
import pro.xite.dev.weatherwhenever.manage.DbManager;
import pro.xite.dev.weatherwhenever.manage.IDataProviderListener;
import pro.xite.dev.weatherwhenever.manage.PrefsManager;
import pro.xite.dev.weatherwhenever.manage.RecentCitiesList;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, IDataProviderListener {

    public static final String TAG_TRACER = "TRACER";
    private static final int PROMPT_AFTER_3_SEC = 3000;
    private static final int PROMPT_INSTANT = 0;

    private NavigationView navigationView;
    private TextView textViewWhereverCity;
    private TextView textViewWhereverCountry;
    private TextView textViewDescriptive;
    private TextView textViewTimestamp;
    private DrawerLayout drawerLayout;

    private PrefsManager prefsManager;
    private DbManager dbManager;
    private RecentCitiesList recentCitiesList;

    private Weather weather;
    private Whenever whenever;
    private Wherever wherever;

    final private boolean USE_DATABASE = true;

    MaterialTapTargetPrompt mFabPrompt;
    private OneDayWeatherFragment fragTempNow;
    private OneDayWeatherFragment fragTempTomorrow;
    private OneDayWeatherFragment fragTempForecast1;
    private OneDayWeatherFragment fragTempForecast2;
    private FloatingActionButton fabUpdate;
    private boolean skipPromptUseUpdateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        textViewWhereverCity = findViewById(R.id.textview_wherever_city);
        textViewWhereverCountry = findViewById(R.id.textview_wherever_country);
        textViewDescriptive = findViewById(R.id.textview_descriptive);
        textViewTimestamp = findViewById(R.id.textview_timestamp);
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        fragTempNow = OneDayWeatherFragment.newInstance(OneDayWeatherFragment.SIZE_L);
        loadFragment(R.id.fragment_t_now, fragTempNow);
        fragTempTomorrow = OneDayWeatherFragment.newInstance(OneDayWeatherFragment.SIZE_M);
        loadFragment(R.id.fragment_t_later, fragTempTomorrow);
        fragTempForecast1 = OneDayWeatherFragment.newInstance(OneDayWeatherFragment.SIZE_S);
        loadFragment(R.id.fragment_forecast1, fragTempForecast1);
        fragTempForecast2 = OneDayWeatherFragment.newInstance(OneDayWeatherFragment.SIZE_S);
        loadFragment(R.id.fragment_forecast2, fragTempForecast2);

        initDrawer();
        initFloatActionButton();

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

        if (USE_DATABASE)
            recentCitiesList = dbManager.loadRecentCitiesList();
        else
            recentCitiesList = prefsManager.loadRecentCitiesList();

        if (recentCitiesList == null)
            recentCitiesList = new RecentCitiesList();

        addCityToNavigationMenu();

        wherever = recentCitiesList.getLatestCity();
        weather = recentCitiesList.getLatestWeather();
        whenever = recentCitiesList.getLatestForecast();

        updateViews();

    }

    private void addCityToNavigationMenu() {
        Menu menu = navigationView.getMenu();
        menu.getItem(0).getSubMenu().clear();
        for (Wherever city : recentCitiesList.getCities())
            menu.getItem(0).getSubMenu().add(city.getName());
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
                        .setTarget(R.id.find_city_button)
                        .show();
            }
        }, delayMillis);

    }

    private void promptUseUpdateData(int delayMillis) {
        if (skipPromptUseUpdateData) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Animation animation = AnimationUtils.loadAnimation(fabUpdate.getContext(), R.anim.shake_shake);
                    animation.setDuration(250L);
                    fabUpdate.startAnimation(animation);
                }
            }, delayMillis);
        } else {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (mFabPrompt != null) {
                        return;
                    }
                    SpannableStringBuilder secondaryText = new SpannableStringBuilder(getString(R.string.prompt_update_description));
                    secondaryText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(MainActivity.this, R.color.colorAccent)),
                            getResources().getInteger(R.integer.accent_start_update_description),
                            getResources().getInteger(R.integer.accent_end_update_description),
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    SpannableStringBuilder primaryText = new SpannableStringBuilder(getString(R.string.prompt_update_title));
                    mFabPrompt = new MaterialTapTargetPrompt.Builder(MainActivity.this)
                            .setTarget(findViewById(R.id.fab))
                            .setPrimaryText(primaryText)
                            .setSecondaryText(secondaryText)
                            .setBackButtonDismissEnabled(true)
                            .setAnimationInterpolator(new FastOutSlowInInterpolator())
                            .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                                @Override
                                public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {
                                    if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED
                                            || state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED) {
                                        mFabPrompt = null;
                                        skipPromptUseUpdateData = true;
                                    }
                                }
                            })
                            .create();
                    mFabPrompt.show();
                }
            }, delayMillis);
        }

    }

    private void initDrawer() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    Snackbar snack = null;

    private void dismissSnack() {
        if (snack != null && snack.isShown()) snack.dismiss();
    }

    private void initFloatActionButton() {
        fabUpdate = findViewById(R.id.fab);
        fabUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snack = Snackbar.make(view, R.string.snack_text_updating_weather, Snackbar.LENGTH_INDEFINITE);
                snack.show();
                requestOwm();
            }
        });
    }

    private void loadFragment(int containerID, Fragment fragment) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(containerID, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
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
        if (resultCode == RESULT_OK) {
            whenever = null;
            weather = null;
            wherever = null;
            wherever = (Wherever) data.getSerializableExtra(FindCityActivity.RESULT_CITY);
            recentCitiesList.addUnique(wherever, weather, whenever);
            requestOwm();
        }
    }

    private void requestOwm() {
        new OwmActualWeatherProvider().asyncRequest(this, wherever.getName());
        new OwmNearestForecastProvider().asyncRequest(this, wherever.getName());
    }

    @Override
    public void onSerializedDataReceived(Serializable data) {
        Log.d(TAG_TRACER, Helpers.getMethodName());
        if (data instanceof Weather) {
            weather = (Weather) data;
            Log.d(TAG_TRACER, String.format("Got new Weather t==%d", (int) weather.getTemperature()));
        } else if (data instanceof Whenever) {
            whenever = (Whenever) data;
            Log.d(TAG_TRACER, String.format("Got new Whenever %d", (int) whenever.getLatestForecast().getTemperature()));
        }
        dismissSnack();
        tryToSavePreferences();
        tryToUpdateDb();
        updateViews();
    }

    private void updateViews() {
        if (weather != null) {
            fragTempNow.setWeather(weather);
            textViewDescriptive.setText(String.format("%s\n%s..%s",
                    weather.getDescription(),
                    Helpers.tempToString(weather.getMinTemperature()),
                    Helpers.tempToString(weather.getMaxTemperature())
            ));
            textViewTimestamp.setText(String.format("%d mins ago",
                    (System.currentTimeMillis() - weather.getDate().getTime()) / (1000 * 60)
            ));

            if ((System.currentTimeMillis() - weather.getDate().getTime()) / (1000 * 60) > 60) {
                promptUseUpdateData(PROMPT_AFTER_3_SEC);
            }

        }
        if (wherever != null) {
            textViewWhereverCity.setText(wherever.getName());
            textViewWhereverCountry.setText(wherever.getCountryName());
        }
        if (whenever != null) {
            fragTempTomorrow.setWeather(whenever.getNearestForecast());
            fragTempForecast1.setWeather(whenever.getLatestForecast());
            fragTempForecast2.setWeather(whenever.getLatestForecast());
        }

        if (recentCitiesList.getCounter() == 0)
            promptUseSearchCity(PROMPT_AFTER_3_SEC);

    }

    private void tryToSavePreferences() {
        if (wherever != null && weather != null && whenever != null) {
            recentCitiesList.addUnique(wherever, weather, whenever);
            prefsManager.savePrefs(recentCitiesList);
            addCityToNavigationMenu();
        }
    }

    private void tryToUpdateDb() {
        if (wherever != null && weather != null && whenever != null) {
            dbManager.updateData(wherever, weather, whenever);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
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

        if (id == R.id.find_city_button) {
            Intent intent = new Intent(this, FindCityActivity.class);
            startActivityForResult(intent, FindCityActivity.REQUEST_CODE);
            return true;
        }

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
