package pro.xite.dev.weatherwhenever;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

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
        return new Gson().fromJson(gson, RecentCitiesList.class);
    }

    public void savePrefs(@NonNull final RecentCitiesList recentCitiesList) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String gson = new Gson().toJson(recentCitiesList);
        editor.putString(RECENT_CITIES_LIST, gson);
        editor.apply();
    }

}
