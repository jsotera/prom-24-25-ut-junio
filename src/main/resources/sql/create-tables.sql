CREATE TABLE `prom_junio`.`editor_mapa` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `fila` INT NOT NULL,
  `columna` INT NOT NULL,
  `sprite` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id`))
COMMENT = '	';


CREATE TABLE `prom_junio`.`mapas` (
  `nombre` VARCHAR(45) NOT NULL,
  `filas` INT NOT NULL,
  `columnas` INT NOT NULL,
  PRIMARY KEY (`nombre`));