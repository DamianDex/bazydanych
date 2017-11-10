package helpers;

import dao.CategoriesDAOImpl;
import dao.CustomersDAOImpl;
import dao.ProductsDAOImpl;
import dao.SuppliersDAOImpl;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import service.CategoriesServiceImpl;
import service.CustomersServiceImpl;
import service.ProductsServiceImpl;
import service.SuppliersServiceImpl;

public class ServiceHelper {

    public CategoriesServiceImpl getCategoriesService() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        CategoriesDAOImpl categoriesDAO = new CategoriesDAOImpl();
        categoriesDAO.setSessionFactory(sessionFactory);

        CategoriesServiceImpl categoriesService = new CategoriesServiceImpl();
        categoriesService.setCategoriesDAO(categoriesDAO);

        return categoriesService;
    }

    public ProductsServiceImpl getProductsService() {

        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        ProductsDAOImpl productsDAO = new ProductsDAOImpl();
        productsDAO.setSessionFactory(sessionFactory);

        ProductsServiceImpl productsService = new ProductsServiceImpl();
        productsService.setProductsDAO(productsDAO);

        return productsService;
    }

    public SuppliersServiceImpl getSuppliersService() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        SuppliersDAOImpl suppliersDAO = new SuppliersDAOImpl();
        suppliersDAO.setSessionFactory(sessionFactory);

        SuppliersServiceImpl suppliersService = new SuppliersServiceImpl();
        suppliersService.setSuppliersDAO(suppliersDAO);

        return suppliersService;
    }

    public CustomersServiceImpl getCustomersServiceImpl() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        CustomersDAOImpl customersDAO = new CustomersDAOImpl(sessionFactory);

        CustomersServiceImpl customersService = new CustomersServiceImpl();
        customersService.setCustomersDAO(customersDAO);

        return customersService;
    }
}
