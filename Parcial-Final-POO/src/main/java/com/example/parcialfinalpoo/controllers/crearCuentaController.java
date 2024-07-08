package com.example.parcialfinalpoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.sql.*;
import com.example.parcialfinalpoo.DB.Database; //00070523 - Importacion de la clase Database para trabajar con la base de datos creada en el package DB
import javafx.stage.Stage;

public class crearCuentaController { //00070523 - Controlador para la creacion de cuentas de los Clientes
    @FXML
    private Button btCrearCuenta; //00070523 - Boton para crear cuenta
    @FXML
    private Button btInicioSesion; //00070523 - Boton que manda a la ventana de iniciar sesion
    @FXML
    private TextArea txtNombre; //00070523 - Campo de texto para el nombre
    @FXML
    private TextArea txtDui; //00070523 - Campo de texto para el DUI
    @FXML
    private TextArea txtUbi; //00070523 - Campo de texto para la direccion
    @FXML
    private TextArea txtTelefono; //00070523 - Campo de texto para el telefono
    @FXML
    private PasswordField txtContra; //00070523 - Campo de texto para la contra

    //00070523 - Metodo que se ejecuta al inicializar el controlador
    @FXML
    private void initialize() {
        btCrearCuenta.setOnAction(event -> { //00070523 - Accion que realiza el boton Crear Cuenta
            String nombre = txtNombre.getText(); //00070523 - Obteniendo texto del campo de nombre
            String dui = txtDui.getText(); //00070523 - Obteniendo texto del campo de DUI
            String direccion = txtUbi.getText(); //00070523 - Obteniendo texto del campo de direccion
            String telefono = txtTelefono.getText(); //00070523 - Obteniendo texto del campo de telefono
            String contrasenia = txtContra.getText(); //00070523 - Obteniendo texto del campo de contra

            try {
                guardarEnTablaCliente(nombre, dui, direccion, telefono, contrasenia); //00070523 - Se llama el metodo para los datos de los clientes
            } catch (SQLException e) { //00070523 - Capturando posibles excepciones
                e.printStackTrace(); //00070523 - Imprimiendo errores
            }
        });
    }

    //00070523 - Metodo para guardar datos en la tabla Cliente
    private void guardarEnTablaCliente(String nombre, String dui, String direccion, String telefono, String contrasenia) throws SQLException {
        String insert = "INSERT INTO Cliente (nombre_completo, DUI, direccion, telefono, contrasenia) VALUES (?, ?, ?, ?, ?)"; //00070523 -  Consulta para insertar datos

        try (Connection conn = Database.ConexionBD(); //00070523 - Conectandose a la base de datos para hacer los inserts
             PreparedStatement pstmt = conn.prepareStatement(insert)) { //00070523 - Preparando la consulta

            pstmt.setString(1, nombre); //00070523 - Asignando el nombre al primer parametro
            pstmt.setString(2, dui); //00070523 - Asignando el DUI al segundo parametro
            pstmt.setString(3, direccion); //00070523 - Asignando la direccion al tercer parametro
            pstmt.setString(4, telefono); //00070523 - Asignando el telefono al cuarto parametro
            pstmt.setString(5, contrasenia); //00070523 - Asignando la contra al quinto parametro

            pstmt.executeUpdate(); //00070523 - Ejecutando la consulta
        }
    }

    //00070523 - Metodo en donde se realiza la accion de Iniciar Sesion
    @FXML
    private void inicioSesionButtonOnAction(ActionEvent event) {
        abrirVentana("/com/example/parcialfinalpoo/viewers/loginCliente.fxml", "Opciones de Usuario"); //00070523 - Abre la ventana de opciones de usuario
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
