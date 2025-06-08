-- Crear base de datos
CREATE DATABASE BD2_Apellido;
USE BD2_Apellido;

-- Tabla clientes
CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE
);

-- Tabla peliculas
CREATE TABLE peliculas (
    id_pelicula INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    stock INT NOT NULL DEFAULT 0
);

-- Tabla alquileres
CREATE TABLE alquileres (
    id_alquiler INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    id_cliente INT NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('ACTIVO', 'DEVUELTO', 'RETRASADO') DEFAULT 'ACTIVO',
    FOREIGN KEY (id_cliente) REFERENCES clientes(id_cliente)
);

-- Tabla detalle_alquiler
CREATE TABLE detalle_alquiler (
    id_alquiler INT NOT NULL,
    id_pelicula INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_alquiler, id_pelicula),
    FOREIGN KEY (id_alquiler) REFERENCES alquileres(id_alquiler),
    FOREIGN KEY (id_pelicula) REFERENCES peliculas(id_pelicula)
);

-- Insertar datos de prueba
INSERT INTO clientes (nombre, email) VALUES
('Juan Pérez', 'juan.perez@email.com'),
('María García', 'maria.garcia@email.com'),
('Carlos López', 'carlos.lopez@email.com');

INSERT INTO peliculas (titulo, genero, stock) VALUES
('Avatar', 'Ciencia Ficción', 5),
('Titanic', 'Romance', 3),
('El Padrino', 'Drama', 2);