package org.agoncal.sample.jaxrs.jwt.rest;

import org.agoncal.sample.jaxrs.jwt.filter.JWTTokenNeeded;
import org.agoncal.sample.jaxrs.jwt.util.LoggerProducer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class EchoEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Client client;
    private WebTarget echoTarget;

    // ======================================
    // =          Injection Points          =
    // ======================================

    @ArquillianResource
    private URI baseURL;

    // ======================================
    // =         Deployment methods         =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class)
                .addClasses(EchoEndpoint.class, LoggerProducer.class, JWTTokenNeeded.class, EchoApplicationConfig.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =          Lifecycle methods         =
    // ======================================

    @Before
    public void initWebTarget() {
        client = ClientBuilder.newClient();
        echoTarget = client.target(baseURL).path("api/echo");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    public void shouldEchoNoMessage() throws Exception {
        Response response = echoTarget.request(TEXT_PLAIN).get();
        assertEquals(200, response.getStatus());
        assertEquals("no message", response.readEntity(String.class));
    }

    @Test
    public void shouldEchoHello() throws Exception {
        Response response = echoTarget.queryParam("message", "hello").request(TEXT_PLAIN).get();
        assertEquals(200, response.getStatus());
        assertEquals("hello", response.readEntity(String.class));
    }
}
