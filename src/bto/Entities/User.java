package bto.Entities;
import bto.Enums.MaritalStatus;

/**
 * Represents a user in the system with personal information and authentication capabilities.
 * This class manages user details such as NRIC, name, age, and marital status,
 * along with authentication functionality including login and password management.
 */
public class User implements UserNeeds {
	/** The user's National Registration Identity Card number (unique identifier) */
	private String nric;

	/** The user's password for authentication */
	private String password;

	/** The user's age */
	private int age;

	/** The user's marital status */
	private MaritalStatus maritalStatus;

	/** The user's name */
	private String name;

	/**
	 * Default constructor for creating an empty User object.
	 */
	public User() {}

	/**
	 * Parameterized constructor for creating a User with all required attributes.
	 *
	 * @param nric The National Registration Identity Card number
	 * @param password The user's password
	 * @param age The user's age
	 * @param maritalStatus The user's marital status
	 * @param name The user's name
	 */
	public User(String nric, String password, int age, MaritalStatus maritalStatus, String name) {
		this.nric = nric;
		this.password = password;
		this.age = age;
		this.maritalStatus = maritalStatus;
		this.name = name;
	}

	/**
	 * Authenticates the user based on credentials.
	 *
	 * @return true if login is successful, false otherwise
	 */
	public boolean login() {
		// Implementation for login logic
		return false; // Placeholder
	}

	/**
	 * Changes the user's password after validating the old password.
	 *
	 * @param oldPassword The current password for verification
	 * @param newPassword The new password to set
	 * @return true if password was successfully changed, false if old password is incorrect
	 */
	public boolean changePassword(String oldPassword, String newPassword) {
		// Implementation for password change
		if (this.password.equals(oldPassword)) {
			this.password = newPassword;
			return true;
		}
		return false;
	}

	/**
	 * Returns a string containing the user's basic information.
	 *
	 * @return A string with the user's name, NRIC, age, and marital status
	 */
	public String getDetails() {
		return "Name: " + name + ", NRIC: " + nric + ", Age: " + age + ", Marital Status: " + maritalStatus;
	}

	/**
	 * Generates a formatted display of the user's profile.
	 * This method is designed to be polymorphic for different user types.
	 *
	 * @return A formatted string representation of the user's profile
	 */
	public String displayProfile() {
		StringBuilder profile = new StringBuilder();
		profile.append("\n=== USER PROFILE ===\n");
		profile.append("Name: ").append(name).append("\n");
		profile.append("NRIC: ").append(nric).append("\n");
		profile.append("Age: ").append(age).append("\n");
		profile.append("Marital Status: ").append(maritalStatus).append("\n");
		return profile.toString();
	}

	/**
	 * Gets the user's NRIC.
	 *
	 * @return The user's NRIC
	 */
	public String getNric() {
		return nric;
	}

	/**
	 * Sets the user's NRIC.
	 *
	 * @param nric The NRIC to set
	 */
	public void setNric(String nric) {
		this.nric = nric;
	}

	/**
	 * Gets the user's password.
	 *
	 * @return The user's password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the user's password.
	 *
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the user's age.
	 *
	 * @return The user's age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * Sets the user's age.
	 *
	 * @param age The age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Gets the user's marital status.
	 *
	 * @return The user's marital status
	 */
	public MaritalStatus getMaritalStatus() {
		return maritalStatus;
	}

	/**
	 * Sets the user's marital status.
	 *
	 * @param maritalStatus The marital status to set
	 */
	public void setMaritalStatus(MaritalStatus maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	/**
	 * Gets the user's name.
	 *
	 * @return The user's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the user's name.
	 *
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}