package si2024_pl34.historiaActualizarPericial;

public class PeritajeEntity {
    private String numeroReferencia;
    private String descripcion;
    private String prioridad;
    private String fechaRechazo;
    private String motivoRechazo;
    private String solicitante;
    private int colegiadoId;
    private String estado;

    public PeritajeEntity() {}

    public PeritajeEntity(String numeroReferencia, String descripcion, String prioridad,
                          String fechaRechazo, String motivoRechazo, String solicitante,
                          int colegiadoId, String estado) {
        this.numeroReferencia = numeroReferencia;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.fechaRechazo = fechaRechazo;
        this.motivoRechazo = motivoRechazo;
        this.solicitante = solicitante;
        this.colegiadoId = colegiadoId;
        this.estado = estado;
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

    public String getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(String fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public int getColegiadoId() {
        return colegiadoId;
    }

    public void setColegiadoId(int colegiadoId) {
        this.colegiadoId = colegiadoId;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}