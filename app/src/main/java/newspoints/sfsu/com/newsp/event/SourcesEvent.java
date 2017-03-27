package newspoints.sfsu.com.newsp.event;

import java.util.ArrayList;

/**
 * Event to publish the sources selected by the User in order to attach to Video or Audio
 * <p>
 * Created by Pavitra on 4/12/2016.
 */
public class SourcesEvent extends BaseEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnListSelected extends OnDone<String> {
        public OnListSelected(ArrayList sourcesList) {
            super(sourcesList);
        }
    }

    public static class OnAudioSourcesLoaded extends OnDone<String> {
        public OnAudioSourcesLoaded(ArrayList<String> selectedSourcesList) {
            super(selectedSourcesList);
        }
    }

    public static class OnVideoSourcesLoaded extends OnDone<String> {
        public OnVideoSourcesLoaded(ArrayList<String> selectedSourcesList) {
            super(selectedSourcesList);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }
}
