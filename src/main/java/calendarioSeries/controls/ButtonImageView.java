package calendarioSeries.controls;

import calendarioSeries.MainApp;
import calendarioSeries.controllers.MainViewController;
import calendarioSeries.models.Serie;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * Created by Cotel on 12/07/2016.
 */
public class ButtonImageView extends StackPane {

    private ImageView imageView;
    private Text text;
    private Serie serie;
    private MainViewController controller;

    public ButtonImageView(Serie serie, MainViewController controller) {
        this.imageView = new ImageView();
        this.controller = controller;
        this.serie = serie;
        this.imageView.setFitHeight(300);
        this.imageView.setFitWidth(210);

        if (!serie.getPoster().equals("N/A")) {
            Image img = new Image(serie.getPoster());
            this.imageView.setImage(img);
            this.text = new Text("");
        } else {
            Image img = new Image(MainApp.class.getClassLoader().getResourceAsStream("images/no-image.png"));
            this.imageView.setImage(img);
            this.text = new Text(this.serie.getTitle());
        }

        this.setOnMouseClicked(event -> {
            onMouseClicked(event);
        });

        ContextMenu menu = new ContextMenu();
        MenuItem delete = new MenuItem("Eliminar");
        delete.setOnAction(event -> {
            this.controller.getSerieService().removeSerie(this.serie);
            this.controller.populateMonth(this.controller.getMesActual());
            this.controller.refreshSeries();

            ObservableList<Node> list = this.controller.getImagenes().getChildren();
            list.remove(list.indexOf(this));

            event.consume();
        });
        menu.getItems().add(delete);

        this.setOnContextMenuRequested(event -> {
            menu.show(this, event.getScreenX(), event.getScreenY());
            event.consume();
        });

        this.getChildren().addAll(imageView, text);
    }

    protected void onMouseClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
            System.out.println(this.serie.toJson());
    }

    public Serie getSerie() {
        return serie;
    }
}
