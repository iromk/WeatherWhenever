package pro.xite.dev.weatherwhenever.data.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import pro.xite.dev.weatherwhenever.manage.LeakSafeHandler;
import pro.xite.dev.weatherwhenever.manage.DataProviderListener;

/**
 * Data loader and parser for Current weather data provided by OWM.
 */
public class OwmActualWeatherProvider extends OwmDataProvider {

    /**
     * Api url data identifier name.
     */
    private static final String OWM_ACTION_WEATHER = "weather";

    @Override
    protected String getApiAction() { return OWM_ACTION_WEATHER; }

    public void request(String city, DataProviderListener activity) {
        requestOwmService(city, OwmCity.class,
                new LeakSafeHandler<OwmWeather>(activity));
        requestOwmService(city, OwmWeather.class,
                new LeakSafeHandler<OwmWeather>(activity));
    }

}
