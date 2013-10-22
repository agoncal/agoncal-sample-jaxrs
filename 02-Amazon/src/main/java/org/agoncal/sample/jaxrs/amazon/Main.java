package org.agoncal.sample.jaxrs.amazon;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
public class Main {

    public static void main(String[] args) {

        URI uriAmazon = UriBuilder.fromUri("http://free.apisigning.com/onca/xml").queryParam("Service", "AWSECommerceService").queryParam("AWSAccessKeyId", "AKIAIYNLC7WME6YSY66A").build();
        URI uriSearch = UriBuilder.fromUri(uriAmazon).queryParam("Operation", "ItemSearch").build();
        URI uriSearchBooks = UriBuilder.fromUri(uriSearch).queryParam("SearchIndex", "Books").build();
        Client client = ClientBuilder.newClient();

        URI uriSearchBooksByKeyword = UriBuilder.fromUri(uriSearchBooks).queryParam("Keywords", "Java EE 7").build();
        URI uriSearchBooksWithImages = UriBuilder.fromUri(uriSearchBooks).queryParam("Condition", "All").queryParam("ResponseGroup", "Images").queryParam("Title", "Java EE 7").build();

        System.out.println(uriSearchBooksByKeyword.toString());
        System.out.println(uriSearchBooksWithImages.toString());

        Response response = client.target(uriSearchBooksByKeyword).request().get();
        System.out.println(response.getStatus());
        response = client.target(uriSearchBooksWithImages).request().get();
        System.out.println(response.getStatus());
    }
}
