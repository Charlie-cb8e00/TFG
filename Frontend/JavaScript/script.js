function irA(event, url) {
  window.location.href = url;
}

document.addEventListener('DOMContentLoaded', function () {
  activarAlarmas();
  activarMonedero();
});

function activarAlarmas() {
  const contenidoAlarmas = document.querySelector('.content[aria-label="Alarmas configuradas"]');
  const modalEditarElement = document.getElementById('modalEditar');
  const formularioAlarma = document.getElementById('formAlarma');
  if (!contenidoAlarmas || !modalEditarElement || !formularioAlarma) return;

  const modalEditar = bootstrap.Modal.getOrCreateInstance(modalEditarElement);
  const modalEliminarElement = document.getElementById('modalEliminar');
  const modalEliminar = bootstrap.Modal.getOrCreateInstance(modalEliminarElement);
  let alarmaActual = null;

  document.getElementById('btnNuevaAlarma').addEventListener('click', function () {
    alarmaActual = null;
    formularioAlarma.reset();
    document.getElementById('modalEditarLabel').textContent = 'Añadir alarma';
    document.getElementById('btnConfirmarEditar').textContent = 'Añadir';
    modalEditar.show();
  });

  contenidoAlarmas.addEventListener('click', function (event) {
    const boton = event.target.closest('.btn-edit, .btn-delete');
    if (!boton) return;

    alarmaActual = boton.closest('.alarm');
    if (boton.classList.contains('btn-edit')) {
      document.getElementById('inputTiempoAlarma').value = alarmaActual.querySelector('.alarm__time').textContent;
      document.getElementById('inputNombreAlarma').value = alarmaActual.querySelector('.alarm__name').textContent;
      document.getElementById('inputParadaAlarma').value = alarmaActual.querySelector('.alarm__stop').textContent;
      document.getElementById('modalEditarLabel').textContent = 'Editar alarma';
      document.getElementById('btnConfirmarEditar').textContent = 'Confirmar';
      modalEditar.show();
    } else {
      modalEliminar.show();
    }
  });

  formularioAlarma.addEventListener('submit', function (event) {
    event.preventDefault();
    const nuevoTiempo = document.getElementById('inputTiempoAlarma').value.trim();
    const nuevoNombre = document.getElementById('inputNombreAlarma').value.trim();
    const nuevaParada = document.getElementById('inputParadaAlarma').value.trim();

    if (alarmaActual) {
      alarmaActual.querySelector('.alarm__time').textContent = nuevoTiempo;
      alarmaActual.querySelector('.alarm__name').textContent = nuevoNombre;
      alarmaActual.querySelector('.alarm__stop').textContent = nuevaParada;
    } else {
      const alarma = document.createElement('div');
      alarma.className = 'alarm';

      const tiempo = document.createElement('span');
      tiempo.className = 'alarm__time';
      tiempo.textContent = nuevoTiempo;

      const info = document.createElement('span');
      info.className = 'alarm__info';

      const nombre = document.createElement('strong');
      nombre.className = 'alarm__name';
      nombre.textContent = nuevoNombre;

      const parada = document.createElement('strong');
      parada.className = 'alarm__stop';
      parada.textContent = nuevaParada;

      const acciones = document.createElement('span');
      acciones.className = 'alarm__actions';
      const botonEditar = document.createElement('button');
      botonEditar.type = 'button';
      botonEditar.className = 'btn-alarm btn-edit';
      botonEditar.textContent = 'Editar';

      const botonEliminar = document.createElement('button');
      botonEliminar.type = 'button';
      botonEliminar.className = 'btn-alarm btn-delete';
      botonEliminar.textContent = 'Eliminar';

      acciones.append(botonEditar, botonEliminar);

      info.append(nombre, parada, acciones);
      alarma.append(tiempo, info);
      contenidoAlarmas.append(alarma);
    }

    modalEditar.hide();
  });

  document.getElementById('btnConfirmarEliminar').addEventListener('click', function () {
    if (alarmaActual) {
      alarmaActual.remove();
    }
    modalEliminar.hide();
  });
}

function activarMonedero() {
  const formularioTarjeta = document.getElementById('formTarjeta');
  const listaTarjetas = document.querySelector('.list-group');
  if (!formularioTarjeta || !listaTarjetas) return;

  const modalTarjeta = bootstrap.Modal.getOrCreateInstance(document.getElementById('modalNuevaTarjeta'));
  document.getElementById('btnNuevaTarjeta').addEventListener('click', function () {
    formularioTarjeta.reset();
    modalTarjeta.show();
  });

  formularioTarjeta.addEventListener('submit', function (event) {
    event.preventDefault();
    const tarjeta = document.createElement('div');
    tarjeta.className = 'wallet-card list-group-item d-flex align-items-center gap-3 py-3';

    const icono = document.createElement('span');
    icono.className = 'wallet-card__icon rounded';
    icono.setAttribute('aria-hidden', 'true');
    icono.textContent = '🚌';

    const informacion = document.createElement('div');
    informacion.className = 'min-w-0 flex-grow-1';

    const nombre = document.createElement('h2');
    nombre.className = 'h6 mb-1';
    nombre.textContent = document.getElementById('inputNombreTarjeta').value.trim();

    const descripcion = document.createElement('p');
    descripcion.className = 'text-secondary small mb-0';
    descripcion.textContent = document.getElementById('inputDetalleTarjeta').value.trim();

    informacion.append(nombre, descripcion);
    tarjeta.append(icono, informacion);
    listaTarjetas.append(tarjeta);
    modalTarjeta.hide();
  });
}