package newspoints.sfsu.com.newsp_data.manager;

import java.util.List;

import newspoints.sfsu.com.newsp_data.entities.Audio;
import newspoints.sfsu.com.newsp_data.entities.MyEvent;
import newspoints.sfsu.com.newsp_data.entities.NPVideo;
import newspoints.sfsu.com.newsp_data.entities.Project;
import newspoints.sfsu.com.newsp_data.entities.User;

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
     * @param NPVideo to be inserted
     */
    public NPVideo insertVideo(NPVideo NPVideo);

    /**
     * Update a NPVideo from the DB
     *
     * @param NPVideo to be updated
     */
    public void updateVideo(NPVideo NPVideo);


    /**
     * Delete a NPVideo with a certain id from the DB
     *
     * @param videoId of video to be deleted
     */
    public boolean deleteVideoById(Long videoId);


    /**
     * @param videoId - of the NPVideo we want to fetch
     * @return Return a NPVideo by its id
     */
    NPVideo getVideoById(Long videoId);


    /**
     * List all the NPVideo from the DB
     *
     * @return list of videos
     */
    public List<NPVideo> listVideos();


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
    NPVideo getAudioById(Long audioId);


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
    NPVideo getProjectById(Long projectId);


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
    NPVideo getEventById(Long eventId);


    /**
     * List all the Event from the DB
     *
     * @return list of events
     */
    public List<MyEvent> listEvents();
}
