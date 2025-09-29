-- Datos para carga inicial de la base de datos

-- Para si2024_pl34.tkrun:
BEGIN TRANSACTION;

-- Insertar datos en la tabla colegiado (antes tabla persona)
-- Insertar una persona que sea colegiado
INSERT INTO colegiado (numero_colegiado, nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, estado) 
VALUES ('1', 'Colegiado_inicial_prueba', 'García', '12345678A', 'Calle Mayor 1', '1980-01-01', 'ES1234567890123456789012', 'Ingeniero aeroespacial', 'colegiado_inicial@ejemplo.com', '600111111', 'Colegiado');

-- Insertar un pre-colegiado
INSERT INTO colegiado (numero_colegiado, nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, estado) 
VALUES ('2', 'Pre-colegiado_inicial prueba', 'López', '87654321B', 'Calle Menor 2', '1985-02-02', 'ES9876543210987654321098', 'Ingeniera industrial', 'precolegiado_inicial@ejemplo.com', '600222222', 'Pre-colegiado');

-- Insertar un peritos
INSERT INTO colegiado (numero_colegiado, nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion,email,telefono, estado) 
VALUES ('3', 'Perito_inicial prueba', 'Martínez', '11111111C', 'Calle Mediana 3', '1990-03-03', 'ES1111111110111111111011', 'Perito','peritoinicial@ejemplo.com','600000001', 'Perito');
INSERT INTO colegiado (numero_colegiado, nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion,email,telefono, estado) 
VALUES ('4', 'Perito1', 'Perez', '11111111A', 'Calle Mediana 15', '1990-08-23', 'ES1111111110111111111000', 'Perito','perito1@ejemplo.com','600110011', 'Perito');
INSERT INTO colegiado (numero_colegiado, nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion,email,telefono, estado) 
VALUES ('5', 'Perito2', 'Ramirez', '11111222A', 'Calle Calle 12', '1980-04-11', 'ES1111111110000111111000', 'Perito','perito2@ejemplo.com','603300001', 'Perito');

-- Insertar una persona que no sea colegiado, pre-colegiado ni perito
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, estado) 
VALUES ('Persona_inicial otros', 'Sánchez', '22222222D', 'Calle Pequeña 4', '1995-04-04', 'ES2222222220222222222022', 'Otro');

-- Insertar persona solicitando colegiación
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, fecha_solicitud_colegiacion, estado) 
VALUES ('Persona_inicial_solicitando_colegiacion', 'Otro', '33333333E', 'Calle Otro 5', '2000-05-05', 'ES3333333330333333333033', 'Ingeniero en informática', 'solicitante_colegiacion@ejemplo.com', '600444444', '2023-01-15', 'Pendiente');

-- Insertar persona solicitando colegiación
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, fecha_solicitud_colegiacion, estado) 
VALUES ('Persona_inicial_solicitando_colegiacion2', 'Otro', '71779257A', 'Calle Otro 5', '2001-05-05', 'ES3333323438393373633033', 'Ingeniero en informática', 'solicitante_colegiacion@ejemplo.com', '689035081', '2023-02-15', 'Pendiente');

-- Insertar persona solicitando colegiación
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, fecha_solicitud_colegiacion, estado) 
VALUES ('Persona_inicial_solicitando_colegiacio3', 'Otro', '71779257B', 'Calle Otro 5', '2002-05-05', 'ES3345678360333133333033', 'Máster en Ingeniería Informática', 'solicitante_colegiacion2@ejemplo.com', '689035082', '2023-03-15', 'Pendiente');

-- Insertar persona solicitando colegiación
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, fecha_solicitud_colegiacion, estado) 
VALUES ('Persona_inicial_solicitando_colegiacion4', 'Otro', '71779257C', 'Calle Otro 5', '2003-05-05', 'ES3333333330333684673890', 'Licenciatura en Informática', 'solicitante_colegiacion3@ejemplo.com', '689035083', '2023-04-15', 'Pendiente');

-- Insertar persona solicitando colegiación
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, fecha_solicitud_colegiacion, estado) 
VALUES ('Persona_inicial_solicitando_colegiacion5', 'Otro', '71779257D', 'Calle Otro 5', '2003-06-06', 'ES3333456330333684673890', 'Licenciatura en Derecho', 'solicitante_colegiacion4@ejemplo.com', '689035084', '2023-05-15', 'Pendiente');



-- Persona solicitando ser perito
INSERT INTO colegiado (nombre, apellidos, dni, direccion, fecha_nacimiento, numero_cuenta, titulacion, email, telefono, estado) 
VALUES ('Persona_inicial_solicitando_perito', 'Otra', '55555555G', 'Calle Otra 7', '2010-07-07', 'ES5555555550555555555055', 'Ingeniero mecánico', 'solicitante_perito@ejemplo.com', '600555555', 'Colegiado');

-- Insertar personas para inscribirse en cursos

-- Insertar datos en la tabla solicitudes_peritos
INSERT INTO solicitudes_peritos (año_realizacion_curso, estado, fecha_solicitud, colegiado_id) 
VALUES (2019, 'Pendiente', '2020-07-07', 6);

