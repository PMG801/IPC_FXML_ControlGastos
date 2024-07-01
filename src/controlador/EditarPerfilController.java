/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class EditarPerfilController implements Initializable {

    @FXML
    private Label textNombre;
    @FXML
    private Label textApellidos;
    @FXML
    private TextField nombre;
    @FXML
    private TextField apellidos;
    @FXML
    private TextField correo;
    @FXML
    private Label textCorreo;
    @FXML
    private Label textContraseña;
    @FXML
    private Label textContraseña2;
    @FXML
    private Label textObligado;
    @FXML
    private Button btatras;
    @FXML
    private PasswordField contraseña;
    @FXML
    private PasswordField contraseña2;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ImageView perfil;
    @FXML
    private Label usuario;
    @FXML
    private Button btEditar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            usuario.setText(Acount.getInstance().getLoggedUser().getNickName());
            nombre.setText(Acount.getInstance().getLoggedUser().getName());
            apellidos.setText(Acount.getInstance().getLoggedUser().getSurname());
            correo.setText(Acount.getInstance().getLoggedUser().getEmail());
            perfil.setImage(Acount.getInstance().getLoggedUser().getImage());
            contraseña.setText(Acount.getInstance().getLoggedUser().getPassword());
            contraseña2.setText(Acount.getInstance().getLoggedUser().getPassword());
            
            comboBox.getItems().addAll("/resources/images/perfil1.png","/resources/images/perfil2.png","/resources/images/perfil3.png","/resources/images/perfil4.png","/resources/images/perfil5.png","/resources/images/perfil6.png","/resources/images/perfil7.png");
            comboBox.setCellFactory((c) -> {return new EditarPerfilController.ImageTabCell();} );

            // Listener para cambiar la imagen cuando se selecciona un elemento del ComboBox
            comboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue != null) {
                    Image image = new Image(getClass().getResourceAsStream(newValue));
                    perfil.setImage(image);
                } 
            });
        } catch (AcountDAOException ex) {
            Logger.getLogger(EditarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditarPerfilController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void atras(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Main.fxml"));
        Scene loginScene = new Scene(loginParent);
        
        // Obtener la ventana actual
        Stage window = (Stage) btatras.getScene().getWindow();
        
        // Establecer la nueva escena
        window.setScene(loginScene);
        window.setResizable(true);
        window.show();
    }

    @FXML
    private void editar(ActionEvent event) throws AcountDAOException {
        try {
            LocalDate currentDate = LocalDate.now();
            Object propertyValue = perfil.imageProperty().get();
            Image image = (Image) propertyValue;
                       
            if (verificarRegistro()) {               
                Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Editar perfil");
                alerta.setHeaderText("¿Estás seguro de realizar los cambios?");
                
                ButtonType buttonTypeSi = new ButtonType("SI");
                ButtonType buttonTypeNo = new ButtonType("NO");
                
                alerta.getButtonTypes().setAll(buttonTypeSi,buttonTypeNo);
                
                ImageView imageView = new ImageView(new Image("/resources/images/warning.png"));
                        // Establecer el tamaño deseado para la imagen
                imageView.setFitWidth(85);
                imageView.setFitHeight(80);
                alerta.setGraphic(imageView);

                Stage stage = (Stage) alerta.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/icono.png")));
                
                Optional<ButtonType> result = alerta.showAndWait();
                if(result.isPresent()){
                    if(result.get() == buttonTypeSi){
                        User user = Acount.getInstance().getLoggedUser();
                        user.setName(nombre.getText());
                        user.setSurname(apellidos.getText());
                        user.setPassword(contraseña.getText());
                        user.setEmail(correo.getText());
                        user.setImage(image);
                        
                        Parent mainParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Main.fxml"));
                        Scene mainScene = new Scene(mainParent);

                        // Obtener la ventana actual
                        Stage window = (Stage) btEditar.getScene().getWindow();

                        // Establecer la nueva escena
                        window.setScene(mainScene);
                        window.setResizable(true);
                        window.show();
                    }
                    else if(result.get() == buttonTypeNo){
                        alerta.close();
                    }
                }
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
                 
        if("".equals(nombre.getText())) {isOk = false; textNombre.setStyle("-fx-text-fill: red;");}
        else textNombre.setStyle("-fx-text-fill: white;");

        if("".equals(apellidos.getText())) {isOk = false; textApellidos.setStyle("-fx-text-fill: red;");}
        else textApellidos.setStyle("-fx-text-fill: white;");

        if("".equals(correo.getText()) || !checkEmail(correo.getText())) {isOk = false; textCorreo.setStyle("-fx-text-fill: red;");}
        else textCorreo.setStyle("-fx-text-fill: white;");

        if("".equals(contraseña.getText()) || !checkPassword(contraseña.getText())) {isOk = false; textContraseña.setStyle("-fx-text-fill: red;");}
        else textContraseña.setStyle("-fx-text-fill: white;");

        if("".equals(contraseña2.getText()) || !contraseña.getText().equals(contraseña2.getText())) {isOk = false; textContraseña2.setStyle("-fx-text-fill: red;");}
        else textContraseña2.setStyle("-fx-text-fill: white;");

        return isOk;
    }
}


