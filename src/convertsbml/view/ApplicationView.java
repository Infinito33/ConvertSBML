package convertsbml.view;

import convertsbml.basic.FXResourceManager;
import convertsbml.controller.LoadMatlabFileDialogController;
import convertsbml.matlab.MatlabReader;
import convertsbml.model.ApplicationModel;
import convertsbml.model.dialog.LoadMatlabFileDialogModel;
import convertsbml.model.entities.matlab.ModelMatlab;
import convertsbml.model.entities.slv.ModelSlv;
import convertsbml.sbml.SBMLCreator;
import convertsbml.slv.SlvReader;
import convertsbml.controller.ApplicationController;
import convertsbml.model.entities.matlab.ComplexMatlabData;
import convertsbml.model.entities.matlab.SimpleMatlabData;
import convertsbml.model.enums.EComplexityMatlabModel;
import convertsbml.view.dialog.LoadMatlabFileDialog;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Główny kontroler aplikacji. Posiada wszystkie akcje dostępne w głównym oknie
 * aplikacji.
 *
 * @author Magda
 */
public class ApplicationView implements Initializable {

    private ApplicationModel appModel;
    private ApplicationController controller;
    private FXResourceManager resourceManager;
    private SBMLCreator sbmlCreator;

    private SlvReader slvReader;
    private MatlabReader matlabReader;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox modelsBox;
    @FXML
    private GridPane contentPane;

    @FXML
    private TextArea contentArea;

    /**
     * Utworzenie modelu aplikacji {@link ApplicationModel} oraz załadowanie
     * biblioteki libsbml.
     *
     * @param url URL.
     * @param rb rb.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.loadLibrary("sbmlj");
        slvReader = new SlvReader();
        matlabReader = new MatlabReader();
        resourceManager = new FXResourceManager();
        sbmlCreator = new SBMLCreator();
    }

    public void postInitialize() {
        appModel = controller.getModel();
        createBindings();
    }

    /**
     * Utworzenie bindingów pomiędzy modelem a widokiem.
     */
    private void createBindings() {

    }

    /**
     * Akcja do wczytywania pliku z modelem SLV.
     */
    @FXML
    private void loadSlvModelAction() {
        //Utworzenie obiektu do wybierania plików
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        //Pobranie okna sceny w celu wyświetlenia na nim dialogu do wyboru pliku
        Scene scene = mainPane.getScene();
        Window window = scene.getWindow();

        //Utworzenie obiektu pliku i przypisanie do niego wybranego pliku z okna dialogowego
        File slvFile = fileChooser.showOpenDialog(window);
        //Zapisanie naszego pliku slv do głównego modelu danych.
        if (slvFile != null) {
            appModel.setSlvModelFile(slvFile);

            //Utworzenie obiektu slv reader (do czytania plikow slv)
            //Utworzenie obiektu ModelSlv, ktory bedzie zawieral dane z pliku slv i przypisanie do niego danych po odczytaniu ich z funkcji slvReader.readSlv
            ModelSlv modelSlv = slvReader.readSlv(appModel.getSlvModelFile().getAbsolutePath());
            modelSlv.setSlvFile(slvFile);
            appModel.addSlvModel(modelSlv);

            addSlvModelToMenuList(modelSlv);
        }
    }

    /**
     * Dodanie modelu do listy w menu.
     *
     * @param modelSlv model, który zostanie dodany.
     */
    private void addSlvModelToMenuList(ModelSlv modelSlv) {
        TitledPane modelPane = prepareSlvModelSubMenu(modelSlv);
        modelsBox.getChildren().add(modelPane);
    }

    /**
     * Przygotowanie pod-menu osobno dla kazdego modelu.
     *
     * @param modelSlv model z danymi.
     * @return rozwijany panel z modelem.
     */
    private TitledPane prepareSlvModelSubMenu(ModelSlv modelSlv) {
        //Utworzenie kolejnych przycisków i menu dla modelu.
        Button showContentBtn = new Button("Zawartość");
        showContentBtn.setUserData(this);
        //Akcja która zostanie wywołana w momencie wciśnięcia przycisku.
        showContentBtn.setOnAction((ActionEvent event) -> {
            showModelContentAction(modelSlv.getContent(), modelSlv.getName());
        });

        Button showStatisticsBtn = new Button("Statystyki");
        showStatisticsBtn.setOnAction((ActionEvent event) -> {
            showSlvModelStatisticsAction(modelSlv);
        });
        Button convertToSbmlBtn = new Button("Konwertuj do SBML");
        convertToSbmlBtn.setOnAction((ActionEvent event) -> {
            convertSlvToSbmlAction(modelSlv);
        });

        //Ustawienie stylów i wykości dla przycisków
        showContentBtn.getStyleClass().add("sub-menu-button");
        showStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");
        showContentBtn.setMinHeight(30.0);
        showStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        //Dodanie wszystkich przycisków do jednego komponentu i ustawianie jego właściwości
        VBox innerBox = new VBox(showContentBtn, showStatisticsBtn, convertToSbmlBtn);
        TitledPane modelPane = new TitledPane("SLV: " + modelSlv.getName(), innerBox);
        modelPane.setExpanded(Boolean.FALSE);
        modelPane.getStyleClass().add("menu-titled-pane");
        modelPane.setMaxWidth(Double.MAX_VALUE);
        modelPane.setMinHeight(30.0);
        modelPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(modelPane, Priority.ALWAYS);
        HBox.setHgrow(modelPane, Priority.ALWAYS);

        //Powiązanie minimalnych szerokości przycisków z szerokością całego komponentu rodzica.
        showContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        convertToSbmlBtn.minWidthProperty().bind(modelPane.widthProperty());

        return modelPane;
    }

