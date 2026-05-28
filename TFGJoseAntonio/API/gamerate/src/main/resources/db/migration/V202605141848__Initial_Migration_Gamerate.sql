CREATE TABLE desarrolladores
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(100)          NOT NULL,
    pais   VARCHAR(255)          NULL,
    CONSTRAINT pk_desarrolladores PRIMARY KEY (id)
);

CREATE TABLE generos
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_generos PRIMARY KEY (id)
);

CREATE TABLE juego_generos
(
    genero_id BIGINT NOT NULL,
    juego_id  BIGINT NOT NULL,
    CONSTRAINT pk_juego_generos PRIMARY KEY (genero_id, juego_id)
);

CREATE TABLE juego_plataformas
(
    juego_id      BIGINT NOT NULL,
    plataforma_id BIGINT NOT NULL,
    CONSTRAINT pk_juego_plataformas PRIMARY KEY (juego_id, plataforma_id)
);

CREATE TABLE juegos
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    titulo            VARCHAR(150)          NOT NULL,
    descripcion       TEXT                  NULL,
    fecha_lanzamiento date                  NULL,
    imagen_url        VARCHAR(255)          NULL,
    desarrollador_id  BIGINT                NULL,
    CONSTRAINT pk_juegos PRIMARY KEY (id)
);

CREATE TABLE plataformas
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(50)           NOT NULL,
    CONSTRAINT pk_plataformas PRIMARY KEY (id)
);

CREATE TABLE reviews
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NOT NULL,
    game_id    BIGINT                NOT NULL,
    score      DECIMAL(3, 1)         NULL,
    comment    TEXT                  NULL,
    created_at datetime              NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (id)
);

CREATE TABLE usuarios
(
    id             BIGINT AUTO_INCREMENT  NOT NULL,
    nombre_usuario VARCHAR(50)            NOT NULL,
    email          VARCHAR(100)           NOT NULL,
    password       VARCHAR(255)           NOT NULL,
    rol            ENUM ('USER', 'ADMIN') NULL,
    fecha_creacion datetime               NULL,
    CONSTRAINT pk_usuarios PRIMARY KEY (id)
);

ALTER TABLE reviews
    ADD CONSTRAINT uc_8154d3d100416df8152a5dcf1 UNIQUE (user_id, game_id);

ALTER TABLE desarrolladores
    ADD CONSTRAINT uc_desarrolladores_nombre UNIQUE (nombre);

ALTER TABLE generos
    ADD CONSTRAINT uc_generos_nombre UNIQUE (nombre);

ALTER TABLE plataformas
    ADD CONSTRAINT uc_plataformas_nombre UNIQUE (nombre);

ALTER TABLE usuarios
    ADD CONSTRAINT uc_usuarios_email UNIQUE (email);

ALTER TABLE usuarios
    ADD CONSTRAINT uc_usuarios_nombre_usuario UNIQUE (nombre_usuario);

ALTER TABLE juegos
    ADD CONSTRAINT FK_JUEGOS_ON_DESARROLLADOR FOREIGN KEY (desarrollador_id) REFERENCES desarrolladores (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_GAME FOREIGN KEY (game_id) REFERENCES juegos (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_USER FOREIGN KEY (user_id) REFERENCES usuarios (id);

ALTER TABLE juego_generos
    ADD CONSTRAINT fk_juegen_on_genero FOREIGN KEY (genero_id) REFERENCES generos (id);

ALTER TABLE juego_generos
    ADD CONSTRAINT fk_juegen_on_juego FOREIGN KEY (juego_id) REFERENCES juegos (id);

ALTER TABLE juego_plataformas
    ADD CONSTRAINT fk_juepla_on_juego FOREIGN KEY (juego_id) REFERENCES juegos (id);

ALTER TABLE juego_plataformas
    ADD CONSTRAINT fk_juepla_on_plataforma FOREIGN KEY (plataforma_id) REFERENCES plataformas (id);