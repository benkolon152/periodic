package com.example.periodic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.swing.text.html.parser.Element;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PeriodicTableController implements Initializable {
    public List<Element> elements;

    public void readCsv(String filename){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readCsv("periodic-table-data.csv");
    }
}