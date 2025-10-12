package si2024_pl34.AsignacionPericialesaPeritosdisponibles;

public class PeritajesDTO {

	private int id;
	private int id_colegiado;
	private String fecha_solicitud;
	private String fecha_realizacion;
	private String estado;
	private String prioridad;
	private int numero_referencia;
	private String descripcion;
	private int numero_colegiado;
	private String nombre;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId_colegiado() {
		return id_colegiado;
	}
	public void setId_colegiado(int id_colegiado) {
		this.id_colegiado = id_colegiado;
	}
	public String getFecha_solicitud() {
		return fecha_solicitud;
	}
	public void setFecha_solicitud(String fecha_solicitud) {
		this.fecha_solicitud = fecha_solicitud;
	}
	public String getFecha_realizacion() {
		return fecha_realizacion;
	}
	public void setFecha_realizacion(String fecha_realizacion) {
		this.fecha_realizacion = fecha_realizacion;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getPrioridad() {
		return prioridad;
	}
	public void setPrioridad(String prioridad) {
		this.prioridad = prioridad;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public int getNumero_referencia() {
		return numero_referencia;
	}
	public void setNumero_referencia(int numero_referencia) {
		this.numero_referencia = numero_referencia;
	}
	public int getNumero_colegiado() {
		return numero_colegiado;
	}
	public void setNumero_colegiado(int numero_colegiado) {
		this.numero_colegiado = numero_colegiado;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
}
