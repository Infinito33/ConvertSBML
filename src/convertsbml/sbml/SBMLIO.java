package convertsbml.sbml;

import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.libsbml;

/**
 * Klasa odpowiedzialna za zapisywanie i odczytywanie plików SBML.
 *
 * @author Magda
 */
public class SBMLIO {

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
