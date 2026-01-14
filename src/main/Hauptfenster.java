import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
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
    private JButton btnLoeschen;


    private ArrayList<Vokabel> alleVokabeln = new ArrayList<>();
    private ArrayList<Vokabel> gefilterteListe = new ArrayList<>();
    private DefaultTableModel tableModel;
    private Vokabel aktuelleAbfrageVokabel = null;

    public Hauptfenster() {
        setContentPane(mainPanel);
        setTitle("Lern Fix 2026 - Vokabeltrainer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        String[] spalten = {"Deutsches Wort", "Übersetzung", "Level", "Gelernt?"};

        tableModel = new DefaultTableModel(spalten, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblVokabeln.setModel(tableModel);
        tblVokabeln.setAutoCreateRowSorter(true);

        if (cmbSprache.getItemCount() == 0) {
            cmbSprache.addItem("Englisch");
            cmbSprache.addItem("Französisch");
        }
        if (cmbLevel.getItemCount() == 0) {
            cmbLevel.addItem("Anfänger");
            cmbLevel.addItem("Fortgeschritten");
        }

        initObjekte();
        aktualisiereTabelle();

        ActionListener filterListener = e -> aktualisiereTabelle();
        cmbSprache.addActionListener(filterListener);
        cmbLevel.addActionListener(filterListener);

        btnSpeichern.addActionListener(e -> speichern());
        btnLoeschen.addActionListener(e -> eintragLoeschen());
        btnPruefen.addActionListener(e -> pruefeAntwort());
        txtAntwort.addActionListener(e -> pruefeAntwort());

        tblVokabeln.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblVokabeln.getSelectedRow() != -1) {
                    try {
                        int viewRow = tblVokabeln.getSelectedRow();
                        int modelRow = tblVokabeln.convertRowIndexToModel(viewRow);
                        if (modelRow < gefilterteListe.size()) {
                            startAbfrage(gefilterteListe.get(modelRow));
                        }
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        });

        setVisible(true);
    }

    public void initObjekte() {
        //Englisch - Anfänger
        alleVokabeln.add(new Vokabel("Hund", "Dog", "Englisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Bitte", "Please", "Englisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Essen", "Eat", "Englisch", "Anfänger", false));
        //Englisch - Fortgeschritten
        alleVokabeln.add(new Vokabel("Eichhörnchen", "Squirrel", "Englisch", "Fortgeschritten", false));
        alleVokabeln.add(new Vokabel("Regenmantel", "Raincoat", "Englisch", "Fortgeschritten", false));
        alleVokabeln.add(new Vokabel("Schätzen", "Estimate", "Englisch", "Fortgeschritten", false));

        //Französisch - Anfänger
        alleVokabeln.add(new Vokabel("Katze", "Chat", "Französisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Danke", "Merci", "Französisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Rot", "Rouge", "Französisch", "Anfänger", false));
        //Französisch - Fortgeschritten
        alleVokabeln.add(new Vokabel("Tasche", "Poche", "Französisch", "Fortgeschritten", false));
        alleVokabeln.add(new Vokabel("Englisch", "Anglais", "Französisch", "Fortgeschritten", false));
        alleVokabeln.add(new Vokabel("Schwer", "Lourde", "Französisch", "Fortgeschritten", false));
    }

    private void aktualisiereTabelle() {
        tableModel.setRowCount(0);
        gefilterteListe.clear();

        String gewaehlteSprache = (String) cmbSprache.getSelectedItem();
        String gewaehltesLevel = (String) cmbLevel.getSelectedItem();

        for (Vokabel v : alleVokabeln) {
            if (v.getSprache().equals(gewaehlteSprache) && v.getLevel().equals(gewaehltesLevel)) {
                gefilterteListe.add(v);

                String anzeigeFremd = v.istGelernt() ? v.getFremdsprache() : "???";
                String gelerntText = v.istGelernt() ? "Ja" : "Nein";

                Object[] zeile = {
                        v.getDeutsch(),
                        anzeigeFremd,
                        v.getLevel(),
                        gelerntText
                };
                tableModel.addRow(zeile);
            }
        }
    }

    private void speichern() {
        String deutsch = txtDeutsch.getText();
        String fremd = txtFremd.getText();
        String sprache = (String) cmbSprache.getSelectedItem();
        String level = (String) cmbLevel.getSelectedItem();
        boolean gelernt = chkGelernt.isSelected();

        if (deutsch.isEmpty() || fremd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Bitte alle Felder ausfüllen!");
            return;
        }

        Vokabel neu = new Vokabel(deutsch, fremd, sprache, level, gelernt);
        alleVokabeln.add(neu);
        aktualisiereTabelle();

        txtDeutsch.setText("");
        txtFremd.setText("");
        chkGelernt.setSelected(false);
    }

    private void eintragLoeschen() {
        try {
            int selectedRow = tblVokabeln.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Bitte erst eine Zeile auswählen!");
                return;
            }

            int modelRow = tblVokabeln.convertRowIndexToModel(selectedRow);
            Vokabel zuLoeschen = gefilterteListe.get(modelRow);

            if (istSystemVokabel(zuLoeschen)) {
                JOptionPane.showMessageDialog(this, "System-Wörter können nicht gelöscht werden!");
                return;
            }

            alleVokabeln.remove(zuLoeschen);
            aktualisiereTabelle();

            lblAbfrageWort.setText("Wort gelöscht.");
            txtAntwort.setText("");
            aktuelleAbfrageVokabel = null;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Fehler beim Löschen: " + e.getMessage());
        }
    }

    private boolean istSystemVokabel(Vokabel v) {
        String[] geschuetzt = {"Hund", "Bitte", "Essen", "Eichhörnchen", "Regenmantel", "Schätzen",
                "Katze", "Danke", "Rot", "Tasche", "Englisch", "Schwer"};

        for (String s : geschuetzt) {
            if (v.getDeutsch().equals(s)) {
                return true;
            }
        }
        return false;
    }

    private void startAbfrage(Vokabel v) {
        aktuelleAbfrageVokabel = v;
        lblAbfrageWort.setText("Was heißt '" + v.getDeutsch() + "'?");
        txtAntwort.setText("");
        txtAntwort.setBackground(Color.WHITE);
        txtAntwort.requestFocus();
    }

    private void pruefeAntwort() {
        if (aktuelleAbfrageVokabel == null) return;
        String eingabe = txtAntwort.getText();

        if (aktuelleAbfrageVokabel.istLösungRichtig(eingabe)) {
            txtAntwort.setBackground(Color.GREEN);
            JOptionPane.showMessageDialog(this, "Richtig! Super.");
            aktuelleAbfrageVokabel.setIstGelernt(true);
            aktualisiereTabelle();
        } else {
            txtAntwort.setBackground(Color.RED);
            JOptionPane.showMessageDialog(this, "Leider falsch.");
        }
    }

    public static void main(String[] args) {
        new Hauptfenster();
    }
}

