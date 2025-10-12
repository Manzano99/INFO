package si2024_pl34.historiaCancelarInscripcion;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.table.DefaultTableCellRenderer;

import si2024_pl34.util.ThemeManager;

public class CancelarInscripcionView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel lblTitulo;
    private JLabel lblNumColegiado;
    private JTextField txtNumColegiado;
    private JButton btnBuscar;
    private JTable tablaCursosInscritos;
    private JScrollPane scrollPaneCursos;
    private JLabel lblInfo;
    private JButton btnCancelarInscripcion;
    private JComboBox<String> comboOrdenar; // Nuevo combo para orden

    public CancelarInscripcionView() {
        setTitle("Cancelar Inscripción en Cursos");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setSize(1500, 700);
        setLocationRelativeTo(null);

        // Aplicar FlatTheme
        ThemeManager themeManager = new ThemeManager();
        themeManager.establecerTemaComponente(this, null);

        lblTitulo = new JLabel("Cancelar inscripción en cursos");
        lblTitulo.setFont(new Font(lblTitulo.getFont().getName(), Font.BOLD, 18));

        lblNumColegiado = new JLabel("Nº Colegiado:");
        txtNumColegiado = new JTextField();
        txtNumColegiado.setPreferredSize(new Dimension(120, 25));
        btnBuscar = new JButton("Buscar");

        lblInfo = new JLabel("Cursos en los que está inscrito el colegiado:");
        lblInfo.setFont(new Font(lblInfo.getFont().getName(), Font.PLAIN, 14));

        // Columnas de la tabla de cursos inscritos - Añadido "% Devolución"
        String[] columnNames = {
            "Título", "Fecha Inicio", "Fecha Fin", "Estado Inscripción", "Fecha Inscripción", 
            "Método Pago", "Fecha Pago", "Cantidad Pagada (€)", "% Devolución", "Fecha Cancelación", "Cantidad Devuelta (€)", "Admite cancelación", "Nº Lista Espera"
        };
        Object[][] data = {}; // Sin datos por ahora

        tablaCursosInscritos = new JTable(data, columnNames);
        scrollPaneCursos = new JScrollPane(tablaCursosInscritos);

        // --- ESTILO VISUAL SIMILAR A VisualizarCursosView ---
        tablaCursosInscritos.setRowHeight(25);
        tablaCursosInscritos.setShowGrid(false);
        tablaCursosInscritos.setIntercellSpacing(new Dimension(0, 0));
        tablaCursosInscritos.getTableHeader().setBackground(new Color(240, 240, 240));
        tablaCursosInscritos.getTableHeader().setFont(tablaCursosInscritos.getTableHeader().getFont().deriveFont(Font.BOLD));
        tablaCursosInscritos.setSelectionBackground(new Color(232, 242, 254));
        tablaCursosInscritos.setSelectionForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tablaCursosInscritos.setDefaultRenderer(Object.class, centerRenderer);

        btnCancelarInscripcion = new JButton("Cancelar inscripción seleccionada");

        // Combo para ordenar
        comboOrdenar = new JComboBox<>(new String[] {
            "Fecha de Inicio", "Fecha de Fin", "Título", "Cantidad Pagada", "Fecha Cancelación", "Estado Inscripción"
        });
        comboOrdenar.setSelectedIndex(0);

        // Layout usando GroupLayout para alineación similar a VisualizarCursosView
        GroupLayout layout = new GroupLayout(getContentPane());
        layout.setHorizontalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblTitulo)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(lblNumColegiado)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(txtNumColegiado, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED)
                            .addComponent(btnBuscar, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.UNRELATED)
                            .addComponent(comboOrdenar, GroupLayout.PREFERRED_SIZE, 180, GroupLayout.PREFERRED_SIZE)) // Añadido combo
                        .addComponent(lblInfo)
                        .addComponent(scrollPaneCursos, GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
                        .addComponent(btnCancelarInscripcion, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(10)
                    .addComponent(lblTitulo)
                    .addGap(18)
                    .addGroup(layout.createParallelGroup(Alignment.BASELINE)
                        .addComponent(lblNumColegiado)
                        .addComponent(txtNumColegiado, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBuscar)
                        .addComponent(comboOrdenar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)) // Añadido combo
                    .addGap(18)
                    .addComponent(lblInfo)
                    .addPreferredGap(ComponentPlacement.RELATED)
                    .addComponent(scrollPaneCursos, GroupLayout.PREFERRED_SIZE, 320, GroupLayout.PREFERRED_SIZE)
                    .addGap(18)
                    .addComponent(btnCancelarInscripcion)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);
    }

    // Getters para los componentes (útiles para el controlador)
    public JTextField getTxtNumColegiado() {
        return txtNumColegiado;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public JTable getTablaCursosInscritos() {
        return tablaCursosInscritos;
    }

    public JButton getBtnCancelarInscripcion() {
        return btnCancelarInscripcion;
    }

    public JComboBox<String> getComboOrdenar() {
        return comboOrdenar;
    }
}
