package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashBoardFormController {
    public AnchorPane pane;
    public Button btnCustomer;

    public void customerButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)pane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/CustomerForm.fxml")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}
