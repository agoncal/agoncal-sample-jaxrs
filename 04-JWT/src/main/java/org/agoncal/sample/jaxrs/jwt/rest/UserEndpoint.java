package org.agoncal.sample.jaxrs.jwt.rest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.agoncal.sample.jaxrs.jwt.domain.User;
import org.agoncal.sample.jaxrs.jwt.repository.UserRepository;
import org.agoncal.sample.jaxrs.jwt.util.KeyGenerator;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.security.Key;
import java.util.List;
import java.util.logging.Logger;

import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.MediaType.APPLICATION_FORM_URLENCODED;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/users")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
public class UserEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private UserRepository userRepository;

    @Context
    private UriInfo uriInfo;

    @Inject
    private Logger logger;

    @Inject
    private KeyGenerator keyGenerator;

    // ======================================
    // =          Business methods          =
    // ======================================

    @POST
    @Path("/login")
    @Consumes(APPLICATION_FORM_URLENCODED)
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        try {

            logger.info("#### login/password : " + username + "/" + password);

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return Response.ok().header(AUTHORIZATION, "Bearer " + token).build();

        } catch (Exception e) {
            return Response.status(UNAUTHORIZED).build();
        }
    }

    private void authenticate(String login, String password) throws Exception {
        User user = userRepository.findByLoginPassword(login, password);
        if (user == null)
            throw new SecurityException("Invalid user/password");
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
    }

    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder().setSubject(username).signWith(SignatureAlgorithm.HS512, key).compact();
        logger.info("#### generating token for a key : " + jwtToken + " - " + key);
        return jwtToken;

    }

    @POST
    public Response create(User user) {
        logger.info("#### create user : " + user);
        User created = userRepository.create(user);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(created.getId()).build()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") String id) {
        logger.info("#### find user by id : " + id);

        User user = userRepository.findById(id);

        if (user == null)
            return Response.status(NOT_FOUND).build();

        return Response.ok(user).build();
    }

    @GET
    @Path("/count")
    public Response countAllUsers() {
        logger.info("#### count all users");

        Long nbUsers = userRepository.countNumberOfUsers();

        return Response.ok(nbUsers).build();
    }

    @GET
    public Response allUsers() {
        logger.info("#### find all users");

        List<User> allUsers = userRepository.findAllUsers();

        if (allUsers == null)
            return Response.status(NOT_FOUND).build();

        return Response.ok(allUsers).build();
    }

    @DELETE
    @Path("/{id}")
    public Response remove(@PathParam("id") String id) {
        userRepository.delete(id);
        return Response.noContent().build();
    }
}
