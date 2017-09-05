package convertsbml.controller;

import convertsbml.model.ApplicationModel;
import convertsbml.view.ApplicationView;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Klasa odpowiedzialna za przedstawienie widoku na ekranie, czyli jego
 * inicjalizację oraz wyświetlenie.
 *
 * @author Magda
 */
public class ApplicationController {

    private Stage stage;
    private Parent root;
    private Scene scene;

    private ApplicationView view;
    private ApplicationModel model;

    public ApplicationController(Stage stage) {
        this.stage = stage;
        model = new ApplicationModel();
        init();
    }

    /**
     * Konstruktor odpowiedzialny za inicjalizację widoku - ładuje UI z pliku
     * *.fxml oraz tworzy scenę i przypisuje ją do głównego kontenera.
     */
    public void init() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/convertsbml/view/fxml/ApplicationView.fxml"));
            root = loader.load();
            scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/convertsbml/view/css/styles.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("Convert SBML");
            view = loader.getController();
            view.setController(this);
            view.postInitialize();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Wyświetla okno na ekranie.
     */
    public void showWindow() {
        stage.show();
    }

    public ApplicationModel getModel() {
        return model;
    }

    public ApplicationView getView() {
        return view;
    }

}
