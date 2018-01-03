package convertsbml.sbml;

import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.libsbml;

/**
 * Klasa odpowiedzialna za zapisywanie i odczytywanie plików SBML.
 *
 * @author Magda
 */
public class SBMLIO {

    /**
     * Odpowiada za zapis dokumentu wygenerowanego w trakcie konwersji do pliku.
     *
     * @param sbmlDoc dokument SBML.
     * @param filename nazwa dokumentu.
     * @return True jeśli zapis się powiódł, w innym przypadku false.
     */
    public static boolean writeExampleSBML(SBMLDocument sbmlDoc, String filename) {
        int result = libsbml.writeSBML(sbmlDoc, filename);

        if (result == 1) {
            System.out.println("Wrote file \"" + filename + "\"");
            return true;
        } else {
            System.out.println("Failed to write \"" + filename + "\"");
            return false;
        }
    }
}
