package com.example.periodic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PeriodicTableController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}