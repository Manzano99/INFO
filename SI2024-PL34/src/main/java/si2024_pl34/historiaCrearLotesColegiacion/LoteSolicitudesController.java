package si2024_pl34.historiaCrearLotesColegiacion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class LoteSolicitudesController {
    private LoteSolicitudesModel model;
    private LoteSolicitudesView view;

    public LoteSolicitudesController(LoteSolicitudesModel model, LoteSolicitudesView view) {
        this.model = model;
        this.view = view;
        this.view.setController(this);
    }

    public void cargarColegiosPendientes() {
        List<LoteSolicitudesDTO> lista = null;
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:DemoDB.db")) {
            lista = model.obtenerColegiosPendientes(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            view.mostrarMensaje("❌ Error al cargar solicitudes pendientes.");
            return;
        }
    
        if (lista != null && !lista.isEmpty()) {
            view.mostrarColegios(lista);
        } else {
            view.mostrarColegios(Collections.emptyList()); // 🔄 Limpiar la tabla si no hay datos
            view.mostrarMensaje("No hay solicitudes pendientes.");
        }
    }
    
    
    

public void generarFichero() {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:DemoDB.db")) {
        String rutaArchivo = model.generarFicheroLote(conn);
        
        if (rutaArchivo != null) {
            view.mostrarMensaje("✅ Fichero generado con éxito en:\n" + rutaArchivo);
            cargarColegiosPendientes(); // 🔄 Actualizar la tabla después
        } else {
            view.mostrarMensaje("⚠ No hay solicitudes pendientes.");
        }
    } catch (SQLException e) {
        e.printStackTrace();
        view.mostrarMensaje("❌ Error al generar el fichero de lote.");
    }
}

}
