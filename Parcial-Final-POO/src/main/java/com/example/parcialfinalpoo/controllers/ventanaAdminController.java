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
    private ComboBox<String> cbMes; // 00044623 se crea una variable de tipo ComboBox para seleccionar el mes

    @FXML
    private TextField tfAno;

    @FXML
    private Label lblReporteB;

    @FXML
    private ComboBox<String> cbFacilitador; // 00044623 se crea una variable de tipo ComboBox para seleccionar el facilitador

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
    ); // 00044623 se crea la lista de meses

    ObservableList<String> facilitadores = FXCollections.observableArrayList(
            "Visa", "MasterCard", "American Express"
    ); // 00044623 se crea la lista de facilitadores

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbMes.setItems(meses); // 00044623 se asignan los meses al ComboBox
        cbMes.getSelectionModel().selectFirst(); // 00044623 selecciona el primer elemento por defecto
        cbFacilitador.setItems(facilitadores); // 00044623 se asignan los facilitadores al ComboBox
        cbFacilitador.getSelectionModel().selectFirst(); // 00044623 selecciona el primer elemento por defecto
    }

    @FXML
    public void crearReporteB(){ // 00044623 metodo para crear reporte B
        if (tfIDCliente.getText().isEmpty() && tfAno.getText().isEmpty()) { // 00044623 verifica si ambos campos están vacíos
            lblError3B.setText("Debe llenar todos los campos"); // 00044623 muestra un mensaje de error por si lo estan
            tiempoLabel(lblError3B, 5); // 00044623 muestra el mensaje de error por 5 segundos
        } else if (tfAno.getText().isEmpty()) { // 00044623 verifica si solo el campo del año está vacío
            lblErrorAnoB.setText("Debe ingresar el ano"); // 00044623 muestra un mensaje de error por si lo esta
            tiempoLabel(lblErrorAnoB, 5); // 00044623 muestra el mensaje por 5 segundos
        } else if (tfIDCliente.getText().isEmpty()) { // 00044623 verifica si solo el campo del ID está vacío
            lblErrorIDB.setText("Debe ingresar el ID"); // 00044623 muestra un mensaje de error por si lo esta
            tiempoLabel(lblErrorIDB, 5); // 00044623 muestra el mensaje por 5 segundos
        } else {
            String idClienteS = tfIDCliente.getText(); // 00044623 convierte el texto del ID a String
            String anoS = tfAno.getText(); // 00044623 convierte el texto del año a String
            if (!esEntero(idClienteS) && !esEntero(anoS)) { // 00044623 verifica que ambos campos sean números
                lblError3B.setText("Debe ingresar numeros validos"); // 00044623 muestra un mensaje de error por si no lo son
                tiempoLabel(lblError3B, 5); // 00044623 muestra el mensaje por 5 segundos
            } else if (!esEntero(idClienteS)) { // 00044623 verifica si el ID no es un número
                lblErrorIDB.setText("Debe ingresar un ID valido"); // 00044623 muestra un mensaje de error por si no lo es
                tiempoLabel(lblErrorIDB, 5); // 00044623 muestra el mensaje por 5 segundos
            } else if (!esEntero(anoS)) { // 00044623 verifica si el año no es un número
                lblErrorAnoB.setText("Debe ingresar un ano valido"); // 00044623 muestra un mensaje de error por si no lo es
                tiempoLabel(lblErrorAnoB, 5); // 00044623 muestra el mensaje por 5 segundos
            } else {
                int idCliente = Integer.parseInt(tfIDCliente.getText()); // 00044623 convierte el ID a entero
                int ano = Integer.parseInt(tfAno.getText()); // 00044623 convierte el año a entero

                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345"); // 00044623 se conecta a la base de datos

                    int mes = cbMes.getSelectionModel().getSelectedIndex() + 1; // 00044623 se obtiene el mes a traves del indice
                    String query = "SELECT SUM(t.monto) AS total_gastado " + // 00044623 se define consulta SQL para obtener el total gastado
                            "FROM Transaccion t " +
                            "JOIN Tarjeta tar ON t.tarjeta_id = tar.id " +
                            "JOIN Cliente c ON tar.cliente_id = c.id " +
                            "WHERE c.id = ? AND MONTH(t.fecha) = ? AND YEAR(t.fecha) = ?";

                    PreparedStatement pstmt = conn.prepareStatement(query); // 00044623 prepara la consulta SQL
                    pstmt.setInt(1, idCliente); // 00044623 establece el ID del cliente en la consulta
                    pstmt.setInt(2, mes);    // 00044623 establece el mes en la consulta
                    pstmt.setInt(3, ano);    // 00044623 establece el año en la consulta

                    ResultSet rs = pstmt.executeQuery(); // 00044623 ejecuta la consulta
                    double totalGastado = 0; // 00044623 establece por defecto el total gastado a 0

                    if (rs.next()) { // 00044623 verifica si hay resultados en la consulta
                        totalGastado = rs.getDouble("total_gastado"); // 00044623 obtiene el total gastado
                    }

                    LocalDateTime now = LocalDateTime.now(); // crea un objeto LocalDateTime con la hora actual
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // formatea la hora a un formato de hh:mm:ss
                    String hora = now.format(formatter); // 00044623 obtiene la hora actual

                    String nombreArchivo = "Reporte B - " + hora + " - " + LocalDate.now() + ".txt"; // 00044623 define el nombre del archivo
                    String rutaArchivo = "Reportes/" + nombreArchivo; // 00044623 define la ruta del archivo
                    File file = new File(rutaArchivo); // 00044623 crea un archivo con la ruta especificada

                    Writer writer = new FileWriter(file); // 00044623 crea un escritor para el archivo
                    writer.write("Reporte B - Total gastado por el cliente en " + cbMes.getValue() + " del " + ano + ": "); // 00044623 escribe en el archivo
                    writer.write(String.valueOf(totalGastado)); // 00044623 escribe el total gastado en el archivo
                    writer.flush(); // 00044623 guarda los cambios en el archivo

                    lblReporteB.setText("Reporte creado correctamente"); // 00044623 muestra un mensaje de exito
                    limpiarB(); // 00044623 limpia los campos de entrada
                    tiempoLabel(lblReporteB, 2); // 00044623 muestra el mensaje por 2 segundos

                    conn.close(); // 00044623 cierra la conexión a la base de datos
                } catch (SQLException e) { // 00044623 se manejan las posibles excepciones SQL
                    System.out.println("No se pudo conectar a la bd"); // 00044623 imprime mensaje de error
                    e.printStackTrace(); // 00044623 imprime la traza de la excepción
                } catch (IOException e) { // 00044623 maneja excepciones de entrada/salida
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @FXML
    public void crearReporteD(){ // 00044623 metodo para crear reporte D
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345"); // 00044623 conecta a la base de datos

            String facilitador = cbFacilitador.getValue(); // 00044623 obtiene el facilitador seleccionado

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
                    "c.id, c.nombre_completo, tar.facilitador"; // se define la consulta SQL para obtener los clientes que han realizado compras con cierto facilitador

            PreparedStatement pstmt = conn.prepareStatement(query); // 00044623 prepara la consulta SQL
            pstmt.setString(1, facilitador); // 00044623 establece el facilitador en la consulta
            ResultSet rs = pstmt.executeQuery(); // 00044623 ejecuta la consulta

            LocalDateTime now = LocalDateTime.now(); // crea un objeto LocalDateTime con la hora actual
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss"); // formatea la hora a un formato de hh:mm:ss
            String hora = now.format(formatter); // 00044623 obtiene la hora actual

            String nombreArchivo = "Reporte D - " + hora + " - " + LocalDate.now() + ".txt"; // 00044623 define el nombre del archivo
            String rutaArchivo = "Reportes/" + nombreArchivo; // 00044623 define la ruta del archivo
            File file = new File(rutaArchivo); // 00044623 crea un archivo con la ruta especificada
            Writer writer = new FileWriter(file); // 00044623 crea un escritor para el archivo

            if (rs.next()) { // 00044623 verifica si hay resultados en la consulta
                writer.write("Reporte D\n\n" + "id_cliente\tnombre_completo\tfacilitador\tcantidad_compras\ttotal_gastado\n");
                while (rs.next()) { // 00044623 itera sobre los resultados de la consulta
                    int clienteId = rs.getInt("cliente_id"); // 00044623 obtiene el ID del cliente
                    String nombreCompleto = rs.getString("nombre_completo"); // 00044623 obtiene el nombre completo del cliente
                    String facilitadorConsulta = rs.getString("facilitador"); // 00044623 obtiene el facilitador
                    int cantidadCompras = rs.getInt("cantidad_compras"); // 00044623 obtiene la cantidad de compras
                    double totalGastado = rs.getDouble("total_gastado"); // 00044623 obtiene el total gastado

                    writer.write(clienteId + "\t\t\t" + nombreCompleto + "\t\t\t" + facilitadorConsulta + "\t\t\t" + cantidadCompras + "\t\t\t" + totalGastado + "\n"); // 00044623 escribe en el archivo
                }
            } else {
                writer.write("Reporte D: No existe ningun cliente con el facilitador: " + facilitador); // 00044623 escribe en el archivo
            }
            writer.flush(); // 00044623 guarda los cambios en el archivo

            lblReporteD.setText("Reporte creado correctamente"); // 00044623 muestra un mensaje de éxito en lblReporteD
            limpiarD(); // 00044623 limpia los campos de entrada
            tiempoLabel(lblReporteD, 2); // 00044623 muestra lblReporteD por 2 segundos

            conn.close(); // 00044623 cierra la conexión a la base de datos
        } catch (SQLException e) { // 00044623 maneja excepciones SQL
            e.printStackTrace(); // 00044623 imprime la traza de la excepción
        } catch (IOException e) { // 00044623 maneja excepciones de entrada/salida
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void btnLimpiarB(){ // 00044623 método para limpiar campos de reporte B
        limpiarB(); // 00044623 llama al método limpiarB
    }

    @FXML
    public void btnLimpiarD(){ // 00044623 método para limpiar campos de reporte D
        limpiarD(); // 00044623 llama al método limpiarD
    }

    private void tiempoLabel(Label label, double tiempo){ // 00044623 método para mostrar una etiqueta por cierto tiempo
        label.setVisible(true); // 00044623 hace visible la etiqueta por si acaso estaba invisible
        Duration duration = Duration.seconds(tiempo); // 00044623 establece la duración del tiempo
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> { // 00044623 crea una línea de tiempo
            label.setVisible(false); // 00044623 oculta la etiqueta después del tiempo especificado
        }));
        timeline.play(); // 00044623 inicia la línea de tiempo
    }

    private boolean esEntero(String cadena) { // 00044623 método para verificar si una cadena es un entero válido
        try {
            Integer.parseInt(cadena); // 00044623 intenta convertir la cadena a entero
            return true; // 00044623 retorna verdadero si tiene éxito
        } catch (NumberFormatException e) {
            return false; // 00044623 retorna falso si ocurre una excepción
        }
    }

    private void limpiarB(){ // 00044623 método para limpiar campos del reporte B
        tfIDCliente.setText(""); // 00044623 limpia el campo de ID del cliente
        cbMes.getSelectionModel().selectFirst(); // 00044623 selecciona "Enero" en ComboBox
        tfAno.setText(""); // 00044623 limpia el campo del año
    }

    private void limpiarD(){ // 00044623 método para limpiar campos del reporte D
        cbFacilitador.getSelectionModel().selectFirst(); // 00044623 selecciona "Visa" en ComboBox
    }
}