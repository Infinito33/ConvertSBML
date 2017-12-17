package convertsbml.sbml;

import convertsbml.converters.EquationToRateRuleConverter;
import convertsbml.model.entities.matlab.ComplexMatlabData;
import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.matlab.ParameterMatlab;
import convertsbml.model.entities.matlab.SimpleMatlabData;
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
     * Stworzenie nowego pliku SBML na bazie modelu SLV.
     *
     * @param modelSlv model z danymi z pliku *.slv.
     */
    public void createSBMLFromSlv(ModelSlv modelSlv) {
        docModel.setId(modelSlv.getName());

        //Zapis parametrów z modelu SLV
        //modelSlv.getParameters().forEach(param -> writeSlvParameter(param));
        //Zapis równań SLV w postaci reguł.
        modelSlv.getEquations().forEach(eq -> writeSlvRateRule(eq));

        //Zapisanie gatunków do modelu SBML.
        Compartment compartment = docModel.createCompartment();
        compartment.setId(modelSlv.getName() + "Comp");
        modelSlv.getParameters().forEach(param -> writeSlvSpeciesWithCompartment(compartment.getId(), param));

        //Walidacja dokumentu SBML
        boolean isValidated = SBMLValidator.validateExampleSBML(document);
        System.out.println("Document is: " + isValidated);

        //Zapis dokumentu SBML
        if (isValidated) {
            SBMLIO.writeExampleSBML(document, "testSBML.xml");
        }
    }

    /**
     * Dodanie nowego parametru do modelu SBML - SLV.
     *
     * @param parameterSlv parametr, który ostanie dodany.
     */
    public void writeSlvParameter(ParameterSlv parameterSlv) {
        Parameter para = docModel.createParameter();
        para.setId(parameterSlv.getName());
        para.setValue(parameterSlv.getValue());
        para.setConstant(Boolean.FALSE);
    }

    /**
     * Dodanie nowej reguły (Rate Rule) do modelu SBML na podstawie równania
     * SLV.
     *
     * @param equationSlv równanie, które zostanie przekstałcone na regułę.
     */
    public void writeSlvRateRule(EquationSlv equationSlv) {
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
    public void writeSlvSpeciesWithCompartment(String compartmentName, ParameterSlv parameterSlv) {
        Species species = docModel.createSpecies();
        species.setId(parameterSlv.getName());
        species.setCompartment(compartmentName);
    }

    /**
     * Zapis modelu SBML na podstawie modelu prostego Matlab.
     *
     * @param simpleModel model prosty Matlab
     */
    public void createSBMLFromSimpleMatlab(SimpleMatlabData simpleModel) {
        String id = simpleModel.getModelFile().getName().substring(0, simpleModel.getModelFile().getName().length() - 2);
        docModel.setId(id);

        //Zapisanie parametrów modelu
        simpleModel.getParameters().forEach(param -> writeMParameter(param));
        //Zapisanie równań jako reguły
        simpleModel.getEquations().forEach(eq -> writeMRateRule(eq));

        Compartment compartment = docModel.createCompartment();
        compartment.setId(id + "Comp");

        //Dodanie gatunków
        simpleModel.getyEquations().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param));
        simpleModel.getEquations().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param));
        simpleModel.getFunctionVariables().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param));

        //Walidacja utworzonego dokumentu SBML
        boolean isValidated = SBMLValidator.validateExampleSBML(document);

        System.out.println("Document is: " + isValidated);

        //Zapis dokumentu
        if (isValidated) {
            SBMLIO.writeExampleSBML(document, "testMatlabSBML.xml");
        }
    }

    /**
     * Zapis modelu SBML na podstawie modelu złożonego Matlab.
     *
     * @param stochasticData model stochastyczny Matlab.
     * @param determinisicData model deterministyczny Matlab.
     */
    public void createSBMLFromComplexMatlab(ComplexMatlabData stochasticData, ComplexMatlabData determinisicData) {
        String id = stochasticData.getModelFile().getName().substring(0, stochasticData.getModelFile().getName().length() - 2);
        docModel.setId(id);

        Compartment compartment = docModel.createCompartment();
        compartment.setId(id + "Comp");

        stochasticData.getParameters().forEach(param -> writeMParameter(param));
        stochasticData.getEquations().forEach(eq -> writeMRateRule(eq));
        stochasticData.getFunctionVariables().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param));
        stochasticData.getApoptopicFactorsVars().forEach(var -> writeMSpeciesWithCompartment(compartment.getId(), var));

        boolean isValidated = SBMLValidator.validateExampleSBML(document);
        System.out.println("Document is: " + isValidated);

        if (isValidated) {
            SBMLIO.writeExampleSBML(document, "testMatlabStochSBML.xml");
        }

        initialize();

        id = determinisicData.getModelFile().getName().substring(0, determinisicData.getModelFile().getName().length() - 2);
        docModel.setId(id);

        Compartment compartment2 = docModel.createCompartment();
        compartment2.setId(id + "Comp");

        determinisicData.getParameters().forEach(param -> writeMParameter(param));
        determinisicData.getEquations().forEach(eq -> writeMRateRule(eq));
        determinisicData.getFunctionVariables().forEach(param -> writeMSpeciesWithCompartment(compartment2.getId(), param));
        determinisicData.getApoptopicFactorsVars().forEach(var -> writeMSpeciesWithCompartment(compartment2.getId(), var));

        boolean isValidated2 = SBMLValidator.validateExampleSBML(document);
        System.out.println("Document is: " + isValidated2);

        if (isValidated2) {
            SBMLIO.writeExampleSBML(document, "testMatlabDetSBML.xml");
        }
    }

    /**
     * Dodanie nowego parametru do modelu SBML - Matlab.
     *
     * @param parameterMatlab parametr, który ostanie dodany.
     */
    public void writeMParameter(ParameterMatlab parameterMatlab) {
        Parameter para = docModel.createParameter();
        para.setId(parameterMatlab.getName());
        Double value = convertDecimalWithPowToDouble(parameterMatlab.getValue());
        para.setValue(value);
        para.setConstant(Boolean.FALSE);
    }

    public Double convertDecimalWithPowToDouble(String value) {
        if (value.contains("/")) {
            int indexOfSlash = value.indexOf("/");
            value = value.substring(0, indexOfSlash);
        }
        if (value.contains("^")) {
            int startOfExponent = value.lastIndexOf("^");
            String exponentWithElements = value.substring(startOfExponent + 1, value.length());
            String exponent = exponentWithElements.replace("(", "").replace(")", "");

            int signIndex = value.lastIndexOf("*");

            String number = "";
            if (signIndex == -1) {
                number = "1";
            } else {
                number = value.substring(0, signIndex);
            }

            String resultNumber = number + "E" + exponent;

            double resultValue = 0;
            try {
                resultValue = Double.parseDouble(resultNumber);
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                return resultValue;
            }
            return resultValue;
        } else {
            return Double.parseDouble(value);
        }
    }

    /**
     * Dodanie nowej reguły (Rate Rule) do modelu SBML na podstawie równania
     * Matlab..
     *
     * @param equationSlv równanie, które zostanie przekstałcone na regułę.
     */
    public void writeMRateRule(EquationM equationM) {
        RateRule rateRule = docModel.createRateRule();
        conv.convertToRateRuleFrom(equationM, rateRule);
    }

    /**
     * Dodanie nowego gatunku do modelu SBML - Matlab
     *
     * @param compartmentName nazwa przedziału (model Matlab) do którego
     * przypisany zostanie gatunek.
     * @param equationMatlab równanie, którego lewa strona (zmienna) zostanie
     * dodana jako gatunek.
     */
    public void writeMSpeciesWithCompartment(String compartmentName, EquationM equationMatlab) {
        Species species = docModel.createSpecies();
        species.setId(equationMatlab.getLeftSide());
        species.setCompartment(compartmentName);
    }

    /**
     * Dodanie nowego gatunku do modelu SBML - Matlab
     *
     * @param compartmentName nazwa przedziału (model Matlab) do którego
     * przypisany zostanie gatunek.
     * @param variable zmienna funkcji, która zostanie dodana jako gatunek.
     */
    public void writeMSpeciesWithCompartment(String compartmentName, String variable) {
        Species species = docModel.createSpecies();
        System.out.println("Variable is: " + variable);
        species.setId(variable);
        species.setCompartment(compartmentName);
    }

}
