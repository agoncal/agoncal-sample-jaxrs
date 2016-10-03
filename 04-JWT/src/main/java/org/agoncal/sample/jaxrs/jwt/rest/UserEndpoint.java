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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
    public Response authenticateUser(@FormParam("login") String login,
                                     @FormParam("password") String password) {

        try {

            logger.info("#### login/password : " + login + "/" + password);

            // Authenticate the user using the credentials provided
            authenticate(login, password);

            // Issue a token for the user
            String token = issueToken(login);

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
    }

    private String issueToken(String login) {
        // The issued token must be associated to a user (login)
        Key key = keyGenerator.generateKey();
        String jwtToken = Jwts.builder()
                .setSubject(login)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(15L)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        logger.info("#### generating token for a key : " + jwtToken + " - " + key);
        return jwtToken;

    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
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
