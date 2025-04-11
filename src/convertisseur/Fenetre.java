package convertisseur;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Fenetre extends JPanel {

    private final JTextField inputFilePathField;
    private final JButton browseInputButton;
    private final JTextField outputFilePathField;
    private final JButton browseOutputButton;
    private final JRadioButton csvRadioButton;
    private final JRadioButton sqlRadioButton;
    private final JPanel csvOptionsPanel;
    private final JRadioButton commaRadioButton;
    private final JRadioButton semicolonRadioButton;
    private final JTextField tableNameField;
    private final JButton convertButton;
    private final JTextArea logArea;

    public Fenetre() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Ligne 1 : Fichier JSON d'entrée
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Fichier JSON:"), gbc);

        inputFilePathField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(inputFilePathField, gbc);
        gbc.weightx = 0;

        browseInputButton = new JButton("Parcourir");
        gbc.gridx = 2;
        add(browseInputButton, gbc);
        browseInputButton.addActionListener(e -> {
            // remplacer la String par : "\\fichiers test"
            JFileChooser fc = new JFileChooser(new File("C:\\Users\\oskan\\Documents\\fichiers conversion\\"));
            if (fc.showOpenDialog(Fenetre.this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();
                inputFilePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Nouveau bouton carré pour ouvrir l'explorateur sur le dossier du fichier d'entrée
        JButton openInputFolderButton = new JButton("Ouvrir");
        openInputFolderButton.setPreferredSize(new Dimension(26, 26));
        gbc.gridx = 3;
        add(openInputFolderButton, gbc);
        openInputFolderButton.addActionListener(e -> {
            String path = inputFilePathField.getText().trim();
            if (!path.isEmpty()) {
                File file = new File(path);
                File directory = file.isDirectory() ? file : file.getParentFile();
                try {
                    Desktop.getDesktop().open(directory);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Fenetre.this, "Impossible d'ouvrir l'explorateur sur ce dossier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ligne 2 : Fichier de sortie
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Fichier de sortie:"), gbc);

        outputFilePathField = new JTextField();
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        add(outputFilePathField, gbc);
        gbc.weightx = 0;

        browseOutputButton = new JButton("Parcourir");
        gbc.gridx = 2;
        add(browseOutputButton, gbc);
        browseOutputButton.addActionListener(e -> {
            // remplacer la String par : "\\fichiers test"
            JFileChooser fc = new JFileChooser("C:\\Users\\oskan\\Documents\\fichiers conversion\\out\\out.csv");
            if (fc.showSaveDialog(Fenetre.this) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fc.getSelectedFile();
                outputFilePathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Nouveau bouton carré pour ouvrir l'explorateur sur le dossier du fichier de sortie
        JButton openOutputFolderButton = new JButton("Ouvrir");
        openOutputFolderButton.setPreferredSize(new Dimension(26, 26));
        gbc.gridx = 3;
        add(openOutputFolderButton, gbc);
        openOutputFolderButton.addActionListener(e -> {
            String path = outputFilePathField.getText().trim();
            if (!path.isEmpty()) {
                File file = new File(path);
                File directory = file.isDirectory() ? file : file.getParentFile();
                try {
                    Desktop.getDesktop().open(directory);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(Fenetre.this, "Impossible d'ouvrir l'explorateur sur ce dossier.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        // Ligne 3 : Choix du mode de conversion
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Mode de conversion:"), gbc);

        csvRadioButton = new JRadioButton("CSV");
        sqlRadioButton = new JRadioButton("SQL");
        ButtonGroup modeGroup = new ButtonGroup();
        modeGroup.add(csvRadioButton);
        modeGroup.add(sqlRadioButton);
        csvRadioButton.setSelected(true);

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modePanel.add(csvRadioButton);
        modePanel.add(sqlRadioButton);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(modePanel, gbc);
        gbc.gridwidth = 1;

        // Ligne 4 : Options spécifiques pour CSV : choix du séparateur (virgule ou point-virgule)
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Séparateur CSV:"), gbc);

        csvOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        commaRadioButton = new JRadioButton("EN (,)");
        semicolonRadioButton = new JRadioButton("FR (;)");
        ButtonGroup delimiterGroup = new ButtonGroup();
        delimiterGroup.add(semicolonRadioButton);
        delimiterGroup.add(commaRadioButton);
        // Choix par défaut sur la virgule
        semicolonRadioButton.setSelected(true);
        csvOptionsPanel.add(semicolonRadioButton);
        csvOptionsPanel.add(commaRadioButton);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(csvOptionsPanel, gbc);
        gbc.gridwidth = 1;

        // Ligne 5 : Option SQL (nom de la table)
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Nom de la table:"), gbc);
        tableNameField = new JTextField("ma_table", 10);
        gbc.gridx = 1;
        add(tableNameField, gbc);

        // Activation/désactivation : si CSV est sélectionné, afficher les options CSV et masquer le nom de la table ; si SQL est sélectionné, l'inverse
        csvRadioButton.addActionListener(e -> {
            csvOptionsPanel.setEnabled(true);
            setEnabledRecursively(csvOptionsPanel, true);
            tableNameField.setEnabled(false);
        });
        sqlRadioButton.addActionListener(e -> {
            csvOptionsPanel.setEnabled(false);
            setEnabledRecursively(csvOptionsPanel, false);
            tableNameField.setEnabled(true);
        });
        // Par défaut, pour CSV c'est activé
        csvOptionsPanel.setEnabled(true);
        setEnabledRecursively(csvOptionsPanel, true);
        tableNameField.setEnabled(false);

        // Ligne 6 : Bouton de conversion
        convertButton = new JButton("Convertir");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        add(convertButton, gbc);
        gbc.gridwidth = 1;

        convertButton.addActionListener(e -> startConversion());

        // Ligne 7 : Zone de log pour afficher les messages
        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        add(scrollPane, gbc);
    }

    // Méthode utilitaire pour activer/désactiver récursivement tous les composants d'un container
    private void setEnabledRecursively(Container container, boolean enabled) {
        container.setEnabled(enabled);
        for (Component comp : container.getComponents()) {
            comp.setEnabled(enabled);
            if (comp instanceof Container) {
                setEnabledRecursively((Container) comp, enabled);
            }
        }
    }

    // Méthode déléguant la conversion selon le mode sélectionné
    private void startConversion() {
        String inputPath = inputFilePathField.getText().trim();
        String outputPath = outputFilePathField.getText().trim();
        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner les fichiers d'entrée et de sortie.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "Le fichier JSON d'entrée n'existe pas.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        convertButton.setEnabled(false);
        logArea.append("Début de la conversion...\n");

        IConvertisseur converter;
        if (csvRadioButton.isSelected()) {
            char delimiter = commaRadioButton.isSelected() ? ',' : ';';
            converter = new ConvertEnCsv(delimiter);
        } else {
            String tableName = tableNameField.getText().trim();
            converter = new ConvertEnSql(tableName);
        }

        new Thread(() -> {
            try {
                converter.convert(inputFile, outputFile);
                SwingUtilities.invokeLater(() -> logArea.append("Conversion terminée.\n"));
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> logArea.append("Erreur : " + ex.getMessage() + "\n"));
                ex.printStackTrace();
            } finally {
                SwingUtilities.invokeLater(() -> convertButton.setEnabled(true));
            }
        }).start();
    }
}
