package convertsbml.view.dialog;

import convertsbml.Const;
import convertsbml.controller.LoadMatlabFileDialogController;
import convertsbml.model.dialog.LoadMatlabFileDialogModel;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * FXML Controller class
 *
 * @author Magda
 */
public class LoadMatlabFileDialog implements Initializable {

    private final String MATLAB_EXT = "*.m";

    private Dialog<ButtonType> dialog;
    private LoadMatlabFileDialogModel model;
    private LoadMatlabFileDialogController controller;

    @FXML
    private CheckBox simpleModelCheck;
    @FXML
    private TextField simpleModelPath;
    @FXML
    private Button chooseSimpleModelPathBtn;
    @FXML
    private TextField simpleParametersPath;
    @FXML
    private Button chooseSimpleParameterPathBtn;

    @FXML
    private CheckBox complexModelCheck;
    @FXML
    private TextField complexDeterministicModelPath;
    @FXML
    private Button chooseComplexDeterministicModelPathBtn;
    @FXML
    private TextField complexDeterministicParametersPath;
    @FXML
    private Button chooseComplexDeterministicParametersPathBtn;
    @FXML
    private TextField complexStochasticModelPath;
    @FXML
    private Button chooseComplexStochasticModelPathBtn;
    @FXML
    private TextField complexStochasticParametersPath;
    @FXML
    private Button chooseComplexStochasticParametersPathBtn;

    /**
     * Inicjalizacja podstawowych elementów okna i ich utworzenie.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Dialog();
        ButtonType applyBtn = new ButtonType("Zatwierdź", ButtonData.APPLY);
        ButtonType cancelBtn = new ButtonType("Anuluj", ButtonData.CANCEL_CLOSE);
        dialog.getDialogPane().getButtonTypes().add(applyBtn);
        dialog.getDialogPane().getButtonTypes().add(cancelBtn);
        dialog.setTitle("Wczytaj model Matlab");
    }

    /**
     * Inicjalizacja pozostałych elementów już po utworzeniu okna.
     */
    public void postInitialize(GridPane pane) {
        dialog.getDialogPane().setContent(pane);
        model = controller.getModel();
        simpleModelCheck.setSelected(Boolean.TRUE);
        createBindings();
    }

