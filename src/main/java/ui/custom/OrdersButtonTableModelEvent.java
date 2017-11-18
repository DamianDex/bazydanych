package ui.custom;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class OrdersButtonTableModelEvent extends TableModelEvent {
    int row;

    public OrdersButtonTableModelEvent(TableModel source, int row) {
        this(source);
        this.row = row;
    }

    public OrdersButtonTableModelEvent(TableModel source) {
        super(source);
    }

    public int getRow() {
        return row;
    }
}
