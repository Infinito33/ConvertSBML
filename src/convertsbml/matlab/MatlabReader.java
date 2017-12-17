package convertsbml.matlab;

import convertsbml.basic.AbstractReader;
import convertsbml.model.dialog.LoadMatlabFileDialogModel;
import convertsbml.model.entities.matlab.ModelMatlab;
import convertsbml.model.entities.matlab.ParameterMatlab;
import convertsbml.model.entities.matlab.ComplexMatlabData;
import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.matlab.SimpleMatlabData;
import convertsbml.model.enums.EComplexMatlabModelType;
import convertsbml.model.enums.EComplexityMatlabModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private final String PARAMETER = "[A-Za-z]*=.*;";

    private final String C_MODEL_FACTOR_EQUATION = "^[A-Z]{2,}.*";
    private final String C_MODEL_EQUATION = "dy\\([0-9]*\\)=.*";

    private final String S_Y_EQUATION = "[A-Za-z]*=y.*";
    private final String S_EQUATION = "[A-Za-z0-9].*=[^0]{1}.*";

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

    public MatlabReader(LoadMatlabFileDialogModel matlabData) {
        this.matlabData = matlabData;
        this.extractor = new MatlabContentExtractor();
    }

    /**
     * Ogólny odczyt modelu.
     *
     * @param isSimpleModel czy jest to model prosty.
     * @return
     */
    public ModelMatlab readModel(Boolean isSimpleModel) {
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
            List<String> simpleModelLines = readFileAsList(matlabData.getSimpleModelPath().get());
            List<String> simpleParametersLines = readFileAsList(matlabData.getSimpleParametersPath().get());

            SimpleMatlabData simpleMatlabData = analyzeSimpleModel(simpleModelLines);
            List<ParameterMatlab> parameters = analyzeParameters(simpleParametersLines);

            simpleMatlabData.setParameters(parameters);
            simpleMatlabData.setModelContent(simpleModelLines);
            simpleMatlabData.setParametersContent(simpleParametersLines);
            simpleMatlabData.setModelFile(new File(matlabData.getSimpleModelPath().get()));
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
     * @throws java.io.IOException
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
            ComplexMatlabData deterministicModel = readDeterministicPart();
            ComplexMatlabData stochasticModel = readStochasticPart();

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
        List<String> deterministicModelLines = readFileAsList(matlabData.getComplexDeterministicModelPath().get());
        List<String> deterministicParametersLines = readFileAsList(matlabData.getComplexDeterministicParametersPath().get());

        ComplexMatlabData deterministicModel = analyzeComplexModel(deterministicModelLines);
        extractor.resetVariableNumber();
        List<ParameterMatlab> parameters = analyzeParameters(deterministicParametersLines);
        deterministicModel.setParameters(parameters);
        deterministicModel.setModelType(EComplexMatlabModelType.DETERMINISTIC);
        deterministicModel.setModelContent(deterministicModelLines);
        deterministicModel.setParametersContent(deterministicParametersLines);
        deterministicModel.setModelFile(new File(matlabData.getComplexDeterministicModelPath().get()));

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

        ComplexMatlabData stochasticModel = analyzeComplexModel(stochasticModelLines);
        extractor.resetVariableNumber();
        List<ParameterMatlab> parameters = analyzeParameters(stochasticParametersLines);
        stochasticModel.setParameters(parameters);
        stochasticModel.setModelType(EComplexMatlabModelType.STOCHASTIC);
        stochasticModel.setModelContent(stochasticModelLines);
        stochasticModel.setParametersContent(stochasticParametersLines);
        stochasticModel.setModelFile(new File(matlabData.getComplexStochasticModelPath().get()));

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
                continue;
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
            boolean isParamFound = PARAMETER_PATTERN.matcher(line).find() && !line.startsWith("%") && !line.startsWith("function");
            if (isParamFound) {
                ParameterMatlab param = extractor.extractParameterFrom(line);
                param.setFull(line);
                parameters.add(param);
            }
        }
        return parameters;
    }

}
