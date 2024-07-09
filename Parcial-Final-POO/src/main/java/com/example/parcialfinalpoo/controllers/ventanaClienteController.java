package com.example.parcialfinalpoo.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import javax.swing.text.LabelView;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;

public class ventanaClienteController implements Initializable {
    @FXML
    private ComboBox<String> cbTipoTarjeta;

    @FXML
    private ComboBox<String> cbFacilitador;

    @FXML
    private TextField tfDUI;

    @FXML
    private Label lblT1;

    @FXML
    private Label lblT2;

    @FXML
    private Label lblErrorTar;

    ObservableList<String> tipoTarjeta = FXCollections.observableArrayList("Debito", "Credito");
    ObservableList<String> facilitador = FXCollections.observableArrayList("Visa", "MasterCard", "American Express");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbTipoTarjeta.setItems(tipoTarjeta);
        cbTipoTarjeta.getSelectionModel().selectFirst();
        cbFacilitador.setItems(facilitador);
        cbFacilitador.getSelectionModel().selectFirst();
    }

    @FXML
    private void btnAsignarTarjeta(){
        if (!tfDUI.getText().isEmpty()) {
            int idCliente = obtenerIDCliente();
            if (idCliente != -1){
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345");

                    String nTar = generarNumero();
                    String fech = generarFecha();
                    String tipo = cbTipoTarjeta.getValue();
                    String fac = cbFacilitador.getValue();

                    String insertQuery = "INSERT INTO Tarjeta (numero, fecha_expiracion, tipo, facilitador, cliente_id) " +
                            "VALUES (?, ?, ?, ?, ?)";

                    PreparedStatement ps = conn.prepareStatement(insertQuery);
                    ps.setString(1, nTar);
                    ps.setString(2, fech);
                    ps.setString(3, tipo);
                    ps.setString(4, fac);
                    ps.setInt(5, idCliente);
                    ps.executeUpdate();

                    lblT1.setText("Tu tarjeta se te fue asignada correctamente.");
                    lblT2.setText("Revisa el SMS en tu telefono para mayor informacion");
                    tiempoLabel(lblT1);
                    tiempoLabel(lblT2);
                    limpiparTar();

                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                lblErrorTar.setText("El DUI ingresado no existe. Intente de nuevo");
            }
        } else {
            lblErrorTar.setText("Debe ingresar su DUI!");
        }
    }

    @FXML
    private void btnLimpiarTarjeta(){
        limpiparTar();
    }

    private int obtenerIDCliente() {
        String DUI = tfDUI.getText();
        String query = "SELECT id FROM Cliente WHERE DUI = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345");
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, DUI);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return -1;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR AL OBTENER EL ID DEL CLIENTE", e);
        }
    }

    private String generarNumero(){
        Random random = new Random();

        StringBuilder numeroTarjeta = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            int digito = random.nextInt(10);
            numeroTarjeta.append(digito);
        }

        return numeroTarjeta.toString();
    }

    private String generarFecha(){
        Random random = new Random();

        String mes = "";
        int numero = (random.nextInt(12) + 1);

        if (numero < 10){
            mes = "0" + numero;
        } else {
            mes = String.valueOf(numero);
        }

        int ano = random.nextInt(31) + 25;

        return mes + "/" + ano;
    }

    private void limpiparTar(){
        tfDUI.setText("");
        cbTipoTarjeta.getSelectionModel().selectFirst();
        cbFacilitador.getSelectionModel().selectFirst();
        lblErrorTar.setText("");
    }

    private void tiempoLabel(Label label){
        label.setVisible(true);
        Duration duration = Duration.seconds(2);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            label.setVisible(false);
        }));
        timeline.play();
    }
}