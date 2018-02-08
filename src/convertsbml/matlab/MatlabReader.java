package convertsbml.matlab;

import convertsbml.basic.AbstractReader;
import convertsbml.model.dialog.LoadMatlabFileDialogModel;
import convertsbml.model.entities.matlab.ModelMatlab;
import convertsbml.model.entities.matlab.ParameterMatlab;
import convertsbml.model.entities.matlab.ComplexMatlabData;
import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.matlab.SimpleMatlabData;
import convertsbml.model.entities.matlab.StatusChangeMatlabData;
import convertsbml.model.enums.EComplexMatlabModelType;
import convertsbml.model.enums.EComplexityMatlabModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Klasa odpowiedzialna za odczyt plików Matlaba o rozszerzeniu *.m.
 *
 * @author Magda
 */
public class MatlabReader extends AbstractReader {

    private LoadMatlabFileDialogModel matlabData;
    private MatlabContentExtractor extractor;

    /**
     * Lista REGEX do wykrywania odpowiednich części pliku. R oznacza Regular, C
     * oznacza Complex.
     */
    private final String MODEL_FUNCTION = "function.*";
    private final String MODEL_PARAMETER = "^\\[.*";
    private final String MODEL_ZEROS = "zeros";
    private final String PARAMETER = "[A-Za-z0-9]*=.*;";

    private final String C_MODEL_FACTOR_EQUATION = "^[A-Z]{2,}.*";
    private final String C_MODEL_EQUATION = "dy\\([0-9]*\\)=.*";

    private final String S_Y_EQUATION = "[A-Za-z]*=y.*";
    private final String S_EQUATION = "[A-Za-z0-9].*=[^0]{1}.*";

    /**
     * Wzorce dla pliku status change.
     */
    private final String STATUS_ASSIGNMENTS = "[A-Za-z0-9]{1,10}=[A-Za-z0-9]{1,10}";
    private final String STATUS_RO = "ro=.*";
    private final String STATUS_ROINT = "roint=.*";
    private final String STATUS_FD = "fd=.*";
    private final String STATUS_A = "a=.*";
    private final String STATUS_TCHANGE = "Tchange=.*";
    private final String STATUS_EQUATION = "[A-Za-z0-9]{1,10}=.*";

    /**
     * Wzorce dla simulation file.
     */
    private final String SIMULATION_VARIABLES = "[A-Za-z0-9\\(\\)].*=[0-9\\*\\+\\-\\/].*";

    /**
     * Lista wzorców dopasowujących tekst do konkretnych wyrażeń REGEX.
     */
    private final Pattern MODEL_FUNCTION_PATTERN = Pattern.compile(MODEL_FUNCTION);
    private final Pattern MODEL_PARAMETER_PATTERN = Pattern.compile(MODEL_PARAMETER);
    private final Pattern MODEL_ZEROS_PATTERN = Pattern.compile(MODEL_ZEROS);
    private final Pattern PARAMETER_PATTERN = Pattern.compile(PARAMETER);

    private final Pattern C_MODEL_FACTOR_EQUATION_PATTERN = Pattern.compile(C_MODEL_FACTOR_EQUATION);
    private final Pattern C_MODEL_EQUATION_PATTERN = Pattern.compile(C_MODEL_EQUATION);

    private final Pattern S_Y_EQUATION_PATTERN = Pattern.compile(S_Y_EQUATION);
    private final Pattern S_EQUATION_PATTERN = Pattern.compile(S_EQUATION);

    /**
     * Lista wzorców dopasowujących tekst z pliku status change do konkretnych
     * wyrażeń REGEX.
     */
    private final Pattern STATUS_ASSIGNMENTS_PATTERN = Pattern.compile(STATUS_ASSIGNMENTS);
    private final Pattern STATUS_RO_PATTERN = Pattern.compile(STATUS_RO);
    private final Pattern STATUS_ROINT_PATTERN = Pattern.compile(STATUS_ROINT);
    private final Pattern STATUS_FD_PATTERN = Pattern.compile(STATUS_FD);
    private final Pattern STATUS_A_PATTERN = Pattern.compile(STATUS_A);
    private final Pattern STATUS_TCHANGE_PATTERN = Pattern.compile(STATUS_TCHANGE);
    private final Pattern STATUS_EQUATION_PATTERN = Pattern.compile(STATUS_EQUATION);

