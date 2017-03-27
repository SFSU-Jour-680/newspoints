package newspoints.sfsu.com.newsp.event;

import java.util.ArrayList;

/**
 * Event to publish the sources selected by the User in order to attach to Video or Audio
 * <p/>
 * Created by Pavitra on 4/12/2016.
 */
public class QuestionsEvent extends BaseEvent {
    public static final OnLoadingError FAILED = new OnLoadingError(UNHANDLED_MSG, UNHANDLED_CODE);

    public static class OnAudioQuestionsLoaded extends OnDone<String> {
        public OnAudioQuestionsLoaded(ArrayList<String> selectedQuestionsList) {
            super(selectedQuestionsList);
        }
    }

    public static class OnVideoQuesLoaded extends OnDone<String> {
        public OnVideoQuesLoaded(ArrayList<String> selectedQuestionsList) {
            super(selectedQuestionsList);
        }
    }

    public static class OnLoadingError extends OnFailed {
        public OnLoadingError(String errorMessage, int code) {
            super(errorMessage, code);
        }
    }

}
