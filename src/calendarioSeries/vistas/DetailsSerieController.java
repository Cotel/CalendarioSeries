/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Serie;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Cotel
 */
public class DetailsSerieController {
    
    @FXML
    private Button back;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private ImageView poster;
    @FXML
    private ListView temporadas;
    @FXML
    private ListView capitulos;
    
    private Serie serie;
    private Scene anterior;
    private MainApp mainApp;
    
    public DetailsSerieController() {
        this.serie = MainViewController.serieToPass;
        this.anterior = MainViewController.sceneToPass;
    }
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        this.title.setText(serie.getTitulo());
        this.description.setText(serie.getDescription());
        Image image = new Image(serie.getUrlImagen());
        this.poster.setImage(image);
        this.poster.getStyleClass().clear();
        populateLists();
    }
    
    public void setSerie(Serie serie) {
        this.serie = serie;
    }
    
    public void setAnteriorScene(Scene scene) {
        this.anterior = scene;
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    private void populateLists() {
        List<String> temps = new ArrayList<String>();
        for(int i=0; i<serie.getTemporadas(); i++) {
            temps.add("Temporada " + (i+1));
        }
        ObservableList<String> observableList = FXCollections.observableArrayList(temps);
        temporadas.setItems(observableList);
        temporadas.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                List<String> caps = new ArrayList<>();
                for(int i=0; i<serie.getCapitulos()[newValue.intValue()].length; i++) {
                    String aux = serie.getCapitulos()[newValue.intValue()][i];
                    String numCap = (newValue.intValue()+1)+"x"+(i+1);
                    String nomCap = aux.substring(0, aux.lastIndexOf(' '));
                    String fecCap = aux.substring(aux.lastIndexOf(' '), aux.length());
                    String res = "%-20s %-70s %50s";
                    res = String.format(res, numCap, nomCap, fecCap);                                                   // String.format solo coge 2 argumentos
                    caps.add(res);
                }
                ObservableList<String> thisCaps = FXCollections.observableArrayList(caps);
                capitulos.setItems(thisCaps);
            }
            
        });
    }
    
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            
            mainApp.scene.setCursor(Cursor.WAIT);
            Scene scene = MainViewController.sceneToPass;
            mainApp.primaryStage.setScene(scene);            
            mainApp.primaryStage.show();
            mainApp.scene.setCursor(Cursor.DEFAULT);            
        } catch (IOException ex) {
            Logger.getLogger(DetailsSerieController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            event.consume();
        }
    }
    
}
