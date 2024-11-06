import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.ArrayList;

/**
 * The ModifyIngredient class provides a GUI panel for modifying ingredients 
 * in a recipe management system. It allows users to view, add, and remove 
 * ingredients associated with recipes.
 */
public class ModifyIngredient extends JPanel {

    private JTextField ingredientNameTextField;
    private JTextField countTextField;
    private JTextField measurementTypeTextField;
    private JButton submitButton;
    private JLabel resultLabel;
    private JList<String> itemList;
    private DefaultListModel<String> listModel;
    private JButton removeButton;

    /**
     * Constructs a ModifyIngredient panel with specified recipe and ingredient IDs.
     *
     * @param recipeId the ID of the recipe to which the ingredient belongs
     * @param ingredientId the ID of the ingredient to be modified (may be null for new ingredients)
     * @param recipeIngId the ID of the recipe ingredient association (may be null if adding a new ingredient)
     */
    public ModifyIngredient(int recipeId, Integer ingredientId, Integer recipeIngId) {
        // Set layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label for Ingredient Name
        JLabel ingredientNameLabel = new JLabel("Ingredient Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        this.add(ingredientNameLabel, gbc);

        // TextField for Ingredient Name
        ingredientNameTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        this.add(ingredientNameTextField, gbc);

        // Label for Count
        JLabel countLabel = new JLabel("Count:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        this.add(countLabel, gbc);

        // TextField for Count
        countTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        this.add(countTextField, gbc);

        // Label for Measurement Type
        JLabel measurementTypeLabel = new JLabel("Measurement Type:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        this.add(measurementTypeLabel, gbc);

        // TextField for Measurement Type
        measurementTypeTextField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        this.add(measurementTypeTextField, gbc);

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
        itemList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(itemList);
        scrollPane.setPreferredSize(new Dimension(200, 200));
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        this.add(scrollPane, gbc);

        // Remove button
        removeButton = new JButton("Remove Selected Item");
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridheight = 1;
        this.add(removeButton, gbc);

        // Return to manager select button
        JButton returnButton = new JButton("Return to Item");
        gbc.gridx = 0;
        gbc.gridy = 5;
        this.add(returnButton, gbc);
        returnButton.addActionListener(e -> GUI.switchPage(new EditItemPage(recipeId)));

        if (ingredientId != null){
          ArrayList<ArrayList<String>> ingredient = Group63Database.query("SELECT name, quantity, quantity_type FROM ingredient WHERE ingredient_id = " + ingredientId);

          ingredientNameTextField.setText(ingredient.get(1).get(0)); // Set ingredient name
          countTextField.setText(ingredient.get(1).get(1)); // Set count
          measurementTypeTextField.setText(ingredient.get(1).get(2));

        }

        loadItemsFromDatabase();

        // Add ActionListener to handle button clicks
        /**
         * Handles the submission of a new or modified ingredient.
         * Validates input, checks if the ingredient already exists, 
         * and adds or updates it in the database as necessary.
         */
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ingredientName = ingredientNameTextField.getText();
                String count = countTextField.getText();
                String measurementType = measurementTypeTextField.getText();

                if (ingredientName.isEmpty() || count.isEmpty() || measurementType.isEmpty()) {
                    resultLabel.setText("Please fill in all required fields.");
                } else {
                    // Check if the ingredient already exists in the inventory table
                    ArrayList<ArrayList<String>> inventoryResult = Group63Database.query("SELECT inventory_id FROM inventory WHERE name = '" + ingredientName + "'");

                    if (inventoryResult != null && inventoryResult.size() > 1) {
                        // Ingredient exists in inventory, get the inventory_id
                        String inventoryId = inventoryResult.get(1).get(0);
                        
                        // Now check if the ingredient already exists in the ingredient table
                        ArrayList<ArrayList<String>> ingredientResult = Group63Database.query("SELECT ingredient_id FROM ingredient WHERE name = '" + ingredientName + "' AND quantity = '" + count + "' AND quantity_type = '" + measurementType + "'");

                        String ingredientId;

                        if (ingredientResult != null && ingredientResult.size() > 1) {
                            // Ingredient exists, get the ingredient_id
                            ingredientId = ingredientResult.get(1).get(0);
                            resultLabel.setText("Ingredient " + ingredientName + " already exists.");
                        } else {
                            // Ingredient does not exist, find the next available ingredient_id
                            ArrayList<ArrayList<String>> maxIdResult = Group63Database.query("SELECT MAX(ingredient_id) FROM ingredient");
                            int newIngredientId = 1; // Default to 1 if no records exist

                            if (maxIdResult != null && maxIdResult.size() > 1) {
                                String maxIdStr = maxIdResult.get(1).get(0);
                                if (maxIdStr != null) {
                                    newIngredientId = Integer.parseInt(maxIdStr) + 1; // Increment for the next ID
                                }
                            }

                            // Insert new ingredient with the manually set ingredient_id
                            Group63Database.insert("INSERT INTO ingredient (ingredient_id, name, quantity, quantity_type, inventory_id) VALUES ("
                                + newIngredientId + ", '" + ingredientName + "', " + count + ", '" + measurementType + "', " + inventoryId + ")");

                            // Retrieve the newly inserted ingredient_id
                            ingredientResult = Group63Database.query("SELECT ingredient_id FROM ingredient WHERE name = '" 
                                + ingredientName + "' AND quantity = '" + count + "' AND quantity_type = '" + measurementType + "'");

                            if (ingredientResult != null && ingredientResult.size() > 1) {
                                ingredientId = ingredientResult.get(1).get(0); // Get the ingredient_id from the query result
                                resultLabel.setText("Ingredient " + ingredientName + " added successfully.");
                            } else {
                                resultLabel.setText("Error: Unable to retrieve ingredient ID.");
                                return;
                            }
                        }

                        // Find the next available recipe_ing_id
                        ArrayList<ArrayList<String>> recipeIngIdResult = Group63Database.query("SELECT MAX(recipe_ing_id) FROM recipe_ingredient");
                        int nextRecipeIngId = 1; // Default to 1 if no records exist

                        if (recipeIngIdResult != null && recipeIngIdResult.size() > 1) {
                            String maxIdStr = recipeIngIdResult.get(1).get(0);
                            if (maxIdStr != null) {
                                nextRecipeIngId = Integer.parseInt(maxIdStr) + 1; // Increment for the next ID
                            }
                        }

                        if (ingredientId != null){
                          Group63Database.update("DELETE FROM recipe_ingredient WHERE recipe_ing_id =" + recipeIngId + ";");
                        }

                        // Insert the ingredient into the recipe_ingredient table using the new ingredient_id
                        Group63Database.insert("INSERT INTO recipe_ingredient (recipe_ing_id, recipe_id, name, ingredient_id) VALUES ("
                            + nextRecipeIngId + ", " + recipeId + ", '" + ingredientName + "', " + ingredientId + ")");

                        resultLabel.setText("Ingredient " + ingredientName + " added to recipe successfully.");
                    } else {
                        // Prompt to update the inventory
                        int response = JOptionPane.showConfirmDialog(null,
                            "Ingredient " + ingredientName + " is not in the inventory. Would you like to add it to the inventory?");
                        if (response == JOptionPane.YES_OPTION) {
                            // Redirect to inventory update page
                            GUI.switchPage(InventoryPage.createScrollableInventoryPage());
                        } else {
                            resultLabel.setText("Please add the ingredient to the inventory first.");
                        }
                    }

                    // Refresh the list after adding
                    loadItemsFromDatabase();
                }
            }
        });

        /**
         * Prompts the user to add an ingredient to the inventory if it doesn't exist.
         *
         * @param ingredientName the name of the ingredient to be added
         */
        itemList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    String selectedItem = itemList.getSelectedValue();
                    if (selectedItem != null) {
                        // Parse the selected item details and autofill the fields
                        String[] itemParts = selectedItem.split("  ");
                        ingredientNameTextField.setText(itemParts[0]); // Set ingredient name
                        countTextField.setText(itemParts[1]); // Set count
                        measurementTypeTextField.setText(itemParts[2]); // Set measurement type
                    }
                }
            }
        });

        /**
         * Removes the selected ingredient from the JList and deletes it from the database.
         * Provides a confirmation dialog before deletion.
         */
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem = itemList.getSelectedValue();
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
     * Loads items from the database into the JList for display.
     * Clears the current list and fetches ingredient data to populate the list model.
     */
    private void loadItemsFromDatabase() {
        listModel.clear(); // Clear the current list
        ArrayList<ArrayList<String>> items = Group63Database.query("SELECT name, quantity, quantity_type FROM ingredient");

        // Check if items are fetched successfully
        if (items != null && !items.isEmpty()) {
            // Skip the header row
            for (int i = 1; i < items.size(); i++) {
                listModel.addElement(items.get(i).get(0) + "  " + items.get(i).get(1) + "  " + items.get(i).get(2)); // Add each item name to the list model
            }
        }
    }
}