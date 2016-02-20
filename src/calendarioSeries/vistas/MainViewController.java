/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Mes;
import calendarioSeries.modelos.Serie;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Cotel
 */
public class MainViewController {
    
    @FXML
    private Label labelNombreMes;
    @FXML
    private Label labelNumMes;
    @FXML
    private Label labelNumAno;
    @FXML
    private List<Label> diasMes;
    @FXML
    private Button next;
    @FXML
    private Button previous;
    @FXML
    private MenuItem addNew;
    @FXML
    private MenuItem returnToMonth;
    @FXML
    private TilePane imagenes;    
    
    public MainApp mainApp;
    private Mes mesActual;
    private List<Serie> series;
    private int hoy;
    private int esteMes;
    private int esteAno;
    
    public static Serie serieToPass;
    public static Scene sceneToPass;
    
    public MainViewController() {
        mesActual = new Mes();
        this.series = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        this.hoy = cal.get(Calendar.DAY_OF_MONTH);
        this.esteMes = mesActual.getNumMes();
        this.esteAno = mesActual.getNumAno();
        try {
            File datos = new File("seriesUsuario.json");
            Scanner in = new Scanner(datos);
            String toJson = "";
            while(in.hasNext()) {
                toJson += in.nextLine();
            }
            JSONObject sesion = new JSONObject(toJson);
            Set<String> ids = sesion.keySet();
            for (String id : ids) {
                Serie aux = new Serie(id);
                JSONArray lastVisto = sesion.getJSONArray(id);
                String lastVistoString = lastVisto.getString(1);
//                System.out.println(lastVisto.get(1));
                aux.setVistosHasta(Integer.parseInt(lastVistoString.substring(0, lastVistoString.lastIndexOf('x'))),
                        Integer.parseInt(lastVistoString.substring(lastVistoString.lastIndexOf('x')+1, lastVistoString.length())));
                this.series.add(new Serie(id));
            }
            
        } catch (FileNotFoundException e) {
                        
        }
    }
    
