package bto.EntitiesProjectRelated;

import bto.Entities.*;

/**
 * The OfficerRegistration class represents a registration request from an HDB officer
 * to work on a specific BTO project. It tracks the officer, project, and registration status.
 * <p>
 * Registration status can be one of three values:
 * <ul>
 *   <li>PENDING - Initial status when an officer registers for a project</li>
 *   <li>APPROVED - Status when a manager approves the registration</li>
 *   <li>REJECTED - Status when a manager rejects the registration</li>
 * </ul>
 */
public class OfficerRegistration {
    /** The HDB officer assigned to or registering for this project */
    private HDBOfficer hdbOfficer;

    /** The housing project that the officer is registering for or assigned to */
    private Project project;

    /**
     * The current status of the officer's registration.
     */
    private String registrationStatus;

    /**
     * Default constructor for OfficerRegistration.
     * Initializes the registration status to "PENDING".
     */
    public OfficerRegistration() {
        this.registrationStatus = "PENDING";
    }

    /**
     * Parameterized constructor for OfficerRegistration.
     * Creates a registration for a specific officer and project with a default status of "PENDING".
     *
     * @param hdbOfficer The HDB officer requesting registration
     * @param project The project the officer is registering for
     */
    public OfficerRegistration(HDBOfficer hdbOfficer, Project project) {
        this();
        this.hdbOfficer = hdbOfficer;
        this.project = project;
    }

    /**
     * Gets the current registration status.
     * Alias for getRegistrationStatus() for backward compatibility.
     *
     * @return The current registration status
     */
    public String getStatus() {
        return registrationStatus;
    }

    /**
     * Updates the registration status.
     * Only allows changing to "APPROVED" or "REJECTED".
     *
     * @param status The new status to set
     * @return true if the status was updated successfully, false if an invalid status was provided
     */
    public boolean updateStatus(String status) {
        if (status.equals("APPROVED") || status.equals("REJECTED")) {
            this.registrationStatus = status;
            return true;
        }
        return false;
    }

    /**
     * Gets the details of the officer associated with this registration.
     *
     * @return A string containing the officer's details
     */
    public String getOfficerDetails() {
        return hdbOfficer.getDetails();
    }

    /**
     * Gets the details of the project associated with this registration.
     *
     * @return A string containing the project's details
     */
    public String getProjectDetails() {
        return project.getDetails();
    }

    /**
     * Gets the HDB officer associated with this registration.
     *
     * @return The HDB officer
     */
    public HDBOfficer getHdbOfficer() {
        return hdbOfficer;
    }

    /**
     * Sets the HDB officer for this registration.
     *
     * @param hdbOfficer The HDB officer to set
     */
    public void setHdbOfficer(HDBOfficer hdbOfficer) {
        this.hdbOfficer = hdbOfficer;
    }

    /**
     * Gets the project associated with this registration.
     *
     * @return The project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project for this registration.
     *
     * @param project The project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the current registration status.
     *
     * @return The current registration status ("PENDING", "APPROVED", or "REJECTED")
     */
    public String getRegistrationStatus() {
        return registrationStatus;
    }

    /**
     * Sets the registration status.
     * Only allows changing to "APPROVED" or "REJECTED".
     * If an invalid status is provided, the status remains unchanged.
     *
     * @param registrationStatus The registration status to set
     */
    public void setRegistrationStatus(String registrationStatus) {
        // Only allow changing to APPROVED or REJECTED
        if (registrationStatus.equals("APPROVED") || registrationStatus.equals("REJECTED")) {
            this.registrationStatus = registrationStatus;
        }
        // If an invalid status is provided, the status remains unchanged
    }
}