-- Conectar a la base de datos Derby
CONNECT 'jdbc:derby:/usr/local/tomcat/data/polimarket;create=true';

-- Creación de la tabla CUENTAS
CREATE TABLE CUENTAS (
                         id_cuenta INT PRIMARY KEY,
                         username VARCHAR(50) UNIQUE NOT NULL,
                         password VARCHAR(50) NOT NULL
);

-- Creación de la tabla USUARIOS
CREATE TABLE USUARIOS (
                          id_usuario INT PRIMARY KEY,
                          nombre VARCHAR(100),
                          telefono VARCHAR(15),
                          email VARCHAR(100),
                          cuenta_id INT,
                          FOREIGN KEY (cuenta_id) REFERENCES CUENTAS(id_cuenta)
);

-- Creación de la tabla ANUNCIOS
CREATE TABLE ANUNCIOS (
                          id_anuncio INT PRIMARY KEY,
                          titulo VARCHAR(100),
                          descripcion VARCHAR(255),
                          imagen VARCHAR(100),
                          categoria VARCHAR(50),
                          precio DECIMAL(10, 2),
                          usuario_id INT,
                          FOREIGN KEY (usuario_id) REFERENCES USUARIOS(id_usuario)
);

-- Creación de la tabla VALORACIONES
CREATE TABLE VALORACIONES (
                              id_valoracion INT PRIMARY KEY,
                              estrellas INT,
                              comentario VARCHAR(255),
                              anuncio_id INT,
                              usuario_id INT,
                              FOREIGN KEY (anuncio_id) REFERENCES ANUNCIOS(id_anuncio),
                              FOREIGN KEY (usuario_id) REFERENCES USUARIOS(id_usuario)
);

-- Inserción de datos en la tabla CUENTAS
INSERT INTO CUENTAS (id_cuenta, username, password) VALUES (1, 'johndoe', 'password123');
INSERT INTO CUENTAS (id_cuenta, username, password) VALUES (2, 'janedoe', 'password456');

-- Inserción de datos en la tabla USUARIOS
INSERT INTO USUARIOS (id_usuario, nombre, telefono, email, cuenta_id)
VALUES (1, 'Juan Perez', '123456789', 'juan@example.com', 1);
INSERT INTO USUARIOS (id_usuario, nombre, telefono, email, cuenta_id)
VALUES (2, 'Ana Gomez', '987654321', 'ana@example.com', 2);
