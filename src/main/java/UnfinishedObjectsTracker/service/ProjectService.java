package UnfinishedObjectsTracker.service;

import UnfinishedObjectsTracker.models.Project;
import UnfinishedObjectsTracker.repository.ProjectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service("projectService")
public class ProjectService {

    @Autowired
    private ProjectDao projectDao;

    public void formatSingleDate(int id){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        Project thisProject = projectDao.findById(id);
        LocalDateTime date = thisProject.getCreationDate();
         thisProject.setDate(date.format(formatter));

    }
}
