/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Mes;
import calendarioSeries.modelos.Serie;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

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
    private TilePane imagenes;
    
    private MainApp mainApp;
    private Mes mesActual;
    private List<Serie> series;
    
    public MainViewController() {
        mesActual = new Mes();
        this.series = new ArrayList<>();
        this.series.add(new Serie("arrow"));
        this.series.add(new Serie("narcos"));
        this.series.add(new Serie("the flash"));
        this.series.add(new Serie("game of thrones"));
        this.series.add(new Serie("house of cards"));
        this.series.add(new Serie("the magicians"));
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
    
    private void populateImagenes() {
        for (Serie serie : series) {
            Image image = new Image(serie.getUrlImagen());
            ImageView poster = new ImageView();
            poster.setImage(image);
            // poster.setPreserveRatio(true);
            poster.setFitWidth(210);
            poster.setFitHeight(300);
//            poster.setSmooth(true);
            imagenes.getChildren().add(poster);
        }
    }
    
    private void showDetallesMes(Mes mes) {
        System.out.println(mes.getNumAno() + " - " + mes.getNumMes() + "(" + mes.getDiasMes() + ")");
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
            diasMes.get(firstDay + i).setText(Integer.toString(count++));
        }
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
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
        
    }
}
