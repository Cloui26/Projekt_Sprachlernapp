
public class Vokabel {
    private String deutsch;
    private String fremdsprache;
    private String sprache;      // "Englisch" oder "Französisch"
    private String level;        // "Anfänger" oder "Fortgeschritten"
    private boolean istGelernt;  // Speichert, ob man es schon kann

    // Konstruktor
    public Vokabel(String deutsch, String fremdsprache, String sprache, String level, boolean istGelernt) {
        this.deutsch = deutsch;
        this.fremdsprache = fremdsprache;
        this.sprache = sprache;
        this.level = level;
        this.istGelernt = istGelernt;
    }

    // Logik: Prüft Antwort
    public boolean istLösungRichtig(String eingabeUser) {
        return this.fremdsprache.equalsIgnoreCase(eingabeUser);
    }

    // Getter & Setter
    public String getDeutsch() { return deutsch; }
    public String getFremdsprache() { return fremdsprache; }
    public String getSprache() { return sprache; }
    public String getLevel() { return level; }

    public boolean istGelernt() { return istGelernt; }


    public void setIstGelernt(boolean istGelernt) {
        this.istGelernt = istGelernt;
    }
}