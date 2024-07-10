-- crear base de datos con nombre BCN
create database BCN;

use BCN;

-- Tabla Cliente
CREATE TABLE Cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_completo VARCHAR(50) NOT NULL,
    dui VARCHAR(11) UNIQUE NOT NULL,
    direccion VARCHAR(75) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    contrasenia VARCHAR(20) NOT NULL
);

-- Tabla Tarjeta
CREATE TABLE Tarjeta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(20) UNIQUE NOT NULL,
    fecha_expiracion VARCHAR(7) NOT NULL,
    tipo VARCHAR(10) NOT NULL,
    facilitador VARCHAR(30) NOT NULL,
    cliente_id INT NOT NULL,
    FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
);

-- Tabla Transaccion
CREATE TABLE Transaccion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha VARCHAR(15) NOT NULL,
    monto DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    tarjeta_id INT NOT NULL,
    FOREIGN KEY (tarjeta_id) REFERENCES Tarjeta(id)
);

-- Tabla Admin (Administradores)
CREATE TABLE Admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(20) NOT NULL
);

-- Algunos inserts de prueba

-- Inserts para tabla Cliente
INSERT INTO Cliente (nombre_completo, dui, direccion, telefono, contrasenia) VALUES
('Juan Pérez', '12345678-9', '123 Calle Falsa', '555-1234', 'contrasenia1'),
('María Gómez', '23456789-0', '456 Avenida Real', '555-5678', 'contrasenia2'),
('Carlos Ruiz', '34567890-1', '789 Boulevard Central', '555-8765', 'contrasenia3'),
('Ana López', '45678901-2', '101 Calle Nueva', '555-6543', 'contrasenia4'),
('Luis Fernández', '56789012-3', '202 Avenida Vieja', '555-4321', 'contrasenia5'),
('José Martínez', '67890123-4', '303 Plaza Mayor', '555-3456', 'contrasenia6'),
('Marta Sánchez', '78901234-5', '404 Calle Antigua', '555-2345', 'contrasenia7'),
('Pedro García', '89012345-6', '505 Avenida Moderno', '555-1235', 'contrasenia8'),
('Lucía Morales', '90123456-7', '606 Calle Principal', '555-6789', 'contrasenia9'),
('Miguel Torres', '01234567-8', '707 Calle Segunda', '555-7890', 'contrasenia10');

-- Inserts para tabla Tarjeta
INSERT INTO Tarjeta (numero, fecha_expiracion, tipo, facilitador, cliente_id) VALUES
('1111222233334444', '12/2025', 'Crédito', 'Visa', 1),
('2222333344445555', '11/2024', 'Débito', 'American Express', 2),
('3333444455556666', '10/2026', 'Crédito', 'Visa', 3),
('4444555566667777', '09/2023', 'Débito', 'MasterCard', 4),
('5555666677778888', '08/2025', 'Crédito', 'American Express', 5),
('6666777788889999', '07/2024', 'Débito', 'MasterCard', 6),
('7777888899990000', '06/2026', 'Crédito', 'Visa', 7),
('8888999900001111', '05/2023', 'Débito', 'MasterCard', 8),
('9999000011112222', '04/2025', 'Crédito', 'American Express', 9),
('0000111122223333', '03/2024', 'Débito', 'MasterCard', 10);

-- Inserts para tabla Transaccion
INSERT INTO Transaccion (fecha, monto, descripcion, tarjeta_id) VALUES
('2024-01-01', 100.50, 'Compra en supermercado', 1),
('2024-02-15', 200.75, 'Pago de servicios', 2),
('2024-03-20', 150.00, 'Cena en restaurante', 3),
('2024-04-10', 50.25, 'Compra en tienda de ropa', 4),
('2024-05-05', 300.00, 'Pago de alquiler', 5),
('2024-06-22', 400.10, 'Compra de electrodomésticos', 6),
('2024-07-18', 250.30, 'Suscripción anual', 7),
('2024-08-12', 100.75, 'Compra de libros', 8),
('2024-09-25', 500.00, 'Viaje de vacaciones', 9),
('2024-10-30', 75.60, 'Compra en farmacia', 10);

-- Insert para un administrador
INSERT INTO Admin (username, password) VALUES ('admi', '12345');