/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Mes;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;

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
    
    private MainApp mainApp;
    
    public MainViewController() {
        
    }
    
    @FXML
    public void initialize() {
        showDetallesMes(new Mes());
    }
    
    private void showDetallesMes(Mes mes) {
        labelNombreMes.setText(mes.getNombreMes().toUpperCase());
        labelNumMes.setText(Integer.toString(mes.getNumMes()));
        labelNumAno.setText(Integer.toString(mes.getNumAno()));
        
        int i = 1;
        for(Label label : diasMes) {
            label.setText(Integer.toString(i++));
        }
    }
    
    @FXML
    private void handleButtonPressed() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Prueba");
        alert.setHeaderText("Prueba");
        alert.setContentText("Esto es una prueba");
        
        alert.showAndWait();
    }
    
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
