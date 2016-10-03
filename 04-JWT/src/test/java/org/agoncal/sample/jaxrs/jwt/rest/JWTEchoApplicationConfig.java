package org.agoncal.sample.jaxrs.jwt.rest;

import org.agoncal.sample.jaxrs.jwt.filter.JWTTokenNeededFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationPath("api")
public class JWTEchoApplicationConfig extends Application {

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet();
        classes.add(JWTTokenNeededFilter.class);
        classes.add(EchoEndpoint.class);
        classes.add(UserEndpoint.class);
        return classes;
    }
}