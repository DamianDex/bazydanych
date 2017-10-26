package ui;


import javax.swing.*;

public class MainPanel {

    private JPanel mainPanel;
    private JTabbedPane tabsPanel;
    private JPanel categoriesTab;

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Bazy danych");
        MainPanel mainPanel = new MainPanel();
        jFrame.setContentPane(mainPanel.mainPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setVisible(true);
        jFrame.setSize(600, 600);

        mainPanel.initUI();
    }

    private void initUI() {
    }
}