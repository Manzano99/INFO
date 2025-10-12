package si2024_pl34.historiaActualizarPericial;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

public class ActualizarPericialView extends JFrame {

    private JTable tabla;
    private JTable tablaHistorial;
    private JTextArea txtMotivo;
    private JButton btnAceptar, btnRechazar, btnCancelar;

    public ActualizarPericialView() {
        setTitle("Actualizar Estado de Periciales");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        tabla = new JTable();
        JScrollPane scrollTabla = new JScrollPane(tabla);
        add(scrollTabla, BorderLayout.CENTER);

        // Panel inferior (botones, motivo y historial)
        JPanel panelInferior = new JPanel(new BorderLayout(5, 5));

        // Tabla de historial
        String[] columnasHistorial = {"Referencia", "Acción", "Motivo"};
        tablaHistorial = new JTable(new DefaultTableModel(new Object[0][0], columnasHistorial));
        JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
        scrollHistorial.setBorder(BorderFactory.createTitledBorder("Historial de acciones recientes"));
        scrollHistorial.setPreferredSize(new Dimension(1000, 120));
        panelInferior.add(scrollHistorial, BorderLayout.NORTH);

        // Área de motivo
        txtMotivo = new JTextArea(3, 40);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        scrollMotivo.setBorder(BorderFactory.createTitledBorder("Motivo de Rechazo/Cancelación"));
        panelInferior.add(scrollMotivo, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel();
        btnAceptar = new JButton("Aceptar");
        btnRechazar = new JButton("Rechazar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnAceptar);
        panelBotones.add(btnRechazar);
        panelBotones.add(btnCancelar);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);
    }

    public void actualizarTabla(List<ActualizarPericialDTO> datos) {
        String[] columnas = {"Referencia", "Descripción", "Prioridad", "Solicitante", "Estado", "Perito"};
        Object[][] filas = new Object[datos.size()][6];

        for (int i = 0; i < datos.size(); i++) {
            ActualizarPericialDTO dto = datos.get(i);
            filas[i][0] = dto.getNumeroReferencia();
            filas[i][1] = dto.getDescripcion();
            filas[i][2] = dto.getPrioridad();
            filas[i][3] = dto.getSolicitante();
            filas[i][4] = dto.getEstado();
            filas[i][5] = dto.getNombrePerito();
        }

        DefaultTableModel model = new DefaultTableModel(filas, columnas);
        tabla.setModel(model);
    }

    public void actualizarHistorial(String referencia, String accion, String motivo) {
        DefaultTableModel modelo = (DefaultTableModel) tablaHistorial.getModel();
        modelo.addRow(new Object[]{referencia, accion, motivo != null ? motivo : ""});
    }

    public String getReferenciaSeleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            return (String) tabla.getValueAt(fila, 0);
        }
        return null;
    }

    public String getEstadoSeleccionadoPericial() {
        int fila = tabla.getSelectedRow();
        if (fila >= 0) {
            return (String) tabla.getValueAt(fila, 4); // columna "Estado"
        }
        return null;
    }
    
    public void limpiarMotivo() {
        txtMotivo.setText("");
    }

    public String getMotivo() {
        return txtMotivo.getText().trim();
    }

    public void setAceptarListener(ActionListener listener) {
        btnAceptar.addActionListener(listener);
    }

    public void setRechazarListener(ActionListener listener) {
        btnRechazar.addActionListener(listener);
    }

    public void setCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}