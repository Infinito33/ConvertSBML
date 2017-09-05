/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package convertsbml.basic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author tomasz.huchro
 */
public class FXResourceManager {

    public Node loadFxml(String absolutePath) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(absolutePath));
            return node;
        } catch (IOException ex) {
            Logger.getLogger(FXResourceManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