    private final Pattern SIMULATION_VARIABLES_PATTERN = Pattern.compile(SIMULATION_VARIABLES);

    public MatlabReader() {
        this.extractor = new MatlabContentExtractor();
    }

    public MatlabReader(LoadMatlabFileDialogModel matlabData) {
        this.matlabData = matlabData;
        this.extractor = new MatlabContentExtractor();
    }

    /**
     * Ustawienie modelu z dialogu do wykorzystania w przetwarzaniu.
     *
     * @param matlabData model z danymi z dialogu.
     */
    public void setModel(LoadMatlabFileDialogModel matlabData) {
        this.matlabData = matlabData;
    }

    /**
     * Ogólny odczyt modelu.
     *
     * @return gotowy model matlab ze wszystkimi danymi.
     */
    public ModelMatlab readModel() {
        Boolean isSimpleModel = matlabData.getIsSimpleModel().get();
        if (isSimpleModel) {
            return readSimpleModel();
        } else {
            return readComplexModel();
        }
    }

    /**
     * Odczyt modelu prostego Matlab.
     *
     * @return Gotowy obiekt modelu z modelem prostym Matlab.
     */
    public ModelMatlab readSimpleModel() {
        ModelMatlab matlabModel = new ModelMatlab();
        matlabModel.setGeneralType(EComplexityMatlabModel.SIMPLE);
        try {
            //Odczyt pliku z modelem i parametrami jako lista linii.
            List<String> simpleModelLines = readFileAsList(matlabData.getSimpleModelPath().get());
            List<String> simpleParametersLines = readFileAsList(matlabData.getSimpleParametersPath().get());
            List<String> simpleSimulationLines = readFileAsList(matlabData.getSimpleSimulationEntryPath().get());

            //Analiza prostego modelu i parametrów i przypisanie danych
            SimpleMatlabData simpleMatlabData = analyzeSimpleModel(simpleModelLines);
            List<ParameterMatlab> parameters = analyzeParameters(simpleParametersLines);
            Map<String, Double> analyzeSimulationFile = analyzeSimulationFile(simpleSimulationLines, parameters);

            simpleMatlabData.setParameters(parameters);
            simpleMatlabData.setModelContent(simpleModelLines);
            simpleMatlabData.setParametersContent(simpleParametersLines);
            simpleMatlabData.setModelFile(new File(matlabData.getSimpleModelPath().get()));
            simpleMatlabData.setVariableInitials(analyzeSimulationFile);
            matlabModel.setSimpleModel(simpleMatlabData);
        } catch (IOException ex) {
            Logger.getLogger(MatlabReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matlabModel;
    }

    /**
     * Analiza prostego modelu z matlaba.
     *
     * @param simpleModelLines plik modelu w postaci linii.
     * @return dane modelu.
     */
    public SimpleMatlabData analyzeSimpleModel(List<String> simpleModelLines) throws IOException {
        SimpleMatlabData simpleMatlabData = new SimpleMatlabData();
        List<EquationM> equations = new ArrayList<>();
        List<EquationM> yEquations = new ArrayList<>();

        for (String line : simpleModelLines) {
            //Wyszukiwanie elementu funkcji
            if (simpleMatlabData.getFunction() == null) {
                boolean isFunctionFound = MODEL_FUNCTION_PATTERN.matcher(line).find();
                if (isFunctionFound) {
                    simpleMatlabData.setFunction(line);
                    List<String> variables = extractor.extractVariablesFrom(line);
                    simpleMatlabData.getFunctionVariables().addAll(variables);
                    continue;
                }
            }
            //Wyszukiwanie listy parametrów
            if (simpleMatlabData.getParams() == null) {
                boolean isParameterListFound = MODEL_PARAMETER_PATTERN.matcher(line).find();
                if (isParameterListFound) {
                    simpleMatlabData.setParams(line);
                    continue;
                }
            }
            //Wyszukiwanie funkcji zeros
            if (simpleMatlabData.getZeros() == null) {
                boolean isZerosFound = MODEL_ZEROS_PATTERN.matcher(line).find();
                if (isZerosFound) {
                    simpleMatlabData.setZeros(line);
                    continue;
                }
            }
            //Wyszukiwanie równań typu: AAA=y(5);
            boolean isYEquationFound = S_Y_EQUATION_PATTERN.matcher(line).find();
            if (isYEquationFound) {
                EquationM yEquation = extractor.extractYEquationFrom(line);
                yEquations.add(yEquation);
                continue;
            }
            //Wyszukiwanie równań
            boolean isEquationFound = S_EQUATION_PATTERN.matcher(line).find() && !line.startsWith("%") && !line.startsWith("dy(");
            if (isEquationFound) {
                EquationM equation = extractor.extractEquationFrom(line);
                equations.add(equation);
            }
        }
        simpleMatlabData.setyEquations(yEquations);
        simpleMatlabData.setEquations(equations);
        return simpleMatlabData;
    }

    /**
     * Odczyt złożonego modelu Matlab.
     *
     * @return Gotowy obiekt modelu z konkretnym modelem wczytanym z pliku.
     */
    public ModelMatlab readComplexModel() {
        ModelMatlab matlabModel = new ModelMatlab();
        matlabModel.setGeneralType(EComplexityMatlabModel.COMPLEX);

        try {
            //Odczyt obu złożonych modeli.
            ComplexMatlabData deterministicModel = readDeterministicPart();
            ComplexMatlabData stochasticModel = readStochasticPart();

            //Ustawienie ich w głównym modelu.
            matlabModel.setDeterministicModel(deterministicModel);
            matlabModel.setStochasticModel(stochasticModel);
        } catch (IOException ex) {
            Logger.getLogger(MatlabReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return matlabModel;
    }

    /**
     * Odczytuje deterministyczny model wraz z jego parametrami z plików *.m.
     *
     * @return dane odczytane z pliku z modelem.
     * @throws IOException
     */
    private ComplexMatlabData readDeterministicPart() throws IOException {
        //Odczyt modelu deterministycznego i parametrów jako linii.
        List<String> deterministicModelLines = readFileAsList(matlabData.getComplexDeterministicModelPath().get());
        List<String> deterministicParametersLines = readFileAsList(matlabData.getComplexDeterministicParametersPath().get());
        List<String> deterministicSimulationLines = readFileAsList(matlabData.getComplexDeterministicSimulationEntryPath().get());

        //Wyciągnięcie danych z modelu i utworzenie obiektu modelu z danymi.
        ComplexMatlabData deterministicModel = analyzeComplexModel(deterministicModelLines);
        List<ParameterMatlab> parameters = analyzeParameters(deterministicParametersLines);
        Map<String, Double> simulationData = analyzeSimulationFile(deterministicSimulationLines, parameters);

        deterministicModel.setParameters(parameters);
        deterministicModel.setModelType(EComplexMatlabModelType.DETERMINISTIC);
        deterministicModel.setModelContent(deterministicModelLines);
        deterministicModel.setParametersContent(deterministicParametersLines);
        deterministicModel.setModelFile(new File(matlabData.getComplexDeterministicModelPath().get()));
        deterministicModel.setVariableInitials(simulationData);

        return deterministicModel;
    }

    /**
     * Odczytuje stochastyczny model wraz z jego parametrami z plików *.m.
     *
     * @return dane odczytane z pliku z modelem.
     * @throws IOException
     */
    private ComplexMatlabData readStochasticPart() throws IOException {
        List<String> stochasticModelLines = readFileAsList(matlabData.getComplexStochasticModelPath().get());
        List<String> stochasticParametersLines = readFileAsList(matlabData.getComplexStochasticParametersPath().get());
        List<String> stochasticSimulationLines = readFileAsList(matlabData.getComplexStochasticSimulationEntryPath().get());
        List<String> stochasticStatusChangeLines = readFileAsList(matlabData.getStochasticStatusChangePath().get());

        ComplexMatlabData stochasticModel = analyzeComplexModel(stochasticModelLines);
        List<ParameterMatlab> parameters = analyzeParameters(stochasticParametersLines);
        Map<String, Double> simulationData = analyzeSimulationFile(stochasticSimulationLines, parameters);
        StatusChangeMatlabData statusChangeData = analyzeStatusChange(stochasticStatusChangeLines);

        stochasticModel.setParameters(parameters);
        stochasticModel.setModelType(EComplexMatlabModelType.STOCHASTIC);
        stochasticModel.setModelContent(stochasticModelLines);
        stochasticModel.setParametersContent(stochasticParametersLines);
        stochasticModel.setModelFile(new File(matlabData.getComplexStochasticModelPath().get()));
        stochasticModel.setVariableInitials(simulationData);
        stochasticModel.setStatusChangeData(statusChangeData);

        return stochasticModel;
    }

    /**
     * Analiza złożonego modelu z matlaba.
     *
     * @param complexModelLines plik modelu w postaci linii.
     * @return dane modelu.
     */
    private ComplexMatlabData analyzeComplexModel(List<String> complexModelLines) {
        ComplexMatlabData complexMatlabData = new ComplexMatlabData();
        List<String> apoptoticFactors = new ArrayList<>();
        List<EquationM> equations = new ArrayList<>();
        for (String line : complexModelLines) {
            //Wyszukiwanie elementu funkcji
            if (complexMatlabData.getFunction() == null) {
                boolean isFunctionFound = MODEL_FUNCTION_PATTERN.matcher(line).find();
                if (isFunctionFound) {
                    List<String> variables = extractor.extractVariablesFrom(line);
                    complexMatlabData.getFunctionVariables().addAll(variables);
                    complexMatlabData.setFunction(line);
                    continue;
                }
            }
            //Wyszukiwanie listy parametrów
            if (complexMatlabData.getParams() == null) {
                boolean isParameterListFound = MODEL_PARAMETER_PATTERN.matcher(line).find();
                if (isParameterListFound) {
                    complexMatlabData.setParams(line);
                    continue;
                }
            }
            //Wyszukiwanie apoptotic factors
            boolean areFactorEquationsFound = C_MODEL_FACTOR_EQUATION_PATTERN.matcher(line).find();
            if (areFactorEquationsFound) {
                String var = extractor.extractApoptopicFactorVariable(line);
                apoptoticFactors.add(line);
                complexMatlabData.getApoptopicFactorsVars().add(var);
                continue;
            }
            //Wyszukiwanie funkcji zeros
            if (complexMatlabData.getZeros() == null) {
                boolean isZerosFound = MODEL_ZEROS_PATTERN.matcher(line).find();
                if (isZerosFound) {
                    complexMatlabData.setZeros(line);
                    continue;
                }
            }
            //Wyszukiwanie równań
            boolean isEquationFound = C_MODEL_EQUATION_PATTERN.matcher(line).find() && !line.startsWith("%");
            if (isEquationFound) {
                Set<String> variables = new HashSet<>();
                EquationM equation = extractor.extractComplexEquationFrom(line, variables);
                equations.add(equation);
                complexMatlabData.getFunctionVariables().addAll(variables);
            }
        }
        complexMatlabData.setApoptoticFactors(apoptoticFactors);
        complexMatlabData.setEquations(equations);
        return complexMatlabData;
    }

    /**
     * Analiza pliku z parametrami dla modelu Matlab.
     *
     * @param parametersLines plik parametrów w postaci linii.
     * @return listę parametrów modelu Matlab.
     */
    private List<ParameterMatlab> analyzeParameters(List<String> parametersLines) {
        List<ParameterMatlab> parameters = new ArrayList<>();
        for (String paramLine : parametersLines) {
            String line = paramLine.trim();
            //Szukanie parametru, jeśli znaleziono to wydobywanie go z konkretnej linii.
            boolean isParamFound = PARAMETER_PATTERN.matcher(line).find() && !line.startsWith("%") && !line.startsWith("function");
            if (isParamFound) {
                ParameterMatlab param = extractor.extractParameterFrom(line);
                param.setFull(line);
                parameters.add(param);
            }
        }
        return parameters;
    }

    /**
     * Analiza pliku StatusChange z modelu stochastycznego Matlab.
     *
     * @param stochasticStatusChangeLines kolejne linie pliku statusChange
     * @return dane pliku status change.
     */
    private StatusChangeMatlabData analyzeStatusChange(List<String> stochasticStatusChangeLines) {
        List<EquationM> equations = new ArrayList<>();
        List<EquationM> assignments = new ArrayList<>();
        StatusChangeMatlabData statusChangeData = new StatusChangeMatlabData();
        for (String line : stochasticStatusChangeLines) {
            //Wyszukiwanie elementu funkcji
            if (statusChangeData.getFunction() == null) {
                boolean isFunctionFound = MODEL_FUNCTION_PATTERN.matcher(line).find();
                if (isFunctionFound) {
                    statusChangeData.setFunction(line);
                    List<String> variables = extractor.extractVariablesFrom(line);
                    statusChangeData.getFunctionVariables().addAll(variables);
                    continue;
                }
            }
            //Wyszukiwanie listy parametrów
            if (statusChangeData.getParams() == null) {
                boolean isParameterListFound = MODEL_PARAMETER_PATTERN.matcher(line).find();
                if (isParameterListFound) {
                    statusChangeData.setParams(line);
                    continue;
                }
            }
            //Wyszukiwanie 'ro'
            if (statusChangeData.getRo() == null) {
                boolean isRoFound = STATUS_RO_PATTERN.matcher(line).find();
                if (isRoFound) {
                    EquationM equation = extractor.extractEquationFrom(line);
                    statusChangeData.setRo(equation);
                    continue;
                }
            }

            //Wyszukiwanie 'roint'
            if (statusChangeData.getRoint() == null) {
                boolean isRointFound = STATUS_ROINT_PATTERN.matcher(line).find();
                if (isRointFound) {
                    EquationM equation = extractor.extractEquationFrom(line);
                    statusChangeData.setRoint(equation);
                    continue;
                }
            }

            //Wyszukiwanie 'fd'
            if (statusChangeData.getFd() == null) {
                boolean isFdFound = STATUS_FD_PATTERN.matcher(line).find();
                if (isFdFound) {
                    EquationM equation = extractor.extractEquationFrom(line);
                    statusChangeData.setFd(equation);
                    continue;
                }
            }

            //Wyszukiwanie 'a'
            if (statusChangeData.getA() == null) {
                boolean isAFound = STATUS_A_PATTERN.matcher(line).find();
                if (isAFound) {
                    EquationM equation = extractor.extractEquationFrom(line);
                    statusChangeData.setA(equation);
                    continue;
                }
            }

            //Wyszukiwanie 'Tchange'
            if (statusChangeData.getTchange() == null) {
                boolean isTchangeFound = STATUS_TCHANGE_PATTERN.matcher(line).find();
                if (isTchangeFound) {
                    EquationM equation = extractor.extractEquationFrom(line);
                    statusChangeData.setTchange(equation);
                    continue;
                }
            }

            //Wyszukiwanie równań
            boolean isEquationFound = STATUS_EQUATION_PATTERN.matcher(line).find() && !line.startsWith("%") && !line.trim().startsWith("if") && !line.trim().endsWith("ss;");
            if (isEquationFound) {
                EquationM equation = extractor.extractStatusChangeEquationFrom(line);
                equations.add(equation);
                statusChangeData.getFunctionVariables().add(equation.getLeftSide());
                continue;
            }

            //Wyszukiwanie przypisań
            boolean isAssignmentFound = STATUS_ASSIGNMENTS_PATTERN.matcher(line).find() && !line.startsWith("%") && !line.contains("rand") && !line.trim().startsWith("if") && !line.trim().endsWith("ss;");
            if (isAssignmentFound) {
                Set<String> variables = new HashSet<>();
                EquationM assignment = extractor.extractStatusChangeAssignEquationFrom(line, variables);
                assignments.add(assignment);
                statusChangeData.getFunctionVariables().addAll(variables);
            }
        }
        statusChangeData.getEquations().addAll(equations);

        return statusChangeData;
    }

    /**
     * Analiza pliku simulation z początkowymi wartościami zmiennych.
     *
     * @param lines plik w postaci linii.
     * @param parameters lista parametrów wraz z wartościami.
     * @return mapa zawierająca pary zmienna - wartość.
     */
    public Map<String, Double> analyzeSimulationFile(List<String> lines, List<ParameterMatlab> parameters) {
        Map<String, Double> initialValues = new HashMap<>();

        for (String line : lines) {
            //Wyszukiwanie wartości zmiennych
            boolean isVariableFound = SIMULATION_VARIABLES_PATTERN.matcher(line).find();
            if (isVariableFound) {
                extractor.extractVariableValue(line, initialValues, parameters);
            }
        }

        return initialValues;
    }

}
