// ============================================================
// app.js — Sistema Aerolínea
// Consume la API REST de Spring Boot mediante fetch.
// Regla: siempre usar /dto para listar datos en tablas.
// ============================================================

const BASE_URL = "http://localhost:8080/api/v1";

// ============================================================
// UTILIDADES GENERALES
// ============================================================

// Muestra un toast de notificación (éxito o error)
function mostrarToast(mensaje, tipo = "success") {
    const toast = document.getElementById("toast");
    const toastBody = document.getElementById("toast-body");
    toastBody.textContent = mensaje;
    toast.className = `toast align-items-center text-white border-0 bg-${tipo === "success" ? "success" : "danger"}`;
    const bsToast = new bootstrap.Toast(toast, { delay: 3000 });
    bsToast.show();
}

// Muestra el modal de confirmación y ejecuta el callback al confirmar
function confirmarEliminacion(mensaje, callback) {
    document.getElementById("modal-texto").textContent = mensaje;
    const modal = new bootstrap.Modal(document.getElementById("modal-confirmar"));
    modal.show();
    const btnConfirmar = document.getElementById("modal-btn-confirmar");
    const nuevoBtn = btnConfirmar.cloneNode(true);
    btnConfirmar.parentNode.replaceChild(nuevoBtn, btnConfirmar);
    nuevoBtn.addEventListener("click", () => {
        modal.hide();
        callback();
    });
}

// Muestra u oculta un formulario (usa clases Bootstrap d-none)
function toggleFormulario(idForm, mostrar) {
    const form = document.getElementById(idForm);
    if (mostrar) {
        form.classList.remove("d-none");
    } else {
        form.classList.add("d-none");
    }
}

// Formatea una fecha ISO a formato legible
function formatearFecha(fecha) {
    if (!fecha) return "-";
    return new Date(fecha).toLocaleString("es-AR");
}

// Convierte datetime-local a timestamp para enviar al backend
function fechaParaBackend(valor) {
    if (!valor) return null;
    return new Date(valor).getTime();
}

// Puebla un <select> con opciones cargadas desde un endpoint /dto
async function cargarSelect(idSelect, endpoint, valorId, textoFn) {
    try {
        const respuesta = await fetch(`${BASE_URL}/${endpoint}/dto`);
        const datos = await respuesta.json();
        const select = document.getElementById(idSelect);
        select.innerHTML = '<option value="">-- Seleccionar --</option>';
        datos.forEach(item => {
            const option = document.createElement("option");
            option.value = item[valorId];
            option.textContent = textoFn(item);
            select.appendChild(option);
        });
    } catch (error) {
        console.error(`Error cargando select ${idSelect}:`, error);
    }
}

// ============================================================
// NAVEGACIÓN ENTRE SECCIONES
// ============================================================

function inicializarNavegacion() {
    const botones = document.querySelectorAll(".sa-nav-btn");
    const secciones = document.querySelectorAll(".sa-seccion");

    botones.forEach(btn => {
        btn.addEventListener("click", () => {
            const seccion = btn.dataset.seccion;

            // Actualizar botón activo
            botones.forEach(b => b.classList.remove("active"));
            btn.classList.add("active");

            // Mostrar sección correspondiente
            secciones.forEach(s => s.classList.add("d-none"));
            document.getElementById(`sec-${seccion}`).classList.remove("d-none");

            // Cargar datos de la sección
            cargarSeccion(seccion);
        });
    });
}

function cargarSeccion(seccion) {
    switch (seccion) {
        case "vuelos":    cargarVuelos();    break;
        case "reservas":  cargarReservas();  break;
        case "usuarios":  cargarUsuarios();  break;
        case "consultas": cargarConsultas(); break;
        case "aviones":   cargarAviones();   break;
        case "catalogo":
            cargarAerolineas();
            cargarAeropuertos();
            cargarCiudades();
            break;
        case "tarifas":
            cargarTarifas();
            cargarAsientos();
            break;
        case "personal":
            cargarPilotos();
            cargarPersonas();
            break;
        case "pagos":
            cargarPagos();
            cargarTarjetas();
            break;
    }
}

// ============================================================
// VUELOS
// ============================================================

