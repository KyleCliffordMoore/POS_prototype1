import java.util.List;

/**
 * OrderItem class represents an item in the menu. It has fields for the name, price, and type of the item.
 * For meals, it also has fields for the size, side, and entrees.
 * The class has a constructor for meals and a constructor for other items.
 * It has getters for all fields.
 * The toString method returns the name and price of the item.
 * @author Jess Cadena
 */

public class OrderItem {
    private String name;
    private double price;
    private String type; // "Meal", "Drink", "Appetizer"
    // Additional fields for meals
    private String size;
    private String side;
    private List<String> entrees;

    /**
     * Constructor for items that are not meals.
     * @param name
     * @param price
     * @param type
     */
    public OrderItem(String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    /**
     * Constructor for meals
     * @param name
     * @param price
     * @param type
     * @param size
     * @param side
     * @param entrees
     */
    // Constructor for meals
    public OrderItem(String name, double price, String type, String size, String side, List<String> entrees) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.size = size;
        this.side = side;
        this.entrees = entrees;
    }

    
    // Getters
    /**
     * getter for Name
     * @return name
     */
    public String getName() { return name; }
    /**
     * getter for Price
     * @return price
     */
    public double getPrice() { return price;}
    /**
     * getter for Type
     * @return type
     */
    public String getType() { return type; }
    /**
     * getter for Size
     * @return size
     */
    public String getSize() { return size; }
    /**
     * getter for Side
     * @return side
     */
    public String getSide() { return side; }
    /**
     * getter for Entrees
     * @return entrees
     */
    public List<String> getEntrees() { return entrees; }

    /**
     * toString method returns the name and price of the item.
     */
    @Override
    public String toString() {
        return name + ": $" + String.format("%.2f", price);
    }
}