-- Insertar datos en la tabla cursos
INSERT INTO cursos (titulo, descripcion, fecha_inicio, fecha_fin, duracion_horas, max_plazas, estado) 
VALUES ('Curso de Periciales Informáticas', 'Curso avanzado de periciales informáticas', '2025-04-11', '2025-04-30', 40, 30, 'Abierto');
INSERT INTO cursos (titulo, descripcion, fecha_inicio, fecha_fin, duracion_horas, max_plazas, estado) 
VALUES ('Curso de Seguridad Informática', 'Curso básico de seguridad informática', '2025-04-01', '2025-04-30', 30, 25, 'Planificado');
INSERT INTO cursos (titulo, descripcion, fecha_inicio, fecha_fin, duracion_horas, max_plazas, estado) 
VALUES ('Curso de Desarrollo Web', 'Curso intermedio de desarrollo web', '2025-05-01', '2025-05-31', 50, 20, 'Planificado');
INSERT INTO cursos (titulo, descripcion, fecha_inicio, fecha_fin, duracion_horas, max_plazas, estado) 
VALUES ('Curso de Inteligencia Artificial', 'Curso introductorio de inteligencia artificial', '2025-06-01', '2025-06-30', 60, 15, 'Planificado');
INSERT INTO cursos (titulo, descripcion, fecha_inicio, fecha_fin, duracion_horas, max_plazas, estado) 
VALUES ('Curso de Prueba 1', 'Curso prueba para colectivos todos los colectivos', '2025-06-01', '2025-06-30', 60, 20, 'Abierto');
INSERT INTO cursos (titulo, descripcion, fecha_inicio, fecha_fin, duracion_horas, max_plazas, estado) 
VALUES ('Curso de Prueba 2', 'Curso prueba para peritos y colegiados', '2025-06-01', '2025-06-30', 60, 15, 'Abierto');

-- Insertar datos en la tabla plazos_inscripciones_curso
INSERT INTO plazos_inscripciones_curso (id_curso, fecha_inicio, fecha_fin)
VALUES (1, '2025-02-20', '2025-03-28');
INSERT INTO plazos_inscripciones_curso (id_curso, fecha_inicio, fecha_fin)
VALUES (2, '2025-03-20', '2025-03-31');
INSERT INTO plazos_inscripciones_curso (id_curso, fecha_inicio, fecha_fin)
VALUES (3, '2025-04-20', '2025-04-30');
INSERT INTO plazos_inscripciones_curso (id_curso, fecha_inicio, fecha_fin)
VALUES (4, '2025-05-20', '2025-05-31');
INSERT INTO plazos_inscripciones_curso (id_curso, fecha_inicio, fecha_fin)
VALUES (5, '2025-02-20', '2025-05-27');
INSERT INTO plazos_inscripciones_curso (id_curso, fecha_inicio, fecha_fin)
VALUES (6, '2025-02-20', '2025-05-08');

