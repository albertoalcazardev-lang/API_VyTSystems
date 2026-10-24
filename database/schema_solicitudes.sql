CREATE DATABASE IF NOT EXISTS vyt_systems;
USE vyt_systems;

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(120) NOT NULL UNIQUE,
    rol ENUM('ADMINISTRADOR', 'CONSULTA') NOT NULL
);

INSERT IGNORE INTO usuarios (id_usuario, nombre, correo, rol) VALUES
(1, 'Director del medio', 'admin@vyt.mx', 'ADMINISTRADOR'),
(2, 'Licenciado Fabela', 'consulta@vyt.mx', 'CONSULTA');

CREATE TABLE IF NOT EXISTS solicitudes (
    id_solicitud INT AUTO_INCREMENT PRIMARY KEY,
    fecha_entrega DATE NOT NULL,
    hora_entrega TIME NOT NULL,
    autoridad_receptora VARCHAR(120) NOT NULL,
    asunto VARCHAR(180) NOT NULL,
    medio_entrega ENUM('DIGITAL', 'IMPRESO') NOT NULL,
    estado_solicitud ENUM('ENTREGADA', 'EN_PROCESO') NOT NULL,
    tipo_solicitud VARCHAR(80) NOT NULL,
    evidencia VARCHAR(255) NOT NULL,
    tiene_acuse BOOLEAN NOT NULL,
    detalle_acuse VARCHAR(100) NOT NULL,
    plazo_vencimiento DATE NOT NULL,
    prioridad ENUM('ALTA', 'MEDIA', 'BAJA') NOT NULL,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_solicitud_unica (asunto, autoridad_receptora, fecha_entrega),
    INDEX idx_solicitudes_estado_prioridad (estado_solicitud, prioridad),
    INDEX idx_solicitudes_plazo (plazo_vencimiento)
);
