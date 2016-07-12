package calendarioSeries.controllers;

import calendarioSeries.controls.ButtonImageView;
import calendarioSeries.models.Serie;
import calendarioSeries.utils.URLReader;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cotel on 12/07/2016.
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

    public void initialize() {

    }

    private void addAndClose(MouseEvent event) {
        ButtonImageView source = (ButtonImageView) event.getSource();

        mainController.getSerieService().addSerie(source.getSerie());
        mainController.addOneImage(source.getSerie());
        mainController.populateMonth(mainController.getMesActual());

        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void appendToResults(ButtonImageView btn) {
        resultados.getChildren().add(btn);
    }

    @FXML
    public void handleOk() {
        String title = titulo.getText().replaceAll(" ", "+").toLowerCase();
        String aux = URLReader.readUrl(BASE + title + "&type=series" + "&r=json");
        resultados.getChildren().clear();
        try {
            JSONObject search = new JSONObject(aux);

            if (search.getString("Response").equals("True")) {
                JSONArray res = search.getJSONArray("Search");

                List<Serie> list = new ArrayList<>();
                for (int i = 0; i < res.length(); i++) {
                    JSONObject current = new JSONObject(res.get(i).toString());
                    Serie s = new Serie(current.getString("imdbID"));
                    list.add(s);
                }

//                resultados.setPrefRows(list.size());
                for (Serie s : list) {
                    ButtonImageView btnImageView = new ButtonImageView(s, mainController);
                    btnImageView.setOnMouseClicked(this::addAndClose);
                    appendToResults(btnImageView);
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void setMainController(MainViewController mainController) {
        this.mainController = mainController;
    }

}