    /**
     * Wyświetlenie zawartości modelu.
     *
     * @param modelContent zawartość modelu.
     * @param modelName nazwa amodelu.
     */
    private void showModelContentAction(List<String> modelContent, String modelName) {
        //Wyczyszczenie aktualnej zawartości obiektu trzymającego pole tekstowe z modelem.
        contentPane.getChildren().clear();
        //Załadowanie widoku do wyświetlenia zawartości modelu.
        GridPane content = (GridPane) resourceManager.loadFxml("/convertsbml/view/fxml/slv/ContentView.fxml");

        //Pobranie labelki i ustawienie tekstu.
        Label modelNameLbl = (Label) content.getChildren().get(1);
        modelNameLbl.setText(modelName);

        //Pobranie text area i ustawienie tekstu.
        TextArea contentArea = (TextArea) content.getChildren().get(0);
        for (String line : modelContent) {
            contentArea.appendText(line + "\n");
        }
        contentArea.positionCaret(0);
        //Dodanie wszystkiego do głównego komponentu.
        contentPane.getChildren().add(content);
    }

    /**
     * Wypisanie statystyk modelu slv na ekranie.
     *
     * @param modelSlv model slv z danymi.
     */
    private void showSlvModelStatisticsAction(ModelSlv modelSlv) {
        //Wyczyszczenie aktualnej zawartości obiektu trzymającego pole tekstowe z modelem.
        contentPane.getChildren().clear();
        //Załadowanie widoku do wyświetlenia zawartości modelu.        
        GridPane content = (GridPane) resourceManager.loadFxml("/convertsbml/view/fxml/slv/StatisticsSlvView.fxml");

        StatisticsSlvView statView = (StatisticsSlvView) resourceManager.getController();
        statView.setData(modelSlv);
        statView.postInitialize();
        contentPane.getChildren().add(content);
    }

    /**
     * Akcja konwertowania modelu SLV do formatu SBML.
     *
     * @param modelSlv model slv
     */
    public void convertSlvToSbmlAction(ModelSlv modelSlv) {
        sbmlCreator.initialize();
        sbmlCreator.createSBMLFromSlv(modelSlv);
    }

    /**
     * Akcja do wczytywania pliku z modelem Matlab.
     */
    @FXML
    private void loadMatlabModelAction() {
        //Utwórz kontroler, który utworzy widok i załaduje go oraz pokaże dialog na ekranie.
        LoadMatlabFileDialogController controller = new LoadMatlabFileDialogController();
        LoadMatlabFileDialog dialog = controller.getView();
        Boolean dialogResult = dialog.showDialog();
        ModelMatlab matlabModel = null;
        //Jeśli dialog został pokazany i zostały wybrane opcje, pobierz jego dane, ustaw do czytnika matlaba i odczytaj model.
        if (dialogResult) {
            LoadMatlabFileDialogModel model = dialog.getController().getModel();
            matlabReader.setModel(model);
            matlabModel = matlabReader.readModel();
            if (matlabModel != null) {
                //Jeśli model odczytany poprawnie, dodaj do listy modeli
                appModel.addMatlabModel(matlabModel);
                addMatlabModelToMenuList(matlabModel);
            }
        }
    }

    /**
     * Dodanie modelu do listy w menu.
     *
     * @param modelMatlab model, który zostanie dodany.
     */
    private void addMatlabModelToMenuList(ModelMatlab modelMatlab) {
        TitledPane modelPane = prepareMatlabModelSubMenu(modelMatlab);
        modelsBox.getChildren().add(modelPane);
    }

