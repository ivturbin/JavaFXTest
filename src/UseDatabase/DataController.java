package UseDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class DataController implements AddListener{

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

    private ArrayList<Pair<Integer, String>> groups = new ArrayList<>();
    private ArrayList<Student> currentStudents = new ArrayList<>();

    @FXML
    void initialize() {
        databaseOK();
        groups = Database.getGroups();
        List<String> groupNames = new ArrayList<>();
        groups.forEach(pair -> groupNames.add(pair.getValue()));
        cmbStudGroup.setItems(FXCollections.observableArrayList(groupNames));

        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("lastName"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("name"));
        secondNameColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("secondName"));
        birthDateColumn.setCellValueFactory(new PropertyValueFactory<Student, Date>("birthDate"));
        isEnrolledColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("Enrolled"));
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

        AddStudentController ctrl = fxmlLoader.getController();
        ctrl.addListener(this);

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
        Pair<Integer, String> currentGroup = groups.stream()
                .filter(group -> group.getValue() == cmbStudGroup.getValue().toString())
                .collect(Collectors.toList()).get(0); //Получить имя группы из комбобокса, найти соответствующую запись в словаре групп (groups) и записать ее в currentGroup

        Database.setGroupId(currentGroup.getKey());

        setItemsToTable();

    }

    private void setItemsToTable() {
        ArrayList<Student> currentStudents = new ArrayList<>(Database.getStudents().stream()
                .filter(student -> student.getGroupId() == Database.getGroupId())
                .collect(Collectors.toList())); //Получить лист студентов с совпадающим id_group
        tvStudents.setItems(javafx.collections.FXCollections.observableList(currentStudents));
        tvStudents.setDisable(false);

        btnAddStud.setDisable(false);
    }

    private void databaseOK() {
        if (Database.isNotConnected()) {
            btnAddStud.getScene().getWindow().hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Нет подключения к БД.");
            alert.showAndWait();
        }
    }

    @Override
    public void studentAdded() {
        setItemsToTable();
    }
}