-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS `ClinicaMedica` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `ClinicaMedica`;

-- Tabla para los roles de usuario (administrador, médico, paciente, etc.)
-- Se utilizará como un "combo" en la tabla de usuarios
CREATE TABLE `Roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(50) NOT NULL UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- Tabla para los usuarios del sistema
CREATE TABLE `Usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `contraseña` VARCHAR(255) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `estado` ENUM('activo', 'inactivo') NOT NULL DEFAULT 'activo',
  `rol_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`rol_id`) REFERENCES `Roles` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Tabla para los pacientes
CREATE TABLE `Pacientes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(20) DEFAULT NULL,
  `fechaNacimiento` DATE DEFAULT NULL,
  `DUI` VARCHAR(10) UNIQUE,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- Tabla para las especialidades médicas
CREATE TABLE `Especialidades` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL UNIQUE,
  `descripcion` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;

-- Tabla para los médicos
CREATE TABLE `Medicos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) NOT NULL,
  `id_Especialidad` INT NOT NULL,
  `costo_consulta` DECIMAL(10, 2) NOT NULL,
  `genero` ENUM('Masculino', 'Femenino', 'Otro') DEFAULT NULL,
  `DUI` VARCHAR(10) NOT NULL UNIQUE,
  `telefono` VARCHAR(20) DEFAULT NULL,
  `direccion` VARCHAR(255) DEFAULT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `imagen` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_Especialidad`) REFERENCES `Especialidades` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Tabla para los horarios de los médicos
CREATE TABLE `Horarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `medico_id` INT NOT NULL,
  `diaSemana` ENUM('Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado', 'Domingo') NOT NULL,
  `horaInicio` TIME NOT NULL,
  `horaFin` TIME NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`medico_id`) REFERENCES `Medicos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  UNIQUE KEY `idx_horario_medico` (`medico_id`, `diaSemana`, `horaInicio`)
) ENGINE = InnoDB;

-- Tabla para las citas
CREATE TABLE `Citas` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `paciente_id` INT NOT NULL,
  `medico_id` INT NOT NULL,
  `fechaCita` DATE NOT NULL,
  `horaCita` TIME NOT NULL,
  `costoConsulta` DECIMAL(10, 2) NOT NULL,
  `estado` ENUM('Pendiente', 'Confirmada', 'Cancelada', 'Finalizada') NOT NULL DEFAULT 'Pendiente',
  PRIMARY KEY (`id`),
  FOREIGN KEY (`paciente_id`) REFERENCES `Pacientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`medico_id`) REFERENCES `Medicos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  UNIQUE KEY `idx_cita_unica` (`medico_id`, `fechaCita`, `horaCita`)
) ENGINE = InnoDB;

-- Tabla para el historial de citas de los pacientes
CREATE TABLE `Historiales` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `paciente_id` INT NOT NULL,
  `medico_id` INT NOT NULL,
  `especialidad_id` INT NOT NULL,
  `fecha_cita` DATE NOT NULL,
  `observaciones` TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`paciente_id`) REFERENCES `Pacientes` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`medico_id`) REFERENCES `Medicos` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  FOREIGN KEY (`especialidad_id`) REFERENCES `Especialidades` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;