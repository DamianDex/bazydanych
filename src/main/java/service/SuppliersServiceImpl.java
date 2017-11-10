package service;

import dao.SuppliersDAO;
import entities.Suppliers;

import java.util.List;

public class SuppliersServiceImpl implements SuppliersService {

    private SuppliersDAO suppliersDAO;

    public void setSuppliersDAO(SuppliersDAO suppliersDAO) {
        this.suppliersDAO = suppliersDAO;
    }

    public void addSupplier(Suppliers supppliers) {
        suppliersDAO.addSupplier(supppliers);
    }

    public void updateSupplier(Suppliers suppliers) {
        suppliersDAO.updateSupplier(suppliers);
    }

    public List<Suppliers> listSuppliers() {
        return suppliersDAO.listSuppliers();
    }

    public Suppliers getSupplierById(int supplierId) {
        return suppliersDAO.getSupplierById(supplierId);
    }

    public void deletesupplier(int supplierId) {
        suppliersDAO.removeSupplierById(supplierId);
    }
}
