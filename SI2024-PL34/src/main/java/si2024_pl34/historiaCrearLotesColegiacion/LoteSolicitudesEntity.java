package si2024_pl34.historiaCrearLotesColegiacion;

import java.time.LocalDate;

public class LoteSolicitudesEntity {
    private int id;
    private LocalDate fechaCreacion;
    private int cantidadSolicitudes;

    public LoteSolicitudesEntity(int id, LocalDate fechaCreacion, int cantidadSolicitudes) {
        this.id = id;
        this.fechaCreacion = fechaCreacion;
        this.cantidadSolicitudes = cantidadSolicitudes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getCantidadSolicitudes() {
        return cantidadSolicitudes;
    }

    public void setCantidadSolicitudes(int cantidadSolicitudes) {
        this.cantidadSolicitudes = cantidadSolicitudes;
    }
}


