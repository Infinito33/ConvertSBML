package convertsbml.model.entities.matlab;

import java.util.ArrayList;
import java.util.List;

/**
 * Model danych dla prostego modelu matlab.
 *
 * @author Magda
 */
public class SimpleMatlabData extends AbstractMatlabModel {
    
    private List<String> yEquations;
    
    public SimpleMatlabData() {
        yEquations = new ArrayList<>();
    }

    public List<String> getyEquations() {
        return yEquations;
    }

    public void setyEquations(List<String> yEquations) {
        this.yEquations = yEquations;
    }
    
    
    
}
