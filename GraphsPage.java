import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

/**
 * GraphsPage is a {@link JPanel} wich will create a new page which will
 * query {@link Group63Database} and then generate a graph of the amount of supplies used during
 * a set time peroid.
 * 
 * The page will contain a start and end date field, a next page of the graph button, a back page for the graph, and finally
 * a return to home {@link ManagerSelectPage}
 * 
 * @author Kyle Moore
 * 
 * @see ManagerSelectPage 
 * @see Histogram 
 */
public class GraphsPage extends JPanel {

    /**
     * Refrence to the histogram that is supposed to be displayed inbetween the title and the next/back/homepage buttons.
     * 
     * @see Histogram
     */
    Histogram hist;

    /**
     * Gets the labels of every Appetizer from the {@link Group63Database} and the amount of times that it appears.
     * 
     * @param items
     * takes in the refrence to a HashMap<String, Integer>.
     * String represents the label.
     * Integer represents the amount of times that label appears in the Postgres SQL query.
     * @param startDate Start date as a String
     * @param endDate end date as a String
     * @return the refrence to the hashmap
     * @apiNote note that the inputed hashmap is updated.
     */
    private static HashMap<String, Integer> getMapOfAppItems(HashMap<String, Integer> items, String startDate, String endDate) {
        ArrayList<ArrayList<String>> drink_table = Group63Database.query("SELECT di.name\n" + //
                        "FROM appetizer_item di\n" + //
                        "JOIN line_item li ON di.line_item_id = li.line_item_id\n" + //
                        "JOIN receipt r ON li.receipt_id = r.receipt_id\n" + //
                        "WHERE r.date BETWEEN '"+startDate+"' AND '"+endDate+"';");

        for (int j = 1 ; j < drink_table.size(); j++) {
            String key = drink_table.get(j).get(0);
            // System.out.println(key);
            items.put(key, items.getOrDefault(key, 0) + 1);
        }

        return items;
    }

    /**
     * Gets the labels of every Drink item from the {@link Group63Database} and the amount of times that it appears.
     * 
     * @param items
     * takes in the refrence to a HashMap<String, Integer>.
     * String represents the label.
     * Integer represents the amount of times that label appears in the Postgres SQL query.
     * @param startDate Start date as a String
     * @param endDate end date as a String
     * @return the refrence to the hashmap
     * @apiNote note that the inputed hashmap is updated.
     */
    private static HashMap<String, Integer> getMapOfDrinkItems(HashMap<String, Integer> items, String startDate, String endDate) {
        ArrayList<ArrayList<String>> drink_table = Group63Database.query("SELECT di.name\n" + //
                        "FROM drink_item di\n" + //
                        "JOIN line_item li ON di.line_item_id = li.line_item_id\n" + //
                        "JOIN receipt r ON li.receipt_id = r.receipt_id\n" + //
                        "WHERE r.date BETWEEN '"+startDate+"' AND '"+endDate+"';");

        for (int j = 1 ; j < drink_table.size(); j++) {
            String key = drink_table.get(j).get(0);
            // System.out.println(key);
            items.put(key, items.getOrDefault(key, 0) + 1);
        }

        return items;
    }

