package convertsbml.view.dialog;

import convertsbml.Const;
import convertsbml.controller.LoadMatlabFileDialogController;
import convertsbml.model.dialog.LoadMatlabFileDialogModel;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
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

    private Button okButton;
    private ButtonType applyBtn;

    /**
     * Inicjalizacja podstawowych elementów okna i ich utworzenie.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dialog = new Dialog();
        applyBtn = new ButtonType("Zatwierdź", ButtonData.APPLY);
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
        createActions();

        okButton = (Button) dialog.getDialogPane().lookupButton(applyBtn);
        okButton.setDisable(Boolean.TRUE);
    }

    /**
     * Utworzenie połączeń obustronnych pomiędzy danymi z modelu a komponentami
     * z widoku.
     */
    private void createBindings() {
        //Połącz zmienną isSimpleModel z modelu z wartością checkboxa simpleModelCheck z widoku
        controller.getModel().getIsSimpleModel().bind(simpleModelCheck.selectedProperty());
        controller.getModel().getIsComplexModel().bind(complexModelCheck.selectedProperty());
        controller.getModel().getSimpleModelPath().bind(simpleModelPath.textProperty());
        //Połącz zmienną simpleParametersPath z modelu z wartością pola tekstowego simpleParametersPath z widoku.
        controller.getModel().getSimpleParametersPath().bind(simpleParametersPath.textProperty());
        controller.getModel().getComplexStochasticModelPath().bind(complexStochasticModelPath.textProperty());
        controller.getModel().getComplexStochasticParametersPath().bind(complexStochasticParametersPath.textProperty());
        controller.getModel().getComplexDeterministicModelPath().bind(complexDeterministicModelPath.textProperty());
        controller.getModel().getComplexDeterministicParametersPath().bind(complexDeterministicParametersPath.textProperty());

        //Bindowanie pola tekstowego ze ścieżką - simpleModelPath tak, aby zostało wyłączone w momencie gdy checkbox complexModelCheck zostanie włączony - wzajemna relacja wyłączania się.
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

    /**
     * Tworzy akcje związane z polami tekstowymi w dialogu w celu walidacji.
     */
    private void createActions() {
        simpleModelPath.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(simpleModelCheck.isSelected() && !validateSimplePaths(newValue, model.getSimpleParametersPath().get()));
        });

        simpleParametersPath.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(simpleModelCheck.isSelected() && !validateSimplePaths(newValue, model.getSimpleModelPath().get()));
        });

        complexDeterministicModelPath.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(complexModelCheck.isSelected() && !validateComplexPath(newValue, model.getComplexDeterministicModelPath().get(), model.getComplexStochasticModelPath().get(), model.getComplexStochasticParametersPath().get()));
        });

        complexDeterministicParametersPath.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(complexModelCheck.isSelected() && !validateComplexPath(newValue, model.getComplexDeterministicParametersPath().get(), model.getComplexStochasticModelPath().get(), model.getComplexStochasticParametersPath().get()));
        });

        complexStochasticModelPath.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(complexModelCheck.isSelected() && !validateComplexPath(newValue, model.getComplexDeterministicModelPath().get(), model.getComplexDeterministicParametersPath().get(), model.getComplexStochasticParametersPath().get()));
        });

        complexStochasticParametersPath.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(complexModelCheck.isSelected() && !validateComplexPath(newValue, model.getComplexDeterministicModelPath().get(), model.getComplexDeterministicParametersPath().get(), model.getComplexStochasticModelPath().get()));
        });
    }

    /**
     * Walidacja ścieżki do modelu lub parametru.
     *
     * @param path ścieżka aktualnie wpisywana.
     * @param nextPath kolejna ścieżka.
     * @return True, jeśli wszystkie wyamgane ścieżki są wypełnione.
     */
    private Boolean validateSimplePaths(String path, String nextPath) {
        if (path.isEmpty() || nextPath.isEmpty()) {
            return Boolean.FALSE;
        } else {
            File file = new File(path);
            File nextFile = new File(nextPath);
            if (file.exists() && nextFile.exists()) {
                return Boolean.TRUE;
            } else {
                return Boolean.FALSE;
            }
        }
    }

    /**
     * Walidacja ścieżek do modeli oraz parametrów złożonego modelu matlab.
     *
     * @param path ścieżka aktualnie wpisywana.
     * @param nextPaths kolejne ścieżki.
     * @return True, jeśli wszystkie wymagane ścieżki są wypełnione.
     */
    private Boolean validateComplexPath(String path, String... nextPaths) {
        if (path.isEmpty()) {
            return Boolean.FALSE;
        }
        File file = new File(path);
        if (!file.exists()) {
            return Boolean.FALSE;
        }
        for (String nextPath : nextPaths) {
            if (nextPath.isEmpty()) {
                return Boolean.FALSE;
            }
            file = new File(nextPath);
            if (!file.exists()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Pokazuje dialog.
     *
     * @return Zwraca True, jeśli wciśnięto przycisk OK, w innym przypadku
     * False.
     */
    public Boolean showDialog() {
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonType.APPLY.getButtonData()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * Wybiera ścieżkę dla prostego modelu.
     */
    @FXML
    public void chooseSimpleModelPathAction() {
        //File modelPath = loadFile("Wybierz ścieżkę modelu prostego", MATLAB_EXT);
        //File modelPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\ConvertSBML\\Modele_Matlab");
        File modelPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab");
        if (modelPath != null) {
            simpleModelPath.setText(modelPath.getAbsolutePath());
            model.setName(modelPath.getName());
        }
    }

    /**
     * Wybiera ścieżkę dla parametrów modelu prostego.
     */
    @FXML
    public void chooseSimpleParameterPathAction() {
        //File parametersPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT);
        //File parametersPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\ConvertSBML\\Modele_Matlab");
        File parametersPath = loadFile("Wybierz ścieżkę parametrów modelu prostego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab");
        if (parametersPath != null) {
            simpleParametersPath.setText(parametersPath.getAbsolutePath());
        }
    }

    /**
     * Wybiera ścieżkę dla złożonego modelu deterministycznego.
     */
    @FXML
    public void chooseComplexDeterministicModelPathAction() {
        //File modelPath = loadFile("Wybierz ścieżkę deterministycznego modelu złożonego", MATLAB_EXT);
        //File modelPath = loadFile("Wybierz ścieżkę deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\ConvertSBML\\Modele_Matlab");
        File modelPath = loadFile("Wybierz ścieżkę deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        if (modelPath != null) {
            complexDeterministicModelPath.setText(modelPath.getAbsolutePath());
            model.setName(modelPath.getName());
        }
    }

    /**
     * Wybiera ścieżkę dla parametrów złożonego modelu deterministycznego.
     */
    @FXML
    public void chooseComplexDeterministicParametersPathAction() {
        //File parametersPath = loadFile("Wybierz ścieżkę parametrów deterministycznego modelu złożonego", MATLAB_EXT);
        //File parametersPath = loadFile("Wybierz ścieżkę parametrów deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\ConvertSBML\\Modele_Matlab");
        File parametersPath = loadFile("Wybierz ścieżkę parametrów deterministycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        if (parametersPath != null) {
            complexDeterministicParametersPath.setText(parametersPath.getAbsolutePath());
        }
    }

    /**
     * Wybiera ścieżkę dla złożonego modelu stochastycznego.
     */
    @FXML
    public void chooseComplexStochasticModelPathAction() {
        //File modelPath = loadFile("Wybierz ścieżkę stochastycznego modelu złożonego", MATLAB_EXT);
        //File modelPath = loadFile("Wybierz ścieżkę stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\ConvertSBML\\Modele_Matlab");
        File modelPath = loadFile("Wybierz ścieżkę stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        if (modelPath != null) {
            complexStochasticModelPath.setText(modelPath.getAbsolutePath());
        }
    }

    /**
     * Wybiera ścieżkę dla parametrów złożonego modelu stochastycznego.
     */
    @FXML
    public void chooseComplexStochasticParametersPathAction() {
        //File parametersPath = loadFile("Wybierz ścieżkę parametrów stochastycznego modelu złożonego", MATLAB_EXT);
        //File parametersPath = loadFile("Wybierz ścieżkę parametrów stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomasz.huchro\\Desktop\\ConvertSBML\\Modele_Matlab");
        File parametersPath = loadFile("Wybierz ścieżkę parametrów stochastycznego modelu złożonego", MATLAB_EXT, "C:\\Users\\tomol_000\\Desktop\\ConvertSBML\\Modele_Matlab\\JTB");
        if (parametersPath != null) {
            complexStochasticParametersPath.setText(parametersPath.getAbsolutePath());
        }
    }

    /**
     * Wczytanie pliku poprzez utworzenie okna dialogowego do wyboru pliku.
     *
     * @param title Tytuł dialogu.
     * @param extension Możliwe do wyboru rozszerzenia.
     * @return Ścieżka do wybranego pliku.
     */
    public File loadFile(String title, String extension) {
        //Użycie istniejącej już funkcji o tej samej nazwie, lecz z innymi parametrami. Ścieżka początkowa jest tu niepotrzebna więc jest pustym Stringiem - "".
        return loadFile(title, extension, "");
    }

    /**
     * Wczytanie pliku poprzez utworzenie okna dialogowego do wyboru pliku.
     *
     * @param title Tytuł dialogu.
     * @param extension Możliwe do wyboru rozszerzenia.
     * @param initialPath Początkowa ścieżka, do której przejdzie dialog po
     * starcie.
     * @return Ścieżka do wybranego pliku.
     */
    public File loadFile(String title, String extension, String initialPath) {
        //Utworzenie obiektu do wyboru pliku
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        if (!initialPath.equals("")) {
            //Ustawienie początkowego miejsca
            fileChooser.setInitialDirectory(new File(initialPath));
        }

        //Dodanie filtra, tak aby przyjmował tylko pliki o roszerzeniu *.m
        ExtensionFilter filter = new ExtensionFilter("Matlab file", extension);
        fileChooser.getExtensionFilters().add(filter);

        //Otwarcie dialogu i pobranie wybranie pliku
        File resultFile = fileChooser.showOpenDialog(dialog.getOwner());

        return resultFile;
    }

    /**
     * Akcja na naciśnięcie checkboxa dotyczącego prostego modelu Matlab.
     */
    @FXML
    private void simpleModelCheckAction() {
        //Odznaczenie checkboxa dla złożonego modelu i usunięcie tekstów.
        complexModelCheck.setSelected(!simpleModelCheck.selectedProperty().get());
        complexDeterministicModelPath.setText(Const.EMPTY);
        complexDeterministicParametersPath.setText(Const.EMPTY);
        complexStochasticModelPath.setText(Const.EMPTY);
        complexStochasticParametersPath.setText(Const.EMPTY);
    }

    /**
     * Akcja na naciśnięcie checkboxa dotyczącego złożonego modelu Matlab.
     */
    @FXML
    private void complexModelCheckAction() {
        //Odznaczenie checkboxa dla prostego modelu i usunięcie tekstów.
        simpleModelCheck.setSelected(!complexModelCheck.selectedProperty().get());
        simpleModelPath.setText(Const.EMPTY);
        simpleParametersPath.setText(Const.EMPTY);
    }

    /**
     * Ustawienie kontrolera dla aktualnego widoku.
     *
     * @param controller kontroler.
     */
    public void setController(LoadMatlabFileDialogController controller) {
        this.controller = controller;
    }

    /**
     * Pobranie kontrolera aktualnego widoku.
     *
     * @return kontroler widoku.
     */
    public LoadMatlabFileDialogController getController() {
        return controller;
    }

}
