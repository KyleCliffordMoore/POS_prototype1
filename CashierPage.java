import javax.swing.*;
import java.util.Vector;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;


/**
 * CashierPage is a JPanel that represents the main cashier interface for processing customer orders. 
 * It provides buttons for selecting meals, sides, drinks, appetizers, and entrees and allows the cashier 
 * to review and remove items from the order list.
 * @author Jess Cadena
 */
public class CashierPage extends JPanel {
    
   //for order list and total price
    private DefaultListModel<OrderItem> orderListModel;
    private JList<OrderItem> orderList;
    private CashierLabel totalLabel;
    private double totalPrice;

    //for creating meals 
    private JButton selectedMealButton;
    private JButton selectedSideButton;
    private int selectedEntreesCount = 0;
    private Vector<String> selectedEntrees = new Vector<>();
    private int maxEntreesAllowed = 0;
    private JLabel entreeSelectionLabel;

     /**
     * Constructs a new CashierPage with a variety of buttons to create an order
     * by selecting meals, sides, drinks, appetizers, and entrees.
     * It also displays the current order and total price.
     */
    public CashierPage() {
        
        //Setting column widths
        GridBagLayout layout = new GridBagLayout();
        int[] columns = new int[9];
        for (int i = 0; i < 9; i++) {
            columns[i] = 90;
        }
        layout.columnWidths = columns;

        //setting layout
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();

        //setting constraints
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.BOTH; 
        constraints.weightx = 1; 
        constraints.weighty = 1; 
        constraints.gridwidth = 1; 

        //for adjusting everythings size propertionally to others
        int row;
        int col;

        totalPrice = 0.0;

        //create order list
        orderListModel = new DefaultListModel<>();
        orderList = new JList<>(orderListModel);
        JScrollPane orderScrollPane = new JScrollPane(orderList);
        orderList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Meals buttons --------------------------------------------------------------------------
        col = 0;
        row = 1;

        constraints.gridx = col;
        constraints.gridy = row;
        add(new CashierLabel("Meals"), constraints);
        
        ArrayList<ArrayList<String>> Meals = Group63Database.query("SELECT item_name, price FROM menu WHERE type = 'Meal';");
        Meals.remove(0);
        
         for (int i = 0; i < Meals.size(); i++) {
            if (i % 3 == 0) { // Move to the next row after 6 items
                row++;
                col = 0;
            }
            constraints.gridx = col++;
            constraints.gridy = row;

            String mealname = Meals.get(i).get(0);
            double mealprice = Double.parseDouble(Meals.get(i).get(1));

            add(new MealButton(mealname, mealprice), constraints);
           }
      
        // Sides buttons --------------------------------------------------------------------------
        col = 0;
        row += 1;
        
        constraints.gridx = col;
        constraints.gridy = row;
        add(new CashierLabel("Sides"), constraints);
        
        ArrayList<ArrayList<String>> sides = Group63Database.query("SELECT name FROM recipe WHERE recipe_type = 'side';");
        sides.remove(0);

        for (int i = 0; i < sides.size(); i++) {
            if (i % 3 == 0) { // Move to the next row after 6 items
                row++;
                col = 0;
            }
            constraints.gridx = col++;
            constraints.gridy = row;
            add(new SideButton(sides.get(i).get(0)), constraints);
           }
        
           if (col == 3) { // Move to the next row after 6 items
            row++;
            col = 0;
            }
            constraints.gridx = col++;
            constraints.gridy = row;
           add(new SideButton("50/50"), constraints);
           int SidesRow = row;

        //drinks buttons --------------------------------------------------------------------------
        col = 3;
        row = 1;
        
        constraints.gridx = col;
        constraints.gridy = row;
        add(new CashierLabel("Drinks"), constraints);

        ArrayList<ArrayList<String>> Drinks = Group63Database.query("SELECT item_name, price FROM menu WHERE type = 'Drink';");
        Drinks.remove(0);

        for (int i = 0; i < Drinks.size(); i++) {
            if (i % 3 == 0) { 
                row++;
                col = 3;
            }
           
            constraints.gridx = col++;
            constraints.gridy = row;

            String drinkName = Drinks.get(i).get(0);
            Double drinkPrice =  Double.parseDouble(Drinks.get(i).get(1));

            add(new drinkAndAppetizerButton(drinkName, drinkPrice, "Drink", new Color(183, 232, 230)), constraints);
        }
       
        // Appetizers buttons--------------------------------------------------------------------------
        col = 3;
        row += 1;
        constraints.gridx = col;
        constraints.gridy = row;
        add(new CashierLabel("Appetizers"), constraints);

        ArrayList<ArrayList<String>> Appetizers = Group63Database.query("SELECT item_name, price FROM menu WHERE type = 'Appetizer';");
        Appetizers.remove(0);

        for (int i = 0; i < Appetizers.size(); i++) {
            if (i % 3 == 0) { 
                row++;
                col = 3;
            }
            constraints.gridx = col++;
            constraints.gridy = row;
           
            String appetizerName = Appetizers.get(i).get(0);
            Double appetizerPrice =  Double.parseDouble(Appetizers.get(i).get(1));

            add(new drinkAndAppetizerButton(appetizerName, appetizerPrice, "Appetizer", new Color(250, 182, 243)), constraints);
        }
        int Appetizersrow = row;
        // Entrees order list --------------------------------------------------------------------------
       
        col = 0;
        if (SidesRow > Appetizersrow){
            row = SidesRow + 1;
        } else {
            row = Appetizersrow;
        }
        row = row + 1;
        constraints.gridx = col;
        constraints.gridy = row;

        add(new CashierLabel("Entrees"), constraints);

        // Entree selection label
        entreeSelectionLabel = new CashierLabel("Select 0 entrees");
        constraints.gridx = 1;
        constraints.gridy = row;
        constraints.gridwidth = 2;
        add(entreeSelectionLabel, constraints);
        constraints.gridwidth = 1;
       
        ArrayList<ArrayList<String>> entrees = Group63Database.query("SELECT name FROM recipe WHERE recipe_type = 'entree';");
        entrees.remove(0);

        row = row + 1;
        col = 0;

        for (int i = 0; i < entrees.size(); i++) {
            if (i % 6 == 0) { // Move to the next row after 6 items
                row++;
                col = 0;
            }
            constraints.gridx = col++;
            constraints.gridy = row;
            add(new EntreeButton(entrees.get(i).get(0)), constraints);
        }

        //CompleteItem button--------------------------------------------------------------------------
        constraints.gridx = 2;
        constraints.gridy = row + 1;
        constraints.gridwidth = 2;
        add(new CompleteItemButton("Complete Item"), constraints);

        //return to home button--------------------------------------------------------------------------
        JButton HomeButton = new JButton("Home");
        constraints.gridx = 0;
        constraints.gridy = row + 1;
        constraints.gridwidth = 1;
        add(HomeButton, constraints);
        HomeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new EmployeeOrManagerSelectionPage(true));
            }
        });


        //orderviewer --------------------------------------------------------------------------
        constraints.gridx = 6;
        constraints.gridy = 2;
        constraints.gridheight = row - 1; 
        constraints.gridwidth = 3; 
        add(orderScrollPane, constraints);

        //  Remove Button --------------------------------------------------------------------------
        JButton removeButton = new JButton("Remove Selected");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               
                int selectedIndex = orderList.getSelectedIndex();
                
                if (selectedIndex != -1) {
                    OrderItem removedItem = orderListModel.get(selectedIndex);
                    orderListModel.remove(selectedIndex);
                    updateTotalPrice(removedItem.getPrice(), false);
                }
            }
        });
        constraints.gridx = 4;
        constraints.gridy = row + 1;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        add(removeButton, constraints);
        
        //total label ----------------------------------------------------------------------------
        constraints.gridx = 5;
        constraints.gridy = row + 1;
        totalLabel = new CashierLabel("Total: $0.00");
        add(totalLabel, constraints);

        //Checkout Button--------------------------------------------------------------------------
        constraints.gridx = 6;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        add(new OrderLabel("Order"), constraints);

        constraints.gridy = row + 1;
        add(new CheckoutButton("Checkout"), constraints);
 
    }

    //CLASSESSSSSSSSSS--------------------------------------------------------------------------------

     /**
     * Custom button class for the CashierPage. Inherits from JButton and adjusts appearance and size.
     * @author Jess Cadena
     */
    private class CashierButton extends JButton {

        public CashierButton(String text) {
            super(text);
            setFont(new Font("Arial", Font.BOLD, 13));
        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            int max = Math.max(size.width, size.height);
            return new Dimension(max, max); 
        }
    }

    /**
     * MealButton is a subclass of CashierButton, customized to handle meal selections.
     * It changes the button appearance based on selection and allows entree choices.
     * @author Jess Cadena
     */
    private class MealButton extends CashierButton {
       
        public MealButton(String text, double itemPrice) {
            super(text + ": $" + String.format("%.2f", itemPrice));
            setBackground(new Color(237, 234, 142));

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedMealButton == MealButton.this) {
                        // Deselect
                        setBackground(new Color(237, 234, 142));
                        selectedMealButton = null;
                        maxEntreesAllowed = 0;
                        selectedEntreesCount = 0;
                        entreeSelectionLabel.setText("Select 0 entrees");
                        resetSelections();
                    } else {
                        // Select
                        if (selectedMealButton != null) {
                            selectedMealButton.setBackground(new Color(237, 234, 142)); 
                        }
                        selectedMealButton = MealButton.this;
                        setBackground(new Color(125, 123, 50)); // Darken to indicate selection

                        // Set the max entrees allowed based on the selected meal
                        if (text.equals("Bowl")) {
                            maxEntreesAllowed = 1;
                        } else if (text.equals("Plate")) {
                            maxEntreesAllowed = 2;
                        } else if (text.equals("Bigger Plate")) {
                            maxEntreesAllowed = 3;
                        }
                        selectedEntreesCount = 0;
                        entreeSelectionLabel.setText("Select " + maxEntreesAllowed + " entrees");
                    }    
                }
            });
        }
    }
     

     /**
     * SideButton is a subclass of CashierButton, customized to handle side selections.
     * It changes the button appearance based on selection.
     * @author Jess Cadena
     */
    private class SideButton extends CashierButton {

        public SideButton(String text) {
            super(text);
            setBackground(new Color(247, 188, 104));

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedSideButton == SideButton.this) {
                        // Deselect
                        setBackground(new Color(247, 188, 104));
                        selectedSideButton = null;   
                    }
                    else{
                        // Select
                        if (selectedSideButton != null) {
                            selectedSideButton.setBackground(new Color(247, 188, 104)); // Reset color
                        }
                        selectedSideButton = SideButton.this;
                        setBackground(new Color(173, 128, 62)); // Darken to indicate selection
                    }
                }
            });
        }
    }

    /**
     * EntreeButton is a subclass of CashierButton, customized to handle entree selections.
     * It limits the number of entrees a user can select based on the meal type.
     * @author Jess Cadena
     */

    private class EntreeButton extends CashierButton {

        public EntreeButton(String text) {
            super(text);
            setBackground(new Color(212, 255, 214));

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (getBackground().equals(new Color(134, 166, 136))) {
                        // Button is currently selected; allow deselection
                        setBackground(new Color(212, 255, 214)); // Deselect
                        selectedEntreesCount--;
                        selectedEntrees.remove(text);
                    } else {
                        // Button is not selected; check if we can select more entrees
                        if (maxEntreesAllowed == 0 || selectedEntreesCount >= maxEntreesAllowed) {
                            return;
                        }
                        setBackground(new Color(134, 166, 136));
                        selectedEntreesCount++;
                        selectedEntrees.add(text);
                    }

                    entreeSelectionLabel.setText("Select " + (maxEntreesAllowed - selectedEntreesCount) + " entrees");
                }
            });
        }
    }


      /**
     * drinkAndAppetizerButton is a subclass of CashierButton, customized to handle drink and appetizer selections.
     * It adds the selected item to the order list and updates the total price.
     * @author Jess Cadena
     */
    private class drinkAndAppetizerButton extends CashierButton {

        public drinkAndAppetizerButton(String text, double itemPrice, String type, Color color) {
            super(text + ": $" + String.format("%.2f", itemPrice));
            setBackground(color);

            //adding to order list
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    OrderItem item = new OrderItem(text.split(":")[0], itemPrice, type);
                    orderListModel.addElement(item);
                    updateTotalPrice(itemPrice, true);
                }
            });
        }
    }
   

      /**
     * CompleteItemButton finalizes the meal selection by verifying that all components (meal, side, entrees) are selected.
     * It adds the complete meal to the order list and updates the total price.
     * @author Jess Cadena
     */
    private  class CompleteItemButton extends CashierButton {
        
        public CompleteItemButton(String text){
            super(text);
            setBackground(new Color(211, 211, 211));

            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedMealButton == null || selectedSideButton == null || selectedEntreesCount < maxEntreesAllowed) {
                        JOptionPane.showMessageDialog(null, "Please complete your meal selection.");
                        return;
                    }
                    
                    String size = selectedMealButton.getText().split(":")[0]; 
                    String side = selectedSideButton.getText();
                    List<String> entrees = new ArrayList<>(selectedEntrees);

                    StringBuilder mealDescription = new StringBuilder();
                    for (String entree : entrees) {
                        mealDescription.append(entree).append(", ");
                    }

                    selectedEntrees.clear();
                    mealDescription.append(side).append(", ").append(size);
                    double price = Double.parseDouble(selectedMealButton.getText().split("\\$")[1]);
                    String mealDescriptionString = mealDescription.toString();

                    OrderItem mealItem = new OrderItem(mealDescriptionString.split(":")[0], price, "Meal", size, side, entrees);

                    orderListModel.addElement(mealItem);
                    updateTotalPrice(price, true);

                    // Reset selections
                    resetSelections();
                }
            });
        }
    }

    /**
     * resetSelections resets the meal, side, and entree selections to their default state.
     * @author Jess Cadena
     */
    private void resetSelections() {
        // Reset meal selection
        if (selectedMealButton != null) {
            selectedMealButton.setBackground(new Color(237, 234, 142));
        }
        selectedMealButton = null;

        // Reset side selection
        if (selectedSideButton != null) {
            selectedSideButton.setBackground(new Color(247, 188, 104));
        }
        selectedSideButton = null;

        // Reset entree selections
        selectedEntreesCount = 0;
        selectedEntrees.clear();
        for (Component component : getComponents()) {
            if (component instanceof EntreeButton) {
                component.setBackground(new Color(212, 255, 214));
            }
        }

        // Reset max entrees allowed and label
        maxEntreesAllowed = 0;
        entreeSelectionLabel.setText("Select 0 entrees");
    }
    

    /**
     * CheckoutButton processes the entire order and records the transaction in the database.
     * It handles inserting the receipt and line items into their respective tables.
     * @author Jess Cadena
     */
    private class CheckoutButton extends CashierButton {
        
        public CheckoutButton(String text){
            super(text);
            setFont(new Font("Arial", Font.BOLD, 20));
            setBackground(new Color(255, 71, 77));
        
            addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (orderListModel.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "No items in the order to checkout.");
                        return;
                    }

                    // Calculate total price
                    double totalPrice = 0.0;
                    for (int i = 0; i < orderListModel.size(); i++) {
                        totalPrice += orderListModel.get(i).getPrice();
                    }

                    // Step 1: Insert into the receipt table
                    String insertReceiptSQL = "INSERT INTO receipt (date, totalamount, order_time) VALUES (CURRENT_TIMESTAMP, ?, CURRENT_TIME) RETURNING receipt_id;";
                    int receiptId = -1;

                    try (PreparedStatement pstmt = Group63Database.dataBaseConnection.prepareStatement(insertReceiptSQL)) {
                        pstmt.setDouble(1, totalPrice);
                        ResultSet rs = pstmt.executeQuery();
                        if (rs.next()) {
                            receiptId = rs.getInt("receipt_id");
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    // Step 2: Insert into the line_item table
                    for (int i = 0; i < orderListModel.size(); i++) {
                        OrderItem orderItem = orderListModel.get(i);
                        double itemPrice = orderItem.getPrice();

                        // Insert into line_item
                        String insertLineItemSQL = "INSERT INTO line_item (receipt_id, price) VALUES (?, ?) RETURNING line_item_id;";
                        int lineItemId = -1;
                        try (PreparedStatement pstmt = Group63Database.dataBaseConnection.prepareStatement(insertLineItemSQL)) {
                            pstmt.setInt(1, receiptId);
                            pstmt.setDouble(2, itemPrice);
                            ResultSet rs = pstmt.executeQuery();
                            if (rs.next()) {
                                lineItemId = rs.getInt("line_item_id");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            return;
                        }

                        // Step 3: Insert into specific tables based on the item type
                        switch (orderItem.getType()) {
                            case "Appetizer":
                                String insertAppetizerSQL = "INSERT INTO appetizer_item (line_item_id, name, price) VALUES (?, ?, ?)";
                                try (PreparedStatement pstmt = Group63Database.dataBaseConnection.prepareStatement(insertAppetizerSQL)) {
                                    pstmt.setInt(1, lineItemId);
                                    pstmt.setString(2, orderItem.getName());
                                    pstmt.setDouble(3, itemPrice);
                                    pstmt.executeUpdate();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;

                            case "Drink":
                                String insertDrinkSQL = "INSERT INTO drink_item (line_item_id, name, price, size) VALUES (?, ?, ?, ?)";
                                try (PreparedStatement pstmt = Group63Database.dataBaseConnection.prepareStatement(insertDrinkSQL)) {
                                    pstmt.setInt(1, lineItemId);
                                    pstmt.setString(2, orderItem.getName());
                                    pstmt.setDouble(3, itemPrice);
                                    pstmt.setString(4, "Regular");  // Adjust size if necessary
                                    pstmt.executeUpdate();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;

                            case "Meal":
                            String insertMealSQL = "INSERT INTO meal_item (line_item_id, size, price, meat1, meat2, meat3, side) VALUES (?, ?, ?, ?, ?, ?, ?)";
                            try (PreparedStatement pstmt = Group63Database.dataBaseConnection.prepareStatement(insertMealSQL)) {
                                pstmt.setInt(1, lineItemId);
                                pstmt.setString(2, orderItem.getSize());
                                pstmt.setDouble(3, itemPrice);
                                List<String> meats = orderItem.getEntrees();
                                pstmt.setString(4, meats.size() > 0 ? meats.get(0) : null);
                                pstmt.setString(5, meats.size() > 1 ? meats.get(1) : null);
                                pstmt.setString(6, meats.size() > 2 ? meats.get(2) : null);
                                pstmt.setString(7, orderItem.getSide());
                                pstmt.executeUpdate();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                                break;

                            default:
                                // Handle other types if necessary
                                break;
                    }
                }

                // Clear the order list after checkout
                orderListModel.clear();
                totalPrice = 0.0;
                totalLabel.setText("Total: $0.00");
                JOptionPane.showMessageDialog(null, "Checkout completed.");
            
            }
        });
    
    }
    }

    /**
    * CashierLabel is a subclass of JLabel, customized to display cashier-related text.
    * @author Jess Cadena
    */
    private class CashierLabel extends JLabel {

        public CashierLabel(String text) {
            super(text);
            setFont(new Font("Arial", Font.BOLD, 24));
            setHorizontalAlignment(JLabel.CENTER); 
        }
    }

    /**
     * OrderLabel is a subclass of JLabel, customized to display order-related text.
     * @author Jess Cadena
     */
    private class OrderLabel extends JLabel {

        public OrderLabel(String text) {
            super(text);
            setFont(new Font("Arial", Font.BOLD, 30));
            setHorizontalAlignment(JLabel.CENTER);
        }
    }


    /**
     * Updates the total price of the current order.
     * @author Jess Cadena
     * @param itemPrice The price of the item to add or subtract.
     * @param isAdding Boolean flag indicating whether the item price should be added (true) or subtracted (false) from the total.
     */
    private void updateTotalPrice(double itemPrice, boolean isAdding) {
        if (isAdding) {
            totalPrice += itemPrice;
        } else {
            totalPrice -= itemPrice;
        }
        totalLabel.setText("Total: $" + String.format("%.2f", totalPrice));
    }
}