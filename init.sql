CREATE SCHEMA IF NOT EXISTS polimarketDB;
SET SCHEMA polimarketDB;

-- Creación de la tabla 'usuarios'
CREATE TABLE usuarios (
                          id_usuario BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          email VARCHAR(255),
                          foto VARCHAR(255),
                          telefono VARCHAR(255),
                          username VARCHAR(255)
);

-- Creación de la tabla 'anuncios'
CREATE TABLE anuncios (
                          id_anuncio BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                          categoria VARCHAR(255),
                          descripcion VARCHAR(255),
                          imagen VARCHAR(255),
                          precio DECIMAL(38, 2),
                          titulo VARCHAR(255),
                          usuario_id BIGINT,
                          CONSTRAINT FK_anuncios_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario)
);

-- Creación de la tabla 'valoraciones'
CREATE TABLE valoraciones (
                              id_valoracion BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              comentario VARCHAR(255),
                              estrellas INT,
                              anuncio_id BIGINT,
                              usuario_id BIGINT,
                              CONSTRAINT FK_valoraciones_anuncios FOREIGN KEY (anuncio_id) REFERENCES anuncios (id_anuncio),
                              CONSTRAINT FK_valoraciones_usuarios FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario)
);
