package service;

import entities.Products;

import java.util.List;

public interface ProductsService {

    public void addProduct(Products products);

    public void updateProduct(Products products);

    public List<Object[]> listProductsWithCategories(double minPrice, double maxPrice);

    public Products getProductById(int productid);

    public void removeProductById(int productid);
}
