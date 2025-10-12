package si2024.pl34.historiaProcesarSolicitudes;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.LayoutStyle.ComponentPlacement;

public class ProcesarSolicitudesView extends JFrame {
    private JButton btnCargarArchivo;
    private JLabel lblArchivo;
    private JButton btnProcesar;
    private JButton btnCancelar;
    private JLabel lblSolicitudesAfectadas;
    private JTable tablaSolicitudes;
    private JScrollPane scrollPane;

    public ProcesarSolicitudesView() {
        setResizable(true);
        setTitle("Procesamiento de Solicitudes de Movilidad");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        setMinimumSize(getSize());

        btnCargarArchivo = new JButton("Seleccionar archivo");
        btnCargarArchivo.setFont(new Font("Dialog", Font.PLAIN, 14));

        lblArchivo = new JLabel("Ningún archivo seleccionado");
        lblArchivo.setFont(new Font("Dialog", Font.ITALIC, 12));

        btnProcesar = new JButton("Procesar solicitudes");
        btnProcesar.setOpaque(true);
        btnProcesar.setForeground(Color.WHITE);
        btnProcesar.setFont(new Font("Dialog", Font.BOLD, 14));
        btnProcesar.setFocusPainted(false);
        btnProcesar.setBorderPainted(false);
        btnProcesar.setBackground(new Color(0, 123, 255));

        btnCancelar = new JButton("Cancelar");
        btnCancelar.setOpaque(true);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Dialog", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);
        btnCancelar.setBorderPainted(false);
        btnCancelar.setBackground(new Color(220, 53, 69));

        lblSolicitudesAfectadas = new JLabel("Lista de solicitudes a procesar");
        lblSolicitudesAfectadas.setFont(new Font("Dialog", Font.BOLD, 16));

        // Crear la tabla con mejor formato
        tablaSolicitudes = new JTable();
        DefaultTableModel modeloTabla = new DefaultTableModel(
                new Object[][] {},
                new String[] {
                        "DNI del Estudiante", "Fecha de Solicitud", "Titulaciones", "Estado", "Motivo de Rechazo"
                }) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaSolicitudes.setModel(modeloTabla);
        tablaSolicitudes.setRowHeight(25);
        tablaSolicitudes.setFont(new Font("Dialog", Font.PLAIN, 12));
        tablaSolicitudes.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));

        scrollPane = new JScrollPane(tablaSolicitudes);

        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(20)
        			.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 750, Short.MAX_VALUE)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(btnCargarArchivo)
        					.addGap(15)
        					.addComponent(lblArchivo))
        				.addComponent(lblSolicitudesAfectadas)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addComponent(btnCancelar, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnProcesar, GroupLayout.PREFERRED_SIZE, 184, GroupLayout.PREFERRED_SIZE)))
        			.addGap(20))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(20)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnCargarArchivo)
        				.addComponent(lblArchivo))
        			.addGap(25)
        			.addComponent(lblSolicitudesAfectadas)
        			.addGap(10)
        			.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
        			.addGap(20)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(btnCancelar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
        				.addComponent(btnProcesar, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        			.addGap(20))
        );

        getContentPane().setLayout(groupLayout);
    }

    // Método para acceder a la tabla desde fuera de la clase
    public JTable getTablaSolicitudes() {
        return tablaSolicitudes;
    }

    public JButton getBtnCargarArchivo() {
        return btnCargarArchivo;
    }

    public JButton getBtnProcesar() {
        return btnProcesar;
    }

    public JButton getBtnCancelar() {
        return btnCancelar;
    }

    public JLabel getLblArchivo() {
        return lblArchivo;
    }

    public JLabel getLblSolicitudesAfectadas() {
        return lblSolicitudesAfectadas;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JFrame getFrame() {
        return this;
    }

}
