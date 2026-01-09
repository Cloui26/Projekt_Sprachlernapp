import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;



public class Hauptfenster extends JFrame{
    private JPanel mainPanel;
    private JTextField txtDeutsch;
    private JTextField txtFremd;
    private JComboBox cmbSprache;
    private JComboBox cmbLevel;
    private JCheckBox chkGelernt;
    private JButton btnSpeichern;
    private JTable tblVokabeln;
    private JLabel lblAbfrageWort;
    private JTextField txtAntwort;
    private JButton btnPruefen;

    private ArrayList<Vokabel> vokabelListe = new ArrayList<>();
    private DefaultTableModel tableModel;

    private Vokabel aktuelleAbfrageVokabel = null;
    private int aktuelleZeileIndex = -1; // Merkt sich, welche Zeile in der Tabelle markiert ist

    public Hauptfenster() {
        setContentPane(mainPanel);
        setTitle("Lern Fix - Vokabeltrainer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600); // Etwas breiter für die neue Spalte
        setLocationRelativeTo(null);


        String[] spalten = {"Deutsch", "Fremdsprache", "Sprache", "Level", "Gelernt?"};
        tableModel = new DefaultTableModel(spalten, 0);
        tblVokabeln.setModel(tableModel);


        cmbSprache.addItem("Englisch");
        cmbSprache.addItem("Französisch");
        cmbLevel.addItem("Anfänger");
        cmbLevel.addItem("Fortgeschritten");


        initObjekte();



        btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });


        tblVokabeln.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblVokabeln.getSelectedRow() != -1) {

                    aktuelleZeileIndex = tblVokabeln.getSelectedRow();
                    startAbfrage(aktuelleZeileIndex);
                }
            }
        });

        btnPruefen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pruefeAntwort();
            }
        });

        setVisible(true);
    }


    public void initObjekte() {
        // Letzter Wert ist jetzt true/false für "Gelernt"
        Vokabel v1 = new Vokabel("Hund", "Dog", "Englisch", "Anfänger", true);
        Vokabel v2 = new Vokabel("Katze", "Chat", "Französisch", "Anfänger", false);
        Vokabel v3 = new Vokabel("Haus", "House", "Englisch", "Anfänger", false);

        objektHinzufuegen(v1);
        objektHinzufuegen(v2);
        objektHinzufuegen(v3);
    }

    private void objektHinzufuegen(Vokabel v) {
        vokabelListe.add(v);
        // "Ja" oder "Nein" für die Tabelle statt true/false (sieht schöner aus)
        String gelerntText = v.istGelernt() ? "Ja" : "Nein";

        Object[] zeile = {
                v.getDeutsch(),
                v.getFremdsprache(),
                v.getSprache(),
                v.getLevel(),
                gelerntText
        };
        tableModel.addRow(zeile);
    }

    // --- Speichern ---
    private void speichern() {
        String deutsch = txtDeutsch.getText();
        String fremd = txtFremd.getText();
        String sprache = (String) cmbSprache.getSelectedItem();
        String level = (String) cmbLevel.getSelectedItem();
        boolean gelernt = chkGelernt.isSelected(); // Wert aus Checkbox holen

        if (deutsch.isEmpty() || fremd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte Felder ausfüllen!");
            return;
        }

        Vokabel neu = new Vokabel(deutsch, fremd, sprache, level, gelernt);
        objektHinzufuegen(neu);

        txtDeutsch.setText("");
        txtFremd.setText("");
        chkGelernt.setSelected(false); // Checkbox zurücksetzen
    }

    // --- Abfrage ---
    private void startAbfrage(int viewRowIndex) {
        int modelRow = tblVokabeln.convertRowIndexToModel(viewRowIndex);
        aktuelleAbfrageVokabel = vokabelListe.get(modelRow);

        lblAbfrageWort.setText("Was heißt '" + aktuelleAbfrageVokabel.getDeutsch() + "'?");
        txtAntwort.setText("");
        txtAntwort.setBackground(Color.WHITE);
    }

    // --- Prüfen (Das Herzstück mit Update) ---
    private void pruefeAntwort() {
        if (aktuelleAbfrageVokabel == null) return;

        String eingabe = txtAntwort.getText();

        if (aktuelleAbfrageVokabel.istLösungRichtig(eingabe)) {
            // 1. Feedback geben
            txtAntwort.setBackground(Color.GREEN);
            JOptionPane.showMessageDialog(this, "Richtig! Status auf 'Gelernt' gesetzt.");

            // 2. Objekt aktualisieren
            aktuelleAbfrageVokabel.setIstGelernt(true);

            // 3. Tabelle an der richtigen Stelle aktualisieren
            // Spalte 4 ist "Gelernt?"
            if (aktuelleZeileIndex != -1) {
                tableModel.setValueAt("Ja", aktuelleZeileIndex, 4);
            }

        } else {
            txtAntwort.setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "Falsch! Lösung: " + aktuelleAbfrageVokabel.getFremdsprache());
        }
    }

    public static void main(String[] args) {
        new Hauptfenster();
    }
}

