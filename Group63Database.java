import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 *  Group63Database class is a class that establishes a connection to the group database and contains methods to query the database.
 */
public class Group63Database {
    
    /**
     * URL contains the url to the group database.
     */
    private static final String URL = "Its a secret :)";
    /**
     * USER_NAME contains the username to the group database.
     */
    private static final String USER_NAME = "Its a secret :)";
    /**
    * PASS_WORD contains the password to the group database.
    */
    private static final String PASS_WORD = "Its a secret :)";

    /**
     * Refrence to group database connection, initilized when init() is called.
     */
    public static Connection dataBaseConnection = null;

    /**
     * Establishes a connection to the database or exits if connection was not possible.
     */
    public static void init() {

        try {
            Class.forName("org.postgresql.Driver");
            dataBaseConnection = DriverManager.getConnection(URL, USER_NAME, PASS_WORD);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Closes connection to the database.
     */
    public static void close() {
        try {
            dataBaseConnection.close();
            JOptionPane.showMessageDialog(null,"Connection Closed.");
          } catch(Exception e) {
            JOptionPane.showMessageDialog(null,"Connection NOT Closed.");
          }
    }

    /**
     * queryResultSet takes in a Postgres SQL query as a String and then passes it to the group database.
     * 
     * @param query_ a Postgres SQL query
     * @return ResultSet A class containing information about SQL table returned by the Postgres SQL query.
     */
    public static ResultSet queryResultSet(String query_) {
        try{
            //create a statement object
            Statement stmt = dataBaseConnection.createStatement();
            //create an SQL statement
            String sqlStatement = query_;
            //send statement to DBMS
            ResultSet result = stmt.executeQuery(sqlStatement);
            // System.out.println(result.getString("quantity"));
            return result;
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Invalid Query");
        }
        return null; // Something bad happened
    }

    /**
     * resultSetTo2DArrayListString(ResultSet resultSet) takes in a ResultSet object, extracts the variables, converts them all to Strrings 
     * and then places them in a 2d Arraylist in row major order with the first row containing the titles of each column.
     * 
     * @param resultSet
     * @return ArrayList<ArrayList<String>> A 2d ArrayList of String in row major order, creating a table with the first row containing titles.
     * @throws SQLException Throws SQLException if ResultSet is NULL.
     */
    public static ArrayList<ArrayList<String>> resultSetTo2DArrayListString(ResultSet resultSet) throws SQLException {

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        //System.out.println(columnCount);

        
        ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
        ArrayList<String> header = new ArrayList<String>(columnCount);

        for (int i = 1; i < columnCount + 1; i++) {
            header.add(metaData.getColumnName(i));
        }
        table.add(header);

        while (resultSet.next()) {
            ArrayList<String> row = new ArrayList<String>(columnCount);

            for (int i = 1; i < columnCount+1; i++) {
                Object value = resultSet.getObject(i); // Get the value as Object

                row.add(value != null ? value.toString() : "null"); // Append the value or null
            }
            table.add(row);

        }

        return table;
    }

    /**
     * ArrayList<ArrayList<String>> query(String query_) takes in a PostgresSQL query and then returns the 2d table.
     * 
     * @param query_ String containing the PostgresSQL query.
     * @return ArrayList<ArrayList<String>> A 2d ArrayList of String in row major order, creating a table with the first row containing titles.
     */
    public static ArrayList<ArrayList<String>> query(String query_) {
        ArrayList<ArrayList<String>> table = null;
        ResultSet result = Group63Database.queryResultSet(query_);
        try{

            table = resultSetTo2DArrayListString(result);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Invalid Query");
        }
        return table;
    }

    /**
     * Takes in a 2d ArrayList of Strings and returns a pretty formated String.
     * 
     * @param table 2d ArrayList of Strings.
     * @return String representing the table in a 1d, pretty format.
     */
    public static String tableToStringPrettyFormat(ArrayList<ArrayList<String>> table) {

        StringBuilder toReturn = new StringBuilder();

        if (table.isEmpty()) return "";

        // Calculate the maximum width of each column
        int columnCount = table.get(0).size();
        int[] maxWidths = new int[columnCount]; // maxWidths

        // Determine the maximum width for each column
        for (ArrayList<String> row : table) 
            for (int i = 0; i < columnCount; i++) // Check if String is null?
                maxWidths[i] = Math.max(maxWidths[i], row.get(i).length());




        for (int rowIndex = 0; rowIndex < table.size(); rowIndex++) {
            ArrayList<String> row = table.get(rowIndex);

            for (int i = 0; i < columnCount; i++) {
                String item = row.get(i);

                // Format each element to align with the header
                toReturn.append(String.format("%-" + (maxWidths[i] + 3) + "s", item));
            }
            toReturn.append("\n");
        }

        return toReturn.toString();

        
    }

    /**
     * update(String updateStatement) takes in a PostgresSQL update query and returns True if the update succeeded and False if the update failed.
     * 
     * @param updateStatement A String representing the Postgres SQL update query.
     * @return returns True if update was sucessesful otherwise returns false.
     */
    public static boolean update(String updateStatement) {
        try{

            //create a statement object
            Statement stmt = dataBaseConnection.createStatement();
            //create an SQL statement
            String sqlStatement = updateStatement;
            //send statement to DBMS
            return stmt.execute(sqlStatement);
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,"Invalid Query");
        }
        return false; // Something bad happened
    }

    /**
     * insert(String insertStatement) takes in a PostgresSQL insert query.
     * 
     * @param insertStatement A String representing the Postgres SQL insert query.
     */
    public static void insert(String insertStatement) {
        
        try (Statement stmt = dataBaseConnection.createStatement()) {
            
            stmt.executeUpdate(insertStatement);
        } catch (SQLException e) {
            
            JOptionPane.showMessageDialog(null, "Invalid Query: " + e.getMessage());
        }
    }
    

}
