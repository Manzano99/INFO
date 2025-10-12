package si2024_pl34.historiaCancelarInscripcion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Comparator;
import java.util.List;
import si2024_pl34.util.ThemeManager;

public class CancelarInscripcionController {

    private final CancelarInscripcionModel model;
    private final CancelarInscripcionView view;
    private int idColegiadoActual = -1;
    private List<CancelarInscripcionModel.CursoInscritoDTO> cursosInscritosActuales;

    public CancelarInscripcionController(CancelarInscripcionModel model, CancelarInscripcionView view) {
        this.model = model;
        this.view = view;
        ThemeManager themeManager = new ThemeManager();
        themeManager.establecerTemaComponente(view, null);
        initController();
        view.setVisible(true);
    }

    private void initController() {
        view.getBtnBuscar().addActionListener(e -> buscarColegiadoYMostrarCursos());
        view.getBtnCancelarInscripcion().addActionListener(e -> cancelarInscripcionSeleccionada());
        view.getComboOrdenar().addActionListener(e -> ordenarYCargarCursosEnTabla());
    }

    // Buscar colegiado y mostrar cursos en la tabla
    private void buscarColegiadoYMostrarCursos() {
        // Limpiar tabla antes de buscar
        limpiarTabla();
        // Obtener el número de colegiado
        String numColegiado = view.getTxtNumColegiado().getText().trim();
        if (numColegiado.isEmpty()) {
            mostrarError("Introduce el nº de colegiado.");
            return;
        }
        idColegiadoActual = model.buscarColegiadoPorNumero(numColegiado);
        if (idColegiadoActual == -1) {
            mostrarError("No existe colegiado con ese número.");
            limpiarTabla();
            return;
        }
        cursosInscritosActuales = model.listarCursosInscritos(idColegiadoActual);
        ordenarYCargarCursosEnTabla();
    }

    // Ordena la lista según el combo y carga la tabla
    private void ordenarYCargarCursosEnTabla() {
        if (cursosInscritosActuales == null) {
            limpiarTabla();
            return;
        }
        String criterio = (String) view.getComboOrdenar().getSelectedItem();
        if (criterio != null) {
            switch (criterio) {
                case "Fecha de Inicio":
                    cursosInscritosActuales.sort(Comparator.comparing(CancelarInscripcionModel.CursoInscritoDTO::getFechaInicio, Comparator.nullsLast(String::compareTo)));
                    break;
                case "Fecha de Fin":
                    cursosInscritosActuales.sort(Comparator.comparing(CancelarInscripcionModel.CursoInscritoDTO::getFechaFin, Comparator.nullsLast(String::compareTo)));
                    break;
                case "Título":
                    cursosInscritosActuales.sort(Comparator.comparing(CancelarInscripcionModel.CursoInscritoDTO::getTitulo, Comparator.nullsLast(String::compareToIgnoreCase)));
                    break;
                case "Cantidad Pagada":
                    cursosInscritosActuales.sort(Comparator.comparingDouble(CancelarInscripcionModel.CursoInscritoDTO::getCantidadPagada).reversed());
                    break;
                case "Fecha Cancelación":
                    cursosInscritosActuales.sort(Comparator.comparing(CancelarInscripcionModel.CursoInscritoDTO::getFechaCancelacion, Comparator.nullsLast(String::compareTo)));
                    break;
                case "Estado Inscripción":
                    cursosInscritosActuales.sort(Comparator.comparing(CancelarInscripcionModel.CursoInscritoDTO::getEstadoInscripcion, Comparator.nullsLast(String::compareToIgnoreCase)));
                    break;
            }
        }
        cargarCursosEnTabla();
    }

