package convertsbml.controller;

import convertsbml.view.ApplicationView;
import convertsbml.model.dialog.LoadMatlabFileDialogModel;
import convertsbml.view.dialog.LoadMatlabFileDialog;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Magda
 */
public class LoadMatlabFileDialogController {

    private LoadMatlabFileDialog view;
    private LoadMatlabFileDialogModel model;

    public LoadMatlabFileDialogController() {
        model = new LoadMatlabFileDialogModel();
        init();
    }

    public LoadMatlabFileDialogModel getModel() {
        return model;
    }

    public LoadMatlabFileDialog getView() {
        return view;
    }

    private void init() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/convertsbml/view/dialog/fxml/LoadMatlabFileDialog.fxml"));
            GridPane pane = (GridPane) loader.load();
            view = loader.getController();
            view.setController(this);
            view.postInitialize(pane);            
        } catch (IOException ex) {
            Logger.getLogger(ApplicationView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
