
public class AlaCarteMenuItem extends MenuItem{

    private Food food;

    /**
     * MenuItem default Constructor
     * stores food into Food food.
     * @param food
     */
    public AlaCarteMenuItem(Food food){
        this.food = food;
    }

    /**
     * getName() method from abstract class MenuItem
     * return name of food
     * @return food.getName()
     */
    public String getName() {
        return food.getName();
    }

    /**
     * setName() method from abstract class MenuItem
     * set name of food
     * @param name - String
     */
    public void setName(String name) {
        this.food.setName(name);
    }

    /**
     * getPrice() method from abstract class MenuItem
     * return price of food
     * @return food.getPrice()
     */
    public double getPrice() {
        return food.getPrice();
    }

    /**
     * setPrice() method from abstract class MenuItem
     * set price of food
     * @param price
     */
    public void setPrice(double price) {
        this.food.setPrice(price);
    }

    /**
     * getDescription() method from abstract class MenuItem
     * return description of food
     * @return food.getDescription()
     */
    public String getDescription() {
        return food.getDescription();
    }

    /**
     * setDecription() method from abstract class MenuItem
     * set description of food
     */
    public void setDescription(String desc) {
        this.food.setDescription(desc);
    }

    /**
     * getType() method from abstract class MenuItem
     * return type of food
     * @return food.getType()
     */
    public Food.FOODTYPES getType(){
        return food.getType();
    }

    /**
     * setType() method from abstract class MenuItem
     * set type for food
     * @param type
     */
    public void setType(Food.FOODTYPES type) {
        this.food.setType(type);
    }

    /**
     * getFood() overwrites getfood() method from MenuItem class
     * @return this.food
     */
    public Food getFood() {
        return this.food;
    }

    //    public ArrayList<Food> getFood() {
//        ArrayList<Food> foodArr = new ArrayList<Food>();
//        foodArr.add(this.food);
//        return foodArr;
//    }
}
