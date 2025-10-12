package si2024_pl34.historiaActualizarPericial;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

public class ActualizarPericialController {

    private ActualizarPericialModel model;
    private ActualizarPericialView view;

    public ActualizarPericialController(ActualizarPericialModel model, ActualizarPericialView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        cargarPericiales();
        view.setAceptarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aceptarPericial();
            }
        });
        view.setRechazarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rechazarPericial();
            }
        });
        view.setCancelarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarPericial();
            }
        });
    }

    private void cargarPericiales() {
        List<ActualizarPericialDTO> lista = model.obtenerPericialesAsignadas();
        view.actualizarTabla(lista);
    }

    private boolean esPericialEditable(String estado) {
        return estado.equalsIgnoreCase("Asignado");
    }

    private void aceptarPericial() {
        String referencia = view.getReferenciaSeleccionada();
        String estado = view.getEstadoSeleccionadoPericial();

        if (referencia == null) {
            view.mostrarMensaje("Seleccione una pericial.");
            return;
        }

        if (!esPericialEditable(estado)) {
            view.mostrarMensaje("Esta pericial ya ha sido rechazada o cancelada. No se puede modificar.");
            return;
        }

        boolean exito = model.actualizarEstado(referencia, "Aceptado", null, null);
        if (exito) {
            view.mostrarMensaje("Pericial aceptada correctamente.");
            view.actualizarHistorial(referencia, "Aceptada", "");
            view.limpiarMotivo(); // Limpia el campo de texto tras la acción
            cargarPericiales();
        } else {
            view.mostrarMensaje("No se pudo aceptar la pericial.");
        }
    }

    private void rechazarPericial() {
        String referencia = view.getReferenciaSeleccionada();
        String estado = view.getEstadoSeleccionadoPericial();
        String motivo = view.getMotivo();

        if (referencia == null || motivo.isEmpty()) {
            view.mostrarMensaje("Seleccione una pericial y escriba el motivo de rechazo.");
            return;
        }

        if (!esPericialEditable(estado)) {
            view.mostrarMensaje("Esta pericial ya ha sido rechazada o cancelada. No se puede modificar.");
            return;
        }

        boolean exito = model.actualizarEstado(referencia, "Rechazado", motivo, LocalDate.now().toString());
        if (exito) {
            view.mostrarMensaje("Pericial rechazada correctamente.");
            view.actualizarHistorial(referencia, "Rechazada", motivo);
            view.limpiarMotivo(); // Limpia el campo de texto tras la acción
            cargarPericiales();
        } else {
            view.mostrarMensaje("No se pudo rechazar la pericial.");
        }
    }

    private void cancelarPericial() {
        String referencia = view.getReferenciaSeleccionada();
        String estado = view.getEstadoSeleccionadoPericial();
        String motivo = view.getMotivo();

        if (referencia == null || motivo.isEmpty()) {
            view.mostrarMensaje("Seleccione una pericial y escriba el motivo de cancelación.");
            return;
        }

        if (!esPericialEditable(estado)) {
            view.mostrarMensaje("Esta pericial ya ha sido rechazada o cancelada. No se puede modificar.");
            return;
        }

        boolean exito = model.actualizarEstado(referencia, "Cancelado", motivo, LocalDate.now().toString());
        if (exito) {
            view.mostrarMensaje("Pericial cancelada correctamente.");
            view.actualizarHistorial(referencia, "Cancelada", motivo);
            view.limpiarMotivo(); // Limpia el campo de texto tras la acción
            cargarPericiales();
        } else {
            view.mostrarMensaje("No se pudo cancelar la pericial.");
        }
    }
}