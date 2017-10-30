package convertsbml.view;

import convertsbml.model.entities.slv.EquationSlv;
import convertsbml.model.entities.slv.ModelSlv;
import convertsbml.model.entities.slv.ParameterSlv;
import convertsbml.model.entities.slv.RuleSlv;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 *
 * @author Magda
 */
public class StatisticsSlvView implements Initializable {

    @FXML
    private Label modelPathLbl;
    @FXML
    private Label modelNameLbl;
    @FXML
    private Label equationsAmountLbl;
    @FXML
    private Label parametersAmountLbl;
    @FXML
    private Label rulesAmountLbl;
    @FXML
    private TextArea statisticsArea;

    private ModelSlv modelSlv;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void postInitialize() {
        modelPathLbl.setText(modelSlv.getSlvFile().getAbsolutePath());
        modelNameLbl.setText(modelSlv.getName());
        Integer equationsSize = modelSlv.getEquations().size();
        equationsAmountLbl.setText("" + equationsSize);
        Integer parametersSize = modelSlv.getParameters().size();
        parametersAmountLbl.setText("" + parametersSize);
        Integer rulesSize = modelSlv.getRules().size();
        rulesAmountLbl.setText("" + rulesSize);
    }

    public void setData(ModelSlv modelSlv) {
        this.modelSlv = modelSlv;
    }

    @FXML
    private void showEquationsAction() {
        statisticsArea.setText("");
        for (EquationSlv equation : modelSlv.getEquations()) {
            statisticsArea.appendText(equation.getFull() + "\n");
        }
    }

    @FXML
    private void showParametersAction() {
        statisticsArea.setText("");
        for (ParameterSlv parameter : modelSlv.getParameters()) {
            statisticsArea.appendText(parameter.getName() + "\n");
        }
    }

    @FXML
    private void showRulesAction() {
        statisticsArea.setText("");
        for (RuleSlv rule : modelSlv.getRules()) {
            statisticsArea.appendText(rule.getName() + "\n");
        }
    }

}
