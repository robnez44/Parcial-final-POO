package com.example.parcialfinalpoo.viewers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ventanaClienteApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ventanaClienteApplication.class.getResource("/com/example/parcialfinalpoo/viewers/ventanaCliente.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 642, 773);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}