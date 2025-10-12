package si2024_pl34.historiaActualizarPericial;

public class ColegiadoEntity {
    private String numeroColegiado;
    private String nombre;
    private String apellidos;
    private String dni;

    public ColegiadoEntity() {}

    public ColegiadoEntity(String numeroColegiado, String nombre, String apellidos, String dni) {
        this.numeroColegiado = numeroColegiado;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
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

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}