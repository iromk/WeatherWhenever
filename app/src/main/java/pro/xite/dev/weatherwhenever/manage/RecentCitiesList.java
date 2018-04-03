package pro.xite.dev.weatherwhenever.manage;

import java.io.Serializable;
import java.util.ArrayList;

import pro.xite.dev.weatherwhenever.data.Whenever;
import pro.xite.dev.weatherwhenever.data.Wherever;
import pro.xite.dev.weatherwhenever.data.Weather;

/**
 * Created by Roman Syrchin on 3/30/18.
 */

/**
 * Holds last MAX_RECENT_CITIES allowing to retrieve weather data by city name.
 */
public class RecentCitiesList implements Serializable {

    private static final int MAX_RECENT_CITIES = 5;

    private ArrayList<Wherever> cities = new ArrayList<>();
    private ArrayList<Weather> weathers = new ArrayList<>();
    private ArrayList<Whenever> forecasts = new ArrayList<>();

    public int getCounter() {
        return counter;
    }

    private int counter = 0;

    public void add(Wherever city, Weather weather, Whenever forecast) {
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

    public ArrayList<Wherever> getCities() {
        return cities;
    }

    public Wherever getLatestCity() {
        return cities.get(cities.size()-1);
    }

    public Weather getLatestWeather() {
        return weathers.get(weathers.size()-1);
    }

    public Whenever getLatestForecast() {
        return forecasts.get(forecasts.size()-1);
    }

    private int indexForCityByName(String cityName) {
        if(counter > 0) {
            int n = 0;
            for(Wherever city : cities) {
                if (city.getName().equals(cityName))
                    return n;
                else n++;
            }
        }
        return -1;
    }

    public Wherever getCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return cities.get(n);
        }
        return null;
    }

    public Weather getWeatherForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return weathers.get(n);
        }
        return null;
    }

    public Whenever getForecastForCity(CharSequence cityName) {
        int n = indexForCityByName(cityName.toString());
        if(n > -1) {
            return forecasts.get(n);
        }
        return null;
    }
}
