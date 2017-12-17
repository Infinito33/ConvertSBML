package convertsbml.model.entities.matlab;

import java.util.ArrayList;
import java.util.List;

/**
 * Model danych dla prostego modelu matlab.
 *
 * @author Magda
 */
public class SimpleMatlabData extends AbstractMatlabModel {

    private List<EquationM> yEquations;

    public SimpleMatlabData() {
        yEquations = new ArrayList<>();
    }

    public List<EquationM> getyEquations() {
        return yEquations;
    }

    public void setyEquations(List<EquationM> yEquations) {
        this.yEquations = yEquations;
    }

}
