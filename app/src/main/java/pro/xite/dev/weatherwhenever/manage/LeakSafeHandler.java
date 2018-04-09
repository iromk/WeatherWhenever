package pro.xite.dev.weatherwhenever.manage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.io.Serializable;
import java.lang.ref.WeakReference;

/**
 * Created by Roman Syrchin on 3/27/18.
 */

public class LeakSafeHandler extends Handler {

    private WeakReference<IDataProviderListener> weakReference;

    public LeakSafeHandler(IDataProviderListener listener) {
        weakReference = new WeakReference<>(listener);
    }

    @Override
    public void handleMessage(Message msg) {
        IDataProviderListener activity = weakReference.get();
        if(activity != null) {
            final Bundle bundle = msg.getData();
            final Serializable object = bundle.getSerializable(IDataProviderListener.KEY_SERIALIZABLE);
            activity.onSerializedDataReceived(object);
            super.handleMessage(msg);
        }
    }
}
