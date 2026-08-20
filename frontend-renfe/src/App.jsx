import { useEffect, useState } from "react";

function App() {
  const [email, setEmail] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [usuario, setUsuario] = useState(null);
  const [error, setError] = useState("");

  const [vista, setVista] = useState("inicio");

  const [rutas, setRutas] = useState([]);
  const [origenId, setOrigenId] = useState("");
  const [destinoId, setDestinoId] = useState("");
  const [fecha, setFecha] = useState("");
  const [viajes, setViajes] = useState([]);
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  const [errorBusqueda, setErrorBusqueda] = useState("");

  const [viajeSeleccionado, setViajeSeleccionado] = useState(null);
  const [vagones, setVagones] = useState([]);
  const [cargandoAsientos, setCargandoAsientos] = useState(false);
  const [errorAsientos, setErrorAsientos] = useState("");
  const [asientoSeleccionado, setAsientoSeleccionado] = useState(null);

  const [reservaConfirmada, setReservaConfirmada] = useState(false);
  const [misViajes, setMisViajes] = useState([]);

  const [errorReserva, setErrorReserva] = useState("");

  const origenes = Array.from(
    new Map(rutas.map((ruta) => [ruta.origen.id, ruta.origen])).values()
  );

  const destinosDisponibles = rutas
    .filter((ruta) => ruta.origen.id === Number(origenId))
    .map((ruta) => ruta.destino);

  useEffect(() => {
    async function cargarRutas() {
      try {
        const respuesta = await fetch("http://localhost:8081/rutas");

        if (!respuesta.ok) {
          throw new Error("No se pudieron cargar las rutas.");
        }

        const datos = await respuesta.json();
        setRutas(datos);
      } catch (error) {
        setErrorBusqueda(error.message);
      }
    }

    cargarRutas();
  }, []);

  useEffect(() => {
    if (vista !== "asientos" || !viajeSeleccionado) {
      return;
    }

    function formatearHora(hora) {
      return hora ? hora.slice(0, 5) : "";
    }

    async function cargarAsientos() {
      try {
        setCargandoAsientos(true);
        setErrorAsientos("");

        const respuesta = await fetch(
          `http://localhost:8081/viajes/${viajeSeleccionado.id}/asientos`
        );

        if (!respuesta.ok) {
          throw new Error("No se pudieron cargar los asientos.");
        }

        const datos = await respuesta.json();
        setVagones(datos);
      } catch (error) {
        setErrorAsientos(error.message);
      } finally {
        setCargandoAsientos(false);
      }
    }

    cargarAsientos();
  }, [vista, viajeSeleccionado]);

  useEffect(() => {
    if (vista !== "misViajes" || !usuario) {
      return;
    }

    async function cargarMisViajes() {
      try {
        const token = localStorage.getItem("token");

        const respuesta = await fetch(
          "http://localhost:8081/reservas/mis-reservas",
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        if (!respuesta.ok) {
          throw new Error("No se pudieron cargar tus viajes.");
        }

        const datos = await respuesta.json();
        setMisViajes(datos);
      } catch (error) {
        setErrorReserva(error.message);
      }
    }

    cargarMisViajes();
  }, [vista, usuario]);

  async function iniciarSesion(evento) {
    evento.preventDefault();

    if (!email.trim() || !contrasena.trim()) {
      setError("Introduce tu correo electrónico y tu contraseña.");
      return;
    }

    try {
      setError("");

      const respuestaLogin = await fetch(
        "http://localhost:8081/usuarios/login",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            email,
            password: contrasena,
          }),
        }
      );

      const datosLogin = await respuestaLogin.json();

      if (!respuestaLogin.ok || !datosLogin.token) {
        throw new Error("Correo electrónico o contraseña incorrectos.");
      }

      localStorage.setItem("token", datosLogin.token);

      const respuestaPerfil = await fetch(
        "http://localhost:8081/usuarios/perfil",
        {
          headers: {
            Authorization: `Bearer ${datosLogin.token}`,
          },
        }
      );

      if (!respuestaPerfil.ok) {
        throw new Error("No se pudo obtener el perfil del usuario.");
      }

      const perfil = await respuestaPerfil.json();
      setUsuario(perfil);
    } catch (error) {
      setError(error.message);
    }
  }

  function cerrarSesion() {
    localStorage.removeItem("token");
    setUsuario(null);
    setEmail("");
    setContrasena("");
    setVista("inicio");
  }

  async function buscarViajes(evento) {
    evento.preventDefault();

    if (!origenId || !destinoId || !fecha) {
      setErrorBusqueda("Completa origen, destino y fecha.");
      return;
    }

    try {
      setErrorBusqueda("");

      const parametros = new URLSearchParams({
        origen: origenId,
        destino: destinoId,
        fecha,
      });

      const respuesta = await fetch(
        `http://localhost:8081/viajes/buscar?${parametros}`
      );

      if (!respuesta.ok) {
        throw new Error("No se pudieron buscar los viajes.");
      }

      const datos = await respuesta.json();
      setViajes(datos);
      setBusquedaRealizada(true);
    } catch (error) {
      setErrorBusqueda(error.message);
    }
  }

  function seleccionarViaje(viaje) {
    setViajeSeleccionado(viaje);
    setAsientoSeleccionado(null);
    setReservaConfirmada(false);
    setVista("asientos");
  }

  function seleccionarAsiento(asiento, ocupado) {
    if (!ocupado) {
      setAsientoSeleccionado(asiento);
    }
  }

  async function confirmarReserva() {
    try {
      setErrorReserva("");

      const token = localStorage.getItem("token");

      if (!token) {
        throw new Error("Tu sesión ha caducado. Inicia sesión de nuevo.");
      }

      const respuesta = await fetch("http://localhost:8081/reservas", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          viajeId: viajeSeleccionado.id,
          asiento: asientoSeleccionado,
        }),
      });

      const datos = await respuesta.json();

      if (!respuesta.ok) {
        throw new Error(datos.message || datos.detail || "No se pudo crear la reserva.");
      }

      setMisViajes((viajesActuales) => [...viajesActuales, datos]);
      setReservaConfirmada(true);
    } catch (error) {
      setErrorReserva(error.message);
    }
  }

  setMisViajes((viajesActuales) => [...viajesActuales, nuevaReserva]);
  setReservaConfirmada(true);


  function volverInicio() {
    setVista("inicio");
    setBusquedaRealizada(false);
  }

  if (!usuario) {
    return (
      <main className="pagina">
        <section className="login">
          <p className="marca">RENFE</p>
          <h1>Bienvenido</h1>
          <p className="subtitulo">Inicia sesión para gestionar tus viajes.</p>

          <form onSubmit={iniciarSesion}>
            <label htmlFor="email">Correo electrónico</label>
            <input
              id="email"
              type="email"
              value={email}
              onChange={(evento) => setEmail(evento.target.value)}
              placeholder="tu@email.com"
            />

            <label htmlFor="contrasena">Contraseña</label>
            <input
              id="contrasena"
              type="password"
              value={contrasena}
              onChange={(evento) => setContrasena(evento.target.value)}
              placeholder="Introduce tu contraseña"
            />

            {error && <p className="error">{error}</p>}

            <button className="boton-principal" type="submit">
              Iniciar sesión
            </button>
          </form>
        </section>
      </main>
    );
  }

  return (
    <main className="dashboard">
      <header className="barra-superior">
        <p className="marca">RENFE</p>

        <nav>
          <button
            className={`enlace-menu ${vista === "inicio" ? "activo" : ""}`}
            onClick={volverInicio}
          >
            Inicio
          </button>

          <button
            className={`enlace-menu ${vista === "buscar" ? "activo" : ""}`}
            onClick={() => setVista("buscar")}
          >
            Buscar viaje
          </button>

          <button
            className={`enlace-menu ${
              vista === "misViajes" ? "activo" : ""
            }`}
            onClick={() => setVista("misViajes")}
          >
            Mis viajes
          </button>
        </nav>

        <button className="boton-salir" onClick={cerrarSesion}>
          Cerrar sesión
        </button>
      </header>

      <section className="contenido">
        {vista === "inicio" && (
          <>
            <div className="bienvenida">
              <div>
                <p className="texto-superior">TU ÁREA PERSONAL</p>
                <h1>Hola, {usuario.nombre}</h1>
                <p>
                  Consulta horarios, encuentra tu próximo destino y gestiona
                  tus reservas.
                </p>
              </div>
              <div className="icono-tren">🚆</div>
            </div>

            <section>
              <h2>¿Qué quieres hacer?</h2>

              <div className="opciones">
                <button
                  className="tarjeta-opcion"
                  onClick={() => setVista("buscar")}
                >
                  <span className="icono-tarjeta">🔎</span>
                  <strong>Buscar viaje</strong>
                  <small>Consulta trayectos, fechas y horarios disponibles.</small>
                  <span className="flecha">→</span>
                </button>

                <button
                  className="tarjeta-opcion"
                  onClick={() => setVista("misViajes")}
                >
                  <span className="icono-tarjeta">🎫</span>
                  <strong>Mis viajes</strong>
                  <small>Revisa tus próximas reservas y viajes realizados.</small>
                  <span className="flecha">→</span>
                </button>
              </div>
            </section>
          </>
        )}

        {vista === "buscar" && (
          <>
            <button className="volver" onClick={volverInicio}>
              ← Volver al inicio
            </button>

            <h1 className="titulo-pagina">Buscar viaje</h1>
            <p className="subtitulo">
              Selecciona tu origen, destino y fecha de salida.
            </p>

            <form className="formulario-viaje" onSubmit={buscarViajes}>
              <div>
                <label htmlFor="origen">Origen</label>
                <select
                  id="origen"
                  value={origenId}
                  onChange={(evento) => {
                    setOrigenId(evento.target.value);
                    setDestinoId("");
                  }}
                >
                  <option value="">Selecciona origen</option>
                  {origenes.map((origen) => (
                    <option key={origen.id} value={origen.id}>
                      {origen.nombre}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label htmlFor="destino">Destino</label>
                <select
                  id="destino"
                  value={destinoId}
                  disabled={!origenId}
                  onChange={(evento) => setDestinoId(evento.target.value)}
                >
                  <option value="">Selecciona destino</option>
                  {destinosDisponibles.map((destino) => (
                    <option key={destino.id} value={destino.id}>
                      {destino.nombre}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label htmlFor="fecha">Fecha de salida</label>
                <input
                  id="fecha"
                  type="date"
                  value={fecha}
                  onChange={(evento) => setFecha(evento.target.value)}
                />
              </div>

              <button className="boton-principal boton-buscar" type="submit">
                Buscar viajes
              </button>
            </form>

            {errorBusqueda && <p className="error">{errorBusqueda}</p>}

            {busquedaRealizada && (
              <section className="resultados">
                <h2>Viajes disponibles</h2>

                {viajes.length === 0 ? (
                  <p className="sin-resultados">
                    No hay viajes disponibles para esta búsqueda.
                  </p>
                ) : (
                  viajes.map((viaje) => (
                    <article className="viaje viaje-detallado" key={viaje.id}>
                      <div className="viaje-ruta">
                        <div className="hora-estacion">
                          <strong>{formatearHora(viaje.horaSalida)} h</strong>
                          <span>{viaje.origen}</span>
                          <span className="tren">{viaje.tren}</span>
                        </div>

                        <div className="conexion">
                          <span className="duracion-total">
                            Viaje disponible
                          </span>
                          <div className="linea-ruta"></div>
                          <span>{viaje.fecha}</span>
                        </div>

                        <div className="hora-estacion">
                          <strong>{formatearHora(viaje.horaLlegada)} h</strong>
                          <span>{viaje.destino}</span>
                          <span className="tren">{viaje.tren}</span>
                        </div>
                      </div>

                      <aside className="disponibilidad disponible">
                        <span>✓</span>
                        <strong>Desde {viaje.precio} €</strong>
                        <small>{viaje.estado}</small>
                        <button onClick={() => seleccionarViaje(viaje)}>
                          Seleccionar
                        </button>
                      </aside>

                      <footer className="viaje-pie">
                        ♿ Plaza H disponible <span>•</span> 🌿 Cero emisiones
                      </footer>
                    </article>
                  ))
                )}
              </section>
            )}
          </>
        )}

        {vista === "asientos" && (
          <>
            <button className="volver" onClick={() => setVista("buscar")}>
              ← Volver a los viajes
            </button>

            <h1 className="titulo-pagina">Selecciona tu asiento</h1>
            <p className="subtitulo">
              {viajeSeleccionado?.origen} (
              {formatearHora(viajeSeleccionado?.horaSalida)} h) →{" "}
              {viajeSeleccionado?.destino} (
              {formatearHora(viajeSeleccionado?.horaLlegada)} h)
            </p>

            <section className="leyenda-asientos">
              <span><i className="asiento libre"></i> Disponible</span>
              <span><i className="asiento ocupado"></i> Ocupado</span>
              <span><i className="asiento elegido"></i> Seleccionado</span>
            </section>

            {cargandoAsientos && <p>Cargando asientos disponibles...</p>}

            {errorAsientos && <p className="error">{errorAsientos}</p>}

            {!cargandoAsientos &&
              !errorAsientos &&
              vagones.map((vagon) => (
                <section className="vagon" key={vagon.vagon}>
                  <h2>Vagón {vagon.vagon}</h2>

                  <div className="asientos">
                    {vagon.asientos.map((asiento) => {
                      const seleccionado =
                        asientoSeleccionado === asiento.asiento;

                      return (
                        <button
                          key={asiento.asiento}
                          disabled={asiento.ocupado}
                          onClick={() =>
                            seleccionarAsiento(asiento.asiento, asiento.ocupado)
                          }
                          className={`asiento ${
                            asiento.ocupado
                              ? "ocupado"
                              : seleccionado
                                ? "elegido"
                                : "libre"
                          }`}
                        >
                          {asiento.asiento.replace(`V${vagon.vagon}-`, "")}
                        </button>
                      );
                    })}
                  </div>
                </section>
              ))}

            <section className="resumen-asiento">
              <div>
                <strong>Asiento seleccionado</strong>
                <p>{asientoSeleccionado ?? "Todavía no has elegido asiento"}</p>
              </div>

              <div>
                <strong>Precio del viaje</strong>
                <p>{viajeSeleccionado?.precio} €</p>
              </div>

              <button
                className="boton-principal"
                disabled={!asientoSeleccionado}
                onClick={() => setVista("confirmacion")}
              >
                Continuar
              </button>
            </section>
          </>
        )}

        {vista === "confirmacion" && (
          <>
            <button className="volver" onClick={() => setVista("asientos")}>
              ← Volver a los asientos
            </button>

            {!reservaConfirmada ? (
              <section className="confirmacion">
                <p className="marca">RESUMEN DE RESERVA</p>
                <h1>Confirma tu viaje</h1>

                <div className="datos-reserva">
                  <div>
                    <span>Trayecto</span>
                    <strong>
                      {viajeSeleccionado?.origen} → {viajeSeleccionado?.destino}
                    </strong>
                  </div>

                  <div>
                    <span>Horario</span>
                    <strong>
                      {formatearHora(viajeSeleccionado?.horaSalida)} h →{" "}
                      {formatearHora(viajeSeleccionado?.horaLlegada)} h
                    </strong>
                  </div>

                  <div>
                    <span>Asiento</span>
                    <strong>{asientoSeleccionado}</strong>
                  </div>

                  <div>
                    <span>Precio</span>
                    <strong>{viajeSeleccionado?.precio} €</strong>
                  </div>
                </div>

                {errorReserva && <p className="error">{errorReserva}</p>}
                <button className="boton-principal" onClick={confirmarReserva}>
                  Confirmar reserva
                </button>
              </section>
            ) : (
              <section className="confirmacion reserva-exitosa">
                <div className="icono-exito">✓</div>
                <p className="marca">RESERVA CONFIRMADA</p>
                <h1>¡Tu viaje está reservado!</h1>
                <p className="subtitulo">
                  Tu asiento {asientoSeleccionado} ha quedado reservado.
                </p>

                <button className="boton-principal" onClick={volverInicio}>
                  Volver al inicio
                </button>
              </section>
            )}
          </>
        )}

        {vista === "misViajes" && (
          <>
            <button className="volver" onClick={volverInicio}>
              ← Volver al inicio
            </button>

            <h1 className="titulo-pagina">Mis viajes</h1>

            {misViajes.length === 0 ? (
              <section className="sin-viajes">
                <div>🎫</div>
                <h2>Aún no tienes viajes reservados</h2>
                <p>Cuando confirmes una reserva, aparecerá aquí.</p>
              </section>
            ) : (
              <section className="lista-mis-viajes">
                {misViajes.map((viaje) => (
                  <article className="mi-viaje" key={viaje.id}>
                    <div className="fecha-viaje">
                      <span>Fecha</span>
                      <strong>{viaje.fecha}</strong>
                    </div>

                    <div className="trayecto-viaje">
                      <strong>
                        {formatearHora(viaje.horaSalida)} h —{" "}
                        {formatearHora(viaje.horaLlegada)} h
                      </strong>
                      <span>
                        {viaje.origen} → {viaje.destino}
                      </span>
                    </div>

                    <div className="detalle-viaje">
                      <span>Asiento</span>
                      <strong>{viaje.asiento}</strong>
                    </div>

                    <div className="precio-viaje">
                      <span>Precio</span>
                      <strong>{viaje.precio} €</strong>
                    </div>
                  </article>
                ))}
              </section>
            )}
          </>
        )}
      </section>
    </main>
  );
}

export default App;