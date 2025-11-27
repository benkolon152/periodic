package com.example.periodic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import javax.swing.text.html.parser.Element;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class PeriodicTableController implements Initializable {
    public List<Elements> elements;

    public int[][] posMatrix= {
            {1,1}
    };

    public Map<Integer, Integer[]> posMap = Map.of(1, new Integer[] {1,1});

    public Map<String, Integer[]> symbolPosMap;

    public void populateSymbolPosMap(){
        symbolPosMap = new HashMap<String, Integer[]>();
        symbolPosMap.put("H", new Integer[]{1,1});
        symbolPosMap.put("He", new Integer[]{1, 18});
        symbolPosMap.put("Li", new Integer[]{2,1});
        symbolPosMap.put("Be", new Integer[]{2,2});
        symbolPosMap.put("B", new Integer[]{2,13});
        symbolPosMap.put("C", new Integer[]{2,14});
        symbolPosMap.put("N", new Integer[]{2,15});
        symbolPosMap.put("O", new Integer[]{2,16});
        symbolPosMap.put("F", new Integer[]{2,17});
        symbolPosMap.put("Ne", new Integer[]{2,18});
        symbolPosMap.put("Na", new Integer[]{3,1});
        symbolPosMap.put("Mg", new Integer[]{3,2});
        symbolPosMap.put("Al", new Integer[]{3,13});
        symbolPosMap.put("Si", new Integer[]{3,14});
        symbolPosMap.put("P", new Integer[]{3,15});
        symbolPosMap.put("S", new Integer[]{3,16});
        symbolPosMap.put("Cl", new Integer[]{3,17});
        symbolPosMap.put("Ar", new Integer[]{3,18});
    }


    @FXML public GridPane periodicGrid;

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

    public void addElement(
            GridPane grid,
            int atomicNum,
            int col, int row,
            Color color,
            String symbol,
            String name
    ){
        Rectangle rect = new Rectangle(60, 50);
        rect.setStroke(Color.BLACK);
        rect.setFill(color);
        rect.setArcHeight(8);
        rect.setArcWidth(8);

        Label symbolLabel = new Label(symbol);
        symbolLabel.setTranslateX(25);
        symbolLabel.setTranslateY(-10);
        symbolLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 16px;");

        Label nameLabel = new Label(name);
        nameLabel.setTranslateX(8);
        nameLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: black;");

        grid.add(rect, col, row);
        grid.add(symbolLabel, col, row);
        grid.add(nameLabel, col, row);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            readCsv("periodic-table-data.csv");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        populateSymbolPosMap();

        // place and show elements on view
        /*addElement(periodicGrid,
                5, 13, 2, Color.web("#FFB5B5"), "B", "Boron");*/

        for (int i = 0; i < 18; i++){
            Elements element = elements.get(i);
            addElement(periodicGrid,
                    element.atomicNumber,
                    symbolPosMap.get(element.symbol)[1], symbolPosMap.get(element.symbol)[0],
                    element.cpkColor, element.symbol, element.name);
        }
    }
}