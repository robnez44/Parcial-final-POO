package com.example.parcialfinalpoo.controllers;

import com.example.parcialfinalpoo.DB.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    @FXML
    private Label lblCuentaCreada; //00070523 - Label que aparece cuando se crea la cuenta
    private MediaPlayer mediaPlayer; // 00070523 - Se declara una variable mediaPlayer de tipo MediaPlayer
    private Media mediaError; // 00070523 - Se declara una variable mediaError de tipo Media

    //00070523 - Metodo que se ejecuta al inicializar el controlador
    @FXML
    private void initialize() {
        lblCuentaCreada.setVisible(false); //00070523 - Texto de cuenta creada exitosamente inicia oculto

        //00070523 - Ruta del archivo de sonido debe ser correcta
        try {
            mediaError = new Media(getClass().getResource("com/example/parcialfinalpoo/viewers/sonido/error.mp3").toExternalForm());
            mediaPlayer = new MediaPlayer(mediaError);
        } catch (Exception e) {
            e.printStackTrace();
        }

        btCrearCuenta.setOnAction(event -> { //00070523 - Accion que realiza el boton Crear Cuenta
            String nombre = txtNombre.getText(); //00070523 - Obteniendo texto del campo de nombre
            String dui = txtDui.getText(); //00070523 - Obteniendo texto del campo de DUI
            String direccion = txtUbi.getText(); //00070523 - Obteniendo texto del campo de direccion
            String telefono = txtTelefono.getText(); //00070523 - Obteniendo texto del campo de telefono
            String contrasenia = txtContra.getText(); //00070523 - Obteniendo texto del campo de contra

            if (nombre.isEmpty() || dui.isEmpty() || direccion.isEmpty() || telefono.isEmpty() || contrasenia.isEmpty()) { //00070523 - Si la condicion se cumple se lanzara la alerta si un campo es vacio
                mostrarError("Debe llenar todos los campos"); // 00070523 - Mensaje de alerta
            } else {
                try {
                    guardarEnTablaCliente(nombre, dui, direccion, telefono, contrasenia); //00070523 - Se llama el metodo para los datos de los clientes
                    lblCuentaCreada.setVisible(true); //00070523 - Mostrar el el texto cuenta creada cuanto se haya presionado el boton de crear cuenta
                } catch (SQLException e) { //00070523 - Capturando posibles excepciones
                    e.printStackTrace(); //00070523 - Imprimiendo errores
                }
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

    //00070523 - Metodo para mostrar un mensaje de error
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error"); // 00070523 - Titulo de error de la alerta
        alert.setHeaderText(null); // 00070523 - encabezado de la alerta como nulo
        alert.setContentText(mensaje); // el mensaje de la alerta es pasado como parametro
        alert.showAndWait(); // 00070523 - Muestra la alerta y espera a que el usuario la cierre

        // Reproducir sonido de error
        if (mediaPlayer != null) {
            mediaPlayer.play(); //00070523 - Inicia la reproduccion del MediaPlayer desde el principio
        }
    }
}
