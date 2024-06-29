/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

/**
 * FXML Controller class
 *
 * @author JORGE
 */
public class NuevaCategoriaController implements Initializable {

    @FXML
    private Label textNombre;
    @FXML
    private Label textDescripcion;
    @FXML
    private TextField nombre;
    @FXML
    private TextArea descripcion;
    @FXML
    private Button btAñadir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void añadir(ActionEvent event) throws AcountDAOException, IOException {
        Boolean isOk = true;
        if ("".equals(nombre.getText())) {
            isOk = false;
            textNombre.setStyle("-fx-text-fill: red;");
        } else {
            textNombre.setStyle("-fx-text-fill: white;");
        }
        if ("".equals(descripcion.getText())) {
            isOk = false;
            textDescripcion.setStyle("-fx-text-fill: red;");
        } else {
            textDescripcion.setStyle("-fx-text-fill: white;");
        }

        if (isOk) {
            if (Acount.getInstance().registerCategory(nombre.getText(), descripcion.getText())) {
                Stage window = (Stage) btAñadir.getScene().getWindow();
                window.close();
            }
        }
    }
    
}
