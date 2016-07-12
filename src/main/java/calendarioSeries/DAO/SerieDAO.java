package calendarioSeries.DAO;

import calendarioSeries.models.Serie;

import java.util.List;

/**
 * Created by Cotel on 10/07/2016.
 */
public interface SerieDAO {

    public void addSerie(Serie serie);

    public Serie getById(String id);

    public List<Serie> getAll();

    public void update(Serie serie);

    public void delete(Serie serie);

    public void deleteAll();


}
