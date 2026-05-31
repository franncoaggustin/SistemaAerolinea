package com.aerolinea.SistemaAerolinea.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración global de CORS para el sistema.
 * Permite que el frontend HTML/CSS/JS pueda consumir los endpoints
 * de la API REST desde el navegador sin errores de seguridad.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Habilita CORS para todos los endpoints de la API.
     * Permite peticiones desde cualquier origen con los métodos HTTP necesarios.
     * @param registry el registro de configuración CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}