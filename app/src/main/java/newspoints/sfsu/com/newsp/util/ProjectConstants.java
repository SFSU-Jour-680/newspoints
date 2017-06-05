package newspoints.sfsu.com.newsp.util;

import android.graphics.Typeface;
import android.os.Environment;

import java.util.HashSet;
import java.util.Set;

public class ProjectConstants {

    public static final String SOURCE = "source";
    public static final String SHOT = "shot";
    public static final String QUESTION = "question";
    public static final String CATEGORY = "category";
    public static final String SELECTED_CATEGORY = "selected_category";
    public static final String PROJECT_NAME = "project name";
    public static final String PROJECT_ID = "PID";
    public static final String SELECTED_IMAGE_PATH = "image path";
    public static final String XML_IMPORTED = "xml_imported";
    public static final String mainDirPath = "/storage/emulated/0/journalist/";


    // cloud storage option
    public static final int CLOUD_GOOGLE_DRIVE = 0x47;
    public static final int CLOUD_DROPBOX = 0x44;
    public static final int CLOUD_NOT_UPLOADED = 0xFF;
    // DropBox API keys
    final static public String DROPBOX_APP_KEY = " Dropbox App Key here";
    final static public String DROPBOX_APP_SECRET = " Dropbox App Secret";
    public static final String ACCESS_KEY_NAME = "dropbox_access_key";
    public static final String ACCESS_SECRET_NAME = "dropbox_access_secret";
    public static final String ACCOUNT_PREFS_NAME = "Dropbox_key_secret_pref_file";

    // code for Video, NPAudio, Shots, Sources and Questions
    public static final int AUDIO_CAPTURE_CODE = 11;
    public static final int VIDEO_RECORD_CODE = 12;
    public static final int SHOTS_CODE = 13;
    public static final int SOURCES_CODE = 14;
    public static final int QUESTIONS_CODE = 15;

    public static String selectedProjectName = "";
    public static String selectedProjectPath = "";
    public static String selectedProjectCategory = "";
    public static String selectedProjectCateogryPath = "";
    // Typeface
    public static Typeface ROBOT_MEDIUM;
    public static Typeface ROBOT_REGULAR;
    public static Set<String> shots = new HashSet<>();
    public static Set<String> sources = new HashSet<>();
    public static Set<String> questions = new HashSet<>();
    // MyVideo recording Prefix
    public static String VIDEO_RECORDING_ID = "MyVideo Recording ";

    public static Set<String> getShots() {
        shots.add("establishing");
        shots.add("wide");
        shots.add("medium");
        shots.add("close");
        shots.add("xclose");
        shots.add("game-action");
        shots.add("interview");
        shots.add("reverse");
        shots.add("closeshot");
        shots.add("Back_Stage");
        shots.add("Extra_Wide");
        shots.add("Disaster_Shot");
        shots.add("Intense_Fire");
        shots.add("Light_Fire");
        shots.add("Close_shot");

        return shots;
    }

    public static Set<String> getSources() {
        sources.add("Brother");
        sources.add("Mother");
        sources.add("Sister");
        sources.add("First_disaster");
        sources.add("Chief");
        sources.add("Second_Disaster");
        sources.add("First_Fire");
        sources.add("Chief_Fire");
        sources.add("Second_Fire");
        sources.add("coach");
        sources.add("head_coach");
        sources.add("management");
        sources.add("media_relations");
        sources.add("official");
        sources.add("player");
        sources.add("victim");
        sources.add("victim");
        sources.add("witness");
        sources.add("neighbor");
        sources.add("official");
        sources.add("firefighter");
        sources.add("captain");
        sources.add("chief");
        sources.add("police");
        sources.add("protester");
        sources.add("moderator");
        sources.add("official");
        sources.add("panelist");
        sources.add("participant");
        sources.add("performer");
        sources.add("artist");
        sources.add("supporter");
        sources.add("organizer");
        sources.add("funder");
        sources.add("family");
        sources.add("audience");

        return sources;
    }

    public static Set<String> getQuestions() {
        questions.add("age");
        questions.add("");
        questions.add("placeholder");
        questions.add("Time");
        questions.add("Place");
        questions.add("People_involved");
        questions.add("assess_performance");
        questions.add("turning_point");
        questions.add("areas_improve");
        questions.add("key_contributors");
        questions.add("takeaways");
        questions.add("noah");
        questions.add("spelling");
        questions.add("fire_started");
        questions.add("people_injured_killed");
        questions.add("arson_suspected");
        questions.add("unsafe_conditions");
        questions.add("how_escape");
        questions.add("unsafe_escape");
        questions.add("what_lost");
        questions.add("where_live");
        questions.add("contact");
        questions.add("spelling");
        questions.add("meeting_reason");
        questions.add("why_protest");
        questions.add("how_affect");
        questions.add("your_position");
        questions.add("what-is-new");
        questions.add("contact");
        questions.add("significance");
        questions.add("your_role");
        questions.add("your_role");
        questions.add("how_long_perform");
        questions.add("how_important_performing");
        questions.add("people_participating");
        questions.add("other_locations");
        questions.add("other_locations");
        questions.add("police_participating");
        questions.add("peaceful");
        questions.add("arrests");


        return questions;
    }

    // method to generate the Path of the video for specific project and category
    public static String getAbsolutePathToVideo(String projectName) {
        final String SYSTEM_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

        // get Project ID and Category in order to build the absolute path
        String projectDetails[] = ProjectConstants.dbClass.getProjectMainDetails(projectName);

        // finally build the path
        String selectedVideoPath = SYSTEM_PATH + "/journalist/" + projectName + "/" +
                projectDetails[1] + "/";

        return selectedVideoPath;
    }

}
