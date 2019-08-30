package learnfx.javafx9be.ch04lambda;

public interface Roarable {
    default void roar() {
        System.out.println("Roar!");
    }
}
