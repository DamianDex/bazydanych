package dao;


import entities.Categories;

import java.util.List;

public interface CategoriesDAO {
    public void addCategory(Categories categories);

    public void updateCategory(Categories categories);

    public List<Categories> listCategories();

    public Categories getCategoryById(int id);

    public void removeCategoryById(int id);
}
