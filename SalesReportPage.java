import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Sales Report Page displays a sales report of every item sold in given a time window
 * 
 * It uses a big query to display the total revenue of each item sold in the time window
 * 
 * @author Anzal Khan
 */

public class SalesReportPage extends JPanel {

    private JTextField startTimeField, endTimeField;
    private JTable reportTable;
    private JScrollPane scrollPane;
    /**
     * Constructor for the sales report page
     */
    public SalesReportPage() {
        setLayout(new BorderLayout());

        // Top panel for entering time window
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel startTimeLabel = new JLabel("Start Date & Time (YYYY-MM-DD HH:MM:SS): ");
        startTimeField = new JTextField(20);
        JLabel endTimeLabel = new JLabel("End Date & Time (YYYY-MM-DD HH:MM:SS): ");
        endTimeField = new JTextField(20);
        JButton generateReportButton = new JButton("Generate Report");

        inputPanel.add(startTimeLabel);
        inputPanel.add(startTimeField);
        inputPanel.add(endTimeLabel);
        inputPanel.add(endTimeField);
        inputPanel.add(generateReportButton);

        add(inputPanel, BorderLayout.NORTH);

        // Table to display the sales report
        reportTable = new JTable();
        scrollPane = new JScrollPane(reportTable);
        add(scrollPane, BorderLayout.CENTER);

        // Action listener for the Generate Report button
        generateReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String startTime = startTimeField.getText().trim();
                String endTime = endTimeField.getText().trim();

                if (!startTime.isEmpty() && !endTime.isEmpty()) {
                    generateReport(startTime, endTime);
                } else {
                    JOptionPane.showMessageDialog(SalesReportPage.this, "Please enter valid start and end times.");
                }
            }
        });

        // Back to Manager Selection Button
        JButton backToSelectionButton = new JButton("Back to Manager Selection");
        add(backToSelectionButton, BorderLayout.SOUTH);

        backToSelectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new ManagerSelectPage());
            }
        });
    }

    /**
     * Method to generate the sales report based on the time window
     * @param startTime - string that represents the time stamp of the lower bound of the window
     * @param endTime - string that represents the time stamp of the upper bound of the window
     */
    private void generateReport(String startTime, String endTime) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish connection
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_63", "csce331_63", "group63");

            // SQL query using date and time comparison with casting
            String query = "SELECT meat AS item, COUNT(*) AS count, SUM(price) AS total_revenue " +
            "FROM ( " +
            "    SELECT mi.meat1 AS meat, mi.price " +
            "    FROM meal_item mi " +
            "    JOIN line_item li ON mi.line_item_id = li.line_item_id " +
            "    JOIN receipt r ON li.receipt_id = r.receipt_id " +
            "    WHERE r.date BETWEEN ? AND ? " +
            "      AND r.order_time BETWEEN ? AND ? " +
            "      AND mi.meat1 <> '' " +
            "    UNION ALL " +
            "    SELECT mi.meat2, mi.price " +
            "    FROM meal_item mi " +
            "    JOIN line_item li ON mi.line_item_id = li.line_item_id " +
            "    JOIN receipt r ON li.receipt_id = r.receipt_id " +
            "    WHERE r.date BETWEEN ? AND ? " +
            "      AND r.order_time BETWEEN ? AND ? " +
            "      AND mi.meat2 <> '' " +
            "    UNION ALL " +
            "    SELECT mi.meat3, mi.price " +
            "    FROM meal_item mi " +
            "    JOIN line_item li ON mi.line_item_id = li.line_item_id " +
            "    JOIN receipt r ON li.receipt_id = r.receipt_id " +
            "    WHERE r.date BETWEEN ? AND ? " +
            "      AND r.order_time BETWEEN ? AND ? " +
            "      AND mi.meat3 <> '' " +
            "    UNION ALL " +
            "    SELECT mi.side, mi.price " +
            "    FROM meal_item mi " +
            "    JOIN line_item li ON mi.line_item_id = li.line_item_id " +
            "    JOIN receipt r ON li.receipt_id = r.receipt_id " +
            "    WHERE r.date BETWEEN ? AND ? " +
            "      AND r.order_time BETWEEN ? AND ? " +
            "      AND mi.side <> '' " +
            ") AS combined_items " +
            "GROUP BY item " +
            "UNION ALL " +
            "SELECT di.name AS item, COUNT(*) AS count, SUM(di.price) AS total_revenue " +
            "FROM drink_item di " +
            "JOIN line_item li ON di.line_item_id = li.line_item_id " +
            "JOIN receipt r ON li.receipt_id = r.receipt_id " +
            "WHERE r.date BETWEEN ? AND ? " +
            "  AND r.order_time BETWEEN ? AND ? " +
            "GROUP BY di.name " +
            "UNION ALL " +
            "SELECT ai.name AS item, COUNT(*) AS count, SUM(ai.price) AS total_revenue " +
            "FROM appetizer_item ai " +
            "JOIN line_item li ON ai.line_item_id = li.line_item_id " +
            "JOIN receipt r ON li.receipt_id = r.receipt_id " +
            "WHERE r.date BETWEEN ? AND ? " +
            "  AND r.order_time BETWEEN ? AND ? " +
            "GROUP BY ai.name;";


            // Prepare statement with scrollable ResultSet
            statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            
            // Convert string to Timestamp
            // Timestamp startTimestamp = Timestamp.valueOf(startTime);
            // Timestamp endTimestamp = Timestamp.valueOf(endTime);
            
            // Set parameters for the date range (start and end date)
            // Split the input time to separate date and time parts
            java.sql.Date startDate = java.sql.Date.valueOf(startTime.split(" ")[0]);
            java.sql.Date endDate = java.sql.Date.valueOf(endTime.split(" ")[0]);
            Timestamp startTimestamp = Timestamp.valueOf(startTime);
            Timestamp endTimestamp = Timestamp.valueOf(endTime);

            // Set the parameters in a loop
            for (int i = 1; i < 12; i += 2) {
                // First, set the date values (for r.date)
                statement.setDate(2 * i - 1, startDate);
                statement.setDate(2 * i, endDate);
                
                // Then set the timestamp values (for r.order_time)
                statement.setTimestamp(2 * i + 1, startTimestamp);
                statement.setTimestamp(2 * i + 2, endTimestamp);
            }       

            resultSet = statement.executeQuery();

            // Check if the result set is empty
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(this, "No data found for the specified time range.");
                return; // Exit if no data
            }

            // Move the cursor back to the beginning of the result set
            resultSet.beforeFirst();

            // Get metadata and column count
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Fetch rows of data
            resultSet.last(); // Move to last row to get row count
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst(); // Move back to the beginning

            Object[][] data = new Object[rowCount][columnCount];
            int rowIndex = 0;
            while (resultSet.next()) {
                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    data[rowIndex][colIndex - 1] = resultSet.getObject(colIndex);
                }
                rowIndex++;
            }

            // Update table model with new data
            reportTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Please use YYYY-MM-DD HH:MM:SS.");
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
