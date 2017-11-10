package service;

import entities.Suppliers;

import java.util.List;

public interface SuppliersService {

    public void addSupplier(Suppliers supppliers);

    public void updateSupplier(Suppliers suppliers);

    public List<Suppliers> listSuppliers();

    public Suppliers getSupplierById(int supplierId);

    public void deletesupplier(int supplierId);

}