    @FXML
    public void initialize() {        
        next = new Button();
        next.setId("next");
        previous = new Button();
        previous.setId("previous");
        imagenes.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                imagenes.hgapProperty().set(newSceneWidth.doubleValue()/88);
            }
        });
        populateImagenes();
        showDetallesMes(mesActual);
    }
    
    public List<Serie> getSeries() {
        return this.series;
    }
    
    public void populateImagenes() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                imagenes.getChildren().clear();
                for (Serie serie : series) {
                    try {
                        Image image = new Image(serie.getUrlImagen());
                        ImageView poster = new ImageView();
                        ContextMenu menu = new ContextMenu();
                        MenuItem delete = new MenuItem("Eliminar");
                        delete.setId(serie.getId());
                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                menu.hide();
                                MenuItem clicked = (MenuItem) event.getSource();
                                String toDelete = clicked.getId();
                                for (Serie serie : series) {
                                    if(serie.getId().equals(toDelete)) {
                                        series.remove(serie);                                
                                        populateImagenes();
                                        showDetallesMes(mesActual);
                                    }
                                }
                                event.consume();
                            }
                        });

                        menu.getItems().add(delete);


                        poster.setId(serie.getTitulo());
                        poster.setImage(image);
                        poster.setCache(true);
                        // poster.setPreserveRatio(true);
                        poster.setFitWidth(210);
                        poster.setFitHeight(300);
                        poster.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent event) {
                                menu.show(poster, event.getScreenX(), event.getScreenY());
                                event.consume();
                            }                    
                        });
                        poster.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                if(event.getButton() == MouseButton.PRIMARY) {
                                    try {
                                        serieToPass = serie;
                                        sceneToPass = mainApp.scene;

                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("DetailsSerieController.fxml"));                                    
                                        Parent root = loader.load();
                                        DetailsSerieController controller = loader.getController();
                                        controller.setMainApp(mainApp);

                                        Scene scene = new Scene(root);
                                        mainApp.primaryStage.setScene(scene);
                                        mainApp.primaryStage.show();
                                    } catch (IOException ex) {
                                        ex.printStackTrace();
                                    }

                                }
                            }
                            
                        });
                        imagenes.getChildren().add(poster);
                    } catch (IllegalArgumentException e) {
                        Image image = new Image("file:src/calendarioSeries/resources/no-image.png");
                        ImageView poster = new ImageView();
                        ContextMenu menu = new ContextMenu();
                        MenuItem delete = new MenuItem("Eliminar");
                        delete.setId(serie.getId());
                        delete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                menu.hide();
                                MenuItem clicked = (MenuItem) event.getSource();
                                String toDelete = clicked.getId();
                                for (Serie serie : series) {
                                    if(serie.getId().equals(toDelete)) {
                                        series.remove(serie);                                
                                        populateImagenes();
                                        showDetallesMes(mesActual);
                                    }
                                }
                                event.consume();
                            }
                        });

                        menu.getItems().add(delete);


                        poster.setId(serie.getTitulo());
                        poster.setImage(image);
                        //poster.setCache(true);
                        //poster.setPreserveRatio(true);
                        poster.setFitWidth(210);
                        poster.setFitHeight(300);
                        poster.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                            @Override
                            public void handle(ContextMenuEvent event) {
                                menu.show(poster, event.getScreenX(), event.getScreenY());
                                event.consume();
                            }                    
                        });
                        Text text = new Text(serie.getTitulo());
                        text.getStyleClass().add("label");
                        StackPane pane = new StackPane();
                        pane.getChildren().addAll(poster, text);
                        
                        imagenes.getChildren().add(pane);
                    } finally {
                        rellenarArchivo();
                    }
                }
            }
        });
        
    }
    
    private void rellenarArchivo() {
        try {
            File file = new File("seriesUsuario.json");
            PrintWriter pw = new PrintWriter(file);
            JSONObject array = new JSONObject();
            int count = 0;
            for (Serie serie : series) {
                JSONArray auxi = new JSONArray();
                auxi.put(0, serie.getTitulo());
                auxi.put(1, serie.getLastVisto());
                array.put(serie.getId(), auxi);
            }
            pw.println(array.toString());
            pw.flush();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
            
    
    public void showDetallesMes(Mes mes) {
        // System.out.println(mes.getNumAno() + " - " + mes.getNumMes() + "(" + mes.getDiasMes() + ")");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                for (Label diasMe : diasMes) {
                    diasMe.setStyle("");
                    diasMe.setId("");
                }
                labelNombreMes.setText(mes.getNombreMes().toUpperCase());
                labelNumMes.setText(Integer.toString(mes.getNumMes()+1));
                labelNumAno.setText(Integer.toString(mes.getNumAno()));

                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MONTH, mes.getNumMes());
                cal.set(Calendar.YEAR, mes.getNumAno());
                cal.set(Calendar.DAY_OF_MONTH, 1);

                int firstDay = cal.getTime().getDay();
                if(firstDay != 0) {
                    firstDay -= 1;
                } else {
                    firstDay = 6;
                }        
                int count = 1;

                for(int i=0; i<mes.getDiasMes(); i++) {
                    String caps = "";
                    for (Serie serie : series) {
                        Map<Integer, String> capitulosMes = serie.getCapitulosMes(mes.getNumAno(), mes.getNumMes()+1);
                        if(capitulosMes.get(count) != null) {
                            for (Map.Entry<Integer, String> entry : capitulosMes.entrySet()) {
                                if(entry.getKey() == count) {
                                    if(entry.getValue().length() >= 30) {
                                        caps += serie.getTitulo() + ": +2 caps";
                                    } else {
                                        caps += serie.getTitulo() + ": " + entry.getValue();
                                        caps += "\n";
                                    }
                                }
                            }                    
                        }
                    }
                    diasMes.get(firstDay + i).setText(Integer.toString(count) + "\n\n" + caps);
                    if(mes.getNumAno() == esteAno) {
                        if(mes.getNumMes() == esteMes) {
                            if(count == hoy) {                                               
                                diasMes.get(firstDay + i).setId("hoy");
                            } else if(count < hoy) {
                                diasMes.get(firstDay + i).setStyle("-fx-text-fill: grey;");
                            }
                        } else if(mes.getNumMes() < esteMes) {
                            diasMes.get(firstDay + i).setStyle("-fx-text-fill: grey;");
                        }
                    } else if(mes.getNumAno() < esteAno) {
                        diasMes.get(firstDay + i).setStyle("-fx-text-fill: grey;");
                    }
                    count++;
                }
            }
        });
    }
    
    @FXML
    private void addNewSerie() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainViewController.class.getResource("NewSerieView.fxml"));
            root = loader.load();
            
            NewSerieController controller = loader.getController();
            controller.setMainController(this);
            
            Stage stage = new Stage();
            stage.setTitle("Añade una nueva serie");
            stage.setScene(new Scene(root));
            stage.setMinHeight(650);
            stage.setMinWidth(600);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleReturnToActual(ActionEvent event) {
        this.mesActual = new Mes();
        showDetallesMes(mesActual);
    }
    
    @FXML
    private void handleButtonPressed(ActionEvent event) {
        Button buttonClicked = (Button) event.getSource();
        String id = buttonClicked.getId();
        for (Label diasMe : diasMes) {
            diasMe.setText("");
        }
        
        if(id.equals("next")) {
            if(mesActual.getNumMes() != 11) {
                mesActual = new Mes(mesActual.getNumAno(), mesActual.getNumMes()+1);
            } else {
                mesActual = new Mes(mesActual.getNumAno()+1, 0);
            }
        } else if(id.equals("previous")) {
            if(mesActual.getNumMes() != 0) {
                mesActual = new Mes(mesActual.getNumAno(), mesActual.getNumMes()-1);
            } else {
                mesActual = new Mes(mesActual.getNumAno()-1, 11);
            }
        }
        showDetallesMes(mesActual);
        event.consume();
    }
    
    public Mes getMesActual() {
        return mesActual;
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
    }
}
