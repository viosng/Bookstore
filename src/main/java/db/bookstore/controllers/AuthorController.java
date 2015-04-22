package db.bookstore.controllers;

import db.bookstore.dao.Author;
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
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private BookstoreService bookstoreService;

    @RequestMapping(method = RequestMethod.GET)
    public String showAuthors(ModelMap model) {
        model.addAttribute("authors", bookstoreService.getAllAuthors());
        return "authors";
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String showAuthor(@PathVariable int id, ModelMap model) {
        Author author = bookstoreService.getAuthor(id);
        if (author == null) {
            return "authorNotFound";
        }
        model.addAttribute("author", author);
        model.addAttribute("books", bookstoreService.getBooksOfAuthor(author));
        return "author";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateAuthor(HttpServletRequest rq, ModelMap model) {
        String deathDate = rq.getParameter("deathDate");
        int id = Integer.parseInt(rq.getParameter("id"));
        bookstoreService.updateAuthor(
                id,
                rq.getParameter("name"),
                new DateTime(rq.getParameter("birthDate")),
                deathDate.equals("") ? null : new DateTime(deathDate));
        model.put("authorIsUpdated", true);
        return showAuthor(id, model);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreatePage() {
        return "createAuthor";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createAuthor(HttpServletRequest rq, ModelMap model) {
        String deathDate = rq.getParameter("deathDate");
        bookstoreService.addAuthor(
                rq.getParameter("name"),
                new DateTime(rq.getParameter("birthDate")),
                deathDate.equals("") ? null : new DateTime(deathDate));
        model.put("authorIsAdded", true);
        return "createAuthor";
    }
}