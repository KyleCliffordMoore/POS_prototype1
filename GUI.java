import java.awt.event.*;
import javax.swing.*;
import java.awt.Component;

/**
 * GUI class is the main class for the GUI system. It initializes the GUI system as the Login Page 
 * and establishes a connection to the database.
 */

public class GUI extends JFrame implements ActionListener {
    private static JFrame windowFrame;
    private static Component currentPage;

    /**
    *
    * This function attempts to dispose current window and switch to the specified newPage.
    * Also updates currentPage constant and revalidates the window
    *
    * @params newPage - is the new page to redirect to 
    *
    */
    public static void switchPage(Component newPage) {
      windowFrame.remove(currentPage);
      windowFrame.add(newPage);
      currentPage = newPage;
      windowFrame.revalidate();
      windowFrame.repaint();
    }

    public static int getWindowFrameWidth() { return windowFrame.getWidth(); }
    public static int getWindowFrameHeight() { return windowFrame.getHeight(); }


    /**
    *
    * The main function initializes the GUI system as the Login Page and
    * establishes a connection to the database
    *
    * @params args - list of arguments for main to perform (NOT USED)
    * @see LoginPage
    *
    */
    public static void main(String[] args)
    {
      //Building the connection
      Group63Database.init();

      // create Main Frame
      windowFrame = new JFrame("DB GUI");

      //set the page ----------------------------------change back after testing
      currentPage = new LoginPage();
      windowFrame.add(currentPage);
      
      //windowFrame settings
      windowFrame.setSize(400, 400);
      windowFrame.setLocationRelativeTo(null);
      windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      windowFrame.setVisible(true);
    }
 
    /**
    *
    * This function attempts to dispose all windows and close access to the database
    * if the Action Command e is "Close"
    *
    * @params e - is an action event performed by the user
    *
    */
    public void actionPerformed(ActionEvent e)
    {
        String s = e.getActionCommand();
        if (s.equals("Close")) {
          windowFrame.dispose();
          Group63Database.close();
        }
    }
}

      


