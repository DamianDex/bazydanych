package dao;

import entities.Products;

import java.util.List;

public interface ProductsDAO {

    public void addProduct(Products products);

    public void updateProduct(Products products);

    public List<Object[]> listProductsWithCategories(double minPrice, double maxPrice);

    public List<Products> listProductsByPrice(double minPrice, double maxPrice);

    public List<Products> listChoiceProducts(double minPrice, double maxPrice, int categoryId, int supplierId);

    public List<Products> listProductsByCategories(double minPrice, double maxPrice, int categoryId);

    public List<Products> listProductsBySupplier(double minPrice, double maxPrice, int supplierId);

    public List<Products> listProducts();

    public List<Products> listProductsByName(String nameStart);

    public Products getProductById(int productid);

    public void removeProductById(int productid);
}