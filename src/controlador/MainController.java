/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package controlador;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop.Action;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Acount;
import model.AcountDAOException;
import model.Category;
import model.Charge;

/**
 * FXML Controller class
 *
 * @author JORGE
 */
public class MainController implements Initializable {

    @FXML
    private Menu btEliminar;
    @FXML
    private MenuItem btEditar;
    @FXML
    private MenuItem btCerrarSesion;
    @FXML
    private Button btAñadir;
    @FXML
    private TableColumn<Charge, Integer> columId;
    @FXML
    private TableColumn<Charge, String> columNombre;
    @FXML
    private TableColumn<Charge, Double> columPrecio;
    @FXML
    private TableColumn<Charge, LocalDate> columFecha;
    @FXML
    private TableColumn<Charge, Void> columDetalles;
    @FXML
    private TableColumn<Charge, Void> columCerrar;
    @FXML
    private ImageView perfil1;
    @FXML
    public TableView tabla;
    @FXML
    private Button pdfBoton;
    
    private ObservableList<Charge> data = null;
    private ObservableList<Charge> filtroData = null;
    @FXML
    private TableColumn<Charge, Void> columEditar;
    @FXML
    private MenuItem filtroFechaI;
    @FXML
    private DatePicker fechaInicio;
    @FXML
    private MenuItem filtroFechaF;
    @FXML
    private DatePicker fechaFinal;
    @FXML
    private Menu filtroCategoria;
    @FXML
    private MenuItem filtroLimpiar;
    @FXML
    private Label textoUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeTableColumns();
        loadUserData();
    }

    private void initializeTableColumns() {
        // Inicializar las columnas de la tabla
        columId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columNombre.setCellValueFactory(new PropertyValueFactory<>("name"));
        columPrecio.setCellValueFactory(new PropertyValueFactory<>("cost"));
        columFecha.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Configurar celdas personalizadas
        setupDetailsCellFactory();
        setupDeleteCellFactory();
        setupEditCellFactory();
    }

    private void setupDetailsCellFactory() {
        Callback<TableColumn<Charge, Void>, TableCell<Charge, Void>> cellFactory = new Callback<TableColumn<Charge, Void>, TableCell<Charge, Void>>() {
            @Override
            public TableCell<Charge, Void> call(final TableColumn<Charge, Void> param) {
                final TableCell<Charge, Void> cell = new TableCell<Charge, Void>() {
                    private final Button btnDetalles = new Button("Detalles");

                    {
                        // Configurar el botón de detalles
                        btnDetalles.setOnAction((ActionEvent event) -> {
                            Charge data = getTableView().getItems().get(getIndex());
                            showExpenseDetails(data);
                        });
                        // Establecer estilo del botón
                        btnDetalles.setStyle("-fx-background-color: #d5d5d5;\n" +
                                             "-fx-cursor: hand;\n"  +
                                             "-fx-background-color: white;\n" +
                                             "-fx-background-radius: 10;\n" +
                                             "-fx-border-width: 0;\n" +
                                             "-fx-border-radius: 1;\n" +
                                             "-fx-text-fill: rgb(3, 104, 107);\n" +
                                             "-fx-effect: dropshadow(gaussian, rgb(41, 98, 147), 10, 0, 0, 0);\n");
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btnDetalles);
                        }
                    }

                    private void showExpenseDetails(Charge data) {
                        // Método para mostrar los detalles del gasto
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Detalles del gasto");
                        alert.setHeaderText(null);

                        Label texto = new Label("Nombre: " + data.getName() + "\n" +
                                "Descripción: " + data.getDescription() + "\n" +
                                "Categoría: " + data.getCategory().getName() + "\n" +
                                "Fecha: " + data.getDate() + "\n" +
                                "Coste: " + data.getCost() + "€\n" +
                                "Unidades: " + data.getUnits() + "\n");

                        ImageView ticket = new ImageView(data.getImageScan());
                        ticket.setFitHeight(100); ticket.setFitWidth(150);

                        HBox content = new HBox(10); // 10 es el espacio entre los elementos
                        content.getChildren().addAll( texto, ticket);

                        alert.getDialogPane().setContent(content);

                        ImageView imageView = new ImageView(new Image("/resources/images/lupa.png"));
                             // Establecer el tamaño deseado para la imagen
                        imageView.setFitWidth(50);
                        imageView.setFitHeight(50);
                        alert.setGraphic(imageView);

                        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/images/icono.png")));


                        alert.showAndWait();
                    }
                };
                return cell;
            }
        };
        columDetalles.setCellFactory(cellFactory);
    }

    private void setupDeleteCellFactory() {
        // Configurar celdas para eliminar gastos
        Callback<TableColumn<Charge, Void>, TableCell<Charge, Void>> eliminarCellFactory = new Callback<TableColumn<Charge, Void>, TableCell<Charge, Void>>() {
        @Override
        public TableCell<Charge, Void> call(final TableColumn<Charge, Void> param) {
            final TableCell<Charge, Void> cell = new TableCell<Charge, Void>() {

                private final Button btnEliminar = new Button("❌");
                

                {
                    btnEliminar.setOnAction((ActionEvent event) -> {
                        Charge data = getTableView().getItems().get(getIndex());
                        showExpenseDetails(data);
                        
                    });
                    
                    btnEliminar.setStyle( 
                                "-fx-background-color: #d5d5d5;\n" +
                                "-fx-cursor: hand;\n"  +
                                "-fx-background-color: white;\n" + "    -fx-background-radius: 10;\n" +
                                "-fx-border-width: 0;\n" + "    -fx-border-radius: 1;\n" +
                                "-fx-text-fill: rgb(255, 0, 0);\n");
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btnEliminar);
                    }
                }
                
                private void showExpenseDetails(Charge data) {
                    Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
                    alerta.setTitle("Eliminar Gasto");
                    alerta.setHeaderText("¿Estas seguro de querer eliminar este gasto?");

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
                            tabla.getItems().remove(data);
                            try {
                                // Aquí puedes agregar lógica adicional para eliminar el gasto de la base de datos si es necesario
                                Acount.getInstance().removeCharge(data);
                            } catch (AcountDAOException ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        else if(result.get() == buttonTypeNo){alerta.close();}
                    }
                }
            };
            return cell;
        }
    };

    columCerrar.setCellFactory(eliminarCellFactory);
    }

    private void setupEditCellFactory() {
        // Configurar celdas para editar gastos
        Callback<TableColumn<Charge, Void>, TableCell<Charge, Void>> editarCellFactory = new Callback<TableColumn<Charge, Void>, TableCell<Charge, Void>>() {
        @Override
        public TableCell<Charge, Void> call(final TableColumn<Charge, Void> param) {
            final TableCell<Charge, Void> cell = new TableCell<Charge, Void>() {

                private final ImageView imageView;
                private final HBox hbox;

                {
                    // Cargar la imagen y verificar si se carga correctamente
                    Image image = new Image(getClass().getResourceAsStream("/resources/images/edi.png"));
                    if (image.isError()) {
                        System.err.println("Error al cargar la imagen");
                    }
                    imageView = new ImageView(image);

                    imageView.setOnMouseClicked((MouseEvent event) -> {
                        System.out.println("Imagen clicada"); // Impresión de depuración
                        Charge data = getTableView().getItems().get(getIndex());
                        try {
                            try {
                                showExpenseDetails(data);
                            } catch (AcountDAOException ex) {
                                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    });
                    imageView.setFitWidth(27); // Ajusta el tamaño de la imagen según sea necesario
                    imageView.setFitHeight(27);
                    imageView.setStyle("-fx-cursor: hand;"); // Cambia el cursor cuando se pasa sobre la imagen



                    hbox = new HBox(imageView);
                    hbox.setAlignment(Pos.CENTER);
                }

                @Override
                public void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbox);
                    }
                }

                private void showExpenseDetails(Charge data) throws IOException, AcountDAOException {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/javafxmlapplication/EditarGasto.fxml"));
                    Parent parent = loader.load();

                    EditarGastoController controller = loader.getController();
                    controller.valoresDefecto(data);

                    Scene scene = new Scene(parent);

                    Stage window = (Stage) imageView.getScene().getWindow();

                    window.setScene(scene);
                    window.setResizable(false);
                    window.show();
                }
            };
            return cell;
        }
    };

    columEditar.setCellFactory(editarCellFactory);
    }

    private void loadUserData() {
        // Cargar datos de usuario en la tabla
        try {
            Acount log = Acount.getInstance();
            perfil1.setImage(log.getLoggedUser().getImage());
            textoUsuario.setText(log.getLoggedUser().getNickName());
            List<Charge> list = log.getUserCharges();
            this.data = FXCollections.observableArrayList(list);
            tabla.setItems(data);
            loadCategoryMenuItems(log.getUserCategories());
        } catch (AcountDAOException | IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadCategoryMenuItems(List<Category> categories) {
        // Cargar elementos de menú para las categorías
        for (Category categoria : categories) {
            MenuItem cat = new MenuItem(categoria.getName());
            MenuItem cat2 = new MenuItem(categoria.getName());
            cat.setOnAction(event -> {
                try {
                    handleCategoryMenuItemAction(categoria);
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            cat2.setOnAction(event -> handleFilterByCategoryMenuItemAction(categoria.getName()));
            btEliminar.getItems().add(cat);
            filtroCategoria.getItems().add(cat2);
        }
    }

    private void handleCategoryMenuItemAction(Category categoria) throws IOException {
        // Manejar la acción de hacer clic en un elemento de menú de categoría
        try {
            Acount.getInstance().removeCategory(categoria);
            btEliminar.getItems().removeIf(item -> item.getText().equals(categoria.getName()));
            filtroCategoria.getItems().removeIf(item -> item.getText().equals(categoria.getName()));
            removeChargesByCategory(categoria);
            tabla.refresh();
        } catch (AcountDAOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void removeChargesByCategory(Category category) throws IOException, AcountDAOException {
        // Eliminar gastos de la tabla que pertenecen a la categoría especificada
        data.removeIf(charge -> charge.getCategory().equals(category));
        actualizar();
    }

    private void handleFilterByCategoryMenuItemAction(String categoryName) {
        // Manejar la acción de filtrar por una categoría específica
        if (categoryName != null && !categoryName.isEmpty()) {
            ObservableList<Charge> items = FXCollections.observableArrayList(data);
            ObservableList<Charge> itemsFiltered = FXCollections.observableArrayList();
            for (Charge charge : items) {
                if (charge.getCategory().getName().equals(categoryName)) {
                    itemsFiltered.add(charge);
                }
            }
            tabla.setItems(itemsFiltered);
            this.filtroData = itemsFiltered;
        }
    }


    @FXML
    private void nuevaCategoria(ActionEvent event) throws IOException, AcountDAOException {
        Image icon = new Image(getClass().getResourceAsStream("/resources/images/icono.png"));
        
        Parent parent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/NuevaCategoria.fxml"));
        Scene scene = new Scene(parent);
        
        Stage window = new Stage();
        
        window.setScene(scene);
        window.getIcons().add(icon);
        window.setTitle("Controlador de Gastos");
        window.setResizable(false);
        window.showAndWait();
        
        actualizar();
    }

    private void actualizar() throws IOException, AcountDAOException {
        Parent editarParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/Main.fxml"));
        Scene editarScene = new Scene(editarParent);
        
        Stage window = (Stage) perfil1.getScene().getWindow();
        window.setScene(editarScene);
        window.show();
        
        btEliminar.getItems().clear();
        List<Category> listaCategoria = Acount.getInstance().getUserCategories();
        for(int i = 0; i < listaCategoria.size(); i++){
            Category categoria = listaCategoria.get(i);
            MenuItem cat = new MenuItem(categoria.getName());
            btEliminar.getItems().add(cat);
        }
    }

    @FXML
    private void editar(ActionEvent event) throws IOException {
        Parent editarParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/EditarPerfil.fxml"));
        Scene editarScene = new Scene(editarParent);

        // Obtener la ventana actual
        Stage window = (Stage) perfil1.getScene().getWindow();

        // Establecer la nueva escena
        window.setScene(editarScene);
        window.setResizable(false);        
        window.show();
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws AcountDAOException, IOException {
        Acount.getInstance().logOutUser();
        Parent logInParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/LogIn.fxml"));
        Scene logInScene = new Scene(logInParent);

        // Obtener la ventana actual
        Stage window = (Stage) perfil1.getScene().getWindow();

        // Establecer la nueva escena
        window.setScene(logInScene);
        window.setResizable(false);
        window.show();
    }

    @FXML
    private void click(MouseEvent event) throws IOException {
        Parent editarParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/EditarPerfil.fxml"));
        Scene editarScene = new Scene(editarParent);

        // Obtener la ventana actual
        Stage window = (Stage) perfil1.getScene().getWindow();

        // Establecer la nueva escena
        window.setScene(editarScene);
        window.setResizable(false);        
        window.show();
    }

    @FXML
    private void añadir(ActionEvent event) throws IOException {
        Parent editarParent = FXMLLoader.load(getClass().getResource("/javafxmlapplication/NuevoGasto.fxml"));
        Scene editarScene = new Scene(editarParent);

        // Obtener la ventana actual
        Stage window = (Stage) perfil1.getScene().getWindow();

        // Establecer la nueva escena
        window.setScene(editarScene);
        window.setResizable(false);        
        window.show();
    }

    @FXML
    private void pdf(ActionEvent event) {
        Document document = new Document() {};
        ObservableList<Charge> info = data;
        if(filtroData != null) {info = filtroData;}
        try {
            String home = System.getProperty("user.home");
            File file = new File(home + "/ReporteGastos.pdf");
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            // Añadir el título del reporte
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, BaseColor.BLACK);
            titleFont.setStyle(home);
            Paragraph title = new Paragraph("Reporte de Gastos", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Añadir los datos de los gastos
            PdfPTable table = new PdfPTable(5); // Número de columnas
            table.setWidthPercentage(100);
            table.setSpacingBefore(20);
            Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font tableCellFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
            table.addCell(createColumnHeaderStyle("Nombre", tableHeaderFont));
            table.addCell(createColumnHeaderStyle("Fecha", tableHeaderFont));
            table.addCell(createColumnHeaderStyle("Coste", tableHeaderFont));
            table.addCell(createColumnHeaderStyle("Categoria", tableHeaderFont));
            table.addCell(createColumnHeaderStyle("Descripción", tableHeaderFont));
            for (Charge gasto : info) {
                table.addCell(createCellStyle(gasto.getName(), tableCellFont));
                table.addCell(createCellStyle(gasto.getDate().toString(), tableCellFont));
                table.addCell(createCellStyle(String.valueOf(gasto.getCost()) + "€", tableCellFont));
                table.addCell(createCellStyle(gasto.getCategory().getName(), tableCellFont));
                table.addCell(createCellStyle(gasto.getDescription(), tableCellFont));
            }
            document.add(table);
            document.add(Chunk.NEWLINE);

            // Agregar los datos del total gastado y el rango de fechas
            LocalDate firstDate = Collections.min(info, Comparator.comparing(Charge::getDate)).getDate();
            LocalDate lastDate = Collections.max(info, Comparator.comparing(Charge::getDate)).getDate();
            double totalGastado = info.stream().mapToDouble(Charge::getCost).sum();

            // Crear una tabla para mostrar el total gastado y el rango de fechas
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setWidthPercentage(50);
            summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            summaryTable.addCell(createColumnHeaderStyle("Total Gastado:", tableHeaderFont));
            summaryTable.addCell(createCellStyle(totalGastado + "€", tableCellFont));
            summaryTable.addCell(createColumnHeaderStyle("Rango de Fechas:", tableHeaderFont));
            summaryTable.addCell(createCellStyle(firstDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - " + lastDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), tableCellFont));
            document.add(summaryTable);

            document.close();

            // Muestra un mensaje de éxito
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reporte generado");
            alert.setHeaderText(null);
            alert.setContentText("El reporte de gastos ha sido generado exitosamente en: " + file.getAbsolutePath());
            alert.showAndWait();

        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
            // Muestra un mensaje de error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al generar reporte");
            alert.setHeaderText(null);
            alert.setContentText("Ocurrió un error al generar el reporte de gastos.");
            alert.showAndWait();
        }
    } 
    
    private PdfPCell createColumnHeaderStyle(String content, Font font) {
        PdfPCell columnHeaderStyle = new PdfPCell(new Phrase(content, font));        
        columnHeaderStyle.setBackgroundColor(new BaseColor(0, 100, 109));
        columnHeaderStyle.setVerticalAlignment(Element.ALIGN_MIDDLE);
        columnHeaderStyle.setHorizontalAlignment(Element.ALIGN_CENTER);
        columnHeaderStyle.setBorderColor(BaseColor.BLACK);
        columnHeaderStyle.setPadding(8);
        return columnHeaderStyle;
    }
     
    private PdfPCell createCellStyle(String content, Font font) {
        PdfPCell cellStyle = new PdfPCell(new Phrase(content, font));        
        cellStyle.setBackgroundColor(new BaseColor(0, 100, 109, 30)); // 30 es el valor de transparencia
        cellStyle.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cellStyle.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellStyle.setBorderColor(BaseColor.BLACK);
        cellStyle.setPadding(8);
        return cellStyle;
    }

    @FXML
    private void añadirFechaI(ActionEvent event) {
        LocalDate startDate = fechaInicio.getValue();
        LocalDate endDate = fechaFinal.getValue();
        if (startDate != null && endDate != null) {
            // Verificar si ya existe un filtro aplicado
            if (filtroData == null) {
                // Si no hay filtro aplicado, simplemente usar la lista original de datos
                filtroData = FXCollections.observableArrayList(data);
                tabla.setItems(filtroData);
            }

            // Limpiar la lista de datos filtrados
            filtroData.clear();

            // Filtrar los elementos y agregarlos a la lista de datos filtrados
            for (Charge charge : data) {
                LocalDate chargeDate = charge.getDate();
                if (chargeDate.isAfter(startDate.minusDays(1)) && chargeDate.isBefore(endDate.plusDays(1))) {
                    filtroData.add(charge);
                }
            }
        }
    }

    @FXML
    private void añadirFechaF(ActionEvent event) {
        LocalDate startDate = fechaInicio.getValue();
        LocalDate endDate = fechaFinal.getValue();
        if (startDate != null && endDate != null) {
            // Verificar si ya existe un filtro aplicado
            if (filtroData == null) {
                // Si no hay filtro aplicado, simplemente usar la lista original de datos
                filtroData = FXCollections.observableArrayList(data);
                tabla.setItems(filtroData);
            }

            // Limpiar la lista de datos filtrados
            filtroData.clear();

            // Filtrar los elementos y agregarlos a la lista de datos filtrados
            for (Charge charge : data) {
                LocalDate chargeDate = charge.getDate();
                if (chargeDate.isAfter(startDate.minusDays(1)) && chargeDate.isBefore(endDate.plusDays(1))) {
                    filtroData.add(charge);
                }
            }
        }
    }


    @FXML
    private void limpiar(ActionEvent event) throws IOException, AcountDAOException {
        this.filtroData = FXCollections.observableArrayList(this.data);
        actualizar();
        fechaInicio.setValue(null);
        fechaFinal.setValue(null);
    }
    
}
