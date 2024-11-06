import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Manager Select Page is a JPanel with tons of buttons to access different manager reports and graphs.
 * 
 * @author Anzal Khan, Jess Cadena, Kyle Moore
 */

public class ManagerSelectPage extends JPanel {
    /**
     * Constructor for the manager select page, a page with all the buttons for every manager report
     */
    public ManagerSelectPage() {

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Title (Manager Options) - Centered
        JLabel title = new JLabel("Manager Options");
        title.setHorizontalAlignment(SwingConstants.CENTER);  // Ensure text is centered within the label

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3; // Span across multiple columns to center it
        constraints.anchor = GridBagConstraints.CENTER; // Center it in the grid
        constraints.insets = new Insets(10, 10, 20, 10); // Optional: Add padding for better spacing
        add(title, constraints);

        // Graphs button
        JButton graphsButton = new JButton("Product Usage Chart");
        constraints.gridy = 1;
        constraints.gridwidth = 1; // Reset gridwidth to 1
        constraints.anchor = GridBagConstraints.CENTER; // Center button
        add(graphsButton, constraints);

        graphsButton.addActionListener(e -> GUI.switchPage(new GraphsPage()));

        // Employee List button
        JButton employeeListButton = new JButton("Employee List");
        constraints.gridy = 2;
        add(employeeListButton, constraints);

        employeeListButton.addActionListener(e -> GUI.switchPage(new EmployeeManagerPage()));

        // Stock button
        JButton stockButton = new JButton("All Tables");
        constraints.gridy = 3;
        add(stockButton, constraints);

        stockButton.addActionListener(e -> GUI.switchPage(new StockPage()));

        // Sales Report button
        JButton salesReportButton = new JButton("Sales Report");
        constraints.gridy = 4;
        add(salesReportButton, constraints);
        salesReportButton.addActionListener(e -> GUI.switchPage(new SalesReportPage()));

        // New button (similar to StockPage button)
        JButton xzReportButton = new JButton("X and Z Reports");
        constraints.gridy = 5;  // Adjust as needed
        // constraints.gridwidth = 1; // Adjust gridwidth if needed
        // constraints.insets = new Insets(20, 10, 10, 10); // Add padding if needed
        add(xzReportButton, constraints);
        xzReportButton.addActionListener(e -> GUI.switchPage(new XZReportsPage()));

        // Restock Report button
        JButton restockReportButton = new JButton("Restock Report");
        constraints.gridy = 6;
        add(restockReportButton, constraints);
        restockReportButton.addActionListener(e -> GUI.switchPage(new RestockReportPage()));


        JButton EditMenuButton = new JButton("Edit Menu");
        constraints.gridy = 7;
        add(EditMenuButton, constraints);
        EditMenuButton.addActionListener(e -> GUI.switchPage(ItemPage.createScrollableItemPage()));

        JButton EditInventoryButton = new JButton("Edit Inventory");
        constraints.gridy = 8;
        add(EditInventoryButton, constraints);
       // EditInventoryButton.addActionListener(e -> GUI.switchPage(new InventoryPage()));
        EditInventoryButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(InventoryPage.createScrollableInventoryPage());
            }
        });


        // Back button
        JButton backToSelectionButton = new JButton("Back to Home");
        constraints.gridy = 10;  // Adjust position below other buttons
        // constraints.gridwidth = 3; // Span across multiple columns to center it
        // constraints.insets = new Insets(20, 10, 10, 10); // Add padding to separate from other buttons
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new EmployeeOrManagerSelectionPage(true));
            }
        });
    }
}
