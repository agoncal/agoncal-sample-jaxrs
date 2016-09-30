package org.agoncal.sample.jaxrs.jwt.rest;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Path("/securedecho")
public class SecuredEchoEndpoint {

    // ======================================
    // =          Injection Points          =
    // ======================================

    @Inject
    private Logger logger;

    // ======================================
    // =          Business methods          =
    // ======================================

    @GET
    @Secured
    @Produces(TEXT_PLAIN)
    public Response echo(@QueryParam("message") String message) {
        logger.info("#### Received message: " + message);
        return Response.ok().entity(message == null ? "no message" : message).build();
    }
}
