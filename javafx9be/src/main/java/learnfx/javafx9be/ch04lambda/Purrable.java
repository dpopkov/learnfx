package learnfx.javafx9be.ch04lambda;

public interface Purrable {
    default void purr() {
        System.out.println("Purrrrrrr...");
    }
}
