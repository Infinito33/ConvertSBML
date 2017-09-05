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
import convertsbml.view.dialog.LoadMatlabFileDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
    private void loadFileAction() {
        //Utworzenie obiektu do wybierania plików
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        //fileChooser.setInitialDirectory(new File("D:\\Programy\\Dropbox\\MY\\Projekt"));
        fileChooser.setInitialDirectory(new File("C:\\Users\\tomasz.huchro\\Desktop\\Projekt"));

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

    private void addSlvModelToMenuList(ModelSlv modelSlv) {
        TitledPane modelPane = prepareSlvModelSubMenu(modelSlv);
        modelsBox.getChildren().add(modelPane);
    }

    /**
     * Przygotowanie pod-menu osobno dla kazdego modelu.
     *
     * @param modelName nazwa modelu.
     * @return rozwijany panel z modelem.
     */
    private TitledPane prepareSlvModelSubMenu(ModelSlv modelSlv) {
        Button showContentBtn = new Button("Zawartość");
        showContentBtn.setUserData(this);
        showContentBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showSlvModelContentAction(modelSlv);
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
        showContentBtn.getStyleClass().add("sub-menu-button");
        showStatisticsBtn.getStyleClass().add("sub-menu-button");
        convertToSbmlBtn.getStyleClass().add("sub-menu-button");
        showContentBtn.setMinHeight(30.0);
        showStatisticsBtn.setMinHeight(30.0);
        convertToSbmlBtn.setMinHeight(30.0);

        VBox innerBox = new VBox(showContentBtn, showStatisticsBtn, convertToSbmlBtn);
        TitledPane modelPane = new TitledPane(modelSlv.getName(), innerBox);
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

    private void showSlvModelContentAction(ModelSlv modelSlv) {
        contentPane.getChildren().clear();
        GridPane content = (GridPane) resourceManager.loadFxml("/convertsbml/view/fxml/slv/ContentView.fxml");
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/convertsbml/view/fxml/slv/ContentView.fxml"));
        //StatisticsView statView = (StatisticsView) loader.getController();

        TextArea contentArea = (TextArea) content.getChildren().get(0);
        for (String line : modelSlv.getContent()) {
            contentArea.appendText(line + "\n");
        }
        contentPane.getChildren().add(content);
    }

    private void showSlvModelStatisticsAction(ModelSlv modelSlv) {
        contentPane.getChildren().clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/convertsbml/view/fxml/slv/StatisticsView.fxml"));
        GridPane content = null;
        try {
            content = loader.load();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationView.class.getName()).log(Level.SEVERE, null, ex);
        }
        StatisticsView statView = (StatisticsView) loader.getController();
        statView.setData(modelSlv);
        statView.postInitialize();
        contentPane.getChildren().add(content);
    }

    @FXML
    public void testSBML(ActionEvent event) {
        SBMLCreator sbmlCreator = new SBMLCreator();
        sbmlCreator.createSBMLFile(appModel.getSlvModels().get(0));
    }

    @FXML
    private void loadMatlabModelAction() {
        LoadMatlabFileDialogController controller = new LoadMatlabFileDialogController();
        LoadMatlabFileDialog dialog = controller.getView();
        Boolean dialogResult = dialog.showDialog();
        if (dialogResult) {
            LoadMatlabFileDialogModel model = dialog.getController().getModel();
            MatlabReader reader = new MatlabReader(model);
            ModelMatlab matlabModel = null;
            if (model.getIsSimpleModel().get()) {
                matlabModel = reader.readSimpleModel();
            } else {
                matlabModel = reader.readComplexModel();
            }

            appModel.setMatlabModel(matlabModel);
            System.out.println("model done");
        }
    }

    @FXML
    private void closeAppAction(ActionEvent event) {
        System.exit(0);
    }

    public void setController(ApplicationController controller) {
        this.controller = controller;
    }
}
