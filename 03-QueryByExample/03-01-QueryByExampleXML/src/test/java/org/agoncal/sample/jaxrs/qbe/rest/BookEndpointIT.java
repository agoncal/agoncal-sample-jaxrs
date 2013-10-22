package org.agoncal.sample.jaxrs.qbe.rest;

import org.agoncal.sample.jaxrs.qbe.model.Book;
import org.agoncal.sample.jaxrs.qbe.model.Books;
import org.agoncal.sample.jaxrs.qbe.model.Language;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class BookEndpointIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final String XML_BOOK = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><book><description>The best Scifi book</description><isbn>1234-5678</isbn><language>ENGLISH</language><nbOfPages>345</nbOfPages><price>45.5</price><publisher>Penguin</publisher><title>H2G2</title><version>0</version></book>";
    private static final String XML_BOOKS = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><books><book><description>The best Scifi book</description><isbn>1234-5678</isbn><language>ENGLISH</language><nbOfPages>345</nbOfPages><price>45.5</price><publisher>Penguin</publisher><title>H2G2</title><version>0</version></book><book><description>The best Scifi book</description><isbn>1234-5678</isbn><language>ENGLISH</language><nbOfPages>345</nbOfPages><price>45.5</price><publisher>Penguin</publisher><title>H2G2</title><version>0</version></book></books>";
    private static final String URL = "http://localhost:8080/sampleJaxRsQBE/rest";

    private Client client = ClientBuilder.newClient();

    // ======================================
    // =                 Tests              =
    // ======================================

    @Test
    public void shouldMarshallABook() throws JAXBException {

        // given
        Book book = new Book("1234-5678", "H2G2", "The best Scifi book", 45.5f, 345, "Penguin", Language.ENGLISH);
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Book.class);
        Marshaller m = context.createMarshaller();

        // when
        m.marshal(book, writer);

        // then
        assertEquals(XML_BOOK, writer.toString());
    }

    @Test
    public void shouldMarshallAListOfBooks() throws JAXBException {

        // given
        Books books = new Books();
        books.add(new Book("1234-5678", "H2G2", "The best Scifi book", 45.5f, 345, "Penguin", Language.ENGLISH));
        books.add(new Book("1234-5678", "H2G2", "The best Scifi book", 45.5f, 345, "Penguin", Language.ENGLISH));
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Books.class);
        Marshaller m = context.createMarshaller();

        // when
        m.marshal(books, writer);

        // then
        assertEquals(XML_BOOKS, writer.toString());
    }

    @Test
    public void shouldCheckURIs() throws IOException {

        // Valid URIs
        assertEquals(200, client.target(URL + "/books").request().get().getStatus());
        assertEquals(200, client.target(URL + "/books").request(MediaType.APPLICATION_XML).get().getStatus());
        assertEquals(200, client.target(URL + "/books/1000").request().get().getStatus());
        assertEquals(200, client.target(URL + "/books/1000").request(MediaType.APPLICATION_XML).get().getStatus());

        // Invalid MIME TYPE
        assertEquals(406, client.target(URL + "/books").request(MediaType.APPLICATION_OCTET_STREAM).get().getStatus());

        // Invalid URIs
        assertEquals(404, client.target(URL + "/books/DUMMY").request().get().getStatus());
        assertEquals(404, client.target(URL + "/dummy").request().get().getStatus());
    }

    @Test
    public void shouldGetBookById() throws IOException {

        // given
        Response response = client.target(URL + "/books/1000").request(MediaType.APPLICATION_XML).get();
        assertEquals(200, response.getStatus());

        // when
        Book book = response.readEntity(Book.class);

        // then
        assertEquals("ISBN", "143024626X", book.getIsbn());
        assertEquals("Title", "Beginning Java EE 7", book.getTitle());
    }

    @Test
    public void shouldGetAllBooks() throws IOException {

        // given
        Response response = client.target(URL + "/books").request(MediaType.APPLICATION_XML).get();

        // when
        Books books = response.readEntity(Books.class);

        // then
        assertEquals(200, response.getStatus());
        assertEquals(5, books.size());
    }

    @Test
    public void shouldCRUDBooks() throws IOException {

        int initialSize = client.target(URL + "/books").request(MediaType.APPLICATION_XML).get(Books.class).size();

        // creates a book
        Book book = new Book("1234-5678", "H2G2", "The best Scifi book", 45.5f, 345, "Penguin", Language.ENGLISH);
        Response response = client.target(URL + "/books").request().post(Entity.entity(book, MediaType.APPLICATION_XML));
        assertEquals(201, response.getStatus());
        URI locationNewBook = response.getLocation();

        // checks there is one more book
        assertEquals(initialSize + 1, client.target(URL + "/books").request(MediaType.APPLICATION_XML).get(Books.class).size());

        // deletes the created book
        response = client.target(URL + locationNewBook).request().delete();
        assertEquals(204, response.getStatus());

        // checks there is one less book
        assertEquals(initialSize, client.target(URL + "/books").request(MediaType.APPLICATION_XML).get(Books.class).size());
    }

    @Test
    public void shouldGetBooksByQBEWithXML() throws IOException {

        Book example = new Book();
        example.setTitle("Beginning");

        Response response = client.target(URL + "/books").path("/query")
                .queryParam("example", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><book><title>Java</title></book>")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(5, response.readEntity(Books.class).size());

        response = client.target(URL + "/books").path("/query")
                .queryParam("example", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><book><title>Beginning</title></book>")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());

        response = client.target(URL + "/books").path("/query")
                .queryParam("example", "<book><title>Beginning</title></book>")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());

        response = client.target(URL + "/books").path("/query")
                .queryParam("example", "<book><title>Beginning</title><description>leading</description></book>")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());

        response = client.target(URL + "/books").path("/query")
                .queryParam("example", "<book><title>Beginning</title><description>leading</description><publisher>APress</publisher></book>")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());

        response = client.target(URL + "/books").path("/query")
                .queryParam("example", "<book><titl not well formatted xml</dummy></book>")
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(404, response.getStatus());
    }


    @Test
    public void shouldGetBooksByQBEWithObject() throws IOException, JAXBException {

        Book example = new Book();

        example.setTitle("Java");
        Response response = client.target(URL + "/books").path("/query")
                .queryParam("example", example.toXML())
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(5, response.readEntity(Books.class).size());

        example.setTitle("Beginning");
        response = client.target(URL + "/books").path("/query")
                .queryParam("example", example.toXML())
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());

        example.setTitle("Beginning");
        example.setDescription("leading");
        response = client.target(URL + "/books").path("/query")
                .queryParam("example", example.toXML())
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());

        example.setTitle("Beginning");
        example.setDescription("leading");
        example.setPublisher("APress");
        response = client.target(URL + "/books").path("/query")
                .queryParam("example", example.toXML())
                .request(MediaType.APPLICATION_XML)
                .get();

        assertEquals(200, response.getStatus());
        assertEquals(1, response.readEntity(Books.class).size());
    }
}
