package UseDatabase;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class DataController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button showButton;

    @FXML
    void initialize() {
        assert showButton != null : "fx:id=\"showButton\" was not injected: check your FXML file 'Data.fxml'.";

    }
}

