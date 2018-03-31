package pro.xite.dev.weatherwhenever;

import java.io.Serializable;
import java.util.ArrayList;

import pro.xite.dev.weatherwhenever.owm.OwmCity;
import pro.xite.dev.weatherwhenever.owm.OwmNearestForecast;
import pro.xite.dev.weatherwhenever.owm.OwmWeather;

/**
 * Created by Roman Syrchin on 3/30/18.
 */
public class RecentCitiesList implements Serializable {

    private static final int MAX_RECENT_CITIES = 5;

    private ArrayList<OwmCity> cities = new ArrayList<>();
    private ArrayList<OwmWeather> weathers = new ArrayList<>();
    private ArrayList<OwmNearestForecast> forecasts = new ArrayList<>();
    private int counter = 0;

    public void add(OwmCity city, OwmWeather weather, OwmNearestForecast forecast) {
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

    public ArrayList<OwmCity> getCities() {
        return cities;
    }

    public OwmCity getLatestCity() {
        return cities.get(cities.size()-1);
    }

    public OwmWeather getLatestWeather() {
        return weathers.get(weathers.size()-1);
    }

    public OwmNearestForecast getLatestForecast() {
        return forecasts.get(forecasts.size()-1);
    }

    private int indexForCityByName(String cityName) {
        if(counter > 0) {
            int n = 0;
            for(OwmCity city : cities) {
                if (city.getName().equals(cityName))
                    return n;
                else n++;
            }
        }
        return -1;
    }

    public OwmCity getCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return cities.get(n);
        }
        return new OwmCity();
    }

    public OwmWeather getWeatherForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return weathers.get(n);
        }
        return new OwmWeather();
    }

    public OwmNearestForecast getForecastForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return forecasts.get(n);
        }
        return new OwmNearestForecast();
    }
}
