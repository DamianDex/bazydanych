package ui;

import dao.CategoriesDAOImpl;
import entities.Categories;
import helpers.ServiceHelper;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import service.CategoriesServiceImpl;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CategoriesPanel extends JPanel {
    private JPanel categoriesPanel;
    private JTable categoriesTable;
    private JTextField categoryDescription;
    private JTextField categoryName;
    private JTextField categoryId;
    private JButton readAllButton;
    private JButton addNewButton;
    private JButton updateButton;
    private JButton deleteButton;

    private CategoriesServiceImpl categoriesService = new ServiceHelper().getCategoriesService();

    private static final String[] COLUMN_HEADINGS = {"Category Id","Category Name", "Category Description"};
    private static final int INITIAL_ROW_NUMBER = 0;

    private Categories selectedCategory;


    public CategoriesPanel() {

        initUI();
        addAllActionListeners();

    }

    private void addAllActionListeners() {
        addActionListenerToReadAllBtn();
        addActionListenerToTableRowSelection();
        addActionListenerToUpdateBtn();
    }

    private void addActionListenerToReadAllBtn() {
        readAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                pushDataFromDbToTable();
            }
        });
    }

    private void addActionListenerToUpdateBtn() {
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                selectedCategory.setCategoryId(Integer.parseInt(categoryId.getText()));
                selectedCategory.setCategoryName(categoryName.getText());
                selectedCategory.setDescription(categoryDescription.getText());
                categoriesService.updateCategory(selectedCategory);
                pushDataFromDbToTable();
            }
        });
    }

    private void pushDataFromDbToTable() {
        List<Categories> categoriesList = categoriesService.listCategories();
        DefaultTableModel tableModel = (DefaultTableModel) categoriesTable.getModel();
        tableModel.setRowCount(0); //clear JTable

        for (Categories category : categoriesList) {
            tableModel.addRow(category.toArray());
        }
    }

    private void addActionListenerToTableRowSelection() {
        ListSelectionModel cellSelectionModel = categoriesTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Integer id = Integer.valueOf(categoriesTable.getValueAt(categoriesTable.getSelectedRow(), 0).toString());
                    selectedCategory = categoriesService.getCategoryById(id);
                    categoryId.setText(String.valueOf(selectedCategory.getCategoryId()));
                    categoryName.setText(selectedCategory.getCategoryName());
                    categoryDescription.setText(selectedCategory.getDescription());
                } catch (Exception ex) {
                    //do nothing
                }
            }
        });
    }

    private void initEmptyCategoriesTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        categoriesTable.setModel(model);
        categoriesTable.setRowSelectionAllowed(true);
        categoriesTable.setCellSelectionEnabled(false);
    }

    private void initUI() {
        initEmptyCategoriesTable();
    }
}
