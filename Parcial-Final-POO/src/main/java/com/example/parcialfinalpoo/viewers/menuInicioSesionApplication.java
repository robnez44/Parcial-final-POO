package com.example.parcialfinalpoo.viewers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class menuInicioSesionApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(menuInicioSesionApplication.class.getResource("/com/example/parcialfinalpoo/viewers/menuInicioSesion.fxml"));
        Scene scene = new Scene(loader.load(), 865, 642);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}