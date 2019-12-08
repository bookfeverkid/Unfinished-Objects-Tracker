package UnfinishedObjectsTracker.repository;


import UnfinishedObjectsTracker.models.Image;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

@Repository
public interface ImageDao extends JpaRepository<Image, String> {

//    Image findById(String id);

//    Image findByProjectId(int id);

    @Transactional
    @Query(value = "INSERT into project_images (image_id, project_id) VALUES ( :imageId, :projectId)", nativeQuery = true)
    @Modifying
    void createNewTitleImage(@Param("imageId") int imageId, @Param("projectId") int projectId);


    @Transactional
    @Query(value = "INSERT into post_images (image_id, post_id) VALUES ( :imageId, :postId)", nativeQuery = true)
    @Modifying
    void createNewPostImage(@Param("imageId") int imageId, @Param("postId") int postId);

    @Query(value="SELECT * " +
            "FROM image " +
            "INNER JOIN project_images ON(image.id = project_images.image_id) " +
            "INNER JOIN project ON(project_images.project_id = project.project_id) " +
            "WHERE project.project_id =:projectId", nativeQuery = true)
    ArrayList<Image> listImagesByProjectId(@Param("projectId") int projectId);

    @Query(value="SELECT * " +
            "FROM image " +
            "INNER JOIN post_images ON(image.id = post_images.image_id) " +
            "INNER JOIN post ON(post_images.post_id = post.post_id) " +
            "WHERE post.post_id =:postId", nativeQuery = true)
    ArrayList<Image> listImagesByPostId(@Param("postId") int postId);

    @Transactional
    @Modifying
    @Query(value ="DELETE image, project_images, post_images " +
            "FROM image " +
            "INNER JOIN  project_images ON(image.id = project_images.image_id) " +
            "INNER JOIN  post_images ON(image.id = post_images.image_id) " +
            "WHERE project_images.project_id = :id", nativeQuery = true)
    int deleteProjectImage(@Param("id") int id);


    @Transactional
    @Modifying
    @Query(value ="DELETE image, project_images FROM image "+
            "INNER JOIN project_images ON (project_images.image_id = image.id) " +
            "WHERE project_images.project_id = :id", nativeQuery = true)
    int deleteProjectTitleImage(@Param("id") int id);


    @Transactional
    @Modifying
    @Query(value ="DELETE image, post_images FROM image " +
            "INNER JOIN post_images ON (post_images.image_id = image.id) " +
            "INNER JOIN post ON (post_images.post_id = post.post_id) " +
            "WHERE post.project_id = :id", nativeQuery = true)
    int deletePostImage(@Param("id") int id);
}