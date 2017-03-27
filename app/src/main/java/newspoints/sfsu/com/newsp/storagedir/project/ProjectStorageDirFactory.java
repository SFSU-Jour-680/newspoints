package newspoints.sfsu.com.newsp.storagedir.project;

import java.io.File;

/**
 * Defines the project storage directory of all the projects created in Newspoints application
 * <p>
 * Created by Pavitra on 4/10/2016.
 */

public abstract class ProjectStorageDirFactory {

    /**
     * Returns the root directory of application
     *
     * @return
     */
    public abstract File getAppRootDirectory();

    /**
     * Returns the root directory of each project
     *
     * @param projectName
     * @return
     */
    public abstract File getProjectStorageDirectory(String projectName);

    /**
     * Returns the sub directory of each {@link com.newspoints.journalist.entities.Project} that contains the corresponding
     * category folders
     *
     * @param category
     * @return
     */
    public abstract File getProjectCategoryStorageDirectory(String projectName, String category);
}
