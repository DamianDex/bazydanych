package service;

import dao.ProductsDAO;
import entities.Products;

import java.util.List;

public class ProductsServiceImpl implements ProductsService {

    ProductsDAO productsDAO;

    public void setProductsDAO(ProductsDAO productsDAO) {
        this.productsDAO = productsDAO;
    }

    public void addProduct(Products products) {
        productsDAO.addProduct(products);
    }

    public void updateProduct(Products products) {
        productsDAO.updateProduct(products);
    }

    public List<Object[]> listProductsWithCategories(double minPrice, double maxPrice) {
        return productsDAO.listProductsWithCategories(minPrice, maxPrice);
    }

    public List<Products> listProductsByPrice(double minPrice, double maxPrice) {
        return productsDAO.listProductsByPrice(minPrice, maxPrice);
    }

    public List<Products> listProducts() {
        return productsDAO.listProducts();
    }

    public List<Products> listChoiceProducts(double minPrice, double maxPrice, int categoryId, int supplierId){
        return productsDAO.listChoiceProducts(minPrice,maxPrice,categoryId,supplierId);
    }

    public List<Products> listProductsByCategories(double minPrice, double maxPrice, int categoryId){
     return productsDAO.listProductsByCategories(minPrice,maxPrice,categoryId);
    }

    public List<Products> listProductsBySupplier(double minPrice, double maxPrice, int supplierId){
        return productsDAO.listProductsBySupplier(minPrice,maxPrice,supplierId);
    }

    @Override
    public List<Products> listProductsByName(String nameStart) {
        return productsDAO.listProductsByName(nameStart);
    }

    public Products getProductById(int productid) {
        return productsDAO.getProductById(productid);
    }

    public void removeProductById(int productid) {
        productsDAO.removeProductById(productid);
    }
}
