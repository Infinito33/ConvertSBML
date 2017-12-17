package convertsbml.model.entities.matlab;

import convertsbml.model.enums.EComplexMatlabModelType;
import java.util.ArrayList;
import java.util.List;

/**
 * Model danych dla złożonego modelu matlab.
 *
 * @author Magda
 */
public class ComplexMatlabData extends AbstractMatlabModel {

    private List<String> apoptoticFactors;
    private List<String> apoptopicFactorsVars;
    private EComplexMatlabModelType modelType;
    private List<String> yVariables;

    public ComplexMatlabData() {
        apoptoticFactors = new ArrayList<>();
        yVariables = new ArrayList<>();
        apoptopicFactorsVars = new ArrayList<>();
    }

    public List<String> getApoptopicFactors() {
        return apoptoticFactors;
    }

    public void setApoptoticFactors(List<String> apoptoticFactors) {
        this.apoptoticFactors = apoptoticFactors;
    }

    public EComplexMatlabModelType getModelType() {
        return modelType;
    }

    public void setModelType(EComplexMatlabModelType modelType) {
        this.modelType = modelType;
    }

    public List<String> getyVariables() {
        return yVariables;
    }

    public void setyVariables(List<String> yVariables) {
        this.yVariables = yVariables;
    }

    public List<String> getApoptopicFactorsVars() {
        return apoptopicFactorsVars;
    }

    public void setApoptopicFactorsVars(List<String> apoptopicFactorsVars) {
        this.apoptopicFactorsVars = apoptopicFactorsVars;
    }

}
