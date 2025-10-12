package si2024_pl34.AsignacionPericialesaPeritosdisponibles;

public class ColegiadoDTO {
	private int id;
	private int numero_colegiado;
	private String nombre;
	private String apellidos;
	private String email;
	private String telefono;

	public int getNumero_colegiado() {
		return numero_colegiado;
	}
	public void setNumero_colegiado(int numero_identificativo) {
		this.numero_colegiado = numero_identificativo;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellidos() {
		return apellidos;
	}
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
