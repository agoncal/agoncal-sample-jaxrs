package org.agoncal.sample.jaxrs.qbe.provider.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@ApplicationPath("/rest")
public class ApplicationConfig extends Application {

    // ======================================
    // =             Attributes             =
    // ======================================

    private final Set<Class<?>> classes;

    // ======================================
    // =            Constructors            =
    // ======================================

    public ApplicationConfig() {
        HashSet<Class<?>> c = new HashSet<>();
        c.add(BookEndpoint.class);
        c.add(BookParamConverterProvider.class);
        classes = Collections.unmodifiableSet(c);
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }
}