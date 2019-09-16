package learnfx.javafx9be.ch10web.spark;

import static spark.Spark.*;

/**
 * View at: http://localhost:4567/hello
 */
public class HelloSpark {
    public static void main(String[] args) {
        get("/hello", (req, resp) -> "Hello Spark!");
    }
}
