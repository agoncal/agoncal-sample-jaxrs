package org.agoncal.sample.jaxrs.jwt.rest;

import org.agoncal.sample.jaxrs.jwt.domain.User;
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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.StringReader;
import java.net.URI;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(Arquillian.class)
@RunAsClient
public class UserEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final User TEST_USER = new User("id", "last name", "first name", "login", "password");
    private static String userId;
    private Client client;
    private WebTarget userTarget;

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
                .addClasses(User.class, UserEndpoint.class)
                .addClasses(PasswordUtils.class, KeyGenerator.class, SimpleKeyGenerator.class, LoggerProducer.class, UserApplicationConfig.class)
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
        userTarget = client.target(baseURL).path("api/users");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    public void shouldFailLogin() throws Exception {
        Form form = new Form();
        form.param("login", "dummyLogin");
        form.param("password", "dummyPassword");

        Response response = userTarget.path("login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertEquals(401, response.getStatus());
        assertNull(response.getHeaderString(HttpHeaders.AUTHORIZATION));
    }

    @Test
    @InSequence(1)
    public void shouldGetAllUsers() throws Exception {
        Response response = userTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
    }

    @Test
    @InSequence(2)
    public void shouldCreateUser() throws Exception {
        Response response = userTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_USER, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        userId = getUserId(response);
    }

    @Test
    @InSequence(3)
    public void shouldGetAlreadyCreatedUser() throws Exception {
        Response response = userTarget.path(userId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(userId, jsonObject.getString("id"));
        assertEquals(TEST_USER.getLastName(), jsonObject.getString("lastName"));
    }

    @Test
    @InSequence(4)
    public void shouldRemoveUser() throws Exception {
        Response response = userTarget.path(userId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(204, response.getStatus());
        Response checkResponse = userTarget.path(userId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private String getUserId(Response response) {
        String location = response.getHeaderString("location");
        return location.substring(location.lastIndexOf("/") + 1);
    }

    private static JsonObject readJsonContent(Response response) {
        JsonReader jsonReader = readJsonStringFromResponse(response);
        return jsonReader.readObject();
    }

    private static JsonReader readJsonStringFromResponse(Response response) {
        String jsonString = response.readEntity(String.class);
        StringReader stringReader = new StringReader(jsonString);
        return Json.createReader(stringReader);
    }
}
