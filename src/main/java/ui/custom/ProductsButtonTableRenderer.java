package ui.custom;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProductsButtonTableRenderer extends JButton implements TableCellRenderer {

    private String name;
    private Color color;


    public ProductsButtonTableRenderer(String name, Color color) {
        setOpaque(true);
        this.name = name;
        this.color = color;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setForeground(table.getSelectionForeground());
        setBackground(table.getSelectionBackground());
        setText(name);
        setForeground(color);
        setFont(new Font("Arial", Font.PLAIN, 20));

        return this;
    }
}
