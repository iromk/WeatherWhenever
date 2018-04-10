package pro.xite.dev.weatherwhenever.data.ods;

import android.util.Log;

import java.net.URL;
import java.util.Locale;

import pro.xite.dev.weatherwhenever.data.WebJsonProvider;
import pro.xite.dev.weatherwhenever.manage.IDataProviderListener;

/**
 * Created by Roman Syrchin on 4/8/18.
 */
public class OdsCityProvider extends WebJsonProvider {

    /**
     * OpenDataSoft API definitions
     */
    private static final String ODS_RECORD_SEARCH_API_BASE_URL =
            "https://data.opendatasoft.com/api/records/1.0/search/";

    private static final String GET_PARAMS_TEMPLATE = "?dataset=%s&rows=%d&%s";
    private static final String GET_CRITERIA_TEMPLATE = "&q=%s";

    private static final String ODS_DATASET_ID = "geonames-all-cities-with-a-population-1000@public";
    private static final int ODS_RESULT_ROWS_LIMIT = 8;
    private static final String ODS_EXTRA_GET_PARAMS = "&facet=country";

    /**
     * Response codes
     */
    private static final int RESPONSE_CODE_200 = 200;

    private static final String TAG_TRACER = "TRACER/ODS";

    @Override
    protected URL getRequestUrl(String... criteria) {
        final StringBuilder getRequestUrl = new StringBuilder(ODS_RECORD_SEARCH_API_BASE_URL);
        getRequestUrl.append(String.format(Locale.getDefault(), GET_PARAMS_TEMPLATE,
                            ODS_DATASET_ID,
                            ODS_RESULT_ROWS_LIMIT,
                            ODS_EXTRA_GET_PARAMS));

        if(criteria.length > 0) {
            getRequestUrl.append(String.format(GET_CRITERIA_TEMPLATE, criteria[0]));
        } else {
            Log.d(TAG_TRACER, "getRequestUrl: no criteria were specified, result will be unpredictable. You warned :P");
        }

        try {
            return new URL(getRequestUrl.toString());

        } catch (Exception e) {
            Log.d(TAG_TRACER, String.format("getRequestUrl: malformed url string [%s]", getRequestUrl.toString()));
            return null;
        }
    }

    @Override
    protected Class<?> getTargetClass() {
        return OdsResponse.class;
    }

    public static void asyncRequest(IDataProviderListener listener, String cityname) {
        getInstance().asyncRequest(listener, cityname);
    }

    private static WebJsonProvider getInstance() {
        return new OdsCityProvider(); // it could be more sophisticated when needed.
    }

}
