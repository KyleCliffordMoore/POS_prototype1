import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * Stock Page displays any table from the database. It prompts the user to input a table name.
 * 
 * It uses a simple query to select all the data from the table and displays it in a JTable.
 * 
 * @author Anzal Khan
 */

public class StockPage extends JPanel {

    private JTextField searchField;
    private JTable resultTable;
    private JScrollPane scrollPane;

    /**
     * Constructor for the stock page
     */
    public StockPage() {
        setLayout(new BorderLayout());

        // Top panel for search bar
        JPanel searchPanel = new JPanel(new FlowLayout());
        JLabel searchLabel = new JLabel("Enter Table Name: ");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        add(searchPanel, BorderLayout.NORTH);

        // Result table (Initially empty)
        resultTable = new JTable();
        scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);

        // Action listener for the search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tableName = searchField.getText().trim();
                if (!tableName.isEmpty()) {
                    loadTableData(tableName);
                } 
                else {
                    JOptionPane.showMessageDialog(StockPage.this, "Please enter a table name.");
                }
            }
        });


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
     * Method to check if the table exists in the database
     * @param tableName - the table name to check existence of in the database
     * @param connection - The connection to our database with credentials
     * @return - A boolean representing if the table exists or not
     */
    private boolean doesTableExist(String tableName, Connection connection) {
        boolean tableExists = false;
        PreparedStatement checkStatement = null;
        ResultSet resultSet = null;
        try {
            // Query to check if the table exists in the database
            String checkQuery = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_schema = 'public' AND table_name = ?)";
            checkStatement = connection.prepareStatement(checkQuery);
            checkStatement.setString(1, tableName);
            resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                tableExists = resultSet.getBoolean(1);  // True if table exists, false otherwise
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (checkStatement != null) checkStatement.close();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return tableExists;
    }

    /**
     * Method to load data from the SQL table and display it in the JTable
     * @param tableName - the table from the database to display
     */
    private void loadTableData(String tableName) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            // Establish the connection (Update with your connection details)
            connection = DriverManager.getConnection("jdbc:postgresql://csce-315-db.engr.tamu.edu/csce331_63", "csce331_63", "group63");

            // Check if the table exists
            if (!doesTableExist(tableName, connection)) {
                JOptionPane.showMessageDialog(this, "Error: The table '" + tableName + "' does not exist.");
                return;
            }

            // Create a scrollable ResultSet
            String query = "SELECT * FROM " + tableName;
            statement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery();

            // Fetch metadata to dynamically get the column names and data
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Column names
            String[] columnNames = new String[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                columnNames[i - 1] = metaData.getColumnName(i);
            }

            // Move cursor to the last row to get the row count, then back to the start
            resultSet.last();
            int rowCount = resultSet.getRow();
            resultSet.beforeFirst();

            // Data from the table
            Object[][] data = new Object[rowCount][columnCount];
            int rowIndex = 0;
            while (resultSet.next()) {
                for (int colIndex = 1; colIndex <= columnCount; colIndex++) {
                    data[rowIndex][colIndex - 1] = resultSet.getObject(colIndex);
                }
                rowIndex++;
            }

            // Create a new table model with the data
            resultTable.setModel(new javax.swing.table.DefaultTableModel(data, columnNames));

        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        } 
        finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } 
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
