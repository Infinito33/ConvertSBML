package convertsbml.model.entities.matlab;

import convertsbml.model.enums.EComplexityMatlabModel;

/**
 * Dane pochodzące z plików matlab.
 *
 * @author Magda
 */
public class ModelMatlab {

    private String name;
    
    private SimpleMatlabData simpleModel;

    private ComplexMatlabData deterministicModel;
    private ComplexMatlabData stochasticModel;
    
    private EComplexityMatlabModel generalType;

    public ComplexMatlabData getDeterministicModel() {
        return deterministicModel;
    }

    public void setDeterministicModel(ComplexMatlabData deterministicModel) {
        this.deterministicModel = deterministicModel;
    }

    public ComplexMatlabData getStochasticModel() {
        return stochasticModel;
    }

    public void setStochasticModel(ComplexMatlabData stochasticModel) {
        this.stochasticModel = stochasticModel;
    }

    public SimpleMatlabData getSimpleModel() {
        return simpleModel;
    }

    public void setSimpleModel(SimpleMatlabData simpleModel) {
        this.simpleModel = simpleModel;
    }

    public EComplexityMatlabModel getGeneralType() {
        return generalType;
    }

    public void setGeneralType(EComplexityMatlabModel generalType) {
        this.generalType = generalType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
