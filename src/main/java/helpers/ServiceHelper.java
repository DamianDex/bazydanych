package helpers;

import dao.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import reports.CustomerReportImpl;
import reports.SalesReportImpl;
import service.*;

public class ServiceHelper {

    public CustomerReportServiceImpl getCustomerReportService() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        CustomerReportImpl bestCustomerReport = new CustomerReportImpl(sessionFactory);

        CustomerReportServiceImpl bestCustomerReportService = new CustomerReportServiceImpl();
        bestCustomerReportService.setCustomerReport(bestCustomerReport);

        return bestCustomerReportService;
    }

    public SalesReportServiceImpl getSalesReportService() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        SalesReportImpl salesReport = new SalesReportImpl();
        salesReport.setSessionFactory(sessionFactory);

        SalesReportServiceImpl salesReportService = new SalesReportServiceImpl();
        salesReportService.setSalesReport(salesReport);

        return salesReportService;
    }

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


    public OrdersServiceImpl getOrdersServiceImpl() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        OrdersDAOImpl ordersDAO = new OrdersDAOImpl(sessionFactory);

        OrdersServiceImpl ordersService = new OrdersServiceImpl();
        ordersService.setOrdersDAO(ordersDAO);

        return ordersService;
    }
}