    /**
     * Przygotowanie pod-menu osobno dla kazdego modelu.
     *
     * @param modelName nazwa modelu.
     * @return rozwijany panel z modelem.
     */
    private TitledPane prepareMatlabModelSubMenu(ModelMatlab modelMatlab) {
        if (modelMatlab.getGeneralType() == EComplexityMatlabModel.SIMPLE) {
            return prepareSimpleMatlabModelSubMenu(modelMatlab.getSimpleModel());
        } else {
            return prepareComplexMatlabModelSubMenu(modelMatlab.getStochasticModel(), modelMatlab.getDeterministicModel());
        }
    }

    /**
     * Przygotowanie menu dla prostego modelu matlab.
     *
     * @param simpleModel prosty model matlab.
     * @return Gotowe menu.
     */
    private TitledPane prepareSimpleMatlabModelSubMenu(SimpleMatlabData simpleModel) {
        //Utworzenie kolejnych przycisków i przypisanie im konkretnych akcji (funkcji) dla akcji kliknięcia.
        Button showModelContentBtn = new Button("Zawartość modelu");
        showModelContentBtn.setOnAction((ActionEvent event) -> {
            showModelContentAction(simpleModel.getModelContent(), simpleModel.getModelFile().getName());
        });
        Button showParametersContentBtn = new Button("Zawartość parametrów");
        showParametersContentBtn.setOnAction((ActionEvent) -> {
            showModelContentAction(simpleModel.getParametersContent(), simpleModel.getModelFile().getName());
        });
        Button showStatisticsBtn = new Button("Statystyki");
        showStatisticsBtn.setOnAction((ActionEvent event) -> {
            showSimpleMatlabModelStatisticsAction(simpleModel);
        });
        Button convertToSbmlBtn = new Button("Konwertuj do SBML");
        convertToSbmlBtn.setOnAction((ActionEvent event) -> {
            convertSimpleMatlabToSbmlAction(simpleModel);
        });

        //Ustawienei stylów i wysokości dla przycisków
        showModelContentBtn.getStyleClass().add("sub-menu-button");
        showParametersContentBtn.getStyleClass().add("sub-menu-button");
        showStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");
        showModelContentBtn.setMinHeight(30.0);
        showParametersContentBtn.setMinHeight(30.0);
        showStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        //Dodanie przycisków do wspólnego komponentu, ustawienie dodatkowych stylów i właściwości
        VBox innerBox = new VBox(showModelContentBtn, showParametersContentBtn, showStatisticsBtn, convertToSbmlBtn);
        TitledPane modelPane = new TitledPane("Matlab: " + simpleModel.getModelFile().getName(), innerBox);
        modelPane.setExpanded(Boolean.FALSE);
        modelPane.getStyleClass().add("menu-titled-pane");
        modelPane.setMaxWidth(Double.MAX_VALUE);
        modelPane.setMinHeight(30.0);
        modelPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(modelPane, Priority.ALWAYS);
        HBox.setHgrow(modelPane, Priority.ALWAYS);

        //Powiązanie szerokości ogólnego komponentu z poszczególnymi przyciskami.
        showModelContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showParametersContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        convertToSbmlBtn.minWidthProperty().bind(modelPane.widthProperty());

        return modelPane;
    }

    /**
     * Akcja do pokazania statystyk prostego modelu matlab.
     *
     * @param simpleModel model.
     */
    private void showSimpleMatlabModelStatisticsAction(SimpleMatlabData simpleModel) {
        //Wyczyszczenie aktualnej zawartości obiektu trzymającego pole tekstowe z modelem.
        contentPane.getChildren().clear();
        //Załadowanie widoku do wyświetlenia zawartości modelu.        
        GridPane content = (GridPane) resourceManager.loadFxml("/convertsbml/view/fxml/matlab/StatisticsMatlabView.fxml");

        //Pobranie obiektu widoku
        StatisticsMatlabView statView = (StatisticsMatlabView) resourceManager.getController();
        statView.setData(simpleModel);
        statView.postInitialize();
        //Dodanie do głównego kontenera powyższego widoku
        contentPane.getChildren().add(content);
    }

    /**
     * Akcja konwertowania modelu prostego Matlab do formatu SBML.
     *
     * @param simpleModel prosty model matlab.
     */
    private void convertSimpleMatlabToSbmlAction(SimpleMatlabData simpleModel) {
        sbmlCreator.initialize();
        //Utworzenie modelu w postaci SBML.
        sbmlCreator.createSBMLFromSimpleMatlab(simpleModel);
    }

