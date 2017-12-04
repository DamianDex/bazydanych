package reports;

import dao.CategoriesDAOImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.List;

public class SalesReportImpl implements SalesReport {
    private static final Logger LOGGER = LoggerFactory.logger(CategoriesDAOImpl.class);
    private SessionFactory sessionFactory;

    private static final String[] columnNames= {"Year", "Month", "Sales value"};

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Object[]> generateReport() {
        Session session = prepareSession();

        String queryString = "select year(o.orderDate), month(o.orderDate), sum(od.unitprice * od.quantity) " +
                "from Orders o " +
                "join OrderDetails od " +
                "on o.orderId = od.pk.orders.orderId " +
                "group by year(o.orderDate), month(o.orderDate) " +
                "order by year(o.orderDate), month(o.orderDate)";

        Query query = session.createQuery(queryString);

        List<Object[]> reportByYearList = query.list();
        for (Object[] reportByMonth : reportByYearList) {
            LOGGER.info("Report by Month: " + reportByMonth[0] + "  " + reportByMonth[1]);
        }
        finishSession(session);
        return reportByYearList;
    }

    private void finishSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }

    private Session prepareSession() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }
}