async function cargarVuelos() {
    try {
        const respuesta = await fetch(`${BASE_URL}/vuelos/dto`);
        const vuelos = await respuesta.json();
        const tbody = document.getElementById("tbody-vuelos");
        tbody.innerHTML = "";
        vuelos.forEach(v => {
            tbody.innerHTML += `
                <tr>
                    <td>${v.idVuelo}</td>
                    <td><span class="font-monospace">${v.numeroVuelo}</span></td>
                    <td><span class="badge bg-primary">${v.estadoVuelo ?? "-"}</span></td>
                    <td>${formatearFecha(v.fechaSalida)}</td>
                    <td>${v.nombreAerolinea ?? "-"}</td>
                    <td>${v.nombreAeropuertoOrigen ?? "-"}</td>
                    <td>${v.nombreAeropuertoDestino ?? "-"}</td>
                    <td>
                        <button class="btn btn-warning btn-accion me-1" onclick="editarVuelo(${v.idVuelo})">Editar</button>
                        <button class="btn btn-danger btn-accion" onclick="eliminarVuelo(${v.idVuelo}, 'Vuelo ${v.numeroVuelo}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar vuelos.", "error");
    }
}

document.getElementById("btn-nuevo-vuelo").addEventListener("click", async () => {
    document.getElementById("vuelo-id").value = "";
    document.getElementById("vuelo-numeroVuelo").value = "";
    document.getElementById("vuelo-estadoVuelo").value = "PROGRAMADO";
    document.getElementById("vuelo-fechaSalida").value = "";
    document.getElementById("form-vuelo-titulo").textContent = "Nuevo Vuelo";
    await Promise.all([
        cargarSelect("vuelo-avion",            "aviones",     "idAvion",      a => `${a.numeroAvion} — ${a.tipoAvion}`),
        cargarSelect("vuelo-piloto",           "pilotos",     "idPiloto",     p => `${p.numeroPiloto} — ${p.nombrePersona} ${p.apellidoPersona}`),
        cargarSelect("vuelo-aerolinea",        "aerolineas",  "idAerolinea",  a => a.nombreAerolinea),
        cargarSelect("vuelo-aeropuertoOrigen", "aeropuertos", "idAeropuerto", a => a.nombreAeropuerto),
        cargarSelect("vuelo-aeropuertoDestino","aeropuertos", "idAeropuerto", a => a.nombreAeropuerto),
    ]);
    toggleFormulario("form-vuelo", true);
});

document.getElementById("btn-cancelar-vuelo").addEventListener("click", () => {
    toggleFormulario("form-vuelo", false);
});

document.getElementById("btn-guardar-vuelo").addEventListener("click", async () => {
    const id = document.getElementById("vuelo-id").value;
    const body = {
        numeroVuelo:         parseInt(document.getElementById("vuelo-numeroVuelo").value),
        estadoVuelo:         document.getElementById("vuelo-estadoVuelo").value,
        fechaSalida:         fechaParaBackend(document.getElementById("vuelo-fechaSalida").value),
        avion:               { idAvion:          parseInt(document.getElementById("vuelo-avion").value) },
        piloto:              { idPiloto:         parseInt(document.getElementById("vuelo-piloto").value) },
        aerolinea:           { idAerolinea:      parseInt(document.getElementById("vuelo-aerolinea").value) },
        aeropuertoOrigen:    { idAeropuerto:     parseInt(document.getElementById("vuelo-aeropuertoOrigen").value) },
        aeropuertoDestino:   { idAeropuerto:     parseInt(document.getElementById("vuelo-aeropuertoDestino").value) },
    };

    // Validación básica frontend — campos vacíos
    if (document.getElementById("vuelo-numeroVuelo").value === "" ||
        document.getElementById("vuelo-avion").value === "" ||
        document.getElementById("vuelo-piloto").value === "" ||
        document.getElementById("vuelo-aerolinea").value === "" ||
        document.getElementById("vuelo-aeropuertoOrigen").value === "" ||
        document.getElementById("vuelo-aeropuertoDestino").value === "" ||
        !body.fechaSalida) {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const url    = id ? `${BASE_URL}/vuelos/${id}` : `${BASE_URL}/vuelos`;
        const method = id ? "PUT" : "POST";
        const respuesta = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });

        if (respuesta.ok) {
            mostrarToast(id ? "Vuelo actualizado." : "Vuelo creado.");
            toggleFormulario("form-vuelo", false);
            cargarVuelos();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al guardar el vuelo.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

async function editarVuelo(id) {
    try {
        const respuesta = await fetch(`${BASE_URL}/vuelos/${id}`);
        const v = await respuesta.json();
        document.getElementById("vuelo-id").value           = v.idVuelo;
        document.getElementById("vuelo-numeroVuelo").value  = v.numeroVuelo;
        document.getElementById("vuelo-estadoVuelo").value  = v.estadoVuelo;
        document.getElementById("vuelo-fechaSalida").value  = v.fechaSalida
            ? new Date(v.fechaSalida).toISOString().slice(0, 16) : "";
        document.getElementById("form-vuelo-titulo").textContent = "Editar Vuelo";
        await Promise.all([
            cargarSelect("vuelo-avion",            "aviones",     "idAvion",      a => `${a.numeroAvion} — ${a.tipoAvion}`),
            cargarSelect("vuelo-piloto",           "pilotos",     "idPiloto",     p => `${p.numeroPiloto} — ${p.nombrePersona} ${p.apellidoPersona}`),
            cargarSelect("vuelo-aerolinea",        "aerolineas",  "idAerolinea",  a => a.nombreAerolinea),
            cargarSelect("vuelo-aeropuertoOrigen", "aeropuertos", "idAeropuerto", a => a.nombreAeropuerto),
            cargarSelect("vuelo-aeropuertoDestino","aeropuertos", "idAeropuerto", a => a.nombreAeropuerto),
        ]);
        if (v.idAvion)             document.getElementById("vuelo-avion").value             = v.idAvion;
        if (v.idPiloto)            document.getElementById("vuelo-piloto").value            = v.idPiloto;
        if (v.idAerolinea)         document.getElementById("vuelo-aerolinea").value         = v.idAerolinea;
        if (v.idAeropuertoOrigen)  document.getElementById("vuelo-aeropuertoOrigen").value  = v.idAeropuertoOrigen;
        if (v.idAeropuertoDestino) document.getElementById("vuelo-aeropuertoDestino").value = v.idAeropuertoDestino;
        toggleFormulario("form-vuelo", true);
    } catch (error) {
        mostrarToast("Error al cargar el vuelo.", "error");
    }
}

function eliminarVuelo(id, nombre) {
    confirmarEliminacion(`¿Eliminar el vuelo ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/vuelos/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) {
                mostrarToast("Vuelo eliminado.");
                cargarVuelos();
            } else {
                mostrarToast("Error al eliminar el vuelo.", "error");
            }
        } catch (error) {
            mostrarToast("Error de conexión.", "error");
        }
    });
}

