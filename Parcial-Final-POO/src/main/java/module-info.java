module com.example.parcialfinalpoo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    exports com.example.parcialfinalpoo;
    opens com.example.parcialfinalpoo to javafx.fxml;
    exports com.example.parcialfinalpoo.controllers;
    opens com.example.parcialfinalpoo.controllers to javafx.fxml;
    exports com.example.parcialfinalpoo.viewers;
    opens com.example.parcialfinalpoo.viewers to javafx.fxml;
}