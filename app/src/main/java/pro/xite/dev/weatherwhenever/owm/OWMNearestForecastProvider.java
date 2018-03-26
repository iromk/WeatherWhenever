package pro.xite.dev.weatherwhenever.owm;

import android.os.Handler;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

public class OWMNearestForecastProvider extends OWMDataProvider {

    /**
     * Api url data identifier name.
     */
    private static final String OWM_ACTION_WEATHER = "forecast";

    @Override
    protected String getApiAction() { return OWM_ACTION_WEATHER; }

    public void request(String city, Handler handler) {
        requestOWM(city, OWMNearestForecast.class, handler);
    }


}
