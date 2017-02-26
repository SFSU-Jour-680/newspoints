package newspoints.sfsu.com.newsp_lib.bus;

import org.greenrobot.eventbus.EventBus;

/**
 * Singleton BusProvider to create the synchronized EventBus instance
 * <p/>
 * Created by Pavitra on 4/12/2016.
 */
public class BusProvider {
    private static volatile EventBus mInstance;

    private BusProvider() {
    }

    public static EventBus bus() {
        EventBus localInstance = mInstance;
        if (localInstance == null) {
            synchronized (EventBus.class) {
                localInstance = mInstance;
                if (localInstance == null) {
                    mInstance = localInstance = EventBus.getDefault();
                }
            }
        }
        return localInstance;
    }
}
