package db.bookstore.dao.impl;

import db.bookstore.dao.Author;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by StudentDB on 21.04.2015.
 */
public class DefaultAuthorTest {

    private DateTime birthDate;
    private DateTime deathDate;
    private Author author;

    @Before
    public void setUp() throws Exception {
        birthDate = new DateTime("2000-01-01");
        deathDate = new DateTime("2010-01-01");
        author = new DefaultAuthor(1, "name", birthDate, deathDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() throws Exception {
        author = new DefaultAuthor(1, "name", deathDate, birthDate);
    }

    @Test
    public void testGetId() throws Exception {
        assertEquals(1, author.getId());
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("name", author.getName());

    }

    @Test
    public void testGetBirthDate() throws Exception {
        assertEquals(birthDate, author.getBirthDate());

    }

    @Test
    public void testGetDeathDate() throws Exception {
        assertEquals(deathDate, author.getDeathDate());
    }
}