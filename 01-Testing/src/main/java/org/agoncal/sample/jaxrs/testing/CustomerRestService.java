package org.agoncal.sample.jaxrs.testing;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @Path("{login: [a-zA-Z]*}")
    public Customer getCustomerByLogin(@PathParam("login") String login) {
        System.out.println("getCustomerByLogin : " + login);
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date());
        customer.setLogin(login);
        return customer;
    }

    @GET
    @Path("{customerId : \\d+}")
    public Customer getCustomerById(@PathParam("customerId") Long id) {
        System.out.println("getCustomerById : " + id);
        Customer customer = new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date());
        customer.setId(id);
        return customer;
    }

    @GET
    public List<Customer> getCustomersByZipCode(@QueryParam("zip") Long zip) {
        System.out.println("getCustomerByZipCode : " + zip);
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date()));
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date()));
        return customers;
    }

    @GET
    @Path("search")
    public List<Customer> getCustomerByName(@MatrixParam("firstname") String firstname, @MatrixParam("surname") String surname) {
        System.out.println("getCustomerByName : " + firstname + " - " + surname);
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date()));
        customers.add(new Customer("John", "Smith", "jsmith@gmail.com", "1234565", new Date()));
        return customers;
    }
}