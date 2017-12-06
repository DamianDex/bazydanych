package entities;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class Products implements Serializable {

    @Id
    @GenericGenerator(name = "productsGenerator", strategy = "increment")
    @GeneratedValue(generator = "productsGenerator")
    @Column(name = "productid")
    private int productid;

    @ManyToOne
    @JoinColumn(name = " supplierId")
    private Suppliers suppliers;

    @ManyToOne
    @JoinColumn(name = " categoryId")
    private Categories categories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.products", cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    private Set<OrderDetails> orderDetails;

    private String productname;
    private String quantityperunit;
    private double unitprice;
    private int unitsinstock;
    private int unitsonorder;
    private int reorderlevel;
    private int discontinued;

    public Products() {
    }

    public Products(String productname, String quantityperunit, double unitprice, int unitsinstock,
                    int unitsonorder, int reorderlevel, int discontinued) {
        this.productname = productname;
        this.quantityperunit = quantityperunit;
        this.unitprice = unitprice;
        this.unitsinstock = unitsinstock;
        this.unitsonorder = unitsonorder;
        this.reorderlevel = reorderlevel;
        this.discontinued = discontinued;
    }

    public Products(String productname, String quantityperunit, double unitprice, int unitsinstock,
                    int unitsonorder, int reorderlevel, int discontinued, Categories categories, Suppliers suppliers) {
        this.productname = productname;
        this.suppliers = suppliers;
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

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Suppliers getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(Suppliers suppliers) {
        this.suppliers = suppliers;
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

    public Set<OrderDetails> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Set<OrderDetails> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        String catName = (categories != null) ? categories.getCategoryName() : "";
        return "Products{" +
                "productid=" + productid +
                ", productname='" + productname + '\'' +
                ", categories=" + catName +
                ", suppliers=" + suppliers.getCompanyName() +
                ", quantityperunit='" + quantityperunit + '\'' +
                ", unitprice=" + unitprice +
                ", unitsinstock=" + unitsinstock +
                ", unitsonorder=" + unitsonorder +
                ", reorderlevel=" + reorderlevel +
                ", discontinued=" + discontinued +
                '}';
    }

    public String[] toArray() {
        String[] fields = {String.valueOf(productid), productname, suppliers.getCompanyName(),
                categories.getCategoryName(), quantityperunit, String.valueOf(unitprice),
                String.valueOf(unitsinstock), String.valueOf(unitsonorder), String.valueOf(reorderlevel),
                String.valueOf(discontinued)};
        return fields;
    }
}