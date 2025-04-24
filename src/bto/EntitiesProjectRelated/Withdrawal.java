package bto.EntitiesProjectRelated;

import bto.Entities.*;
import java.util.Date;

/**
 * Represents a withdrawal request for a project application.
 * Tracks the status and details of a withdrawal process.
 */
public class Withdrawal {
    /** Applicant initiating the withdrawal */
    private Applicant applicant;

    /** Project application associated with the withdrawal */
    private ProjectApplication application;

    /** Current status of the withdrawal request */
    private String status;

    /** Date when the withdrawal request was made */
    private Date requestDate;

    /**
     * Default constructor initializes withdrawal with a pending status
     * and sets the request date to the current date.
     */
    public Withdrawal() {
        this.status = "PENDING";
        this.requestDate = new Date(); // Sets current date as request date
    }

    /**
     * Parameterized constructor creates a withdrawal for a specific applicant and application.
     *
     * @param applicant The applicant requesting the withdrawal
     * @param application The project application being withdrawn
     */
    public Withdrawal(Applicant applicant, ProjectApplication application) {
        this();
        this.applicant = applicant;
        this.application = application;
    }

    /**
     * Retrieves the current status of the withdrawal request.
     *
     * @return The status of the withdrawal
     */
    public String getStatus() {
        return status;
    }

    /**
     * Updates the status of the withdrawal request.
     *
     * @param newStatus The new status to be set
     * @return Always returns true after updating the status
     */
    public boolean updateStatus(String newStatus) {
        this.status = newStatus;
        return true;
    }

    /**
     * Retrieves detailed information about the applicant.
     *
     * @return A string containing the applicant's details
     */
    public String getApplicantDetails() {
        return applicant.getDetails();
    }

    /**
     * Generates a string with key details of the associated project application.
     *
     * @return A string containing project and application status information
     */
    public String getApplicationDetails() {
        return "Application for " + application.getProject().getProjectName() +
                ", Status: " + application.getStatus();
    }

    /**
     * Gets the applicant associated with the withdrawal.
     *
     * @return The applicant object
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Sets the applicant for the withdrawal.
     *
     * @param applicant The applicant to be associated with the withdrawal
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Gets the project application associated with the withdrawal.
     *
     * @return The project application object
     */
    public ProjectApplication getApplication() {
        return application;
    }

    /**
     * Sets the project application for the withdrawal.
     *
     * @param application The project application to be associated with the withdrawal
     */
    public void setApplication(ProjectApplication application) {
        this.application = application;
    }

    /**
     * Retrieves the date when the withdrawal request was made.
     *
     * @return The request date
     */
    public Date getRequestDate() {
        return requestDate;
    }

    /**
     * Sets the date of the withdrawal request.
     *
     * @param requestDate The date to be set as the request date
     */
    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    /**
     * Sets the status of the withdrawal request.
     *
     * @param status The new status to be set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}