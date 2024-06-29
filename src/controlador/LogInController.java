/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;

/**
 * FXML Controller class
 *
 * @author JORGE
 */
public class LogInController implements Initializable {

    @FXML
    private HBox hboxCenter;
    @FXML
    private Label Error;
    @FXML
    private Button btnEnter;
    @FXML
    private TextField Usuario;
    @FXML
    private PasswordField contraseña;
    @FXML
    private Hyperlink linkRegistro;
    @FXML
    private Label error;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnEnterOnAction() throws IOException, AcountDAOException {
        try {
            if(Acount.getInstance().logInUserByCredentials(Usuario.getText(), contraseña.getText())){
                Parent mainParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Main.fxml"));
                Scene mainScene = new Scene(mainParent);

                // Obtener la ventana actual
                Stage window = (Stage) btnEnter.getScene().getWindow();

                // Establecer la nueva escena
                window.setScene(mainScene);
                window.setResizable(true);
                window.show();
            }
            else{
                error.setText("Usuario o contraseña incorrecto");
            }
             
        } catch (IOException ex) {
            // Manejar la excepción
            
        }
        
        
        
    }

    @FXML
    private void registerOnAction(ActionEvent event) throws IOException {
        Parent registroParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Registro.fxml"));
        Scene registroScene = new Scene(registroParent);
        
        // Obtener la ventana actual
        Stage window = (Stage) linkRegistro.getScene().getWindow();
        
        // Establecer la nueva escena
        window.setScene(registroScene);
        window.show();
    }

    @FXML
    private void pressBoton(KeyEvent event) throws IOException, AcountDAOException {
        if(event.getCode() == KeyCode.ENTER){
            btnEnterOnAction();
        }            
    }
    
}
