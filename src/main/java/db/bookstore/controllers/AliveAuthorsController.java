package db.bookstore.controllers;

import db.bookstore.services.BookstoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by vio on 22.04.2015.
 */
@Controller
@RequestMapping("/alive")
public class AliveAuthorsController {

    @Autowired
    private BookstoreService bookstoreService;

    @RequestMapping(method = RequestMethod.GET)
    public String show(ModelMap model) {
        model.addAttribute("books", bookstoreService.getBooksOfAliveAuthors());
        return "books";
    }
}