package com.example.periodic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import javax.swing.text.html.parser.Element;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PeriodicTableController implements Initializable {
    public List<Elements> elements;

    public void readCsv(String filename) throws FileNotFoundException {
        elements = new ArrayList<>();
        File fin = new File(filename);
        Scanner sfin = new Scanner(fin);
        String firstLine = sfin.nextLine();
        while (sfin.hasNextLine()){
            String fileLine = sfin.nextLine();
            elements.add(new Elements(fileLine));
        }
        sfin.close();
        System.out.println(elements.size());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            readCsv("periodic-table-data.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}