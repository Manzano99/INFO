package si2024_pl34.historiaCancelarColegiado;

public class HistoriaCancelarColegiadoModel {
    private int idColegiado;
    private String motivoBaja;
    private String fechaBaja;
    private String justificante; // ruta al archivo justificante (si se sube)

    public HistoriaCancelarColegiadoModel(int idColegiado, String motivoBaja, String fechaBaja, String justificante) {
        this.idColegiado = idColegiado;
        this.motivoBaja = motivoBaja;
        this.fechaBaja = fechaBaja;
        this.justificante = justificante;
    }

    public int getIdColegiado() { return idColegiado; }
    public String getMotivoBaja() { return motivoBaja; }
    public String getFechaBaja() { return fechaBaja; }
    public String getJustificante() { return justificante; }
}