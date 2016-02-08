/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries;

import calendarioSeries.modelos.Mes;
import calendarioSeries.vistas.MainViewController;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Cotel
 */
public class MainApp extends Application {
    
    private Stage primaryStage;
    private AnchorPane rootLayout;
    
    private ObservableList<Mes> meses = FXCollections.observableArrayList();
    
    public MainApp() {
        meses.add(new Mes());        
    }
    
    public ObservableList<Mes> getDatosMes() {
        return meses;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Calendario Series");
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("vistas/MainView.fxml"));
            rootLayout = (AnchorPane) loader.load();
            
            MainViewController controller = loader.getController();
            controller.setMainApp(this);
            
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
