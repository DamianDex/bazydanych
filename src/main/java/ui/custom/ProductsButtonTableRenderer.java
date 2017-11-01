package ui.custom;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProductsButtonTableRenderer extends JButton implements TableCellRenderer {

    public ProductsButtonTableRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setForeground(table.getSelectionForeground());
        setBackground(table.getSelectionBackground());
        setText("New Order");

        return this;
    }
}