    private void createBindings() {
        controller.getModel().getIsSimpleModel().bind(simpleModelCheck.selectedProperty());
        controller.getModel().getIsComplexModel().bind(complexModelCheck.selectedProperty());
        controller.getModel().getSimpleModelPath().bind(simpleModelPath.textProperty());
        controller.getModel().getSimpleParametersPath().bind(simpleParametersPath.textProperty());
        controller.getModel().getComplexStochasticModelPath().bind(complexStochasticModelPath.textProperty());
        controller.getModel().getComplexStochasticParametersPath().bind(complexStochasticParametersPath.textProperty());
        controller.getModel().getComplexDeterministicModelPath().bind(complexDeterministicModelPath.textProperty());
        controller.getModel().getComplexDeterministicParametersPath().bind(complexDeterministicParametersPath.textProperty());

        simpleModelPath.disableProperty().bindBidirectional(complexModelCheck.selectedProperty());
        simpleParametersPath.disableProperty().bindBidirectional(complexModelCheck.selectedProperty());
        chooseSimpleModelPathBtn.disableProperty().bindBidirectional(complexModelCheck.selectedProperty());
        chooseSimpleParameterPathBtn.disableProperty().bindBidirectional(complexModelCheck.selectedProperty());

        complexDeterministicModelPath.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        complexDeterministicParametersPath.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        chooseComplexDeterministicModelPathBtn.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        chooseComplexDeterministicParametersPathBtn.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        complexStochasticModelPath.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        complexStochasticParametersPath.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        chooseComplexStochasticModelPathBtn.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());
        chooseComplexStochasticParametersPathBtn.disableProperty().bindBidirectional(simpleModelCheck.selectedProperty());

    }

    public Boolean showDialog() {
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().getButtonData() == ButtonType.APPLY.getButtonData()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @FXML
    public void chooseSimpleModelPathAction() {
        //String modelPath = loadFile("Wybierz ścieżkę modelu prostego", MATLAB_EXT);
        //String modelPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\Projekt\\Modele Matlab");
        String modelPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab");
        simpleModelPath.setText(modelPath);
    }

    @FXML
    public void chooseSimpleParameterPathAction() {
        //String parametersPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT);
        //String parametersPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\Projekt\\Modele Matlab");
        String parametersPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab");
        simpleParametersPath.setText(parametersPath);
    }

    @FXML
    public void chooseComplexDeterministicModelPathAction() {
        //String modelPath = loadFile("Wybierz ścieżkę deterministycznego modelu złożonego", MATLAB_EXT);
        //String modelPath = loadFile("Wybierz ścieżkę deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\Projekt\\Modele Matlab");
        String modelPath = loadFile("Wybierz ścieżkę deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        complexDeterministicModelPath.setText(modelPath);
    }

    @FXML
    public void chooseComplexDeterministicParametersPathAction() {
        //String parametersPath = loadFile("Wybierz ścieżkę parametrów deterministycznego modelu złożonego", MATLAB_EXT);
        //String parametersPath = loadFile("Wybierz ścieżkę parametrów deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\Projekt\\Modele Matlab");
        String parametersPath = loadFile("Wybierz ścieżkę parametrów deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        complexDeterministicParametersPath.setText(parametersPath);
    }

    @FXML
    public void chooseComplexStochasticModelPathAction() {
        //String modelPath = loadFile("Wybierz ścieżkę stochastycznego modelu złożonego", MATLAB_EXT);
        //String modelPath = loadFile("Wybierz ścieżkę stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\Projekt\\Modele Matlab");
        String modelPath = loadFile("Wybierz ścieżkę stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        complexStochasticModelPath.setText(modelPath);
    }

    @FXML
    public void chooseComplexStochasticParametersPathAction() {
        //String parametersPath = loadFile("Wybierz ścieżkę parametrów stochastycznego modelu złożonego", MATLAB_EXT);
        //String parametersPath = loadFile("Wybierz ścieżkę parametrów stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\Projekt\\Modele Matlab");
        String parametersPath = loadFile("Wybierz ścieżkę parametrów stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        complexStochasticParametersPath.setText(parametersPath);
    }

    /**
     * Wczytanie pliku poprzez utworzenie okna dialogowego do wyboru pliku.
     *
     * @param title Tytuł dialogu.
     * @param extension Możliwe do wyboru rozszerzenia.
     * @return Ścieżka do wybranego pliku.
     */
    public String loadFile(String title, String extension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        ExtensionFilter filter = new ExtensionFilter("Matlab file", extension);
        fileChooser.getExtensionFilters().add(filter);

        File resultFile = fileChooser.showOpenDialog(dialog.getOwner());
        String path = Const.EMPTY;
        if (resultFile != null) {
            path = resultFile.getAbsolutePath();
        }
        return path;
    }

    public String loadFile(String title, String extension, String initialPath) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(initialPath));

        ExtensionFilter filter = new ExtensionFilter("Matlab file", extension);
        fileChooser.getExtensionFilters().add(filter);

        File resultFile = fileChooser.showOpenDialog(dialog.getOwner());
        String path = Const.EMPTY;
        if (resultFile != null) {
            path = resultFile.getAbsolutePath();
        }
        return path;
    }

    @FXML
    private void simpleModelCheckAction() {
        complexModelCheck.setSelected(!simpleModelCheck.selectedProperty().get());
        complexDeterministicModelPath.setText(Const.EMPTY);
        complexDeterministicParametersPath.setText(Const.EMPTY);
        complexStochasticModelPath.setText(Const.EMPTY);
        complexStochasticParametersPath.setText(Const.EMPTY);
    }

    @FXML
    private void complexModelCheckAction() {
        simpleModelCheck.setSelected(!complexModelCheck.selectedProperty().get());
        simpleModelPath.setText(Const.EMPTY);
        simpleParametersPath.setText(Const.EMPTY);
    }

    public void setController(LoadMatlabFileDialogController controller) {
        this.controller = controller;
    }

    public LoadMatlabFileDialogController getController() {
        return controller;
    }

}
