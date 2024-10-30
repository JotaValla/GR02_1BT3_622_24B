-- Creación de la base de datos
CREATE DATABASE IF NOT EXISTS polimarket;
USE polimarket;

-- Creación de la tabla 'usuarios'
CREATE TABLE IF NOT EXISTS usuarios (
                                        id_usuario BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        email VARCHAR(255),
    foto VARCHAR(255),
    telefono VARCHAR(255),
    username VARCHAR(255)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Creación de la tabla 'anuncios'
CREATE TABLE IF NOT EXISTS anuncios (
                                        id_anuncio BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                        categoria VARCHAR(255),
    descripcion VARCHAR(255),
    imagen VARCHAR(255),
    precio DECIMAL(38, 2),
    titulo VARCHAR(255),
    usuario_id BIGINT,
    CONSTRAINT FKnonliymh7f3v70k87lf8p22jm FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Creación de la tabla 'valoraciones'
CREATE TABLE IF NOT EXISTS valoraciones (
                                            id_valoracion BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                                            comentario VARCHAR(255),
    estrellas INT,
    anuncio_id BIGINT,
    usuario_id BIGINT,
    CONSTRAINT FK5eclfgku6ymssgmvhsj4a30yj FOREIGN KEY (anuncio_id) REFERENCES anuncios (id_anuncio),
    CONSTRAINT FKmtbedrv2q0wjdsrvnb57g8whw FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
