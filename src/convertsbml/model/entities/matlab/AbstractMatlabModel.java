package convertsbml.model.entities.matlab;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Abstrakcyjny model danych dla modeli matlab.
 *
 * @author Magda
 */
public abstract class AbstractMatlabModel {

    private String function;
    private Set<String> functionVariables;
    private String params;
    private String zeros;
    private List<EquationM> equations;
    private List<ParameterMatlab> parameters;
    private File modelFile;
    private File parameterFile;
    private List<String> modelContent;
    private List<String> parametersContent;

    public AbstractMatlabModel() {
        equations = new ArrayList<>();
        parameters = new ArrayList<>();
        modelContent = new ArrayList<>();
        parametersContent = new ArrayList<>();
        functionVariables = new HashSet<>();
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getZeros() {
        return zeros;
    }

    public void setZeros(String zeros) {
        this.zeros = zeros;
    }

    public List<EquationM> getEquations() {
        return equations;
    }

    public void setEquations(List<EquationM> equations) {
        this.equations = equations;
    }

    public List<ParameterMatlab> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterMatlab> parameters) {
        this.parameters = parameters;
    }

    public File getModelFile() {
        return modelFile;
    }

    public void setModelFile(File modelFile) {
        this.modelFile = modelFile;
    }

    public File getParameterFile() {
        return parameterFile;
    }

    public void setParameterFile(File parameterFile) {
        this.parameterFile = parameterFile;
    }

    public List<String> getModelContent() {
        return modelContent;
    }

    public void setModelContent(List<String> modelContent) {
        this.modelContent = modelContent;
    }

    public List<String> getParametersContent() {
        return parametersContent;
    }

    public void setParametersContent(List<String> parametersContent) {
        this.parametersContent = parametersContent;
    }

    public Set<String> getFunctionVariables() {
        return functionVariables;
    }

    public void setFunctionVariables(Set<String> functionVariables) {
        this.functionVariables = functionVariables;
    }

}
