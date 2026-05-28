-- =============================================================================
-- 1. INSERTAR DESARROLLADORES
-- =============================================================================
INSERT INTO desarrolladores (nombre, pais) VALUES
                                               ('Nintendo', 'Japón'),
                                               ('Rockstar Games', 'EEUU'),
                                               ('FromSoftware', 'Japón'),
                                               ('CD Projekt Red', 'Polonia');

-- =============================================================================
-- 2. INSERTAR GÉNEROS
-- =============================================================================
INSERT INTO generos (nombre) VALUES
                                 ('Acción'),
                                 ('Aventura'),
                                 ('RPG'),
                                 ('Souls-like'),
                                 ('Mundo Abierto');

-- =============================================================================
-- 3. INSERTAR JUEGOS
-- Nota: El ID del desarrollador debe existir en la tabla 'desarrolladores'
-- =============================================================================
INSERT INTO juegos (titulo, descripcion, desarrollador_id) VALUES
                                                               ('The Legend of Zelda: Breath of the Wild', 'Exploración libre en el reino de Hyrule.', 1),
                                                               ('Elden Ring', 'Un viaje épico por las Tierras Intermedias.', 3),
                                                               ('Red Dead Redemption 2', 'La vida de Arthur Morgan en el salvaje oeste.', 2),
                                                               ('Cyberpunk 2077', 'RPG de acción ambientado en Night City.', 4);

-- =============================================================================
-- 4. RELACIONAR JUEGOS CON GÉNEROS (Tabla intermedia)
-- =============================================================================
-- Zelda (1) es Acción (1), Aventura (2) y Mundo Abierto (5)
INSERT INTO juego_generos (juego_id, genero_id) VALUES (1, 1), (1, 2), (1, 5);

-- Elden Ring (2) es RPG (3) y Souls-like (4)
INSERT INTO juego_generos (juego_id, genero_id) VALUES (2, 3), (2, 4);

-- RDR2 (3) es Acción (1) y Mundo Abierto (5)
INSERT INTO juego_generos (juego_id, genero_id) VALUES (3, 1), (3, 5);

-- =============================================================================
-- 5. INSERTAR USUARIO PARA PRUEBAS (Login)
-- =============================================================================
-- IMPORTANTE: Por ahora la password va en texto plano.
-- Cuando implementemos BCrypt (Seguridad), este insert cambiará.
INSERT INTO usuarios (nombre_usuario, email, password, rol) VALUES
                                                                ('admin', 'admin@gamerate.com', '1234', 'ADMIN'),
                                                                ('usuario1', 'user@gamerate.com', 'pass123', 'USER');