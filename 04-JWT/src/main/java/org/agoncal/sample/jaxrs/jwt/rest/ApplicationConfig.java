package org.agoncal.sample.jaxrs.jwt.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationPath("api")
public class ApplicationConfig extends Application {

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    // @Override
    // public Set<Class<?>> getClasses() {
    //
    //     Set<Class<?>> classes = new HashSet();
    //     classes.add(EchoEndpoint.class);
    //     classes.add(SecuredEchoEndpoint.class);
    //     classes.add(AttendeeEndpoint.class);
    //     classes.add(AuthenticationFilter.class);
    //     return classes;
    // }
}