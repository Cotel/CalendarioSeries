package calendarioSeries.services;

import calendarioSeries.models.Serie;

import java.util.List;

/**
 * Created by Cotel on 10/07/2016.
 */
public interface SerieService {

    public void addSerie(Serie serie);

    public Serie getSerie(String id);

    public List<Serie> listSeries();

    public void removeSerie(Serie serie);

    public void updateSerie(Serie serie);

}
