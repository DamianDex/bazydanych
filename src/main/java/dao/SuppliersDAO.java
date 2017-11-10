package dao;

import entities.Suppliers;

import java.util.List;

public interface SuppliersDAO {

    public void addSupplier(Suppliers supppliers);

    public void updateSupplier(Suppliers suppliers);

    public List<Suppliers> listSuppliers();

    public Suppliers getSupplierById(int supplierId);

    public void removeSupplierById(int supplierId);


}

