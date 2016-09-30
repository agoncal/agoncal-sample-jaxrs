package org.agoncal.sample.jaxrs.jwt.rest;

import org.agoncal.sample.jaxrs.jwt.domain.Attendee;
import org.agoncal.sample.jaxrs.jwt.repository.AttendeeRepository;
import org.agoncal.sample.jaxrs.jwt.util.LoggerProducer;
import org.agoncal.sample.jaxrs.jwt.util.PasswordUtils;
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
public class AttendeeEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private static final Attendee TEST_ATTENDEE = new Attendee("id", "last name", "first name", "login", "password");
    private static String attendeeId;
    private Client client;
    private WebTarget webTarget;

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
                .addClasses(Attendee.class, AttendeeRepository.class, AttendeeEndpoint.class)
                .addClasses(PasswordUtils.class, Secured.class, LoggerProducer.class, AttendeeApplicationConfig.class)
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
        webTarget = client.target(baseURL).path("api/attendees");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    public void shouldFailLogin() throws Exception {
        Form form = new Form();
        form.param("username", "dummyUsername");
        form.param("password", "dummyPaswword");

        Response response = webTarget.path("login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));

        assertEquals(401, response.getStatus());
        assertNull(response.getHeaderString(HttpHeaders.AUTHORIZATION));
    }

    @Test
    @InSequence(1)
    public void shouldGetAllAttendees() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
    }

    @Test
    @InSequence(2)
    public void shouldCreateAttendee() throws Exception {
        Response response = webTarget.request(APPLICATION_JSON_TYPE).post(Entity.entity(TEST_ATTENDEE, APPLICATION_JSON_TYPE));
        assertEquals(201, response.getStatus());
        attendeeId = getAttendeeId(response);
    }

    @Test
    @InSequence(3)
    public void shouldGetAlreadyCreatedAttendee() throws Exception {
        Response response = webTarget.path(attendeeId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(200, response.getStatus());
        JsonObject jsonObject = readJsonContent(response);
        assertEquals(attendeeId, jsonObject.getString("id"));
        assertEquals(TEST_ATTENDEE.getLastName(), jsonObject.getString("lastName"));
    }

    @Test
    @InSequence(4)
    public void shouldRemoveAttendee() throws Exception {
        Response response = webTarget.path(attendeeId).request(APPLICATION_JSON_TYPE).delete();
        assertEquals(204, response.getStatus());
        Response checkResponse = webTarget.path(attendeeId).request(APPLICATION_JSON_TYPE).get();
        assertEquals(404, checkResponse.getStatus());
    }

    // ======================================
    // =           Private methods          =
    // ======================================

    private String getAttendeeId(Response response) {
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
