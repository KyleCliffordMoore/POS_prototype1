import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Generates a bar that contains add, remove, and edit buttons for a certain employee, this bar is a
 * member of {@link EmployeeManagerPage} and extends {@link JPanel}.
 * 
 * @see JPanel
 * @see EmployeeManagerPage
 * @see AddEmployee
 * @see EditEmployee
 */
public class EmployeeSlice extends JPanel {

    /**
    * Contains the refrence to the class that called it. 
    */
    private final EmployeeManagerPage parent;
    /**
     * Maximum length of characters to do displayed.
     */
    final static int MAX_CHAR_LENGTH = 50;
    /**
     * String representing employee name.
     */
    private String employName;
    /**
     * int representing id.
     */
    private final int id;

    /**
     * Generates a bar that contains add, remove, and edit buttons for a certain employee.
     * 
     * @param parent refrence to calling class
     * @param id id of the employee
     * @param employeeName name of the employee
     * 
     * @see EmployeeManagerPage
     * @see AddEmployee
     * @see EditEmployee
     */
    public EmployeeSlice(EmployeeManagerPage parent, int id, String employeeName) {

        this.parent = parent;
        this.employName = employeeName;
        this.id = id;
        EmployeeSlice me = this;

        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), 200));
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(1, 0, 1, 0);

        int spacing = MAX_CHAR_LENGTH - employeeName.length();
        JLabel name = new JLabel(employName + " ".repeat(spacing > 0 ? spacing : 0));
        name.setPreferredSize(new Dimension(GUI.getWindowFrameWidth()/5, 200));
        name.setFont(new Font("Courier New", Font.PLAIN, 12));
        JButton removeButton = new JButton("Remove Employee");
        JButton configButton = new JButton("Edit Employee");

        
        removeButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Group63Database.update("UPDATE employee SET position=\'Fired\' WHERE employee_id = " + id + ";");
                System.out.println("remove employee");
                parent.removeSlice(me);
                System.out.println(me);
            }
        });

        configButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                
                GUI.switchPage(new EditEmployee(id));
            }
        });

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        add(name, constraints);
        constraints.gridx = 1;
        add(removeButton, constraints);
        constraints.gridx = 2;
        add(configButton, constraints);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), getPreferredSize().height));
                revalidate();
                repaint();
            }
        });

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(new Color(200, 200, 200, 125));
        g.drawRect(0, 0, getWidth(), getHeight());
        

        
    }

    /**
     * 
     * @return the id of the employee.
     */
    public int getId() {return id;}

}
