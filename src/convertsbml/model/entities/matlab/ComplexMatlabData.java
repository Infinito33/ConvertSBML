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
    private EComplexMatlabModelType modelType;

    public ComplexMatlabData() {
        apoptoticFactors = new ArrayList<>();
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

}
