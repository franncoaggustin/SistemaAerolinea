package com.aerolinea.SistemaAerolinea;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del Sistema de Aerolínea.
 * Punto de entrada de la aplicación Spring Boot.
 * Inicia el servidor embebido Tomcat en el puerto 8080.
 */
@SpringBootApplication
public class SistemaAerolineaApplication {

	/**
	 * Método principal que arranca la aplicación Spring Boot.
	 * @param args argumentos de línea de comandos.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SistemaAerolineaApplication.class, args);
	}
}