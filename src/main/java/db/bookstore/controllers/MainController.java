package db.bookstore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by vio on 22.04.2015.
 */
@Controller
@RequestMapping("/")
public class MainController {
    @RequestMapping(method = RequestMethod.GET)
    public String showMainPage(ModelMap model) {
        model.addAttribute("message", "Hello");
        return "index";
    }

}