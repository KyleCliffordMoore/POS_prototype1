import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;

/**
 * EditInventory class creates a JPanel that allows the user to edit an item in the inventory.
 * The user can enter the name, quantity, quantity type, and restock level of the item and submit it to the database.
 * The user can also view the list of items in the inventory and go back to the inventory page.
 */
public class EditInventory extends JPanel {

    private int inventoryID;

    private JTextField idField;
    private JTextField name;
    private JTextField quantity;
    private JTextField quantityType;
    private JTextField restockLevel;
    private JButton createButton;
    private JButton editButton;

    public EditInventory(int inventoryID) {
        this.inventoryID = inventoryID;

        ArrayList<String> inventoryRow = Group63Database.query("SELECT * FROM inventory WHERE inventory_id = " + inventoryID + ";").get(1);

        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight()));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel idJLabel = new JLabel("id, leave blank to generate new one ");
        idField = new JTextField(inventoryRow.get(0),32);
        add(idField, constraints);
        constraints.gridx = 1;
        add(idJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        name = new JTextField(inventoryRow.get(1), 32);
        add(name, constraints);
        JLabel nameJLabel = new JLabel("name");
        constraints.gridx = 1;
        add(nameJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        quantity = new JTextField(inventoryRow.get(2), 32);
        add(quantity, constraints);
        JLabel quantityLabel = new JLabel("Quantity");
        constraints.gridx = 1;
        add(quantityLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        quantityType = new JTextField(inventoryRow.get(4), 32);
        add(quantityType, constraints);
        JLabel quantityTypeLabel = new JLabel("Quantity Type");
        constraints.gridx = 1;
        add(quantityTypeLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        restockLevel = new JTextField(inventoryRow.get(3), 32);
        add(restockLevel, constraints);
        JLabel restockLabel = new JLabel("Restock Level");
        constraints.gridx = 1;
        add(restockLabel, constraints);
        
        constraints.gridy++;
        constraints.gridx = 0;
        editButton = new JButton("Edit Item");
        add(editButton, constraints);


        JButton backToSelectionButton = new JButton("Back to Inventory");
        constraints.gridy = 99;
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(InventoryPage.createScrollableInventoryPage());

            }

        });


        editButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                int id = -1;

                String textId = idField.getText();

                if (textId == null || textId.length() == 0) {
                    id = inventoryID;
                } else {
                    id = Integer.parseInt(textId);
                }

                String sname = name.getText() != null ? name.getText() : "NULL";
                double squantity = Double.parseDouble(quantity.getText().isEmpty() ? "0.0" : quantity.getText());
                String stype = quantityType.getText() != null ? quantityType.getText() : "NULL";
                double srestockLevel = Double.parseDouble(restockLevel.getText().isEmpty() ? "0.0" : restockLevel.getText());

                String sqlStatement = 
                "UPDATE inventory SET inventory_id = " + id +
                ", name = '" + sname + "', quantity = " + squantity + 
                ", quantity_type = '" + stype + "', restock_level = " + srestockLevel + 
                " WHERE inventory_id = " + inventoryID + ";";

                System.out.println(sqlStatement);

                Group63Database.update(sqlStatement);

                GUI.switchPage(InventoryPage.createScrollableInventoryPage());
            }
        });
    }

}