package si2024_pl34.historiaCrearLotesColegiacion;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoteSolicitudesModel {
    private static final String URL = "jdbc:sqlite:DemoDB.db";

    /**
     * Obtiene la lista de colegiados en estado "Pendiente".
     */
    public List<LoteSolicitudesDTO> obtenerColegiosPendientes(Connection conn) {
        List<LoteSolicitudesDTO> lista = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT dni, nombre, apellidos, titulacion, fecha_solicitud_colegiacion " +
                "FROM colegiado WHERE estado = 'Pendiente'");
             ResultSet rs = stmt.executeQuery()) {
    
            while (rs.next()) {
                String fechaTexto = rs.getString("fecha_solicitud_colegiacion");
                LocalDate fecha = null;
                if (fechaTexto != null && !fechaTexto.isEmpty()) {
                    fecha = LocalDate.parse(fechaTexto); // se espera formato ISO: yyyy-MM-dd
                }
    
                lista.add(new LoteSolicitudesDTO(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("titulacion"),
                        fecha
                ));
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    

    /**
     * Genera un archivo y cambia el estado de "Pendiente" a "En revisión".
     */
    public String generarFicheroLote(Connection conn) {
        try {
            conn.setAutoCommit(false); // 🔒 Inicia transacción manual
    
            List<LoteSolicitudesDTO> solicitudes = obtenerColegiosPendientes(conn);
    
            if (solicitudes.isEmpty()) {
                System.out.println("⚠ No hay solicitudes pendientes.");
                return null;
            }
    
            String fechaActual = LocalDate.now().toString();
            String rutaArchivo = System.getProperty("user.home") + "/Documents/Lote_Colegiacion_" + fechaActual + ".txt";
    
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaArchivo))) {
                writer.write("Lote de solicitudes de colegiación - Fecha de generación: " + fechaActual);
                writer.newLine();
                writer.write("Total de solicitudes: " + solicitudes.size());
                writer.newLine();
                writer.write("--------------------------------------------------");
                writer.newLine();
                writer.write("DNI, Nombre, Apellidos, Titulación, Fecha de Solicitud");
                writer.newLine();
                writer.write("--------------------------------------------------");
                writer.newLine();
    
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
                for (LoteSolicitudesDTO solicitud : solicitudes) {
                    String fechaSolicitudFormateada = (solicitud.getFechaSolicitud() != null)
                            ? solicitud.getFechaSolicitud().format(formatter)
                            : "—";
    
                    writer.write(String.join(", ",
                            solicitud.getDni(),
                            solicitud.getNombre(),
                            solicitud.getApellidos(),
                            solicitud.getTitulacion(),
                            fechaSolicitudFormateada));
                    writer.newLine();
                }
    
                actualizarEstadoSolicitudes(conn);
                conn.commit();
    
                System.out.println("✅ Fichero generado en: " + rutaArchivo);
                return rutaArchivo;
    
            } catch (IOException e) {
                conn.rollback(); // ❌ Si falla el archivo, deshacemos cambios
                e.printStackTrace();
                return null;
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    

    /**
     * Cambia el estado de los colegiados de "Pendiente" a "En revisión".
     */
     private void actualizarEstadoSolicitudes(Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE colegiado SET estado = 'En revisión' WHERE estado = 'Pendiente'")) {
    
            int filasActualizadas = stmt.executeUpdate();
            System.out.println("📝 Colegiados actualizados a 'En revisión': " + filasActualizadas);
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
