package learnfx.javafx9be.ch11javafx3d;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

public class SimpleScene3D extends Application {
    @Override
    public void start(Stage stage) {
        Group sceneRoot = new Group();
        final int sceneWidth = 500;
        final int sceneHeight = 300;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, Color.WHITE);
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-1000);
        scene.setCamera(camera);

        final PhongMaterial redMaterial = createMaterial(Color.DARKRED, Color.RED);
        final PhongMaterial greenMaterial = createMaterial(Color.DARKGREEN, Color.GREEN);
        final PhongMaterial blueMaterial = createMaterial(Color.DEEPSKYBLUE, Color.BLUE);

        // Step 1b: Add a primitive
        final Cylinder cylinder = new Cylinder(50, 100);
        cylinder.setMaterial(blueMaterial);

        // Step 1c: Translate and Rotate primitive into position
        cylinder.setRotationAxis(Rotate.X_AXIS);
        cylinder.setRotate(45);
        cylinder.setTranslateZ(-200);

        // Step 1d: Add and Transform more primitives
        final Box cube = new Box(50, 50, 50);
        cube.setMaterial(greenMaterial);
        final Sphere sphere = new Sphere(50);
        sphere.setMaterial(redMaterial);

        cube.setRotationAxis(Rotate.Y_AXIS);
        cube.setRotate(45);
        cube.setTranslateX(-150);
        cube.setTranslateY(-150);
        cube.setTranslateZ(150);
        sphere.setTranslateX(150);
        sphere.setTranslateY(150);
        sphere.setTranslateZ(-150);

        // Step 1e: All Together Now: Grouped Primitives
        Group primitiveGroup = new Group(cylinder, cube, sphere);
        primitiveGroup.setRotationAxis(Rotate.Z_AXIS);
        primitiveGroup.setRotate(180);
        sceneRoot.getChildren().addAll(primitiveGroup);

        // Step 2a: Primitive Picking for Primitives
        scene.setOnMouseClicked(event -> {
            Node picked = event.getPickResult().getIntersectedNode();
            if (picked != null) {
                double scalar = 2.0;
                if (picked.getScaleX() > 1.0) {
                    scalar = 1.0;
                }
                picked.setScaleX(scalar);
                picked.setScaleY(scalar);
                picked.setScaleZ(scalar);
            }
        });

        stage.setTitle("SimpleScene3D");
        stage.setScene(scene);
        stage.show();
    }

    private PhongMaterial createMaterial(Color diffuseColor, Color specularColor) {
        final PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuseColor);
        material.setSpecularColor(specularColor);
        return material;
    }
}
