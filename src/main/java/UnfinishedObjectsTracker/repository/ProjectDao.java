package UnfinishedObjectsTracker.repository;

import UnfinishedObjectsTracker.models.Post;
import UnfinishedObjectsTracker.models.Project;
import UnfinishedObjectsTracker.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;

@Repository
public interface ProjectDao extends JpaRepository<Project, Long> {

    User findByTitle(String title);
    Project findById(int id);
    Project findByDescription(String description);
    /*
     * This query ties a project to the specified user
     */
    @Transactional
    @Query(value = "INSERT into ownership (user_id, project_id) VALUES ( :userId, :projectId)", nativeQuery = true)
    @Modifying
    void createNewProject(@Param("userId") int userId, @Param("projectId") int projectId);

    /*
     * This query shows the list of projects by user
     */
    @Query(value="SELECT project.project_id, project.title, project.description, project.creation_date, project.percent_complete " +
            "FROM project " +
            "INNER JOIN ownership ON(project.project_id = ownership.project_id) " +
            "INNER JOIN user ON(ownership.user_id = user.user_id) " +
            "WHERE user.user_id = :userId", nativeQuery =true)
    ArrayList<Project> listProjectsByUser(@Param("userId") int userId);

    /*
     * Updates a project with new information
     */
    @Transactional
    @Query(value="UPDATE project SET project.title = :title,  project.description = :description WHERE project.project_id = :id", nativeQuery =true)
    @Modifying(clearAutomatically = true)
    int updateProject(@Param("id") int id, @Param("title")String title, @Param("description")String description);


    @Transactional
    @Query(value="UPDATE project SET project.percent_complete = :percentComplete WHERE project.project_id = :id", nativeQuery =true)
    @Modifying(clearAutomatically = true)
    int updateProjectProgress(@Param("id") int id, @Param("percentComplete") int percentComplete);
    /*
    Deletes a project from the database in project and ownership tables
     */
    @Transactional
    @Modifying
    @Query(value ="DELETE project, ownership " +
            "FROM project INNER JOIN  ownership " +
            "ON project.project_id = ownership.project_id " +
            "WHERE project.project_id = :id", nativeQuery = true)
    int deleteProject(@Param("id") int id);


}


