package pro.xite.dev.weatherwhenever.data;

import pro.xite.dev.weatherwhenever.manage.IDataProviderListener;

/**
 * Created by Roman Syrchin on 4/9/18.
 */
public interface IDataProvider {

    void setListener(IDataProviderListener dataProviderListener);
    void request(String criteria);
    void setRequestRate(long millis);

}
