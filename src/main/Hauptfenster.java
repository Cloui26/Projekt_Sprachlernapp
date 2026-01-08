import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Hauptfenster extends JFrame {
    private JTextField txtWort;
    private JTextField txtUebersetzung;
    private JTextField txtSchwierigkeit;
    private JCheckBox chkGelernt;
    private JButton btnSpeichern;
    private JButton btnFilter;
    private JTable tblVokabeln;
    private JPanel mainPanel;

    private ArrayList<Vokabel> vokabelListe = new ArrayList<>();

    private DefaultTableModel tableModel;

    public Hauptfenster() {
        setContentPane(mainPanel);
        setTitle("Lern Fix");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);


        String[] spaltenNamen = {"Wort", "Übersetzung", "Level", "Gelernt?"};
        tableModel = new DefaultTableModel(spaltenNamen, 0);
        tblVokabeln.setModel(tableModel);



        initObjekte();

        setVisible(true);

        //Button

    }

    public void initObjekte(){
        Vokabel v1 = new Vokabel("Table", "Tisch", 1, true);
        Vokabel v2 = new Vokabel("Environment", "Umwelt", 6, false);
        Vokabel v3 = new Vokabel("Decision", "Entscheidung", 4, false);

        addVokabel(v1);
        addVokabel(v2);
        addVokabel(v3);
    }

    private void addVokabel(Vokabel v){
        vokabelListe.add(v);
        Object[] zeile = {v.getOriginalWort(), v.getUebersetzung(), v.getSchwierigkeitsGrad()};
        tableModel.addRow(zeile);
    }

    public static void main(String[] args) {
        new Hauptfenster();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
