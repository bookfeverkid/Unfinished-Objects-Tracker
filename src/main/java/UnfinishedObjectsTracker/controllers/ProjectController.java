package UnfinishedObjectsTracker.controllers;

import UnfinishedObjectsTracker.models.Project;
import UnfinishedObjectsTracker.models.User;
import UnfinishedObjectsTracker.repository.ProjectDao;
import UnfinishedObjectsTracker.repository.UserDao;
import UnfinishedObjectsTracker.service.ProjectService;
import UnfinishedObjectsTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.logging.Logger;

@Controller
public class ProjectController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserDao userDao;

    private Logger log = Logger.getLogger(ProjectController.class.getName());

    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
        for (Project p: userProjects) {
            log.info(p.getTitle());
        }
        //projectDao.listProjectsByUser()
        //System.out.println(user);
        modelAndView.addObject("title",  "UFO Home");
        modelAndView.addObject("WelcomeMessage", "Welcome " + user.getUsername() + "!");
        if (userProjects.size()== 0) {
            modelAndView.addObject("NoProjects", "No current projects.  " +
                    "Would you like to create a project?");
        }
        modelAndView.addObject("userProjects", projectDao.listProjectsByUser(user.getId()));
       // modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("project/home");
        return modelAndView;
    }

    @RequestMapping(value="/new", method= RequestMethod.GET)
    public ModelAndView displayCreateProject(){
        ModelAndView modelAndView = new ModelAndView();
        Project project = new Project();

        modelAndView.addObject("project", project);
        modelAndView.addObject("title", "UFO");
        modelAndView.setViewName("project/new");
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.POST)
    public ModelAndView submitNewProject(@Valid Project newProject, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();

        //this should retrieve the currently logged in user
        //user details contains everything but the Id for the user
        //so we have to do another query to grab it, this isn't efficient so probably
        //needs refactoring at some point
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User u = userService.findUserByEmail(userDetails.getUsername());
        log.info("should return the user ID:" +u.getId());
        int thisUser = u.getId();
//        System.out.println("user name " + userDetails.getUsername());
//        System.out.println("user ID" + thisUser);

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("project/new");
        } else{
            //this is just rough checking to ensure we have a user to save this project to
            if(thisUser != 0) {
                Project thisProject = projectDao.save(newProject);
                System.out.println("this is the project id " + thisProject.getId());
                projectDao.saveLink(thisUser,thisProject.getId());
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = userService.findUserByEmail(auth.getName());
                ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
                modelAndView.addObject("project", new Project());
                modelAndView.addObject("WelcomeMessage",  user.getUsername() + "'s Projects");
                modelAndView.addObject("userProjects", projectDao.listProjectsByUser(user.getId()));
                modelAndView.setViewName("project/home");
            } else {
                System.out.println("No user found so going to new again");
                modelAndView.setViewName("project/new");
            }

        }


        return modelAndView;
    }
}
