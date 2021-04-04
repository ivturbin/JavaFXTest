package UseDatabase;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import javafx.util.converter.BooleanStringConverter;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataController {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabStudents;

    @FXML
    private Button btnAddStud;

    @FXML
    private Button btnEdit;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Student> tvStudents;

    @FXML
    private ComboBox cmbStudGroup;

    @FXML
    private Tab tabJournal;

    @FXML
    private ComboBox<?> cmbJourGroup;

    @FXML
    private ComboBox<?> cmbSemester;

    @FXML
    private TableColumn<Student, String> lastNameColumn;

    @FXML
    private TableColumn<Student, String> nameColumn;

    @FXML
    private TableColumn<Student, String> secondNameColumn;

    @FXML
    private TableColumn<Student, Date> birthDateColumn;

    @FXML
    private TableColumn<Student, String> isEnrolledColumn;

    private Database database = new Database();
    private ArrayList<Pair<Integer, String>> groups = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();

    @FXML
    void initialize() {
        database.connect(Config.SUPERUSER_NAME, Config.SUPERUSER_PASSWORD);
        databaseOK();
        groups = database.getGroups();
        students = database.getStudents();
        List<String> groupNames = new ArrayList<>();
        groups.forEach(pair -> groupNames.add(pair.getValue()));
        cmbStudGroup.setItems(FXCollections.observableArrayList(groupNames));
    }

    @FXML
    void btnAddStudPressed() {
        System.out.println("Добавить");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/UseDatabase/scenes/AddStud.fxml"));

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = fxmlLoader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.getScene().getStylesheets().add("/UseDatabase/assets/JMetroDarkTheme.css");
        stage.setTitle("Добавить студента");
        stage.showAndWait();
    }

    @FXML
    void cmbStudGroupPresed() {
        Pair<Integer, String> pair = groups.stream()
                .filter(group -> group.getValue() == cmbStudGroup.getValue().toString())
                .collect(Collectors.toList()).get(0); //Получить имя группы из комбобокса, найти соответствующую запись в словаре групп (groups) и записать ее в pair

        ArrayList<Student> currentStudents = new ArrayList<>(students.stream()
                .filter(student -> student.getGroupId() == pair.getKey())
                .collect(Collectors.toList())); //Получить лист студентов с совпадающим id_group

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        secondNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("secondName"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<Student, Date>("birthDate"));
        isEnrolledColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("Enrolled"));

        tvStudents.setItems(FXCollections.observableArrayList(currentStudents));
        tvStudents.setDisable(false);

        btnAddStud.setDisable(false);
    }

    private void databaseOK() {
        if (!database.isConnected()) {
            btnAddStud.getScene().getWindow().hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Нет подключения к БД.");
            alert.showAndWait();
        }
    }

}