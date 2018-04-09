package pro.xite.dev.weatherwhenever.manage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import pro.xite.dev.weatherwhenever.data.WebJsonProvider;

/**
 * Created by Roman Syrchin on 3/27/18.
 */

public class LeakSafeHandler<T extends Serializable> extends Handler {

    private WeakReference<DataProviderListener> weakReference;

    public LeakSafeHandler(DataProviderListener activity) {
        weakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        DataProviderListener activity = weakReference.get();
        if(activity != null) {
            final Bundle bundle = msg.getData();
            final Serializable object = bundle.getSerializable(WebJsonProvider.DATA_KEY);
            activity.onSerializedDataReceived(object);
            super.handleMessage(msg);
        }
    }
}
