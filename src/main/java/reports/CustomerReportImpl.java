package reports;

import dao.CategoriesDAOImpl;
import helpers.SessionHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.List;

public class CustomerReportImpl implements CustomerReport{
    private static final Logger LOGGER = LoggerFactory.logger(CategoriesDAOImpl.class);
    private static final String[] columnNames = {"Year", "Month", "Customer Name", "Sales value"};
    private final SessionHelper sessionHelper;

    public CustomerReportImpl(SessionFactory sessionFactory) {
        sessionHelper = new SessionHelper(sessionFactory);
    }

    @Override
    public List<Object[]> generateCustomerReport(String companyName) {
        Session session = sessionHelper.prepareSession();

        String queryString = "select year(o.orderDate), month(o.orderDate), c.companyname, sum(od.unitprice * od.quantity) " +
                "from Orders o " +
                "join OrderDetails od " +
                "on o.orderId = od.pk.orders.orderId " +
                "join Customers c " +
                "on o.customers.customerid = c.customerid " +
                "where c.companyname = :companyName " +
                "group by year(o.orderDate), month(o.orderDate), c.companyname " +
                "order by year(o.orderDate), month(o.orderDate), sum(od.unitprice * od.quantity) desc ";

        Query query = session.createQuery(queryString);
        query.setParameter("companyName", companyName);
        List<Object[]> reportByYearList = query.list();
        for (Object[] reportByMonth : reportByYearList) {
            LOGGER.info("Report by Month: " + reportByMonth[0] + "  " + reportByMonth[1]);
        }
        sessionHelper.finishSession(session);
        return reportByYearList;
    }
}