// ============================================================
// RESERVAS
// ============================================================

async function cargarReservas() {
    try {
        const respuesta = await fetch(`${BASE_URL}/reservas/dto`);
        const reservas = await respuesta.json();
        const tbody = document.getElementById("tbody-reservas");
        tbody.innerHTML = "";
        reservas.forEach(r => {
            tbody.innerHTML += `
                <tr>
                    <td>${r.idReserva}</td>
                    <td>${r.numeroReserva}</td>
                    <td><span class="badge bg-success">${r.estadoReserva ?? "-"}</span></td>
                    <td>${formatearFecha(r.fechaReserva)}</td>
                    <td>${r.idUsuario ?? "-"}</td>
                    <td>${r.idVuelo ?? "-"}</td>
                    <td>${r.idAsiento ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarReserva(${r.idReserva}, 'Reserva ${r.numeroReserva}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar reservas.", "error");
    }
}

document.getElementById("btn-nueva-reserva").addEventListener("click", async () => {
    document.getElementById("reserva-numeroReserva").value = "";
    document.getElementById("reserva-fechaReserva").value = "";
    document.getElementById("reserva-estadoReserva").value = "CONFIRMADA";
    await Promise.all([
        cargarSelect("reserva-usuario", "usuarios", "idUsuario", u => `${u.correoElectronicoUsuario}`),
        cargarSelect("reserva-vuelo",   "vuelos",   "idVuelo",   v => `Vuelo ${v.numeroVuelo} — ${v.nombreAeropuertoOrigen ?? ""} → ${v.nombreAeropuertoDestino ?? ""}`),
        cargarSelect("reserva-asiento", "asientos", "idAsiento", a => `Fila ${a.filaAsiento}${a.letraAsiento} — ${a.nombreClase}`),
        cargarSelect("reserva-pago",    "pagos",    "idPago",    p => `Pago #${p.numeroPago} ($${p.cantidadPago})`),
    ]);
    toggleFormulario("form-reserva", true);
});

document.getElementById("btn-cancelar-reserva").addEventListener("click", () => {
    toggleFormulario("form-reserva", false);
});

