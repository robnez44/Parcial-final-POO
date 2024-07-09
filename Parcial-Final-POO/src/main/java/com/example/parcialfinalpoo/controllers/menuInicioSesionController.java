package com.example.parcialfinalpoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class menuInicioSesionController { //00070523 - Controlador para el menu de inicio de sesion
    @FXML
    private Button buttonAdmin; //00070523 - Boton para la opcion administrador (id del boton)
    @FXML
    private Button buttonUser; //00070523 - Boton para la opcion usuario (id del boton)

    //00070523 - Metodo que se ejecuta al presionar el boton de administrador
    @FXML
    private void adminButtonOnAction(ActionEvent event) {
        abrirVentana("/com/example/parcialfinalpoo/viewers/loginAdmin.fxml", "Administrador"); //00070523 - Abre la ventana de inicio de sesion de administrador
    }

    //00070523 - Metodo que se ejecuta al presionar el boton de usuario
    @FXML
    private void userButtonOnAction(ActionEvent event) {
        abrirVentana("/com/example/parcialfinalpoo/viewers/loginCliente.fxml", "Opciones de Usuario"); //00070523 - Abre la ventana de inicio de sesion de usuario
    }

    //00070523 - Metodo para abrir una nueva ventana
    private void abrirVentana(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile)); //00070523 - Cargando el archivo FXML
            Stage st = new Stage(); //00070523 - Creando una nueva ventana (stage)
            st.setTitle(title); //00070523 - Titulo de la ventana
            st.setScene(new Scene(loader.load())); //00070523 - Estableciendo la escena de la ventana con el contenido cargado desde el archivo FXML
            st.show(); //00070523 - Mostrando la ventana
        } catch (IOException e) { //00070523 - Capturando excepciones de entrada/salida
            e.printStackTrace(); //00070523 - Imprimiendo errores
        }
    }
}
