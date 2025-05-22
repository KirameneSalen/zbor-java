module org.example.companiezborclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.companiezborclient to javafx.fxml;
    exports org.example.companiezborclient;
}