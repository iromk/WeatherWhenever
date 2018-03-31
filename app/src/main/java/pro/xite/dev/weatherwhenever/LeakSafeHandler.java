package pro.xite.dev.weatherwhenever;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import pro.xite.dev.weatherwhenever.data.owm.OwmData;
import pro.xite.dev.weatherwhenever.data.owm.OwmDataProvider;

/**
 * Created by Roman Syrchin on 3/27/18.
 */

public class LeakSafeHandler<T extends OwmData> extends Handler {

    private WeakReference<ViewUpdatable> weakReference;

    public LeakSafeHandler(ViewUpdatable activity) {
        weakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        ViewUpdatable activity = weakReference.get();
        if(activity != null) {
            final Bundle bundle = msg.getData();
            final T owm = (T) bundle.getSerializable(OwmDataProvider.OWM_DATA_KEY); // TODO check cast
            activity.updateViews(owm);
            super.handleMessage(msg);
        }
    }
}
