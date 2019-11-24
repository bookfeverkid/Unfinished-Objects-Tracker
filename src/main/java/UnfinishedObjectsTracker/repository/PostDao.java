package UnfinishedObjectsTracker.repository;

import UnfinishedObjectsTracker.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Repository
public interface PostDao extends JpaRepository<Post, Long> {


    Post findById(int id);

    Post findByProjectId(int projectId);

    /*
     * This query ties a post to the specified project
     */
    @Transactional
    @Query(value = "INSERT into post (project_id) VALUES ( :projectId)", nativeQuery = true)
    @Modifying
    void createNewPost(@Param("projectId") int projectId);


    @Query(value = "SELECT post.post_id, post.title, post.description, post.post_date, post.project_id " +
            "FROM post " +
            "INNER JOIN project ON(post.project_id = project.project_id)" +
            "WHERE project.project_id = :projectId", nativeQuery = true)
    ArrayList<Post> listPostsByProjectId(@Param("projectId") int projectId);

    @Transactional
    @Query(value="UPDATE post SET post.title = :title,  post.description = :description WHERE post.post_id = :id", nativeQuery =true)
    @Modifying(clearAutomatically = true)
    int updatePost(@Param("id") int id, @Param("title")String title, @Param("description")String description);


    @Transactional
    @Modifying
    @Query(value ="DELETE post " +
            "FROM post " +
            "WHERE post.project_id = :id", nativeQuery = true)
    int deletePost(@Param("id") int id);


}
