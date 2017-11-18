package ui.custom;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

public class TotalPriceChangedTableModelEvent extends TableModelEvent {
    public TotalPriceChangedTableModelEvent(TableModel source) {
        super(source);
    }
}
