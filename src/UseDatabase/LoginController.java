package UseDatabase;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.xml.crypto.Data;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    void initialize() {
        Database database = new Database();
        database.connect(Config.SUPERUSER_NAME, Config.SUPERUSER_PASSWORD);
        if (!database.isConnected()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Нет подключения к БД.");
            alert.showAndWait();
            loginButton.getScene().getWindow().hide();
        }
        loginButton.setOnAction(actionEvent -> {
            System.out.println("Войти");
            String username = loginField.getText().trim();
            String password = passwordField.getText().trim();
            if (username.equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Введите логин.");
                System.out.println("Логин не введен");
                alert.showAndWait();
            } else {
                connect(username, password, database);
            }
        });
    }

    void connect(String username, String password, Database database) {
            if (database.hasUser(username)) {
                if (database.correctPassword(username, password)) {
                    loginButton.getScene().getWindow().hide();
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/UseDatabase/scenes/Data.fxml"));

                    try {
                        fxmlLoader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Parent root = fxmlLoader.getRoot();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText(null);
                    alert.setContentText("Пароль введен неверно.");
                    System.out.println("Введен некорректный пароль");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(null);
                alert.setContentText("Нет пользователя с таким логином.");
                System.out.println("Введен некорректный логин");
                alert.showAndWait();
            }
    }
}

