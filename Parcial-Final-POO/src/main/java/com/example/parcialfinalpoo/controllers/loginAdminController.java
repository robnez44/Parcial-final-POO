package com.example.parcialfinalpoo.controllers;

import com.example.parcialfinalpoo.DB.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.sql.Connection;

public class loginAdminController implements Initializable {

    @FXML
    private ImageView brandingImageView; // Declaracion del ImageView para la imagen de la marca,00038723

    @FXML
    private ImageView lockImageView; // Declaracion del ImageView para la imagen del candado,00038723

    @Override
    public void initialize(URL location, ResourceBundle resources) { // Metodo para inicializar el controlador,00038723

        String brandingImagePath = getClass().getResource("/imagenes/birdegg.branding.jpg").toString(); // Ruta de la imagen de la marca,00038723
        Image brandingImage = new Image(brandingImagePath); // Crea la imagen de la marca,00038723
        brandingImageView.setImage(brandingImage); // Configura la imagen de la marca eb el ImageView, 00038723

        String lockImagePath = getClass().getResource("/imagenes/birdegg.lock.png").toString(); // Ruta de la imagen del candado,00038723
        Image lockImage = new Image(lockImagePath); // Crea la imagen del candado,00038723
        lockImageView.setImage(lockImage); // Configura la imagen del candado en el ImageView,00038723
    }

    @FXML
    private void volverboton(ActionEvent event) { // Metodo para manejar el evento del boton "volver",00038723
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parcialfinalpoo/viewers/menuInicioSesion.fxml")); // Carga la vista "menuInicioSesion",00038723

        try {
            Parent root = loader.load(); // Carga la interfaz grafica desde el archivo FXML,00038723
            menuInicioSesionController controller = loader.getController(); // Obtiene el controlador de la vista,00038723

            Scene scene = new Scene(root); // Crea una nueva escena con la interfaz cargada,00038723
            Stage stage = new Stage(); // Crea un nuevo escenario (ventana),00038723
            stage.initModality(Modality.APPLICATION_MODAL); // Configura la modalidad de la ventana,00038723
            stage.setScene(scene); // Establece la escena en el escenario,00038723
            stage.show(); // Muestra la bueva ventana,00038723

            Stage loginStage = (Stage) usernameTextFile.getScene().getWindow(); // Obtiene  la ventana actual (login),00038723
            loginStage.close(); // Cierra  la ventana actual,00038723

        } catch (IOException e) {
            throw new RuntimeException(e); // Maneja posibles errores de entrada/salida,00038723
        }
    }

    @FXML
    private TextField usernameTextFile; // Declaracion del campo de texto para el nombre de usuario,00038723

    @FXML
    private PasswordField CONTRATEXT; // Declaracion del campo de texto para la contraseña,00038723

    @FXML
    private Label loginmessageLabel; // Declaracion de la etiqueta para mensajes de login,00038723

    public void loginButton(ActionEvent event) { // Metodo para manejar el evento del boton de login,00038723

        if (usernameTextFile.getText().isBlank() == false && CONTRATEXT.getText().isBlank() == false) { // Verifica si los campos no estan vacios,00038723
            validateLogin(); // Llama al metodo para validar el login,00038723
        } else {
            loginmessageLabel.setText("Digite sus credenciales para poder ingresar"); // Muestra mensaje de error si los campos estan vacios,00038723
        }
    }

    @FXML
    public void validateLogin() { // Metodo para validar las credenciales de login,00038723
        String username = usernameTextFile.getText(); // Obtiene el texto del campo de nombre de usuario,00038723
        String password = CONTRATEXT.getText(); // Obtiene el texto del campo de contrasena,00038723

        if (username.isBlank() || password.isBlank()) { // Verifica si los campos estan vacios,00038723
            loginmessageLabel.setText("Digite sus credenciales para poder ingresar"); // Muestra mensaje de error si los campos estan vacios,00038723
            return; // Sale del metodo si los campos estan vacios,00038723
        }

        try {
            Connection connection = Database.ConexionBD(); // Establece conexion con la base de datos,00038723
            Statement statement = connection.createStatement(); // Crea una declaracion SQL,00038723
            String query = "SELECT * FROM Admin WHERE username='" + username + "' AND password='" + password + "'"; // Consulta SQL para validar el usuario y contaseña, 00038723
            ResultSet resultSet = statement.executeQuery(query); // Ejecuta la consulta y obtiene el resultado,00038723

            if (resultSet.next()) { // Verifica si hay resultados en la consulta,00038723
                Stage loginStage = (Stage) usernameTextFile.getScene().getWindow(); // Obtiene la ventana actual (login),00038723
                loginStage.close(); // Cierra la ventana actual,00038723

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parcialfinalpoo/viewers/ventanaAdmin.fxml")); // Carga la vista "ventanaAdmin",00038723
                Parent root = loader.load(); // Carga la interfaz grafica desde el archivo FXML,00038723
                ventanaAdminController controller = loader.getController(); // Obtiene el controlador de la vista,00038723

                Scene scene = new Scene(root); // Crea una nueva escena con la interfaz cargada,00038723
                Stage stage = new Stage(); // Crea un nuevo escenario (ventana),00038723
                stage.initModality(Modality.APPLICATION_MODAL); // Configura la modalidad de la ventana,00038723
                stage.setScene(scene); // Establece la escena en el escenario,00038723
                stage.show(); // Muestra la nueva ventana,00038723

            } else {
                loginmessageLabel.setText("Credenciales invalidas. Intenta de nuevo"); // Muestra mensaje de error si las credenciales son invalidas,00038723
            }
            connection.close(); // Cierra la conexion con la base de datos,00038723
        } catch (Exception e) {
            e.printStackTrace(); // Maneja posibles excepciones y errores,00038723
        }
    }
}