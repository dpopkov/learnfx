package learnfx.javafx9be.ch04lambda;

/**
 * Represents an abstract Cat containing default methods common to all cats.
 */
public interface Cat {
    String getCatKind();

    String getFurDescription();

    default void growl() {
        System.out.println("Crrrrowl!!");
    }

    default void walk() {
        System.out.println(getCatKind() + " is walking.");
    }

    default void eat() {
        System.out.println(getCatKind() + " is eating.");
    }

    default void sleep() {
        System.out.println(getCatKind() + " is sleeping");
    }
}
