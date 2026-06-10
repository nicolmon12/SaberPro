DROP TABLE IF EXISTS pagos_saber_pro;
DROP TABLE IF EXISTS resultados_saber_pro;
DROP TABLE IF EXISTS estudiantes;
DROP TABLE IF EXISTS docentes;
DROP TABLE IF EXISTS directores;
DROP TABLE IF EXISTS resoluciones_beneficios;
DROP TABLE IF EXISTS facultades;

CREATE TABLE facultades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    codigo VARCHAR(20) UNIQUE NOT NULL
);

CREATE TABLE directores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) DEFAULT 'ADMIN',
    facultad_id BIGINT,
    FOREIGN KEY (facultad_id) REFERENCES facultades(id)
);

CREATE TABLE docentes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) DEFAULT 'ADMIN',
    facultad_id BIGINT,
    FOREIGN KEY (facultad_id) REFERENCES facultades(id)
);

CREATE TABLE estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento VARCHAR(10) DEFAULT 'CC',
    numero_documento VARCHAR(20) UNIQUE NOT NULL,
    primer_apellido VARCHAR(100) NOT NULL,
    segundo_apellido VARCHAR(100),
    primer_nombre VARCHAR(100) NOT NULL,
    segundo_nombre VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    telefono VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    facultad_id BIGINT,
    semestre INT DEFAULT 1,
    aprobado_saber_pro BOOLEAN DEFAULT FALSE,
    comprobante_pago VARCHAR(255),
    FOREIGN KEY (facultad_id) REFERENCES facultades(id)
);

CREATE TABLE resultados_saber_pro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id BIGINT NOT NULL,
    numero_registro VARCHAR(50) UNIQUE,
    puntaje_total INT,
    puntaje_nivel VARCHAR(20),
    comunicacion_escrita INT,
    comunicacion_escrita_nivel VARCHAR(20),
    razonamiento_cuantitativo INT,
    razonamiento_cuantitativo_nivel VARCHAR(20),
    lectura_critica INT,
    lectura_critica_nivel VARCHAR(20),
    competencias_ciudadanas INT,
    competencias_ciudadanas_nivel VARCHAR(20),
    ingles INT,
    ingles_nivel VARCHAR(20),
    nivel_ingles_general VARCHAR(5),
    tipo_examen VARCHAR(10) DEFAULT 'TOTAL'  COMMENT 'UNICO=solo mañana, TOTAL=mañana+tarde',
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE
);

CREATE TABLE resoluciones_beneficios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_programa VARCHAR(20) NOT NULL,
    puntaje_minimo INT NOT NULL,
    puntaje_maximo INT,
    beneficio TEXT NOT NULL,
    descripcion TEXT
);

CREATE TABLE pagos_saber_pro (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estudiante_id BIGINT NOT NULL,
    fecha_pago DATE NOT NULL,
    comprobante_url VARCHAR(255),
    estado VARCHAR(20) DEFAULT 'PENDIENTE',
    observaciones TEXT,
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE
);

-- ─────────────────────────────────────────────────────────────
-- DATOS BASE
-- ─────────────────────────────────────────────────────────────
INSERT INTO facultades (nombre, codigo) VALUES 
('Ingeniería de Sistemas', 'SIS'),
('Ingeniería Industrial', 'IND'),
('Diseño de Software', 'DSW'),
('Formulación', 'FORM');

-- password = 'admin123' bcrypt
INSERT INTO directores (nombre, apellido, email, password, rol, facultad_id) VALUES
('Admin', 'Sistema', 'admin@saberpro.edu', '$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq', 'ADMIN', 1),
('Coordinador', 'Académico', 'coordinador@saberpro.edu', '$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq', 'COORDINADOR', 1);

INSERT INTO docentes (nombre, apellido, cedula, email, password, facultad_id) VALUES
('Carlos', 'Mendoza', '12345678', 'docente@saberpro.edu', '$2a$10$zbremxMkOGxqGYJtOBzUvOZPCR8nc0uz73qfHO16o9PEyRMuTl8gK', 1);

