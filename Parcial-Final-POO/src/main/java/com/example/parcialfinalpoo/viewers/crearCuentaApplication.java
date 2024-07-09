package com.example.parcialfinalpoo.viewers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class crearCuentaApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(crearCuentaApplication.class.getResource("/com/example/parcialfinalpoo/viewers/crearCuenta.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 906, 772);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}