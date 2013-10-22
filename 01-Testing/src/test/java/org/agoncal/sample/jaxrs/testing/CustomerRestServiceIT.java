package org.agoncal.sample.jaxrs.testing;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.RuntimeDelegate;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class CustomerRestServiceIT {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final String XML = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><customer><email>jsmith@gmail.com</email><firstName>John</firstName><lastName>Smith</lastName><phoneNumber>1234565</phoneNumber></customer>";

    // ======================================
    // =                 Tests              =
    // ======================================

    @Test
    public void shouldMarshallACustomer() throws JAXBException {
        // given
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Customer.class);
        Marshaller m = context.createMarshaller();
        m.marshal(customer, writer);

        // then
        assertEquals(XML, writer.toString());
    }

    @Test
    public void shouldMarshallAListOfCustomers() throws JAXBException {
        Customers books = new Customers();
        books.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565"));
        books.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565"));
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(Customers.class);
        Marshaller m = context.createMarshaller();
        m.marshal(books, writer);
    }

    @Test //@Ignore
    public void shouldCheckURIs() throws IOException {

        URI uri = UriBuilder.fromUri("http://localhost/").port(8282).build();

        // Create an HTTP server listening at port 8282
        HttpServer server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);
        // Create a handler wrapping the JAX-RS application
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new ApplicationConfig(), HttpHandler.class);
        // Map JAX-RS handler to the server root
        server.createContext(uri.getPath(), handler);
        // Start the server
        server.start();

        Client client = ClientBuilder.newClient();

        // Valid URIs
        assertEquals(200, client.target("http://localhost:8282/customer/agoncal").request().get().getStatus());
        assertEquals(200, client.target("http://localhost:8282/customer/1234").request().get().getStatus());
        assertEquals(200, client.target("http://localhost:8282/customer?zip=75012").request().get().getStatus());
        assertEquals(200, client.target("http://localhost:8282/customer/search;firstname=Antonio;surname=Goncalves").request().get().getStatus());

        // Invalid URIs
        assertEquals(404, client.target("http://localhost:8282/customer/AGONCAL").request().get().getStatus());
        assertEquals(404, client.target("http://localhost:8282/customer/dummy/1234").request().get().getStatus());

        // Stop HTTP server
        server.stop(0);
    }

    @Test //@Ignore
    public void shouldGetCustomerByLogin() throws IOException {

        URI uri = UriBuilder.fromUri("http://localhost/").port(8282).build();

        // Create an HTTP server listening at port 8282
        HttpServer server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);
        // Create a handler wrapping the JAX-RS application
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new ApplicationConfig(), HttpHandler.class);
        // Map JAX-RS handler to the server root
        server.createContext(uri.getPath(), handler);
        // Start the server
        server.start();

        Client client = ClientBuilder.newClient();

        // Valid URIs
        Response response = client.target("http://localhost:8282/customer/agoncal").request().get();
        assertEquals(200, response.getStatus());

        System.out.println("###############################");
        System.out.println(response.readEntity(String.class));

        // Stop HTTP server
        server.stop(0);
    }

    @Test //@Ignore
    public void shouldGetCustomers() throws IOException {

        URI uri = UriBuilder.fromUri("http://localhost/").port(8282).build();

        // Create an HTTP server listening at port 8282
        HttpServer server = HttpServer.create(new InetSocketAddress(uri.getPort()), 0);
        // Create a handler wrapping the JAX-RS application
        HttpHandler handler = RuntimeDelegate.getInstance().createEndpoint(new ApplicationConfig(), HttpHandler.class);
        // Map JAX-RS handler to the server root
        server.createContext(uri.getPath(), handler);
        // Start the server
        server.start();

        Client client = ClientBuilder.newClient();

        // Valid URIs
        Response response = client.target("http://localhost:8282/customer").request().get();
        assertEquals(200, response.getStatus());

        System.out.println("###############################");
        System.out.println(response.readEntity(String.class));

        // Stop HTTP server
        server.stop(0);
    }
}
