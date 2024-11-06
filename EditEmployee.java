import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;

/**
 * Page that extends {@link JPanel} to edit Employees.
 * 
 * @see EmployeeSlice
 * @see EmployeeManagerPage
 */
public class EditEmployee extends JPanel {

    /**
     * ID of the employee.
     */
    private int employeeID;

    /**
     * Field to input ID
     */
    private JTextField idField;
    /**
     * Field to input name
     */
    private JTextField name;
     /**
     * Field to input position
     */
    private JTextField position;
    /**
     * Field to input hours
     */
    private JTextField hours;
        /**
     * Field to input pay
     */
    private JTextField pay;
    /**
     * Field to input password
     */
    private JPasswordField passwordField;
    /**
     * Field to input username
     */
    private JButton editButton;

    /**
     * Generates a new EditEmployee page
     * 
     * @param employeeID takes in the int ID of a particular employee that should be editted.
     */
    public EditEmployee(int employeeID) {
        this.employeeID = employeeID;

        ArrayList<String> employeeRow = Group63Database.query("SELECT * FROM employee WHERE employee_id = " + employeeID + ";").get(1);

        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight()));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel idJLabel = new JLabel("id, leave blank to generate new one ");
        idField = new JTextField(employeeRow.get(0),32);
        add(idField, constraints);
        constraints.gridx = 1;
        add(idJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        name = new JTextField(employeeRow.get(1), 32);
        add(name, constraints);
        JLabel nameJLabel = new JLabel("name");
        constraints.gridx = 1;
        add(nameJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        position = new JTextField(employeeRow.get(2), 32);
        add(position, constraints);
        JLabel posJLabel = new JLabel("position ");
        constraints.gridx = 1;
        add(posJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        hours = new JTextField(employeeRow.get(3), 32);
        add(hours, constraints);
        JLabel hrsLabel = new JLabel("hrs ");
        constraints.gridx = 1;
        add(hrsLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        pay = new JTextField(employeeRow.get(4), 32);
        add(pay, constraints);
        JLabel payLabel = new JLabel("pay ");
        constraints.gridx = 1;
        add(payLabel, constraints);
        

        constraints.gridy++;
        constraints.gridx = 0;
        passwordField = new JPasswordField(employeeRow.get(5), 32);
        add(passwordField, constraints);
        JLabel passwordLabel = new JLabel("password ");
        constraints.gridx = 1;
        add(passwordLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        editButton = new JButton("Edit Employee");
        add(editButton, constraints);


        JButton backToSelectionButton = new JButton("Back to Employee list");
        constraints.gridy = 99;
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(new EmployeeManagerPage());

            }

        });


        editButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                int id = -1;

                String textId = idField.getText();

                if (textId == null || textId.length() == 0) {
                    id = employeeID;
                } else {
                    id = Integer.parseInt(textId);
                }

                String sname = name.getText() != null ? name.getText() : "NULL";
                String pos = position.getText() != null ? position.getText() : "NULL";
                String pass = passwordField.getPassword() != null ? new String(passwordField.getPassword()) : "NULL";

                int hrsWorked = Integer.parseInt(hours.getText() != null ? hours.getText() : "NULL");
                int payInt = Integer.parseInt(pay.getText() != null ? pay.getText() : "NULL");

                String sqlStatement = 
                "UPDATE employee SET employee_id = "+id+
                ", name = '"+sname+"', position = '"+pos+"', hours = "+hrsWorked+", pay = "+payInt+", password = '"+pass+"' WHERE employee_id = "+employeeID+";";

                System.out.println(sqlStatement);

                Group63Database.update(sqlStatement);

                GUI.switchPage(new EmployeeManagerPage());
            }
        });
    }

}