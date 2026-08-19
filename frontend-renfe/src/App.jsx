import { useState } from "react";

const letrasAsiento = ["A", "B", "C", "D"];

const asientosOcupados = ["V1-1C", "V1-3B", "V1-5D", "V2-2A", "V2-4C"];

function App() {
  const [usuario, setUsuario] = useState("");
  const [contrasena, setContrasena] = useState("");
  const [sesionIniciada, setSesionIniciada] = useState(false);
  const [error, setError] = useState("");

  const [vista, setVista] = useState("inicio");
  const [origen, setOrigen] = useState("");
  const [destino, setDestino] = useState("");
  const [fecha, setFecha] = useState("");
  const [busquedaRealizada, setBusquedaRealizada] = useState(false);
  const [errorBusqueda, setErrorBusqueda] = useState("");

  const [viajeSeleccionado, setViajeSeleccionado] = useState(null);
  const [asientoSeleccionado, setAsientoSeleccionado] = useState(null);

  function iniciarSesion(evento) {
    evento.preventDefault();

    if (!usuario.trim() || !contrasena.trim()) {
      setError("Introduce tu usuario y tu contraseña.");
      return;
    }

    setError("");
    setSesionIniciada(true);
  }

  function cerrarSesion() {
    setSesionIniciada(false);
    setUsuario("");
    setContrasena("");
    setVista("inicio");
  }

  function buscarViajes(evento) {
    evento.preventDefault();

    if (!origen.trim() || !destino.trim() || !fecha) {
      setErrorBusqueda("Completa origen, destino y fecha.");
      return;
    }

    setErrorBusqueda("");
    setBusquedaRealizada(true);
  }

  function volverInicio() {
    setVista("inicio");
    setBusquedaRealizada(false);
  }

  function seleccionarViaje(horaSalida, horaLlegada, precio) {
    setViajeSeleccionado({
      horaSalida,
      horaLlegada,
      precio,
    });

    setAsientoSeleccionado(null);
    setVista("asientos");
  }

  function seleccionarAsiento(asiento, ocupado) {
    if (!ocupado) {
      setAsientoSeleccionado(asiento);
    }
  }

  if (!sesionIniciada) {
    return (
      <main className="pagina">
        <section className="login">
          <p className="marca">RENFE</p>
          <h1>Bienvenido</h1>
          <p className="subtitulo">Inicia sesión para gestionar tus viajes.</p>

          <form onSubmit={iniciarSesion}>
            <label htmlFor="usuario">Usuario</label>
            <input
              id="usuario"
              type="text"
              value={usuario}
              onChange={(evento) => setUsuario(evento.target.value)}
              placeholder="Introduce tu usuario"
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

          <button className="enlace-menu">Mis viajes</button>
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
                <h1>Hola, {usuario}</h1>
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

                <button className="tarjeta-opcion">
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
              Indica los datos de tu trayecto para ver los viajes disponibles.
            </p>

            <form className="formulario-viaje" onSubmit={buscarViajes}>
              <div>
                <label htmlFor="origen">Origen</label>
                <input
                  id="origen"
                  type="text"
                  value={origen}
                  onChange={(evento) => setOrigen(evento.target.value)}
                  placeholder="Ejemplo: Madrid"
                />
              </div>

              <div>
                <label htmlFor="destino">Destino</label>
                <input
                  id="destino"
                  type="text"
                  value={destino}
                  onChange={(evento) => setDestino(evento.target.value)}
                  placeholder="Ejemplo: Barcelona"
                />
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
                <h2>
                  Viajes de {origen} a {destino}
                </h2>

                <article className="viaje viaje-detallado">
                  <div className="viaje-ruta">
                    <div className="hora-estacion">
                      <strong>07:30 h</strong>
                      <span>{origen}</span>
                      <span className="tren">AVE</span>
                    </div>

                    <div className="conexion">
                      <span className="duracion-total">2 horas 45 minutos</span>
                      <div className="linea-ruta"></div>
                      <span>Directo</span>
                    </div>

                    <div className="hora-estacion">
                      <strong>10:15 h</strong>
                      <span>{destino}</span>
                      <span className="tren">AVE</span>
                    </div>
                  </div>

                  <aside className="disponibilidad disponible">
                    <span>✓</span>
                    <strong>Desde 35 €</strong>
                    <small>Plazas disponibles</small>
                    <button
                      onClick={() =>
                        seleccionarViaje("07:30 h", "10:15 h", "35 €")
                      }
                    >
                      Seleccionar
                    </button>
                  </aside>

                  <footer className="viaje-pie">
                    ♿ Plaza H disponible <span>•</span> 🌿 Cero emisiones
                  </footer>
                </article>

                <article className="viaje viaje-detallado">
                  <div className="viaje-ruta">
                    <div className="hora-estacion">
                      <strong>10:00 h</strong>
                      <span>{origen}</span>
                      <span className="tren">AVE</span>
                    </div>

                    <div className="conexion">
                      <span className="duracion-total">3 horas 10 minutos</span>
                      <div className="linea-ruta"></div>
                      <span>Directo</span>
                    </div>

                    <div className="hora-estacion">
                      <strong>13:10 h</strong>
                      <span>{destino}</span>
                      <span className="tren">ALVIA</span>
                    </div>
                  </div>

                  <aside className="disponibilidad completo">
                    <span>!</span>
                    <strong>Tren completo</strong>
                    <small>No quedan plazas</small>
                  </aside>

                  <footer className="viaje-pie">
                    ♿ Plaza H disponible <span>•</span> 🌿 Cero emisiones
                  </footer>
                </article>
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
              {origen} ({viajeSeleccionado?.horaSalida}) → {destino} (
              {viajeSeleccionado?.horaLlegada})
            </p>

            <section className="leyenda-asientos">
              <span><i className="asiento libre"></i> Disponible</span>
              <span><i className="asiento ocupado"></i> Ocupado</span>
              <span><i className="asiento elegido"></i> Seleccionado</span>
            </section>

            {[1, 2].map((numeroVagon) => (
              <section className="vagon" key={numeroVagon}>
                <h2>Vagón {numeroVagon}</h2>

                <div className="asientos">
                  {Array.from({ length: 24 }, (_, indice) => {
                    const fila = Math.floor(indice / 4) + 1;
                    const letra = letrasAsiento[indice % 4];
                    const asiento = `V${numeroVagon}-${fila}${letra}`;
                    const ocupado = asientosOcupados.includes(asiento);
                    const seleccionado = asientoSeleccionado === asiento;

                    return (
                      <button
                        key={asiento}
                        disabled={ocupado}
                        onClick={() => seleccionarAsiento(asiento, ocupado)}
                        className={`asiento ${
                          ocupado
                            ? "ocupado"
                            : seleccionado
                              ? "elegido"
                              : "libre"
                        }`}
                      >
                        {fila}
                        {letra}
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
                <p>{viajeSeleccionado?.precio}</p>
              </div>

              <button
                className="boton-principal"
                disabled={!asientoSeleccionado}
              >
                Continuar
              </button>
            </section>
          </>
        )}
      </section>
    </main>
  );
}

export default App;