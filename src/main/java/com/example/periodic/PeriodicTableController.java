package com.example.periodic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;

import java.io.*;
import java.net.URL;
import java.util.*;

public class PeriodicTableController implements Initializable {
    public List<Elements> elements;

    @FXML public GridPane periodicGrid;

    /**
     * Read CSV from classpath resource `periodic-table-data.csv` if present,
     * otherwise fall back to a file by the given filename in working dir.
     */
    public void readCsv(String filename) {
        elements = new ArrayList<>();
        Scanner sfin = null;
        InputStream is = PeriodicTableController.class.getResourceAsStream("periodic-table-data.csv");
        try {
            if (is != null) {
                sfin = new Scanner(new BufferedInputStream(is));
            } else {
                File fin = new File(filename);
                sfin = new Scanner(fin);
            }
            // Attempt to skip a header line if present
            if (sfin.hasNextLine()) {
                String header = sfin.nextLine();
                // if header looks like data (starts with digit) put it back by creating an iterator style handling
                if (header.trim().isEmpty() == false && Character.isDigit(header.trim().charAt(0))) {
                    // header appears to be data, process it
                    elements.add(new Elements(header));
                }
            }
            while (sfin.hasNextLine()) {
                String fileLine = sfin.nextLine().trim();
                if (fileLine.isEmpty()) continue;
                if (fileLine.startsWith("#")) continue;
                elements.add(new Elements(fileLine));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (sfin != null) sfin.close();
            if (is != null) {
                try { is.close(); } catch (IOException ignored) {}
            }
        }
        System.out.println("Read elements: " + elements.size());
    }

    public void addElement(
            GridPane grid,
            int atomicNum,
            int col, int row,
            Color color,
            String symbol,
            String name
    ){
        // convert 1-based to 0-based indices for GridPane
        int c = Math.max(0, col - 1);
        int r = Math.max(0, row - 1);

        Rectangle rect = new Rectangle(80, 60);
        rect.setStroke(Color.BLACK);
        rect.setFill(color);
        rect.setArcHeight(8);
        rect.setArcWidth(8);

        Label symbolLabel = new Label(symbol);
        symbolLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black; -fx-font-size: 16px;");

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: black;");

        VBox labelsBox = new VBox(2, symbolLabel, nameLabel);
        labelsBox.setAlignment(Pos.CENTER);

        StackPane cell = new StackPane(rect, labelsBox);
        cell.setPadding(new Insets(4));
        cell.setAlignment(Pos.CENTER);

        // allow cell to grow to fill the grid cell if constraints permit
        GridPane.setHgrow(cell, Priority.ALWAYS);
        GridPane.setVgrow(cell, Priority.ALWAYS);
        GridPane.setValignment(cell, VPos.CENTER);

        grid.add(cell, c, r);
    }

    /**
     * Return row,col (1-based) for the given atomic number using a standard layout:
     * rows 1..7 = main periods, row 8 = lanthanides (58..71), row 9 = actinides (90..103).
     */
    private Integer[] getCoordsForAtomicNumber(int z){
        if (z == 1) return new Integer[]{1,1};            // H
        if (z == 2) return new Integer[]{1,18};           // He

        // Period 2: 3-10 -> cols 1,2,13..18
        if (z >= 3 && z <= 10){
            if (z == 3) return new Integer[]{2,1};
            if (z == 4) return new Integer[]{2,2};
            int offset = z - 5; // 5->col13
            return new Integer[]{2, 13 + offset};
        }

        // Period 3: 11-18 -> cols 1,2,13..18
        if (z >= 11 && z <= 18){
            if (z == 11) return new Integer[]{3,1};
            if (z == 12) return new Integer[]{3,2};
            int offset = z - 13; // 13->col13
            return new Integer[]{3, 13 + offset};
        }

        // Period 4: 19-36 -> cols 1..18 (row 4)
        if (z >= 19 && z <= 36){
            return new Integer[]{4, 1 + (z - 19)};
        }

        // Period 5: 37-54 -> cols 1..18 (row 5)
        if (z >= 37 && z <= 54){
            return new Integer[]{5, 1 + (z - 37)};
        }

        // Period 6 main row: 55..86
        if (z >= 55 && z <= 86){
            if (z == 55) return new Integer[]{6,1};
            if (z == 56) return new Integer[]{6,2};
            if (z == 57) return new Integer[]{6,3}; // La
            if (z >= 72){ // 72..86 -> cols 4..18
                return new Integer[]{6, 4 + (z - 72)};
            }
            // 58..71 are lanthanides -> handled below
        }

        // Period 7 main row: 87..118
        if (z >= 87 && z <= 118){
            if (z == 87) return new Integer[]{7,1};
            if (z == 88) return new Integer[]{7,2};
            if (z == 89) return new Integer[]{7,3}; // Ac
            if (z >= 104){ // 104..118 -> cols 4..18
                return new Integer[]{7, 4 + (z - 104)};
            }
            // 90..103 are actinides -> handled below
        }

        // Lanthanides row (separate): 58..71 -> row 8 cols 4..17
        if (z >= 58 && z <= 71){
            return new Integer[]{8, 4 + (z - 58)};
        }

        // Actinides row (separate): 90..103 -> row 9 cols 4..17
        if (z >= 90 && z <= 103){
            return new Integer[]{9, 4 + (z - 90)};
        }

        // Fallback - not placed
        return null;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        readCsv("periodic-table-data.csv");

        // place and show all elements using atomic-number-to-grid mapping
        for (Elements element : elements){
            Integer[] pos = getCoordsForAtomicNumber(element.atomicNumber);
            if (pos == null) continue;
            int row = pos[0];
            int col = pos[1];
            addElement(periodicGrid,
                    element.atomicNumber,
                    col, row,
                    element.cpkColor, element.symbol, element.name);
        }
    }
}