document.getElementById("btn-guardar-reserva").addEventListener("click", async () => {
    const body = {
        numeroReserva: parseInt(document.getElementById("reserva-numeroReserva").value),
        estadoReserva: document.getElementById("reserva-estadoReserva").value,
        fechaReserva:  fechaParaBackend(document.getElementById("reserva-fechaReserva").value),
        usuario:       { idUsuario: parseInt(document.getElementById("reserva-usuario").value) },
        vuelo:         { idVuelo:   parseInt(document.getElementById("reserva-vuelo").value) },
        asiento:       { idAsiento: parseInt(document.getElementById("reserva-asiento").value) },
        pago:          { idPago:    parseInt(document.getElementById("reserva-pago").value) },
    };

    if (document.getElementById("reserva-numeroReserva").value === "" ||
        document.getElementById("reserva-usuario").value === "" ||
        document.getElementById("reserva-vuelo").value === "" ||
        document.getElementById("reserva-asiento").value === "" ||
        document.getElementById("reserva-pago").value === "" ||
        !body.fechaReserva) {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/reservas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Reserva creada.");
            toggleFormulario("form-reserva", false);
            cargarReservas();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la reserva.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarReserva(id, nombre) {
    confirmarEliminacion(`¿Eliminar la reserva ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/reservas/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) {
                mostrarToast("Reserva eliminada.");
                cargarReservas();
            } else {
                mostrarToast("Error al eliminar la reserva.", "error");
            }
        } catch (error) {
            mostrarToast("Error de conexión.", "error");
        }
    });
}

// ============================================================
// USUARIOS
// ============================================================

async function cargarUsuarios() {
    try {
        const respuesta = await fetch(`${BASE_URL}/usuarios/dto`);
        const usuarios = await respuesta.json();
        const tbody = document.getElementById("tbody-usuarios");
        tbody.innerHTML = "";
        usuarios.forEach(u => {
            tbody.innerHTML += `
                <tr>
                    <td>${u.idUsuario}</td>
                    <td>${u.numeroUsuario}</td>
                    <td>${u.correoElectronicoUsuario}</td>
                    <td>${u.idPersona ?? "-"}</td>
                    <td>
                        <button class="btn btn-warning btn-accion me-1" onclick="editarUsuario(${u.idUsuario})">Editar</button>
                        <button class="btn btn-danger btn-accion" onclick="eliminarUsuario(${u.idUsuario}, '${u.correoElectronicoUsuario}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar usuarios.", "error");
    }
}

document.getElementById("btn-nuevo-usuario").addEventListener("click", async () => {
    document.getElementById("usuario-id").value = "";
    document.getElementById("usuario-numeroUsuario").value = "";
    document.getElementById("usuario-correoElectronicoUsuario").value = "";
    document.getElementById("usuario-contraseniaUsuario").value = "";
    document.getElementById("form-usuario-titulo").textContent = "Nuevo Usuario";
    await cargarSelect("usuario-persona", "personas", "idPersona", p => `${p.nombrePersona} ${p.apellidoPersona} — DNI ${p.dniPersona}`);
    toggleFormulario("form-usuario", true);
});

document.getElementById("btn-cancelar-usuario").addEventListener("click", () => {
    toggleFormulario("form-usuario", false);
});

document.getElementById("btn-guardar-usuario").addEventListener("click", async () => {
    const id = document.getElementById("usuario-id").value;
    const correo = document.getElementById("usuario-correoElectronicoUsuario").value.trim();

    // Validación de formato de email en el frontend
    const regexEmail = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!regexEmail.test(correo)) {
        mostrarToast("El correo electrónico no tiene un formato válido.", "error");
        return;
    }

    const body = {
        numeroUsuario:            parseInt(document.getElementById("usuario-numeroUsuario").value),
        correoElectronicoUsuario: correo,
        contraseniaUsuario:       document.getElementById("usuario-contraseniaUsuario").value,
        persona:                  { idPersona: parseInt(document.getElementById("usuario-persona").value) },
    };

    if (document.getElementById("usuario-numeroUsuario").value === "" ||
        !body.correoElectronicoUsuario ||
        !body.contraseniaUsuario ||
        document.getElementById("usuario-persona").value === "") {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const url    = id ? `${BASE_URL}/usuarios/${id}` : `${BASE_URL}/usuarios`;
        const method = id ? "PUT" : "POST";
        const respuesta = await fetch(url, {
            method,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast(id ? "Usuario actualizado." : "Usuario creado.");
            toggleFormulario("form-usuario", false);
            cargarUsuarios();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al guardar el usuario.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

async function editarUsuario(id) {
    try {
        const respuesta = await fetch(`${BASE_URL}/usuarios/${id}`);
        const u = await respuesta.json();
        document.getElementById("usuario-id").value = u.idUsuario;
        document.getElementById("usuario-numeroUsuario").value = u.numeroUsuario;
        document.getElementById("usuario-correoElectronicoUsuario").value = u.correoElectronicoUsuario;
        document.getElementById("usuario-contraseniaUsuario").value = "";
        document.getElementById("form-usuario-titulo").textContent = "Editar Usuario";
        await cargarSelect("usuario-persona", "personas", "idPersona", p => `${p.nombrePersona} ${p.apellidoPersona} — DNI ${p.dniPersona}`);
        if (u.idPersona) document.getElementById("usuario-persona").value = u.idPersona;
        toggleFormulario("form-usuario", true);
    } catch (error) {
        mostrarToast("Error al cargar el usuario.", "error");
    }
}

function eliminarUsuario(id, nombre) {
    confirmarEliminacion(`¿Eliminar el usuario ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/usuarios/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) {
                mostrarToast("Usuario eliminado.");
                cargarUsuarios();
            } else {
                mostrarToast("Error al eliminar el usuario.", "error");
            }
        } catch (error) {
            mostrarToast("Error de conexión.", "error");
        }
    });
}

// ============================================================
// CONSULTAS
// ============================================================

async function cargarConsultas() {
    try {
        const respuesta = await fetch(`${BASE_URL}/consultas/dto`);
        const consultas = await respuesta.json();
        const tbody = document.getElementById("tbody-consultas");
        tbody.innerHTML = "";
        consultas.forEach(c => {
            tbody.innerHTML += `
                <tr>
                    <td>${c.idConsulta}</td>
                    <td>${c.numeroConsulta}</td>
                    <td>${formatearFecha(c.fechaConsulta)}</td>
                    <td>${c.tipoConsulta ?? "-"}</td>
                    <td><span class="badge bg-secondary">${c.estadoConsulta ?? "-"}</span></td>
                    <td class="text-truncate" style="max-width:150px;" title="${c.detalleConsulta ?? ""}">${c.detalleConsulta ?? "-"}</td>
                    <td>${c.idUsuario ?? "-"}</td>
                    <td>${c.idVuelo ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarConsulta(${c.idConsulta}, 'Consulta ${c.numeroConsulta}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar consultas.", "error");
    }
}

document.getElementById("btn-nueva-consulta").addEventListener("click", async () => {
    document.getElementById("consulta-numeroConsulta").value = "";
    document.getElementById("consulta-fechaConsulta").value = "";
    document.getElementById("consulta-tipoConsulta").value = "";
    document.getElementById("consulta-estadoConsulta").value = "";
    document.getElementById("consulta-detalleConsulta").value = "";
    await Promise.all([
        cargarSelect("consulta-usuario", "usuarios", "idUsuario", u => u.correoElectronicoUsuario),
        cargarSelect("consulta-vuelo",   "vuelos",   "idVuelo",   v => `Vuelo ${v.numeroVuelo}`),
    ]);
    toggleFormulario("form-consulta", true);
});

document.getElementById("btn-cancelar-consulta").addEventListener("click", () => {
    toggleFormulario("form-consulta", false);
});

document.getElementById("btn-guardar-consulta").addEventListener("click", async () => {
    const body = {
        numeroConsulta: parseInt(document.getElementById("consulta-numeroConsulta").value),
        fechaConsulta:  fechaParaBackend(document.getElementById("consulta-fechaConsulta").value),
        tipoConsulta:   document.getElementById("consulta-tipoConsulta").value.trim(),
        estadoConsulta: document.getElementById("consulta-estadoConsulta").value.trim(),
        detalleConsulta:document.getElementById("consulta-detalleConsulta").value.trim(),
        usuario:        { idUsuario: parseInt(document.getElementById("consulta-usuario").value) },
        vuelo:          document.getElementById("consulta-vuelo").value
                        ? { idVuelo: parseInt(document.getElementById("consulta-vuelo").value) }
                        : null,
    };

    if (document.getElementById("consulta-numeroConsulta").value === "" ||
        !body.tipoConsulta || !body.estadoConsulta ||
        !body.detalleConsulta ||
        document.getElementById("consulta-usuario").value === "" ||
        !body.fechaConsulta) {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/consultas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Consulta creada.");
            toggleFormulario("form-consulta", false);
            cargarConsultas();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la consulta.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarConsulta(id, nombre) {
    confirmarEliminacion(`¿Eliminar la consulta ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/consultas/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) {
                mostrarToast("Consulta eliminada.");
                cargarConsultas();
            } else {
                mostrarToast("Error al eliminar la consulta.", "error");
            }
        } catch (error) {
            mostrarToast("Error de conexión.", "error");
        }
    });
}

// ============================================================
// AVIONES
// ============================================================

async function cargarAviones() {
    try {
        const respuesta = await fetch(`${BASE_URL}/aviones/dto`);
        const aviones = await respuesta.json();
        const tbody = document.getElementById("tbody-aviones");
        tbody.innerHTML = "";
        aviones.forEach(a => {
            tbody.innerHTML += `
                <tr>
                    <td>${a.idAvion}</td>
                    <td>${a.numeroAvion}</td>
                    <td>${a.tipoAvion ?? "-"}</td>
                    <td>${a.tipoTurbina ?? "-"}</td>
                    <td>${a.nombreAerolinea ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarAvion(${a.idAvion}, '${a.tipoAvion} (${a.numeroAvion})')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar aviones.", "error");
    }
}

document.getElementById("btn-nuevo-avion").addEventListener("click", async () => {
    document.getElementById("avion-numeroAvion").value = "";
    document.getElementById("avion-tipoAvion").value = "";
    document.getElementById("avion-tipoTurbina").value = "";
    await cargarSelect("avion-aerolinea", "aerolineas", "idAerolinea", a => a.nombreAerolinea);
    toggleFormulario("form-avion", true);
});

document.getElementById("btn-cancelar-avion").addEventListener("click", () => {
    toggleFormulario("form-avion", false);
});

document.getElementById("btn-guardar-avion").addEventListener("click", async () => {
    const body = {
        numeroAvion:  parseInt(document.getElementById("avion-numeroAvion").value),
        tipoAvion:    document.getElementById("avion-tipoAvion").value.trim(),
        tipoTurbina:  document.getElementById("avion-tipoTurbina").value.trim(),
        aerolinea:    { idAerolinea: parseInt(document.getElementById("avion-aerolinea").value) },
    };

    if (document.getElementById("avion-numeroAvion").value === "" ||
        !body.tipoAvion || !body.tipoTurbina ||
        document.getElementById("avion-aerolinea").value === "") {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/aviones`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Avión creado.");
            toggleFormulario("form-avion", false);
            cargarAviones();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear el avión.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarAvion(id, nombre) {
    confirmarEliminacion(`¿Eliminar el avión ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/aviones/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) {
                mostrarToast("Avión eliminado.");
                cargarAviones();
            } else {
                mostrarToast("Error al eliminar el avión.", "error");
            }
        } catch (error) {
            mostrarToast("Error de conexión.", "error");
        }
    });
}

// ============================================================
// CATÁLOGO — AEROLÍNEAS
// ============================================================

async function cargarAerolineas() {
    try {
        const respuesta = await fetch(`${BASE_URL}/aerolineas/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-aerolineas");
        tbody.innerHTML = "";
        datos.forEach(a => {
            tbody.innerHTML += `
                <tr>
                    <td>${a.idAerolinea}</td>
                    <td>${a.nombreAerolinea}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarAerolinea(${a.idAerolinea}, '${a.nombreAerolinea}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar aerolíneas.", "error");
    }
}

document.getElementById("btn-nueva-aerolinea").addEventListener("click", () => {
    document.getElementById("aerolinea-nombreAerolinea").value = "";
    toggleFormulario("form-aerolinea", true);
});

document.getElementById("btn-cancelar-aerolinea").addEventListener("click", () => {
    toggleFormulario("form-aerolinea", false);
});

document.getElementById("btn-guardar-aerolinea").addEventListener("click", async () => {
    const nombre = document.getElementById("aerolinea-nombreAerolinea").value.trim();
    if (!nombre) { mostrarToast("El nombre no puede estar vacío.", "error"); return; }

    try {
        const respuesta = await fetch(`${BASE_URL}/aerolineas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nombreAerolinea: nombre })
        });
        if (respuesta.ok) {
            mostrarToast("Aerolínea creada.");
            toggleFormulario("form-aerolinea", false);
            cargarAerolineas();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la aerolínea.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarAerolinea(id, nombre) {
    confirmarEliminacion(`¿Eliminar la aerolínea ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/aerolineas/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Aerolínea eliminada."); cargarAerolineas(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// CATÁLOGO — AEROPUERTOS
// ============================================================

async function cargarAeropuertos() {
    try {
        const respuesta = await fetch(`${BASE_URL}/aeropuertos/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-aeropuertos");
        tbody.innerHTML = "";
        datos.forEach(a => {
            tbody.innerHTML += `
                <tr>
                    <td>${a.idAeropuerto}</td>
                    <td>${a.nombreAeropuerto}</td>
                    <td>${a.nombreCiudad ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarAeropuerto(${a.idAeropuerto}, '${a.nombreAeropuerto}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar aeropuertos.", "error");
    }
}

document.getElementById("btn-nuevo-aeropuerto").addEventListener("click", async () => {
    document.getElementById("aeropuerto-nombreAeropuerto").value = "";
    await cargarSelect("aeropuerto-ciudad", "ciudades", "idCiudad", c => c.nombreCiudad);
    toggleFormulario("form-aeropuerto", true);
});

document.getElementById("btn-cancelar-aeropuerto").addEventListener("click", () => {
    toggleFormulario("form-aeropuerto", false);
});

document.getElementById("btn-guardar-aeropuerto").addEventListener("click", async () => {
    const nombre = document.getElementById("aeropuerto-nombreAeropuerto").value.trim();
    const idCiudad = parseInt(document.getElementById("aeropuerto-ciudad").value);
    if (!nombre || !idCiudad) { mostrarToast("Completá todos los campos.", "error"); return; }

    try {
        const respuesta = await fetch(`${BASE_URL}/aeropuertos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nombreAeropuerto: nombre, ciudad: { idCiudad } })
        });
        if (respuesta.ok) {
            mostrarToast("Aeropuerto creado.");
            toggleFormulario("form-aeropuerto", false);
            cargarAeropuertos();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear el aeropuerto.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarAeropuerto(id, nombre) {
    confirmarEliminacion(`¿Eliminar el aeropuerto ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/aeropuertos/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Aeropuerto eliminado."); cargarAeropuertos(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// CATÁLOGO — CIUDADES
// ============================================================

async function cargarCiudades() {
    try {
        const respuesta = await fetch(`${BASE_URL}/ciudades/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-ciudades");
        tbody.innerHTML = "";
        datos.forEach(c => {
            tbody.innerHTML += `
                <tr>
                    <td>${c.idCiudad}</td>
                    <td>${c.nombreCiudad}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarCiudad(${c.idCiudad}, '${c.nombreCiudad}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar ciudades.", "error");
    }
}

document.getElementById("btn-nueva-ciudad").addEventListener("click", () => {
    document.getElementById("ciudad-nombreCiudad").value = "";
    toggleFormulario("form-ciudad", true);
});

document.getElementById("btn-cancelar-ciudad").addEventListener("click", () => {
    toggleFormulario("form-ciudad", false);
});

document.getElementById("btn-guardar-ciudad").addEventListener("click", async () => {
    const nombre = document.getElementById("ciudad-nombreCiudad").value.trim();
    if (!nombre) { mostrarToast("El nombre no puede estar vacío.", "error"); return; }

    try {
        const respuesta = await fetch(`${BASE_URL}/ciudades`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nombreCiudad: nombre })
        });
        if (respuesta.ok) {
            mostrarToast("Ciudad creada.");
            toggleFormulario("form-ciudad", false);
            cargarCiudades();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la ciudad.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarCiudad(id, nombre) {
    confirmarEliminacion(`¿Eliminar la ciudad ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/ciudades/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Ciudad eliminada."); cargarCiudades(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// TARIFAS
// ============================================================

async function cargarTarifas() {
    try {
        const respuesta = await fetch(`${BASE_URL}/tarifas/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-tarifas");
        tbody.innerHTML = "";
        datos.forEach(t => {
            tbody.innerHTML += `
                <tr>
                    <td>${t.idTarifa}</td>
                    <td>${t.numeroTarifa}</td>
                    <td>$${t.precioTarifa}</td>
                    <td>${t.impuestoTarifa}%</td>
                    <td>${t.idVuelo ?? "-"}</td>
                    <td>${t.nombreClase ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarTarifa(${t.idTarifa}, 'Tarifa ${t.numeroTarifa}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar tarifas.", "error");
    }
}

document.getElementById("btn-nueva-tarifa").addEventListener("click", async () => {
    document.getElementById("tarifa-numeroTarifa").value = "";
    document.getElementById("tarifa-precioTarifa").value = "";
    document.getElementById("tarifa-impuestoTarifa").value = "";
    await Promise.all([
        cargarSelect("tarifa-vuelo",  "vuelos",  "idVuelo",  v => `Vuelo ${v.numeroVuelo}`),
        cargarSelect("tarifa-clase",  "clases",  "idClase",  c => c.nombreClase),
    ]);
    toggleFormulario("form-tarifa", true);
});

document.getElementById("btn-cancelar-tarifa").addEventListener("click", () => {
    toggleFormulario("form-tarifa", false);
});

document.getElementById("btn-guardar-tarifa").addEventListener("click", async () => {
    const body = {
        numeroTarifa:   parseInt(document.getElementById("tarifa-numeroTarifa").value),
        precioTarifa:   parseFloat(document.getElementById("tarifa-precioTarifa").value),
        impuestoTarifa: parseFloat(document.getElementById("tarifa-impuestoTarifa").value),
        vuelo:          { idVuelo: parseInt(document.getElementById("tarifa-vuelo").value) },
        clase:          { idClase: parseInt(document.getElementById("tarifa-clase").value) },
    };

    if (document.getElementById("tarifa-numeroTarifa").value === "" ||
        document.getElementById("tarifa-precioTarifa").value === "" ||
        document.getElementById("tarifa-vuelo").value === "" ||
        document.getElementById("tarifa-clase").value === "") {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/tarifas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Tarifa creada.");
            toggleFormulario("form-tarifa", false);
            cargarTarifas();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la tarifa.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarTarifa(id, nombre) {
    confirmarEliminacion(`¿Eliminar la tarifa ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/tarifas/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Tarifa eliminada."); cargarTarifas(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// ASIENTOS
// ============================================================

async function cargarAsientos() {
    try {
        const respuesta = await fetch(`${BASE_URL}/asientos/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-asientos");
        tbody.innerHTML = "";
        datos.forEach(a => {
            tbody.innerHTML += `
                <tr>
                    <td>${a.idAsiento}</td>
                    <td>${a.filaAsiento}</td>
                    <td>${a.letraAsiento}</td>
                    <td><span class="badge bg-info text-dark">${a.estado ?? "-"}</span></td>
                    <td>${a.idAvion ?? "-"}</td>
                    <td>${a.nombreClase ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarAsiento(${a.idAsiento}, 'Asiento ${a.filaAsiento}${a.letraAsiento}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar asientos.", "error");
    }
}

document.getElementById("btn-nuevo-asiento").addEventListener("click", async () => {
    document.getElementById("asiento-filaAsiento").value = "";
    document.getElementById("asiento-letraAsiento").value = "";
    document.getElementById("asiento-estado").value = "DISPONIBLE";
    await Promise.all([
        cargarSelect("asiento-avion", "aviones", "idAvion", a => `${a.numeroAvion} — ${a.tipoAvion}`),
        cargarSelect("asiento-clase", "clases",  "idClase", c => c.nombreClase),
    ]);
    toggleFormulario("form-asiento", true);
});

document.getElementById("btn-cancelar-asiento").addEventListener("click", () => {
    toggleFormulario("form-asiento", false);
});

document.getElementById("btn-guardar-asiento").addEventListener("click", async () => {
    const body = {
        filaAsiento:  parseInt(document.getElementById("asiento-filaAsiento").value),
        letraAsiento: document.getElementById("asiento-letraAsiento").value.trim().toUpperCase(),
        estado:       document.getElementById("asiento-estado").value,
        avion:        { idAvion: parseInt(document.getElementById("asiento-avion").value) },
        clase:        { idClase: parseInt(document.getElementById("asiento-clase").value) },
    };

    if (document.getElementById("asiento-filaAsiento").value === "" ||
        !body.letraAsiento ||
        document.getElementById("asiento-avion").value === "" ||
        document.getElementById("asiento-clase").value === "") {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/asientos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Asiento creado.");
            toggleFormulario("form-asiento", false);
            cargarAsientos();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear el asiento.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarAsiento(id, nombre) {
    confirmarEliminacion(`¿Eliminar el asiento ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/asientos/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Asiento eliminado."); cargarAsientos(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// PERSONAL — PILOTOS
// ============================================================

async function cargarPilotos() {
    try {
        const respuesta = await fetch(`${BASE_URL}/pilotos/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-pilotos");
        tbody.innerHTML = "";
        datos.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.idPiloto}</td>
                    <td>${p.numeroPiloto}</td>
                    <td>${p.nombrePersona ?? "-"}</td>
                    <td>${p.apellidoPersona ?? "-"}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarPiloto(${p.idPiloto}, '${p.nombrePersona} ${p.apellidoPersona}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar pilotos.", "error");
    }
}

document.getElementById("btn-nuevo-piloto").addEventListener("click", async () => {
    document.getElementById("piloto-numeroPiloto").value = "";
    await cargarSelect("piloto-persona", "personas", "idPersona", p => `${p.nombrePersona} ${p.apellidoPersona} — DNI ${p.dniPersona}`);
    toggleFormulario("form-piloto", true);
});

document.getElementById("btn-cancelar-piloto").addEventListener("click", () => {
    toggleFormulario("form-piloto", false);
});

document.getElementById("btn-guardar-piloto").addEventListener("click", async () => {
    const body = {
        numeroPiloto: parseInt(document.getElementById("piloto-numeroPiloto").value),
        persona:      { idPersona: parseInt(document.getElementById("piloto-persona").value) },
    };

    if (document.getElementById("piloto-numeroPiloto").value === "" ||
        document.getElementById("piloto-persona").value === "") {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/pilotos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Piloto creado.");
            toggleFormulario("form-piloto", false);
            cargarPilotos();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear el piloto.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarPiloto(id, nombre) {
    confirmarEliminacion(`¿Eliminar el piloto ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/pilotos/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Piloto eliminado."); cargarPilotos(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// PERSONAL — PERSONAS
// ============================================================

async function cargarPersonas() {
    try {
        const respuesta = await fetch(`${BASE_URL}/personas/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-personas");
        tbody.innerHTML = "";
        datos.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.idPersona}</td>
                    <td>${p.dniPersona}</td>
                    <td>${p.nombrePersona}</td>
                    <td>${p.apellidoPersona}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarPersona(${p.idPersona}, '${p.nombrePersona} ${p.apellidoPersona}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar personas.", "error");
    }
}

document.getElementById("btn-nueva-persona").addEventListener("click", () => {
    document.getElementById("persona-dniPersona").value = "";
    document.getElementById("persona-nombrePersona").value = "";
    document.getElementById("persona-apellidoPersona").value = "";
    toggleFormulario("form-persona", true);
});

document.getElementById("btn-cancelar-persona").addEventListener("click", () => {
    toggleFormulario("form-persona", false);
});

document.getElementById("btn-guardar-persona").addEventListener("click", async () => {
    const body = {
        dniPersona:      parseInt(document.getElementById("persona-dniPersona").value),
        nombrePersona:   document.getElementById("persona-nombrePersona").value.trim(),
        apellidoPersona: document.getElementById("persona-apellidoPersona").value.trim(),
    };

    if (document.getElementById("persona-dniPersona").value === "" ||
        !body.nombrePersona || !body.apellidoPersona) {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/personas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Persona creada.");
            toggleFormulario("form-persona", false);
            cargarPersonas();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la persona.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarPersona(id, nombre) {
    confirmarEliminacion(`¿Eliminar la persona ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/personas/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Persona eliminada."); cargarPersonas(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// PAGOS
// ============================================================

async function cargarPagos() {
    try {
        const respuesta = await fetch(`${BASE_URL}/pagos/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-pagos");
        tbody.innerHTML = "";
        datos.forEach(p => {
            tbody.innerHTML += `
                <tr>
                    <td>${p.idPago}</td>
                    <td>${p.numeroPago}</td>
                    <td>$${p.cantidadPago}</td>
                    <td>${formatearFecha(p.fechaPago)}</td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarPago(${p.idPago}, 'Pago N° ${p.numeroPago}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar pagos.", "error");
    }
}

document.getElementById("btn-nuevo-pago").addEventListener("click", () => {
    document.getElementById("pago-numeroPago").value = "";
    document.getElementById("pago-cantidadPago").value = "";
    document.getElementById("pago-fechaPago").value = "";
    toggleFormulario("form-pago", true);
});

document.getElementById("btn-cancelar-pago").addEventListener("click", () => {
    toggleFormulario("form-pago", false);
});

document.getElementById("btn-guardar-pago").addEventListener("click", async () => {
    const body = {
        numeroPago:   parseInt(document.getElementById("pago-numeroPago").value),
        cantidadPago: parseFloat(document.getElementById("pago-cantidadPago").value),
        fechaPago:    fechaParaBackend(document.getElementById("pago-fechaPago").value),
    };

    if (document.getElementById("pago-numeroPago").value === "" ||
        document.getElementById("pago-cantidadPago").value === "" ||
        !body.fechaPago) {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/pagos`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Pago creado.");
            toggleFormulario("form-pago", false);
            cargarPagos();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear el pago.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarPago(id, nombre) {
    confirmarEliminacion(`¿Eliminar el pago ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/pagos/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Pago eliminado."); cargarPagos(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// TARJETAS
// ============================================================

async function cargarTarjetas() {
    try {
        const respuesta = await fetch(`${BASE_URL}/tarjetas/dto`);
        const datos = await respuesta.json();
        const tbody = document.getElementById("tbody-tarjetas");
        tbody.innerHTML = "";
        datos.forEach(t => {
            tbody.innerHTML += `
                <tr>
                    <td>${t.idPago}</td>
                    <td>${t.numeroTarjeta}</td>
                    <td><span class="badge bg-dark">${t.tipoTarjeta ?? "-"}</span></td>
                    <td>
                        <button class="btn btn-danger btn-accion" onclick="eliminarTarjeta(${t.idPago}, 'Tarjeta ${t.numeroTarjeta}')">Eliminar</button>
                    </td>
                </tr>`;
        });
    } catch (error) {
        mostrarToast("Error al cargar tarjetas.", "error");
    }
}

document.getElementById("btn-nueva-tarjeta").addEventListener("click", async () => {
    document.getElementById("tarjeta-numeroTarjeta").value = "";
    document.getElementById("tarjeta-numeroPago").value = "";
    document.getElementById("tarjeta-cantidadPago").value = "";
    document.getElementById("tarjeta-fechaPago").value = "";
    await cargarSelect("tarjeta-tipoTarjeta", "tipos-tarjeta", "idTipoTarjeta", t => t.tipoTarjeta);
    toggleFormulario("form-tarjeta", true);
});

document.getElementById("btn-cancelar-tarjeta").addEventListener("click", () => {
    toggleFormulario("form-tarjeta", false);
});

document.getElementById("btn-guardar-tarjeta").addEventListener("click", async () => {
    // Tarjeta hereda de Pago: se envían los campos de Pago + los propios de Tarjeta
    const body = {
        numeroPago:   parseInt(document.getElementById("tarjeta-numeroPago").value),
        cantidadPago: parseFloat(document.getElementById("tarjeta-cantidadPago").value),
        fechaPago:    fechaParaBackend(document.getElementById("tarjeta-fechaPago").value),
        numeroTarjeta:parseInt(document.getElementById("tarjeta-numeroTarjeta").value),
        tipoTarjeta:  { idTipoTarjeta: parseInt(document.getElementById("tarjeta-tipoTarjeta").value) },
    };

    if (document.getElementById("tarjeta-numeroPago").value === "" ||
        document.getElementById("tarjeta-cantidadPago").value === "" ||
        !body.fechaPago ||
        document.getElementById("tarjeta-numeroTarjeta").value === "" ||
        document.getElementById("tarjeta-tipoTarjeta").value === "") {
        mostrarToast("Completá todos los campos obligatorios.", "error");
        return;
    }

    try {
        const respuesta = await fetch(`${BASE_URL}/tarjetas`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body)
        });
        if (respuesta.ok) {
            mostrarToast("Tarjeta creada.");
            toggleFormulario("form-tarjeta", false);
            cargarTarjetas();
        } else {
            const errores = await respuesta.json();
            mostrarToast(Array.isArray(errores) ? errores.join(" | ") : "Error al crear la tarjeta.", "error");
        }
    } catch (error) {
        mostrarToast("Error de conexión.", "error");
    }
});

function eliminarTarjeta(id, nombre) {
    confirmarEliminacion(`¿Eliminar la tarjeta ${nombre}?`, async () => {
        try {
            const respuesta = await fetch(`${BASE_URL}/tarjetas/${id}`, { method: "DELETE" });
            if (respuesta.status === 204) { mostrarToast("Tarjeta eliminada."); cargarTarjetas(); }
            else mostrarToast("Error al eliminar.", "error");
        } catch (error) { mostrarToast("Error de conexión.", "error"); }
    });
}

// ============================================================
// INICIO — carga la sección por defecto al abrir la página
// ============================================================
document.addEventListener("DOMContentLoaded", () => {
    inicializarNavegacion();
    cargarVuelos();
});
