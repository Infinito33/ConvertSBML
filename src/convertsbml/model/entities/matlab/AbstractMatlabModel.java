package convertsbml.model.entities.matlab;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstrakcyjny model danych dla modeli matlab.
 *
 * @author Magda
 */
public abstract class AbstractMatlabModel {

    private String function;
    private String params;
    private String zeros;
    private List<EquationM> equations;
    private List<ParameterMatlab> parameters;
    private File modelFile;
    private File parameterFile;

    public AbstractMatlabModel() {
        equations = new ArrayList<>();
        parameters = new ArrayList<>();
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

}
