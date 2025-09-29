--Primero se deben borrar todas las tablas (de detalle a maestro) y lugo anyadirlas (de maestro a detalle)
-- Para las fechas se usará el formato ISO 8601 (YYYY-MM-DD). O en caso de que se necesite la hora, el formato ISO 8601 extendido (YYYY-MM-DD HH:MM:SS).
--Para si2024_pl34.tkrun:
BEGIN TRANSACTION;

-- Eliminar tablas en orden inverso a sus dependencias
DROP TABLE IF EXISTS colectivos_curso;

DROP TABLE IF EXISTS colectivos;

DROP TABLE IF EXISTS inscripciones_curso;

DROP TABLE IF EXISTS plazos_inscripciones_curso;

DROP TABLE IF EXISTS cursos;

DROP TABLE IF EXISTS solicitudes_peritos;

DROP TABLE IF EXISTS persona;

DROP TABLE IF EXISTS solicitudes_colegiacion;

DROP TABLE IF EXISTS colegiado;

DROP TABLE IF EXISTS peritaje;

DROP TABLE IF EXISTS calendario_curso;

DROP TABLE IF EXISTS cuota;


DROP TABLE IF EXISTS recibos;

-- Tabla para almacenar colegiados
CREATE TABLE
  IF NOT EXISTS colegiado (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_colegiado TEXT,
    nombre TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    dni TEXT NOT NULL UNIQUE,
    direccion TEXT,
    fecha_nacimiento TEXT,
    numero_cuenta TEXT,
    titulacion TEXT,
    email TEXT DEFAULT NULL,
    telefono TEXT DEFAULT NULL,
    fecha_ultimo_peritaje TEXT,
    fecha_solicitud_colegiacion TEXT,
    estado TEXT,
    motivo_rechazo_colegiazion TEXT
  );

-- Tabla para solicitudes de inscripción en listas de peritos privados
CREATE TABLE
  IF NOT EXISTS solicitudes_peritos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    año_realizacion_curso INTEGER,
    estado TEXT NOT NULL DEFAULT 'Pendiente',
    fecha_solicitud TEXT NOT NULL,
    colegiado_id INTEGER NOT NULL,
    FOREIGN KEY (colegiado_id) REFERENCES colegiado (id)
  );

-- Tabla para cursos de formación
CREATE TABLE
  IF NOT EXISTS cursos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    descripcion TEXT,
    fecha_inicio TEXT,
    fecha_fin TEXT,
    duracion_horas INTEGER,
    max_plazas INTEGER,
    estado TEXT NOT NULL DEFAULT 'Planificado',
    permite_cancelacion BOOLEAN DEFAULT 0,
    fecha_limite_cancelacion TEXT,
    porcentaje_devolucion INTEGER DEFAULT 0,
    permite_lista_espera BOOLEAN DEFAULT 0
  );

-- Tabla para plazos de inscripciones a cursos
CREATE TABLE
  IF NOT EXISTS plazos_inscripciones_curso (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_curso INTEGER NOT NULL,
    fecha_inicio TEXT NOT NULL,
    fecha_fin TEXT NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES cursos (id)
  );

-- Tabla para inscripciones a cursos
CREATE TABLE
  IF NOT EXISTS inscripciones_curso (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_cuota INTEGER NOT NULL,
    id_colegiado INTEGER NOT NULL,
    estado TEXT NOT NULL DEFAULT 'pre-inscrito',
    metodo_pago TEXT,
    incidencia_pago TEXT,
    fecha_inscripcion TEXT NOT NULL,
    fecha_pago TEXT,
    cantidad_pagada REAL DEFAULT 0,
    fecha_cancelacion TEXT,
    cantidad_devolucion REAL DEFAULT 0,
    n_lista_espera INTEGER,
    FOREIGN KEY (id_cuota) REFERENCES cuota (id),
    FOREIGN KEY (id_colegiado) REFERENCES colegiado (id)
  );

-- Tabla para peritajes
CREATE TABLE
  IF NOT EXISTS peritaje (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fecha_realizacion TEXT,
    solicitante TEXT,
    fecha_solicitud TEXT NOT NULL,
    estado TEXT NOT NULL DEFAULT 'Pendiente',
    prioridad TEXT NOT NULL DEFAULT 'Normal',
    descripcion TEXT,
    numero_referencia TEXT UNIQUE NOT NULL,
    fecha_rechazo TEXT,
    motivo_rechazo TEXT,
    colegiado_id INTEGER,
    FOREIGN KEY (colegiado_id) REFERENCES colegiado (id)
  );

-- Tabla para calendario de cursos
CREATE TABLE
  IF NOT EXISTS calendario_curso (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_curso INTEGER NOT NULL,
    fecha_inicio TEXT NOT NULL,
    fecha_fin TEXT NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES cursos (id)
  );

-- Tabla para cuotas
CREATE TABLE
  IF NOT EXISTS cuota (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_curso INTEGER NOT NULL,
    nombre TEXT NOT NULL,
    cuota REAL NOT NULL,
    FOREIGN KEY (id_curso) REFERENCES cursos (id)
  );

CREATE TABLE
 IF NOT EXISTS recibos (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  id_colegiado INTEGER NOT NULL,
  año INTEGER NOT NULL,
  estado TEXT NOT NULL CHECK (estado IN ('EMITIDO','PAGADO', 'DEVUELTO')),
  importe REAL NOT NULL DEFAULT 0,
  fecha_emision TEXT NOT NULL DEFAULT (DATE('now')),
  incidencia TEXT,
  FOREIGN KEY (id_colegiado) REFERENCES colegiado(id)
);

COMMIT;