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
    private ComboBox<String> cbFacilitador;

    @FXML
    private Label lblReporteD;

    @FXML
    private Label lblErrorIDB;

    @FXML
    private Label lblErrorAnoB;

    @FXML
    private Label lblError3B;

    @FXML
    private Button btbCrearReporteB;

    // crear una lista observable para el ComboBox
    ObservableList<String> meses = FXCollections.observableArrayList(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    );

    ObservableList<String> facilitadores = FXCollections.observableArrayList(
            "Visa", "MasterCard", "American Express"
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbMes.setItems(meses);
        cbMes.getSelectionModel().selectFirst(); // selecionar enero por defecto
        cbFacilitador.setItems(facilitadores);
        cbFacilitador.getSelectionModel().selectFirst();
    }

    @FXML
    public void crearReporteB(){
        if (tfIDCliente.getText().isEmpty() && tfAno.getText().isEmpty()) {
            lblError3B.setText("Debe llenar todos los campos");
            tiempoLabel(lblError3B, 5);
        } else if (tfAno.getText().isEmpty()) {
            lblErrorAnoB.setText("Debe ingresar el ano");
            tiempoLabel(lblErrorAnoB, 5);
        } else if (tfIDCliente.getText().isEmpty()) {
            lblErrorIDB.setText("Debe ingresar el ID");
            tiempoLabel(lblErrorIDB, 5);
        } else {
            String idClienteS = tfIDCliente.getText();
            String anoS = tfAno.getText();
            if (!esEntero(idClienteS) && !esEntero(anoS)) {
                lblError3B.setText("Debe ingresar numeros validos");
                tiempoLabel(lblError3B, 5);
            } else if (!esEntero(idClienteS)) {
                lblErrorIDB.setText("Debe ingresar un ID valido");
                tiempoLabel(lblErrorIDB, 5);
            } else if (!esEntero(anoS)) {
                lblErrorAnoB.setText("Debe ingresar un ano valido");
                tiempoLabel(lblErrorAnoB, 5);
            } else {
                int idCliente = Integer.parseInt(tfIDCliente.getText());
                int ano = Integer.parseInt(tfAno.getText());

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345");

                    int mes = cbMes.getSelectionModel().getSelectedIndex() + 1; // Obtener el índice del mes seleccionado

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

                    Writer writer = new FileWriter(file);
                    writer.write("Reporte B - Total gastado por el cliente en " + cbMes.getValue() + " del " + ano + ": ");
                    writer.write(String.valueOf(totalGastado));
                    writer.flush();

                    lblReporteB.setText("Reporte creado correctamente");
                    limpiarB();
                    tiempoLabel(lblReporteB, 2);

                    conn.close();
                } catch (SQLException e) {
                    System.out.println("No se pudo conectar a la bd");
                    e.printStackTrace();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    @FXML
    public void crearReporteD(){
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345");

            String facilitador = cbFacilitador.getValue();

            String query = "SELECT " +
                    "c.id AS cliente_id, " +
                    "c.nombre_completo, " +
                    "tar.facilitador, " +
                    "COUNT(t.id) AS cantidad_compras, " +
                    "SUM(t.monto) AS total_gastado " +
                    "FROM " +
                    "Cliente c " +
                    "JOIN " +
                    "Tarjeta tar ON c.id = tar.cliente_id " +
                    "JOIN " +
                    "Transaccion t ON tar.id = t.tarjeta_id " +
                    "WHERE " +
                    "tar.facilitador = ? " +
                    "GROUP BY " +
                    "c.id, c.nombre_completo, tar.facilitador";

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, facilitador);
            ResultSet rs = pstmt.executeQuery();

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String hora = now.format(formatter);

            String nombreArchivo = "Reporte D - " + hora + " - " + LocalDate.now() + ".txt";
            String rutaArchivo = "Reportes/" + nombreArchivo;
            File file = new File(rutaArchivo);
            Writer writer = new FileWriter(file);

            if (rs.next()) {
                writer.write("Reporte D\n\n" + "id_cliente\tnombre_completo\tfacilitador\tcantidad_compras\ttotal_gastado\n");
                while (rs.next()) {
                    int clienteId = rs.getInt("cliente_id");
                    String nombreCompleto = rs.getString("nombre_completo");
                    String facilitadorConsulta = rs.getString("facilitador");
                    int cantidadCompras = rs.getInt("cantidad_compras");
                    double totalGastado = rs.getDouble("total_gastado");

                    writer.write(clienteId + "\t\t\t" + nombreCompleto + "\t\t\t" + facilitadorConsulta + "\t\t\t" + cantidadCompras + "\t\t\t" + totalGastado + "\n");
                }
            } else {
                writer.write("Reporte D: No existe ningun cliente con el facilitador: " + facilitador);
            }
            writer.flush();

            lblReporteD.setText("Reporte creado correctamente");
            limpiarD();
            tiempoLabel(lblReporteD, 2);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnLimpiarB(){
        limpiarB();
    }

    @FXML
    public void btnLimpiarD(){
        limpiarD();
    }

    private void tiempoLabel(Label label, double tiempo){
        label.setVisible(true);
        Duration duration = Duration.seconds(tiempo);
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> {
            label.setVisible(false);
        }));
        timeline.play();
    }

    private boolean esEntero(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void limpiarB(){
        tfIDCliente.setText("");
        cbMes.getSelectionModel().selectFirst();
        tfAno.setText("");
    }

    private void limpiarD(){
        cbFacilitador.getSelectionModel().selectFirst();
    }
}



