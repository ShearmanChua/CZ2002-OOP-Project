import java.util.ArrayList;

public class PromotionMenuItem extends MenuItem{

    private ArrayList<Food> food = new ArrayList<Food>();
    private double price = 0;
    private String promoName;
    private String description;

    /**
     * Default constructor of promotionalMenuItem
     * with price being the combined of both items
     * @param food - arraylist<food>
     * @param name - String
     * @param desc - String
     */
    public PromotionMenuItem(ArrayList<Food>food, String name, String desc){
        this.food = food;
        this.promoName = name;
        this.description = desc;

        for(int i=0; i<food.size();i++){
            this.price+=food.get(i).getPrice();
        }
    }

    /**
     * Default constructor of promotionalMenuItem
     * with price given
     * @param food - arraylist<food>
     * @param price - double
     * @param name - String
     * @param desc - String
     */
    public PromotionMenuItem(ArrayList<Food>food, double price, String name, String desc){
        this.food = food;
        this.price = price;
        this.promoName = name;
        this.description = desc;
    }

    /**
     * getName() method from abstract class MenuItem
     * get name of promotional item
     * @return promotion name
     */
    public String getName() {
        return this.promoName;
    }

    /**
     * setName() method from abstract class MenuItem
     * set name of promotional item
     * @param name - String
     */
    public void setName(String name) {
        this.promoName = name;
    }

    /**
     * getDescription() method from abstract class MenuItem
     * return description of promotional item
     * @return food.getDescription()
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * setDecription() method from abstract class MenuItem
     * set description of food
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * getPrice() method from abstract class MenuItem
     * return price of promotional item
     * @return food.getPrice()
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * setPrice() method from abstract class MenuItem
     * set price of promotional item
     * @param price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * getArrayFood() overwrites method from MenuItem
     * @return food - arraylist of food
     */
    public ArrayList<Food> getArrayFood(){
        return this.food;
    }



}
