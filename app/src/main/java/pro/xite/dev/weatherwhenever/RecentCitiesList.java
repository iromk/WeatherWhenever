package pro.xite.dev.weatherwhenever;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.function.Consumer;

import pro.xite.dev.weatherwhenever.owm.OWMCity;
import pro.xite.dev.weatherwhenever.owm.OWMNearestForecast;
import pro.xite.dev.weatherwhenever.owm.OWMWeather;

/**
 * Created by Roman Syrchin on 3/30/18.
 */
public class RecentCitiesList implements Serializable {

    private static final int MAX_RECENT_CITIES = 5;

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

    public ArrayDeque<OWMCity> getCities() {
        return cities;
    }

    public OWMCity getLatestCity() {
        return cities.getLast();
    }

    public OWMWeather getLatestWeather() {
        return weathers.getLast();
    }

    public OWMNearestForecast getLatestForecast() {
        return forecasts.getLast();
    }
}
