package bto.EntitiesProjectRelated;

import bto.Entities.Applicant;
import bto.Enums.ApplicationStatus;
import bto.Enums.FlatType;

/**
 * The ProjectApplication class represents an application submitted by an applicant for a BTO housing project.
 * It tracks the relationship between applicants and projects, maintains application status,
 * handles withdrawal requests, and stores the selected flat type when applicable.
 * <p>
 * Applications can have one of several statuses defined in the ApplicationStatus enum:
 * PENDING, SUCCESSFUL, UNSUCCESSFUL, or BOOKED.
 */
public class ProjectApplication {
    /** The applicant who submitted this application */
    private Applicant applicant;

    /** The BTO project that the applicant is applying for */
    private Project project;

    /**
     * The current status of the application in the processing workflow.
     * Represents the application's stage in the overall selection process.
     */
    private ApplicationStatus status;

    /**
     * Indicates the withdrawal status if the applicant has withdrawn their application.
     * May contain reasons or specific withdrawal states.
     */
    private String withdrawalStatus;

    /**
     * The specific flat type selected by the applicant.
     * Represents the applicant's preference for unit size and configuration.
     */
    private FlatType selectedFlatType;

    /**
     * Default constructor for ProjectApplication.
     * Initializes the application status to PENDING and withdrawalStatus to null.
     */
    public ProjectApplication() {
        status = ApplicationStatus.PENDING;
        withdrawalStatus = null;
    }

    /**
     * Parameterized constructor for ProjectApplication.
     * Creates an application for a specific applicant and project with default status of PENDING.
     *
     * @param applicant The applicant submitting the application
     * @param project The project being applied for
     */
    public ProjectApplication(Applicant applicant, Project project) {
        this();
        this.applicant = applicant;
        this.project = project;
    }

    /**
     * Updates the status of this application.
     *
     * @param newStatus The new status to set
     * @return true indicating the operation was successful
     */
    public boolean updateStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
        return true;
    }

    /**
     * Gets the details of the project associated with this application.
     *
     * @return A string containing the project's details
     */
    public String getProjectDetails() {
        return project.getDetails();
    }

    /**
     * Gets the details of the applicant associated with this application.
     *
     * @return A string containing the applicant's details
     */
    public String getApplicantDetails() {
        return applicant.getDetails();
    }

    /**
     * Gets the applicant who submitted this application.
     *
     * @return The applicant
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Sets the applicant for this application.
     *
     * @param applicant The applicant to set
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Gets the project associated with this application.
     *
     * @return The project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project for this application.
     *
     * @param project The project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the withdrawal status of this application.
     * Can be "PENDING", "APPROVED", "REJECTED", or null if no withdrawal request exists.
     *
     * @return The withdrawal status, or null if no withdrawal request exists
     */
    public String getWithdrawalStatus() {
        return withdrawalStatus;
    }

    /**
     * Sets the withdrawal status of this application.
     *
     * @param withdrawalStatus The withdrawal status to set
     */
    public void setWithdrawalStatus(String withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    /**
     * Gets the flat type selected by the applicant.
     * This is typically set after an application is approved and the applicant selects a specific flat type.
     *
     * @return The selected flat type, or null if no flat type has been selected yet
     */
    public FlatType getSelectedFlatType() {
        return selectedFlatType;
    }

    /**
     * Sets the flat type selected by the applicant.
     *
     * @param selectedFlatType The selected flat type to set
     */
    public void setSelectedFlatType(FlatType selectedFlatType) {
        this.selectedFlatType = selectedFlatType;
    }

    /**
     * Gets the current status of this application.
     *
     * @return The application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this application.
     *
     * @param status The application status to set
     */
    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }
}