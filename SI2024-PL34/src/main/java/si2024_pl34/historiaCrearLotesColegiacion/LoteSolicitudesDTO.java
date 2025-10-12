package si2024_pl34.historiaCrearLotesColegiacion;

import java.time.LocalDate;

public class LoteSolicitudesDTO {
    private String dni;
    private String nombre;
    private String apellidos;
    private String titulacion;
    private LocalDate fechaSolicitud;
    

    public LoteSolicitudesDTO(String dni, String nombre, String apellidos, String titulacion,LocalDate fechaSolicitud) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.titulacion = titulacion;
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getTitulacion() {
        return titulacion;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }
}
