package calendarioSeries.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by Cotel on 10/07/2016.
 */
public class HibernateUtil {

    private static final EntityManagerFactory emf;

    static {
        emf = Persistence.createEntityManagerFactory("Repository");
    }

    public static EntityManager openSession() {
        return emf.createEntityManager();
    }

}
