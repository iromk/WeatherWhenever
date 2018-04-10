package pro.xite.dev.weatherwhenever.data.owm;

/**
 * Created by Roman Syrchin on 3/26/18.
 */

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

    @Override
    protected Class<?> getTargetClass() {
        return OwmWeather.class;
    }

}
