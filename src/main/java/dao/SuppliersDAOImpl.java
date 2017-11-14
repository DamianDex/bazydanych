package dao;

import entities.Suppliers;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import java.util.List;

public class SuppliersDAOImpl implements SuppliersDAO {

    private static final Logger LOGGER = LoggerFactory.logger(SuppliersDAOImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addSupplier(Suppliers suppliers) {
        Session session = prepareSession();
        session.save(suppliers);
        LOGGER.info("Supplier saved successfully, Supplier details = " + suppliers);
        finishSession(session);
    }

    public void updateSupplier(Suppliers suppliers) {
        Session session = prepareSession();
        session.update(suppliers);
        LOGGER.info("Supplier updated successfully, Supplier Details = " + suppliers);
        finishSession(session);
    }

    public List<Suppliers> listSuppliers() {
        Session session = prepareSession();
        List<Suppliers> suppliersList = session.createQuery("from Suppliers").list();
        for (Suppliers supplier : suppliersList) {
            LOGGER.info("Supplier List: " + supplier);
        }
        finishSession(session);
        return suppliersList;
    }

    public Suppliers getSupplierById(int supplierId) {
        Session session = prepareSession();
        Suppliers suppliers = session.load(Suppliers.class, new Integer(supplierId));
        LOGGER.info("Supplier loaded successfully, Supplier details = " + suppliers);
        finishSession(session);
        return suppliers;
    }

    public void removeSupplierById(int supplierId) {
        Session session = prepareSession();
        Suppliers supplier = session.load(Suppliers.class, new Integer(supplierId));
        if (supplier != null) {
            session.delete(supplier);
        }
        LOGGER.info("Supplier deleted successfully, Supplier details = " + supplier);
        finishSession(session);
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
