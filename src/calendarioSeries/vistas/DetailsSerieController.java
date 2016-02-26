/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Capitulo;
import calendarioSeries.modelos.Serie;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Cotel
 */
public class DetailsSerieController {
    
    private final static String VISTO = "✓";
    private final static String NOVISTO = "×";
    
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
    private ArrayList<Serie> series;
    private Scene anterior;
    private MainApp mainApp;
    
    public DetailsSerieController() {
        //this.serie = MainViewController.serieToPass;
        this.anterior = MainViewController.sceneToPass;
    }
    
    /**
     * Initializes the controller class.
     */
    @FXML
    public void initialize() {
        
    }
    
    void setData(Serie serie, ArrayList<Serie> series) {
        this.series = series;
        this.serie = serie;
        this.title.setText(serie.getTitulo());
        this.description.setText(serie.getDescription());
        Image image = new Image(serie.getUrlImagen());
        this.poster.setImage(image);
        this.poster.getStyleClass().clear();
        populateLists();
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
                    Capitulo aux = serie.getCapitulos()[newValue.intValue()][i];
                    int numCap = i+1;
                    String nomCap = aux.getTitulo().get();
                    String res = "%s\t %02d\t\t %-50s";
                    if(aux.getVisto().get() == true)
                        res = String.format(res, VISTO, numCap, nomCap);
                    else
                        res = String.format(res, NOVISTO, numCap, nomCap);
                    caps.add(res);                                                           
                }
                final int tempActual = newValue.intValue();
                
                ObservableList<String> thisCaps = FXCollections.observableArrayList(caps);
                capitulos.setItems(thisCaps);
                capitulos.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(event.getClickCount() == 2) {
                            int index = capitulos.getSelectionModel().getSelectedIndex();
                            serie.getCapitulos()[tempActual][index].changeVisto();
                            String cell = (String)capitulos.getSelectionModel().getSelectedItem();
                            Capitulo aux = serie.getCapitulos()[tempActual][index];
                            String nomCap = aux.getTitulo().get();
                            int numCap = index+1;
                            cell = "%s\t %02d\t\t %-50s";
                            if(aux.getVisto().get() == true)
                                cell = String.format(cell, VISTO, numCap, nomCap);
                            else
                                cell = String.format(cell, NOVISTO, numCap, nomCap);
                            caps.set(index, cell);
                            ObservableList<String> thisCaps = FXCollections.observableArrayList(caps);
                            capitulos.setItems(thisCaps);
                        }
                    }
                });
                
            }
            
        });
    }
    
    public void goBack(ActionEvent event) {
        int i=0;
        for (Serie serie : series) {
            if(this.serie.equals(serie)){
                series.set(i, serie);
                break;
            }
            i++;
        }
        File file;
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            file = new File("data.db");
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(this.series);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
            Parent root = loader.load();
            MainViewController controller = loader.getController();
            //controller.setSeries(series);
            
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
