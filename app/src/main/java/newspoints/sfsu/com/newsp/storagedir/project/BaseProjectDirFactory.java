package newspoints.sfsu.com.newsp.storagedir.project;

import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Factory class to create Application root directory, Project directory specific to each {@link newspoints.sfsu.com.newsp_data.entities.Project}
 * and subdirectories that defines each categories.
 * <p>
 * Created by Pavitra on 4/10/2016.
 */
public class BaseProjectDirFactory extends ProjectStorageDirFactory {
    // Standard storage location for project files
    private static final String ROOT_DIR = "journalists";
    private static final String TAG = "~!@#$BaseProjDir";


    @Override
    public File getAppRootDirectory() {
        return new File(Environment.getExternalStorageDirectory() + "/" + ROOT_DIR);
    }

    @Override
    public File getProjectStorageDirectory(String projectName) {
        return new File(Environment.getExternalStorageDirectory() + "/" + ROOT_DIR + "/" + projectName);
    }

    @Override
    public File getProjectCategoryStorageDirectory(String projectName, String category) {
        return new File(Environment.getExternalStorageDirectory() + "/" + ROOT_DIR + "/" + projectName + "/" + category);
    }

    /**
     * Creates root directory for the application by the name <tt>'journalists'</tt>. If the directory is already present, then it
     * simply returns
     *
     * @return
     */
    public File createAppRootDirectory() {
        File projectDir = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            projectDir = getAppRootDirectory();
            if (projectDir != null) {
                if (!projectDir.mkdirs()) {
                    if (!projectDir.exists()) {
                        return null;
                    }
                }
            }
        } else {
            Log.i(TAG, "getProjectDir: External storage is not mounted READ?WRITE");
        }
        return projectDir;
    }


}
