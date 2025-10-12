package si2024_pl34.historiaCrearLotesColegiacion;


import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class LoteSolicitudesView extends JFrame {
    private LoteSolicitudesController controller;
    private JTable tableSolicitudes;
    private DefaultTableModel tableModel;
    private JButton btnGenerarFichero;

    public LoteSolicitudesView() {
        setTitle("Gestión de Lotes de Colegiación");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tabla para mostrar colegiados en estado "Pendiente"
        String[] columnNames = {"DNI", "Nombre", "Apellidos", "Titulación", "Fecha de Solicitud"};


        // Crear un modelo de tabla personalizado que bloquee la edición de celdas
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ❌ Bloquea la edición de todas las celdas
            }
        };
        tableSolicitudes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableSolicitudes);

        // Botón para generar un lote y guardar el archivo
        btnGenerarFichero = new JButton("Generar Lote y Guardar Archivo");
        btnGenerarFichero.addActionListener(e -> controller.generarFichero());

        add(scrollPane, BorderLayout.CENTER);
        add(btnGenerarFichero, BorderLayout.SOUTH);
    }

    public void setController(LoteSolicitudesController controller) {
        this.controller = controller;
    }

    public void mostrarColegios(List<LoteSolicitudesDTO> lista) {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Limpiar tabla
    
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
            for (LoteSolicitudesDTO c : lista) {
                String fechaFormateada = c.getFechaSolicitud() != null
                        ? c.getFechaSolicitud().format(formatter)
                        : "—";
                
                tableModel.addRow(new Object[]{
                    c.getDni(),
                    c.getNombre(),
                    c.getApellidos(),
                    c.getTitulacion(),
                    fechaFormateada
                });
            }
    
            tableModel.fireTableDataChanged();
            tableSolicitudes.repaint();
        });
    }
    
    

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void iniciar() {
        setVisible(true);
        controller.cargarColegiosPendientes(); // 🔥 Cargar datos al abrir la ventana
    }
}
