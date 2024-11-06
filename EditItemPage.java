import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;

/**
 * EditItemPage class creates a JPanel that allows the user to edit an item in the menu.
 * The user can edit the name, type, and price of the item and submit it to the database.
 * The user can also view the list of ingredients for the item and remove an ingredient from the recipe.
 * The user can also add a new ingredient to the recipe.
 * @Author Tanuj Narnam
 */

public class EditItemPage extends JPanel {

      private JTextField nameField;
      private JTextField typeField;
      private JTextField priceField;  // Only used for appetizers
      private JPanel ingredientListPanel;  // Panel to display ingredients
      private int recipeId;  // recipe_id of the item being edited
      private boolean isAppetizer;  // Determines if the item is an appetizer

      public EditItemPage(int recipeId) {
          this.recipeId = recipeId;

          setLayout(new GridBagLayout());
          GridBagConstraints constraints = new GridBagConstraints();
          constraints.insets = new Insets(10, 10, 10, 10);
          constraints.fill = GridBagConstraints.HORIZONTAL;

          // Add Label and Text Field for Item Name
          JLabel nameLabel = new JLabel("Item Name:");
          constraints.gridx = 0;
          constraints.gridy = 0;
          add(nameLabel, constraints);

          nameField = new JTextField(20);
          constraints.gridx = 1;
          add(nameField, constraints);

          // Add Label and Text Field for Recipe Type
          JLabel typeLabel = new JLabel("Item Type:");
          constraints.gridx = 0;
          constraints.gridy = 1;
          add(typeLabel, constraints);

          typeField = new JTextField(20);
          constraints.gridx = 1;
          add(typeField, constraints);

          // Label and Text Field for Price (only used if item is an appetizer)
          JLabel priceLabel = new JLabel("Item Price:");
          constraints.gridx = 0;
          constraints.gridy = 2;
          add(priceLabel, constraints);

          priceField = new JTextField(20);
          constraints.gridx = 1;
          add(priceField, constraints);
          priceField.setEnabled(false);

          typeField.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  String recipeType = typeField.getText().toLowerCase();
                  if (recipeType.equals("appetizer") || recipeType.equals("drink")) {
                      priceField.setEnabled(true);
                  } else {
                      priceField.setEnabled(false);
                      priceField.setText("");  // Clear the price field if not an appetizer
                  }
              }
          });

          // Load the current item data from the database
          loadItemData();

          // Ingredient List Panel
          JLabel ingredientLabel = new JLabel("Recipe Ingredients:");
          constraints.gridx = 0;
          constraints.gridy = 3;
          constraints.gridwidth = 2;
          add(ingredientLabel, constraints);

          // Create a scrollable panel to hold the ingredients
          ingredientListPanel = new JPanel();
          ingredientListPanel.setLayout(new BoxLayout(ingredientListPanel, BoxLayout.Y_AXIS));
          JScrollPane scrollPane = new JScrollPane(ingredientListPanel);
          scrollPane.setPreferredSize(new Dimension(400, 200));
          constraints.gridy = 4;
          add(scrollPane, constraints);

          // Add ingredient button to allow users to add new ingredients
          JButton addIngredientButton = new JButton("Add Ingredient");
          constraints.gridy = 5;
          add(addIngredientButton, constraints);

          // Save button to update the edited details
          JButton saveButton = new JButton("Save");
          constraints.gridy = 6;
          constraints.gridwidth = 1;
          add(saveButton, constraints);

          // Back button to return to the item list
          JButton backButton = new JButton("Back to Item List");
          constraints.gridx = 1;
          add(backButton, constraints);

          // Action listener for saving the edited item details
          saveButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  saveItemChanges();
              }
          });

          // Action listener to go back to the Item list page
          backButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  GUI.switchPage(ItemPage.createScrollableItemPage());
              }
          });

          // Action listener to add a new ingredient
          addIngredientButton.addActionListener(new ActionListener() {
              @Override
              public void actionPerformed(ActionEvent e) {
                  GUI.switchPage(new ModifyIngredient(recipeId, null, null));
              }
          });

          // Load ingredients for the recipe
          loadIngredients();
      }

      /**
       *Loads the current item data from the database based on recipeId
       *@author Tanuj Narnam
       */
      // Loads the current item data from the database based on recipeId
      private void loadItemData() {
          try {
              String query = "SELECT name, recipe_type FROM recipe WHERE recipe_id = " + recipeId;
              ResultSet resultSet = Group63Database.queryResultSet(query);

              // Check if recipe exists
              if (resultSet.next()) {
                  nameField.setText(resultSet.getString("name"));
                  typeField.setText(resultSet.getString("recipe_type"));

                  // Check if it's an appetizer and show the price field accordingly
                  isAppetizer = resultSet.getString("recipe_type").equalsIgnoreCase("appetizer") || resultSet.getString("recipe_type").equalsIgnoreCase("drink");
                  priceField.setVisible(isAppetizer);

                  if (isAppetizer) {
                      // Load price from the menu table if it's an appetizer
                      String priceQuery = "SELECT price FROM menu WHERE item_name = '" + resultSet.getString("name") + "';";
                      ResultSet priceSet = Group63Database.queryResultSet(priceQuery);

                      if (priceSet.next()) { // Check if the menu entry exists
                          priceField.setText(priceSet.getString("price"));
                      } else {
                          priceField.setText("Price not found"); // Handle case where price is not found
                      }
                  }
              } else {
                  // Handle case where the recipe is not found
                  JOptionPane.showMessageDialog(null, "Recipe not found!", "Error", JOptionPane.ERROR_MESSAGE);
              }
          } catch (SQLException e) {
              e.printStackTrace();
          }
      }


    /**
     *Saves the changes made to the item (name, type, price if an appetizer)
        *@author Tanuj Narnam
        */
      // Saves the changes made to the item (name, type, price if an appetizer)
      private void saveItemChanges() {
          String newName = nameField.getText();
          String newType = typeField.getText().toLowerCase();

          // Update the recipe table with new name and type
          String updateQuery = "UPDATE recipe SET name = '" + newName + "', recipe_type = '" + newType + "' WHERE recipe_id = " + recipeId;
          Group63Database.update(updateQuery);

          if (newType.equals("appetizer") || newType.equals("drink")) {
              // Enable the price field and ensure a price is entered
              String newPrice = priceField.getText();
              
              if (newPrice.isEmpty()) {
                  JOptionPane.showMessageDialog(this, "Please enter a price for the appetizer.", "Error", JOptionPane.ERROR_MESSAGE);
                  return;
              }

              try {
                  // Check if the item is already in the menu table
                  String checkQuery = "SELECT * FROM menu WHERE item_name = '" + newName + "'";
                  ResultSet resultSet = Group63Database.queryResultSet(checkQuery);

                  if (!resultSet.next()) {
                      // If not in the menu, insert the item into the menu table
                      String insertMenuQuery = "INSERT INTO menu (item_name, price) VALUES ('" + newName + "', " + newPrice + ")";
                      Group63Database.update(insertMenuQuery);
                  } else {
                      // If it's already in the menu, update the price
                      String updatePriceQuery = "UPDATE menu SET price = " + newPrice + " WHERE item_name = '" + newName + "'";
                      Group63Database.update(updatePriceQuery);
                  }
              } catch (SQLException e) {
                  e.printStackTrace();
              }
          } else {
              // If the type is changed from "appetizer" to something else, remove it from the menu
                  String deleteFromMenuQuery = "DELETE FROM menu WHERE item_name = '" + newName + "'";
                  Group63Database.update(deleteFromMenuQuery);
          }

          // Refresh the item list after saving
          GUI.switchPage(ItemPage.createScrollableItemPage());
      }


    /**
     *Loads the ingredients for the recipe and adds them to the ingredient panel
        *@author Tanuj Narnam
        */
      // Loads the ingredients for the recipe and adds them to the ingredient panel
      private void loadIngredients() {
          try {
              String query = "SELECT recipe_ing_id, ingredient_id, name FROM recipe_ingredient WHERE recipe_id = " + recipeId;
              ResultSet resultSet = Group63Database.queryResultSet(query);

              while (resultSet.next()) {
                  int recipeIngId = resultSet.getInt("recipe_ing_id");
                  String ingredientName = resultSet.getString("name");
                  int ingID = resultSet.getInt("ingredient_id");

                  // Create a panel for each ingredient with an option to edit or remove
                  JPanel ingredientPanel = new JPanel();
                  ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.X_AXIS));

                  JLabel ingredientLabel = new JLabel(ingredientName);
                  JButton editButton = new JButton("Edit");
                  JButton removeButton = new JButton("Remove");

                  ingredientPanel.add(ingredientLabel);
                  ingredientPanel.add(Box.createHorizontalStrut(10));  // Spacer
                  ingredientPanel.add(editButton);
                  ingredientPanel.add(removeButton);

                  ingredientListPanel.add(ingredientPanel);

                  // Action listener for removing the ingredient
                  removeButton.addActionListener(new ActionListener() {
                      @Override
                      public void actionPerformed(ActionEvent e) {
                          removeIngredient(recipeIngId, ingredientPanel);
                      }
                  });

                  // Add functionality for editing the ingredient
                  editButton.addActionListener(new ActionListener() {
                      @Override
                      public void actionPerformed(ActionEvent e) {
                          // Placeholder for ingredient editing (implement this part as needed)
                          GUI.switchPage(new ModifyIngredient(recipeId, ingID, recipeIngId));
                      }
                  });
              }

              revalidate();
              repaint();

          } catch (SQLException e) {
              e.printStackTrace();
          }
      }


    /**
     * Removes the ingredient from the recipe and removes the ingredient panel from the UI
     * @param ingredientId The recipe_ing_id of the ingredient to be removed
     * @param ingredientPanel The panel containing the ingredient to be removed
     * @Author Tanuj Narnam
     * */
      // Removes the ingredient from the recipe
      private void removeIngredient(int ingredientId, JPanel ingredientPanel) {
          String deleteQuery = "DELETE FROM recipe_ingredient WHERE recipe_ing_id = " + ingredientId;
          Group63Database.update(deleteQuery);

          // Remove the ingredient panel from the UI
          ingredientListPanel.remove(ingredientPanel);
          revalidate();
          repaint();
      }
  }