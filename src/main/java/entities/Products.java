package entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Products implements Serializable {

    @Id
    @GenericGenerator(name = "productsGenerator", strategy = "increment")
    @GeneratedValue(generator = "productsGenerator")
    private int productid;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Categories categories;

    private String productname;
    private int supplierid;
    private String quantityperunit;
    private double unitprice;
    private int unitsinstock;
    private int unitsonorder;
    private int reorderlevel;
    private int discontinued;

    public Products() {
    }

    public Products(String productname, int supplierid, String quantityperunit, double unitprice, int unitsinstock,
                    int unitsonorder, int reorderlevel, int discontinued) {
        this.productname = productname;
        this.supplierid = supplierid;
        this.quantityperunit = quantityperunit;
        this.unitprice = unitprice;
        this.unitsinstock = unitsinstock;
        this.unitsonorder = unitsonorder;
        this.reorderlevel = reorderlevel;
        this.discontinued = discontinued;
    }

    public Products(String productname, int supplierid, String quantityperunit, double unitprice, int unitsinstock,
                    int unitsonorder, int reorderlevel, int discontinued, Categories categories) {
        this.productname = productname;
        this.supplierid = supplierid;
        this.quantityperunit = quantityperunit;
        this.unitprice = unitprice;
        this.unitsinstock = unitsinstock;
        this.unitsonorder = unitsonorder;
        this.reorderlevel = reorderlevel;
        this.discontinued = discontinued;
        this.categories = categories;
    }

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

    public void setQuantityperunit(String quantityperunit) {
        this.quantityperunit = quantityperunit;
    }

    public String getQuantityperunit() {
        return quantityperunit;
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
                ", categories=" + categories +
                ", productname='" + productname + '\'' +
                ", supplierid=" + supplierid +
                ", quantityperunit='" + quantityperunit + '\'' +
                ", unitprice=" + unitprice +
                ", unitsinstock=" + unitsinstock +
                ", unitsonorder=" + unitsonorder +
                ", reorderlevel=" + reorderlevel +
                ", discontinued=" + discontinued +
                '}';
    }

    public String[] toArray() {
        String[] fields = {String.valueOf(productid), productname, String.valueOf(supplierid), quantityperunit,
                String.valueOf(unitprice), String.valueOf(unitsinstock), String.valueOf(unitsonorder),
                String.valueOf(reorderlevel), String.valueOf(discontinued), categories.toString()};
        return fields;
    }
}
