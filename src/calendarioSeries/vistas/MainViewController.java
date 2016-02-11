/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Mes;
import java.util.Calendar;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
    @FXML
    private Button next;
    @FXML
    private Button previous;
    
    private MainApp mainApp;
    private Mes mesActual;
    
    public MainViewController() {
        mesActual = new Mes();
    }
    
    @FXML
    public void initialize() {
        next = new Button();
        next.setId("next");
        previous = new Button();
        previous.setId("previous");
        showDetallesMes(mesActual);
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
