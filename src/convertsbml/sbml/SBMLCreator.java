package convertsbml.sbml;

import convertsbml.converters.EquationToRateRuleConverter;
import convertsbml.model.entities.slv.EquationSlv;
import convertsbml.model.entities.slv.ModelSlv;
import convertsbml.model.entities.slv.ParameterSlv;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Parameter;
import org.sbml.libsbml.RateRule;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;

/**
 * Klasa odpowiedzialna za tworzenie pliku SBML.
 *
 * @author Magda
 */
public class SBMLCreator {

    private final int level = 2;
    private final int version = 4;

    private SBMLDocument document;
    private Model docModel;
    private EquationToRateRuleConverter conv;

    /**
     * Konstruktor, który wykonuje inicjalizację.
     */
    public SBMLCreator() {
        initialize();
    }

    /**
     * Inicjalizacja kreatora plików SBML.
     */
    public void initialize() {
        document = new SBMLDocument(level, version);
        docModel = document.createModel();
        conv = new EquationToRateRuleConverter();
    }

    /**
     * Stworzenie nowego pliku SBML.
     *
     * @param modelSlv model z danymi z pliku *.slv.
     */
    public void createSBMLFile(ModelSlv modelSlv) {
        docModel.setId("testModel");

        //modelSlv.getParameters().forEach(param -> writeParameter(param));
        modelSlv.getEquations().forEach(eq -> writeRateRule(eq));

        Compartment compartment = docModel.createCompartment();
        compartment.setId(modelSlv.getName());
        modelSlv.getParameters().forEach(param -> writeSpeciesWithCompartment(compartment.getId(), param));

        boolean isValidated = SBMLValidator.validateExampleSBML(document);
        System.out.println("Document is: " + isValidated);

        if (isValidated) {
            SBMLIO.writeExampleSBML(document, "testSBML.xml");
        }
    }

    /**
     * Dodanie nowego parametru do modelu SBML.
     *
     * @param parameterSlv parametr, który ostanie dodany.
     */
    public void writeParameter(ParameterSlv parameterSlv) {
        Parameter para = docModel.createParameter();
        para.setId(parameterSlv.getName());
        para.setValue(parameterSlv.getValue());
        para.setConstant(Boolean.FALSE);
    }

    /**
     * Dodanie nowej reguły (Rate Rule) do modelu SBML.
     *
     * @param equationSlv równanie, które zostanie przekstałcone na regułę.
     */
    public void writeRateRule(EquationSlv equationSlv) {
        RateRule rateRule = docModel.createRateRule();
        conv.convertToRateRuleFrom(equationSlv, rateRule);
    }

    /**
     * Dodanie nowego gatunku do modelu SBML.
     *
     * @param compartmentName nazwa przedziału (model SLV) do którego przypisany
     * zostanie gatunek.
     * @param parameterSlv parametr, który zostanie dodany jako gatunek.
     */
    public void writeSpeciesWithCompartment(String compartmentName, ParameterSlv parameterSlv) {
        Species species = docModel.createSpecies();
        species.setId(parameterSlv.getName());
        species.setCompartment(compartmentName);
    }

}
