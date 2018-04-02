package pro.xite.dev.weatherwhenever.manage;

import java.io.Serializable;

/**
 * Created by Roman Syrchin on 3/27/18.
 */

public interface DataReceiver {

    void onSerializedDataReceived(Serializable object);

}
