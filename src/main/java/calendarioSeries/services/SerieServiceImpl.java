package calendarioSeries.services;

import calendarioSeries.DAO.SerieDAO;
import calendarioSeries.DAO.SerieDAOImpl;
import calendarioSeries.models.Serie;

import java.util.List;

/**
 * Created by Cotel on 10/07/2016.
 */
public class SerieServiceImpl implements SerieService {

    private SerieDAO serieDAO = new SerieDAOImpl();

    @Override
    public void addSerie(Serie serie) {
        serieDAO.addSerie(serie);
    }

    @Override
    public Serie getSerie(String id) {
        return serieDAO.getById(id);
    }

    @Override
    public List<Serie> listSeries() {
        return serieDAO.getAll();
    }

    @Override
    public void removeSerie(Serie serie) {
        serieDAO.delete(serie);
    }

    @Override
    public void updateSerie(Serie serie) {
        serieDAO.update(serie);
    }
}
