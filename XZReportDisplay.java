import java.sql.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.text.*; //DateFormat
import java.time.format.*;

/**
 * getXZReport class is a class that generates a X or Z report for the manager.
 */

public class XZReportDisplay extends JPanel {
    /**
    *
    * Displays a generated X or Z Report.
    * 
    * @param startDate Last recorded Z report date
    * @param endDate Current date
    * @param count Calculated number of sales between start and end date
    * @param revenue Calculated total revenue between start and end date
    */
    public XZReportDisplay(String startDate, String endDate, int count, double revenue) {

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Title (Manager Options) - Centered
        JLabel title = new JLabel("Report");
        title.setHorizontalAlignment(SwingConstants.CENTER);  // Ensure text is centered within the label

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3; // Span across multiple columns to center it
        constraints.anchor = GridBagConstraints.CENTER; // Center it in the grid
        constraints.insets = new Insets(10, 10, 20, 10); // Optional: Add padding for better spacing
        add(title, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        JLabel startDateLabel = new JLabel("Start date: " + startDate);
        add(startDateLabel, constraints);

        constraints.gridy = 2;
        JLabel endDateLabel = new JLabel("End date: " + endDate);
        add(endDateLabel, constraints);

        constraints.gridy = 3;
        JLabel countDateLabel = new JLabel("# Orders: " + count);
        add(countDateLabel, constraints);

        constraints.gridy = 4;
        JLabel revenueDateLabel = new JLabel("Revenue: $" + revenue);
        add(revenueDateLabel, constraints);



        JButton backToSelectionButton = new JButton("Back");
        constraints.gridy = 5;  // Adjust position below other buttons
        // constraints.gridwidth = 3; // Span across multiple columns to center it
        // constraints.insets = new Insets(20, 10, 10, 10); // Add padding to separate from other buttons
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            @Override
            /**
             * Handles the back button being clicked. Switches the page to the X Z Report page.
             * 
             * @param e The action event triggered by pressing the back button.
             */   
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new XZReportsPage());
            }
        });
    }

}