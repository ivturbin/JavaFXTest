package UseDatabase;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

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
    private CheckBox isEnrolledCheckbox;
    ArrayList<AddListener> listeners = new ArrayList<>();

    @FXML
    void initialize() {
        addButton.setDisable(true);

        lastNameField.textProperty().addListener(observable -> checkAddButton());
        nameField.textProperty().addListener(observable -> checkAddButton());
    }

    @FXML
    void setAddButton() {
        Student student = new Student(0, nameField.getText(), secondNameField.getText(),
                lastNameField.getText(), birthDatePicker.getValue()==null? LocalDate.EPOCH : birthDatePicker.getValue(), isEnrolledCheckbox.isSelected(), Database.getGroupId());
        if(!Database.addStudent(student)) {
            addButton.getScene().getWindow().hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Студент не добавлен.");
            alert.showAndWait();
        } else {
            addButton.getScene().getWindow().hide();
            for (AddListener listener:
                 listeners) {
                listener.studentAdded();
            }
        }
    }

    public void addListener(AddListener addListener) {
        listeners.add(addListener);
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
