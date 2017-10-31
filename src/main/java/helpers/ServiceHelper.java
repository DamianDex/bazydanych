package helpers;

import dao.CategoriesDAOImpl;
import dao.CustomersDAOImpl;
import dao.ProductsDAO;
import dao.ProductsDAOImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import service.CategoriesServiceImpl;
import service.CustomersServiceImpl;
import service.ProductsServiceImpl;

public class ServiceHelper {

    public CategoriesServiceImpl getCategoriesService() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        CategoriesDAOImpl categoriesDAO = new CategoriesDAOImpl();
        categoriesDAO.setSessionFactory(sessionFactory);

        CategoriesServiceImpl categoriesService = new CategoriesServiceImpl();
        categoriesService.setCategoriesDAO(categoriesDAO);

        return categoriesService;
    }

    public ProductsServiceImpl getProductsService(){

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        ProductsDAOImpl productsDAO = new ProductsDAOImpl();
        productsDAO.setSessionFactory(sessionFactory);

        ProductsServiceImpl productsService = new ProductsServiceImpl();
        productsService.setProductsDAO(productsDAO);

        return productsService;
    }

    public CustomersServiceImpl getCustomersServiceImpl(){
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        CustomersDAOImpl customersDAO = new CustomersDAOImpl(sessionFactory);

        CustomersServiceImpl customersService = new CustomersServiceImpl();
        customersService.setCustomersDAO(customersDAO);

        return customersService;
    }
}
