package learnfx.javafx9be.ch10web.spark;

import static spark.Spark.init;
import static spark.Spark.webSocket;

public class WebSocketStarter {
    public static void main(String[] args) {
        webSocket("/echo", EchoWebSocket.class);
        init();
    }
}
