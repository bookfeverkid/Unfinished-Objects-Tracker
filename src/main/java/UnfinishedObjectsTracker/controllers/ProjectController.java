package UnfinishedObjectsTracker.controllers;

import UnfinishedObjectsTracker.models.Image;
import UnfinishedObjectsTracker.models.Post;
import UnfinishedObjectsTracker.models.Project;
import UnfinishedObjectsTracker.models.User;
import UnfinishedObjectsTracker.repository.ImageDao;
import UnfinishedObjectsTracker.repository.PostDao;
import UnfinishedObjectsTracker.repository.ProjectDao;
import UnfinishedObjectsTracker.repository.UserDao;
import UnfinishedObjectsTracker.service.ImageService;
import UnfinishedObjectsTracker.service.ProjectService;
import UnfinishedObjectsTracker.service.UserService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Autowired
    private PostDao postDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ImageDao imageDao;

    private Logger log = Logger.getLogger(ProjectController.class.getName());


    @RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();

        //project listing
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
        for (Project p: userProjects) {
        ArrayList<Image> userImages = imageDao.listImagesByProjectId(p.getId());
        // bundle in an if statement to make sure that there is something in this array, otherwise don't set it
        if (!userImages.isEmpty()) {
            Image i = userImages.get(0);
            String base64EncodedImage = Base64.encodeBase64String(i.getData());
            p.setImageString("data:image/jpeg;base64," + base64EncodedImage);
        }
        }

        modelAndView.addObject("title",  "UFO Home");
        modelAndView.addObject("WelcomeMessage", "Welcome " + user.getUsername() + "!");
        if (userProjects.size()== 0) {
            modelAndView.addObject("NoProjects", "No current projects.  " +
                    "Would you like to create a project?");
        }
        //format local date to thymeleaf readable date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        for(Project p: userProjects) {
            LocalDateTime date = p.getCreationDate();
            p.setDate(date.format(formatter));
        }
        //add objects to modelAndView
        modelAndView.addObject("WelcomeMessage",  user.getUsername() + "'s Projects");
        modelAndView.addObject("userProjects", userProjects);
        modelAndView.setViewName("project/home");
        return modelAndView;
    }

    @RequestMapping(value="/new", method= RequestMethod.GET)
    public ModelAndView displayCreateProject(){
        ModelAndView modelAndView = new ModelAndView();
        //create new project object & image object
        Project project = new Project();
        Image image = new Image();
        //add objects to modelAndView
        modelAndView.addObject("project", project);
        modelAndView.addObject("image", image);
        modelAndView.setViewName("project/new");
        return modelAndView;
    }

    @RequestMapping(value="/new", method = RequestMethod.POST)
    public ModelAndView submitNewProject(@Valid Project newProject, BindingResult bindingResult,
                                         @RequestParam(value ="file") MultipartFile file) {
        ModelAndView modelAndView = new ModelAndView();
        //this should retrieve the currently logged in user
        //user details contains everything but the Id for the user
        //so we have to do another query to grab it, this isn't efficient so probably
        //needs refactoring at some point
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User u = userService.findUserByEmail(userDetails.getUsername());
        int thisUser = u.getId();
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("title", "Create New Project");
            modelAndView.setViewName("project/new");
        } else{
            //this is just rough checking to ensure we have a user to save this project to
            if(thisUser != 0) {
                //save project
                Project thisProject = projectDao.save(newProject);
                System.out.println("this is the project id " + thisProject.getId());
                projectDao.createNewProject(thisUser,thisProject.getId());
                //save project image
                log.info("Some image: " + file );
                Image thisImage = imageService.storeFile((MultipartFile) file);
                imageDao.createNewTitleImage(thisImage.getId(), thisProject.getId());
//                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                        .path("/downloadFile/")
//                        .path(thisImage.getId())
//                        .toUriString();

                //project listing
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                User user = userService.findUserByEmail(auth.getName());
                ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
                for (Project p: userProjects) {
                    ArrayList<Image> userImages = imageDao.listImagesByProjectId(p.getId());
                    // bundle in an if statement to make sure that there is something in this array, otherwise don't set it
                    if (userImages.isEmpty()){

                    }
                    if (!userImages.isEmpty()) {
                        Image i = userImages.get(0);
                        String base64EncodedImage = Base64.encodeBase64String(i.getData());
                        p.setImageString("data:image/jpeg;base64," + base64EncodedImage);
                    }
                    // add a check to display default pic if no image uploaded
                }

                //add objects to modelAndView
                String username= userService.findUserByEmail(auth.getName()).getUsername();
                modelAndView.addObject("project", new Project());
                modelAndView.addObject("title",  "UFO Home");
                modelAndView.addObject("WelcomeMessage",  username + "'s Projects");
                modelAndView.addObject("userProjects", userProjects);
                modelAndView.setViewName("project/home");

            } else {
                System.out.println("No user found so going to new again");
                modelAndView.setViewName("project/new");
            }
        }
        return modelAndView;
    }
    @RequestMapping(value="/viewproject/{id}", method = RequestMethod.GET)
    public ModelAndView displayProject(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        Project thisProject = projectDao.findById(id);
        LocalDateTime date = thisProject.getCreationDate();
        thisProject.setDate(date.format(formatter));
        ArrayList<Image> thisImage = imageDao.listImagesByProjectId(thisProject.getId());
        log.info("view image" + thisImage);
        if (!thisImage.isEmpty()) {
            Image i = thisImage.get(0);
            String base64EncodedImage = Base64.encodeBase64String(i.getData());
            thisProject.setImageString("data:image/jpeg;base64," + base64EncodedImage);
        }
        ArrayList<Post> thesePosts = updatePostDate(thisProject);

        //add objects to modelAndView
        modelAndView.addObject("project", thisProject);
        modelAndView.addObject("post", thesePosts);
        modelAndView.setViewName("project/viewproject");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.GET)
    public ModelAndView displayEditProject(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("project", projectDao.findById(id));
        //add objects to modelAndView
        modelAndView.setViewName("project/edit");
        return modelAndView;
    }

    @RequestMapping(value="/edit/{id}", method = RequestMethod.POST)
    public ModelAndView processEditProject(@Valid Project project, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        //Confirm Delete

        //Edit project feature
        int i = projectDao.updateProject(project.getId(), project.getTitle(), project.getDescription());

        //project listing for homepage
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
        for (Project p: userProjects) {
            ArrayList<Image> userImages = imageDao.listImagesByProjectId(p.getId());
            // bundle in an if statement to make sure that there is something in this array, otherwise don't set it
            if (!userImages.isEmpty()) {
                Image img = userImages.get(0);
                String base64EncodedImage = Base64.encodeBase64String(img.getData());
                p.setImageString("data:image/jpeg;base64," + base64EncodedImage);
            }
        }
        String username= userService.findUserByEmail(auth.getName()).getUsername();
        //add objects to modelAndView
        modelAndView.addObject("title",  "UFO Home");
        modelAndView.addObject("WelcomeMessage",  username + "'s Projects");
        modelAndView.addObject("userProjects", userProjects);
        modelAndView.setViewName("project/home");

    return modelAndView;
    }

    @RequestMapping(value="/delete/{id}", method = RequestMethod.GET)
    public ModelAndView displayDeleteProject(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView();
        //add objects to modelAndView
        modelAndView.addObject("Message", "Are you sure you want to delete this project?");
        modelAndView.addObject("project", projectDao.findById(id));
        modelAndView.setViewName("project/delete");
        return modelAndView;
    }
    @RequestMapping(value="/delete", method = RequestMethod.POST)
    public ModelAndView processDeleteProject(Project project){
        ModelAndView modelAndView = new ModelAndView();

        //Delete Project
        int z =postDao.deletePost(project.getId());
        int i =projectDao.deleteProject(project.getId());

        //project listing
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
        for (Project p: userProjects) {
            ArrayList<Image> userImages = imageDao.listImagesByProjectId(p.getId());
            // bundle in an if statement to make sure that there is something in this array, otherwise don't set it
            if (!userImages.isEmpty()) {
                Image img = userImages.get(0);
                String base64EncodedImage = Base64.encodeBase64String(img.getData());
                p.setImageString("data:image/jpeg;base64," + base64EncodedImage);
            }
        };
        String username= userService.findUserByEmail(auth.getName()).getUsername();
        //add objects to modelAndView
        modelAndView.addObject("title",  "UFO Home");
        modelAndView.addObject("WelcomeMessage", username  + "'s Projects");
        modelAndView.addObject("userProjects", userProjects);
        modelAndView.setViewName("project/home");

        return modelAndView;
    }

    @RequestMapping(value="/addpost/{id}", method= RequestMethod.GET)
    public ModelAndView displayAddPost(@PathVariable("id") int id){
        log.info("launching add post page" + id);
        ModelAndView modelAndView = new ModelAndView();
        //create new project object
        Post post = new Post();
        post.setProjectId(id);
        //add objects to modelAndView
        Project project =projectDao.findById(post.getProjectId());
        modelAndView.addObject("post", post);
        modelAndView.addObject("project", project);
        modelAndView.addObject("title", "Add New Post to Project");
        modelAndView.setViewName("project/addpost");
        return modelAndView;
    }

    @RequestMapping(value="/addpost", method = RequestMethod.POST)
    public ModelAndView processAddPost(@Valid Post newPost, @Valid Project project){
        ModelAndView modelAndView = new ModelAndView();

        //save new post to database
        Post thisPost = postDao.save(newPost);
        projectDao.updateProjectProgress(project.getId(), project.getPercentComplete());

        //load Project page with new post

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        Project thisProject = projectDao.findById(thisPost.getProjectId());
        ////update project progress tracker??
        ///How can I do this here?
        LocalDateTime date = thisProject.getCreationDate();
        thisProject.setDate(date.format(formatter));
        ArrayList<Post> thesePosts = updatePostDate(thisProject);

        //add objects to modelAndView
        modelAndView.addObject("project", thisProject);
        modelAndView.addObject("post", thesePosts);
        modelAndView.addObject("title", "Would you like to Edit your project's progress?");
        modelAndView.setViewName("project/viewproject");
        return modelAndView;
    }

    @RequestMapping(value="/editpost/{id}", method = RequestMethod.GET)
    public ModelAndView displayEditPost(@PathVariable("id") int id){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("post", postDao.findById(id));
        //add objects to modelAndView
        modelAndView.setViewName("project/editpost");
        return modelAndView;
    }

    @RequestMapping(value="/editpost/{id}", method = RequestMethod.POST)
    public ModelAndView processEditPost(@Valid Post post, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        //Edit individual post feature
        int i = postDao.updatePost(post.getId(), post.getTitle(), post.getDescription());
        //log.info(post.getProjectId());
        //view project  listing
        log.info("something" + i);
        log.info("p" +post.getProjectId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        Project thisProject = projectDao.findById(post.getProjectId());
        log.info(thisProject.getTitle());
        ArrayList<Post> somePosts = updatePostDate(thisProject);

        //add objects to modelAndView
        modelAndView.addObject("project", thisProject);
        modelAndView.addObject("post", somePosts);
        modelAndView.setViewName("project/viewproject");
        return modelAndView;
    }

    public ArrayList<Project> updateDate(Authentication auth) {
        User user = userService.findUserByEmail(auth.getName());
        ArrayList<Project> userProjects = projectDao.listProjectsByUser(user.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        for(Project p: userProjects) {
            LocalDateTime date = p.getCreationDate();
            p.setDate(date.format(formatter));
        }
        return userProjects;
    }

    public ArrayList<Post> updatePostDate(Project thisProject) {
        Project project = projectService.findById(thisProject.getId());
        ArrayList<Post> post = postDao.listPostsByProjectId(project.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        for(Post p: post) {
            LocalDateTime date = p.getPostDate();
            p.setpDate(date.format(formatter));
        }
        return post;
    }

}
