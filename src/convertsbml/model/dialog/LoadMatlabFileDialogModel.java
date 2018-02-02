package convertsbml.model.dialog;

import convertsbml.Const;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model z danymi dla dialogu związanego z wczytywaniem modelu Matlab -
 * {@link LoadMatlabFileDialogView}.
 *
 * @author Magda
 */
public class LoadMatlabFileDialogModel {

    private BooleanProperty isSimpleModel;
    private BooleanProperty isComplexModel;
    private StringProperty simpleModelPath;
    private StringProperty simpleParametersPath;
    private StringProperty simpleSimulationEntryPath;
    private StringProperty complexDeterministicModelPath;
    private StringProperty complexDeterministicParametersPath;
    private StringProperty complexStochasticModelPath;
    private StringProperty complexStochasticParametersPath;
    private StringProperty complexStochasticSimulationEntryPath;
    private StringProperty complexDeterministicSimulationEntryPath;
    private StringProperty stochasticStatusChangePath;
    private String name;

    /**
     * Domyślny konstruktor inicjalizujący pola.
     */
    public LoadMatlabFileDialogModel() {
        isSimpleModel = new SimpleBooleanProperty(Boolean.FALSE);
        isComplexModel = new SimpleBooleanProperty(Boolean.FALSE);
        simpleModelPath = new SimpleStringProperty(Const.EMPTY);
        simpleParametersPath = new SimpleStringProperty(Const.EMPTY);
        simpleSimulationEntryPath = new SimpleStringProperty(Const.EMPTY);
        complexDeterministicModelPath = new SimpleStringProperty(Const.EMPTY);
        complexDeterministicParametersPath = new SimpleStringProperty(Const.EMPTY);
        complexStochasticModelPath = new SimpleStringProperty(Const.EMPTY);
        complexStochasticParametersPath = new SimpleStringProperty(Const.EMPTY);
        complexStochasticSimulationEntryPath = new SimpleStringProperty(Const.EMPTY);
        complexDeterministicSimulationEntryPath = new SimpleStringProperty(Const.EMPTY);
        stochasticStatusChangePath = new SimpleStringProperty(Const.EMPTY);
    }

    public BooleanProperty getIsSimpleModel() {
        return isSimpleModel;
    }

    public void setIsSimpleModel(BooleanProperty isSimpleModel) {
        this.isSimpleModel = isSimpleModel;
    }

    public BooleanProperty getIsComplexModel() {
        return isComplexModel;
    }

    public void setIsComplexModel(BooleanProperty isComplexModel) {
        this.isComplexModel = isComplexModel;
    }

    public StringProperty getSimpleModelPath() {
        return simpleModelPath;
    }

    public void setSimpleModelPath(String simpleModelPath) {
        this.simpleModelPath.setValue(simpleModelPath);
    }

    public StringProperty getSimpleParametersPath() {
        return simpleParametersPath;
    }

    public void setSimpleParametersPath(String simpleParametersPath) {
        this.simpleParametersPath.setValue(simpleParametersPath);
    }

    public StringProperty getComplexDeterministicModelPath() {
        return complexDeterministicModelPath;
    }

    public void setComplexDeterministicModelPath(String complexDeterministicModelPath) {
        this.complexDeterministicModelPath.setValue(complexDeterministicModelPath);
    }

    public StringProperty getComplexDeterministicParametersPath() {
        return complexDeterministicParametersPath;
    }

    public void setComplexDeterministicParametersPath(String complexDeterministicParametersPath) {
        this.complexDeterministicParametersPath.setValue(complexDeterministicParametersPath);
    }

    public StringProperty getComplexStochasticModelPath() {
        return complexStochasticModelPath;
    }

    public void setComplexStochasticModelPath(String complexStochasticModelPath) {
        this.complexStochasticModelPath.setValue(complexStochasticModelPath);
    }

    public StringProperty getComplexStochasticParametersPath() {
        return complexStochasticParametersPath;
    }

    public void setComplexStochasticParametersPath(String complexStochasticParametersPath) {
        this.complexStochasticParametersPath.setValue(complexStochasticParametersPath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StringProperty getSimpleSimulationEntryPath() {
        return simpleSimulationEntryPath;
    }

    public void setSimpleSimulationEntryPath(String simpleSimulationEntryPath) {
        this.simpleSimulationEntryPath.setValue(simpleSimulationEntryPath);
    }

    public StringProperty getComplexStochasticSimulationEntryPath() {
        return complexStochasticSimulationEntryPath;
    }

    public void setComplexStochasticSimulationEntryPath(String complexStochasticSimulationEntryPath) {
        this.complexStochasticSimulationEntryPath.setValue(complexStochasticSimulationEntryPath);
    }

    public StringProperty getComplexDeterministicSimulationEntryPath() {
        return complexDeterministicSimulationEntryPath;
    }

    public void setComplexDeterministicSimulationEntryPath(String complexDeterministicSimulationEntryPath) {
        this.complexDeterministicSimulationEntryPath.setValue(complexDeterministicSimulationEntryPath);
    }

    public StringProperty getStochasticStatusChangePath() {
        return stochasticStatusChangePath;
    }

    public void setStochasticStatusChangePath(String stochasticStatusChangePath) {
        this.stochasticStatusChangePath.setValue(stochasticStatusChangePath);
    }

}