    /**
     * Gets the labels of every Meal from the {@link Group63Database} and the amount of times that it appears.
     * 
     * @param items
     * takes in the refrence to a HashMap<String, Integer>.
     * String represents the label.
     * Integer represents the amount of times that label appears in the Postgres SQL query.
     * @param startDate Start date as a String
     * @param endDate end date as a String
     * @return the refrence to the hashmap
     * @apiNote note that the inputed hashmap is updated.
     */
    private static HashMap<String, Integer> getMapOfMealItems(HashMap<String, Integer> items, String startDate, String endDate) {
        String myquery = "SELECT mi.size, meat1, meat2, meat3, side, date\r\n" + //
            "        FROM meal_item mi\r\n" + //
            "        JOIN line_item li ON mi.line_item_id = li.line_item_id\r\n" + //
            "        JOIN receipt r ON li.receipt_id = r.receipt_id\r\n" + //
            "        WHERE r.date BETWEEN '"+startDate+"' AND '"+endDate+"'; ";

        // System.err.println(myquery);
        ArrayList<ArrayList<String>> meal_table = Group63Database.query(myquery);

        // Group63Database.tableToStringPrettyFormat(meal_table);
        // System.out.println("Size: " + meal_table.size() + "           size: " + meal_table.get(0));

        for (int j = 1 ; j < meal_table.size(); j++) {
            String size = meal_table.get(j).get(0);
            if (size.equals("Bowl")) {
                String key = meal_table.get(j).get(1);
                items.put(key, items.getOrDefault(key, 0) + 1);
            } else if (size.equals("Plate")) {
                String key = meal_table.get(j).get(1);
                items.put(key, items.getOrDefault(key, 0) + 1);
                String key_ = meal_table.get(j).get(2);
                items.put(key_, items.getOrDefault(key_, 0) + 1);
            } else if (size.equals("Bigger Plate")) {
                String key = meal_table.get(j).get(1);
                items.put(key, items.getOrDefault(key, 0) + 1);
                String key_ = meal_table.get(j).get(2);
                items.put(key_, items.getOrDefault(key_, 0) + 1);
                String key__ = meal_table.get(j).get(3);
                items.put(key__, items.getOrDefault(key__, 0) + 1);
            }
        }
        return items;
    }

    /**
     * Gets the labels of every Ingredient from the {@link Group63Database} and the amount of that ingredient.
     * 
     * @param items
     * takes in the refrence to a HashMap<String, Integer>.
     * String represents the label.
     * Integer represents the amount of times that label appears in the Postgres SQL query.
     * @param startDate Start date as a String
     * @param endDate end date as a String
     * @return the refrence to the hashmap
     * @apiNote note that the inputed hashmap is updated.
     */
    private static HashMap<String, Double> getIngredientNames(HashMap<String, Integer> items) {

        HashMap<String, Double> ingredients = new HashMap<String, Double>();

        for (String key : items.keySet()) {
            ArrayList<ArrayList<String>> mytable = Group63Database.query("SELECT i.name, i.quantity\r\n" + //
                                "FROM ingredient i\r\n" + //
                                "JOIN recipe_ingredient ri ON i.ingredient_id = ri.ingredient_id\r\n" + //
                                "WHERE ri.recipe_id = (\r\n" + //
                                "    SELECT recipe_id\r\n" + //
                                "    FROM recipe\r\n" + //
                                "    WHERE name = 'Orange Chicken'\r\n" + //
                                ");");
            
            // System.err.println(mytable.get(1).get(0));

            for (int j = 1; j < mytable.size(); j++) {
                ingredients.put(mytable.get(j).get(0), ingredients.getOrDefault(mytable.get(j).get(0), 0.0)
                + items.get(key) * Double.parseDouble(mytable.get(j).get(1))
                );
            }
        }

        // for (String key : ingredients.keySet()) System.out.println(key + "      " + ingredients.get(key));

        return ingredients;
    }

    /**
     * Gets the labels of every Meal, drink item, and appetizer item from the {@link Group63Database} and the amount of times that it appears.
     * 
     * @param startDate Start date as a String
     * @param endDate end date as a String
     * @return the refrence to the hashmap where the String represents the item and the Integer represents the amount of time that item appears in the query
     * @apiNote note that the inputed hashmap is updated.
     * 
     * @see getMapOfAppItems()
     * @see getMapOfDrinkItems()
     * @see getMapOfMealItems()
     */
    private static HashMap<String, Integer> getNameOfMealItem_DrinkItem_AppItem(String startDate, String endDate) {

        HashMap<String, Integer> items = new HashMap<String, Integer>();

        // Should be pass by refrence and setting it equal is redundant...
        getMapOfMealItems(items, startDate, endDate);

        // for (String key : items.keySet()) System.out.println(key);

        getMapOfDrinkItems(items, startDate, endDate);
        getMapOfAppItems(items, startDate, endDate);

        return items;
    }

    /**
     * Pass in a hashmap of items labels and the amount of time they are used, then queryies {@link Group63Database} 
     * and returns the amount of ingredients used.
     * 
     * @param items
     * takes in the refrence to a HashMap<String, Integer>.
     * String represents the label.
     * Integer represents the amount of times that label appears in the Postgres SQL query.
     * @return a hashmap
     * where String represents the label of ingredient
     * and Double represents the amount of that ingredient was used.
     */
    private static HashMap<String, Double> getIngredients(String startDate, String endDate) {
        // System.err.println(startDate + "                         " + endDate);
        return getIngredientNames(getNameOfMealItem_DrinkItem_AppItem(startDate, endDate));
    }

