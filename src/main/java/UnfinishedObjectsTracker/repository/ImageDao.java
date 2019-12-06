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
    @Query(value = "INSERT into projectImages (image_id, project_id) VALUES ( :imageId, :projectId)", nativeQuery = true)
    @Modifying
    void createNewTitleImage(@Param("imageId") int imageId, @Param("projectId") int projectId);


    @Query(value="SELECT * " +
            "FROM image " +
            "INNER JOIN projectimages ON(image.id = projectimages.image_id) " +
            "INNER JOIN project ON(projectimages.project_id = project.project_id) " +
            "WHERE project.project_id =:projectId", nativeQuery = true)
    ArrayList<Image> listImagesByProjectId(@Param("projectId") int projectId);

}