-- ─────────────────────────────────────────────────────────────
-- 36 ESTUDIANTES (password = número_documento bcrypt)
-- ─────────────────────────────────────────────────────────────
INSERT INTO estudiantes (tipo_documento, numero_documento, primer_apellido, segundo_apellido, primer_nombre, segundo_nombre, email, password, facultad_id, semestre) VALUES
('CC','1001','BARBOSA','','JHON','','barbosa@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1002','QUINTERO','','CARLOS','','quintero@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1003','PARRA','','ANDRES','','parra@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1004','RINCON','','JORGE','','rincon@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1005','FLOREZ','','YESIKA','','florez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1006','HERNANDEZ','','WILSON','','hernandez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1007','MALAGON','','ERIKA','','malagon@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1008','MORA','','LUIS','','mora@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1009','DURAN','','KEVIN','','duran@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1010','RODRIGUEZ','','DIANA','','rodriguez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1011','GARCIA','','MIGUEL','','garcia@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1012','LOPEZ','','ALEJANDRA','','lopez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1013','MARTINEZ','','NICOLAS','','martinez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1014','SANCHEZ','','PAULA','','sanchez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1015','TORRES','','JULIAN','','torres@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1016','PEREZ','','VALENTINA','','perez@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1017','PINZON','','CAMILO','','pinzon@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1018','JAIMES','','SERGIO','','jaimes@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1019','NINO','','LAURA','','nino@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1020','FABIAN','','OSCAR','','fabian@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1021','HERNANDEZ','','ANDREA','','hernandez2@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1022','LARIOS','','DANIEL','','larios@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1023','CALDERON','','JESSICA','','calderon@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1024','VILLARREAL','','JUAN','','villarreal@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1025','RESTREPO','','MANUELA','','restrepo@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1026','CACERES','','FELIPE','','caceres@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',3,10),
('CC','1027','TABARES','','ISABELLA','','tabares@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',4,10),
('CC','1028','NARANJO','','SAMUEL','','naranjo@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',4,10),
('CC','1029','PRADA','','MARIA','','prada@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',4,10),
('CC','1030','VARGAS','','DAVID','','vargas@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',4,10),
('CC','1031','TORRES','','NATALIA','','torres2@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',4,10),
('CC','1032','ORTIZ','','ESTEBAN','','ortiz@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',4,10),
('CC','1033','VILLAMIZAR','','CAROLINA','','villamizar@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1034','RESTREPO','','SANTIAGO','','restrepo2@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',1,10),
('CC','1035','HIGUERA','','ANDREA','','higuera@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10),
('CC','1036','MATIZ','','ROBERTO','','matiz@university.edu','$2a$10$DDbMlBFuHnVc5eMYD6QlxuqitUzjCDEkXa1TVvLLcKBcKUV6gMLhq',2,10);

-- ─────────────────────────────────────────────────────────────
-- RESULTADOS (36 estudiantes, con nivel por área)
-- Higuera (id=35) y Matiz (id=36) → puntaje_nivel = 'ANULADO'
-- ─────────────────────────────────────────────────────────────
INSERT INTO resultados_saber_pro (estudiante_id, numero_registro, puntaje_total, puntaje_nivel, comunicacion_escrita, comunicacion_escrita_nivel, razonamiento_cuantitativo, razonamiento_cuantitativo_nivel, lectura_critica, lectura_critica_nivel, competencias_ciudadanas, competencias_ciudadanas_nivel, ingles, ingles_nivel, nivel_ingles_general) VALUES
(1,'EK20183007722',200,'Nivel 4',128,'Nivel 3',182,'Nivel 3',202,'Nivel 4',206,'Nivel 4',183,'Nivel 3','B1'),
(2,'EK20183140703',165,'Nivel 3',125,'Nivel 2',151,'Nivel 2',179,'Nivel 3',163,'Nivel 3',205,'Nivel 4','B2'),
(3,'EK20183040545',164,'Nivel 3',159,'Nivel 3',172,'Nivel 3',182,'Nivel 3',142,'Nivel 2',165,'Nivel 3','A2'),
(4,'EK20183040100',155,'Nivel 3',140,'Nivel 3',160,'Nivel 3',170,'Nivel 3',155,'Nivel 3',148,'Nivel 2','A2'),
(5,'EK20183040200',148,'Nivel 2',132,'Nivel 3',145,'Nivel 2',160,'Nivel 3',148,'Nivel 2',140,'Nivel 2','A1'),
(6,'EK20183040300',172,'Nivel 3',148,'Nivel 3',175,'Nivel 3',180,'Nivel 3',168,'Nivel 3',170,'Nivel 3','B1'),
(7,'EK20183040400',190,'Nivel 4',155,'Nivel 3',195,'Nivel 4',188,'Nivel 4',185,'Nivel 4',175,'Nivel 3','B1'),
(8,'EK20183040500',143,'Nivel 2',130,'Nivel 3',138,'Nivel 2',150,'Nivel 2',145,'Nivel 2',135,'Nivel 2','A1'),
(9,'EK20183040600',161,'Nivel 3',145,'Nivel 3',158,'Nivel 3',168,'Nivel 3',160,'Nivel 3',152,'Nivel 3','A2'),
(10,'EK20183040700',178,'Nivel 3',150,'Nivel 3',182,'Nivel 3',175,'Nivel 3',175,'Nivel 3',168,'Nivel 3','B1'),
(11,'EK20183040800',135,'Nivel 2',122,'Nivel 2',130,'Nivel 2',140,'Nivel 2',138,'Nivel 2',128,'Nivel 2','A1'),
(12,'EK20183040900',205,'Nivel 4',160,'Nivel 3',210,'Nivel 4',198,'Nivel 4',200,'Nivel 4',192,'Nivel 4','B2'),
(13,'EK20183041000',158,'Nivel 3',142,'Nivel 3',155,'Nivel 3',165,'Nivel 3',158,'Nivel 3',148,'Nivel 2','A2'),
(14,'EK20183041100',168,'Nivel 3',148,'Nivel 3',170,'Nivel 3',172,'Nivel 3',165,'Nivel 3',160,'Nivel 3','B1'),
(15,'EK20183041200',195,'Nivel 4',158,'Nivel 3',200,'Nivel 4',190,'Nivel 4',188,'Nivel 4',178,'Nivel 3','B1'),
(16,'EK20183041300',152,'Nivel 3',138,'Nivel 3',148,'Nivel 2',160,'Nivel 3',152,'Nivel 3',142,'Nivel 2','A2'),
(17,'EK20183041400',183,'Nivel 4',152,'Nivel 3',188,'Nivel 4',180,'Nivel 3',178,'Nivel 3',172,'Nivel 3','B1'),
(18,'EK20183041500',145,'Nivel 2',132,'Nivel 3',140,'Nivel 2',152,'Nivel 3',145,'Nivel 2',138,'Nivel 2','A1'),
(19,'EK20183041600',170,'Nivel 3',148,'Nivel 3',172,'Nivel 3',175,'Nivel 3',168,'Nivel 3',162,'Nivel 3','B1'),
(20,'EK20183041700',187,'Nivel 4',155,'Nivel 3',192,'Nivel 4',185,'Nivel 4',182,'Nivel 4',175,'Nivel 3','B1'),
(21,'EK20183041800',162,'Nivel 3',145,'Nivel 3',160,'Nivel 3',168,'Nivel 3',160,'Nivel 3',153,'Nivel 3','A2'),
(22,'EK20183041900',140,'Nivel 2',128,'Nivel 2',135,'Nivel 2',148,'Nivel 2',140,'Nivel 2',132,'Nivel 2','A1'),
(23,'EK20183042000',175,'Nivel 3',150,'Nivel 3',178,'Nivel 3',180,'Nivel 3',172,'Nivel 3',165,'Nivel 3','B1'),
(24,'EK20183042100',198,'Nivel 4',160,'Nivel 3',202,'Nivel 4',195,'Nivel 4',192,'Nivel 4',185,'Nivel 4','B2'),
(25,'EK20183042200',155,'Nivel 3',140,'Nivel 3',152,'Nivel 3',162,'Nivel 3',155,'Nivel 3',145,'Nivel 2','A2'),
(26,'EK20183042300',169,'Nivel 3',148,'Nivel 3',168,'Nivel 3',175,'Nivel 3',165,'Nivel 3',158,'Nivel 3','B1'),
(27,'EK20183042400',185,'Nivel 4',155,'Nivel 3',190,'Nivel 4',182,'Nivel 4',180,'Nivel 4',172,'Nivel 3','B1'),
(28,'EK20183042500',148,'Nivel 2',135,'Nivel 3',142,'Nivel 2',155,'Nivel 3',148,'Nivel 2',138,'Nivel 2','A1'),
(29,'EK20183042600',172,'Nivel 3',150,'Nivel 3',175,'Nivel 3',178,'Nivel 3',170,'Nivel 3',162,'Nivel 3','B1'),
(30,'EK20183042700',160,'Nivel 3',142,'Nivel 3',158,'Nivel 3',165,'Nivel 3',160,'Nivel 3',150,'Nivel 2','A2'),
(31,'EK20183042800',192,'Nivel 4',158,'Nivel 3',198,'Nivel 4',188,'Nivel 4',185,'Nivel 4',178,'Nivel 3','B1'),
(32,'EK20183042900',138,'Nivel 2',125,'Nivel 2',132,'Nivel 2',145,'Nivel 2',138,'Nivel 2',128,'Nivel 2','A1'),
(33,'EK20183043000',178,'Nivel 3',152,'Nivel 3',180,'Nivel 3',182,'Nivel 3',175,'Nivel 3',168,'Nivel 3','B1'),
(34,'EK20183043100',165,'Nivel 3',145,'Nivel 3',162,'Nivel 3',170,'Nivel 3',162,'Nivel 3',155,'Nivel 3','A2'),
(35,'EK20183043200',0,'ANULADO',0,'ANULADO',0,'ANULADO',0,'ANULADO',0,'ANULADO',0,'ANULADO','N/A'),
(36,'EK20183043300',0,'ANULADO',0,'ANULADO',0,'ANULADO',0,'ANULADO',0,'ANULADO',0,'ANULADO','N/A');

-- ─────────────────────────────────────────────────────────────
-- RESOLUCIONES
-- ─────────────────────────────────────────────────────────────
INSERT INTO resoluciones_beneficios (tipo_programa, puntaje_minimo, puntaje_maximo, beneficio, descripcion) VALUES
('INGENIERIA', 0,   119, 'Repetir Saber Pro',                              'Debe presentar nuevamente el examen Saber Pro'),
('INGENIERIA', 120, 149, 'Proyecto de Grado + 25% derechos de grado',      'Realizar proyecto de grado y paga 75% de derechos de grado'),
('INGENIERIA', 150, 199, 'Proyecto de Grado + 50% derechos de grado',      'Realizar proyecto de grado y paga 50% de derechos de grado'),
('INGENIERIA', 200, 300, 'Proyecto de Grado aprobado + 100% derechos de grado', 'Beneficio total - exención de derechos de grado'),
('TECNOLOGIA', 0,    89, 'Repetir Saber Pro',                              'Debe presentar nuevamente el examen Saber Pro'),
('TECNOLOGIA', 90,  119, 'Proyecto de Grado + 25% derechos de grado',      'Realizar proyecto de grado'),
('TECNOLOGIA', 120, 200, 'Proyecto de Grado + 50% derechos de grado',      'Beneficio parcial'),
('TECNOLOGIA', 201, 300, 'Proyecto de Grado aprobado + 100% derechos de grado', 'Beneficio total');