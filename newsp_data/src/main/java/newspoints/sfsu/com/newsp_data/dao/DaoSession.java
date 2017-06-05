package newspoints.sfsu.com.newsp_data.dao;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

import newspoints.sfsu.com.newsp_data.entities.NPAudio;
import newspoints.sfsu.com.newsp_data.entities.NPVideo;
import newspoints.sfsu.com.newsp_data.entities.Project;
import newspoints.sfsu.com.newsp_data.entities.User;


/**
 * {@inheritDoc}
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dBUserDaoConfig;
    private final DaoConfig videoDaoConfig;
    private final DaoConfig audioDaoConfig;
    private final DaoConfig projectDaoConfig;

    private final UserDao dBUserDao;
    private final VideoDao videoDao;
    private final AudioDao audioDao;
    private final ProjectDao projectDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig> daoConfigMap) {
        super(db);

        dBUserDaoConfig = daoConfigMap.get(UserDao.class).clone();
        dBUserDaoConfig.initIdentityScope(type);

        videoDaoConfig = daoConfigMap.get(VideoDao.class).clone();
        videoDaoConfig.initIdentityScope(type);

        audioDaoConfig = daoConfigMap.get(AudioDao.class).clone();
        audioDaoConfig.initIdentityScope(type);

        projectDaoConfig = daoConfigMap.get(ProjectDao.class).clone();
        projectDaoConfig.initIdentityScope(type);

        dBUserDao = new UserDao(dBUserDaoConfig, this);
        videoDao = new VideoDao(videoDaoConfig, this);
        audioDao = new AudioDao(audioDaoConfig, this);
        projectDao = new ProjectDao(projectDaoConfig, this);

        registerDao(User.class, dBUserDao);
        registerDao(NPVideo.class, videoDao);
        registerDao(NPAudio.class, audioDao);
        registerDao(Project.class, projectDao);
    }



    public void clear() {
        dBUserDaoConfig.getIdentityScope().clear();
        videoDaoConfig.getIdentityScope().clear();
        audioDaoConfig.getIdentityScope().clear();
        projectDaoConfig.getIdentityScope().clear();
    }

    public UserDao getDBUserDao() {
        return dBUserDao;
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }

    public AudioDao getAudioDao() {
        return audioDao;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }


}
