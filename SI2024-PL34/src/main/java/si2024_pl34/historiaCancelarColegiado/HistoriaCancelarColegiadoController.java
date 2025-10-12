package si2024_pl34.historiaCancelarColegiado;

import javax.swing.*;

import si2024_pl34.util.Database;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoriaCancelarColegiadoController {
    private HistoriaCancelarColegiadoModel model;
    private HistoriaCancelarColegiadoView view;

    public HistoriaCancelarColegiadoController(HistoriaCancelarColegiadoModel model, HistoriaCancelarColegiadoView view) {
        this.model = model;
        this.view = view;
        initController();
    }

    private void initController() {
        view.addCancelarListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarColegiado();
            }
        });
    }

    private void cancelarColegiado() {
        int id = view.getIdColegiado();
        String motivo = view.getMotivo();

        if (motivo.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Debe indicar el motivo de la baja.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = new Database().getConnection()) {
            String sqlCheck = "SELECT estado FROM colegiado WHERE id = ?";
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
            stmtCheck.setInt(1, id);
            ResultSet rs = stmtCheck.executeQuery();
            if (rs.next()) {
                String estado = rs.getString("estado");
                if ("Baja".equalsIgnoreCase(estado)) {
                    JOptionPane.showMessageDialog(view, "El colegiado ya está dado de baja.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(view, "No se encontró el colegiado con ese ID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(view, "¿Está seguro de que desea solicitar la baja?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccione la carpeta para guardar el justificante");
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int userSelection = fileChooser.showSaveDialog(view);
            if (userSelection != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(view, "No se ha seleccionado carpeta. Cancelación abortada.", "Cancelado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            File selectedFolder = fileChooser.getSelectedFile();
            String fecha = obtenerFechaActual();
            String rutaJustificante = selectedFolder.getAbsolutePath() + "/justificante_baja_" + id + ".txt";
            model = new HistoriaCancelarColegiadoModel(id, motivo, fecha, rutaJustificante);

            // Comprobar si hay recibo devuelto en el año actual y su importe
            int year = Integer.parseInt(fecha.substring(0, 4));
            String sqlRecibo = "SELECT estado, importe FROM recibos WHERE id_colegiado = ? AND año = ?";
            PreparedStatement stmtRecibo = conn.prepareStatement(sqlRecibo);
            stmtRecibo.setInt(1, id);
            stmtRecibo.setInt(2, year);
            ResultSet rsRecibo = stmtRecibo.executeQuery();
            boolean deuda = false;
            double importeDeuda = 0.0;
            if (rsRecibo.next() && "DEVUELTO".equalsIgnoreCase(rsRecibo.getString("estado"))) {
                deuda = true;
                importeDeuda = rsRecibo.getDouble("importe");
            }

            // Escribir justificante
            try (FileWriter writer = new FileWriter(rutaJustificante)) {
                writer.write("Justificante de baja\n");
                writer.write("ID Colegiado: " + id + "\n");
                writer.write("Fecha: " + fecha + "\n");
                writer.write("Motivo: " + motivo + "\n");
                if (deuda) {
                    writer.write("\n-- AVISO DE DEUDA --\n");
                    writer.write("El colegiado presenta un recibo devuelto en el año " + year + ".\n");
                    writer.write("Importe pendiente: " + importeDeuda + " €\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(view, "No se pudo guardar el justificante.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sql = "UPDATE colegiado SET estado = ?, motivo_rechazo_colegiazion = ?, fecha_solicitud_colegiacion = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Baja");
            stmt.setString(2, motivo);
            stmt.setString(3, fecha);
            stmt.setInt(4, id);
            stmt.executeUpdate();

            String sql2 = "UPDATE peritaje SET colegiado_id = NULL WHERE colegiado_id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, id);
            stmt2.executeUpdate();

            JOptionPane.showMessageDialog(view, "La baja ha sido procesada correctamente. Justificante generado en:\n" + rutaJustificante, "Éxito", JOptionPane.INFORMATION_MESSAGE);
            view.limpiarCampos();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al procesar la baja.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String obtenerFechaActual() {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        return formato.format(new Date());
    }
}