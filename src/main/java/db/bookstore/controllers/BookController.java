package db.bookstore.controllers;

import db.bookstore.services.BookstoreService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vio on 22.04.2015.
 */
@Controller
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookstoreService bookstoreService;

    @RequestMapping(method = RequestMethod.GET)
    public String showAuthors(ModelMap model) {
        model.addAttribute("books", bookstoreService.getAllBooks());
        return "books";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showBook(@PathVariable int id, ModelMap model) {
        model.addAttribute("book", bookstoreService.getBook(id));
        return "book";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreatePage(ModelMap model) {
        model.put("authors", bookstoreService.getAllAuthors());
        return "createBook";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createAuthor(HttpServletRequest rq, ModelMap model) {
        String deathDate = rq.getParameter("deathDate");
        bookstoreService.addAuthor(
                rq.getParameter("name"),
                new DateTime(rq.getParameter("birthDate")),
                deathDate.equals("") ? null : new DateTime(deathDate));
        model.put("bookIsAdded", true);
        return showCreatePage(model);
    }
}