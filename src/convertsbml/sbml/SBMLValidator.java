package convertsbml.sbml;

import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.SBMLError;

/**
 * Klasa odpowiedzialna za walidację pliku SBML przed jego zapisaniem w celu
 * weryfikacji zawartości.
 *
 * @author Magda
 */
public class SBMLValidator {

    private String consistencyErrors;
    private String validationErrors;

    /**
     * Walidacja dokumentu SBML.
     *
     * @param sbmlDoc dokument SBML.
     * @return True, jesli walidacja jest poprawna, w innym przypadku false.F
     */
    public boolean validateExampleSBML(SBMLDocument sbmlDoc) {
        consistencyErrors = "";
        validationErrors = "";

        if (sbmlDoc == null) {
            System.err.println("validateExampleSBML: given a null SBML Document");
            return false;
        }

        String consistencyMessages = "";
        String validationMessages = "";
        boolean noProblems = true;
        int numCheckFailures = 0;
        int numConsistencyErrors = 0;
        int numConsistencyWarnings = 0;
        int numValidationErrors = 0;
        int numValidationWarnings = 0;

        // LibSBML 3.3 is lenient when generating models from scratch using the
        // API for creating objects. Once the whole model is done and before it
        // gets written out, it's important to check that the whole model is in
        // fact complete, consistent and valid.
        numCheckFailures = (int) sbmlDoc.checkInternalConsistency();
        if (numCheckFailures > 0) {
            noProblems = false;
            for (int i = 0; i < numCheckFailures; i++) {
                SBMLError sbmlErr = sbmlDoc.getError(i);
                if (sbmlErr.isFatal() || sbmlErr.isError()) {
                    ++numConsistencyErrors;
                } else {
                    ++numConsistencyWarnings;
                }
            }

            consistencyMessages = sbmlDoc.getErrorLog().toString();
        }

        // If the internal checks fail, it makes little sense to attempt
        // further validation, because the model may be too compromised to
        // be properly interpreted.
        if (numConsistencyErrors > 0) {
            consistencyMessages += "Further validation aborted.";
        } else {
            numCheckFailures = (int) sbmlDoc.checkConsistency();
            if (numCheckFailures > 0) {
                noProblems = false;
                for (int i = 0; i < numCheckFailures; i++) {
                    SBMLError sbmlErr = sbmlDoc.getError(i);
                    if (sbmlErr.isFatal() || sbmlErr.isError()) {
                        ++numValidationErrors;
                    } else {
                        ++numValidationWarnings;
                    }
                }
                validationMessages = sbmlDoc.getErrorLog().toString();
            }
        }

        if (noProblems) {
            return true;
        } else {
            if (numConsistencyErrors > 0) {
                consistencyErrors = "ERROR: encountered " + numConsistencyErrors
                        + " consistency error"
                        + (numConsistencyErrors == 1 ? "" : "s")
                        + " in model '" + sbmlDoc.getModel().getId() + "'.";
                System.out.println(consistencyErrors);
            }
            if (numConsistencyWarnings > 0) {
                System.out.println("Notice: encountered "
                        + numConsistencyWarnings + " consistency warning"
                        + (numConsistencyWarnings == 1 ? "" : "s")
                        + " in model '" + sbmlDoc.getModel().getId() + "'.");
            }
            System.out.println();
            System.out.println(consistencyMessages);

            if (numValidationErrors > 0) {
                validationErrors = "ERROR: encountered " + numValidationErrors
                        + " validation error"
                        + (numValidationErrors == 1 ? "" : "s") + " in model '"
                        + sbmlDoc.getModel().getId() + "'.";
                System.out.println(validationErrors);
            }
            if (numValidationWarnings > 0) {
                System.out.println("Notice: encountered "
                        + numValidationWarnings + " validation warning"
                        + (numValidationWarnings == 1 ? "" : "s")
                        + " in model '" + sbmlDoc.getModel().getId() + "'.");
            }
            System.out.println();
            System.out.println(validationMessages);

            return (numConsistencyErrors == 0 && numValidationErrors == 0);
        }
    }

    public String getConsistencyErrors() {
        return consistencyErrors;
    }

    public String getValidationErrors() {
        return validationErrors;
    }

}
