package org.agoncal.sample.jaxrs.testing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;

/**
 * @author Antonio Goncalves
 *         APress Book - Beginning Java EE 7 with Glassfish 4
 *         http://www.apress.com/
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/customer")
@Produces(MediaType.TEXT_PLAIN)
public class CustomerRestService {

    // ======================================
    // =           Public Methods           =
    // ======================================

    /**
     * curl http://localhost:8080/chapter22-service-1.0/rs/customers/agoncal
     */
    @GET
    @Path("{login: [a-zA-Z]*}")
    @Produces(MediaType.APPLICATION_XML)
    public Customer getCustomerByLogin(@PathParam("login") String login) {
        System.out.println("getCustomerByLogin : " + login);
        return new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date(), new Date());
    }

    /**
     * curl http://localhost:8080/chapter22-service-1.0/rs/customers/agoncal
     */
    @GET
    @Path("{customerId : \\d+}")
    @Produces(MediaType.APPLICATION_XML)
    public Customer getCustomerById(@PathParam("customerId") Long id) {
        System.out.println("getCustomerById : " + id);
        return new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date(), new Date());
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Customer getCustomerByZipCode(@QueryParam("zip") Long zip) {
        System.out.println("getCustomerByZipCode : " + zip);
        return new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date(), new Date());
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_XML)
    public Customer getCustomerByFirstnameName(@MatrixParam("firstname") String firstname, @MatrixParam("surname") String surname) {
        System.out.println("getCustomerByFirstnameName : " + firstname + " - " + surname);
        return new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date(), new Date());
    }

    @GET
    @Path("cookie")
    @Produces(MediaType.APPLICATION_XML)
    public String echoCookie(@CookieParam("myCookie") String myCookie) {
        System.out.println("echoCookie : " + myCookie);
        return myCookie + " from the server";
    }

    @GET
    @Path("userAgent")
    public String echoUserAgent(@HeaderParam(value = "User-Agent") String userAgent) {
        System.out.println("echoUserAgent : " + userAgent);
        return userAgent + " from the server";
    }

    @GET
    @Path("userAgentRep")
    @Produces(MediaType.TEXT_PLAIN)
    public Response echoUserAgentWithReponse(@HeaderParam(value = "User-Agent") String userAgent) {
        System.out.println("echoUserAgentWithReponse : " + userAgent);
        return Response.ok(userAgent + " from the server", MediaType.TEXT_PLAIN).build();
    }
}