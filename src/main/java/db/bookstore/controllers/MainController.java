package db.bookstore.controllers;

import db.bookstore.services.SerializationService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SerializationService serializationService;

    @RequestMapping(method = RequestMethod.GET)
    public String showMainPage(ModelMap model) {
        model.addAttribute("message", "Bookstore main page");
        return "index";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String showLogout(ModelMap model) {
        return "logout";
    }

    @RequestMapping(value = "/importJson", method = RequestMethod.POST)
    public String importDatabase(ModelMap model) {
        serializationService.importFromJson();
        model.addAttribute("databaseIsImported", true);
        return showMainPage(model);
    }
}