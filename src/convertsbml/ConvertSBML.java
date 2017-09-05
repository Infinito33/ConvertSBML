package convertsbml;

import convertsbml.controller.ApplicationController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Główna klasa uruchamiająca aplikację, ładuje widok UI z pliku *.fxml i
 * wyświetla go.
 *
 * @author Magda
 */
public class ConvertSBML extends Application {

    @Override
    public void start(Stage stage) throws Exception {        
        ApplicationController controller = new ApplicationController(stage);
        controller.showWindow();
    }

    /**
     * @param args argumenty linii poleceń.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
