package UnfinishedObjectsTracker.repository;

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
    Project findById(@Valid Project id);
    Project findByDescription(String description);

    @Transactional
    @Query(value = "INSERT into ownership (user_id, project_id) VALUES ( :userId, :projectId)", nativeQuery = true)
    @Modifying
    void saveLink(@Param("userId") int userId, @Param("projectId") int projectId);

    @Query(value="SELECT project.project_id, project.title, project.description " +
            "FROM project " +
            "INNER JOIN ownership ON(project.project_id = ownership.project_id) " +
            "INNER JOIN user ON(ownership.user_id = user.user_id) " +
            "WHERE user.user_id = :userId", nativeQuery =true)
    public ArrayList<Project> listProjectsByUser(@Param("userId") int userId);


}