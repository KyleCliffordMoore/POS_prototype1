import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;

/**
 * Represents a page where recipes (items) are displayed and managed.
 * This class provides functionality to view, add, edit, and remove items from the recipe database.
 */
public class ItemPage extends JPanel {

    // List of item slices representing the displayed items
    ArrayList<ItemSlice> itemSlices;

    /**
     * Constructor for the ItemPage.
     * It initializes the layout, adds buttons for navigating and adding new items,
     * and dynamically loads item data from the database.
     */
    public ItemPage() {
        itemSlices = new ArrayList<>();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Button to add new recipe (item) to the list
        JButton addRecipeButton = new JButton("Add New Item");
        constraints.gridy = 1;
        add(addRecipeButton, constraints);

        // Button to return to the previous page
        JButton backButton = new JButton("Back to Manager Select");
        constraints.gridy = 0;
        add(backButton, constraints);

        // Action to switch to the "add recipe" page
        addRecipeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new AddNewItemPage());  // Navigate to the page for adding a new recipe
            }
        });

        // Action to return to the previous manager selection page
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new ManagerSelectPage());  // Change to relevant page
            }
        });

        // Fetching data from the recipe table, including recipe_id
        try {
            ResultSet recipeSet = Group63Database.queryResultSet("SELECT recipe_id, name, recipe_type FROM recipe");

            constraints.gridx = 0;
            constraints.gridy = 2;

            // Iterate through the results and create an ItemSlice for each recipe
            while (recipeSet.next()) {
                // Create a new ItemSlice to represent each recipe, including recipe_id
                ItemSlice itemSlice = new ItemSlice(this, recipeSet.getInt(1), recipeSet.getString(2), recipeSet.getString(3));

                constraints.gridy++;

                // Add the ItemSlice to the panel and the list
                add(itemSlice, constraints);
                itemSlices.add(itemSlice);
            }

        } catch (SQLException e) {
            System.err.println("Couldn't query Recipe Database.");
            e.printStackTrace();
        }
    }

    /**
     * Custom painting method to draw the component's background and update the layout.
     * @param g The Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.fillRect(0, 0, GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight());

        // Repaint all item slices
        for (ItemSlice itemSlice : itemSlices) {
            itemSlice.validate();
            itemSlice.repaint();
        }
    }

    /**
     * Removes a specific item slice from the list and UI.
     * @param itemSlice The ItemSlice to be removed.
     * @return True if the slice was removed successfully, false otherwise.
     */
    public boolean removeSlice(ItemSlice itemSlice) {
        boolean toReturn = itemSlices.remove(itemSlice);
        this.remove(itemSlice);
        revalidate();
        repaint();
        paintComponent(getGraphics());
        return toReturn;
    }

    /**
     * Creates a scrollable container for the ItemPage.
     * @return A JScrollPane containing the ItemPage.
     */
    public static JScrollPane createScrollableItemPage() {
        ItemPage itemPage = new ItemPage();
        JScrollPane scrollPane = new JScrollPane(itemPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    /**
     * Represents a visual component for a single recipe (item).
     * Each slice displays recipe details and allows the user to remove or edit the recipe.
     */
    public static class ItemSlice extends JPanel {

        // Reference to the parent page and recipe data
        private final ItemPage parent;
        private int recipeId;
        private String recipeName;
        private String recipeType;

        /**
         * Constructor for the ItemSlice.
         * Initializes the slice with recipe data and adds buttons for removing or editing the recipe.
         * @param parent The parent ItemPage containing this slice.
         * @param recipeId The unique ID of the recipe.
         * @param recipeName The name of the recipe.
         * @param recipeType The type of the recipe.
         */
        public ItemSlice(ItemPage parent, int recipeId, String recipeName, String recipeType) {
            this.parent = parent;
            this.recipeId = recipeId;  // recipe_id is stored but not displayed
            this.recipeName = recipeName;
            this.recipeType = recipeType;

            ItemSlice me = this;

            // Setting up the component's size and layout
            setPreferredSize(new Dimension(850, 30));
            setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(1, 0, 1, 0);

            // Adjusting label spacing based on content length
            int spacing = 50 - recipeName.length();
            JLabel nameLabel = new JLabel(recipeName + " ".repeat(spacing > 0 ? spacing : 0));
            nameLabel.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 5, 200));
            nameLabel.setFont(new Font("Courier New", Font.PLAIN, 12));

            spacing = 25 - recipeType.length();
            JLabel typeLabel = new JLabel(recipeType + " ".repeat(spacing > 0 ? spacing : 0));
            typeLabel.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 5, 200));
            typeLabel.setFont(new Font("Courier New", Font.PLAIN, 12));

            // Buttons for removing or configuring the recipe
            JButton removeButton = new JButton("Remove Item");
            JButton configButton = new JButton("Edit Item");

            // Action listener to remove the recipe from the database and UI
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Group63Database.update("DELETE FROM recipe_ingredient WHERE recipe_id = " + recipeId + ";");
                    Group63Database.update("DELETE FROM recipe WHERE recipe_id = " + recipeId + ";");
                    Group63Database.update("DELETE FROM menu WHERE item_name = '" + recipeName + "';");
                    System.out.println("Removed recipe");
                    parent.removeSlice(me);
                }
            });

            // Action listener to switch to the edit page for the recipe
            configButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GUI.switchPage(new EditItemPage(recipeId));  // Switch to recipe edit page, passing recipeId
                }
            });

            // Adding components to the panel in the appropriate layout positions
            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.gridx = 0;
            constraints.gridy = 0;
            add(nameLabel, constraints);
            constraints.gridx = 1;
            add(typeLabel, constraints);
            constraints.gridx = 2;
            add(removeButton, constraints);
            constraints.gridx = 3;
            add(configButton, constraints);

            revalidate();
            repaint();
        }

        /**
         * Custom painting method for the ItemSlice to draw the component's background.
         * @param g The Graphics object used for painting.
         */
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(200, 200, 200));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(new Color(200, 200, 200, 125));
            g.drawRect(0, 0, getWidth(), getHeight());
        }

        // Getters for the recipe's details
        public String getRecipeName() {
            return recipeName;
        }

        public String getRecipeType() {
            return recipeType;
        }

        public int getRecipeId() {
            return recipeId;
        }
    }
}

