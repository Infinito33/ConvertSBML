package convertsbml.basic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 * Klasa odpowiedzialna za odczyt elementów związanych z JavaFX.
 *
 * @author Magda
 */
public class FXResourceManager {

    private Object controller;

    /**
     * Załadowanie pliku *.fxml do obiektu, tak aby można było go wyświetlić.
     *
     * @param absolutePath ścieżka absolutna do pliku.
     * @return obiekt z widokiem.
     */
    public Node loadFxml(final String absolutePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutePath));
            Node node = loader.load();
            controller = loader.getController();
            return node;
        } catch (IOException ex) {
            Logger.getLogger(FXResourceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Object getController() {
        return controller;
    }

}
