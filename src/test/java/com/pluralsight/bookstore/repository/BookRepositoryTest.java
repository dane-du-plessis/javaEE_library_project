package com.pluralsight.bookstore.repository;

import com.pluralsight.bookstore.model.Book;
import com.pluralsight.bookstore.model.Language;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class BookRepositoryTest {
    private static Long bookId;

    @Inject
    private BookRepository bookRepository;

    @Deployment
    public static JavaArchive createDeployment() {
        return ShrinkWrap.create(JavaArchive.class)
                .addClass(BookRepository.class)
                .addClass(Book.class)
                .addClass(Language.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("META-INF/test-persistence.xml", "persistence.xml");
    }

    @Test
    @InSequence(1)
    public void shouldBeDeployed() {
        assertNotNull(bookRepository);
    }

    @Test
    @InSequence(2)
    public void shouldGetNoBook() {
        assertEquals(Long.valueOf(0), bookRepository.countAll());
    }

    @Test
    @InSequence(3)
    public void shouldCreateABook() {
        //Create a book
        Book book = new Book("Really cool book", "A really cool book, this is",
                123f, "1242423534534254365k", new Date(), 123, "http://someimage", Language.ENGLISH);
        bookRepository.create(book);
        bookId = book.getId();
        assertNotNull(bookId);
    }

    @Test
    @InSequence(4)
    public void shouldFindABook() {
        Book bookFound = bookRepository.find(bookId);
        assertNotNull(bookFound.getId());
        assertEquals(bookFound.getTitle(), "Really cool book");
    }

    @Test
    @InSequence(5)
    public void shouldCountOneBookOnly() {
        assertEquals(Long.valueOf(1), bookRepository.countAll());
        assertEquals(1, bookRepository.findAll().size());
    }

    @Test
    @InSequence(6)
    public void shouldDeleteTheOneBook() {
        bookRepository.delete(bookId);
        Book bookDeleted = bookRepository.find(bookId);
        assertNull(bookDeleted);
    }

    @Test
    @InSequence(7)
    public void findNoBooks() {
        assertEquals(Long.valueOf(0), bookRepository.countAll());
        assertEquals(0, bookRepository.findAll().size());
    }

//    @Test
//    @InSequence(8)
//    public void mustFail() {
//        assertEquals(1, 0);
//    }

    @Test(expected = Exception.class)
    @InSequence(8)
    public void shouldFailCreatingNullBook() {
        bookRepository.create(null);
    }

    @Test(expected = Exception.class)
    @InSequence(9)
    public void shouldFailCreatingNullTitle() {
        Book book = new Book(
                null,
                "A really cool book, this is",
                1f,
                "123654789",
                new Date(),
                123,
                "http://someimage2",
                Language.ENGLISH
        );
        bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    @InSequence(10)
    public void shouldFailWithZeroCost() {
        Book book = new Book(
                "Le Beeg Book",
                "A really cool book, this is",
                0f,
                "123654789",
                new Date(),
                123,
                "http://someimage2",
                Language.ENGLISH
        );
        bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    @InSequence(11)
    public void shouldFailWithNulllIsbn() {
        Book book = new Book(
                "Le Beeg Book",
                "A really cool book, this is",
                100f,
                null,
                new Date(),
                123,
                "http://someimage2",
                Language.ENGLISH
        );
        bookRepository.create(book);
    }

    @Test(expected = Exception.class)
    @InSequence(12)
    public void shouldFailToFindByNullId() {
        bookRepository.find(null);
    }

    @Test
    @InSequence(13)
    public void shouldNotFindByUnknownId() {
        assertNull(bookRepository.find(999999l));
    }

    @Test(expected = Exception.class)
    @InSequence(14)
    public void shouldFailDeleteByNull() {
        bookRepository.delete(null);
    }

    @Test(expected = Exception.class)
    @InSequence(15)
    public void shouldFailToDeleteUnknownId() {
        bookRepository.delete(9999999l);
    }

}
