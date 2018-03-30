package pro.xite.dev.weatherwhenever;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
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

    private ArrayList<OWMCity> cities = new ArrayList<>();
    private ArrayList<OWMWeather> weathers = new ArrayList<>();
    private ArrayList<OWMNearestForecast> forecasts = new ArrayList<>();
    private int counter = 0;

    public void add(OWMCity city, OWMWeather weather, OWMNearestForecast forecast) {
        if(counter == MAX_RECENT_CITIES) {
            cities.remove(0);
            weathers.remove(0);
            forecasts.remove(0);
            counter--;
        }
        counter++;
        cities.add(city);
        weathers.add(weather);
        forecasts.add(forecast);
    }

    public ArrayList<OWMCity> getCities() {
        return cities;
    }

    public OWMCity getLatestCity() {
        return cities.get(cities.size()-1);
    }

    public OWMWeather getLatestWeather() {
        return weathers.get(weathers.size()-1);
    }

    public OWMNearestForecast getLatestForecast() {
        return forecasts.get(forecasts.size()-1);
    }

    private int indexForCityByName(String cityName) {
        if(counter > 0) {
            int n = 0;
            for(OWMCity city : cities) {
                if (city.getName().equals(cityName))
                    return n;
                else n++;
            }
        }
        return -1;
    }

    public OWMCity getCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return cities.get(n);
        }
        return new OWMCity();
    }

    public OWMWeather getWeatherForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return weathers.get(n);
        }
        return new OWMWeather();
    }

    public OWMNearestForecast getForecastForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return forecasts.get(n);
        }
        return new OWMNearestForecast();
    }
}
