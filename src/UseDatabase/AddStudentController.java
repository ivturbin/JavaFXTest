package UseDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class AddStudentController {

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private Button addButton;

    @FXML
    void isEnrolledCheckbox(ActionEvent event) {

    }

    @FXML
    void initialize() {
        addButton.setDisable(true);

        lastNameField.textProperty().addListener(observable -> checkAddButton());
        nameField.textProperty().addListener(observable -> checkAddButton());
    }

    @FXML
    void setAddButton() {

    }

    private void checkAddButton() {
        if ((lastNameField.getText().length() > 0) && (nameField.getText().length() > 0 )) {
            addButton.setDisable(false);
        }
        else {
            addButton.setDisable(true);
        }
    }

}
