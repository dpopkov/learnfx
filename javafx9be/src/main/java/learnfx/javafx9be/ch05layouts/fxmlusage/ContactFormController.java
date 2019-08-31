package learnfx.javafx9be.ch05layouts.fxmlusage;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ContactFormController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;

    @FXML
    protected void saveContactAction(ActionEvent event) {
        System.out.println("Saving the following information:");
        System.out.println("First Name: " + firstNameField.getText());
        System.out.println(" Last Name: " + lastNameField.getText());
    }
}
