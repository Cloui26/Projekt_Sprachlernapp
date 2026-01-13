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


    private DefaultTableModel tableModel;
    private ArrayList<Vokabel> alleVokabeln = new ArrayList<>();
    private ArrayList<Vokabel> gefilterteListe = new ArrayList<>();
    private Vokabel aktuelleAbfrageVokabel = null;

    public Hauptfenster() {
        setContentPane(mainPanel);
        setTitle("Lern Fix 2026 - Vokabeltrainer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);

        String[] spalten = {"Deutsch", "Fremdsprache", "Sprache", "Level", "Gelernt?"};
        tableModel = new DefaultTableModel(spalten, 0);
        tblVokabeln.setModel(tableModel);


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

        ActionListener filterListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aktualisiereTabelle();
                lblAbfrageWort.setText("Wähle ein neues Wort aus der Liste...");
                txtAntwort.setText("");
                txtAntwort.setBackground(Color.WHITE);
                aktuelleAbfrageVokabel = null;
            }
        };
        cmbSprache.addActionListener(filterListener);
        cmbLevel.addActionListener(filterListener);

        btnSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });

        btnPruefen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pruefeAntwort();
            }
        });

        tblVokabeln.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblVokabeln.getSelectedRow() != -1) {
                    int viewRow = tblVokabeln.getSelectedRow();
                    int modelRow = tblVokabeln.convertRowIndexToModel(viewRow);
                    if (modelRow < gefilterteListe.size()) {
                        startAbfrage(gefilterteListe.get(modelRow));
                    }
                }
            }
        });

        setVisible(true);
    }
    public void initObjekte() {
        // Englisch - Anfänger
        alleVokabeln.add(new Vokabel("Hund", "Dog", "Englisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Bitte", "Please", "Englisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Essen", "Eat", "Englisch", "Anfänger", false));

        // Englisch - Fortgeschritten
        alleVokabeln.add(new Vokabel("Eichhörnchen", "Squirrel", "Englisch", "Fortgeschritten", false));
        alleVokabeln.add(new Vokabel("Regenmantel", "Raincoat", "Englisch", "Fortgeschritten", false));
        alleVokabeln.add(new Vokabel("Wohlhabend", "Wealthy", "Englisch", "Fortgeschritten", false));

        // Französisch - Anfänger
        alleVokabeln.add(new Vokabel("Katze", "Chat", "Französisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Danke", "Merci", "Französisch", "Anfänger", false));
        alleVokabeln.add(new Vokabel("Rot", "Rouge", "Französisch", "Anfänger", false));

        // Französisch - Fortgeschritten
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
                        v.getSprache(),
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
            JOptionPane.showMessageDialog(this, "Bitte Textfelder ausfüllen!");
            return;
        }

        Vokabel neu = new Vokabel(deutsch, fremd, sprache, level, gelernt);

        alleVokabeln.add(neu);
        aktualisiereTabelle();

        txtDeutsch.setText("");
        txtFremd.setText("");
        chkGelernt.setSelected(false);
    }

    private void startAbfrage(Vokabel v) {
        aktuelleAbfrageVokabel = v;
        lblAbfrageWort.setText("Übersetze: '" + v.getDeutsch() + "' (" + v.getSprache() + ")");
        txtAntwort.setText("");
        txtAntwort.setBackground(Color.WHITE);
        txtAntwort.requestFocus(); // Setzt Cursor direkt ins Feld
    }

    private void pruefeAntwort() {
        if (aktuelleAbfrageVokabel == null) return;

        String eingabe = txtAntwort.getText();

        if (aktuelleAbfrageVokabel.istLösungRichtig(eingabe)) {
            txtAntwort.setBackground(Color.GREEN);
            JOptionPane.showMessageDialog(this, "Richtig! Super gemacht.");

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

