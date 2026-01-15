import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VokabelTest {

    @Test
    void testIstLoesungRichtig_richtigUndFalsch() {
        Vokabel v = new Vokabel("Hund", "Dog", "Englisch", "Anfänger", false);

        assertTrue(v.istLösungRichtig("dog"));
        assertTrue(v.istLösungRichtig("DOG"));
        assertFalse(v.istLösungRichtig("Cat"));
    }

    @Test
    void testLernStatus_setterUndGetter() {
        Vokabel v = new Vokabel("Bitte", "Please", "Englisch", "Anfänger", false);
        assertFalse(v.istGelernt());
        v.setIstGelernt(true);
        assertTrue(v.istGelernt());
    }
}
