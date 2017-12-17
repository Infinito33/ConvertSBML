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
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

    private SlvReader slvReader;

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
        resourceManager = new FXResourceManager();
    }

    public void postInitialize() {
        appModel = controller.getModel();
        createBindings();
    }

    private void createBindings() {

    }

    @FXML
    private void loadSlvModelAction() {
        //Utworzenie obiektu do wybierania plików
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //fileChooser.setInitialDirectory(new File("D:\\Programy\\Dropbox\\MY\\Projekt"));
        //fileChooser.setInitialDirectory(new File("C:\\Users\\tomasz.huchro\\Desktop\\Projekt"));

        //Pobranie okna sceny w celu wyświetlenia na nim dialogu do wyboru pliku
        Scene scene = mainPane.getScene();
        Window window = scene.getWindow();

        //Utworzenie obiektu pliku i przypisanie do niego wybranego pliku z okna dialogowego
        File slvFile = fileChooser.showOpenDialog(window);
        //Zapisanie naszego pliku slv do głównego modelu danych.
        appModel.setSlvModelFile(slvFile);

        //Utworzenie obiektu slv reader (do czytania plikow slv)
        //Utworzenie obiektu ModelSlv, ktory bedzie zawieral dane z pliku slv i przypisanie do niego danych po odczytaniu ich z funkcji slvReader.readSlv
        ModelSlv modelSlv = slvReader.readSlv(appModel.getSlvModelFile().getAbsolutePath());
        modelSlv.setSlvFile(slvFile);
        appModel.addSlvModel(modelSlv);

        addSlvModelToMenuList(modelSlv);
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
        Button showContentBtn = new Button("Zawartość");
        showContentBtn.setUserData(this);
        showContentBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showModelContentAction(modelSlv.getContent(), modelSlv.getName());
            }
        });
        Button showStatisticsBtn = new Button("Statystyki");
        showStatisticsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSlvModelStatisticsAction(modelSlv);
            }
        });
        Button convertToSbmlBtn = new Button("Konwertuj do SBML");
        convertToSbmlBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                convertSlvToSbmlAction(modelSlv);
            }
        });
        showContentBtn.getStyleClass().add("sub-menu-button");
        showStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");
        showContentBtn.setMinHeight(30.0);
        showStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        VBox innerBox = new VBox(showContentBtn, showStatisticsBtn, convertToSbmlBtn);
        TitledPane modelPane = new TitledPane("SLV: " + modelSlv.getName(), innerBox);
        modelPane.setExpanded(Boolean.FALSE);
        modelPane.getStyleClass().add("menu-titled-pane");
        modelPane.setMaxWidth(Double.MAX_VALUE);
        modelPane.setMinHeight(30.0);
        modelPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(modelPane, Priority.ALWAYS);
        HBox.setHgrow(modelPane, Priority.ALWAYS);

        showContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        convertToSbmlBtn.minWidthProperty().bind(modelPane.widthProperty());

        return modelPane;
    }

    private void showModelContentAction(List<String> modelContent, String modelName) {
        contentPane.getChildren().clear();
        GridPane content = (GridPane) resourceManager.loadFxml("/convertsbml/view/fxml/slv/ContentView.fxml");

        Label modelNameLbl = (Label) content.getChildren().get(1);
        modelNameLbl.setText("SLV: " + modelName);

        TextArea contentArea = (TextArea) content.getChildren().get(0);
        for (String line : modelContent) {
            contentArea.appendText(line + "\n");
        }
        contentPane.getChildren().add(content);
    }

    private void showSlvModelStatisticsAction(ModelSlv modelSlv) {
        contentPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/convertsbml/view/fxml/slv/StatisticsSlvView.fxml"));
        GridPane content = null;
        try {
            content = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationView.class.getName()).log(Level.SEVERE, null, ex);
        }
        StatisticsSlvView statView = (StatisticsSlvView) loader.getController();
        statView.setData(modelSlv);
        statView.postInitialize();
        contentPane.getChildren().add(content);
    }

    public void convertSlvToSbmlAction(ModelSlv modelSlv) {
        SBMLCreator sbmlCreator = new SBMLCreator();
        sbmlCreator.createSBMLFromSlv(modelSlv);
    }

    @FXML
    private void loadMatlabModelAction() {
        LoadMatlabFileDialogController controller = new LoadMatlabFileDialogController();
        LoadMatlabFileDialog dialog = controller.getView();
        Boolean dialogResult = dialog.showDialog();
        ModelMatlab matlabModel = null;
        if (dialogResult) {
            LoadMatlabFileDialogModel model = dialog.getController().getModel();
            MatlabReader reader = new MatlabReader(model);

            matlabModel = reader.readModel(model.getIsSimpleModel().get());

        }
        if (matlabModel != null) {
            appModel.addMatlabModel(matlabModel);
            addMatlabModelToMenuList(matlabModel);
        }
    }

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

    private TitledPane prepareSimpleMatlabModelSubMenu(SimpleMatlabData simpleModel) {
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

        showModelContentBtn.getStyleClass().add("sub-menu-button");
        showParametersContentBtn.getStyleClass().add("sub-menu-button");
        showStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");
        showModelContentBtn.setMinHeight(30.0);
        showParametersContentBtn.setMinHeight(30.0);
        showStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        VBox innerBox = new VBox(showModelContentBtn, showParametersContentBtn, showStatisticsBtn, convertToSbmlBtn);
        TitledPane modelPane = new TitledPane("Matlab: " + simpleModel.getModelFile().getName(), innerBox);
        modelPane.setExpanded(Boolean.FALSE);
        modelPane.getStyleClass().add("menu-titled-pane");
        modelPane.setMaxWidth(Double.MAX_VALUE);
        modelPane.setMinHeight(30.0);
        modelPane.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(modelPane, Priority.ALWAYS);
        HBox.setHgrow(modelPane, Priority.ALWAYS);

        showModelContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showParametersContentBtn.minWidthProperty().bind(modelPane.widthProperty());
        showStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        convertToSbmlBtn.minWidthProperty().bind(modelPane.widthProperty());

        return modelPane;
    }

    private void showSimpleMatlabModelStatisticsAction(SimpleMatlabData simpleModel) {

    }

    private void convertSimpleMatlabToSbmlAction(SimpleMatlabData simpleModel) {
        SBMLCreator sbmlCreator = new SBMLCreator();
        sbmlCreator.createSBMLFromSimpleMatlab(simpleModel);
    }

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

        Button showStochasticStatisticsBtn = new Button("Statystyki s.");
        showStochasticStatisticsBtn.setOnAction((ActionEvent event) -> {
            showComplexMatlabModelStatAction(stochasticModel);
        });
        Button showDeterministicStatisticsBtn = new Button("Statystyki d.");
        showDeterministicStatisticsBtn.setOnAction((ActionEvent event) -> {
            showComplexMatlabModelStatAction(deterministicModel);
        });

        Button convertToSbmlBtn = new Button("Konwertuj do SBML");
        convertToSbmlBtn.setOnAction((ActionEvent event) -> {
            convertComplexMatlabModelToSbmlAction(stochasticModel, deterministicModel);
        });

        showStochModelContentBtn.getStyleClass().add("sub-menu-button");
        showDetermModelContentBtn.getStyleClass().add("sub-menu-button");
        showStochasticParamBtn.getStyleClass().add("sub-menu-button");
        showDetermParamBtn.getStyleClass().add("sub-menu-button");
        showStochasticStatisticsBtn.getStyleClass().add("sub-menu-button");
        showDeterministicStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");

        showStochModelContentBtn.setMinHeight(30.0);
        showDetermModelContentBtn.setMinHeight(30.0);
        showStochasticParamBtn.setMinHeight(30.0);
        showDetermParamBtn.setMinHeight(30.0);
        showStochasticStatisticsBtn.setMinHeight(30.0);
        showDeterministicStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        VBox innerBox = new VBox(showStochModelContentBtn, showDetermModelContentBtn, showStochasticParamBtn, showDetermParamBtn, showStochasticStatisticsBtn, showDeterministicStatisticsBtn, convertToSbmlBtn);
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
        showStochasticStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        showDeterministicStatisticsBtn.minWidthProperty().bind(modelPane.widthProperty());
        convertToSbmlBtn.minWidthProperty().bind(modelPane.widthProperty());

        return modelPane;
    }

    private void showComplexMatlabModelStatAction(ComplexMatlabData complexModel) {

    }

    private void convertComplexMatlabModelToSbmlAction(ComplexMatlabData stochModel, ComplexMatlabData determModel) {
        SBMLCreator sbmlCreator = new SBMLCreator();
        sbmlCreator.createSBMLFromComplexMatlab(stochModel, determModel);
    }

    @FXML
    private void closeAppAction(ActionEvent event) {
        System.exit(0);
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
