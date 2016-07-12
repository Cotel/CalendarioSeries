package calendarioSeries.controllers;

import calendarioSeries.MainApp;
import calendarioSeries.controls.ButtonImageView;
import calendarioSeries.models.Mes;
import calendarioSeries.models.Serie;
import calendarioSeries.services.SerieService;
import calendarioSeries.services.SerieServiceImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Cotel on 10/07/2016.
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

    private Mes mesActual;
    private int[] hoy = new int[3];
    private List<Serie> series;

    private SerieService serieService = new SerieServiceImpl();

    public void initialize() {
//        Serie s = new Serie("tt2193021");
//        serieService.addSerie(s);

        this.mesActual = new Mes();
        Calendar cal = Calendar.getInstance();
        this.hoy[0] = cal.get(Calendar.DAY_OF_MONTH);
        this.hoy[1] = this.mesActual.getNumMes();
        this.hoy[2] = this.mesActual.getNumAno();

        populateImages();
        fillMonth(mesActual);
    }

    private void fillMonth(Mes mes) {
        for (Label l : diasMes) {
            l.setText("");
        }

        labelNombreMes.setText(mes.getNombreMes().toUpperCase());
        labelNumMes.setText(Integer.toString(mes.getNumMes() + 1));
        labelNumAno.setText(Integer.toString(mes.getNumAno()));

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, mes.getNumMes());
        cal.set(Calendar.YEAR, mes.getNumAno());
        cal.set(Calendar.DAY_OF_MONTH, 1);

        int firstDay = cal.getTime().getDay();
        int count = 1;
        if (firstDay != 0) {
            firstDay -= 1;
        } else {
            firstDay = 6;
        }

        for (int i = 0; i < mes.getDiasMes(); i++, count++) {
            diasMes.get(firstDay + i).setText(Integer.toString(count));

            if (mes.getNumAno() == this.hoy[2]) {
                if (mes.getNumMes() == this.hoy[1]) {
                    if (count == this.hoy[0]) {
                        diasMes.get(firstDay + i).setId("hoy");
                    } else if (count < this.hoy[0]) {
                        diasMes.get(firstDay + i).setStyle("-fx-text-fill: grey;");
                    }
                } else if (mes.getNumMes() < this.hoy[1]) {
                    diasMes.get(firstDay + i).setStyle("-fx-text-fill: grey;");
                }
            } else if (mes.getNumAno() < this.hoy[2]) {
                diasMes.get(firstDay + i).setStyle("-fx-text-fill: grey;");
            }
        }
    }

    private void appendToImages(ButtonImageView btn) {
        imagenes.getChildren().add(btn);
    }

    public void refreshSeries() {
        this.series = serieService.listSeries();
    }

    public void populateImages() {
        refreshSeries();
        System.out.println(this.series.size());

        for (Serie s : this.series) {
            ButtonImageView btnImgView = new ButtonImageView(s, this);
            appendToImages(btnImgView);
        }
    }

    public void addOneImage(Serie serie) {
        refreshSeries();
        ButtonImageView btn = new ButtonImageView(serie, this);
        appendToImages(btn);
    }

    public void populateMonth(Mes mes) {

    }

    @FXML
    private void handleButtonPressed(ActionEvent event) {
        Button btnClicked = (Button) event.getSource();
        String id = btnClicked.getId();

        switch (id) {
            case "next":
                if (mesActual.getNumMes() != 11) {
                    mesActual = new Mes(mesActual.getNumAno(), mesActual.getNumMes() + 1);
                } else {
                    mesActual = new Mes(mesActual.getNumAno() + 1, 0);
                }
                break;

            case "previous":
                if (mesActual.getNumMes() != 0) {
                    mesActual = new Mes(mesActual.getNumAno(), mesActual.getNumMes() - 1);
                } else {
                    mesActual = new Mes(mesActual.getNumAno() - 1, 11);
                }
                break;
        }

        fillMonth(mesActual);
        event.consume();
    }

    @FXML
    private void addNewSerie() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getClassLoader().getResource("views/NewSerieView.fxml"));
            root = loader.load();

            NewSerieController controller = loader.<NewSerieController>getController();
            controller.setMainController(this);

            Stage stage = new Stage();
            stage.setMinHeight(810);
            stage.setMinWidth(1115);
            stage.setResizable(false);
            stage.setTitle("Añade una nueva serie");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReturnToActual() {

    }

    @FXML
    private void handleAbout() {
        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.initStyle(StageStyle.UTILITY);
        about.setTitle("About");
        about.setHeaderText("Calendario Series v1.1");
        about.setContentText("Creado por Miguel Coleto."
                + "\nThe MIT License (MIT) Copyright (c) 2016 "
                + "\nhttps://github.com/Cotel/CalendarioSeries");

        about.showAndWait();
    }

    public SerieService getSerieService() {
        return serieService;
    }

    public Mes getMesActual() {
        return mesActual;
    }

    public TilePane getImagenes() {
        return imagenes;
    }
}
