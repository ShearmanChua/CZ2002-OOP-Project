import java.util.ArrayList;
import java.io.Serializable;

public abstract class MenuItem implements Serializable{

	/**
	 * default constructor for abstract class MenuItem
	 */
	public MenuItem(){ }

	/**
	 * abstract getName method
	 * @return String
	 */
	public abstract String getName();

	/**
	 * abstract setName method
	 * @param name
	 */
	public abstract void setName(String name);

	/**
	 * abstract setPrice method
	 * @return double
	 */
	public abstract double getPrice();

	/**
	 * abstract setPrice method
	 * @param price
	 */
	public abstract void setPrice(double price);

	/**
	 *abstact getDescription method
	 * @return description
	 */
	public abstract String getDescription();

	/**
	 * abstract setDescrition method
	 * @param desc
	 */
	public abstract void setDescription(String desc);

	/**
	 * abstract getName method
	 * @return name
	 */
	public ArrayList<Food> getArrayFood(){
		return null;
	}

	/**
	 * getFood method
	 * to be overwritten by other classes
	 * @return null
	 */
	public Food getFood(){
		return null;
	}

	/**
	 * getType method
	 * to be overwritten by other classes
	 * @return null
	 */
	public Food.FOODTYPES getType(){
		return null;
	}

	/**
	 * setType method
	 * to be overwritten by other classes
	 */
	public void setType(Food.FOODTYPES type){

	}





}