package pro.xite.dev.weatherwhenever.manage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.Serializable;
import java.lang.ref.WeakReference;

import pro.xite.dev.weatherwhenever.data.Weather;

/**
 * Created by Roman Syrchin on 3/27/18.
 */

public class LeakSafeHandler<T extends Serializable> extends Handler {

    private WeakReference<DataReceiver> weakReference;

    public LeakSafeHandler(DataReceiver activity) {
        weakReference = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg) {
        DataReceiver activity = weakReference.get();
        if(activity != null) {
            final Bundle bundle = msg.getData();
            final T object = (T) bundle.getSerializable(Weather.DATA_KEY); // TODO check cast
            activity.serializedDataReceiver(object);
            super.handleMessage(msg);
        }
    }
}
