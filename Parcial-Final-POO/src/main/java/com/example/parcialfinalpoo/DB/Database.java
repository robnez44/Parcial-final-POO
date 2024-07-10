package com.example.parcialfinalpoo.DB;

import java.sql.*;

public class Database {//00070523 - Clase Database para manejar las conexiones y los inserts con la base de datos

    //00070523 - Metodo main para mostrar los inserts en consola
    public static void main(String[] args) {
        Connection conn = ConexionBD(); //00070523 - Estableciendo la conexion a la base de datos
        if (conn != null) { //00070523 - Verificando si la conexion no dio error
            InsertAdmins(conn); //00070523 - Insertando datos en la tabla Admin
            System.out.println();

            InsertCliente(conn); //00070523 - Insertando datos en la tabla Cliente
            System.out.println();

            InsertTarjeta(conn); //00070523 - Insertando datos en la tabla Tarjeta
            System.out.println();

            InsertTransaccion(conn); //00070523 - Insertando datos en la tabla Transaccion
            System.out.println();

            closeConnection(conn);//00070523 - Cerrando la conexion a la base de datos
            System.out.println();
        }
    }

    //00070523 - Metodo para conectar a la base de datos
    public static Connection ConexionBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //00070523 - Cargando el driver JDBC de MySQL
            //00070523 - Conectando a la base de datos "Sistema_Banco" con el usuario y contra
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BCN", "root", "chibirobo");
            System.out.println("Conexion a la base de datos realizada correctamente!!");
            return conn;
        } catch (Exception e) { //00070523 - Capturando excepciones en caso de errores
            e.printStackTrace(); //00070523 - Imprimiendo errores
            return null; //00070523 - Retornando null en caso de falla en la conexion a la base de datos
        }
    }

    //00070523 - Metodo para insertar datos en la tabla Admin
    public static void InsertAdmins(Connection conn) {
        //00070523 - Arreglo de consultas para insertar administradores a la tabla Admin
        String[] insert = {
                "INSERT INTO Admin(username,password) VALUES('Roberto Martinez','Chelin123')",
                "INSERT INTO Admin(username,password) VALUES('Carlos Rivas','CR456')",
                "INSERT INTO Admin(username,password) VALUES('Diego Mejia','FCB2012')",
                "INSERT INTO Admin(username,password) VALUES('Daniel Murcia','Messi1028')"
        };
        try (Statement stmt = conn.createStatement()) { //00070523 - Creando una declaracion sql
            for (String inserts : insert) { //00070523 - Iterando sobre cada consulta de insert
                stmt.executeUpdate(inserts); //00070523 - Ejecutando cada consulta de insert
            }
            System.out.println("Administradores ingresados correctamente");
        } catch (SQLException e) { //00070523 - Capturando excepciones
            e.printStackTrace(); //00070523 - Imprimiendo errores
        }
    }

    //00070523 - Metodo para insertar datos en la tabla Cliente
    public static void InsertCliente(Connection conn) {
        //00070523 - Arreglo de consultas para insertar clientes a la tabla Cliente
        String[] insert = {
                "INSERT INTO Cliente(nombre_completo,DUI,direccion,telefono,contrasenia) VALUES('Oracio Paredes','23546109-4','Colonia Miramonte','7433-8906','OP09778')",
                "INSERT INTO Cliente(nombre_completo,DUI,direccion,telefono,contrasenia) VALUES('Alejandra Rodriguez','43586207-5','Nuevo Lourdes','7945-2343','AR02312')",
                "INSERT INTO Cliente(nombre_completo,DUI,direccion,telefono,contrasenia) VALUES('Enrique Guzman','56529179-5','Colonia Escalon','7545-5676','EG04521')",
                "INSERT INTO Cliente(nombre_completo,DUI,direccion,telefono,contrasenia) VALUES('Carolina Figueroa','31648129-4','Ciudad Arce','7234-4534','CF06645')"
        };
        try (Statement stmt = conn.createStatement()) { //00070523 - Creando una declaracion sql
            for (String inserts : insert) { //00070523 - Iterando sobre cada consulta de insert
                stmt.executeUpdate(inserts); //00070523 - Ejecutando cada consulta de insert
            }
            System.out.println("Clientes ingresados correctamente!!");
        } catch (SQLException e) { //00070523 - Capturando excepciones
            e.printStackTrace(); //00070523 - Imprimiendo errores
        }
    }

    //00070523 - Metodo para insertar datos en la tabla Tarjeta
    public static void InsertTarjeta(Connection conn) {
        //00070523 - Arreglo de consultas para insertar tarjetas
        String[] insert = {
                "INSERT INTO Tarjeta(numero_tarjeta,fecha_expiracion,tipo_tarjeta,facilitador,id_cliente) VALUES('786345232445','2027-04-10','Debito','Visa',2)",
                "INSERT INTO Tarjeta(numero_tarjeta,fecha_expiracion,tipo_tarjeta,facilitador,id_cliente) VALUES('603263781227','2028-06-20','Credito','MasterCard',4)",
                "INSERT INTO Tarjeta(numero_tarjeta,fecha_expiracion,tipo_tarjeta,facilitador,id_cliente) VALUES('512445023471','2028-03-02','Debito','American Express',1)",
                "INSERT INTO Tarjeta(numero_tarjeta,fecha_expiracion,tipo_tarjeta,facilitador,id_cliente) VALUES('657332012341','2027-05-25','Credito','MasterCard',2)"
        };
        try (Statement stmt = conn.createStatement()) { //00070523 - Creando una declaracion sql
            for (String inserts : insert) { //00070523 - Iterando sobre cada consulta de insert
                stmt.executeUpdate(inserts); //00070523 - Ejecutando cada consulta de insert
            }
            System.out.println("Tarjetas ingresadas correctamente!!");
        } catch (SQLException e) { //00070523 - Capturando excepciones
            e.printStackTrace(); //00070523 - Imprimiendo errores
        }
    }

    //00070523 - Metodo para insertar datos en la tabla Transaccion
    public static void InsertTransaccion(Connection conn) {
        //00070523 - Arreglo de consultas para insertar transacciones
        String[] insert = {
                "INSERT INTO Transaccion(fecha,monto,descripcion,id_tarjeta) VALUES('2024-02-10',10.22,'Compra en lugar de comida',2)",
                "INSERT INTO Transaccion(fecha,monto,descripcion,id_tarjeta) VALUES('2024-04-02',30.50,'Compra almacenes SIMAN',3)",
                "INSERT INTO Transaccion(fecha,monto,descripcion,id_tarjeta) VALUES('2024-06-15',65.00,'Compra en Super Selectos',1)",
                "INSERT INTO Transaccion(fecha,monto,descripcion,id_tarjeta) VALUES('2024-05-12',11.40,'Compra realizada en Burguer King',4)"
        };
        try (Statement stmt = conn.createStatement()) { //00070523 - Creando una declaracion sql
            for (String inserts : insert) { //00070523 - Iterando sobre cada consulta de insert
                stmt.executeUpdate(inserts); //00070523 - Ejecutando cada consulta de insert
            }
            System.out.println("Transacciones ingresadas correctamente!!");
        } catch (SQLException e) { //00070523 - Capturando excepciones
            e.printStackTrace(); //00070523 - Imprimiendo errores
        }
    }

    //00070523 - Metodo para cerrar la conexion a la base de datos
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {//verificando si la conexion no es nula y verificando si la conexion no esta cerrada
                conn.close();//cierra la conexion a la base de datos
                System.out.println("Conexion cerrada correctamente.");

            }
        } catch (SQLException e) { //00070523 - Capturando excepciones
            e.printStackTrace(); //00070523 - Imprimiendo errores
        }
    }
}
