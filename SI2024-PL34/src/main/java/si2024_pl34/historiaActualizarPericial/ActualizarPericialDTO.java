package si2024_pl34.historiaActualizarPericial;

public class ActualizarPericialDTO {
    private String numeroReferencia;
    private String descripcion;
    private String prioridad;
    private String estado;
    private String solicitante;
    private String motivoRechazo;
    private String fechaRechazo;
    private String nombrePerito;
    private String numeroColegiado;

    public ActualizarPericialDTO() {}

    public ActualizarPericialDTO(String numeroReferencia, String descripcion, String prioridad, String estado,
                                  String solicitante, String motivoRechazo, String fechaRechazo,
                                  String nombrePerito, String numeroColegiado) {
        this.numeroReferencia = numeroReferencia;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.estado = estado;
        this.solicitante = solicitante;
        this.motivoRechazo = motivoRechazo;
        this.fechaRechazo = fechaRechazo;
        this.nombrePerito = nombrePerito;
        this.numeroColegiado = numeroColegiado;
    }

    public String getNumeroReferencia() {
        return numeroReferencia;
    }

    public void setNumeroReferencia(String numeroReferencia) {
        this.numeroReferencia = numeroReferencia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(String fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public String getNombrePerito() {
        return nombrePerito;
    }

    public void setNombrePerito(String nombrePerito) {
        this.nombrePerito = nombrePerito;
    }

    public String getNumeroColegiado() {
        return numeroColegiado;
    }

    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }
}
