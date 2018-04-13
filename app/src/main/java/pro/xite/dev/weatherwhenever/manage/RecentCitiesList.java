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

    public static final int MAX_RECENT_CITIES = 5;

    private ArrayList<Wherever> cities = new ArrayList<>();
    private ArrayList<Weather> weathers = new ArrayList<>();
    private ArrayList<Whenever> forecasts = new ArrayList<>();

    public int getCounter() {
        return counter;
    }

    private int counter = 0;

    public void addUnique(Wherever city, Weather weather, Whenever forecast) {
        int n = indexForCityByName(city.getName());
        if(n > -1) { // update existing city
            weathers.set(n, weather);
            forecasts.set(n, forecast);
        } else { // it is a new city
            if (counter == MAX_RECENT_CITIES) {
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
    }

    public ArrayList<Wherever> getCities() {
        return cities;
    }

    public Wherever getLatestCity() {
        return cities.size() > 0 ? cities.get(cities.size()-1) : null;
    }

    public Weather getLatestWeather() {
        return cities.size() > 0 ? weathers.get(weathers.size()-1) : null;
    }

    public Whenever getLatestForecast() {
        return cities.size() > 0 ? forecasts.get(forecasts.size()-1) : null;
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
