function AbrirMenu() {
  document.getElementById("navegacion").style.left = "0";
}
document.getElementById("cerrar").addEventListener("click", function () {
  document.getElementById("navegacion").style.left = "-250px";
});

  // ----- Paginación
  const ticketsPorPagina = 10;
  let paginaActual = 1;
  const totalPaginas = Math.ceil(tickets.length / ticketsPorPagina);

  function mostrarTickets() {
    // preservamos la fila 0 fija
    const fixedHTML = fixedRow ? fixedRow.outerHTML : "";
    let inicio = (paginaActual - 1) * ticketsPorPagina;
    let fin = inicio + ticketsPorPagina;

    const ticketsPagina = tickets.slice(inicio, fin);

    const rows = ticketsPagina
      .map(
        (t) => `
        <tr>
          <td>${t.id}</td>
          <td>${t.titulo}</td>
          <td>${t.estado}</td>
          <td>${t.prioridad}</td>
          <td>${t.nombre}</td>
          <td>${t.apellido}</td>
          <td>${t.telefono}</td>
          <td>${t.creado}</td>
          <td>
            <div class="accion">
              <button class="editar"><span class="material-symbols-light--edit-outline"></span></button>
              <button class="eliminar"><span class="ic--outline-delete"></span></button>
            </div>
          </td>
        </tr>`
      )
      .join("");

    ticketsBody.innerHTML = fixedHTML + rows;
  }

  function crearBotones() {
    paginaNumeros.innerHTML = "";
    for (let i = 1; i <= totalPaginas; i++) {
      const btn = document.createElement("button");
      btn.textContent = i;
      if (i === paginaActual) btn.classList.add("active");
      btn.addEventListener("click", () => {
        paginaActual = i;
        actualizar();
      });
      paginaNumeros.appendChild(btn);
    }
  }

  function actualizar() {
    mostrarTickets();
    crearBotones();
    prev.disabled = paginaActual === 1;
    next.disabled = paginaActual === totalPaginas;
  }

  prev.addEventListener("click", () => {
    if (paginaActual > 1) {
      paginaActual--;
      actualizar();
    }
  });

  next.addEventListener("click", () => {
    if (paginaActual < totalPaginas) {
      paginaActual++;
      actualizar();
    }
  });

  // Init
  actualizar();

