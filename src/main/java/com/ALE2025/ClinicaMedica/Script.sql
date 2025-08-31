-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.43 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.11.0.7065
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para clinicamedica
CREATE DATABASE IF NOT EXISTS `clinicamedica` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `clinicamedica`;

-- Volcando estructura para tabla clinicamedica.citas
CREATE TABLE IF NOT EXISTS `citas` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paciente_id` int NOT NULL,
  `medico_id` int NOT NULL,
  `estado` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
  `costo_consulta` double NOT NULL,
  `fecha_cita` datetime(6) NOT NULL,
  `hora_cita` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `paciente_id` (`paciente_id`),
  KEY `idx_cita_unica` (`medico_id`) USING BTREE,
  KEY `UKlivyw7dxek36j31a9p7qlfvdb` (`medico_id`,`fecha_cita`,`hora_cita`) USING BTREE,
  CONSTRAINT `citas_ibfk_1` FOREIGN KEY (`paciente_id`) REFERENCES `pacientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `citas_ibfk_2` FOREIGN KEY (`medico_id`) REFERENCES `medicos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.citas: ~5 rows (aproximadamente)
INSERT INTO `citas` (`id`, `paciente_id`, `medico_id`, `estado`, `costo_consulta`, `fecha_cita`, `hora_cita`) VALUES
	(7, 1, 2, 'Pendiente', 20, '2025-08-29 06:00:00.000000', '1970-01-01 06:20:00.000000'),
	(17, 26, 14, 'En espera', 28.5, '2025-09-06 06:00:00.000000', '1970-01-01 21:20:00.000000'),
	(18, 11, 11, 'Solicitado', 25, '2025-10-14 06:00:00.000000', '1970-01-01 19:00:00.000000'),
	(19, 10, 15, 'Finalizado', 32, '2025-08-04 06:00:00.000000', '1970-01-01 15:30:00.000000'),
	(20, 8, 10, 'Pendiente', 23, '2025-08-28 06:00:00.000000', '1970-01-01 20:33:00.000000');

-- Volcando estructura para tabla clinicamedica.especialidades
CREATE TABLE IF NOT EXISTS `especialidades` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `descripcion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.especialidades: ~8 rows (aproximadamente)
INSERT INTO `especialidades` (`id`, `nombre`, `descripcion`) VALUES
	(2, 'Ginecología', 'Rama que estudia el cuerpo femenino qshdahsbsdbasihd'),
	(4, 'Dentista', 'es un profesional de la salud especializado en el cuidado integral de la boca, los dientes y las encías'),
	(5, 'Oftalmología', 'Enfermedades y cirugía del ojo.'),
	(6, 'Cardiología', 'Rama de la medicina que estudia el corazón y el sistema circulatorio.'),
	(7, 'Pediatría', 'Especialidad médica que atiende y trata a niños.'),
	(8, 'Dermatología', 'Especialidad que se encarga de la piel y sus enfermedades.'),
	(9, 'Traumatología', 'Especialidad que se encarga de lesiones y enfermedades del sistema musculoesquelético.'),
	(10, 'Psicologia', 'rama que estudia el pensamiento humano');

