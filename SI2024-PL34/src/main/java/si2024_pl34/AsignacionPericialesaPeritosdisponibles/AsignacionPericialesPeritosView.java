package si2024_pl34.AsignacionPericialesaPeritosdisponibles;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;


public class AsignacionPericialesPeritosView {

private JFrame frame;
private JTable tabPericiales;
private JTable tabListaTap;
private JLabel lblIntroduccion;
private JLabel lblListaTAP;
private JComboBox<String> CBorden;
private JComboBox<String> CBordenador;
private JButton btnAsignar;


public AsignacionPericialesPeritosView() {
	initialize();
}
	private void initialize() {
		frame=new JFrame();
		frame.setTitle("Historial");
		frame.setName("Historial");
		frame.setBounds(0,0,800,600);
		frame.getContentPane().setLayout(new MigLayout("", "[][][grow]", "[][][][][grow][][][][][][]"));
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
				
		tabPericiales = new JTable();
		tabPericiales.setName("tabPericiales");
		tabPericiales.setDefaultEditor(Object.class, null);
		
		JLabel lblorden= new JLabel("Ordenar por:");
		frame.getContentPane().add(lblorden, "cell 2 3,alignx center");

		
		CBorden = new JComboBox<String>();
		frame.getContentPane().add(CBorden, "cell 2 3, alignx center");
		tabPericiales.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		CBorden.addItem("Prioridad");
		CBorden.addItem("Fecha de solicitud");
		CBorden.addItem("Nº Referencia");
		CBorden.addItem("Estado");

		
		JLabel lblforma= new JLabel("De manera:");
		frame.getContentPane().add(lblforma, "cell 2 3,alignx center");

		CBordenador = new JComboBox<String>();
		frame.getContentPane().add(CBordenador, "cell 2 3, alignx center");
		CBordenador.addItem("Ascendente");
		CBordenador.addItem("Descendente");

		JScrollPane tablePanel = new JScrollPane(tabPericiales);
		frame.getContentPane().add(tablePanel, "cell 0 4 3 1,grow");
		
		this.lblIntroduccion = new JLabel("Listado de periciales:");
		frame.getContentPane().add(lblIntroduccion, "cell 0 3");
		
		this.lblIntroduccion = new JLabel("Lista TAP:");
		frame.getContentPane().add(lblIntroduccion, "cell 0 6");


		tabListaTap = new JTable();
		tabListaTap.setName("tabListaTap");
		tabListaTap.setDefaultEditor(Object.class, null);
		
		JScrollPane tablePanel2 = new JScrollPane(tabListaTap);
		frame.getContentPane().add(tablePanel2, "cell 0 7 3 1,grow");
		
		btnAsignar= new JButton("Asignar");
		btnAsignar.setHorizontalAlignment(SwingConstants.RIGHT);
		btnAsignar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		frame.getContentPane().add(btnAsignar, "cell 2 8 2 1");

	}
	public JFrame getFrame() {
		return frame;
	}
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
	public JLabel getLblListaTAP() {
		return lblListaTAP;
	}
	public void setLblListaTAP(JLabel lblListaTAP) {
		this.lblListaTAP = lblListaTAP;
	}
	public JTable getTabListaTap() {
		return tabListaTap;
	}
	public void setTabListaTap(JTable tabListaTap) {
		this.tabListaTap = tabListaTap;
	}
	public JTable getTabPericiales() {
		return tabPericiales;
	}
	public void setTabPericiales(JTable tabPericiales) {
		this.tabPericiales = tabPericiales;
	}
	public JComboBox<String> getCBorden() {
		return CBorden;
	}
	public void setCBorden(JComboBox<String> cBorden) {
		CBorden = cBorden;
	}
	public JComboBox<String> getCBordenador() {
		return CBordenador;
	}
	public void setCBordenador(JComboBox<String> cBordenador) {
		CBordenador = cBordenador;
	}
	public JButton getBtnAsignar() {
		return btnAsignar;
	}
	public void setBtnAsignar(JButton btnAsignar) {
		this.btnAsignar = btnAsignar;
	}

}
