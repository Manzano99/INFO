package si2024_pl34.AsignacionPericialesaPeritosdisponibles;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;

import si2024_pl34.util.Database;
import si2024_pl34.util.UnexpectedException;

public class AsignacionPericialesPeritosModel {

	private Database db=new Database();
	public static final String URL="jdbc:sqlite:DemoDB.db";

	/* Función que realiza la consulta en la base de datos para obtener la lista de los peritajes
	 * 
	 * @return List<PeritajesDTO> la lista de los peritajes del sistema 
	 * 
	 * @since 1.5
	 */	
	public List<PeritajesDTO> obtencionPeritajes() {

		String sql="Select p1.numero_referencia, p1.fecha_solicitud, p1.estado, p1.prioridad, p1.descripcion, "
				+ "c1.numero_colegiado, c1.nombre "
				+ "from peritaje as p1 "
				+ "LEFT JOIN colegiado as c1 on c1.id=p1.colegiado_id";

		try{
			return db.executeQueryPojo(PeritajesDTO.class, sql);
		}catch(Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
	
	/* Función que realiza la consulta en la base de datos para obtener la lista TAP de los peritos del colegio
	 * 
	 * @return List<ColegiadoDTO> la lista TAP de los peritos del sistema ordenada
	 * 
	 * @since 1.5
	 */	
	public List<ColegiadoDTO> obtencionListaTAP() {

		String sql="Select c1.id, c1.numero_colegiado, c1.nombre, c1.apellidos, c1.telefono, c1.email "
				+ "from colegiado as c1 "
				+ "WHERE c1.estado='Perito' "
				+ "ORDER BY c1.fecha_ultimo_peritaje NULLS FIRST";

		try{
			return db.executeQueryPojo(ColegiadoDTO.class, sql);
		}catch(Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/* Función que le asigna un peritaje a un perito 
	 * 
	 * @param int numref, es el numero de referencia del peritaje a asignar
	 * 		  ColegiadoDTO col, los datos del perito al que se le va a asignar el peritaje
	 * 
	 * @since 1.5
	 */	
	public void asignarPeritaje(int numref, ColegiadoDTO col) {

		Connection conn=null;
		try {
			conn = DriverManager.getConnection(URL);
			QueryRunner runner = new QueryRunner();
			
			String formatofecha="yyyy-MM-dd HH:mm:ss";
			Date todayDate = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat(formatofecha);
			String fechaActual = sdf.format(todayDate);
			
			String sql1 = "UPDATE peritaje "
					+ "SET colegiado_id=? , estado='Asignado' "
					+ "WHERE numero_referencia=?;";


			runner.update(conn,sql1,col.getId(),numref);
			
			String sql2 = "UPDATE colegiado "
					+ "SET fecha_ultimo_peritaje=? "
					+ "WHERE numero_colegiado=?;";
			
			runner.update(conn,sql2,fechaActual,col.getNumero_colegiado());


		}
		catch (SQLException e) {
			throw new UnexpectedException(e);
		} finally {
			DbUtils.closeQuietly(conn); 
		}	

	}

}
