package ui.custom;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ProductsButtonTableEditor extends AbstractCellEditor implements TableCellEditor {

    private JButton button;
    private Boolean editorValue;
    private String name;
    private Color color;


    public ProductsButtonTableEditor(String name, Color color) {
        button = new JButton();
        this.name = name;
        this.color = color;
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
        button.setText(name);
        button.setForeground(color);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }
}
