package org.agoncal.sample.jaxrs.jwt.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.agoncal.sample.jaxrs.jwt.domain.Attendee;
import org.agoncal.sample.jaxrs.jwt.repository.AttendeeRepository;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.security.Key;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/attendee")
public class AttendeeEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private AttendeeRepository attendeeRepository;

    @Context
    private UriInfo uriInfo;

    @Inject
    private Logger logger;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @Path("/login")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        try {

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok(token).build();

        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    }

    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        Key key = MacProvider.generateKey();
        String jwtToken = Jwts.builder().setSubject("Joe").signWith(SignatureAlgorithm.HS512, key).compact();
        return jwtToken;

    }

    @POST
    public Response create(Attendee attendee) {
        Attendee created = attendeeRepository.create(attendee);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId()).build()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id, @Context Request request) {

        Attendee attendee = attendeeRepository.findById(id);

        if (attendee == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(attendee).build();
    }

    @GET
    public Response allAttendees() {
        List<Attendee> allAttendees = attendeeRepository.findAllAttendees();

        if (allAttendees == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(allAttendees).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        attendeeRepository.delete(id);
        return Response.noContent().build();
    }
}
