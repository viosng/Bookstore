package db.bookstore.controllers;

import db.bookstore.dao.Author;
import db.bookstore.dao.Book;
import db.bookstore.services.BookstoreService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        Book book = bookstoreService.getBook(id);
        if (book == null) {
            return "bookNotFound";
        }
        model.addAttribute("allAuthors", bookstoreService.getAllAuthors());
        model.addAttribute("book", book);
        return "book";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String updateBook(HttpServletRequest rq, ModelMap model) {
        int id = Integer.parseInt(rq.getParameter("id"));
        bookstoreService.updateBook(
                id,
                rq.getParameter("name"),
                Double.parseDouble(rq.getParameter("price")),
                new DateTime(rq.getParameter("publicationDate")));
        String[] parameterValues = rq.getParameterValues("authors[]");
        if (parameterValues != null && parameterValues.length > 0) {
            List<Author> authors =  Arrays.stream(parameterValues)
                    .map(aid -> bookstoreService.getAuthor(Integer.parseInt(aid)))
                    .collect(Collectors.toList());
            Book book = bookstoreService.getBook(id);
            if (book == null) {
                return "bookNotFound";
            }
            for (Author author : authors) {
                bookstoreService.addAuthority(author, book);
            }
        }
        model.put("bookIsUpdated", true);
        return showBook(id, model);
    }

    @RequestMapping(value = "/removeAuthority", method = RequestMethod.POST)
    public String removeAuthority(HttpServletRequest rq, ModelMap model) {
        Book book = bookstoreService.getBook(Integer.parseInt(rq.getParameter("book_id")));
        if (book == null) {
            return "bookNotFound";
        }
        Author author = bookstoreService.getAuthor(Integer.parseInt(rq.getParameter("author_id")));
        if (author == null) {
            return "bookNotFound";
        }
        bookstoreService.deleteAuthority(author, book);
        model.put("bookIsUpdated", true);
        return showBook(book.getId(), model);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String showCreatePage(ModelMap model) {
        model.put("authors", bookstoreService.getAllAuthors());
        return "createBook";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String createBook(HttpServletRequest rq, ModelMap model) {
        bookstoreService.addBook(
                rq.getParameter("name"),
                Double.parseDouble(rq.getParameter("price")),
                new DateTime(rq.getParameter("publicationDate")),
                Arrays.stream(rq.getParameterValues("authors[]"))
                        .map(id -> bookstoreService.getAuthor(Integer.parseInt(id)))
                        .collect(Collectors.toList()));
        model.put("bookIsAdded", true);
        return showCreatePage(model);
    }
}