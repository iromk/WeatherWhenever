package pro.xite.dev.weatherwhenever.data.owm;

import java.net.MalformedURLException;
import java.net.URL;

import pro.xite.dev.wjdp.WebJsonProvider;


/**
 * Created by Roman Syrchin on 3/26/18.
 */

abstract public class OwmDataProvider extends WebJsonProvider {

    /**
     * OWM API definitions
     */
    private static final String OWM_API_KEY = "62a13a0ac59c620b10c5f3680ec685f0";
    private static final String OWM_API_BASE_URL =
            "http://api.openweathermap.org/data/2.5/%s?appid=%s&q=%s%s";
    private static final String OWM_EXTRA_GET_PARAMS = "&units=metric";


    /**
     * OWM Json keys
     */
    private static final String RESPONSE_CODE_JSON_KEY = "cod";

    /**
     * Response codes
     */
    private static final int RESPONSE_CODE_200 = 200;

    private static final String TAG_TRACER = "TRACER/OWMDP";

    abstract protected String getApiAction();
    protected String getExtraGetParams() { return OWM_EXTRA_GET_PARAMS; }

    @Override
    protected URL getRequestUrl(String... criteria) {
        String urlString = String.format(OWM_API_BASE_URL,
                getApiAction(),
                OWM_API_KEY,
                criteria[0],
                getExtraGetParams());
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
