import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.text.*; //DateFormat
import java.time.format.*;

/**
 * XZReportsPage class is a class that generates a X or Z report for the manager.
 */

public class XZReportsPage extends JPanel {
    // private JTextField usernameField;
    // private JPasswordField passwordField;
    // private JButton loginButton;

    JLabel startDate;
    JLabel currentDate;
    JLabel orderNumber;
    JLabel totalSales;

    /**
    * This displays the button to generate an X or Z Report, as well as go back to the manager page.
    */
    public XZReportsPage() {

        // Use Java's layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.CENTER; // Center it in the grid
        constraints.insets = new Insets(10, 10, 20, 10); // Padding

        JButton XReportButton = new JButton("X Report");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(XReportButton, constraints);

        JButton ZReportButton = new JButton("Z Report (Final)");
        constraints.gridy = 1;
        constraints.gridx = 1;
        add(ZReportButton, constraints);

        JButton backToSelectionButton = new JButton("Back to Manager Selection");
        constraints.gridy = 10;
        add(backToSelectionButton, constraints);

        //Redirect to manager selection page
        backToSelectionButton.addActionListener(new ActionListener() {
            /**
             * Handles the back button being clicked. Switches the page to the manager selection page.
             * 
             * @param e The action event triggered by pressing the back button.
             */   
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new ManagerSelectPage());
            }
        });



        //Calculating x reports
        XReportButton.addActionListener(new ActionListener() {
            @Override
            /**
             * Handles the X Report button being clicked. Calls the perform queries function to display calculated sales data.
             * Switches the page to the X Z Report display page.
             * 
             * @param e The action event triggered by pressing the X Report button.
             */    
            public void actionPerformed(ActionEvent e) {
                
                performQueries(false);
            }
        });

        
        //Calculating z reports
        ZReportButton.addActionListener(new ActionListener() {
            @Override
            /**
             * Handles the Z Report button being clicked. Calls the perform queries function to display calculated sales data.
             * Switches the page to the X Z Report display page. Updates the last recorded Z Report date in the database.
             * 
             * @param e The action event triggered by pressing the Z Report button.
             */    
            public void actionPerformed(ActionEvent e) {
                
                performQueries(true);
            }
        });
    }

    /**
    *
    * Calculates the total revenue and number of sales between the last Z Report date and the current date,
    * then switches page to the generated report display. 
    * If update_db is true, it updates the last Z Report date in the Z Report table.
    * 
    * @param update_db Boolean that describes if to update the last recorded Z Report date
    */
    void performQueries(boolean update_db) {
        String lastZReportString = "select * from z_report;";
        
        String currentDateString = (new java.util.Date()).toString();
        System.out.println(currentDateString);
        try {
            Double sum = 0.0;
            Integer numOrders = 0;
            String lastZReportDayString = Group63Database.resultSetTo2DArrayListString(Group63Database.queryResultSet("select * from z_report")).get(1).get(0);

            //Meal revenue
            String fromLastZReportMealString = "SELECT mi.size, COUNT(*) as count, SUM(mi.price) as total_revenue FROM meal_item mi JOIN line_item li ON mi.line_item_id = li.line_item_id JOIN receipt r ON li.receipt_id = r.receipt_id WHERE r.date BETWEEN '" + lastZReportDayString + "' AND '" + currentDateString +"' GROUP BY mi.size";
            ArrayList<ArrayList<String>> mealRevenueTable = Group63Database.resultSetTo2DArrayListString(Group63Database.queryResultSet(fromLastZReportMealString));
            for (int i = 1; i < mealRevenueTable.size(); i++){
                numOrders += Integer.parseInt(mealRevenueTable.get(i).get(1));
                sum += Double.parseDouble(mealRevenueTable.get(i).get(2));
            }

            //Drink revenue
            String fromLastZReportDrinkString = "SELECT di.name, COUNT(*) as count, SUM(di.price) as total_revenue FROM drink_item di JOIN line_item li ON di.line_item_id = li.line_item_id JOIN receipt r ON li.receipt_id = r.receipt_id WHERE r.date BETWEEN '" + lastZReportDayString + "' AND '" + currentDateString + "' GROUP BY di.name";
            ArrayList<ArrayList<String>> drinkRevenueTable = Group63Database.resultSetTo2DArrayListString(Group63Database.queryResultSet(fromLastZReportDrinkString));
            for (int i = 1; i < drinkRevenueTable.size(); i++){
                numOrders += Integer.parseInt(drinkRevenueTable.get(i).get(1));
                sum += Double.parseDouble(drinkRevenueTable.get(i).get(2));
            }

            String fromLastZReportAppetizerString = "SELECT ai.name, COUNT(*) as count, SUM(ai.price) as total_revenue FROM appetizer_item ai JOIN line_item li ON ai.line_item_id = li.line_item_id JOIN receipt r ON li.receipt_id = r.receipt_id WHERE r.date BETWEEN '" + lastZReportDayString + "' AND '" + currentDateString + "' GROUP BY ai.name";
            ArrayList<ArrayList<String>> appetizerRevenueTable = Group63Database.resultSetTo2DArrayListString(Group63Database.queryResultSet(fromLastZReportAppetizerString));
            for (int i = 1; i < appetizerRevenueTable.size(); i++){
                numOrders += Integer.parseInt(appetizerRevenueTable.get(i).get(1));
                sum += Double.parseDouble(appetizerRevenueTable.get(i).get(2));
            }
            System.out.println(numOrders);
            System.out.println(sum);


            if (update_db) {
                System.out.println("current date:  " + currentDateString);
                Group63Database.update("UPDATE z_report SET date = '"+ currentDateString +"'");
            }

            System.out.println(lastZReportDayString + " " + currentDateString);
            GUI.switchPage(new XZReportDisplay(lastZReportDayString, currentDateString, numOrders, sum));

        }
        catch (SQLException erro){
            // erro.printStackTrace();
        }
    }

}
