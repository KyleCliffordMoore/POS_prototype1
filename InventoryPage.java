import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;

/**
 * The InventoryPage class represents the inventory management page in the GUI.
 * It displays a list of inventory items and allows users to add new inventory items
 * or navigate back to the manager selection page.
 */
public class InventoryPage extends JPanel {

    // A list of InventorySlice objects representing each inventory item on the page.
    ArrayList<InventorySlice> inventorySlices;

    /**
     * Constructor for InventoryPage.
     * Initializes the layout, buttons, and inventory items displayed on the page.
     */
    InventoryPage() {

        // Initialize the list of inventory slices
        inventorySlices = new ArrayList<InventorySlice>();

        // Set layout to GridBagLayout for flexible component placement
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Add a button to add a new inventory item
        JButton addInventoryButton = new JButton("Add New Inventory Item");
        constraints.gridy = 1;
        add(addInventoryButton, constraints);

        // Add a button to navigate back to the Manager Select page
        JButton backToSelectionButton = new JButton("Back Manager Select");
        constraints.gridy = 0;
        add(backToSelectionButton, constraints);

        // Action listener for the back button to switch to the ManagerSelectPage
        backToSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new ManagerSelectPage());
            }
        });

        // Action listener for the add inventory button to switch to AddInventory page
        addInventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new AddInventory());
            }
        });

        // Query the inventory database and add inventory items to the page
        try {
            ResultSet inventorySet = Group63Database.queryResultSet("SELECT inventory_id, name, quantity, quantity_type, restock_level FROM inventory");

            constraints.gridx = 0;
            constraints.gridy = 2;

            // Iterate through each inventory item in the result set and create a new InventorySlice
            while (inventorySet.next()) {
                InventorySlice inventorySlice = new InventorySlice(this, inventorySet.getInt(1), inventorySet.getString(2),
                        inventorySet.getDouble(3), inventorySet.getString(4), inventorySet.getDouble(5));

                constraints.gridy++;

                // Add the inventory slice to the panel and store it in the list
                add(inventorySlice, constraints);
                inventorySlices.add(inventorySlice);
            }

        } catch (SQLException e) {
            System.err.println("Couldn't query Employee Database?");
            e.printStackTrace();
        }

    }

    /**
     * Overrides the paintComponent method to paint the background and revalidate/repaint each inventory slice.
     *
     * @param g The Graphics object used to paint the component.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(getBackground());
        g.fillRect(0, 0, GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight());

        // Validate and repaint each inventory slice
        for (InventorySlice inventorySlice : inventorySlices) {
            inventorySlice.validate();
            inventorySlice.repaint();
        }
    }

    /**
     * Removes an InventorySlice from the panel and revalidates/repaints the component.
     *
     * @param inventorySlice The InventorySlice to be removed.
     * @return true if the slice was successfully removed, false otherwise.
     */
    public boolean removeSlice(InventorySlice inventorySlice) {
        boolean toReturn = inventorySlices.remove(inventorySlice);
        this.remove(inventorySlice);
        revalidate();
        repaint();
        paintComponent(getGraphics());

        return toReturn;
    }

    /**
     * Creates a scrollable view of the InventoryPage.
     *
     * @return A JScrollPane containing the InventoryPage with vertical scrolling enabled.
     */
    public static JScrollPane createScrollableInventoryPage() {
        InventoryPage inventoryPage = new InventoryPage();
        JScrollPane scrollPane = new JScrollPane(inventoryPage);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}