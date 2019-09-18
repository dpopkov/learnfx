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
    private PerspectiveCamera camera;
    private final double cameraQuantity = 10.0;
    private final double cameraModifier = 50.0;
    private final double rotateModifier = 10;
    private final double cameraYLimit = 15;

    private double mouseXOld = 0;
    private double mouseYOld = 0;

    @Override
    public void start(Stage stage) {
        Group sceneRoot = new Group();
        final int sceneWidth = 500;
        final int sceneHeight = 300;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, Color.WHITE);
        camera = new PerspectiveCamera(true);
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

        // Step 2: Add a Movement Keyboard Handler
        scene.setOnKeyPressed(event -> {
            double change = cameraQuantity;
            if (event.isShiftDown()) {
                change = cameraModifier;
            }
            switch (event.getCode()) {
                case W: camera.setTranslateZ(camera.getTranslateZ() + change); break;
                case S: camera.setTranslateZ(camera.getTranslateZ() - change); break;
                case A: camera.setTranslateX(camera.getTranslateX() - change); break;
                case D: camera.setTranslateX(camera.getTranslateX() + change); break;
            }
        });

        // Step 3: Add a Camera Control Mouse Event Handler
        scene.setOnMouseMoved(event -> {
            double mouseYNew = event.getSceneY();
            double mouseXNew = event.getSceneX();
            double aTan2Y =  mouseYNew - mouseYOld;
            double aTan2X = mouseXNew - mouseXOld;
            Rotate xRotate = new Rotate();
            Rotate yRotate = new Rotate();
            if (mouseYNew != mouseYOld) {
                xRotate = cameraRotationOnXAxis(aTan2Y, aTan2X);
            }
            if(mouseXNew != mouseXOld) {
                yRotate = cameraRotationOnYAxis(aTan2Y, aTan2X);
            }
            camera.getTransforms().addAll(xRotate,yRotate);
            mouseXOld = mouseXNew;
            mouseYOld = mouseYNew;
        });

        stage.setTitle("SimpleScene3D");
        stage.setScene(scene);
        stage.show();
    }

    private Rotate cameraRotationOnYAxis(double aTan2Y, double aTan2X) {
        camera.setRotationAxis(Rotate.Y_AXIS);
        double yawRotate = camera.getRotate() + (Math.atan2(aTan2X, aTan2Y) / rotateModifier);
        return new Rotate(yawRotate, Rotate.Y_AXIS);
    }

    private Rotate cameraRotationOnXAxis(double aTan2Y, double aTan2X) {
        camera.setRotationAxis(Rotate.X_AXIS);
        double pitchRotate = camera.getRotate() + (Math.atan2(aTan2Y, aTan2X) / rotateModifier);
        pitchRotate = Math.min(pitchRotate, cameraYLimit);
        pitchRotate = Math.max(pitchRotate, -cameraYLimit);
        return new Rotate(pitchRotate, Rotate.X_AXIS);
    }

    private PhongMaterial createMaterial(Color diffuseColor, Color specularColor) {
        final PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(diffuseColor);
        material.setSpecularColor(specularColor);
        return material;
    }
}
