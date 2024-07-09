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

import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;

public class ventanaClienteController implements Initializable {
    @FXML
    private ComboBox<String> cbTipoTarjeta; // 00044623 se crea una variable de tipo ComboBox para el tipo de tarjeta

    @FXML
    private ComboBox<String> cbFacilitador; // 00044623 se crea una variable de tipo ComboBox para el tipo de facilitador

    @FXML
    private TextField tfDUI;

    @FXML
    private Label lblT1;

    @FXML
    private Label lblT2;

    @FXML
    private Label lblErrorTar;

    ObservableList<String> tipoTarjeta = FXCollections.observableArrayList("Debito", "Credito"); // 00044623 se agregan los tipos de tarjeta en un ObservableList
    ObservableList<String> facilitador = FXCollections.observableArrayList("Visa", "MasterCard", "American Express"); // 00044623 se agregan los tipos de facilitador en un ObservableList

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbTipoTarjeta.setItems(tipoTarjeta); // 00044623 se asignan los tipos de tarjeta en el ComboBox
        cbTipoTarjeta.getSelectionModel().selectFirst(); // 00044623 selecciona el primer elemento por defecto
        cbFacilitador.setItems(facilitador); // 00044623 se asignan los tipos de facilitador en el ComboBox
        cbFacilitador.getSelectionModel().selectFirst(); // 00044623 selecciona el primer elemento por defecto
    }

    @FXML
    private void btnAsignarTarjeta(){ // metodo para asignar una tarjeta a un cliente
        if (!tfDUI.getText().isEmpty()) { // 00044623 verifica si el campo tfDUI no está vacío
            int idCliente = obtenerIDCliente(); // 00044623 obtiene el ID del cliente

            if (idCliente != -1){ // 00044623 verifica si se encontró un ID válido
                try {
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345"); // 00044623 establece la conexión a la base de datos

                    String nTar = generarNumero(); // 00044623 genera un número de tarjeta aleatorio
                    String fech = generarFecha(); // 00044623 genera una fecha de expiración aleatoria
                    String tipo = cbTipoTarjeta.getValue(); // 00044623 obtiene el tipo de tarjeta seleccionado
                    String fac = cbFacilitador.getValue(); // 00044623 obtiene el facilitador seleccionado

                    String insertQuery = "INSERT INTO Tarjeta (numero, fecha_expiracion, tipo, facilitador, cliente_id) " +
                            "VALUES (?, ?, ?, ?, ?)"; // 00044623 define la consulta SQL

                    PreparedStatement ps = conn.prepareStatement(insertQuery); // 00044623 prepara la declaración SQL
                    ps.setString(1, nTar); // 00044623 establece el número de tarjeta en la consulta
                    ps.setString(2, fech); // 00044623 establece la fecha de expiración en la consulta
                    ps.setString(3, tipo); // 00044623 establece el tipo de tarjeta en la consulta
                    ps.setString(4, fac); // 00044623 establece el facilitador en la consulta
                    ps.setInt(5, idCliente); // 00044623 establece el ID del cliente en la consulta
                    ps.executeUpdate(); // 00044623 ejecuta la inserción en la base de datos

                    lblT1.setText("Tu tarjeta se te asignó correctamente."); // 00044623 establece el texto simulando una operación de un banco real
                    lblT2.setText("Revisa el SMS en tu teléfono para mayor información"); // 00044623 establece el texto simulando una operación de un banco real
                    tiempoLabel(lblT1); // 00044623 llama al método para ocultar lblT1 después de un tiempo
                    tiempoLabel(lblT2); // 00044623 llama al método para ocultar lblT2 después de un tiempo
                    limpiparTar(); // 00044623 limpia los campos de entrada y reinicia el ComboBox

                    conn.close(); // 00044623 cierra la conexión a la base de datos

                } catch (SQLException e) { // 00044623 maneja cualquier excepción SQL lanzando una RuntimeException
                    throw new RuntimeException(e);
                }
            } else {
                lblErrorTar.setText("El DUI ingresado no existe. Intente de nuevo"); // 00044623 establece el mensaje de error por si no se encontró el DUI
            }
        } else {
            lblErrorTar.setText("Debe ingresar su DUI!"); // 00044623 establece el mensaje de error por si el campo está vacío
        }
    }

    @FXML
    private void btnLimpiarTarjeta(){ // 00044623 método que se activa al tocar el botón limpiar
        limpiparTar(); // 00044623 llama al método para limpiar los campos de entrada
    }

    private int obtenerIDCliente() { // 00044623 método para obtener el ID de un cliente
        String DUI = tfDUI.getText(); // 00044623 obtiene el valor del campo tfDUI
        String query = "SELECT id FROM Cliente WHERE DUI = ?"; // 00044623 define la consulta SQL para obtener el ID del cliente

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "rober", "12345"); // 00044623 establece la conexión a la base de datos
             PreparedStatement ps = conn.prepareStatement(query)) { // 00044623 prepara la declaración SQL

            ps.setString(1, DUI); // 00044623 establece el valor de DUI en la consulta preparada

            try (ResultSet rs = ps.executeQuery()) { // 00044623 ejecuta la consulta y obtiene el resultado
                if (rs.next()) { // 00044623 verifica si hay resultados en el conjunto de resultados
                    return rs.getInt(1); // 00044623 retorna el ID del cliente encontrado
                } else {
                    return -1; // 00044623 retorna -1 si no se encontró ningún cliente con el DUI especificado
                }
            }
        } catch (SQLException e) { // 00044623 maneja cualquier excepción SQL lanzando una RuntimeException con mensaje personalizado
            throw new RuntimeException("ERROR AL OBTENER EL ID DEL CLIENTE", e);
        }
    }

    private String generarNumero(){ // 00044623 método para generar un número de tarjeta aleatorio
        Random random = new Random(); // 00044623 crea un objeto Random para generar números aleatorios

        StringBuilder numeroTarjeta = new StringBuilder(); // 00044623 crea un StringBuilder para construir el número de tarjeta

        for (int i = 0; i < 16; i++) { // 00044623 genera cada dígito del número de tarjeta
            int digito = random.nextInt(10); // 00044623 genera un dígito aleatorio entre 0 y 9
            numeroTarjeta.append(digito); // 00044623 agrega el dígito al StringBuilder
        }

        return numeroTarjeta.toString(); // 00044623 devuelve el número de tarjeta generado como cadena
    }

    private String generarFecha(){ // 00044623 método para generar una fecha aleatoria
        Random random = new Random(); // 00044623 crea un objeto Random para generar números aleatorios

        String mes = ""; // 00044623 inicializa la variable para el mes
        int numero = (random.nextInt(12) + 1); // 00044623 genera un número aleatorio entre 1 y 12 para el mes

        if (numero < 10){ // 00044623 verifica si el número es menor a 10 para formatearlo con cero inicial
            mes = "0" + numero; // 00044623 agrega un cero inicial al mes
        } else {
            mes = String.valueOf(numero); // 00044623 convierte el número a cadena
        }

        int ano = random.nextInt(31) + 25; // 00044623 genera un año aleatorio entre 25 y 31

        return mes + "/" + ano; // 00044623 devuelve la fecha generada en formato MM/YY
    }

    private void limpiparTar(){ // 00044623 método para limpiar campos
        tfDUI.setText(""); // 00044623 limpia el campo de entrada tfDUI
        cbTipoTarjeta.getSelectionModel().selectFirst(); // 00044623 selecciona el primer elemento en el ComboBox de tipo de tarjeta
        cbFacilitador.getSelectionModel().selectFirst(); // 00044623 selecciona el primer elemento en el ComboBox de facilitador
        lblErrorTar.setText(""); // 00044623 limpia el texto de lblErrorTar
    }

    private void tiempoLabel(Label label){ // 00044623 método para mostrar un label por 2 seg
        label.setVisible(true); // 00044623 hace visible el label pasado como parámetro por si acaso estaba invisible
        Duration duration = Duration.seconds(2); // 00044623 define la duración de 2 segundos
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> { // 00044623 crea una línea de tiempo
            label.setVisible(false); // 00044623 hace invisible el label después de 2 segundos
        }));
        timeline.play(); // 00044623 inicia la animación de la línea de tiempo
    }
}