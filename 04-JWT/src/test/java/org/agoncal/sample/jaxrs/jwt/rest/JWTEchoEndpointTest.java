package org.agoncal.sample.jaxrs.jwt.rest;

import io.jsonwebtoken.Jwts;
import org.agoncal.sample.jaxrs.jwt.domain.User;
import org.agoncal.sample.jaxrs.jwt.filter.JWTTokenNeeded;
import org.agoncal.sample.jaxrs.jwt.filter.JWTTokenNeededFilter;
import org.agoncal.sample.jaxrs.jwt.util.KeyGenerator;
import org.agoncal.sample.jaxrs.jwt.util.LoggerProducer;
import org.agoncal.sample.jaxrs.jwt.util.PasswordUtils;
import org.agoncal.sample.jaxrs.jwt.util.SimpleKeyGenerator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.net.URI;
import java.security.Key;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
@RunAsClient
public class JWTEchoEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final User TEST_USER = new User("id", "last name", "first name", "login", "password");
    private static String token;
    private Client client;
    private WebTarget echoEndpointTarget;
    private WebTarget userEndpointTarget;

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

        // Import Maven runtime dependencies
        File[] files = Maven.resolver().loadPomFromFile("pom.xml")
                .importRuntimeDependencies().resolve().withTransitivity().asFile();

        return ShrinkWrap.create(WebArchive.class)
                .addClasses(EchoEndpoint.class)
                .addClasses(User.class, UserEndpoint.class)
                .addClasses(JWTTokenNeededFilter.class, JWTTokenNeeded.class, KeyGenerator.class, SimpleKeyGenerator.class, PasswordUtils.class)
                .addClasses(LoggerProducer.class, JWTEchoApplicationConfig.class)
                .addAsResource("META-INF/persistence-test.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(files);
    }

    // ======================================
    // =          Lifecycle methods         =
    // ======================================

    @Before
    public void initWebTarget() {
        client = ClientBuilder.newClient();
        echoEndpointTarget = client.target(baseURL).path("api/echo/jwt");
        userEndpointTarget = client.target(baseURL).path("api/users");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    @InSequence(1)
    public void invokingEchoShouldFailCauseNoToken() throws Exception {
        Response response = echoEndpointTarget.request(TEXT_PLAIN).get();
        assertEquals(401, response.getStatus());
    }

    @Test
    @InSequence(2)
    public void shouldCreateAUser() throws Exception {
        Response response = userEndpointTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_USER, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
    }

    @Test
    @InSequence(3)
    public void shouldLogUserIn() throws Exception {
        Form form = new Form();
        form.param("login", TEST_USER.getLogin());
        form.param("password", TEST_USER.getPassword());

        Response response = userEndpointTarget.path("login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertEquals(200, response.getStatus());
        assertNotNull(response.getHeaderString(HttpHeaders.AUTHORIZATION));
        token = response.getHeaderString(HttpHeaders.AUTHORIZATION);

        // Check the JWT Token
        String justTheToken = token.substring("Bearer".length()).trim();
        Key key = new SimpleKeyGenerator().generateKey();
        assertEquals(1, Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getHeader().size());
        assertEquals("HS512", Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getHeader().getAlgorithm());
        assertEquals(4, Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getBody().size());
        assertEquals("login", Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getBody().getSubject());
        assertEquals(baseURL.toString().concat("api/users/login"), Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getBody().getIssuer());
        assertNotNull(Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getBody().getIssuedAt());
        assertNotNull(Jwts.parser().setSigningKey(key).parseClaimsJws(justTheToken).getBody().getExpiration());
    }

    @Test
    @InSequence(4)
    public void invokingEchoShouldSucceedCauseToken() throws Exception {
        Response response = echoEndpointTarget.request(TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, token).get();
        assertEquals(200, response.getStatus());
        assertEquals("no message", response.readEntity(String.class));
    }


    @Test
    @InSequence(5)
    public void shouldEchoHello() throws Exception {
        Response response = echoEndpointTarget.queryParam("message", "hello").request(TEXT_PLAIN).header(HttpHeaders.AUTHORIZATION, token).get();
        assertEquals(200, response.getStatus());
        assertEquals("hello", response.readEntity(String.class));
    }
}
