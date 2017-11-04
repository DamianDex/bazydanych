package ui.custom;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProductsButtonTableEditor extends AbstractCellEditor implements TableCellEditor {

    private JButton button;
    private Boolean editorValue;

    public ProductsButtonTableEditor() {
        button = new JButton();
        //button.setOpaque(true);
        button.addActionListener((ActionEvent e) -> {
            editorValue = true;
            fireEditingStopped();
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(table.getBackground());
        }
        button.setText("New Order");
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }
}
