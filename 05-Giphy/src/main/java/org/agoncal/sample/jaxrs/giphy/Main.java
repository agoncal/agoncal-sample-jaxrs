package org.agoncal.sample.jaxrs.giphy;

import org.apache.commons.io.FileUtils;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class Main {

    private static final String GIPHY_SEARCH_URL = "http://api.giphy.com/v1/gifs/search?api_key=dc6zaTOxFJmzC&limit=100&rating=g&q=";

    public static void main(String[] args) throws IOException {
        goAndGetGiphy("cats");
        goAndGetGiphy("zombies");
    }

    private static void goAndGetGiphy(String what) throws IOException {
        String json = ClientBuilder.newClient().target(GIPHY_SEARCH_URL + what).request().get(String.class);
        readGiphyAndWriteJSon(json, what);
    }

    private static void readGiphyAndWriteJSon(String json, String what) throws IOException {
        JsonReader jsonReader = Json.createReader(new StringReader(json));

        JsonGenerator jsonGenerator = getJsonGenerator(what);
        jsonGenerator.writeStartObject().writeStartArray(what);

        JsonArray jsonArray = jsonReader.readObject().getJsonArray("data");

        for (Object aJsonArray : jsonArray) {
            JsonObject j = (JsonObject) aJsonArray;
            jsonGenerator.writeStartObject();
            jsonGenerator.write("id", j.getJsonString("id"));
            jsonGenerator.write("type", j.getJsonString("type"));
            jsonGenerator.write("slug", j.getJsonString("slug"));
            jsonGenerator.write("trending_datetime", j.getJsonString("trending_datetime"));
            jsonGenerator.write("strength", (int) (Math.random() * 100));
            jsonGenerator.write("url", j.getJsonObject("images").getJsonObject("fixed_height").getJsonString("url"));
            jsonGenerator.write("localUrl", getImageFromURLAndStoreIntoAFile(what, j));
            jsonGenerator.write("width", j.getJsonObject("images").getJsonObject("fixed_height").getJsonString("width"));
            jsonGenerator.write("height", j.getJsonObject("images").getJsonObject("fixed_height").getJsonString("height"));
            jsonGenerator.write("size", j.getJsonObject("images").getJsonObject("fixed_height").getJsonString("size"));
            jsonGenerator.writeEnd();
        }
        jsonGenerator.writeEnd().writeEnd();
        jsonGenerator.close();
    }

    private static String getImageFromURLAndStoreIntoAFile(String what, JsonObject j) throws IOException {
        String fileName = j.getJsonString("id").getString() + "." + j.getJsonString("type").getString();
        InputStream stream = ClientBuilder.newClient().target(j.getJsonObject("images").getJsonObject("fixed_height").getJsonString("url").getString()).request(MediaType.APPLICATION_OCTET_STREAM).get(InputStream.class);
        FileUtils.copyInputStreamToFile(stream, createFile(what, fileName));
        return fileName;
    }

    private static JsonGenerator getJsonGenerator(String what) throws IOException {
        Map<String, Boolean> config = new HashMap<>();
        config.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonGeneratorFactory generatorFactory = Json.createGeneratorFactory(config);
        return generatorFactory.createGenerator(new FileWriter(createFile(what, what + ".json")));
    }

    private static File createFile(String what, String fileName) {
        File root = new File("output/" + what);
        root.mkdirs();
        return new File(root, fileName);
    }
}
