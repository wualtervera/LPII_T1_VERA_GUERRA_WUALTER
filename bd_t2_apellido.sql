-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 21-06-2025 a las 06:29:05
-- Versión del servidor: 10.4.16-MariaDB
-- Versión de PHP: 7.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_t2_apellido`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `alquileres`
--

CREATE TABLE `alquileres` (
  `id_alquiler` int(11) NOT NULL,
  `estado` enum('ACTIVO','DEVUELTO','RETRASADO') NOT NULL,
  `fecha` date NOT NULL,
  `total` decimal(10,2) NOT NULL,
  `id_cliente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `alquileres`
--

INSERT INTO `alquileres` (`id_alquiler`, `estado`, `fecha`, `total`, `id_cliente`) VALUES
(1, 'ACTIVO', '2025-06-20', '33.00', 1),
(2, 'ACTIVO', '2025-06-20', '5.50', 2),
(3, 'ACTIVO', '2025-06-20', '5.50', 3);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id_cliente` int(11) NOT NULL,
  `email` varchar(150) NOT NULL,
  `nombre` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id_cliente`, `email`, `nombre`) VALUES
(1, 'wvera@gmail.com', 'WUALTER VERA GUERRA'),
(2, 'cruizg@gmail.com', 'CHRISTIAN RUIZ GONZALES'),
(3, 'jrodriguezg@gmail.com', 'JUAN RODRIGUES GONZALES');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_alquiler`
--

CREATE TABLE `detalle_alquiler` (
  `id_alquiler` int(11) NOT NULL,
  `id_pelicula` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `detalle_alquiler`
--

INSERT INTO `detalle_alquiler` (`id_alquiler`, `id_pelicula`, `cantidad`) VALUES
(1, 1, 6),
(2, 4, 1),
(3, 2, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `peliculas`
--

CREATE TABLE `peliculas` (
  `id_pelicula` int(11) NOT NULL,
  `genero` varchar(50) NOT NULL,
  `stock` int(11) NOT NULL,
  `titulo` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `peliculas`
--

INSERT INTO `peliculas` (`id_pelicula`, `genero`, `stock`, `titulo`) VALUES
(1, 'Acción', 38, 'Iron Ma ll'),
(2, 'Ciencia Ficción', 4, 'Terminator 2: El juicio final'),
(3, 'Acción', 10, 'Die Hard: La jungla de cristal'),
(4, 'Ciencia Ficción', 5, 'Matrix '),
(5, 'Aventura', 10, 'Mad Max: Fury Road ');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `alquileres`
--
ALTER TABLE `alquileres`
  ADD PRIMARY KEY (`id_alquiler`),
  ADD KEY `FKc32vjwe393watfcig6e5udx5x` (`id_cliente`);

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id_cliente`),
  ADD UNIQUE KEY `UK1c96wv36rk2hwui7qhjks3mvg` (`email`);

--
-- Indices de la tabla `detalle_alquiler`
--
ALTER TABLE `detalle_alquiler`
  ADD PRIMARY KEY (`id_alquiler`,`id_pelicula`),
  ADD KEY `FK6059wr1fl3get229o3hoaq740` (`id_pelicula`);

--
-- Indices de la tabla `peliculas`
--
ALTER TABLE `peliculas`
  ADD PRIMARY KEY (`id_pelicula`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `alquileres`
--
ALTER TABLE `alquileres`
  MODIFY `id_alquiler` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT de la tabla `peliculas`
--
ALTER TABLE `peliculas`
  MODIFY `id_pelicula` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `alquileres`
--
ALTER TABLE `alquileres`
  ADD CONSTRAINT `FKc32vjwe393watfcig6e5udx5x` FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id_cliente`);

--
-- Filtros para la tabla `detalle_alquiler`
--
ALTER TABLE `detalle_alquiler`
  ADD CONSTRAINT `FK2umgu6w5nl9cg9ax6nxi5bqmf` FOREIGN KEY (`id_alquiler`) REFERENCES `alquileres` (`id_alquiler`),
  ADD CONSTRAINT `FK6059wr1fl3get229o3hoaq740` FOREIGN KEY (`id_pelicula`) REFERENCES `peliculas` (`id_pelicula`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
