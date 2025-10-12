package si2024.pl34.historiaProcesarSolicitudes;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import si2024_pl34.util.ThemeManager;

/**
 * Controlador para la gestión de solicitudes de colegiación.
 */
public class ProcesarSolicitudesController {
    private final ProcesarSolicitudesView view;
    private final ProcesarSolicitudesModel model;
    private File archivoRespuesta;

    private static final String ERROR_DUPLICADO = "duplicado";

    /**
     * Constructor del controlador.
     */
    public ProcesarSolicitudesController(ProcesarSolicitudesView view, ProcesarSolicitudesModel model) {
        this.view = view;
        this.model = model;
    }

    /**
     * Inicializa y muestra la vista.
     */
    public void iniciarVista() {
        new ThemeManager().establecerTemaComponente(view.getFrame(), null);

        // Configurar listeners usando métodos de referencia donde sea posible
        view.getBtnCargarArchivo().addActionListener(e -> cargarArchivo());
        view.getBtnProcesar().addActionListener(e -> procesarArchivo());
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());

        // Inicialmente el botón de procesar está deshabilitado
        view.getBtnProcesar().setEnabled(false);

        view.getFrame().setVisible(true);
    }

    /**
     * Maneja la acción de cargar un archivo.
     */
    private void cargarArchivo() {
        JFileChooser fileChooser = crearSelectorArchivo();

        if (fileChooser.showOpenDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
            archivoRespuesta = fileChooser.getSelectedFile();
            view.getLblArchivo().setText(archivoRespuesta.getName());
            view.getBtnProcesar().setEnabled(true);
        } else {
            mostrarMensaje("No se ha seleccionado ningún archivo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea un selector de archivos configurado para JSON.
     */
    private JFileChooser crearSelectorArchivo() {
        JFileChooser fileChooser = new JFileChooser(".");
        fileChooser.setDialogTitle("Seleccionar archivo JSON de respuesta");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON (*.json)", "json"));
        return fileChooser;
    }

    /**
     * Procesa el archivo cargado.
     */
    private void procesarArchivo() {
        if (archivoRespuesta == null) {
            mostrarMensaje("No se ha seleccionado ningún archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<SolicitudDTO> solicitudes = model.procesarArchivoRespuesta(archivoRespuesta);
        actualizarTabla(solicitudes);

        // Mostrar resultados
        mostrarResumenExito(solicitudes);
        mostrarErrores(model.getErrores());

        // Bloquear botón después de procesar
        view.getBtnProcesar().setEnabled(false);
    }

    /**
     * Actualiza la tabla con las solicitudes procesadas.
     */
    private void actualizarTabla(List<SolicitudDTO> solicitudes) {
        DefaultTableModel tableModel = (DefaultTableModel) view.getTablaSolicitudes().getModel();
        tableModel.setRowCount(0);

        solicitudes.forEach(s -> tableModel.addRow(new Object[] {
                s.getDni(),
                s.getFechaSolicitudColegiacion(),
                String.join(", ", s.getTitulaciones()),
                s.getEstado(),
                s.getMotivoRechazoColegiazion()
        }));
    }

    /**
     * Muestra un mensaje de éxito con el resumen de solicitudes procesadas.
     */
    private void mostrarResumenExito(List<SolicitudDTO> solicitudes) {
        if (!solicitudes.isEmpty()) {
            String mensaje = String.format("Se han procesado correctamente %d solicitudes.", solicitudes.size());
            mostrarMensaje(mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Muestra los errores encontrados durante el procesamiento.
     */
    private void mostrarErrores(Map<String, String> errores) {
        if (errores.isEmpty())
            return;

        StringBuilder mensajeErrores = new StringBuilder("Se encontraron problemas durante el procesamiento:\n\n");

        // Mostrar DNIs duplicados
        mostrarErroresFiltrados(mensajeErrores, errores,
                entry -> entry.getValue().contains(ERROR_DUPLICADO),
                "DNIs duplicados encontrados:\n");

        // Mostrar otros errores
        mostrarErroresFiltrados(mensajeErrores, errores,
                entry -> !entry.getValue().contains(ERROR_DUPLICADO),
                null);

        mostrarMensaje(mensajeErrores.toString(), "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Añade errores filtrados al mensaje de error.
     */
    private void mostrarErroresFiltrados(StringBuilder sb, Map<String, String> errores,
            Predicate<Map.Entry<String, String>> filtro,
            String encabezado) {
        List<Map.Entry<String, String>> erroresFiltrados = errores.entrySet().stream()
                .filter(filtro)
                .collect(Collectors.toList());

        if (!erroresFiltrados.isEmpty()) {
            if (encabezado != null) {
                sb.append(encabezado);
            }

            erroresFiltrados.forEach(entry -> {
                if (entry.getValue().contains(ERROR_DUPLICADO)) {
                    sb.append("- ").append(entry.getKey()).append("\n");
                } else {
                    sb.append("- ").append(entry.getKey()).append(": ")
                            .append(entry.getValue()).append("\n");
                }
            });

            sb.append("\n");
        }
    }

    /**
     * Método unificado para mostrar mensajes de diálogo.
     */
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(view.getFrame(), mensaje, titulo, tipo);
    }
}