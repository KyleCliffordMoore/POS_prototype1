import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * Employee or Manager Select Page is a JPanel with two or three buttons depending on the user that logged in.
 * 
 * Employees will see two buttons, Employee and logout. And managers see an additional manager button.
 * 
 * @author Anzal Khan
 */

public class EmployeeOrManagerSelectionPage extends JPanel {
    /**
     * Constructor that sets the layout for choosing either employee or manager view
     * @see ManagerSelectPage.java
     * @see CashierPage.java
     * @param showManagerButton - is a boolean that enables the manager view to be accessed
     */
    public EmployeeOrManagerSelectionPage(boolean showManagerButton) {

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        // Title
        JLabel title = new JLabel("Choose View");
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(title, constraints);


        // Employee button
        JButton employeeButton = new JButton("Employee ");
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.NONE; 
        this.add(employeeButton, constraints);

        employeeButton.addActionListener(new ActionListener() {
            
            @Override
            /**
             * Void method for pressing employee or manager button
             * @param e - ActionEvent representing a button press
             */
            public void actionPerformed(ActionEvent e) {
                
                GUI.switchPage(new CashierPage());

                // JOptionPane.showMessageDialog(EmployeeOrManagerSelectionPage.this, "Button employee was clicked!");

                GUI.switchPage(new CashierPage());
            }
        });


        if (showManagerButton) {
            // Manager button
            JButton managerButton = new JButton("Manager");
            constraints.gridy = 2;
            this.add(managerButton, constraints);

            managerButton.addActionListener(new ActionListener() {
               
                @Override
                public void actionPerformed(ActionEvent e) {
    
                    GUI.switchPage(new ManagerSelectPage());
                }
            });
        }

        // Back button
        JButton logoutButton = new JButton("Logout");
        constraints.gridy = 10;  // Adjust position below other buttons
        // constraints.gridwidth = 3; // Span across multiple columns to center it
        // constraints.insets = new Insets(20, 10, 10, 10); // Add padding to separate from other buttons
        add(logoutButton, constraints);

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GUI.switchPage(new LoginPage());
            }
        });
    }
}