package bto.Interfaces;

import bto.Enums.*;
import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import java.util.List;
import java.util.Map;

/**
 * Interface defining the contract for managing BTO housing project applications.
 */
public interface IApplicationController {
    
    /**
     * Submits a new application for an applicant to a specific project.
     *
     * @param applicant The applicant submitting the application
     * @param project The project being applied for
     * @return true if the application was successfully submitted, false otherwise
     */
    Boolean submitApplication(Applicant applicant, Project project);
    
    /**
     * Processes an application by checking flat availability and updating the application status.
     *
     * @param application The application to process
     * @return true if the application was processed successfully with available units, false otherwise
     */
    boolean processApplication(ProjectApplication application);
    
    /**
     * Updates the status of an application.
     *
     * @param application The application to update
     * @param status The new status to set
     * @return true if the status was updated successfully, false if the application is invalid
     */
    boolean updateApplicationStatus(ProjectApplication application, ApplicationStatus status);
    
    /**
     * Retrieves an application by the applicant's NRIC.
     *
     * @param nric The NRIC of the applicant
     * @return The ProjectApplication object if found, null otherwise
     */
    ProjectApplication getApplicationByApplicantNRIC(String nric);
    
    /**
     * Retrieves all applications for a specific project.
     *
     * @param project The project to filter applications for
     * @return A list of applications for the specified project
     */
    List<ProjectApplication> getApplicationsByProject(Project project);
    
    /**
     * Retrieves all applications in the system.
     *
     * @return A list of all applications
     */
    List<ProjectApplication> getAllApplications();
    
    /**
     * Submits a withdrawal request for an application.
     *
     * @param application The application to withdraw
     * @return true if the withdrawal request was submitted successfully, false if the application is invalid
     */
    boolean requestWithdrawal(ProjectApplication application);
    
    /**
     * Approves a withdrawal request for an application.
     *
     * @param application The application with the withdrawal request
     * @return true if the withdrawal was approved successfully, false otherwise
     */
    boolean approveWithdrawal(ProjectApplication application);
    
    /**
     * Rejects a withdrawal request for an application.
     *
     * @param application The application with the withdrawal request
     * @return true if the withdrawal was rejected successfully, false otherwise
     */
    boolean rejectWithdrawal(ProjectApplication application);
    
    /**
     * Gets the map of all applications indexed by applicant NRIC.
     *
     * @return The map of NRIC to ProjectApplication
     */
    Map<String, ProjectApplication> getApplications();
    
    /**
     * Sets the applications from a list of ProjectApplication objects.
     *
     * @param applicationList The list of applications to set
     */
    void setApplications(List<ProjectApplication> applicationList);
}