package newspoints.sfsu.com.newsp.storagedir.image;

import java.io.File;

/**
 * Defines the album storage directory of the images captured using the Camera..
 */
public abstract class AlbumStorageDirFactory {
    /**
     * Returns the storage directory of all the images belonging to Newspoints application
     *
     * @param albumName
     * @return
     */
    public abstract File getAlbumStorageDir(String albumName);
}
