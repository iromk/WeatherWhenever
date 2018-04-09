package pro.xite.dev.weatherwhenever.data;

import pro.xite.dev.weatherwhenever.manage.DataProviderListener;

/**
 * Created by Roman Syrchin on 4/9/18.
 */
public interface IDataProvider {

    void setListener(DataProviderListener dataProviderListener);
    void request(String criteria);
    void setRequestRate(long millis);

}
