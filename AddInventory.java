import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

/**
 * AddInventory extends {@link JPanel} and is a page that allows the user to input information on a new inventory item, which will then
 * update the group database ({@link Group63Database}).
 * 
 * @see Group63Database 
 * @see InventoryPage 
 * @see InventorySlice 
 * @author Kyle Moore
 */
public class AddInventory extends JPanel {
    
    private JTextField idField;
    private JTextField name;
    private JTextField quantity;
    private JTextField quantityType;
    private JTextField restockLevel;
    private JButton createButton;
    private JTable inventoryTable;
    private DefaultTableModel tableModel;

    /**
     * AddInventory() initializes a new page, this page will handle adding an inventory item to the database.
     * 
     * @see InventoryPage 
     * @see InventorySlice 
     */
    public AddInventory() {
        
        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight()));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        

        constraints.gridy++;
        constraints.gridx = 0;
        name = new JTextField(32);
        add(name, constraints);
        JLabel nameJLabel = new JLabel("Name");
        constraints.gridx = 1;
        add(nameJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        quantity = new JTextField(32);
        add(quantity, constraints);
        JLabel quantityLabel = new JLabel("Quantity");
        constraints.gridx = 1;
        add(quantityLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        quantityType = new JTextField(32);
        add(quantityType, constraints);
        JLabel quantityTypeLabel = new JLabel("Quantity Type");
        constraints.gridx = 1;
        add(quantityTypeLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        restockLevel = new JTextField(32);
        add(restockLevel, constraints);
        JLabel restockLabel = new JLabel("Restock Level");
        constraints.gridx = 1;
        add(restockLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        createButton = new JButton("Add Item");
        add(createButton, constraints);

        JLabel resultsLabel = new JLabel("");
        constraints.gridy++;
        add(resultsLabel, constraints);

        JButton backToSelectionButton = new JButton("Back to Inventory");
        constraints.gridy = 99;
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(InventoryPage.createScrollableInventoryPage());

            }

        });

        createButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
               
                String sname = name.getText() != null ? name.getText() : "NULL";
                double squantity = Double.parseDouble(quantity.getText().isEmpty() ? "0.0" : quantity.getText());
                String stype = quantityType.getText() != null ? quantityType.getText() : "NULL";
                double srestockLevel = Double.parseDouble(restockLevel.getText().isEmpty() ? "0.0" : restockLevel.getText());


                String sqlStatement = "INSERT INTO inventory (name, quantity, restock_level, quantity_type) VALUES ('" 
                + sname + "', " + squantity + ", " + srestockLevel + ", '" + stype + "');";


                Group63Database.insert(sqlStatement);

                loadInventoryTable();
                resultsLabel.setText( sname +" added successfully!");

               // GUI.switchPage(InventoryPage.createScrollableInventoryPage());
            }
        });

    tableModel = new DefaultTableModel();
    inventoryTable = new JTable(tableModel);

    JScrollPane scrollPane = new JScrollPane(inventoryTable);
    scrollPane.setPreferredSize(new Dimension(400, 400));
    constraints.gridx = 2;
    constraints.gridy = 0;
    constraints.gridheight = 7;
    add(scrollPane, constraints);

    loadInventoryTable();
    }
    
    private void loadInventoryTable() {
        // Clear the table before loading new data
        tableModel.setRowCount(0);

        // Set the table headers
        String[] columnNames = {"ID", "Name", "Quantity", "Restock Level", "Quantity Type"};
        tableModel.setColumnIdentifiers(columnNames);

        // Query the inventory data
        ArrayList<ArrayList<String>> inventoryData = Group63Database.query("SELECT * FROM inventory");

        // Populate the table with data
        if (inventoryData != null && inventoryData.size() > 1) {
            for (int i = 1; i < inventoryData.size(); i++) {
                ArrayList<String> row = inventoryData.get(i);
                tableModel.addRow(row.toArray());
            }
        }
    }
}