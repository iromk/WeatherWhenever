package pro.xite.dev.weatherwhenever.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

import android.os.Handler;

import pro.xite.dev.weatherwhenever.LeakSafeHandler;
import pro.xite.dev.weatherwhenever.ViewUpdatable;

/**
 * Data loader and parser for Current weather data provided by OWM.
 */
public class OWMActualWeatherProvider extends OWMDataProvider {

    /**
     * Api url data identifier name.
     */
    private static final String OWM_ACTION_WEATHER = "weather";

    @Override
    protected String getApiAction() { return OWM_ACTION_WEATHER; }

    public void request(String city, Handler handler) {
        requestOWM(city, OWMWeather.class, handler);
    }

    public void request(String city, ViewUpdatable activity) {
        requestOWM(city, OWMWeather.class,
                new LeakSafeHandler<OWMWeather>(activity));
    }

}
