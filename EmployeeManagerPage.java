import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;
import java.sql.*;

/**
 * Employee Manager Page 
 * 
 * @author Kyle Moore
 */

public class EmployeeManagerPage extends JPanel {

    ArrayList<EmployeeSlice> employeeSlices;

    EmployeeManagerPage() {

        employeeSlices = new ArrayList<EmployeeSlice>();

        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        JButton addEmployeeButton = new JButton("Add new Employee");
        constraints.gridy = 1;
        add(addEmployeeButton, constraints);

        JButton backToSelectionButton = new JButton("Back to Manager Select");
        constraints.gridy = 0;
        add(backToSelectionButton, constraints);

        backToSelectionButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(new ManagerSelectPage());

            }

        });

        addEmployeeButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(new AddEmployee());

            }

        });

        try {

            ResultSet employeeSet = Group63Database.queryResultSet("SELECT employee_id, name FROM EMPlOYEE WHERE position != \'Fired\'");


            constraints.gridx = 0;
            constraints.gridy = 2;

            while(employeeSet.next()) {

                EmployeeSlice employeeSlice = new EmployeeSlice(this, employeeSet.getInt(1), employeeSet.getString(2));

                constraints.gridy++;

                add(employeeSlice, constraints);
                employeeSlices.add(employeeSlice);
            }

        } catch (SQLException e) {
            System.err.println("Could\'nt query Employee Database?");
            e.printStackTrace();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);

        g.setColor(getBackground());
        g.fillRect(0, 0, GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight());

        for (EmployeeSlice employeeSlice: employeeSlices) {
            employeeSlice.validate();
            employeeSlice.repaint();
        }

    }

    public boolean removeSlice(EmployeeSlice employeeSlice) {
        boolean toReturn = employeeSlices.remove(employeeSlice);
        this.remove(employeeSlice);
        revalidate();
        repaint();
        paintComponent(getGraphics());

        return toReturn;
    }

}
