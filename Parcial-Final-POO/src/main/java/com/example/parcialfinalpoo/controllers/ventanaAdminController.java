package com.example.parcialfinalpoo.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.nio.Buffer;
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
    private File fileToLoad;

    @FXML
    Label lblExitoA;

    @FXML
    Label lblExitoC;

    @FXML
    private TextField tfClienteConsultaTarjetas;

    @FXML
    private TextField tfClienteCompras; // 00001323 Creo variable que apunta al textField que obtiene el id del cliente

    @FXML
    private DatePicker dpDesde; // 00001323 Creo variable que apunta al primer datePicker

    @FXML
    private DatePicker dpHasta; // 00001323 Creo variable que apunta al seegundo datepicker

    @FXML
    private Label lblErrorExportar; // 00001323 Creo una variable para un label donde se muestran errores a la hora de exportar reporte de transacciones

    @FXML
    private Label lblErrorTarjeta; // 00001323 Creo una variable para el label donde se muestran errores a la hora de exportar reporte de tarjetas

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
  
    public void limpiarA(){                // 00001323 Esta funcion limpia los campos de la pagina de Admin A (Consultar Compras)
        tfClienteCompras.setText("");      // 00001323 Limpia el textfield donde se ingreso el id del cliente
        dpDesde.setValue(null);            // 00001323 Limpia el primer datePicker
        dpHasta.setValue(null);            // 00001323 Limpar el segundo datepicker
    }

    public void buscarTransacciones(){   // 00001323 Esta funcion realiza la busqueda segun los datos ingresados de ID de cliente y el rango de tiempo
        String id;          // 00001323 Declaro una variable id para almacenar el id del usuario
        String date1;       // 00001323 Declaro una variable String para almacenar una fecha
        String date2;       // 00001323 Obtiene una variable String para almacenar una fecha
        DirectoryChooser directoryChooser = new DirectoryChooser();  // 00001323 Creo una variable Directory Choose para permitir que el usuario elija donde guardar su reporte
        directoryChooser.setTitle("Seleccione donde guardar el reporte:"); // 00001323 Defino el titulo de la ventana emergente para indicarle al usuario que hacer


        if (tfClienteCompras!=null && dpDesde.getValue()!=null && dpHasta.getValue()!=null && !tfClienteCompras.getText().isEmpty()) { // 00001323 Si el usuario lleno los campos requeridos, continua con la funcion. Si no retorna un mensaje de error
            id = tfClienteCompras.getText();             // 00001323 Obtiene la informacion ingresada en un TextField y la almacena en una variable String
            date1 = dpDesde.getValue().toString();       // 00001323 Obtiene la fecha obtenida del primer DatePicker y la almacena en una variable String
            date2 = dpHasta.getValue().toString();       // 00001323 Obtiene la fecha obtenida del segundo DatePicker y la almacena en una variable String


            try {                                                                                        // 00001323 Comienza un try para intentar establecer la connexion a la base de datos
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN","root","chibirobo");    // 00001323 Intenta conectarse a la BD creada para la aplicacion

                PreparedStatement pst = conn.prepareStatement("SELECT * FROM Cliente WHERE id = ?"); // 00001323 Creo un statement que retorna la cantidad de usuarios que existen con el id ingresado. Si existe, deberia retornar 1, si no existe el cliente con ese id, retornara 0
                pst.setString(1, id); // 00001323 Determino que valores se le pasaran a la consulta anterior
                ResultSet rsUser = pst.executeQuery(); // 00001323 Ejecuto la consulta para obtener el resultado

                if (rsUser.next()) { // 00001323 Verifico si la consulta anterior retorno algun resultado. Si es asi, el id ingresado es valido
                    Statement st = conn.createStatement();               // 00001323 Creo un nuevo statement para la siguiente consulta
                    ResultSet rs = st.executeQuery("select t.id, t.fecha, t.monto, t.descripcion, c.numero  FROM Transaccion t INNER JOIN Tarjeta c ON t.tarjeta_id = c.id WHERE fecha BETWEEN '"+ date1 +"'AND '"+date2+"' AND c.cliente_id = '" + id + "'"); // 00001323 Hago una consulta a la base de datos y guardo el resultado en una variable ResultSet
                    if(rs.isBeforeFirst()) {                                                                     // 00001323 Verifica que haya resultados almacenados de la consulta anterior
                        File selectedDirectory = directoryChooser.showDialog(new Stage());                       // 00001323 Manda a llamar una ventana emergente que permite al usuario seleccionar la carpeta en la que guardara el reporte
                        lblErrorExportar.setText("");                                                            // 00001323 Remueve le mensaje de error en caso que hubiese fallado antes

                        try {                                                                                   // 00001323 Inicio un nuevo try Catch para detectar problemas al momento de abrir y escribir sobre el archivo.
                            FileWriter writer = new FileWriter(selectedDirectory.getPath() + File.separator + "ReporteTransacciones-" + date1 + "--" + date2 + ".txt"); // 00001323 Declaro e instancio un nuevo FileWriter en la carpeta seleccionada por el usuario y sobre el archivo
                            writer.write("ID | FECHA | MONTO | DESCRIPCION | TERMINACION TARJETA");         // 00001323 Escribo sobre el archivo los nombres de las columna para que sea mas legible ya exportado
                            writer.write(System.lineSeparator());                                               // 00001323 Agregar un "next line"
                            writer.write(System.lineSeparator());                                               // 00001323 Agregar un "next line"
                            while (rs.next()) {                                                                 // 00001323 Recorro los resultados obtenidos a traves de la consulta de la Base de Datos.
                                String terminacion = rs.getString("numero").substring(11, 15);       // 00001323 Extraigo la terminacion de la tarjeta de credito utilizada en la transaccion
                                writer.write((rs.getInt(1) + " | " + rs.getDate(2) + " | " + rs.getDouble(3) + " | " + rs.getString(4) + " | " + terminacion)); // 00001323 Escribo sobre el archivo la informacion de cada resultado, especificando las mismas columnas mencionadas arriba.
                                writer.write(System.lineSeparator());                                           // 00001323 Agregar un "next line" para que cada resultado este en una fila distinta
                            }
                            writer.close();                                                                     // 00001323 Una vez recorridos todos los resultados, cierro el archivo
                            lblExitoA.setText("Reporte generado con exito.");                                   // 00001323 Muestro un mensaje de confirmacion que el reporte se exporto
                        } catch (
                                IOException e) {                                                               // 00001323 Capturo cualquier error al momento de abrir o escribir en el archivo.
                            System.out.println(e);                                                              // 00001323 Imprimo la excepcion en la consola
                            lblErrorExportar.setText("Error al guardar el reporte. Archivo no se pudo crear."); // 00001323 Mando un mensaje de error que se le muestra al usuario en caso que haya algun problema
                            lblExitoA.setText("");                                                              // 00001323 Actualizo el mensaje de exito para que no muestre nada.
                        }
                    } else {                                                                                    // 00001323 Si no hay ninguna transaccion en los resultados entra en esta condicion
                        lblErrorExportar.setText("El Usuario ingresado no tiene ninguna transaccion en ese periodo."); // 00001323 Retorna un mensaje de error diciendo que no hay transacciones en ese periodo
                    }
                } else {                                                                                        // 00001323 En caso que no exista el ID de usuario ingresado, entra en esta condicion
                    lblErrorExportar.setText("El ID de cliente ingresado no existe. Intentelo de nuevo.");      // 00001323 Se retorna un mensaje de error
                    limpiarA();                                                                                 // 00001323 Se limpian los campos para que pueda intentar denuevo
                }
                conn.close();                                                                                   // 00001323 Cierro la conexion a la base de datos
            } catch (Exception e) {                                                                         // 00001323 Capturo cualquier error al momento de abrir la base de datos o abriendo la carpeta seleccionada
                if(directoryChooser == null){                                                               // 00001323 Esta condicion ayuda a determinar la reazon del error
                    lblErrorTarjeta.setText("No se selecciono la ubicacion del archivo. Intente denuevo."); // 00001323 En caso que el Usuario cancele a la hora de seleccionar la ubicacion del archivo, retornara este error.
                } else {                                                                                    // 00001323 Para otros errores que no se relacionan con la carpeta
                    lblErrorTarjeta.setText("No se pude abrir la base de datos.");                          // 00001323 Muestro un mensaje de error al usuario para que pueda intentar nuevamente en caso que no se pueda abrir la base de datos
                }
                lblExitoA.setText("");                                                                      // 00001323 Actualizo el mensaje de exito para que no muestre nada.
            }
        } else {                                                                                       // 00001323 Si hay algun campo vacio, se corre la condicion siguiente
            lblErrorExportar.setText("Debe llenar todos los campos para poder exportar el reporte.");  // 00001323 Se muestra un mensaje al usuario pidiendo que llene todos los campos para poder continuar
            lblExitoA.setText("");                                                                     // 00001323 Actualizo el mensaje de exito para que no muestre nada.
        }
    }

    public void buscarTarjetas() {
        String id;                                                          // 00001323 Creo una variable donde almacenare el id ingresado
        DirectoryChooser directoryChooser = new DirectoryChooser();         // 00001323 Creo una variable Directory Choose para permitir que el usuario elija donde guardar su reporte
        directoryChooser.setTitle("Seleccione donde guardar el reporte:");  // 00001323 Defino el titulo de la ventana emergente para indicarle al usuario que hacer

        if (tfClienteConsultaTarjetas.getText() != null && !tfClienteConsultaTarjetas.getText().isEmpty()) { // 00001323 Verifica que le campo de ID de cliente no este vacio
            id = tfClienteConsultaTarjetas.getText();                                                        // 00001323 Asigno el contenido del textField a la variable ID

            try {                                                                                        // 00001323 Comienza un try para intentar establecer la connexion a la base de datos
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "root", "chibirobo");    // 00001323 Intenta conectarse a la BD creada para la aplicacion

                PreparedStatement pst = conn.prepareStatement("SELECT * FROM Cliente WHERE id = ?"); // 00001323 Creo un statement que retorna la informacion de usuarios que existen con el id ingresado
                pst.setString(1, id);                                                        // 00001323 Estableciendo los parametros necesarios para el statement anterior
                ResultSet rsUser = pst.executeQuery();                                                    // 00001323 Ejecuto la consulta para obtener el resultado

                if (rsUser.next()) {                                                                      // 00001323 Verifico si la consulta anterior retorno algun resultado. Si es asi, el id ingresado es valido
                    String nombre = rsUser.getString(2);                                       // 00001323 Asigno el nombre del resultado obtenido a una variable string
                    String dui = rsUser.getString(3);                                          // 00001323 Asigno el dui del resultado obtenido a una variable String

                    Statement st1 = conn.createStatement();                                               // 00001323 Creo un primer statement para la siguiente consulta
                    ResultSet rsCredito = st1.executeQuery("SELECT * FROM Tarjeta WHERE cliente_id = '" + id + "' AND tipo = 'CREDITO'");  // 00001323 Ejecuto la consulta que retornara la informacion de todas las tarjetas de credito de un usuario en especifico
                    Statement st2 = conn.createStatement();                                               // 00001323 Creo un segundo statement para la siguiente consulta
                    ResultSet rsDebito = st2.executeQuery("SELECT * FROM Tarjeta WHERE cliente_id = '" + id + "' AND tipo = 'DEBITO'");    // 00001323 Ejecuto la consulta que retornara la informacion de todas las tarjetas de debito de un usuario en especifico

                    File selectedDirectory = directoryChooser.showDialog(new Stage());                      // 00001323 Manda a llamar una ventana emergente que permite al usuario seleccionar la carpeta en la que guardara el reporte
                    lblErrorTarjeta.setText("");                                                            // 00001323 Remueve le mensaje de error en caso que hubiese fallado antes

                    try {                                                                                   // 00001323 Inicio un nuevo try Catch para detectar problemas al momento de abrir y escribir sobre el archivo.
                        FileWriter writer = new FileWriter(selectedDirectory.getPath() + File.separator + "TarjetasDeCliente-" + id + ".txt"); // 00001323 Declaro e instancio un nuevo FileWriter en la carpeta seleccionada por el usuario y sobre el archivo
                        writer.write("TARJETAS PERTENECIENTES A '" + nombre + "' - DUI: '" + dui + "'");         // 00001323 Escribo en el archivo un mensaje inicial
                        writer.write(System.lineSeparator());                                                        // 00001323 "Next line"
                        writer.write(System.lineSeparator());                                                        // 00001323 "Next line"
                        writer.write("TARJETAS DE CREDITO:");                                                    // 00001323 Escribo en el archivo el encabezado antes de enlistar las tarjetas de credito
                        writer.write(System.lineSeparator());                                                        // 00001323 "Next line"

                        if (rsCredito.isBeforeFirst()) {                                                             // 00001323 Verifica que haya resultados, es decir si el usuario tiene o no tarjetas
                            while (rsCredito.next()) {                                                               // 00001323 Recorre los resultados del primer ResultSet que contiene tarjetas de credito
                                String terminacion = rsCredito.getString("numero").substring(11, 15);    // 00001323 Extraigo la terminacion de la tarjeta de credito. Ultimos 4 digitos
                                writer.write("XXXX XXXX XXXX " + terminacion);                                  // 00001323 Escribo sobre el archivo la informacion de cada tarjeta de credito (Solo la terminacion, censurando los numeros del principio)
                                writer.write(System.lineSeparator());                                               // 00001323 "Next Line"
                            }
                        } else {                                                                                    // 00001323 En caso que el resultSet no tenga ningun resultado (No hay tarjetas de credito)
                            writer.write("N/A");                                                                // 00001323 Escribo "N/A" en el archivo
                            writer.write(System.lineSeparator());                                                   // 00001323 Next Line
                        }

                        writer.write(System.lineSeparator());                                                   // 00001323 Next Line
                        writer.write("TARJETAS DE DEBITO:");                                                // 00001323 Escribo en el archivo el encabezado antes de enlistar las tarjetas de debito
                        writer.write(System.lineSeparator());                                                   // 00001323 Next Line

                        if (rsDebito.isBeforeFirst()) {                                                         // 00001323 Verifica que el reesultSet de tarjetas de credito no este vacio
                            while (rsDebito.next()) {                                                           // 00001323 Recorre el resultSet
                                String terminacion = rsDebito.getString("numero").substring(11, 15); // 00001323 Extraigo la terminacion de la tarjeta de credito utilizada en la transaccion
                                writer.write("XXXX XXXX XXXX " + terminacion);                              // 00001323 Escribo sobre el archivo la informacion de cada resultado, especificando las mismas columnas mencionadas arriba.
                                writer.write(System.lineSeparator());                                           // 00001323 Next Line
                            }
                        } else {                                                                                // 00001323 Si el resultSet no tuviese resultados entra en esta condicion
                            writer.write("N/A");                                                            // 00001323 Escribe "N/A" en el archivo si no hay tarjetas de debito
                            writer.write(System.lineSeparator());                                               // 00001323 Next Line
                        }
                        writer.close();                                                                         // 00001323 Una vez recorridos todos los resultados, cierro el archivo
                        lblExitoC.setText("Reporte generado con exito.");                                       // 00001323 Muestra un mensaje de exito
                        lblErrorTarjeta.setText("");                                                            // 00001323 Remueve cualquier mensaje de error que haya sido mostrado antes

                    } catch (
                            IOException e) {                                                               // 00001323 Capturo cualquier error al momento de abrir o escribir en el archivo.
                        System.out.println(e);                                                              // 00001323 Imprimo la excepcion en la consola
                        lblErrorTarjeta.setText("Error al guardar el reporte. Archivo no se pudo crear."); // 00001323 Mando un mensaje de error que se le muestra al usuario en caso que haya algun problema
                    }
                    conn.close();                                                                      // 00001323 Cierro la conexion a la base de datos
                }
            } catch (
                    Exception e) {                                                                    // 00001323 Capturo cualquier error al momento de abrir la base de datos
                System.out.println(e);                                                                 // 00001323 Imprimo la excepcion en consola
                if (directoryChooser == null) {                                                          // 00001323 Si el usuario cancela la accion de seleccionar la ubicacion del archivo, el programa fallara y entrara a esta condicion
                    lblErrorTarjeta.setText("No se selecciono la ubicacion del archivo. Intente denuevo."); // 00001323 Se mostrara un mensaje de error indicandole que debe intentar denuevo y seleccionar una ubicacion esta vez
                } else {                                                                                    // 00001323 Para otros errores al intentar abrir la base de datos
                    lblErrorTarjeta.setText("No se pude abrir la base de datos.");                        // 00001323 Muestro un mensaje de error al usuario para que pueda intentar nuevamente
                }
            }

        } else {                                                                                      // 00001323 Si el ID no existe en la bd, entra en esta condicion
            lblErrorTarjeta.setText("Debe ingresar el id del cliente");                               // 00001323 Se muestra un mensaje de error en la consola
        }
    }

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