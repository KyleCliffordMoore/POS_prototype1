import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;

/**
 * RestockReportPage is a JPanel that generates a restock report of the current critical inventory items.
 * 
 * The report queries from the inventory table and returns every item that is in critical quantity
 * 
 * @author Kyle Moore
 */

public class RestockReportPage extends JPanel {
    
    private JScrollPane scrollPane;
    /**
     * Constructor for the restock page
     */
    public RestockReportPage() {

        JPanel inventoryPanel = new JPanel();
        inventoryPanel.setLayout(new BoxLayout(inventoryPanel, BoxLayout.Y_AXIS));

        ArrayList<ArrayList<String>> table = Group63Database.query("SELECT * FROM inventory WHERE restock_level = 0;");

        for (int i = 1; i < table.size(); i++) {
            inventoryPanel.add(new InventoryItemSlice(table.get(i)));
        }

        scrollPane = new JScrollPane(inventoryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 2, GUI.getWindowFrameHeight() - 40)); // Set scroll pane size

        add(scrollPane);

        JButton backButton = new JButton("Back");
        add(backButton);
        backButton.addActionListener(e -> GUI.switchPage(new ManagerSelectPage()));

    }

    @Override
    /**
     * Resizes the Restock Report page
     */
    public void repaint() {
        super.repaint();

        if (scrollPane != null)
            scrollPane.setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 2, GUI.getWindowFrameHeight() - 40));
        revalidate();
    }


    class InventoryItemSlice extends JPanel {

        private final int id;
        private String name;
        private int quantity;
        private int restock_level;
        private final String quantity_type;

        /**
         * Attaches data to necessary row in the restock report
         * @param row - A list of strings representing the data that needs to be spliced and reported
         */
        public InventoryItemSlice(ArrayList<String> row) {
            setLayout(new GridLayout(1, 4));
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            id = Integer.parseInt(row.get(0));
            name = row.get(1);
            quantity = (int) Double.parseDouble(row.get(2));
            restock_level = (int) Double.parseDouble(row.get(3));
            quantity_type = row.get(4);

            JLabel nameLabel = new JLabel("Item: " + name);
            JLabel quantityLabel = new JLabel("Quantity: " + quantity);
            JLabel restock_levelLabel = new JLabel("Restock Severity: " + (restock_level == 0 ? "CRITICAL!" : restock_level == 1 ? "Medium" : "low"));
            JLabel quantity_typeLabel = new JLabel("Size: " + quantity_type);
    
            // // Add the labels to the grid
            add(nameLabel);
            add(quantityLabel);
            add(quantity_typeLabel);
            add(restock_levelLabel);
            
        }


    }
}
