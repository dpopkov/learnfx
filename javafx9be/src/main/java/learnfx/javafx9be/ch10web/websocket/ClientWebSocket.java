package learnfx.javafx9be.ch10web.websocket;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

public class ClientWebSocket {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> future = client.newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:4567/echo"), new ClientSocketListener());
        WebSocket clientWebSocket = future.join();

        clientWebSocket.sendText("Hello World1 " + new Date(), true);
        clientWebSocket.sendText("Hello World2 " + new Date(), true);
    }
}
