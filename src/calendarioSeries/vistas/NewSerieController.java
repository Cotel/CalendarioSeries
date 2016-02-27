/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calendarioSeries.vistas;

import calendarioSeries.MainApp;
import calendarioSeries.modelos.Serie;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Cotel
 */
public class NewSerieController {
    
    private static final String BASE = "https://www.omdbapi.com/?s=";
    
    @FXML
    private TextField titulo;
    @FXML
    private Button ok;
    @FXML
    private TilePane resultados;
    
    private MainViewController mainController;
    
    
    public NewSerieController() {
        
    }
    
    @FXML
    public void initialize() {
        ok.setDefaultButton(true);        
    }
    
    @FXML
    public void handleOk() {
        String tituloAux = titulo.getText().replaceAll(" ", "+").toLowerCase();
        String toJson = readUrl(BASE + tituloAux + "&type=series" + "&r=json");
        resultados.getChildren().clear();
        try {
            JSONObject busqueda = new JSONObject(toJson);
            if(busqueda.getString("Response").equals("True")) {
                JSONArray res = busqueda.getJSONArray("Search");
                resultados.setPrefRows(res.length());
                for(int i=0; i<res.length(); i++) {
                    JSONObject resActual = new JSONObject(res.get(i).toString());
                    HBox resultadoActual = new HBox(50);                    
                    resultadoActual.setMaxWidth(Double.MAX_VALUE);
                    resultadoActual.setAlignment(Pos.CENTER_LEFT);
                    ImageView posterActual = new ImageView();
                    
                    try {
                        Image image = new Image(resActual.getString("Poster"));
                        posterActual.setImage(image);
                        posterActual.setFitHeight(240);
                        posterActual.setFitWidth(180);
                        posterActual.setPreserveRatio(false);
                        resultadoActual.getChildren().add(posterActual);
                    } catch (IllegalArgumentException e) {
//                        System.out.println("Bad url");
                        Image image = new Image(MainApp.class.getResource("resources/no-image.png").toExternalForm());
                        posterActual.setImage(image);
                        posterActual.setFitHeight(240);
                        posterActual.setFitWidth(180);
                        posterActual.setPreserveRatio(false);
                        resultadoActual.getChildren().add(posterActual);
                    }
                    
                    String details;
                    String nomSerie = new String(resActual.getString("Title").getBytes(), "UTF-8");
                    String anoSerie = new String(resActual.getString("Year").getBytes(), "UTF-8");
                    if(nomSerie.length() > 15) {
                        details = "%-12.12s...\t\t Año: %-10s";
                    } else {
                        details = "%-12s\t\t Año: %-10s";
                    }                    
                    details = String.format(details, nomSerie, anoSerie);
                    Label elemento = new Label(details);
                    elemento.setMaxWidth(Double.MAX_VALUE);
                    elemento.setMaxHeight(Double.MAX_VALUE);                    
                    resultadoActual.getChildren().add(elemento);
                    
                    posterActual.setId(resActual.getString("imdbID"));
                    posterActual.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            ImageView clickedButton = (ImageView) event.getSource();
                            Stage stage = (Stage) clickedButton.getScene().getWindow();
                            Task task = new Task() {
                                @Override
                                protected Object call() throws Exception {
                                    mainController.mainApp.scene.setCursor(Cursor.WAIT);                                    
                                    Serie toAdd = new Serie(clickedButton.getId());
                                    boolean possible = true;
                                    for (Serie serie : mainController.getSeries()) {
                                        if(serie.equals(toAdd))
                                            possible = false;
                                    }
                                    if(possible)
                                        mainController.getSeries().add(toAdd);

                                    try {
                                        mainController.populateImagenes();
                                        mainController.showDetallesMes(mainController.getMesActual());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        mainController.mainApp.scene.setCursor(Cursor.DEFAULT);                                    
                                        
                                        return mainController.getSeries();
                                    }                                    
                                }                                
                            };
                            Thread th = new Thread(task);
                            th.setDaemon(true);
                            th.start();
                            stage.close();
                        }
                    });
                    resultados.getChildren().add(resultadoActual);
                }
            } else {
                resultados.getChildren().add(new Label("La busqueda no obtuvo resultados"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(NewSerieController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void changeStyleOnHover(Node node) {
        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setStyle("-fx-background-color: derive(#0096C9, 30%);");
            }
        });
        node.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setStyle("-fx-background-color: #0096C9");
            }            
        });
    }
    
    private String readUrl(String stringUrl) {
        BufferedReader reader = null;
        try {
               URL url = new URL(stringUrl);         
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuffer buffer = new StringBuffer();
                int read;
                char[] chars = new char[1024];
                while((read = reader.read(chars)) != -1)
                    buffer.append(chars, 0, read);
                return buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(reader != null) {
                    reader.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void setMainController(MainViewController mainController) {
        this.mainController = mainController;
    }
    
    
}
