/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.util.Callback;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.Acount;
import model.AcountDAOException;
import model.User;
import static model.User.checkEmail;
import static model.User.checkNickName;
import static model.User.checkPassword;

/**
 * FXML Controller class
 *
 * @author JORGE
 */
public class RegistroController implements Initializable {

    @FXML
    private TextField nombre;
    @FXML
    private TextField apellidos;
    @FXML
    private TextField usuario;
    @FXML
    private TextField correo;
    @FXML
    private PasswordField contraseña;
    @FXML
    private Button btatras;
    @FXML
    private Button btRegistro;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ImageView perfil;
    @FXML
    private Label textNombre;
    @FXML
    private Label textApellidos;
    @FXML
    private Label textUsuario;
    @FXML
    private Label textCorreo;
    @FXML
    private Label textContraseña;
    @FXML
    private Label textContraseña2;
    @FXML
    private Label textObligado;
    @FXML
    private PasswordField contraseña2;
    @FXML
    private Label error;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboBox.getItems().addAll("/resources/images/perfil1.png","/resources/images/perfil2.png","/resources/images/perfil3.png","/resources/images/perfil4.png","/resources/images/perfil5.png","/resources/images/perfil6.png","/resources/images/perfil7.png");
        comboBox.setCellFactory((c) -> {return new ImageTabCell();} );

        // Establecer la imagen por defecto en el ImageView
        Image defaultImage = new Image(getClass().getResourceAsStream("/avatars/default.png"));
        perfil.setImage(defaultImage);

        // Listener para cambiar la imagen cuando se selecciona un elemento del ComboBox
        comboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null) {
                Image image = new Image(getClass().getResourceAsStream(newValue));
                perfil.setImage(image);
            } else {
                perfil.setImage(defaultImage);
            }
        });
        
    }  

    @FXML
    private void atras(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Login.fxml"));
        Scene loginScene = new Scene(loginParent);
        
        // Obtener la ventana actual
        Stage window = (Stage) btatras.getScene().getWindow();
        
        // Establecer la nueva escena
        window.setScene(loginScene);
        window.show();
        
    }

    @FXML
    private void registro(ActionEvent event) throws IOException, AcountDAOException {
        try {                       
            if (verificarRegistro()) {
                System.out.println("Usuario registrado correctamente.");
                Acount log = Acount.getInstance();
                log.logInUserByCredentials(usuario.getText(), contraseña.getText());
                log.registerCategory("Sin categoría", "Categoía por defecto");
                log.logOutUser();
                
                Parent registroParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/LogIn.fxml"));
                Scene registroScene = new Scene(registroParent);

                // Obtener la ventana actual
                Stage window = (Stage) btRegistro.getScene().getWindow();

                // Establecer la nueva escena
                window.setScene(registroScene);
                window.show();
            } else {
                System.out.println("No se pudo registrar el usuario.");
            }
        } catch (IOException ex) {
            // Manejar la excepción
            
        }
    }
    
    class ImageTabCell extends ListCell<String> {
        private ImageView view = new ImageView();
        private Image imagen;

        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                imagen = new Image(item, 50, 60, true, true);
                view.setImage(imagen);
                setGraphic(view);
                setText(null);
            }
        }
    }
    
    private boolean verificarRegistro(){
        Boolean isOk = true;
        error.setText("");
                 
        if("".equals(nombre.getText())) {isOk = false; textNombre.setStyle("-fx-text-fill: red;");}
        else textNombre.setStyle("-fx-text-fill: white;");

        if("".equals(apellidos.getText())) {isOk = false; textApellidos.setStyle("-fx-text-fill: red;");}
        else textApellidos.setStyle("-fx-text-fill: white;");

        if("".equals(usuario.getText()) || !checkNickName(usuario.getText())) {isOk = false; textUsuario.setStyle("-fx-text-fill: red;");}
        else textUsuario.setStyle("-fx-text-fill: white;");
        
        if("".equals(correo.getText()) || !checkEmail(correo.getText())) {isOk = false; textCorreo.setStyle("-fx-text-fill: red;");}
        else textCorreo.setStyle("-fx-text-fill: white;");

        if("".equals(contraseña.getText()) || !checkPassword(contraseña.getText())) {isOk = false; textContraseña.setStyle("-fx-text-fill: red;");}
        else textContraseña.setStyle("-fx-text-fill: white;");

        if("".equals(contraseña2.getText()) || !contraseña.getText().equals(contraseña2.getText())) {isOk = false; textContraseña2.setStyle("-fx-text-fill: red;");}
        else textContraseña2.setStyle("-fx-text-fill: white;");
        
        LocalDate currentDate = LocalDate.now();
        Object propertyValue = perfil.imageProperty().get();
        Image image = (Image) propertyValue;
        if(isOk){
            try{
                Acount.getInstance().registerUser(nombre.getText(), apellidos.getText(), correo.getText(), usuario.getText(), contraseña.getText(), image, currentDate);
            } catch (Exception e) {
                error.setText("Este usuario ya existe.");
                isOk = false;
            }
        }
        return isOk;
    }
}
