package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import entities.Categories;
import helpers.ServiceHelper;
import service.CategoriesServiceImpl;

import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Comparator;
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
    private JPanel buttonPanel;

    private CategoriesServiceImpl categoriesService = new ServiceHelper().getCategoriesService();

    private static final String[] COLUMN_HEADINGS = {"Category Id", "Category Name", "Category Description"};
    private static final int INITIAL_ROW_NUMBER = 0;
    private static final String CATEGORY_CANT_REMOVE = "Category still used by Products";
    private static final String CATEGORY_NAME_LENGTH = "Category name should max length is 15";

    private Categories selectedCategory;


    public CategoriesPanel() {
        initUI();
        addAllActionListeners();
    }

    private void addAllActionListeners() {
        addActionListenerToReadAllBtn();
        addActionListenerToTableRowSelection();
        addActionListenerToUpdateBtn();
        addActionListenerToAddNewBtn();
        addActionListenerToDeleteBtn();
        addActionListenerToCategoryNameTextField();
    }

    private void addActionListenerToCategoryNameTextField() {
        categoryName.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (categoryName.getText().length() > 15)
                    showErrorDialog(CATEGORY_NAME_LENGTH);
            }
        });
    }

    private void addActionListenerToDeleteBtn() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    categoriesService.removeCategoryById(selectedCategory.getCategoryId());
                } catch (PersistenceException ex) {
                    showErrorDialog(CATEGORY_CANT_REMOVE);
                }
                pushDataFromDbToTable();
            }
        });
    }

    private void showErrorDialog(String errorMsg) {
        JOptionPane.showMessageDialog(new JFrame(), errorMsg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void addActionListenerToAddNewBtn() {
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String categoryToAddName = categoryName.getText();
                String categoryToAddDescription = categoryDescription.getText();
                Categories categoryToAdd = new Categories(categoryToAddName, categoryToAddDescription);
                categoriesService.addCategory(categoryToAdd);
                pushDataFromDbToTable();
            }
        });
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

        //FIXME: Maybe should be sorted in query ???
        categoriesList.sort(new Comparator<Categories>() {
            @Override
            public int compare(Categories c1, Categories c2) {
                if (c1.getCategoryId() > c2.getCategoryId())
                    return 1;
                else
                    return -1;
            }
        });

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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        categoriesPanel = new JPanel();
        categoriesPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        categoriesPanel.add(buttonPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        readAllButton = new JButton();
        readAllButton.setText("Read All");
        buttonPanel.add(readAllButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addNewButton = new JButton();
        addNewButton.setText("Add New");
        buttonPanel.add(addNewButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Update");
        buttonPanel.add(updateButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        buttonPanel.add(deleteButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        categoriesPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder("Current selection"));
        final JLabel label1 = new JLabel();
        label1.setText("Category Description");
        panel1.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryDescription = new JTextField();
        panel1.add(categoryDescription, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Category Name");
        panel1.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryName = new JTextField();
        panel1.add(categoryName, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        categoriesPanel.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder("Categories"));
        categoriesTable = new JTable();
        scrollPane1.setViewportView(categoriesTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return categoriesPanel;
    }
}
