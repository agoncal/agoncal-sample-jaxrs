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
public class CustomerRestService {

    // ======================================
    // =           Public Methods           =
    // ======================================

    @GET
    @Path("{login: [a-z]*}")
    public Response getCustomerByLogin(@PathParam("login") String login) {
        System.out.println("getCustomerByLogin : " + login);
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
        customer.setLogin(login);
        return Response.ok(customer).build();
    }

    @GET
    @Path("{customerId : \\d+}")
    public Response getCustomerById(@PathParam("customerId") Long id) {
        System.out.println("getCustomerById : " + id);
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565");
        customer.setId(id);
        return Response.ok(customer).build();
    }

    @GET
    public Response getCustomersByZipCode(@QueryParam("zip") Long zip) {
        System.out.println("getCustomerByZipCode : " + zip);
        Customers customers = new Customers();
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565"));
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565"));
        return Response.ok(customers).build();
    }

    @GET
    @Path("search")
    public Response getCustomerByName(@MatrixParam("firstname") String firstname, @MatrixParam("surname") String surname) {
        System.out.println("getCustomerByName : " + firstname + " - " + surname);
        Customers customers = new Customers();
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565"));
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565"));
        return Response.ok(customers).build();
    }
}