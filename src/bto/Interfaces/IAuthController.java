package bto.Interfaces;

import bto.Entities.*;
import java.util.List;
import java.util.Map;

/**
 * Interface defining the contract for user authentication and account management.
 */
public interface IAuthController {
    
    /**
     * Validates an NRIC number.
     *
     * @param nric The NRIC to validate
     * @return true if the NRIC is valid, false otherwise
     */
    boolean validateNRIC(String nric);
    
    /**
     * Validates a password against the system's password requirements.
     *
     * @param password The password to validate
     * @return true if the password meets requirements, false otherwise
     */
    boolean validatePassword(String password);
    
    /**
     * Validates a user's name.
     *
     * @param name The name to validate
     * @return true if the name is valid, false otherwise
     */
    boolean validateName(String name);
    
    /**
     * Authenticates a user based on NRIC and password.
     *
     * @param nric The NRIC of the user attempting to log in
     * @param password The password for authentication
     * @return The User object if authentication is successful, null otherwise
     */
    User loginUser(String nric, String password);
    
    /**
     * Updates a user's password.
     *
     * @param user The user whose password is being updated
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if the password was successfully updated, false otherwise
     */
    boolean updateNewPassword(User user, String oldPassword, String newPassword);
    
    /**
     * Retrieves all users of type Applicant.
     *
     * @return A list of all Applicant users in the system
     */
    List<Applicant> getAllApplicants();
    
    /**
     * Retrieves all users of type HDBOfficer.
     *
     * @return A list of all HDBOfficer users in the system
     */
    List<HDBOfficer> getAllOfficers();
    
    /**
     * Retrieves all users of type HDBManager.
     *
     * @return A list of all HDBManager users in the system
     */
    List<HDBManager> getAllManagers();
    
    /**
     * Adds a new user to the system.
     *
     * @param user The user to add to the system
     * @return true if the user was added successfully, false otherwise
     */
    boolean addUser(User user);
    
    /**
     * Removes a user from the system by their NRIC.
     *
     * @param nric The NRIC of the user to remove
     * @return true if the user was removed successfully, false otherwise
     */
    boolean removeUser(String nric);
    
    /**
     * Finds a user by their name.
     *
     * @param name The name to search for
     * @return The User object with a matching name, or null if no match is found
     */
    User findUserByName(String name);
    
    /**
     * Gets the map of all users indexed by NRIC.
     *
     * @return The map of NRIC to User objects
     */
    Map<String, User> getUsers();
    
    /**
     * Sets the users map to a new map.
     *
     * @param users The new map of NRIC to User objects
     */
    void setUsers(Map<String, User> users);
}