-- Volcando estructura para tabla clinicamedica.historiales
CREATE TABLE IF NOT EXISTS `historiales` (
  `id` int NOT NULL AUTO_INCREMENT,
  `paciente_id` int NOT NULL,
  `medico_id` int NOT NULL,
  `especialidad_id` int NOT NULL,
  `fecha_cita` datetime(6) NOT NULL,
  `observaciones` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `paciente_id` (`paciente_id`),
  KEY `medico_id` (`medico_id`),
  KEY `especialidad_id` (`especialidad_id`),
  CONSTRAINT `historiales_ibfk_1` FOREIGN KEY (`paciente_id`) REFERENCES `pacientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `historiales_ibfk_2` FOREIGN KEY (`medico_id`) REFERENCES `medicos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `historiales_ibfk_3` FOREIGN KEY (`especialidad_id`) REFERENCES `especialidades` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.historiales: ~8 rows (aproximadamente)
INSERT INTO `historiales` (`id`, `paciente_id`, `medico_id`, `especialidad_id`, `fecha_cita`, `observaciones`) VALUES
	(3, 3, 2, 2, '2025-08-25 06:00:00.000000', 'Ovarios Poliquisticos'),
	(5, 1, 2, 4, '2025-08-25 06:00:00.000000', 'Se le rompio la muela '),
	(7, 4, 12, 6, '2025-08-27 06:00:00.000000', 'Chequeo cardiológico, paciente con presión arterial alta.'),
	(8, 5, 13, 7, '2025-08-29 00:00:00.000000', 'Consulta pediátrica, niño presenta síntomas de resfriado común.'),
	(9, 6, 14, 8, '2025-09-01 00:00:00.000000', 'Consulta por dermatitis, se prescribe crema tópica.'),
	(10, 7, 15, 9, '2025-09-02 00:00:00.000000', 'Paciente con fractura en el antebrazo derecho, se coloca yeso.'),
	(11, 8, 12, 6, '2025-09-03 00:00:00.000000', 'Control de paciente con hipertensión, se ajusta medicación.'),
	(12, 22, 13, 6, '2025-08-27 06:00:00.000000', 'Se le revisaron los niveles de insulina');

-- Volcando estructura para tabla clinicamedica.horarios
CREATE TABLE IF NOT EXISTS `horarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `medico_id` int NOT NULL,
  `dia_semana` enum('Domingo','Jueves','Lunes','Martes','Miércoles','Sábado','Viernes') COLLATE utf8mb4_unicode_ci NOT NULL,
  `hora_fin` time(6) NOT NULL,
  `hora_inicio` time(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_horario_medico` (`medico_id`) USING BTREE,
  CONSTRAINT `horarios_ibfk_1` FOREIGN KEY (`medico_id`) REFERENCES `medicos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.horarios: ~7 rows (aproximadamente)
INSERT INTO `horarios` (`id`, `medico_id`, `dia_semana`, `hora_fin`, `hora_inicio`) VALUES
	(2, 10, 'Miércoles', '14:20:00.000000', '03:19:00.000000'),
	(3, 2, 'Lunes', '12:00:00.000000', '06:00:00.000000'),
	(5, 12, 'Jueves', '18:00:00.000000', '14:00:00.000000'),
	(7, 13, 'Viernes', '19:00:00.000000', '15:00:00.000000'),
	(8, 14, 'Miércoles', '14:00:00.000000', '10:00:00.000000'),
	(10, 15, 'Lunes', '13:30:00.000000', '09:30:00.000000'),
	(13, 10, 'Sábado', '16:05:00.000000', '13:00:00.000000');

-- Volcando estructura para tabla clinicamedica.medicos
CREATE TABLE IF NOT EXISTS `medicos` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_Especialidad` int NOT NULL,
  `costo_consulta` double NOT NULL,
  `genero` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `dui` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `direccion` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `imagen` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `DUI` (`dui`),
  UNIQUE KEY `email` (`email`),
  KEY `id_Especialidad` (`id_Especialidad`),
  CONSTRAINT `medicos_ibfk_1` FOREIGN KEY (`id_Especialidad`) REFERENCES `especialidades` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.medicos: ~7 rows (aproximadamente)
INSERT INTO `medicos` (`id`, `nombre`, `apellido`, `id_Especialidad`, `costo_consulta`, `genero`, `dui`, `telefono`, `direccion`, `email`, `imagen`) VALUES
	(2, 'Alberto', 'Perez', 2, 20, 'Masculino', '04033321-9', '70106238', 'Las lomas', 'Mauricio22@gmail.com', '/images/1756322683433_medico 5.jpg'),
	(10, 'Oscar', 'Cruz', 2, 23, 'Masculino', '01022256-9', '70106238', 'Sonsonate', 'Cheese@gmail.com', '/images/1756315378055_medico 5.jpg'),
	(11, 'Rodolfo', 'Sanchez', 4, 25, 'Masculino', '03022224-1', '70106235', 'Sonsonate', 'Sanchez@gmail.com', '/images/1756149586095_Medico 2.jpg'),
	(12, 'Ana', 'Rodriguez', 6, 25, 'Femenino', '03456789-0', '71234567', 'Santa Tecla', 'ana.rodriguez@example.com', '/images/1756315351832_medico 4.jpg'),
	(13, 'Jose', 'Gomez', 7, 30, 'Masculino', '04567890-1', '72345678', 'San Salvador', 'jose.gomez@example.com', '/images/1756315366083_medico 3.jpg'),
	(14, 'Maria', 'Diaz', 8, 28.5, 'Femenino', '05678901-2', '73456789', 'Antiguo Cuscatlán', 'maria.diaz@example.com', NULL),
	(15, 'David', 'Lopez', 9, 32, 'Masculino', '06789012-3', '74567890', 'Ilopango', 'david.lopez@example.com', NULL);

-- Volcando estructura para tabla clinicamedica.pacientes
CREATE TABLE IF NOT EXISTS `pacientes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `apellido` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefono` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `fechaNacimiento` date DEFAULT NULL,
  `dui` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `fecha_nacimiento` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `DUI` (`dui`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.pacientes: ~42 rows (aproximadamente)
INSERT INTO `pacientes` (`id`, `nombre`, `apellido`, `telefono`, `fechaNacimiento`, `dui`, `fecha_nacimiento`) VALUES
	(1, 'Marvin', 'Hernandez', '70106238', NULL, '08099923-8', '1990-07-17 06:00:00.000000'),
	(2, 'Carmela', 'Cruz', '78451100', NULL, '01055533-3', '2000-05-10 06:00:00.000000'),
	(3, 'Maria', 'Hernandez', '75612144', NULL, '01077744-4', '2001-04-16 06:00:00.000000'),
	(4, 'Carlos', 'Lopez', '77001122', NULL, '01234567-9', '1995-01-20 00:00:00.000000'),
	(5, 'Ana', 'Gomez', '78998877', NULL, '01987654-2', '1988-11-05 00:00:00.000000'),
	(6, 'Luis', 'Perez', '71112233', NULL, '02345678-0', '1992-03-15 00:00:00.000000'),
	(7, 'Sofia', 'Ramirez', '73445566', NULL, '03456789-1', '1990-09-22 00:00:00.000000'),
	(8, 'Pedro', 'Garcia', '79998811', NULL, '04567890-2', '1985-06-30 00:00:00.000000'),
	(9, 'Marta', 'Rodriguez', '70112233', NULL, '05678901-3', '1998-02-10 00:00:00.000000'),
	(10, 'Javier', 'Fernandez', '72223344', NULL, '06789012-4', '1980-08-18 00:00:00.000000'),
	(11, 'Elena', 'Diaz', '75556677', NULL, '07890123-5', '1993-04-25 00:00:00.000000'),
	(12, 'Miguel', 'Sanchez', '76667788', NULL, '08901234-6', '1987-12-01 00:00:00.000000'),
	(13, 'Laura', 'Martínez', '74445599', NULL, '09012345-7', '1999-07-07 00:00:00.000000'),
	(14, 'Ricardo', 'Alvarez', '71234567', NULL, '10123456-8', '1991-05-12 00:00:00.000000'),
	(15, 'Gabriela', 'Torres', '72345678', NULL, '11234567-9', '1989-01-28 00:00:00.000000'),
	(16, 'Roberto', 'Flores', '73456789', NULL, '12345678-0', '1996-10-14 00:00:00.000000'),
	(17, 'Carmen', 'Morales', '74567890', NULL, '13456789-1', '1984-03-03 00:00:00.000000'),
	(18, 'Andres', 'Gutierrez', '75678901', NULL, '14567890-2', '1994-09-09 00:00:00.000000'),
	(19, 'Paola', 'Castillo', '76789012', NULL, '15678901-3', '1997-11-20 00:00:00.000000'),
	(20, 'Sergio', 'Ortiz', '77890123', NULL, '16789012-4', '1986-07-27 00:00:00.000000'),
	(21, 'Alejandra', 'Mendez', '78901234', NULL, '17890123-5', '1992-02-19 00:00:00.000000'),
	(22, 'Fernando', 'Cruz', '79012345', NULL, '18901235-6', '1983-05-08 00:00:00.000000'),
	(23, 'Veronica', 'Vasquez', '70123456', NULL, '19012346-7', '2000-12-05 00:00:00.000000'),
	(24, 'Diego', 'Reyes', '71122334', NULL, '20123457-8', '1995-08-16 00:00:00.000000'),
	(25, 'Maria', 'Soto', '72233445', NULL, '21234568-9', '1988-06-11 00:00:00.000000'),
	(26, 'Hector', 'Castro', '73344556', NULL, '22356789-0', '1991-03-24 00:00:00.000000'),
	(27, 'Diana', 'Ruiz', '74455667', NULL, '23456890-1', '1990-10-31 00:00:00.000000'),
	(28, 'Jose', 'Chavez', '75566778', NULL, '24578901-2', '1987-04-04 00:00:00.000000'),
	(29, 'Lucia', 'Silva', '76677889', NULL, '25789012-3', '1998-09-17 00:00:00.000000'),
	(30, 'Manuel', 'Rojas', '77788990', NULL, '67890123-4', '1982-11-28 00:00:00.000000'),
	(31, 'Patricia', 'Luna', '78899001', NULL, '27890134-5', '1993-01-02 00:00:00.000000'),
	(32, 'Daniel', 'Paz', '79900112', NULL, '28901234-6', '1986-08-13 00:00:00.000000'),
	(33, 'Rosa', 'Vargas', '70011223', NULL, '29012356-7', '1999-05-26 00:00:00.000000'),
	(34, 'Alfredo', 'Moreno', '71122334', NULL, '30234567-8', '1994-02-09 00:00:00.000000'),
	(35, 'Adriana', 'Caceres', '72233445', NULL, '31245678-9', '1985-07-21 00:00:00.000000'),
	(36, 'Jorge', 'Herrera', '73344556', NULL, '32346789-0', '1996-04-10 00:00:00.000000'),
	(37, 'Isabel', 'Guzman', '74455667', NULL, '33467890-1', '1983-12-19 00:00:00.000000'),
	(38, 'Cristian', 'Pineda', '75566778', NULL, '34568901-2', '1990-03-07 00:00:00.000000'),
	(39, 'Liliana', 'Fuentes', '76677889', NULL, '35689012-3', '1997-06-23 00:00:00.000000'),
	(40, 'Kevin', 'Lopez', '77788990', NULL, '36789023-4', '1989-10-01 00:00:00.000000'),
	(41, 'Genesis', 'Ayala', '78899001', NULL, '37901234-5', '1995-11-15 00:00:00.000000'),
	(42, 'Walter', 'Mena', '78554213', NULL, '09088854-0', '1999-07-24 06:00:00.000000');

-- Volcando estructura para tabla clinicamedica.roles
CREATE TABLE IF NOT EXISTS `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.roles: ~2 rows (aproximadamente)
INSERT INTO `roles` (`id`, `nombre`) VALUES
	(85, 'ROLE_ADMINISTRADOR'),
	(87, 'ROLE_MEDICO');

-- Volcando estructura para tabla clinicamedica.usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
  `id` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `contraseña` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `estado` enum('activo','inactivo') COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'activo',
  `rol_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`) USING BTREE,
  KEY `rol_id` (`rol_id`),
  CONSTRAINT `usuarios_ibfk_1` FOREIGN KEY (`rol_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Volcando datos para la tabla clinicamedica.usuarios: ~4 rows (aproximadamente)
INSERT INTO `usuarios` (`id`, `nombre`, `contraseña`, `email`, `estado`, `rol_id`) VALUES
	(15, 'Eneida', '$2a$10$mBobgjq3uM6Im9gHCy6qM.uc2zjuGn.7A7HgPe/o4SWDoP1Fi8SjG', 'lizbeth@gmail.com', 'activo', 85),
	(16, 'Esmeralda', '$2a$10$QSidEuMm/bQZPfc2NubmNOdw06B7Y81jOGSva0vED/FV5aXWF/yOO', 'esme@gmail.com', 'activo', 87),
	(17, 'Adolfo', '$2a$10$auftCSTauJdsJW72biQYUemqMhGo77/n.fNn4aSB/vylcfXttMisK', 'adolfo@gmail.com', 'activo', 87),
	(18, 'Walter', '$2a$10$QEfPt49vLi3q61BiHV0D7.27itCVN1wzvhdFgxldWiaizvPxpC/au', 'walter@gmail.com', 'activo', 85);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;