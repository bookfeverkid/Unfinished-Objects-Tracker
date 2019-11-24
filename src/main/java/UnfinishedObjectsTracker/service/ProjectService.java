package UnfinishedObjectsTracker.service;

import UnfinishedObjectsTracker.models.Post;
import UnfinishedObjectsTracker.models.Project;
import UnfinishedObjectsTracker.repository.PostDao;
import UnfinishedObjectsTracker.repository.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service("projectService")
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private PostDao postDao;

    public Project findById(int id){return projectDao.findById(id);}

    ///public Post findById(int id){return postDao.findById(id);}

//    public void formatSingleCreationDate(int id){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
//        Project thisProject = projectDao.findById(id);
//        LocalDateTime date = thisProject.getCreationDate();
//        thisProject.setDate(date.format(formatter));
//
//    }
//
//    public void formatSinglePostDate(int id){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
//        Project thisProject = projectDao.findById(id);
//        LocalDateTime date = thisProject.getCreationDate();
//        thisProject.setDate(date.format(formatter));
//
//    }
}
