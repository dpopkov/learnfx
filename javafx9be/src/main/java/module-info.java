module javafx9be.main {
    requires javafx.graphics;
    requires javafx.controls;
    requires fontawesomefx;
    requires java.logging;
    requires java.desktop;
    requires javafx.swing;
    requires javafx.web;
    requires java.net.http;
    requires javafx.fxml;
    requires javafx.media;

    opens learnfx.javafx9be.ch01gs to javafx.graphics;
    opens learnfx.javafx9be.ch03fund to javafx.graphics;
    opens learnfx.javafx9be.ch04lambda to javafx.graphics;
    opens learnfx.javafx9be.ch05layouts to javafx.graphics;
    opens learnfx.javafx9be.ch06controls to javafx.graphics;
    opens learnfx.javafx9be.ch07graphics to javafx.graphics;
    opens learnfx.javafx9be.ch07graphics.animation to javafx.graphics;
    opens learnfx.javafx9be.ch09media to javafx.graphics;
    opens learnfx.javafx9be.ch10web to javafx.graphics;
    opens learnfx.javafx9be.ch11javafx3d to javafx.graphics;
}
