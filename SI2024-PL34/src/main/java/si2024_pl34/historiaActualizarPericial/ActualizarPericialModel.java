package si2024_pl34.historiaActualizarPericial;

import si2024_pl34.util.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ActualizarPericialModel {

    private Database db;

    public ActualizarPericialModel(Database db) {
        this.db = db;
    }

    public List<ActualizarPericialDTO> obtenerPericialesAsignadas() {
        List<ActualizarPericialDTO> lista = new ArrayList<>();
        String sql = "SELECT p.numero_referencia, p.descripcion, p.prioridad, p.estado, " +
                     "p.solicitante, p.motivo_rechazo, p.fecha_realizacion, " +
                     "c.nombre || ' ' || c.apellidos AS nombre_perito, c.numero_colegiado " +
                     "FROM peritaje p " +
                     "LEFT JOIN colegiado c ON p.colegiado_id = c.id " +
                     "WHERE p.estado = 'Asignado'";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                ActualizarPericialDTO dto = new ActualizarPericialDTO(
                        rs.getString("numero_referencia"),
                        rs.getString("descripcion"),
                        rs.getString("prioridad"),
                        rs.getString("estado"),
                        rs.getString("solicitante"),
                        rs.getString("motivo_rechazo"),
                        rs.getString("fecha_realizacion"),
                        rs.getString("nombre_perito"),
                        rs.getString("numero_colegiado")
                );
                lista.add(dto);
            }

        } catch (Exception e) {
            System.err.println("Error al obtener periciales asignadas: " + e.getMessage());
        }

        return lista;
    }

    public boolean actualizarEstado(String numeroReferencia, String nuevoEstado, String motivo, String fecha) {
        String sql;

        if (nuevoEstado.equals("Rechazado") || nuevoEstado.equals("Cancelado")) {
            sql = "UPDATE peritaje SET estado = ?, motivo_rechazo = ?, fecha_rechazo = ? WHERE numero_referencia = ?";
        } else if (nuevoEstado.equals("Aceptado")) {
            sql = "UPDATE peritaje SET estado = ?, fecha_realizacion = ? WHERE numero_referencia = ?";
        } else {
            return false;
        }

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevoEstado);

            if (nuevoEstado.equals("Rechazado") || nuevoEstado.equals("Cancelado")) {
                stmt.setString(2, motivo);
                stmt.setString(3, fecha);
                stmt.setString(4, numeroReferencia);
            } else {
                stmt.setString(2, fecha);
                stmt.setString(3, numeroReferencia);
            }

            int filas = stmt.executeUpdate();
            return filas > 0;

        } catch (Exception e) {
            System.err.println("Error al actualizar el estado del peritaje: " + e.getMessage());
            return false;
        }
    }
    private String obtenerEstadoActual(String referencia) {
        String sql = "SELECT estado FROM peritaje WHERE numero_referencia = ?";

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, referencia);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("estado");
            }
        } catch (Exception e) {
            System.err.println("Error al obtener el estado actual: " + e.getMessage());
        }

        return "";
    }
}
