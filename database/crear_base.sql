-- SMAact4_t4: base de datos
-- En local NO es obligatorio (la URL tiene createDatabaseIfNotExist=true),
-- pero en el VPS si se crea a mano junto con su usuario.
CREATE DATABASE IF NOT EXISTS smaact4_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- Las tablas (usuarios, carreras, alumnos) las crea Hibernate al arrancar la app.

-- Consultas utiles para revisar (MySQL Workbench):
-- USE smaact4_db;
-- SELECT id, nombre, email, password, rol FROM usuarios;   -- el password se ve como hash BCrypt ($2a$10$...)
-- SELECT * FROM carreras;
-- SELECT a.id, a.numero_control, a.nombre, a.apellidos, c.clave, c.nombre AS carrera
--   FROM alumnos a JOIN carreras c ON a.carrera_id = c.id;
