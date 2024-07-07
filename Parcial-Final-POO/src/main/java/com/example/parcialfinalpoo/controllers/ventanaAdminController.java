package com.example.parcialfinalpoo.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ventanaAdminController implements Initializable {
    @FXML
    private TextField tfIDCliente;

    @FXML
    private ComboBox<String> cbMes;

    @FXML
    private TextField tfAno;

    @FXML
    private Label lblReporteB;

    @FXML
    private Button btbCrearReporteB;

    // crear una lista observable para el ComboBox
    ObservableList<String> meses = FXCollections.observableArrayList(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbMes.setItems(meses);
        cbMes.getSelectionModel().selectFirst(); // selecionar enero por defecto
    }

    @FXML
    public void crearReporteB(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345");

            int idCliente = Integer.parseInt(tfIDCliente.getText());
            int mes = cbMes.getSelectionModel().getSelectedIndex() + 1; // Obtener el índice del mes seleccionado
            int ano = Integer.parseInt(tfAno.getText());

            // Consulta con PreparedStatement
            String query = "SELECT SUM(t.monto) AS total_gastado " +
                    "FROM Transaccion t " +
                    "JOIN Tarjeta tar ON t.tarjeta_id = tar.id " +
                    "JOIN Cliente c ON tar.cliente_id = c.id " +
                    "WHERE c.id = ? AND MONTH(t.fecha) = ? AND YEAR(t.fecha) = ?";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, idCliente); // ID del cliente
            pstmt.setInt(2, mes);    // Mes
            pstmt.setInt(3, ano);    // Año

            ResultSet rs = pstmt.executeQuery();
            double totalGastado = 0;

            if (rs.next()) {
                totalGastado = rs.getDouble("total_gastado");
            }

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String hora = now.format(formatter);

            String nombreArchivo = "Reporte B - " + hora + " - " + LocalDate.now() + ".txt";
            String rutaArchivo = "Reportes/" + nombreArchivo;
            File file = new File(rutaArchivo);

            // Escribir el archivo
            Writer writer = new FileWriter(file);
            writer.write("Reporte B - Total gastado por el cliente en " + cbMes.getValue() + " del " + ano + ": ");
            writer.write(String.valueOf(totalGastado));
            writer.flush();

            lblReporteB.setText("Reporte creado correctamente");
            limpiarB();
            tiempoLabel(lblReporteB);

            conn.close();
        } catch (SQLException e) {
            System.out.println("No se pudo conectar a la bd");
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnLimpiar(){
        limpiarB();
    }

    private void tiempoLabel(Label label){
        label.setVisible(true);
        Duration duration = Duration.seconds(2);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            label.setVisible(false);
        }));
        timeline.play();
    }

    private void limpiarB(){
        tfIDCliente.setText("");
        cbMes.getSelectionModel().selectFirst();
        tfAno.setText("");
    }
}