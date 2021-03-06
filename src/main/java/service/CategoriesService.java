package service;

import entities.Categories;

import java.util.List;

public interface CategoriesService {

    public void addCategory(Categories categories);

    public void updateCategory(Categories categories);

    public List<Categories> listCategories();

    public Categories getCategoryById(int id);

    public void removeCategoryById(int id);
}
