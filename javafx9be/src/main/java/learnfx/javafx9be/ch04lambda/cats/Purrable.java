package learnfx.javafx9be.ch04lambda.cats;

public interface Purrable {
    default void purr() {
        System.out.println("Purrrrrrr...");
    }
}
