package convertsbml.sbml;

import convertsbml.Const;
import convertsbml.converters.EquationToRuleConverter;
import convertsbml.converters.StringToNumberConverter;
import convertsbml.model.entities.matlab.ComplexMatlabData;
import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.matlab.ParameterMatlab;
import convertsbml.model.entities.matlab.SimpleMatlabData;
import convertsbml.model.entities.slv.EquationSlv;
import convertsbml.model.entities.slv.ModelSlv;
import convertsbml.model.entities.slv.ParameterSlv;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.control.Alert;
import org.sbml.libsbml.ASTNode;
import org.sbml.libsbml.AlgebraicRule;
import org.sbml.libsbml.AssignmentRule;
import org.sbml.libsbml.Compartment;
import org.sbml.libsbml.FunctionDefinition;
import org.sbml.libsbml.InitialAssignment;
import org.sbml.libsbml.Model;
import org.sbml.libsbml.Parameter;
import org.sbml.libsbml.RateRule;
import org.sbml.libsbml.SBMLDocument;
import org.sbml.libsbml.Species;
import org.sbml.libsbml.libsbml;

/**
 * Klasa odpowiedzialna za tworzenie pliku SBML.
 *
 * http://sbml.org/Software/libSBML/Tutorials - Tak wygladaja przyklady
 * parametru, przypisania, rate rules itd.
 *
 * @author Magda
 */
public class SBMLCreator {

    private final int level = 2;
    private final int version = 4;

    private SBMLValidator validator;

    private SBMLDocument document;
    private Model docModel;
    private EquationToRuleConverter ruleConverter;
    private StringToNumberConverter sToNConv;

    private Alert messageDialog;
    private Alert errorDialog;

    /**
     * Konstruktor, który wykonuje inicjalizację.
     */
    public SBMLCreator() {
        initialize();
        validator = new SBMLValidator();

        messageDialog = new Alert(Alert.AlertType.INFORMATION);
        messageDialog.setTitle("Zapis SBML");
        messageDialog.setHeaderText(null);

        errorDialog = new Alert(Alert.AlertType.ERROR);
        errorDialog.setTitle("Błąd zapisu SBML");
        errorDialog.setHeaderText(null);
    }

    /**
     * Inicjalizacja kreatora plików SBML.
     */
    public void initialize() {
        document = new SBMLDocument(level, version);
        docModel = document.createModel();
        ruleConverter = new EquationToRuleConverter();
        sToNConv = new StringToNumberConverter();

        addFunctionDefinitions();
    }

    /**
     * Dodaje dodatkowe definicje funkcji do modelu SBML. Na chwilę obecną
     * wymaga tego tylko jedna funkcja - sign.
     */
    private void addFunctionDefinitions() {
        //Utworzenie definicji funkcji sign
        FunctionDefinition func = docModel.createFunctionDefinition();
        func.setName("sign");
        func.setId("sign");

        //Utworzenie schematu funkcji sign w MathML
        String mathXML = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\">"
                + "          <lambda>"
                + "            <bvar>"
                + "              <ci> x </ci>"
                + "            </bvar>"
                + "            <piecewise>"
                + "              <piece>"
                + "                <cn type=\"integer\"> -1 </cn>"
                + "                <apply>"
                + "                  <lt/>"
                + "                  <ci> x </ci>"
                + "                  <cn type=\"integer\"> 0 </cn>"
                + "                </apply>"
                + "              </piece>"
                + "              <otherwise>"
                + "                <cn type=\"integer\"> 1 </cn>"
                + "              </otherwise>"
                + "            </piecewise>"
                + "          </lambda>"
                + "        </math>";

        //Konwersja schematu funkcji do gałęzi dodawanej do definicji funkcji.
        ASTNode astMath = libsbml.readMathMLFromString(mathXML);
        func.setMath(astMath);

        docModel.addFunctionDefinition(func);
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
        modelSlv.getParameters().forEach(param -> writeSlvSpeciesWithCompartment(modelSlv.getName() + "Comp", param));

        //Walidacja dokumentu SBML
        boolean isValidated = validator.validateExampleSBML(document);

        //Zapis dokumentu SBML
        if (isValidated) {
            boolean result = SBMLIO.writeExampleSBML(document, modelSlv.getName() + ".xml");
            String message = Const.EMPTY;
            if (result) {
                message = "Zapis modelu " + modelSlv.getName() + " do formatu SBML powiódł się.";
            } else {
                message = "Zapis modelu " + modelSlv.getName() + " do formatu SBML nie powiódł się.";
            }
            showResultingDialog(message);
        } else {
            showErrorDialog("Błędy spójności: " + validator.getConsistencyErrors() + "\n" + "Błędy walidacji: " + validator.getValidationErrors());
        }
    }

