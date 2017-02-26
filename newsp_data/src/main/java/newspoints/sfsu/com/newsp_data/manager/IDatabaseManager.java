package newspoints.sfsu.com.newsp_data.manager;

import com.newspoints.journalist.entities.Audio;
import com.newspoints.journalist.entities.MyEvent;
import com.newspoints.journalist.entities.MyVideo;
import com.newspoints.journalist.entities.Project;
import com.newspoints.journalist.entities.User;

import java.util.List;

/**
 * Interface that provides methods for managing the database inside the Application. All the methods for carrying out
 * <tt>CRUD</tt> operations on a specific model are defined in this interface.
 */
public interface IDatabaseManager {

    /**
     * Closing available connections
     */
    public void closeDbConnections();

    /**
     * Delete all tables and content from our database
     */
    public void dropDatabase();

    // ============================================ USER ============================================

    /**
     * Insert a user into the DB
     *
     * @param user to be inserted
     */
    public User insertUser(User user);

    /**
     * Update a user from the DB
     *
     * @param user to be updated
     */
    public void updateUser(User user);

    /**
     * Delete all users with a certain email from the DB
     *
     * @param email of users to be deleted
     */
    public void deleteUserByEmail(String email);

    /**
     * Delete a user with a certain id from the DB
     *
     * @param userId of users to be deleted
     */
    public boolean deleteUserById(Long userId);

    /**
     * @param userId - of the user we want to fetch
     * @return Return a user by its id
     */
    User getUserById(Long userId);


    // ============================================ VIDEO ============================================

    /**
     * Insert a user into the DB
     *
     * @param myVideo to be inserted
     */
    public MyVideo insertVideo(MyVideo myVideo);

    /**
     * Update a myVideo from the DB
     *
     * @param myVideo to be updated
     */
    public void updateVideo(MyVideo myVideo);


    /**
     * Delete a MyVideo with a certain id from the DB
     *
     * @param videoId of video to be deleted
     */
    public boolean deleteVideoById(Long videoId);


    /**
     * @param videoId - of the MyVideo we want to fetch
     * @return Return a MyVideo by its id
     */
    MyVideo getVideoById(Long videoId);


    /**
     * List all the MyVideo from the DB
     *
     * @return list of videos
     */
    public List<MyVideo> listVideos();


    // ============================================ AUDIO ============================================

    /**
     * Insert a Audio into the DB
     *
     * @param audio to be inserted
     */
    public Audio insertAudio(Audio audio);

    /**
     * Update a audio from the DB
     *
     * @param audio to be updated
     */
    public void updateAudio(Audio audio);


    /**
     * Delete a Audio with a certain id from the DB
     *
     * @param audioId of audio to be deleted
     */
    public boolean deleteAudioById(Long audioId);


    /**
     * @param audioId - of the Audio we want to fetch
     * @return Return a Audio by its id
     */
    MyVideo getAudioById(Long audioId);


    /**
     * List all the Audio from the DB
     *
     * @return list of audios
     */
    public List<Audio> listAudios();

    // ============================================ PROJECT ============================================

    /**
     * Insert a Project into the DB
     *
     * @param project to be inserted
     */
    public Audio insertProject(Project project);

    /**
     * Update a project from the DB
     *
     * @param project to be updated
     */
    public void updateProject(Project project);


    /**
     * Delete a Project with a certain id from the DB
     *
     * @param projectId of project to be deleted
     */
    public boolean deleteProjectById(Project projectId);


    /**
     * @param projectId - of the Project we want to fetch
     * @return Return a Project by its id
     */
    MyVideo getProjectById(Long projectId);


    /**
     * List all the Project from the DB
     *
     * @return list of projects
     */
    public List<Project> listProjects();


    // ============================================ EVENT ============================================

    /**
     * Insert a Event into the DB
     *
     * @param event to be inserted
     */
    public Audio insertEvent(MyEvent event);

    /**
     * Update a event from the DB
     *
     * @param event to be updated
     */
    public void updateEvent(MyEvent event);


    /**
     * Delete a Event with a certain id from the DB
     *
     * @param eventId of Event to be deleted
     */
    public boolean deleteEventById(MyEvent eventId);


    /**
     * @param eventId - of the Event we want to fetch
     * @return Return a Event by its id
     */
    MyVideo getEventById(Long eventId);


    /**
     * List all the Event from the DB
     *
     * @return list of events
     */
    public List<MyEvent> listEvents();
}
