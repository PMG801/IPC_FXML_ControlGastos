/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;
import model.User;

/**
 * FXML Controller class
 *
 * @author JORGE
 */
public class EditarGastoController implements Initializable {

    @FXML
    private Label usuario;
    @FXML
    private Label textNombre;
    @FXML
    private Label textCoste;
    @FXML
    private TextField nombre;
    @FXML
    private Label textDescripcion;
    @FXML
    private Label textUnidades;
    @FXML
    private Label textFecha;
    @FXML
    private Label textObligado;
    @FXML
    private Button btatras;
    @FXML
    private Button btEditar;
    @FXML
    private TextArea descripcion;
    @FXML
    private Spinner<Integer> unidades;
    @FXML
    private DatePicker fecha;
    @FXML
    private ImageView ticket;
    @FXML
    private Button btBuscador;
    @FXML
    private Label textCategoria;
    @FXML
    private ComboBox<Category> categoria;
    @FXML
    private TextField coste;

    protected Charge gasto;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
            coste.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*(\\.\\d{0,2})?")) {
                    coste.setText(oldValue);
                }
            });
        
            SpinnerValueFactory<Integer> valorUnidades = new  SpinnerValueFactory.IntegerSpinnerValueFactory(1, 999);
            unidades.setValueFactory(valorUnidades);
            unidades.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) { // Solo permite caracteres numéricos
                unidades.getEditor().setText(oldValue);
            }
            });
            
            List<Category> listaCategoria = Acount.getInstance().getUserCategories();
            for(int i = 0; i < listaCategoria.size(); i++){
                Category catego = listaCategoria.get(i);
                categoria.getItems().add(catego);
                categoria.setCellFactory((c) -> {return new EditarGastoController.CategoryTabCell();} );
                StringConverter<Category> sc = new StringConverter<Category>() {
                    @Override
                    public String toString(Category category) {
                        return category == null ? null : category.getName();
                    }

                    @Override
                    public Category fromString(String string) {
                        // Aquí puedes implementar la lógica para convertir de String a Category si es necesario,
                        // pero para este caso, no es necesario ya que solo estamos mostrando el nombre de la categoría.
                        return null;
                    }
                };

                
                categoria.setConverter(sc);
            }
        } catch (AcountDAOException ex) {
            Logger.getLogger(EditarGastoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditarGastoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }    
    
    public void valoresDefecto(Charge gasto) throws AcountDAOException, IOException{
        this.gasto = gasto;
        if(gasto != null){
            nombre.setText(gasto.getName());
            descripcion.setText(gasto.getDescription());
            coste.setText(String.valueOf(gasto.getCost()));
            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, gasto.getUnits());
            unidades.setValueFactory(valueFactory);
            fecha.setValue(gasto.getDate());
            categoria.setValue(gasto.getCategory());
            if(gasto.getImageScan() != null){
                ticket.setImage(gasto.getImageScan());
            }
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
    private void editar(ActionEvent event) throws AcountDAOException, IOException {
        Object propertyValue = ticket.imageProperty().get();
        Image image = (Image) propertyValue;        
        if (verificarGasto()) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                alerta.setTitle("Editar gasto");
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
                        gasto.setName(nombre.getText());
                        gasto.setDescription(descripcion.getText());
                        gasto.setCost(Double.valueOf(coste.getText()));
                        gasto.setUnits(unidades.getValue());
                        gasto.setDate(fecha.getValue());
                        gasto.setCategory(categoria.getValue());
                        if(ticket != null){
                            gasto.setImageScan(ticket.getImage());
                        }
                        
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
        } else {
            System.out.println("No se pudo guardar el gasto.");
        }
    }

    @FXML
    private void buscador(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir ticket");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog( 
        ((Node)event.getSource()).getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            ticket.setImage(image);
        } 
    }
    
    private boolean verificarGasto() {
        Boolean isOk = true;
        
        if("".equals(nombre.getText())) {isOk = false; textNombre.setStyle("-fx-text-fill: red;");}
        else textNombre.setStyle("-fx-text-fill: white;");
        
        if(null == coste.getText()) {isOk = false; textCoste.setStyle("-fx-text-fill: red;");}
        else textCoste.setStyle("-fx-text-fill: white;");
        
        if("".equals(descripcion.getText())) {isOk = false; textDescripcion.setStyle("-fx-text-fill: red;");}
        else textDescripcion.setStyle("-fx-text-fill: white;");
        
        if(null == unidades.getValue()) {isOk = false; textUnidades.setStyle("-fx-text-fill: red;");}
        else textUnidades.setStyle("-fx-text-fill: white;");
        
        if(null == fecha.getValue()) {isOk = false; textFecha.setStyle("-fx-text-fill: red;");}
        else textFecha.setStyle("-fx-text-fill: white;");
        
        return isOk;
    }
    
    class CategoryTabCell extends ListCell<Category>{
        private Label label = new Label();

        @Override
        public void updateItem(Category item, boolean empty) {
            super.updateItem(item, empty);

            if (item == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                // Convertir el objeto Category a una cadena String
                String categoryName = item.getName();

                // Establecer el texto en la etiqueta


                // Establecer la etiqueta como el contenido de la celda
                setGraphic(label);
                setText(item.getName());
            }
        }
    }
}
