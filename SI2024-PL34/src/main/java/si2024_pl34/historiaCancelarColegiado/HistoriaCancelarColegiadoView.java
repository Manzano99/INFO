package si2024_pl34.historiaCancelarColegiado;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class HistoriaCancelarColegiadoView extends JFrame {
    private JPanel panel;
    private JTextField campoID;
    private JTextArea campoMotivo;
    private JButton btnCancelar;

    public HistoriaCancelarColegiadoView() {
        setTitle("Solicitud de Baja de Colegiado");
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelID = new JLabel("ID del Colegiado:");
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(labelID, gbc);

        campoID = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(campoID, gbc);

        JLabel labelMotivo = new JLabel("Motivo de la Baja:");
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(labelMotivo, gbc);

        campoMotivo = new JTextArea(5, 20);
        JScrollPane scroll = new JScrollPane(campoMotivo);
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(scroll, gbc);

        btnCancelar = new JButton("Solicitar Baja");
        gbc.gridx = 1; gbc.gridy = 2;
        panel.add(btnCancelar, gbc);

        this.setContentPane(panel);
        this.pack();
        this.setLocationRelativeTo(null);
    }

    public void addCancelarListener(ActionListener listener) {
        btnCancelar.addActionListener(listener);
    }

    public Integer getIdColegiado() {
        String texto = campoID.getText().trim();
        
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El campo ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El ID debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }


    public String getMotivo() {
        return campoMotivo.getText();
    }

    public String getJustificante() {
        return "";
    }

    public void limpiarCampos() {
        campoID.setText("");
        campoMotivo.setText("");
    }
}
