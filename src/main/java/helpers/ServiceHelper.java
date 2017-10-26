package helpers;

import dao.CategoriesDAOImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import service.CategoriesServiceImpl;

public class ServiceHelper {

    public CategoriesServiceImpl getCategoriesService() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        CategoriesDAOImpl categoriesDAO = new CategoriesDAOImpl();
        categoriesDAO.setSessionFactory(sessionFactory);

        CategoriesServiceImpl categoriesService = new CategoriesServiceImpl();
        categoriesService.setCategoriesDAO(categoriesDAO);

        return categoriesService;
    }
}
