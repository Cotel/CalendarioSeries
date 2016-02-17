/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries;

import calendarioSeries.modelos.Serie;
import calendarioSeries.vistas.MainViewController;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Cotel
 */
public class MainApp extends Application {
    
    public Stage primaryStage;
    private BorderPane rootLayout;
    public Scene scene;
    
    private ObservableList<Serie> series = FXCollections.observableArrayList();
    
    public MainApp() {

    }
    
    public ObservableList<Serie> getDatosSeries() {
        return series;
    }
    
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Calendario Series");
        
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("vistas/MainView.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            MainViewController controller = loader.getController();
            controller.setMainApp(this);
            
            this.scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setMinHeight(810);
            primaryStage.setMinWidth(1150);
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
