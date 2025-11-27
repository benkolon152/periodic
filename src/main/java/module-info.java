module com.example.periodic {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.periodic to javafx.fxml;
    exports com.example.periodic;
}