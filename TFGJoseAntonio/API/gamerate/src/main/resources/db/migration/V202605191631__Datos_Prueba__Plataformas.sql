-- 1. INSERTAR PLATAFORMAS (Solo la tabla nueva)
INSERT INTO plataformas (nombre) VALUES
                                     ('PC'),
                                     ('PlayStation 5'),
                                     ('Xbox Series X'),
                                     ('Nintendo Switch');

-- 2. RELACIONAR JUEGOS EXISTENTES CON PLATAFORMAS
-- Zelda (id=1) en Switch (id=4)
INSERT INTO juego_plataformas (juego_id, plataforma_id) VALUES (1, 4);

-- Elden Ring (id=2) en PC (1), PS5 (2), Xbox (3)
INSERT INTO juego_plataformas (juego_id, plataforma_id) VALUES (2, 1), (2, 2), (2, 3);

-- RDR2 (3) en PC (1), PS5 (2)
INSERT INTO juego_plataformas (juego_id, plataforma_id) VALUES (3, 1), (3, 2);

-- Cyberpunk (4) en PC (1)
INSERT INTO juego_plataformas (juego_id, plataforma_id) VALUES (4, 1);