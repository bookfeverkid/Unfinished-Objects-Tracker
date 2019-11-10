package UnfinishedObjectsTracker.controllers;

import UnfinishedObjectsTracker.models.User;
import UnfinishedObjectsTracker.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.logging.Logger;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    private Logger log = Logger.getLogger(UserController.class.getName());

    @RequestMapping(value={"/", "/login"}, method = RequestMethod.GET)
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user/login");
        return modelAndView;
    }

    //    @RequestMapping(value = {"/submit"}, method = RequestMethod.POST)
//    public ModelAndView userHome(User user) {
//        System.out.println("NEw User Logged in");
//        ModelAndView modelAndView = new ModelAndView();
//        User userExists = userService.findUserByEmail(user.getEmail());
//        System.out.println("NEw User Logged in");
//        modelAndView.setViewName("ufo/home");
//        return modelAndView;
//    }


    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView viewRegistrationForm(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("user/registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User usernameExists = userService.findUserByUsername(user.getUsername());
        User emailExists =userService.findUserByEmail(user.getEmail());
        if (usernameExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the username provided");
        }
        if (emailExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("user/login");

        }
        return modelAndView;
    }

}
