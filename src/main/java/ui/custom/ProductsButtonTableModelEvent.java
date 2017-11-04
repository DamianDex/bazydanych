package ui.custom;

import entities.Products;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class ProductsButtonTableModelEvent extends TableModelEvent {

    private Products product;

    public ProductsButtonTableModelEvent(TableModel source, Products product) {
        this(source);
        this.product = product;
    }

    public ProductsButtonTableModelEvent(TableModel source) {
        super(source);
    }

    public Products getProduct() {
        return product;
    }
}
