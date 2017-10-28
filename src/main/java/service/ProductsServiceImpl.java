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

    public List<Object> listProductsWithCategories(double minPrice, double maxPrice) {
        return productsDAO.listProductsWithCategories(minPrice, maxPrice);
    }

    public Products getProductById(int productid) {
        return productsDAO.getProductById(productid);
    }

    public void removeProductById(int productid) {
        productsDAO.removeProductById(productid);
    }
}
