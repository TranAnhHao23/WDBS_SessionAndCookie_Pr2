package cg.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cg.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes("user")
public class LoginController {

    @ModelAttribute("user")
    public User setupUserForm() {
        return new User();
    }

    @RequestMapping("/login")
    public ModelAndView Index(@CookieValue(value = "setUser", defaultValue = "") String setUser) {
        ModelAndView modelAndView = new ModelAndView("login");
        Cookie cookie = new Cookie("setUser", setUser);
        modelAndView.addObject("cookieValue", cookie);
        return modelAndView;
    }

    @PostMapping("/dologin")
    public ModelAndView doLogin(@ModelAttribute("user") User user, @CookieValue(value = "setUser", defaultValue = "") String setUser, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (user.getEmail().equals("admin@gmail.com") && user.getPassword().equals("12345")) {
            if (user.getEmail() != null) {
                setUser = user.getEmail();
            }

            // create cookie and set it in response
            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(20);
            response.addCookie(cookie);

            //get all cookie
            Cookie[] cookies = request.getCookies();
            // iterate each cookie
            for (Cookie ck: cookies) {
                if (ck.getName().equals("setUser")){
                    modelAndView.addObject("cookieValue", ck);
                    break;
                } else {
                    ck.setValue("");
                    modelAndView.addObject("cookieValue", ck);
                    break;
                }
            }
            modelAndView.addObject("message", "Login success. Welcome ");
        } else {
            user.setEmail("");
            Cookie cookie = new Cookie("setUser", setUser);
            modelAndView.addObject("cookieValue", cookie);
            modelAndView.addObject("message", "Login failed. Try again.");
        }
        return modelAndView;
    }
}
