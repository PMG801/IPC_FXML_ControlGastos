/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import model.Acount;
import model.AcountDAOException;
import model.Category;

/**
 * FXML Controller class
 *
 * @author JORGE
 */
public class NuevoGastoController implements Initializable {

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
    private TextField coste;
    @FXML
    private ImageView ticket;
    @FXML
    private Button btBuscador;
    @FXML
    private Label textCategoria;
    @FXML
    private ComboBox<Category> categoria;
    
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
                categoria.setCellFactory((c) -> {return new NuevoGastoController.CategoryTabCell();} );
                
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
            Logger.getLogger(NuevoGastoController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NuevoGastoController.class.getName()).log(Level.SEVERE, null, ex);
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
            Acount.getInstance().registerCharge(nombre.getText(), descripcion.getText(),(double) Double.parseDouble(coste.getText()),(int) unidades.getValue(), image, fecha.getValue(), categoria.getSelectionModel().getSelectedItem());
            Parent registroParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Main.fxml"));
            Scene registroScene = new Scene(registroParent);
            
            
            
            // Obtener la ventana actual
            Stage window = (Stage) btEditar.getScene().getWindow();

            // Establecer la nueva escena
            window.setScene(registroScene);
            window.setResizable(true);
            window.show();
        } else {
            System.out.println("No se pudo guardar el gasto.");
        }
    }

    @FXML
    private void buscador(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir ticket");
        fileChooser.getExtensionFilters().addAll(
        new ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.gif"));
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
                label.setText(categoryName);
                

                // Establecer la etiqueta como el contenido de la celda
                setGraphic(label);
            }
        }
    }
    
}
