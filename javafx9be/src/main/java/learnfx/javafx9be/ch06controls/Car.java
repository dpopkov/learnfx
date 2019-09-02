package learnfx.javafx9be.ch06controls;

import javafx.scene.image.Image;

class Car {
    private final String description;
    private final Image backwards;
    private final Image forwards;

    public Car(String description, Image backwards, Image forwards) {
        this.description = description;
        this.backwards = backwards;
        this.forwards = forwards;
    }

    public String getDescription() {
        return description;
    }

    public Image getBackwards() {
        return backwards;
    }

    public Image getForwards() {
        return forwards;
    }

    static Car from(String carForwardFile, String carBackwardFile, String description) {
        Image carForward = new Image(carForwardFile);
        Image carBackward = new Image(carBackwardFile);
        return new Car(description, carBackward, carForward);
    }
}
