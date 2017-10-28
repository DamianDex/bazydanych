package entities;

import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Products implements Serializable {

    @Id
    @GenericGenerator(name = "productsGenerator", strategy = "increment")
    @GeneratedValue(generator = "productsGenerator")
    private int productid;

    @ManyToOne
    @JoinColumn(name="categoryId", nullable = false)
    private Categories categories;

    private String productname;
    private int supplierid;
    private int categoryid;
    private String quantityperunit;
    private double unitprice;
    private int unitsinstock;
    private int unitsonorder;
    private int reorderlevel;
    private int discontinued;

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public int getSupplierid() {
        return supplierid;
    }

    public void setSupplierid(int supplierid) {
        this.supplierid = supplierid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getQuantityperunit() {
        return quantityperunit;
    }

    public void setQuantityperunit(String quantityperunit) {
        this.quantityperunit = quantityperunit;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public int getUnitsinstock() {
        return unitsinstock;
    }

    public void setUnitsinstock(int unitsinstock) {
        this.unitsinstock = unitsinstock;
    }

    public int getUnitsonorder() {
        return unitsonorder;
    }

    public void setUnitsonorder(int unitsonorder) {
        this.unitsonorder = unitsonorder;
    }

    public int getReorderlevel() {
        return reorderlevel;
    }

    public void setReorderlevel(int reorderlevel) {
        this.reorderlevel = reorderlevel;
    }

    public int getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(int discontinued) {
        this.discontinued = discontinued;
    }

    @Override
    public String toString() {
        return "Products{" +
                "productid=" + productid +
                ", productname='" + productname + '\'' +
                ", supplierid=" + supplierid +
                ", categoryid=" + categoryid +
                ", quantityperunit='" + quantityperunit + '\'' +
                ", unitprice=" + unitprice +
                ", unitsinstock=" + unitsinstock +
                ", unitsonorder=" + unitsonorder +
                ", reorderlevel=" + reorderlevel +
                ", discontinued=" + discontinued +
                '}';
    }

    public String[] toArray() {
        String[] fields = {String.valueOf(categoryid), productname, String.valueOf(supplierid), String.valueOf(categoryid),
                quantityperunit, String.valueOf(unitprice), String.valueOf(unitsinstock), String.valueOf(unitsonorder),
                    String.valueOf(reorderlevel), String.valueOf(discontinued)};
        return fields;
    }
}
