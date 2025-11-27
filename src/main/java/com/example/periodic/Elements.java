package com.example.periodic;

import javafx.scene.paint.Color;

public class Elements {
    public int atomicNumber;
    public String symbol;
    public String name;
    public String atomicMass;
    public String cpkHexColor;
    /*
    electronicConfiguration,
    electronegativity,
    atomicRadius,
    ionRadius,
    vanDelWaalsRadius,
    ionizationEnergy,
    electronAffinity,
    oxidationStates,
    standardState,
    bondingType,
    meltingPoint,
    boilingPoint,
    density,
    groupBlock,
    yearDiscovered */

    public Elements(String csvLine){
        String[] split = csvLine.split(",");
        //System.out.println(split);
        atomicNumber = Integer.parseInt(split[0]);
        symbol = split[1].trim();
        name = split[2].trim();
        atomicMass = split[3].trim();
        cpkHexColor = split[4];
        Color cpkColor = Color.TRANSPARENT;
        if (cpkHexColor.length() == 6)
             cpkColor = Color.web("#"+cpkHexColor);
    }
}
