import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * AddNewItemPage class creates a JPanel that allows the user to add a new item to the menu.
 * The user can enter the name, type, and price of the item and submit it to the database.
 * The user can also view the list of items in the menu and remove an item from the menu.
 * @author Jess Cadena
 * @author Tanuj Narnam
 */

public class AddNewItemPage extends JPanel {

    private JTextField nameTextField;
    private JTextField priceTextField;
    private JComboBox<String> typeComboBox;
    private JButton submitButton;
    private JLabel resultLabel;
    private JList<String> ItemList;
    private DefaultListModel<String> listModel;
    private JButton removeButton;


    // Constructor for AddNewItemPage
    public AddNewItemPage() {
        // Set layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label for Product Name
        JLabel nameLabel = new JLabel("Product Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(nameLabel, gbc);

        // TextField for entering Product Name
        nameTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(nameTextField, gbc);

        // Label for Product Type
        JLabel typeLabel = new JLabel("Product Type:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(typeLabel, gbc);

        // ComboBox for selecting Product Type
        String[] productTypes = {"Entree", "Side", "Drink", "Appetizer"};
        typeComboBox = new JComboBox<>(productTypes);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(typeComboBox, gbc);

         // Label for Product price
         JLabel priceLabel = new JLabel("Price if applicable:");
         gbc.gridx = 0;
         gbc.gridy = 2;
         this.add(priceLabel, gbc);
 
         // TextField for entering Product Name
         priceTextField = new JTextField(20);
         gbc.gridx = 1;
         gbc.gridy = 2;
         this.add(priceTextField, gbc);


        // Submit Button
        submitButton = new JButton("Submit");
        gbc.gridx = 1;
        gbc.gridy = 3;
        this.add(submitButton, gbc);

        // Result label to show feedback
        resultLabel = new JLabel("");
        gbc.gridx = 1;
        gbc.gridy = 4;
        this.add(resultLabel, gbc);


        listModel = new DefaultListModel<>();
        ItemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(ItemList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        this.add(scrollPane, gbc);

        //remove button
        removeButton = new JButton("Remove Selected Item");
        gbc.gridx = 2;
        gbc.gridy = 6;
        gbc.gridheight = 1;
        this.add(removeButton, gbc);

        //return to manager select button
        JButton returnButton = new JButton("Return to Item List");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridheight = 1;
        this.add(returnButton, gbc);
        returnButton.addActionListener(e -> GUI.switchPage(ItemPage.createScrollableItemPage()));

        loadItemsFromDatabase();

        // Add ActionListener to handle button clicks
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productName = nameTextField.getText();
                String productType = (String) typeComboBox.getSelectedItem();
                String productPrice = priceTextField.getText();
                if (productName.isEmpty()) {
                    resultLabel.setText("Please enter a product name.");
                } 
                else if ((productType == "Drink" || productType == "Appetizer") && productPrice.isEmpty()) {
                    // Handle the product data as needed
                    resultLabel.setText("Please enter a price for this product.");
                }
                else {
                    if (productType == "Drink" || productType == "Appetizer") {
                        // Handle the product data as needed
                        Group63Database.insert("INSERT INTO menu (item_name, price, type) VALUES ('" + productName + "', '" + productPrice + "', '" + productType + "')");
                    } 
                        // Handle the product data as needed
                        productType = productType.toLowerCase();
                        Group63Database.insert("INSERT INTO recipe (name, recipe_type) VALUES ('" + productName + "', '" + productType + "')"); 
                        resultLabel.setText(productName + " added successfully.");
                        loadItemsFromDatabase(); // Refresh the list after adding

                    }
                }
            });
        // Add ActionListener to the Remove button to delete the selected item
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = ItemList.getSelectedValue();
                if (selectedItem != null) {
                    int confirmation = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to remove " + selectedItem + "?");
                    if (confirmation == JOptionPane.YES_OPTION) {
                        // Delete the item from the database
                        Group63Database.update("DELETE FROM menu WHERE item_name = '" + selectedItem + "'");
                        Group63Database.update("DELETE FROM recipe WHERE name = '" + selectedItem + "'");
                        loadItemsFromDatabase(); // Refresh the list after deletion
                        resultLabel.setText(selectedItem + " removed successfully.");
                    }
                } else {
                    resultLabel.setText("Please select an item to remove.");
                }
            }
        });
    }


    /**
     * Method to create an instance of AddNewItemPage
     * @author Tanuj Narnam
     * @return an instance of AddNewItemPage
     */
    // Method to load items from the recipe table into the JList
    private void loadItemsFromDatabase() {
        listModel.clear(); // Clear the current list
        ArrayList<ArrayList<String>> items = Group63Database.query("SELECT name FROM recipe");

        // Check if items are fetched successfully
        if (items != null && !items.isEmpty()) {
            // Skip the header row
            for (int i = 1; i < items.size(); i++) {
                listModel.addElement(items.get(i).get(0)); // Add each item name to the list model
            }
        }
    }
}