    /**
     * GraphsPage is a {@link JPanel} wich will create a new page which will
     * query {@link Group63Database} and then generate a graph of the amount of supplies used during
     * a set time peroid.
     * 
     * The page will contain a start and end date field, a next page of the graph button, a back page for the graph, and finally
     * a return to home {@link ManagerSelectPage}
     * 
     * @see ManagerSelectPage 
     * @see Histogram
     */
    public GraphsPage() {

        /*SELECT mi.size, meat1, meat2, meat3, side, date
        FROM meal_item mi
        JOIN line_item li ON mi.line_item_id = li.line_item_id
        JOIN receipt r ON li.receipt_id = r.receipt_id
        WHERE r.date BETWEEN '2024-1-1' AND '2024-2-2'; */

        String startDate = "2024-1-1";
        String endDate   = "2024-2-2";

        HashMap<String, Double> data = getIngredients(startDate, endDate);

        setPreferredSize(new Dimension(GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight()));

        // Set layout and add components later
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.CENTER;

        ArrayList<String> labels = new ArrayList<String>();
        ArrayList<Double> amount = new ArrayList<Double>();

        for (Map.Entry<String, Double> entry : data.entrySet()) {
            labels.add(entry.getKey()); 
            amount.add(entry.getValue()); 
        }
        hist = new Histogram(labels, amount);
        add(hist, constraints);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidate();
                repaint();
                // System.out.println("ran");
                setSize(new Dimension(GUI.getWindowFrameWidth(), GUI.getWindowFrameHeight()));
            }
        });


        constraints.weightx = 2.0;
        constraints.weighty = 2.0;
        // Buttons
        SpinnerDateModel dateModel_start = new SpinnerDateModel();
        JSpinner dateSpinner_start = new JSpinner(dateModel_start);
        
        // Set the editor to use a specific date format (yyyy-MM-dd)
        JSpinner.DateEditor dateEditor_start = new JSpinner.DateEditor(dateSpinner_start, "yyyy-MM-dd");
        dateSpinner_start.setEditor(dateEditor_start);

        // Set the spinner to the current date
        dateModel_start.setValue(new Date());

        // Add the date spinner to the GraphPage
        add(new JLabel("Select Start:"));
        add(dateSpinner_start);

        SpinnerDateModel dateModel_end = new SpinnerDateModel();
        JSpinner dateSpinner_end = new JSpinner(dateModel_end);
        
        // Set the editor to use a specific date format (yyyy-MM-dd)
        JSpinner.DateEditor dateEditor_end = new JSpinner.DateEditor(dateSpinner_end, "yyyy-MM-dd");
        dateSpinner_end.setEditor(dateEditor_end);

        // Set the spinner to the current date
        dateModel_end.setValue(new Date());

        // Add the date spinner to the GraphPage
        add(new JLabel("Select End:"));
        add(dateSpinner_end);


        JButton generateGraphButton = new JButton("Generate Graph");
        add(generateGraphButton);

        generateGraphButton.addActionListener(e -> {

            Date start_date = dateModel_start.getDate();
            Date end_date = dateModel_end.getDate();

            remove(hist);

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.CENTER;

            System.out.println(start_date.toString() + "      " + end_date.toString());
            


            HashMap<String, Double> new_data = getIngredients(start_date.toString(), end_date.toString());            

            ArrayList<String> new_labels = new ArrayList<String>();
            ArrayList<Double> new_amount = new ArrayList<Double>();
    
            for (Map.Entry<String, Double> entry : new_data.entrySet()) {
                System.out.println(entry.getKey() + "      " + entry.getValue());
                new_labels.add(entry.getKey()); 
                new_amount.add(entry.getValue()); 
            }


            System.out.println("Called");

            hist = new Histogram(new_labels, new_amount);

            add(hist, constraints);
        });

    }

    /**
     * paintComponent repaints the page and is important to make sure that when next is selected it repaints the new histogram with the next
     * page of information.
     * 
     * @see Histogram
     * @see JPanel
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        hist.revalidate();
        hist.repaint();
    }
}
