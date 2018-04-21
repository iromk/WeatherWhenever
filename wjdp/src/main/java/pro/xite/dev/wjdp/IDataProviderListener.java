package pro.xite.dev.wjdp;

import java.io.Serializable;

/**
 * Created by Roman Syrchin on 3/27/18.
 */

public interface IDataProviderListener {

    String KEY_SERIALIZABLE = "IDPL/SERIAL";

    void onSerializedDataReceived(Serializable object);

}
