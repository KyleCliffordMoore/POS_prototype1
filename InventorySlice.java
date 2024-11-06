import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The InventorySlice class represents a single inventory item in the InventoryPage.
 * It displays item details such as name, quantity, quantity type, and restock level, and
 * provides options to remove or edit the item.
 */
public class InventorySlice extends JPanel {

    private final InventoryPage parent;
    final static int MAX_CHAR_LENGTH = 50; // Maximum character length for item name display
    private String itemName;
    private final int id; // Unique inventory item ID
    private double quantity; // Quantity of the inventory item
    private String quantityType; // Type of the quantity (e.g., kg, liters)
    private double restockLevel; // Restock level threshold

    /**
     * Constructor for InventorySlice.
     * Initializes the display of the inventory item and provides buttons for editing or removing the item.
     *
     * @param parent The InventoryPage to which this slice belongs.
     * @param id The unique ID of the inventory item.
     * @param itemName The name of the inventory item.
     * @param quantity The current quantity of the inventory item.
     * @param quantityType The type of quantity (e.g., units, kg, liters).
     * @param restockLevel The level at which the item needs to be restocked.
     */
    public InventorySlice(InventoryPage parent, int id, String itemName, double quantity, String quantityType, double restockLevel) {

        this.parent = parent;
        this.itemName = itemName;
        this.id = id;
        this.quantity = quantity;
        this.quantityType = quantityType;
        this.restockLevel = restockLevel;

        InventorySlice me = this;

        // Set the preferred size and layout of the panel
        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), 200));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 0, 1, 0);

        // Create and configure labels for item name, quantity, quantity type, and restock level
        int spacing = 25 - itemName.length();
        JLabel name = new JLabel(itemName + " ".repeat(spacing > 0 ? spacing : 0));
        name.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 5, 200));
        name.setFont(new Font("Courier New", Font.PLAIN, 12));

        spacing = 10 - String.valueOf(quantity).length();
        JLabel quantityLabel = new JLabel(String.valueOf(quantity) + " ".repeat(spacing > 0 ? spacing : 0));
        quantityLabel.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 5, 200));
        quantityLabel.setFont(new Font("Courier New", Font.PLAIN, 12));

        spacing = 25 - quantityType.length();
        JLabel quantityTypeLabel = new JLabel(quantityType + " ".repeat(spacing > 0 ? spacing : 0));
        quantityTypeLabel.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 5, 200));
        quantityTypeLabel.setFont(new Font("Courier New", Font.PLAIN, 12));

        spacing = 5 - String.valueOf(restockLevel).length();
        JLabel restockLabel = new JLabel(String.valueOf(restockLevel) + " ".repeat(spacing > 0 ? spacing : 0));
        restockLabel.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 5, 200));
        restockLabel.setFont(new Font("Courier New", Font.PLAIN, 12));

        // Create buttons for removing and editing the inventory item
        JButton removeButton = new JButton("Remove Item");
        JButton configButton = new JButton("Edit Item");

        // Action listener for removing the inventory item
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Group63Database.update("DELETE FROM inventory WHERE inventory_id = " + id + ";");
                System.out.println("removed item");
                parent.removeSlice(me);
                System.out.println(me);
            }
        });

        // Action listener for editing the inventory item
        configButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new EditInventory(id));
            }
        });

        // Add components to the panel with appropriate layout constraints
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(name, constraints);
        constraints.gridx = 1;
        add(quantityLabel, constraints);
        constraints.gridx = 2;
        add(quantityTypeLabel, constraints);
        constraints.gridx = 3;
        add(restockLabel, constraints);
        constraints.gridx = 4;
        add(removeButton, constraints);
        constraints.gridx = 5;
        add(configButton, constraints);

        // Add component listener to handle resizing of the panel
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPreferredSize(new Dimension(725, 30));
                revalidate();
                repaint();
            }
        });

        // Revalidate and repaint the panel after initialization
        revalidate();
        repaint();
    }

    /**
     * Overrides the paintComponent method to customize the background of the panel.
     *
     * @param g The Graphics object used to paint the component.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(200, 200, 200, 125));
        g.drawRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Gets the unique ID of the inventory item.
     *
     * @return The ID of the inventory item.
     */
    public int getId() {
        return id;
    }
}