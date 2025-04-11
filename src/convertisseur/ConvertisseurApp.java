package convertisseur;

import javax.swing.*;

public class ConvertisseurApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Convertisseur JSON (CSV/SQL)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 450);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new Fenetre());
            frame.setVisible(true);
        });
    }
}