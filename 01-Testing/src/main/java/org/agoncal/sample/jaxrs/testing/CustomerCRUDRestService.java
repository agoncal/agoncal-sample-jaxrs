package org.agoncal.sample.jaxrs.testing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/customer")
@Produces(MediaType.APPLICATION_XML)
public class CustomerCRUDRestService {

    // ======================================
    // =           Public Methods           =
    // ======================================

    /**
     * curl http://localhost:8080/chapter22-service-1.0/rs/customers/agoncal
     */
    @GET
    @Path("{login: [a-zA-Z]*}")
    public Customer getCustomerByLogin(@PathParam("login") String login) {
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
        customer.setLogin(login);
        return customer;
    }

    /**
     * curl http://localhost:8080/chapter22-service-1.0/rs/customers/agoncal
     */
    @GET
    @Path("{customerId : \\d+}")
    public Customer getCustomerById(@PathParam("customerId") Long id) {
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
        customer.setId(id);
        return customer;
    }

    @GET
    public Customer getCustomerByZipCode(@QueryParam("zip") Long zip) {
        System.out.println("getCustomerByZipCode : " + zip);
        return new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
    }

    @GET
//    @Path("search")
    public Customer getCustomerByFirstnameName(@MatrixParam("firstname") String firstname, @MatrixParam("surname") String surname) {
        System.out.println("getCustomerByFirstnameName : " + firstname + " - " + surname);
        return new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
    }

    @GET
    @Path("cookie")
    public String echoCookie(@CookieParam("myCookie") String myCookie) {
        System.out.println("echoCookie : " + myCookie);
        return myCookie + " from the server";
    }

    @GET
    @Path("userAgent")
    @Produces(MediaType.TEXT_PLAIN)
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