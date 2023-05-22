module com.example.projektjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.slf4j;


    opens controller to javafx.fxml;
    exports controller;

}