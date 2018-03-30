package pro.xite.dev.weatherwhenever;

import java.io.Serializable;
import java.util.ArrayDeque;

import pro.xite.dev.weatherwhenever.owm.OWMCity;
import pro.xite.dev.weatherwhenever.owm.OWMNearestForecast;
import pro.xite.dev.weatherwhenever.owm.OWMWeather;

/**
 * Created by Roman Syrchin on 3/30/18.
 */
public class RecentCitiesList implements Serializable {

    private static final int MAX_RECENT_CITIES = 5;
    // TODO try to find a way to not be singleton
    private static RecentCitiesList instance;

    private RecentCitiesList() {}

    public static RecentCitiesList getInstance() {
        if(instance == null)
            instance = new RecentCitiesList();
        return instance;
    }

    private ArrayDeque<OWMCity> cities = new ArrayDeque<>();
    private ArrayDeque<OWMWeather> weathers = new ArrayDeque<>();
    private ArrayDeque<OWMNearestForecast> forecasts = new ArrayDeque<>();
    private int counter = 0;

    public void add(OWMCity city, OWMWeather weather, OWMNearestForecast forecast) {
        if(counter == MAX_RECENT_CITIES) {
            cities.removeFirst();
            weathers.removeFirst();
            forecasts.removeFirst();
            counter--;
        }
        counter++;
        cities.add(city);
        weathers.add(weather);
        forecasts.add(forecast);
    }
}
