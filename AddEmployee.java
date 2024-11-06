import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.sql.*;

/**
 * AddEmployee extends {@link JPanel} and is a page that allows the user to input information on a new employee, which will then
 * update the group database ({@link Group63Database}).
 * 
 * @see Group63Database 
 * @see EmployeeManagerPage 
 * @see EmployeeSlice 
 * @author Kyle Moore
 */
public class AddEmployee extends JPanel {
    
    private JTextField idField;
    private JTextField name;
    private JTextField position;
    private JTextField hours;
    private JTextField pay;
    private JPasswordField passwordField;
    private JButton createButton;

    /**
     * AddEmployee() initializes a new page, this page will handle adding an employee to the database.
     * 
     * @see EmployeeManagerPage 
     * @see EmployeeSlice 
     */
    public AddEmployee() {
        
        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight()));

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        constraints.gridx = 0;
        constraints.gridy = 0;
        JLabel idJLabel = new JLabel("id, leave blank to generate new one ");
        idField = new JTextField(32);
        add(idField, constraints);
        constraints.gridx = 1;
        add(idJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        name = new JTextField(32);
        add(name, constraints);
        JLabel nameJLabel = new JLabel("name");
        constraints.gridx = 1;
        add(nameJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        position = new JTextField(32);
        add(position, constraints);
        JLabel posJLabel = new JLabel("position ");
        constraints.gridx = 1;
        add(posJLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        hours = new JTextField(32);
        add(hours, constraints);
        JLabel hrsLabel = new JLabel("hrs ");
        constraints.gridx = 1;
        add(hrsLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        pay = new JTextField(32);
        add(pay, constraints);
        JLabel payLabel = new JLabel("pay ");
        constraints.gridx = 1;
        add(payLabel, constraints);
        

        constraints.gridy++;
        constraints.gridx = 0;
        passwordField = new JPasswordField(32);
        add(passwordField, constraints);
        JLabel passwordLabel = new JLabel("password ");
        constraints.gridx = 1;
        add(passwordLabel, constraints);

        constraints.gridy++;
        constraints.gridx = 0;
        createButton = new JButton("Add Employee");
        add(createButton, constraints);

        JButton backToSelectionButton = new JButton("Back to Employee List");
        constraints.gridy = 99;
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(new EmployeeManagerPage());

            }

        });

        createButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                int id = -1;

                String textId = idField.getText();

                if (textId == null || textId.length() == 0) {
                    ResultSet res = Group63Database.queryResultSet("SELECT MAX(employee_id) FROM employee;");
                    try {
                        res.next();
                        id = res.getInt(1) + 1;
                    } catch (SQLException e1) {
                        System.err.println("Failed to query id");
                        e1.printStackTrace();
                    }
                } else {
                    id = Integer.parseInt(textId);
                }

                String sname = name.getText() != null ? name.getText() : "NULL";
                String pos = position.getText() != null ? position.getText() : "NULL";
                String pass = passwordField.getPassword() != null ? new String(passwordField.getPassword()) : "NULL";

                int hrsWorked = Integer.parseInt(hours.getText() != null ? hours.getText() : "NULL");
                int payInt = Integer.parseInt(pay.getText() != null ? pay.getText() : "NULL");

                String sqlStatement = "INSERT INTO employee (employee_id, name, position, hours, pay, password) VALUES (" 
                + id + ", '" + sname + "', '" + pos + "', " + hrsWorked + ", " + payInt + ",\r\n" + " '" + pass + "')";

                System.out.println(sqlStatement);

                Group63Database.update(sqlStatement);

                GUI.switchPage(new EmployeeManagerPage());
            }
        });
    }

}
