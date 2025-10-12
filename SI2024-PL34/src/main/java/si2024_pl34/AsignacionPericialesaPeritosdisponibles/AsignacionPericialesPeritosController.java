package si2024_pl34.AsignacionPericialesaPeritosdisponibles;

import java.awt.Component;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import si2024_pl34.util.SwingUtil;

public class AsignacionPericialesPeritosController {

	private AsignacionPericialesPeritosModel model;
	private AsignacionPericialesPeritosView view;
	private List<PeritajesDTO> listado;
	private List<ColegiadoDTO> listadoTAP;


	public AsignacionPericialesPeritosController(AsignacionPericialesPeritosModel m, AsignacionPericialesPeritosView v) {
		super();
		this.model = m;
		this.view = v;

		this.initView();
	}

	public void initController() {
		view.getCBorden().addActionListener(e -> SwingUtil.exceptionWrapper(() -> reOrdenarTabla(this.listado,view.getTabPericiales())));
		view.getCBordenador().addActionListener(e -> SwingUtil.exceptionWrapper(() -> reOrdenarTabla(this.listado,view.getTabPericiales())));
		view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> asignarPeritoPeritaje()));

	}



	public void initView() {
		view.getFrame().setVisible(true);
		this.obtenerPeritajes();
		this.obtenerListaTAP();
	}


	/* Función que obtiene de la base de datos la lista de los peritajes del sistema y la muestra en la tabla de la pantalla
	 * 
	 * @since 1.5
	 */
	private void obtenerPeritajes() {

		List<PeritajesDTO> l = model.obtencionPeritajes();
		this.listado=l;
		this.actualizarTabla(l,null, view.getTabPericiales(),null);
		this.reOrdenarTabla(l, view.getTabPericiales());
	}

	/* Función que obtiene de la base de datos la lista TAP de peritos ordenada y la muestra en la tabla de la pantalla
	 * 
	 * @since 1.5
	 */
	private void obtenerListaTAP() {

		List<ColegiadoDTO> l=model.obtencionListaTAP();
		this.listadoTAP=l;
		this.actualizarTabla(null, l, null, view.getTabListaTap());
	}

	/* Función que le asigna a un peritaje un perito de la lista TAP del sistema
	 * 
	 * @since 1.5
	 */
	private void asignarPeritoPeritaje() {

		int fila=view.getTabPericiales().getSelectedRow();
		if(fila<0) {
			JOptionPane.showMessageDialog(null, "No has seleccionado nigún peritaje",
					"Peritaje no seleccionado",JOptionPane.WARNING_MESSAGE);
			return;
		}
		int numref=(int) view.getTabPericiales().getValueAt(fila, 0);
		int i;
		for(i=0;i<this.listado.size();i++) {
			if(this.listado.get(i).getNumero_referencia()==numref) {
				break;
			}
		}

		if(this.listado.get(i).getEstado().equals("Asignado")) {
			JOptionPane.showMessageDialog(null, "El peritaje ya ha sido asignado a un perito.",
					"Peritaje ya asignado",JOptionPane.ERROR_MESSAGE);
			return;
		}else if(!this.listado.get(i).getEstado().equals("Pendiente")) {
			JOptionPane.showMessageDialog(null, "El peritaje no tiene un estado válido.",
					"Error estado",JOptionPane.ERROR_MESSAGE);
			return;
		}

		fila=view.getTabListaTap().getSelectedRow();
		if(fila<0) {
			JOptionPane.showMessageDialog(null, "No has seleccionado nigún perito",
					"Perito no seleccionado",JOptionPane.WARNING_MESSAGE);
			return;
		}
		int numcol=(int) view.getTabListaTap().getValueAt(fila, 0);
		int j;
		for(j=0;j<this.listadoTAP.size();j++) {
			if(this.listadoTAP.get(j).getNumero_colegiado()==numcol) {
				break;
			}
		}

		model.asignarPeritaje(this.listado.get(i).getNumero_referencia(),this.listadoTAP.get(j));
		
		this.obtenerPeritajes();
		this.obtenerListaTAP();
	}

	/* Función que modifica la tabla dada con los datos de los cursos que contiene la lista l
	 * 
	 * @param lPeritajes lista de objetos PeritajesDTO que contiene todos los peritajes que hay en el sistema
	 * 		  lPeritos lista de objetos ColegiadoDTO que contiene la lista de peritos del TAP
	 * 		  tablaPeritajes JTable identifica la tabla de los peritajes a modificar
	 * 		  tablaPeritos JTable identifica la tabla de peritos a modificar    
	 * @since 1.5
	 */	
	private void actualizarTabla(List<PeritajesDTO> lPeritajes,List<ColegiadoDTO> lPeritos, JTable tablaPeritajes,JTable tablaPeritos) {
		if(lPeritajes!=null) {
			TableModel tmodel=SwingUtil.getTableModelFromPojos(lPeritajes, 
					new String[] {"numero_referencia","descripcion","fecha_solicitud","prioridad","estado",
							"numero_colegiado","nombre"});	
			view.getTabPericiales().setModel(tmodel);
			this.ajustarAnchoColumnas(tablaPeritajes);
		}

		if(lPeritos!=null) {
			TableModel tmodel=SwingUtil.getTableModelFromPojos(lPeritos, 
					new String[] {"numero_colegiado","nombre","apellidos","telefono","email"});	
			tablaPeritos.setModel(tmodel);
			this.ajustarAnchoColumnas(tablaPeritos);
		}
	}

	/* Función ordena los datos de la tabla de peritajes del sistema según los criterios ordenados por el usuario
	 * 
	 * @since 1.5
	 */	
	private void reOrdenarTabla(List<PeritajesDTO> listado, JTable tabla) {		
		// Si no hay datos para ordenar, salir
		if(listado==null || listado.isEmpty()) {
			return;
		}

		String orden = (String) view.getCBorden().getSelectedItem();
		boolean esAscendente = "Ascendente".equals(view.getCBordenador().getSelectedItem());

		Comparator<PeritajesDTO> comparador = null;

		// Seleccionar el comparador según el criterio
		if ("Prioridad".equals(orden)) {
			comparador = Comparator.comparing(PeritajesDTO::getPrioridad);
		} else if ("Fecha de solicitud".equals(orden)) {
			comparador = Comparator.comparing(PeritajesDTO::getFecha_solicitud);
		} else if ("Estado".equals(orden)) {
			comparador = Comparator.comparing(PeritajesDTO::getEstado);
		}else if ("Nº Referencia".equals(orden)) {
			comparador = Comparator.comparing(PeritajesDTO::getNumero_referencia);
		}


		// Invertir el comparador si el orden es descendente
		if (!esAscendente && comparador != null) {
			comparador = comparador.reversed();
		}

		if(comparador != null) {
			Collections.sort(listado,comparador);
			this.actualizarTabla(listado,null,tabla,null);
		}
	}
	
	
	/* Función que según los datos escritos en la tabla cambia el ancho de las columnas para que los valores contenidos se puedan ver bien 
	 * 
	 * @param tabla JTable tabla a modificar
	 * 
	 * @since 1.5
	 */	
	private void ajustarAnchoColumnas(JTable tabla) {
		TableColumnModel columnModel = tabla.getColumnModel();
		for (int col = 0; col < tabla.getColumnCount(); col++) {
			TableColumn column = columnModel.getColumn(col);
			int anchoMaximo = 0;

			// 1. Calcular el ancho del encabezado de la columna
			TableCellRenderer headerRenderer = column.getHeaderRenderer();
			if (headerRenderer == null) {
				headerRenderer = tabla.getTableHeader().getDefaultRenderer();
			}
			Component headerComp = headerRenderer.getTableCellRendererComponent(
					tabla, column.getHeaderValue(), false, false, 0, col
					);
			anchoMaximo = headerComp.getPreferredSize().width;

			// 2. Calcular el ancho máximo del contenido de la columna
			for (int fila = 0; fila < tabla.getRowCount(); fila++) {
				TableCellRenderer renderer = tabla.getCellRenderer(fila, col);
				Component comp = tabla.prepareRenderer(renderer, fila, col);
				anchoMaximo = Math.max(comp.getPreferredSize().width, anchoMaximo);
			}

			// 3. Añadir un margen de 10 píxeles y establecer el ancho
			column.setPreferredWidth(anchoMaximo + 10);
		}
	}

}
