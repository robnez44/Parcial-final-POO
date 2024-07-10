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

public class loginClienteController implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private ImageView lockImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        String brandingImagePath = getClass().getResource("/imagenes/birdegg.branding.jpg").toString();
        Image brandingImage = new Image(brandingImagePath);
        brandingImageView.setImage(brandingImage);

        String lockImagePath = getClass().getResource("/imagenes/birdegg.lock.png").toString();
        Image lockImage = new Image(lockImagePath);
        lockImageView.setImage(lockImage);
    }

    @FXML
    private void crearboton(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parcialfinalpoo/viewers/crearCuenta.fxml"));

        try {
            Parent root = loader.load();
            crearCuentaController controller = loader.getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
            
            Stage loginStage = (Stage) duiTextFile.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private TextField duiTextFile;
    @FXML
    private PasswordField CONTRATEXT;


    @FXML
    private Label loginmessageLabel;

    public void loginButton(ActionEvent event) {

        if (duiTextFile.getText().isBlank() == false && CONTRATEXT.getText().isBlank() == false) {
            validateLogin();
        } else {
            loginmessageLabel.setText("Digite sus credenciales para poder ingresar");
        }
    }

    @FXML
    public void validateLogin() {
        String dui = duiTextFile.getText();
        String password = CONTRATEXT.getText();

        if (dui.isBlank() || password.isBlank()) {
            loginmessageLabel.setText("Digite sus credenciales para poder ingresar");
            return;
        }

        try {
            // Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "root", "Root");
            Connection connection = Database.ConexionBD();

            Statement statement = connection.createStatement();
            String query = "SELECT * FROM Cliente WHERE dui='" + dui + "' AND contrasenia='" + password + "'";
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                Stage loginStage = (Stage) duiTextFile.getScene().getWindow();
                loginStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parcialfinalpoo/viewers/ventanaCliente.fxml"));
                Parent root = loader.load();
                ventanaClienteController controller = loader.getController();

                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(scene);
                stage.show();

            } else {
                loginmessageLabel.setText("Credenciales inválidas. Intenta de nuevo");
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}