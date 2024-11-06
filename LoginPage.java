import java.sql.*;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * LoginPage is a JPanel that generates a login page with fields for a username and password.
 * It includes the logic to handle login attempts and checks the user credentials against a database.
 * If the user is a manager, the user is redirected to a different page.
 * 
 * The login is validated based on information fetched from the database, and access is restricted 
 * for users marked as "Fired" in the database.
 * 
 * @author Kyle Moore
 */
public class LoginPage extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

     /**
     * Constructs the LoginPage by setting up the layout, input fields, and the login button.
     * It uses a GridBagLayout for placing components and includes an ActionListener for the login button.
     */

    public LoginPage() {

        // Use Java's layout manager
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();


        // Username field

        JLabel usernameLabel = new JLabel("Username: ");
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(usernameLabel, constraints);

        usernameField = new JTextField(24);
        constraints.gridx = 1;
        add(usernameField, constraints);


        // password field
        JLabel passwordLabel = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(passwordLabel, constraints);

        passwordField = new JPasswordField(24);
        constraints.gridx = 1;
        add(passwordField, constraints);

        // login button
        loginButton = new JButton("Login");
        constraints.gridx = 0;
        constraints.gridy = 2;
        add(loginButton, constraints);


        loginButton.addActionListener(new ActionListener() {
           /**
             * Handles the login action when the button is pressed. It retrieves the username and password,
             * performs a database query to verify credentials, and manages the user based on their role.
             * 
             * @param e The action event triggered by pressing the login button.
             */                                               
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Get user information from database
                try {
                    String queryString = "SELECT name, password, position from employee WHERE name = '" + username + "';";
                    ArrayList<ArrayList<String>> loginQuery = Group63Database.resultSetTo2DArrayListString(Group63Database.queryResultSet(queryString));
                    if (loginQuery.size() < 2){
                        System.out.println("Username was incorrect!");
                        return;
                    }

                    ArrayList<String> loginQueryRow = loginQuery.get(1);
                    String dbName = loginQueryRow.get(0);
                    String dbPassword = loginQueryRow.get(1);
                    String dbPosition = loginQueryRow.get(2);

                    if (dbPosition.equals("Fired")) {
                        System.out.println("This user does not have access!");
                    }
                    else if (username.equals(dbName) && password.equals(dbPassword)) {
                        System.out.println("Welcome");
                        boolean ifManager = (dbPosition.equals("Manager"));

                        GUI.switchPage(new EmployeeOrManagerSelectionPage(ifManager));
                    }
                    else {
                        System.out.println("Password was incorrect!");
                    }
                }
                catch (SQLException erro){
                    erro.printStackTrace();
                }
            }
        });
    }
}
