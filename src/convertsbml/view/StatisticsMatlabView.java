package convertsbml.view;

import convertsbml.model.entities.matlab.AbstractMatlabModel;
import convertsbml.model.entities.matlab.EquationM;
import convertsbml.model.entities.matlab.ParameterMatlab;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

/**
 * Klasa odpowiedzialna za widok związany z przedstawieniem statystyk związanych
 * z modelem Matlab.
 *
 * @author Magda
 */
public class StatisticsMatlabView implements Initializable {

    @FXML
    private Label modelTypeLbl;
    @FXML
    private CheckBox stochasticModelCheck;
    @FXML
    private CheckBox deterministicModelCheck;
    @FXML
    private Label modelNameLbl;
    @FXML
    private Label modelPathLbl;
    @FXML
    private Label equationsAmountLbl;
    @FXML
    private Label parametersAmountLbl;
    @FXML
    private TextArea statisticsArea;
    @FXML
    private Button showEquationsBtn;
    @FXML
    private Button showParametersBtn;

    private List<AbstractMatlabModel> modelsMatlab;

    private Boolean isStochasticSelected;

    /**
     * Metoda pochodząca z interfejsu {@link Initializable}. Wymagana lecz
     * nieużywana.
     *
     * @param url ścieżka widoku.
     * @param rb bundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Inicjalizacja dodatkowa, która odbywa się po utworzeniu i wyświetleniu
     * elementów na ekranie.
     */
    public void postInitialize() {
        modelPathLbl.setText(modelsMatlab.get(0).getModelFile().getAbsolutePath());
        modelNameLbl.setText(modelsMatlab.get(0).getModelFile().getName());
        int equationsSize = modelsMatlab.get(0).getEquations().size();
        equationsAmountLbl.setText("" + equationsSize);
        int parametersSize = modelsMatlab.get(0).getParameters().size();
        parametersAmountLbl.setText("" + parametersSize);
        stochasticModelCheck.setSelected(Boolean.TRUE);
        isStochasticSelected = Boolean.TRUE;
    }

    /**
     * Ustawia model danych.
     *
     * @param modelsMatlab modele matlaba.
     */
    public void setData(AbstractMatlabModel... modelsMatlab) {
        this.modelsMatlab = Arrays.asList(modelsMatlab);

        updateChecksStatus();
    }

    /**
     * Aktualizuje widoczność dodatkowych check box'ów w pierwszym wierszu w
     * zależności od poakzywanego modelu.
     */
    private void updateChecksStatus() {
        if (modelsMatlab.size() == 1) {
            stochasticModelCheck.setDisable(Boolean.TRUE);
            deterministicModelCheck.setDisable(Boolean.TRUE);
            modelTypeLbl.setDisable(Boolean.TRUE);
            stochasticModelCheck.setVisible(Boolean.FALSE);
            deterministicModelCheck.setVisible(Boolean.FALSE);
            modelTypeLbl.setVisible(Boolean.FALSE);
        }
    }

    /**
     * Akcja do pokazania równań modelu.
     */
    @FXML
    private void showEquationsAction() {
        statisticsArea.setText("");

        if (modelsMatlab.size() == 1) {
            for (EquationM equation : modelsMatlab.get(0).getEquations()) {
                statisticsArea.appendText(equation.getFull() + "\n");
            }
        } else {
            if (stochasticModelCheck.isSelected()) {
                for (EquationM equation : modelsMatlab.get(0).getEquations()) {
                    statisticsArea.appendText(equation.getFull() + "\n");
                }
            } else {
                for (EquationM equation : modelsMatlab.get(1).getEquations()) {
                    statisticsArea.appendText(equation.getFull() + "\n");
                }
            }
        }
    }

    /**
     * Akcja do pokazania parametrów modelu.
     */
    @FXML
    private void showParametersAction() {
        statisticsArea.setText("");

        if (modelsMatlab.size() == 1) {
            for (ParameterMatlab parameter : modelsMatlab.get(0).getParameters()) {
                statisticsArea.appendText(parameter.getName() + "\n");
            }
        } else {
            if (stochasticModelCheck.isSelected()) {
                for (ParameterMatlab parameter : modelsMatlab.get(0).getParameters()) {
                    statisticsArea.appendText(parameter.getName() + "\n");
                }
            } else {
                for (ParameterMatlab parameter : modelsMatlab.get(1).getParameters()) {
                    statisticsArea.appendText(parameter.getName() + "\n");
                }
            }
        }
    }

    /**
     * Aktualizacja statusów check boxów.
     */
    @FXML
    private void updateSelectionStatus() {
        if (isStochasticSelected) {
            stochasticModelCheck.setSelected(Boolean.FALSE);
            deterministicModelCheck.setSelected(Boolean.TRUE);
            isStochasticSelected = Boolean.FALSE;
        } else {
            stochasticModelCheck.setSelected(Boolean.TRUE);
            deterministicModelCheck.setSelected(Boolean.FALSE);
            isStochasticSelected = Boolean.TRUE;
        }

    }

}