    /**
     * Przygotowanie podmenu dla złożonego modelu matlab.
     *
     * @param stochasticModel model stochastyczny.
     * @param deterministicModel model determinstyczny.
     * @return gotowy komponent z menu.
     */
    private TitledPane prepareComplexMatlabModelSubMenu(ComplexMatlabData stochasticModel, ComplexMatlabData deterministicModel) {
        Button showStochModelContentBtn = new Button("Model stochastyczny");
        showStochModelContentBtn.setOnAction((ActionEvent event) -> {
            showModelContentAction(stochasticModel.getModelContent(), stochasticModel.getModelFile().getName());
        });
        Button showDetermModelContentBtn = new Button("Model deterministyczny");
        showDetermModelContentBtn.setOnAction((ActionEvent event) -> {
            showModelContentAction(deterministicModel.getModelContent(), stochasticModel.getModelFile().getName());
        });

        Button showStochasticParamBtn = new Button("Parametry s.");
        showStochasticParamBtn.setOnAction((ActionEvent) -> {
            showModelContentAction(stochasticModel.getParametersContent(), stochasticModel.getModelFile().getName());
        });
        Button showDetermParamBtn = new Button("Parametry d.");
        showDetermParamBtn.setOnAction((ActionEvent) -> {
            showModelContentAction(deterministicModel.getParametersContent(), stochasticModel.getModelFile().getName());
        });

        Button showComplexStatisticsBtn = new Button("Statystyki");
        showComplexStatisticsBtn.setOnAction((ActionEvent event) -> {
            showComplexMatlabModelStatAction(stochasticModel, deterministicModel);
        });

        Button convertToSbmlBtn = new Button("Konwertuj do SBML");
        convertToSbmlBtn.setOnAction((ActionEvent event) -> {
            convertComplexMatlabModelToSbmlAction(stochasticModel, deterministicModel);
        });

        showStochModelContentBtn.getStyleClass().add("sub-menu-button");
        showDetermModelContentBtn.getStyleClass().add("sub-menu-button");
        showStochasticParamBtn.getStyleClass().add("sub-menu-button");
        showDetermParamBtn.getStyleClass().add("sub-menu-button");
        showComplexStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");

        showStochModelContentBtn.setMinHeight(30.0);
        showDetermModelContentBtn.setMinHeight(30.0);
        showStochasticParamBtn.setMinHeight(30.0);
        showDetermParamBtn.setMinHeight(30.0);
        showComplexStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        VBox innerBox = new VBox(showStochModelContentBtn, showDetermModelContentBtn, showStochasticParamBtn, showDetermParamBtn, showComplexStatisticsBtn, convertToSbmlBtn);
        TitledPane modelPane = new TitledPane("Matlab: " + stochasticModel.getModelFile().getName(), innerBox);
        modelPane.setExpanded(Boolean.FALSE);
        modelPane.getStyleClass().add("menu-titled-pane");
        modelPane.setMaxWidth(Double.MAX_VALUE);
        modelPane.setMinHeight(30.0);
        modelPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(modelPane, Priority.ALWAYS);
        HBox.setHgrow(modelPane, Priority.ALWAYS);

        showStochModelContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showDetermModelContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showStochasticParamBtn.minWidthProperty().bind(modelPane.widthProperty());
        showDetermParamBtn.minWidthProperty().bind(modelPane.widthProperty());
        showComplexStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        convertToSbmlBtn.minWidthProperty().bind(modelPane.widthProperty());

        return modelPane;
    }

    /**
     * Akcja do pokazania statystyk złożonego modelu matlab.
     *
     * @param simpleModel model.
     */
    private void showComplexMatlabModelStatAction(ComplexMatlabData stochasticModel, ComplexMatlabData deterministicModel) {
        //Wyczyszczenie aktualnej zawartości obiektu trzymającego pole tekstowe z modelem.
        contentPane.getChildren().clear();
        //Załadowanie widoku do wyświetlenia zawartości modelu.        
        GridPane content = (GridPane) resourceManager.loadFxml("/convertsbml/view/fxml/matlab/StatisticsMatlabView.fxml");

        //Pobranie obiektu widoku
        StatisticsMatlabView statView = (StatisticsMatlabView) resourceManager.getController();
        statView.setData(stochasticModel, deterministicModel);
        statView.postInitialize();
        //Dodanie do głównego kontenera powyższego widoku
        contentPane.getChildren().add(content);
    }

    /**
     * Akcja konwertowania modelu złożonego Matlab do formatu SBML.
     *
     * @param stochModel model stochastyczny.
     * @param determModel model deterministyczny.
     */
    private void convertComplexMatlabModelToSbmlAction(ComplexMatlabData stochModel, ComplexMatlabData determModel) {
        SBMLCreator sbmlCreator = new SBMLCreator();
        //Utworzenie pliku z modelem SBML.
        sbmlCreator.createSBMLFromComplexMatlab(stochModel, determModel);
    }

    /**
     * Akcja zamknięcia aplikacji
     *
     * @param event obiekt eventu zamknięcia.
     */
    @FXML
    private void closeAppAction(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Ustawienie kontrolera dla widoku.
     *
     * @param controller kontroler.
     */
    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
