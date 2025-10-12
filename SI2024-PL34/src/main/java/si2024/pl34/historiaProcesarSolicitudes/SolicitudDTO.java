package si2024.pl34.historiaProcesarSolicitudes;

import java.util.List;

public class SolicitudDTO {
    private String dni;
    private String fechaSolicitudColegiacion;
    private List<String> titulaciones;
    private String estado;
    private String motivoRechazoColegiazion;

    // Getters y setters

    public String getMotivoRechazoColegiazion() {
        return motivoRechazoColegiazion;
    }

    public void setMotivoRechazoColegiazion(String motivo_rechazo_colegiazion) {
        this.motivoRechazoColegiazion = motivo_rechazo_colegiazion;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechaSolicitudColegiacion() {
        return fechaSolicitudColegiacion;
    }

    public void setFechaSolicitudColegiacion(String fechaSolicitud) {
        this.fechaSolicitudColegiacion = fechaSolicitud;
    }

    public List<String> getTitulaciones() {
        return titulaciones;
    }

    public void setTitulaciones(List<String> titulaciones) {
        this.titulaciones = titulaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}