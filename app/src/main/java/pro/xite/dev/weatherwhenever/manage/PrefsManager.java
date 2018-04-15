package pro.xite.dev.weatherwhenever.manage;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Weather;

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
        if(gson.length() == 0)
            return new RecentCitiesList();
        else
            return buildGsonWithAdapters().fromJson(gson, RecentCitiesList.class);
    }

    public void savePrefs(@NonNull final RecentCitiesList recentCitiesList) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String gson = buildGsonWithAdapters().toJson(recentCitiesList);
        editor.putString(RECENT_CITIES_LIST, gson);
        editor.apply();
    }

    private Gson buildGsonWithAdapters() {
        return new GsonBuilder()
                .registerTypeAdapter(Wherever.class, new InterfaceAdapter<>())
                .registerTypeAdapter(Whenever.class, new InterfaceAdapter<>())
                .registerTypeAdapter(Weather.class, new InterfaceAdapter<>())
                .create();
    }

}
