package entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Categories implements Serializable {

    @Id
    @GenericGenerator(name = "categoriesGenerator", strategy = "increment")
    @GeneratedValue(generator = "categoriesGenerator")
    private int categoryId;

    private String categoryName;
    private String description;

    @OneToMany
    private Set<Products> products;

    public Categories() {
    }

    public Categories(String categoryName, String description) {
        this.categoryName = categoryName;
        this.description = description;
    }

    public Set<Products> getProducts() {
        return products;
    }

    public void setProducts(Set<Products> products) {
        this.products = products;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "categoryId=" + categoryId +
                ", categoryName='" + categoryName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String[] toArray() {
        String[] fields = {String.valueOf(categoryId), categoryName, description};
        return fields;
    }
}
