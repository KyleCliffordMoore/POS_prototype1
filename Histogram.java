import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * 
 * Generate a histogram then we drawHistogram is called, the draws the histogram.
 * This class Histogram extends {@link JPanel} and is placed inside the {@Link GraphsPage}
 * 
 * @see JPanel
 * @see GraphsPage
 */
public class Histogram extends JPanel {

    /**
     * ArrayList containing the labels that will be displayed above each bar.
     */
    ArrayList<String> labels;
    /**
    * ArrayList containing the amounts that will be displayed above each bar.
    */
    ArrayList<Double> amounts;
    /**
     * int to keep track of the current page of the histogram.
     */
    int currentPage = 0;

    /**
     * final int representing the number of bars to have on each page.
     */
    final int barsPerPage = 5;

    /**
     * The Histogram constructor takes in the labels and the amounts then builds a histogram.
     * 
     * @param labels An ArrayList of names for each item to be displayed.
     * @param amounts An arraylist of the amount of each item to be displayed.
     */
    public Histogram(ArrayList<String> labels, ArrayList<Double> amounts) {
        this.labels = labels;
        this.amounts = amounts;
        setPreferredSize(new Dimension(GUI.getWindowFrameWidth() / 2, GUI.getWindowFrameHeight() / 2));
        setLayout(new BorderLayout());
        // Create a button panel for pagination
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        
        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    revalidate();
                    repaint();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * barsPerPage < labels.size()) {
                    currentPage++;
                    revalidate();
                    repaint();
                }
            }
        });

        JLabel title = new JLabel("Inventory", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15)); // Set font size to 20
        add(title, BorderLayout.NORTH);

        JButton backToSelectionButton = new JButton("Back to Manager Selection");

        // Layout for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(backToSelectionButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        backToSelectionButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {

                GUI.switchPage(new ManagerSelectPage());

            }

        });

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * drawHistogram(Graphics g) draws the histogram
     * @param g is the graphics object being used
     * 
     * @see Graphics
     */
    private void drawHistogram(Graphics g) {
        Map<String, Double> frequencyMap = new HashMap<>();

        for (int i = 0; i < labels.size(); i++) {
            frequencyMap.put(labels.get(i), amounts.get(i));
        }

        final int barWidth = 50;
        final int maxHeight = 200;
        final int spacing = 20;
        double largestAmount = frequencyMap.values().stream().max(Double::compare).orElse(Double.NaN);

        g.setFont(new Font("Arial", Font.PLAIN, 10));

        int x = spacing;
        for (int i = 0; i < barsPerPage; i++) {
            int index = i + currentPage * barsPerPage;
            if (index >= labels.size()) break;

            String label = labels.get(index);
            double amount = amounts.get(index);
        
            // Truncate label to first 10 characters and append "..." if necessary
            if (label.length() > 10) {
                label = label.substring(0, 10) + "...";
            }
        
            // Calculate bar height based on frequency
            int barHeight = (int) (amount / largestAmount * maxHeight);
        
            g.setColor(Color.BLUE);
            g.fillRect(x, getHeight() - barHeight - 50, barWidth, barHeight);
        
            g.setColor(Color.BLACK);
            g.drawString(        label         , x + (barWidth / 2) - 20, getHeight() - barHeight - 70);
        
            g.drawString(String.valueOf(amount), x + (barWidth / 2) - 20, getHeight() - barHeight - 60 );
        
            // Move to the next bar position (after drawing the bar and label)
            x += barWidth + spacing;
        }
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawHistogram(g);
    }
}