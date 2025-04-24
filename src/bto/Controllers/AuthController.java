package bto.Controllers;

import bto.Entities.*;
import bto.Interfaces.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The AuthController class manages user authentication and user account management
 * for the BTO housing system. It handles user login, credential validation, user creation,
 * and provides methods to retrieve users by various criteria.
 * <p>
 * This controller stores all users in a map indexed by their NRIC for efficient retrieval
 * and enforces validation rules for user information such as NRIC format, password strength,
 * and name requirements.
 */
public class AuthController implements IAuthController {
    private Map<String, User> users; // Map of NRIC to User

    /**
     * Default constructor for AuthController.
     * Initializes an empty map to store users.
     */
    public AuthController() {
        users = new HashMap<>();
    }

    /**
     * Validates an NRIC (National Registration Identity Card) number.
     * Valid NRIC format is S or T followed by 7 digits and ending with a capital letter.
     *
     * @param nric The NRIC to validate
     * @return true if the NRIC is valid, false otherwise
     */
    public boolean validateNRIC(String nric) {
        // NRIC format: S/T followed by 7 digits and a letter
        Pattern pattern = Pattern.compile("^[ST]\\d{7}[A-Z]$");
        return pattern.matcher(nric).matches();
    }

    /**
     * Validates a password against the system's password requirements.
     * A valid password must be at least 8 characters long.
     *
     * @param password The password to validate
     * @return true if the password meets requirements, false otherwise
     */
    public boolean validatePassword(String password) {
        // Password requirements: at least 8 characters
        return password != null && password.length() >= 8;
    }

    /**
     * Validates a user's name.
     * A valid name must be non-empty and contain only letters, spaces, and hyphens.
     *
     * @param name The name to validate
     * @return true if the name is valid, false otherwise
     */
    public boolean validateName(String name) {
        // Name requirements: non-empty and contains only letters, spaces, and hyphens
        return name != null && !name.trim().isEmpty() && name.matches("^[a-zA-Z\\s\\-]+$");
    }

    /**
     * Authenticates a user based on NRIC and password.
     * First validates the NRIC format, then checks if the user exists and if the password matches.
     *
     * @param nric The NRIC of the user attempting to log in
     * @param password The password for authentication
     * @return The User object if authentication is successful, null otherwise
     */
    public User loginUser(String nric, String password) {
        if (!validateNRIC(nric)) {
            return null;
        }

        User user = users.get(nric);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

    /**
     * Updates a user's password after validating the new password meets requirements.
     *
     * @param user The user whose password is being updated
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if the password was successfully updated, false otherwise
     */
    public boolean updateNewPassword(User user, String oldPassword, String newPassword) {
        if (user != null && validatePassword(newPassword)) {
            return user.changePassword(oldPassword, newPassword);
        }
        return false;
    }

    /**
     * Retrieves all users of type Applicant, excluding HDBOfficers who also have Applicant functionality.
     *
     * @return A list of all Applicant users in the system
     */
    public List<Applicant> getAllApplicants() {
        List<Applicant> applicants = new ArrayList<>();

        for (User user : users.values()) {
            if (user instanceof Applicant && !(user instanceof HDBOfficer)) {
                applicants.add((Applicant) user);
            }
        }

        return applicants;
    }

    /**
     * Retrieves all users of type HDBOfficer.
     *
     * @return A list of all HDBOfficer users in the system
     */
    public List<HDBOfficer> getAllOfficers() {
        List<HDBOfficer> officers = new ArrayList<>();

        for (User user : users.values()) {
            if (user instanceof HDBOfficer) {
                officers.add((HDBOfficer) user);
            }
        }

        return officers;
    }

    /**
     * Retrieves all users of type HDBManager.
     *
     * @return A list of all HDBManager users in the system
     */
    public List<HDBManager> getAllManagers() {
        List<HDBManager> managers = new ArrayList<>();

        for (User user : users.values()) {
            if (user instanceof HDBManager) {
                managers.add((HDBManager) user);
            }
        }

        return managers;
    }

    /**
     * Adds a new user to the system after validating their information.
     * Validates NRIC, password, and name, then ensures the NRIC is not already in use.
     *
     * @param user The user to add to the system
     * @return true if the user was added successfully, false if validation failed or NRIC is already in use
     */
    public boolean addUser(User user) {
        if (user != null && validateNRIC(user.getNric()) && validatePassword(user.getPassword()) && validateName(user.getName())) {
            if (!users.containsKey(user.getNric())) {
                users.put(user.getNric(), user);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes a user from the system by their NRIC.
     *
     * @param nric The NRIC of the user to remove
     * @return true if the user was removed successfully, false if the user was not found
     */
    public boolean removeUser(String nric) {
        if (users.containsKey(nric)) {
            users.remove(nric);
            return true;
        }
        return false;
    }

    /**
     * Finds a user by their name (case-insensitive).
     *
     * @param name The name to search for
     * @return The first User object with a matching name, or null if no match is found
     */
    public User findUserByName(String name) {
        for (User user : users.values()) {
            if (user.getName().equalsIgnoreCase(name)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Gets the map of all users indexed by NRIC.
     *
     * @return The map of NRIC to User objects
     */
    public Map<String, User> getUsers() {
        return users;
    }

    /**
     * Sets the users map to a new map.
     *
     * @param users The new map of NRIC to User objects
     */
    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
}