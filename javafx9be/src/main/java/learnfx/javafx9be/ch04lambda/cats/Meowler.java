package learnfx.javafx9be.ch04lambda.cats;

public interface Meowler {
    default void meow() {
        System.out.println("MeeeeOww!");
    }
}
