package com.aerolinea.SistemaAerolinea.config;

import com.aerolinea.SistemaAerolinea.model.*;
import com.aerolinea.SistemaAerolinea.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Carga de datos iniciales al arrancar la aplicación.
 * Se ejecuta automáticamente una sola vez gracias a CommandLineRunner.
 * El orden respeta las dependencias entre entidades.
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private TipoTarjetaRepository tipoTarjetaRepository;
    @Autowired private ClaseRepository claseRepository;
    @Autowired private CiudadRepository ciudadRepository;
    @Autowired private AerolineaRepository aerolineaRepository;
    @Autowired private AeropuertoRepository aeropuertoRepository;
    @Autowired private PersonaRepository personaRepository;
    @Autowired private PilotoRepository pilotoRepository;
    @Autowired private AvionRepository avionRepository;
    @Autowired private AsientoRepository asientoRepository;
    @Autowired private TarjetaRepository tarjetaRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private VueloRepository vueloRepository;
    @Autowired private TarifaRepository tarifaRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private ReservaRepository reservaRepository;
    @Autowired private ConsultaRepository consultaRepository;

    /**
     * Método principal ejecutado al iniciar la aplicación.
     * Solo carga datos si la base de datos está vacía.
     * @param args argumentos de línea de comandos.
     */
    @Override
    public void run(String... args) {
        if (tipoTarjetaRepository.count() > 0) {
            return;
        }

        // 1. TipoTarjeta
        TipoTarjeta debito  = tipoTarjetaRepository.save(new TipoTarjeta(null, "DEBITO",  null));
        TipoTarjeta credito = tipoTarjetaRepository.save(new TipoTarjeta(null, "CREDITO", null));

        // 2. Clase
        Clase economy  = claseRepository.save(new Clase(null, "ECONOMY",  null, null));
        Clase turista  = claseRepository.save(new Clase(null, "TURISTA",  null, null));
        Clase business = claseRepository.save(new Clase(null, "BUSINESS", null, null));

        // 3. Ciudad
        Ciudad mendoza = ciudadRepository.save(new Ciudad(null, "Mendoza",      null));
        Ciudad bsas    = ciudadRepository.save(new Ciudad(null, "Buenos Aires", null));
        Ciudad cordoba = ciudadRepository.save(new Ciudad(null, "Córdoba",      null));

        // 4. Aerolinea
        Aerolinea aerolineasArg = aerolineaRepository.save(new Aerolinea(null, "Aerolíneas Argentinas", null, null));
        Aerolinea latam         = aerolineaRepository.save(new Aerolinea(null, "LATAM",                 null, null));

        // 5. Aeropuerto
        Aeropuerto aerMendoza = aeropuertoRepository.save(new Aeropuerto(null, "Aeropuerto El Plumerillo",          mendoza, null, null));
        Aeropuerto aerEzeiza  = aeropuertoRepository.save(new Aeropuerto(null, "Aeropuerto Internacional Ezeiza",   bsas,    null, null));
        Aeropuerto aerCordoba = aeropuertoRepository.save(new Aeropuerto(null, "Aeropuerto Internacional Córdoba",  cordoba, null, null));

        // 6. Persona
        Persona p1 = personaRepository.save(new Persona(null, 12345678, "Carlos",  "López"));
        Persona p2 = personaRepository.save(new Persona(null, 23456789, "María",   "García"));
        Persona p3 = personaRepository.save(new Persona(null, 34567890, "Roberto", "Díaz"));

        // 7. Piloto
        Piloto piloto1 = pilotoRepository.save(new Piloto(null, 1001, p1, null));
        Piloto piloto2 = pilotoRepository.save(new Piloto(null, 1002, p3, null));

        // 8. Avion
        Avion avion1 = avionRepository.save(new Avion(null, 101, "Boeing 737",  "Turbofan", aerolineasArg, null, null));
        Avion avion2 = avionRepository.save(new Avion(null, 102, "Airbus A320", "Turbofan", latam,         null, null));

        // 9. Asiento
        Asiento asiento1 = asientoRepository.save(new Asiento(null, 1, "A", "DISPONIBLE", avion1, economy,  null));
        asientoRepository.save(new Asiento(null, 1, "B", "DISPONIBLE", avion1, economy,  null));
        asientoRepository.save(new Asiento(null, 2, "A", "DISPONIBLE", avion1, business, null));
        asientoRepository.save(new Asiento(null, 1, "A", "DISPONIBLE", avion2, turista,  null));
        asientoRepository.save(new Asiento(null, 1, "B", "DISPONIBLE", avion2, turista,  null));

        // 10. Tarjeta (hereda de Pago — cantidadPago debe ser > 0 por validación @DecimalMin)
        Tarjeta tarjeta1 = new Tarjeta();
        tarjeta1.setNumeroPago(101);
        tarjeta1.setCantidadPago(1.0);
        tarjeta1.setFechaPago(new Date());
        tarjeta1.setNumeroTarjeta(4111111111111111L);
        tarjeta1.setTipoTarjeta(debito);
        tarjeta1 = tarjetaRepository.save(tarjeta1);

        Tarjeta tarjeta2 = new Tarjeta();
        tarjeta2.setNumeroPago(102);
        tarjeta2.setCantidadPago(1.0);
        tarjeta2.setFechaPago(new Date());
        tarjeta2.setNumeroTarjeta(5500005555555559L);
        tarjeta2.setTipoTarjeta(credito);
        tarjeta2 = tarjetaRepository.save(tarjeta2);

        // 11. Usuario
        Usuario usuario1 = usuarioRepository.save(new Usuario(null, 1001, "carlos@email.com", "pass123", p2, null, null, null));
        usuarioRepository.save(new Usuario(null, 1002, "maria@email.com",  "pass456", p1, null, null, null));

        // 12. Vuelo
        Vuelo vuelo1 = vueloRepository.save(new Vuelo(null, 2001, "PROGRAMADO", new Date(), avion1, piloto1, aerolineasArg, aerMendoza, aerEzeiza,  null, null, null));
        Vuelo vuelo2 = vueloRepository.save(new Vuelo(null, 2002, "PROGRAMADO", new Date(), avion2, piloto2, latam,         aerEzeiza,  aerCordoba, null, null, null));

        // 13. Tarifa
        tarifaRepository.save(new Tarifa(null, 1, 15000.0, 0.21, vuelo1, economy));
        tarifaRepository.save(new Tarifa(null, 2, 35000.0, 0.21, vuelo1, business));
        tarifaRepository.save(new Tarifa(null, 3, 12000.0, 0.21, vuelo2, turista));

        // 14. Pago directo
        Pago pago1 = pagoRepository.save(new Pago(null, 1, 15000.0, new Date(), null));

        // 15. Reserva
        reservaRepository.save(new Reserva(null, 3001, "CONFIRMADA", new Date(), usuario1, vuelo1, asiento1, pago1));

        // 16. Consulta
        consultaRepository.save(new Consulta(null, 1, new Date(), "DISPONIBILIDAD", "Consulta sobre vuelo MDZ-EZE", "RESPONDIDA", usuario1, vuelo1));
    }
}