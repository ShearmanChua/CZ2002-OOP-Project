import java.io.Serializable;

public class Staff implements Serializable {
	
	private int staffId;
	private String name;
	private char gender;
	private String jobTitle;
	
	private static final long serialVersionUID = -3741237698171379560L;

	/**
	 * constructor
	 * @param id
	 * @param name
	 * @param gender - 'M' or 'F'
	 * @param title
	 */
	public Staff(int id, String name, char gender, String title) {
		this.staffId = id;
		this.name = name;
		this.gender = gender;
		this.jobTitle = title;
	}
	
	/**
	 * get ID
	 * @return ID
	 */
	public int getID(){
		return this.staffId;
	}
	
	/**
	 * get employeeId
	 * @return employeeId
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * get name
	 * @return name
	 */
	public char getGender(){
		return this.gender;
	}
	
	/**
	 * get gender
	 * @return gender
	 */
	public String getJobTitle(){
		return this.jobTitle;
	}
	
	
	public void setJobTitle(String newJobTitle) {
		jobTitle = newJobTitle;
	}
}
