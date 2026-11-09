function irA(event, url) {
  window.location.href = url;
}

let alarmaActual = null;

document.addEventListener('DOMContentLoaded', function () {
  activarBotones();
});

function activarBotones() {

  // ----- EDITAR -----
  document.querySelectorAll('.btn-edit').forEach(btn => {
    btn.addEventListener('click', function () {
      alarmaActual = this.closest('.alarm');

      const tiempoActual = alarmaActual.querySelector('.alarm__time').textContent;
      const nombreActual = alarmaActual.querySelector('.alarm__name').textContent;
      const paradaActual = alarmaActual.querySelector('.alarm__stop').textContent;

      document.getElementById('inputTiempoAlarma').value = tiempoActual;
      document.getElementById('inputNombreAlarma').value = nombreActual;
      document.getElementById('inputParadaAlarma').value = paradaActual;

      const modal = new bootstrap.Modal(document.getElementById('modalEditar'));
      modal.show();
    });
  });

  // Confirmar edición
  document.getElementById('btnConfirmarEditar').addEventListener('click', function () {
    const nuevoTiempo = document.getElementById('inputTiempoAlarma').value.trim();
    const nuevoNombre = document.getElementById('inputNombreAlarma').value.trim();
    const nuevaParada = document.getElementById('inputParadaAlarma').value.trim();

    if (nuevoTiempo) {
      alarmaActual.querySelector('.alarm__time').textContent = nuevoTiempo;
    }
    if (nuevoNombre) {
      alarmaActual.querySelector('.alarm__name').textContent = nuevoNombre;
    }
    if (nuevaParada) {
      paradaActual.querySelector('.alarm__stop').textContent = nuevaParada;
    }

    bootstrap.Modal.getInstance(document.getElementById('modalEditar')).hide();
  });

  // ----- ELIMINAR -----
  document.querySelectorAll('.btn-delete').forEach(btn => {
    btn.addEventListener('click', function () {
      alarmaActual = this.closest('.alarm');
      const modal = new bootstrap.Modal(document.getElementById('modalEliminar'));
      modal.show();
    });
  });

  // Confirmar eliminación
  document.getElementById('btnConfirmarEliminar').addEventListener('click', function () {
    if (alarmaActual) {
      alarmaActual.remove();
    }
    bootstrap.Modal.getInstance(document.getElementById('modalEliminar')).hide();
  });
}