    /**
     * Dodanie nowego parametru do modelu SBML - SLV.
     *
     * @param parameterSlv parametr, który ostanie dodany.
     */
    public void writeSlvParameter(ParameterSlv parameterSlv) {
        Parameter param = docModel.createParameter();
        param.setId(parameterSlv.getName());
        param.setValue(parameterSlv.getValue());
        param.setConstant(Boolean.FALSE);
    }

    /**
     * Dodanie nowej reguły (Rate Rule) do modelu SBML na podstawie równania
     * SLV.
     *
     * @param equationSlv równanie, które zostanie przekstałcone na regułę.
     */
    public void writeSlvRateRule(EquationSlv equationSlv) {
        RateRule rateRule = docModel.createRateRule();
        ruleConverter.convertToRateRuleFrom(equationSlv, rateRule);
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
        String name = simpleModel.getModelFile().getName().substring(0, simpleModel.getModelFile().getName().length() - 2);
        docModel.setId(name);

        //Zapisanie parametrów modelu
        simpleModel.getParameters().forEach(param -> writeMParameter(param));
        //Zapisanie równań jako reguły
        simpleModel.getEquations().forEach(eq -> writeMRateRule(eq));

        //Utworzenie nazwy przedziału
        Compartment compartment = docModel.createCompartment();
        compartment.setId(name + "Comp");

        //Dodanie gatunków
        simpleModel.getyEquations().forEach(param -> writeSimpleModelSpecificSpeciesWithCompartment(compartment.getId(), param, simpleModel.getVariableInitials()));
        simpleModel.getEquations().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param, simpleModel.getVariableInitials()));
        simpleModel.getFunctionVariables().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param, simpleModel.getVariableInitials()));

        //Walidacja utworzonego dokumentu SBML
        boolean isValidated = validator.validateExampleSBML(document);

        //Zapis dokumentu
        if (isValidated) {
            boolean result = SBMLIO.writeExampleSBML(document, name + ".xml");
            String message = Const.EMPTY;
            if (result) {
                message = "Zapis modelu " + name + " do formatu SBML powiódł się.";
            } else {
                message = "Zapis modelu " + name + " do formatu SBML nie powiódł się.";
            }
            showResultingDialog(message);
        } else {
            showErrorDialog("Błędy spójności: " + validator.getConsistencyErrors() + "\n" + "Błędy walidacji: " + validator.getValidationErrors());
        }
    }

    /**
     * Zapis modelu SBML na podstawie modelu złożonego Matlab.
     *
     * @param stochasticData model stochastyczny Matlab.
     * @param determinisicData model deterministyczny Matlab.
     */
    public void createSBMLFromComplexMatlab(ComplexMatlabData stochasticData, ComplexMatlabData determinisicData) {
        //Usunięcie końcówki z nazwy pliku - .m tak aby została sama nazwa.
        String name = stochasticData.getModelFile().getName().substring(0, stochasticData.getModelFile().getName().length() - 2);
        docModel.setId(name);

        boolean resultOfStoch = false;
        boolean resultOfDeterm = false;

        Compartment compartment = docModel.createCompartment();
        compartment.setId(name + "Comp");

        //Zebranie wszystkich gatunków do jednego zbioru
        Set<String> species = new HashSet<>();
        species.addAll(stochasticData.getFunctionVariables());
        species.addAll(stochasticData.getApoptopicFactorsVars());
        species.addAll(stochasticData.getStatusChangeData().getFunctionVariables());
        species.add("Tchange");

        Set<String> speciesTemp = new HashSet<>(species);
        //Sprawdzenie czy lista gatunków nie zawiera przypadkiem parametru, jeśli tak, usunąć go.
        stochasticData.getParameters().forEach(param -> {
            species.forEach(spec -> {
                if (param.getName().equals(spec)) {
                    speciesTemp.remove(spec);
                }
            });
        });

        //Zapisanie parametrów modelu stochastycznego
        stochasticData.getParameters().forEach(param -> writeMParameter(param));
        stochasticData.getEquations().forEach(eq -> writeMRateRule(eq));

        speciesTemp.forEach(spec -> writeMSpeciesWithCompartment(compartment.getId(), spec, stochasticData.getVariableInitials()));

//        stochasticData.getFunctionVariables().forEach(param -> writeMSpeciesWithCompartment(compartment.getId(), param));
//        stochasticData.getApoptopicFactorsVars().forEach(var -> writeMSpeciesWithCompartment(compartment.getId(), var));
//        stochasticData.getStatusChangeData().getFunctionVariables().forEach(var -> writeMSpeciesWithCompartment(compartment.getId(), var));
        stochasticData.getStatusChangeData().getEquations().forEach(equation -> writeMAssignmentRule(equation));
        stochasticData.getStatusChangeData().getAssignments().forEach(assignment -> writeMAssignmentRule(assignment));
        //writeMAlgebraicRule(stochasticData.getStatusChangeData().getRo());
        //writeMAlgebraicRule(stochasticData.getStatusChangeData().getRoint());
        //writeMAlgebraicRule(stochasticData.getStatusChangeData().getFd());
        //writeMAlgebraicRule(stochasticData.getStatusChangeData().getA());

        //Dopisac reszte rownan pojedynczych
        boolean isValidated = validator.validateExampleSBML(document);
        System.out.println("Document is: " + isValidated);

        //Jeśli dokument jest poprawny, zapis do pliku
        if (isValidated) {
            resultOfStoch = SBMLIO.writeExampleSBML(document, name + "_Stoch.xml");
        } else {
            showErrorDialog("Błędy spójności: " + validator.getConsistencyErrors() + "\n" + "Błędy walidacji: " + validator.getValidationErrors() + " \n Dla modelu stochastycznego: " + name);
        }

        initialize();

        name = determinisicData.getModelFile().getName().substring(0, determinisicData.getModelFile().getName().length() - 2);
        docModel.setId(name);

        Compartment compartment2 = docModel.createCompartment();
        compartment2.setId(name + "Comp");

        //Zapisanie parametrów modelu deterministycznego
        determinisicData.getParameters().forEach(param -> writeMParameter(param));
        determinisicData.getEquations().forEach(eq -> writeMRateRule(eq));
        determinisicData.getFunctionVariables().forEach(param -> writeMSpeciesWithCompartment(compartment2.getId(), param, determinisicData.getVariableInitials()));
        determinisicData.getApoptopicFactorsVars().forEach(var -> writeMSpeciesWithCompartment(compartment2.getId(), var, determinisicData.getVariableInitials()));

        boolean isValidated2 = validator.validateExampleSBML(document);
        System.out.println("Document is: " + isValidated2);

        //Jeśli dokument jest poprawny, zapis do pliku
        if (isValidated2) {
            resultOfDeterm = SBMLIO.writeExampleSBML(document, name + "_Determ.xml");
        } else {
            showErrorDialog("Błędy spójności: " + validator.getConsistencyErrors() + "\n" + "Błędy walidacji: " + validator.getValidationErrors() + " \n Dla modelu deterministycznego: " + name);
        }

        if (resultOfStoch && resultOfDeterm) {
            showResultingDialog("Zapis modeli " + name + " został zakończony sukcesem");
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

        //Dla wartosći prostej parametru, np.: a6=0.1; 
        try {
            Double value = sToNConv.convertDecimalWithPowToDouble(parameterMatlab.getValue());
            para.setValue(value);
        } catch (NumberFormatException e) {
            //Dla wartości parametru ze zmienną, np.: a6=ExtSw*0.1; przypisanie równania zmiennej
            InitialAssignment ia = docModel.createInitialAssignment();
            ia.setSymbol(parameterMatlab.getName());
            ia.setMath(libsbml.parseFormula(parameterMatlab.getValue()));
        }

        para.setConstant(Boolean.FALSE);
    }

    /**
     * Dodanie nowej reguły (Rate Rule) do modelu SBML na podstawie równania
     * Matlab..
     *
     * @param equationM równanie, które zostanie przekstałcone na regułę.
     */
    public void writeMRateRule(EquationM equationM) {
        RateRule rateRule = docModel.createRateRule();
        ruleConverter.convertToRateRuleFrom(equationM, rateRule);
    }

    /**
     * Dodanie nowej reguły (Algebraic Rule) do modelu SBML na podstawie
     * równania Matlab.
     *
     * @param equationM równanie, które zostanie przekształcone na regułę
     */
    public void writeMAlgebraicRule(EquationM equationM) {
        if (!equationM.getLeftSide().contains("rand") && !equationM.getLeftSide().contains("fd") && !equationM.getRightSide().contains("rand") && !equationM.getRightSide().contains("fd")) {
            AlgebraicRule algebraicRule = docModel.createAlgebraicRule();
            ruleConverter.convertToAlgebraicRuleFrom(equationM, algebraicRule);
        }
    }

    /**
     * Dodanie nowej reguły (Assignment Rule) do modelu SBML na podstawie
     * równania Matlab.
     *
     * @param equationM równanie, które zostanie przekształcone na regułę
     */
    public void writeMAssignmentRule(EquationM equationM) {
        if (!equationM.getLeftSide().contains("rand") && !equationM.getLeftSide().contains("fd") && !equationM.getRightSide().contains("rand") && !equationM.getRightSide().contains("fd")) {
            AssignmentRule assignmentRule = docModel.createAssignmentRule();
            ruleConverter.convertToAssignmentRuleFrom(equationM, assignmentRule);
        }
    }

    /**
     * Dodanie nowego gatunku do modelu SBML - Matlab
     *
     * @param compartmentName nazwa przedziału (model Matlab) do którego
     * przypisany zostanie gatunek.
     * @param equationMatlab równanie, którego lewa strona (zmienna) zostanie
     * dodana jako gatunek.
     * @param initialValues poczatkowe wartości zmiennych.
     */
    public void writeMSpeciesWithCompartment(String compartmentName, EquationM equationMatlab, Map<String, Double> initialValues) {
        Species species = docModel.createSpecies();
        species.setId(equationMatlab.getLeftSide());
        species.setCompartment(compartmentName);
        if (initialValues.containsKey(equationMatlab.getLeftSide())) {
            System.out.println("Adding initial value: " + initialValues.get(equationMatlab.getLeftSide()) + " for variable: " + equationMatlab.getLeftSide());
            species.setInitialAmount(initialValues.get(equationMatlab.getLeftSide()));
        } else {
            species.setInitialAmount(0.0);
        }
    }

    /**
     * Dodanie nowego gatunku do modelu SBML - Matlab
     *
     * @param compartmentName nazwa przedziału (model Matlab) do którego
     * przypisany zostanie gatunek.
     * @param equationMatlab równanie, którego lewa strona (zmienna) zostanie
     * dodana jako gatunek.
     * @param initialValues poczatkowe wartości zmiennych.
     */
    public void writeSimpleModelSpecificSpeciesWithCompartment(String compartmentName, EquationM equationMatlab, Map<String, Double> initialValues) {
        Species species = docModel.createSpecies();
        species.setId(equationMatlab.getLeftSide());
        species.setCompartment(compartmentName);
        String rightAssignment = equationMatlab.getRightSide().replace("(", "").replace(");", "");
        if (initialValues.containsKey(rightAssignment)) {
            species.setInitialAmount(initialValues.get(rightAssignment));
        } else {
            species.setInitialAmount(0.0);
        }
    }

    /**
     * Dodanie nowego gatunku do modelu SBML - Matlab
     *
     * @param compartmentName nazwa przedziału (model Matlab) do którego
     * przypisany zostanie gatunek.
     * @param variable zmienna funkcji, która zostanie dodana jako gatunek.
     * @param initialValues poczatkowe wartości zmiennych.
     */
    public void writeMSpeciesWithCompartment(String compartmentName, String variable, Map<String, Double> initialValues) {
        Species species = docModel.createSpecies();
        species.setId(variable);
        species.setCompartment(compartmentName);

        for (Map.Entry<String, Double> entry : initialValues.entrySet()) {
            if (entry.getKey().equals(variable) || entry.getKey().contains(variable)) {
                species.setInitialAmount(entry.getValue());
                return;
            }
        }

        species.setInitialAmount(0.0);
    }

    /**
     * Pokaż dialog z informacją, że model został poprawnie przekonwertowany do
     * formatu SBML.
     *
     * @param message wiadomość do wyświetlenia.
     */
    private void showResultingDialog(String message) {
        messageDialog.setContentText(message);
        messageDialog.showAndWait();
    }

    /**
     * Pokaż dialog z błędem, że model nie został poprawnie przekonwertowanyd do
     * formatu SBML.
     *
     * @param errorMessage wiadomość z błędem.
     */
    private void showErrorDialog(String errorMessage) {
        errorDialog.setContentText(errorMessage);
        errorDialog.showAndWait();
    }

}
