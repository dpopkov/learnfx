package learnfx.javafx9be.ch10web;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Example of {@link HttpClient} request in blocking mode.
 */
public class HttpClientUsage {
    public static void main(String[] args) throws IOException, InterruptedException {
        String query = "http://openjdk.java.net/jeps/110";
        if (args.length == 1) {
            query = args[0];
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(query)).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status code: " + response.statusCode());
        System.out.println("Response body:");
        System.out.println(response.body());
    }
}
