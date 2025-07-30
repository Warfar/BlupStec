/* ====== Menú lateral ====== */
function AbrirMenu() {
  document.getElementById("navegacion").style.left = "0";
}
document.getElementById("cerrar").addEventListener("click", function () {
  document.getElementById("navegacion").style.left = "-250px";
});

document.addEventListener("DOMContentLoaded", function() {
  fetch('/api/usuarios')  // ¡Aquí cambiar de /usuarios a /api/usuarios!
    .then(response => response.json())
    .then(data => {
      const tbody = document.getElementById('usuariosBody');
      tbody.innerHTML = ''; // limpiar antes

      data.forEach((u, index) => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td>${index + 1}</td>
          <td><img src="/uploads/${u.perfil}" alt="perfil" style="width:32px; height:32px; border-radius:50%;"></td>
          <td>${u.nombre}</td>
          <td>${u.email}</td>
          <td>${u.username}</td>
          <td>${u.passwordHash}</td>
          <td>${u.rol}</td>
          <td>${u.activo ? 'Activo' : 'Inactivo'}</td>
          <td>${u.telefono || ''}</td>
          <td>${new Date(u.fecha).toLocaleDateString()}</td>
          <td>
            <button class="editar" class="accion" id="accion"><span
                                            class="material-symbols-light--edit-outline"></span></button>
            <button class="eliminar" data-id="${u.id}"><span class="ic--outline-delete"></span></button>
          </td>
        `;
        tbody.appendChild(tr);
      });
    })
    .catch(error => {
      console.error('Error cargando usuarios:', error);
    });
});


function abrirModal(){
  const modal = document.getElementById("modal_overlay");
  modal.style.display = "flex";
}

function cerrarModal(){
  const modal = document.getElementById("modal_overlay");
  modal.style.display = "none";
}



document.getElementById('imagenPerfil').addEventListener('change', function(event) {
    const file = event.target.files[0]; // el archivo seleccionado
    const preview = document.getElementById('previewImg'); // tu etiqueta <img>

    if (file) {
        const reader = new FileReader();

        reader.onload = function(e) {
            preview.src = e.target.result; // actualiza la imagen con el contenido cargado
        };

        reader.readAsDataURL(file); // convierte la imagen a base64
    }
});

// Delegación: escucha en el tbody donde se agregan dinámicamente los botones
document.getElementById('usuariosBody').addEventListener('click', function(e) {
  if (e.target.closest('.eliminar')) {
    const btn = e.target.closest('.eliminar');
    const userId = btn.getAttribute('data-id');

    if (confirm('¿Estás seguro de eliminar este usuario?')) {
      fetch(`/api/usuarios/${userId}`, {
        method: 'DELETE'
      })
      .then(res => {
        if (res.ok) {
          alert('Usuario eliminado con éxito');
          location.reload(); // actualiza la tabla
        } else {
          alert('Error al eliminar usuario');
        }
      })
      .catch(err => console.error('Error al eliminar:', err));
    }
  }
});


//buscar
document.getElementById('buscar').addEventListener('input', function() {
  const textoBusqueda = this.value.toLowerCase();
  const filas = document.querySelectorAll('#usuariosBody tr');

  filas.forEach(fila => {
    const nombre = fila.cells[2].textContent.toLowerCase(); // columna "Nombre"
    if (nombre.includes(textoBusqueda)) {
      fila.style.display = ''; // mostrar fila
    } else {
      fila.style.display = 'none'; // ocultar fila
    }
  });
});