-- Insertar datos en la tabla cuota (antes colectivos_curso)
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (1, 'Colegiado', 100.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (1, 'Pre-colegiado', 150.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (1, 'Perito', 200.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (1, 'Estudiante', 250.0);

INSERT INTO cuota (id_curso, nombre, cuota) VALUES (2, 'Colegiado', 120.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (2, 'Pre-colegiado', 170.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (2, 'Perito', 220.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (2, 'Estudiante', 270.0);

INSERT INTO cuota (id_curso, nombre, cuota) VALUES (3, 'Colegiado', 130.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (3, 'Pre-colegiado', 180.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (3, 'Perito', 230.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (3, 'Jubilado', 280.0);

INSERT INTO cuota (id_curso, nombre, cuota) VALUES (4, 'Colegiado', 140.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (4, 'Pre-colegiado', 190.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (4, 'Perito', 240.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (4, 'Desempleado', 290.0);

INSERT INTO cuota (id_curso, nombre, cuota) VALUES (5, 'Colegiado', 1000.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (5, 'Pre-colegiado', 1500.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (5, 'Perito', 2000.0);

INSERT INTO cuota (id_curso, nombre, cuota) VALUES (6, 'Colegiado', 1200.0);
INSERT INTO cuota (id_curso, nombre, cuota) VALUES (6, 'Perito', 1300.0);

INSERT INTO peritaje (solicitante,fecha_solicitud,descripcion,numero_referencia) VALUES ('Solicitante1','2025-03-12',"Revisar caja negra Falcon",245);
INSERT INTO peritaje (solicitante,fecha_solicitud,descripcion,numero_referencia) VALUES ('Solicitante2','2025-03-14',"Comprobar ordenador narcotraficante",246);
INSERT INTO peritaje (solicitante,fecha_solicitud,descripcion,numero_referencia) VALUES ('Solicitante3','2025-03-14',"Revisar móvil fiscal del estado",247);
INSERT INTO peritaje (solicitante,fecha_solicitud,descripcion,numero_referencia) VALUES ('Solicitante4','2025-03-15',"Revisar discos duros ordenador paquito el chocolatero",252);

-- Insertar un recibo DEVUELTO para el colegiado con ID 1 en el año actual con importe 125.50€
INSERT INTO recibos (id_colegiado, año, estado, importe)
VALUES (1, strftime('%Y','now'), 'DEVUELTO', 125.50);

-- Insertar un recibo PAGADO para el colegiado con ID 2 en el año actual con importe 100.00€
INSERT INTO recibos (id_colegiado, año, estado, importe)
VALUES (2, strftime('%Y','now'), 'PAGADO', 100.00);

-- Insertar 2 recibos EMITIDOS para pruebas de procesamiento
INSERT INTO recibos (id_colegiado, año, estado, importe)
VALUES (3, strftime('%Y','now'), 'EMITIDO', 120.00);
INSERT INTO recibos (id_colegiado, año, estado, importe)
VALUES (4, strftime('%Y','now'), 'EMITIDO', 120.00);

-- Insertar periciales en la tabla peritajes
INSERT INTO peritaje (fecha_solicitud, fecha_realizacion, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-01', NULL, 'Asignado', 'Urgente', 'Revisión de contrato laboral', '101', 3, 'Laura Gómez Martínez');

INSERT INTO peritaje (fecha_solicitud, fecha_realizacion, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-02', NULL, 'Asignado', 'Normal', 'Análisis de impacto medioambiental', '102', 4, 'Carlos Pérez Díaz');

INSERT INTO peritaje (fecha_solicitud, fecha_realizacion, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-04', '2024-04-10', 'Realizado', 'Normal', 'Valoración de daños materiales', '104', 3, 'Ismael Torres López');

INSERT INTO peritaje (fecha_solicitud, fecha_realizacion, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-03-30', '2024-04-03', 'Realizado', 'Urgente', 'Peritaje en accidente de tráfico', '105', 4, 'Ana María González');

INSERT INTO peritaje (fecha_solicitud, fecha_realizacion, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-05', NULL , 'Rechazado', 'Normal', 'Peritaje rechazado prueba', '106', 5, 'Javier Hernández Ortiz');

INSERT INTO peritaje (fecha_solicitud, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-03', 'Asignado', 'Normal', 'Auditoría de sistema de seguridad', '119', 5, 'María López Fernández');

INSERT INTO peritaje (fecha_solicitud, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-04', 'Asignado', 'Urgente', 'Inspección de instalación eléctrica', '175', 3, 'Javier Torres Gutiérrez');

INSERT INTO peritaje (fecha_solicitud, estado, prioridad, descripcion, numero_referencia, colegiado_id, solicitante)
VALUES ('2024-04-05', 'Asignado', 'Normal', 'Valoración de daños en vehículo', '189', 4, 'Ana Ruiz Salas');

-- Insertar pre-inscritos realizando transferencias para inscripción a un curso

INSERT INTO inscripciones_curso (id, id_cuota, id_colegiado, estado, metodo_pago, fecha_inscripcion)
VALUES
(1, 1, 1, 'Pre-inscrito', 'transferencia', '2025-03-01'),
(2, 2, 2, 'Pre-inscrito', 'transferencia', '2025-03-02'),
(3, 3, 3, 'Pre-inscrito', 'transferencia', '2025-03-03'),
(4, 3, 4, 'Pre-inscrito', 'transferencia', '2025-03-01'),
(5, 3, 5, 'Pre-inscrito', 'transferencia', '2025-02-27'),
(6, 17, 13, 'Pre-inscrito', 'transferencia', '2025-03-01'),
(7, 19, 14, 'Pre-inscrito', 'transferencia', '2025-03-02'),
(8, 19, 15, 'Pre-inscrito', 'transferencia', '2025-03-03'), 
(9, 18, 20, 'Pre-inscrito', 'transferencia', '2025-03-04'),
(10, 20, 16, 'Pre-inscrito', 'transferencia', '2025-04-01'),
(11, 21, 17, 'Pre-inscrito', 'transferencia', '2025-04-02'),
(12, 21, 18, 'Pre-inscrito', 'transferencia', '2025-04-03'),
(13, 20, 19, 'Pre-inscrito', 'transferencia', '2025-04-04');

-- Insertar personas para inscribirse en cursos

INSERT INTO colegiado (numero_colegiado, dni, nombre, apellidos, numero_cuenta, estado) VALUES
('13', '22222222A', 'Luis', 'Sánchez', 'ES0000000000000000000013', 'Colegiado'),
('14', '44444444C', 'David', 'Navarro', 'ES0000000000000000000014', 'Perito'),
('15', '55555555D', 'Elena', 'Torres', 'ES0000000000000000000015', 'Perito'),
('16', '77777777F', 'Sofía', 'Romero', 'ES0000000000000000000016', 'Colegiado'),
('17', '99999999H', 'Laura', 'Muñoz', 'ES0000000000000000000017', 'Perito'),
('18', '00000000I', 'Mario', 'Delgado', 'ES0000000000000000000018', 'Perito'),
('19', '22223333K', 'Andrés', 'Morales', 'ES0000000000000000000019', 'Colegiado'),
('20', '33333333B', 'Marta', 'González', 'ES0000000000000000000020', 'Pre-colegiado');

COMMIT;