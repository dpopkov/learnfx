package learnfx.javafx9be.ch04lambda.cats;

public interface Roarable {
    default void roar() {
        System.out.println("Roar!");
    }
}
