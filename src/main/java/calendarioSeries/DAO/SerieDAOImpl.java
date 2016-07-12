package calendarioSeries.DAO;

import calendarioSeries.models.Serie;
import calendarioSeries.utils.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by Cotel on 10/07/2016.
 */
public class SerieDAOImpl implements SerieDAO {

    private EntityManager manager;

    private void start() {
        manager = HibernateUtil.openSession();
        manager.getTransaction().begin();
    }

    private void finish() {
        manager.getTransaction().commit();
        manager.close();
    }

    @Override
    public void addSerie(Serie serie) {
        this.start();
        manager.persist(serie);
        this.finish();
    }

    @Override
    public Serie getById(String id) {
        this.start();
        Serie res = manager.find(Serie.class, id);
        this.finish();
        return res;
    }

    @Override
    public List<Serie> getAll() {
        this.start();
        List<Serie> list = (List<Serie>) manager.createQuery("from Serie").getResultList();
        this.finish();
        return list;
    }

    @Override
    public void update(Serie serie) {
        this.start();
        manager.merge(serie);
        this.finish();
    }

    @Override
    public void delete(Serie serie) {
        this.start();
        Serie s = manager.merge(serie);
        manager.remove(s);
        this.finish();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Operation not supported");
    }
}
