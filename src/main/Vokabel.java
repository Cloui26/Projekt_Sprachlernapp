public class Vokabel {
    // Attribute
    private String originalWort;
    private String uebersetzung;
    private int schwierigkeitsGrad; // 1 (leicht) bis 3 (schwer)
    private boolean istGelernt;

    // Konstruktor
    public Vokabel(String originalWort, String uebersetzung, int schwierigkeitsGrad, boolean istGelernt) {
        this.originalWort = originalWort;
        this.uebersetzung = uebersetzung;
        this.schwierigkeitsGrad = schwierigkeitsGrad;
        this.istGelernt = istGelernt;
    }

    // Methode für die Logik (wird später getestet)
    public boolean istSchwer() {
        // Logik: Alles ab Stufe 5 ist "schwer"
        return this.schwierigkeitsGrad >= 5;
    }

    // Getter (wichtig für die Tabelle später)
    public String getOriginalWort() { return originalWort; }
    public String getUebersetzung() { return uebersetzung; }
    public int getSchwierigkeitsGrad() { return schwierigkeitsGrad; }
    public boolean isIstGelernt() { return istGelernt; }

    // Setter (falls man nachträglich was ändern will)
    public void setIstGelernt(boolean istGelernt) { this.istGelernt = istGelernt; }

    @Override
    public String toString() {
        return originalWort + " - " + uebersetzung;
    }
}
