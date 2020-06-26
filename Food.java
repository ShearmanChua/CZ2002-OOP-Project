import java.io.Serializable;

public class Food implements Serializable{
    private String name;
    private double price;
    private String description;
    private FOODTYPES type;
    public enum FOODTYPES { MAIN_COURSE, DESSERT, DRINK;}

    /**
     * Default constructor for Food
     * @param name - string of object name
     * @param price - double price
     * @param desc - String of food description
     * @param types - FOODTYPES enum
     */
    public Food(String name, double price, String desc, FOODTYPES types){
        this.name = name;
        this.price = price;
        this.description = desc;
        this.type = types;

    }

    /**
     * get name of Food
     * @return name - string
     */
    public String getName() {
        return name;
    }

    /**
     * get price of food
     * @return price - double
     */
    public double getPrice() {
        return price;
    }

    /**
     * get desc of Food
     * @return description - String
     */
    public String getDescription() {
        return description;
    }

    /**
     * get type of Food
     * @return type - FOODTYPE
     */
    public FOODTYPES getType() {
        return type;
    }

    /**
     * set name of Food
     * @param  name - String
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set desc of Food
     * @param  description - String
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * set price of Food
     * @param price - double
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * set type of Food
     * @param  type - FOODTYPES
     */
    public void setType(FOODTYPES type) {
        this.type = type;
    }
}
