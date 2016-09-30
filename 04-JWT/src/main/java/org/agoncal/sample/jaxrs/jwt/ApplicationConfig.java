package org.agoncal.sample.jaxrs.jwt;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationPath("rs")
public class ApplicationConfig extends Application {

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    @Override
    public Set<Class<?>> getClasses() {

        Set<Class<?>> classes = new HashSet();
        // classes.add(JWTAuthFilter.class);
        return classes;
    }
}