    // Cargar cursos en la tabla
    private void cargarCursosEnTabla() {
        DefaultTableModel tableModel = new DefaultTableModel(
                new String[] {
                        "Título", "Fecha Inicio", "Fecha Fin", "Estado Inscripción", "Fecha Inscripción",
                        "Método Pago", "Fecha Pago", "Cantidad Pagada (€)", "Fecha Cancelación", "Cantidad Devuelta (€)", "Admite cancelación", "Nº Lista Espera"
                }, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        if (cursosInscritosActuales != null) {
            for (CancelarInscripcionModel.CursoInscritoDTO dto : cursosInscritosActuales) {
                tableModel.addRow(new Object[] {
                        dto.getTitulo(),
                        formatearFecha(dto.getFechaInicio()),
                        formatearFecha(dto.getFechaFin()),
                        dto.getEstadoInscripcion(),
                        formatearFecha(dto.getFechaInscripcion()),
                        dto.getMetodoPago(),
                        formatearFecha(dto.getFechaPago()),
                        dto.getCantidadPagada() > 0 ? String.format("%.2f", dto.getCantidadPagada()) : "-",
                        formatearFecha(dto.getFechaCancelacion()),
                        dto.getCantidadDevuelta() > 0 ? String.format("%.2f", dto.getCantidadDevuelta()) : "-",
                        dto.isPermiteCancelacion() ? "Sí" : "No",
                        dto.getNListaEspera() > 0 ? dto.getNListaEspera() : "-"
                });
            }
        }
        view.getTablaCursosInscritos().setModel(tableModel);
    }

    // Cancelar inscripción seleccionada si procede
    private void cancelarInscripcionSeleccionada() {
        int row = view.getTablaCursosInscritos().getSelectedRow();
        if (row == -1 || cursosInscritosActuales == null || row >= cursosInscritosActuales.size()) {
            mostrarError("Selecciona un curso para cancelar la inscripción.");
            return;
        }
        CancelarInscripcionModel.CursoInscritoDTO dto = cursosInscritosActuales.get(row);
        // No permitir cancelar si ya está cancelada
        if ("Cancelada".equalsIgnoreCase(dto.getEstadoInscripcion())) {
            mostrarError("Este curso ya está cancelado.");
            return;
        }
        if (!dto.isPermiteCancelacion()) {
            mostrarError("Este curso no permite cancelación.");
            return;
        }
        if (dto.getFechaLimiteCancelacion() == null) {
            mostrarError("No hay fecha límite de cancelación definida.");
            return;
        }
        String hoy = java.time.LocalDate.now().toString();
        if (hoy.compareTo(dto.getFechaLimiteCancelacion()) > 0) {
            mostrarError("El plazo de cancelación ha finalizado.");
            return;
        }
        
        // Mostrar el porcentaje de devolución en el mensaje de confirmación
        String mensaje = String.format("¿Seguro que deseas cancelar la inscripción?\n" +
                                     "Recibirás una devolución del %d%% del importe pagado: %.2f€", 
                                     dto.getPorcentajeDevolucion(),
                                     dto.getCantidadPagada() * dto.getPorcentajeDevolucion() / 100.0);
        
        int confirm = JOptionPane.showConfirmDialog(view, mensaje, "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String rutaJustificante = model.cancelarInscripcion(dto.getIdInscripcion(), dto.isPermiteCancelacion(), dto.getFechaLimiteCancelacion());
        if (rutaJustificante != null) {
            String mensajeExito = String.format(
                "Inscripción cancelada correctamente.\nSe te devolverán %.2f€\nJustificante guardado en:\n%s", 
                dto.getCantidadPagada() * dto.getPorcentajeDevolucion() / 100.0,
                rutaJustificante
            );
            mostrarInfo(mensajeExito);
            buscarColegiadoYMostrarCursos();
        } else {
            mostrarError("No se pudo cancelar la inscripción.");
        }
    }

    private void limpiarTabla() {
        view.getTablaCursosInscritos().setModel(new DefaultTableModel(
                new String[] {
                        "Título", "Fecha Inicio", "Fecha Fin", "Estado Inscripción", "Fecha Inscripción",
                        "Método Pago", "Fecha Pago", "Cantidad Pagada (€)", "Fecha Cancelación", "Cantidad Devuelta (€)", "Admite cancelación"
                }, 0));
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarInfo(String msg) {
        JOptionPane.showMessageDialog(view, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private String formatearFecha(String fechaISO) {
        if (fechaISO == null || fechaISO.isEmpty()) return "-";
        try {
            java.time.LocalDate fecha = java.time.LocalDate.parse(fechaISO);
            return fecha.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yy"));
        } catch (Exception e) {
            return fechaISO;
        }
    }
}
