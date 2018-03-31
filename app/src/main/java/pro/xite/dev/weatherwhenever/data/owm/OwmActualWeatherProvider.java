package pro.xite.dev.weatherwhenever.data.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import android.os.Handler;

import pro.xite.dev.weatherwhenever.LeakSafeHandler;
import pro.xite.dev.weatherwhenever.ViewUpdatable;

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

    public void request(String city, Handler handler) {
        requestOWM(city, OwmWeather.class, handler);
    }

    public void request(String city, ViewUpdatable activity) {
        requestOWM(city, OwmWeather.class,
                new LeakSafeHandler<OwmWeather>(activity));
    }

}
