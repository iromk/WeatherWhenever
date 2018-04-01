package pro.xite.dev.weatherwhenever;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pro.xite.dev.weatherwhenever.data.CityInfo;
import pro.xite.dev.weatherwhenever.data.ForecastInfo;
import pro.xite.dev.weatherwhenever.data.InterfaceAdapter;
import pro.xite.dev.weatherwhenever.data.WeatherInfo;
import pro.xite.dev.weatherwhenever.data.owm.OwmCity;
import pro.xite.dev.weatherwhenever.data.owm.OwmNearestForecast;
import pro.xite.dev.weatherwhenever.data.owm.OwmWeather;

/**
 * Created by Roman Syrchin on 3/30/18.
 */
public class PrefsManager {

    private static final String RECENT_CITIES_LIST = "RecentCitiesList";
    private final SharedPreferences sharedPreferences;

    public PrefsManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public RecentCitiesList loadRecentCitiesList() {
        final String gson = sharedPreferences.getString(RECENT_CITIES_LIST, "");
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(CityInfo.class, new InterfaceAdapter<>())
                .registerTypeAdapter(ForecastInfo.class, new InterfaceAdapter<>())
                .registerTypeAdapter(WeatherInfo.class, new InterfaceAdapter<>());
        return gsonBuilder.create().fromJson(gson, RecentCitiesList.class);
    }

    public void savePrefs(@NonNull final RecentCitiesList recentCitiesList) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(CityInfo.class, new InterfaceAdapter<>())
                .registerTypeAdapter(ForecastInfo.class, new InterfaceAdapter<>())
                .registerTypeAdapter(WeatherInfo.class, new InterfaceAdapter<>());
        final String gson = gsonBuilder.create().toJson(recentCitiesList);
        editor.putString(RECENT_CITIES_LIST, gson);
        editor.apply();
    }

}
