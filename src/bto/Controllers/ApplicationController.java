package bto.Controllers;

import bto.Enums.*;
import bto.Interfaces.*;
import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ApplicationController class manages BTO (Build-To-Order) housing project applications.
 * This controller acts as a central management point for all application-related operations
 * including submission, processing, status updates, and withdrawal requests.
 * <p>
 * It maintains a map of all applications indexed by applicant NRIC for fast retrieval
 * and provides methods to interact with application entities across the system.
 */
public class ApplicationController implements IApplicationController {
    private Map<String, ProjectApplication> applications; // Map of applicant NRIC to application

    /**
     * Default constructor for ApplicationController.
     * Initializes an empty map to store applications.
     */
    public ApplicationController() {
        applications = new HashMap<>();
    }

    /**
     * Submits a new application for an applicant to a specific project.
     * Verifies that the applicant does not already have an active application.
     *
     * @param applicant The applicant submitting the application
     * @param project The project being applied for
     * @return true if the application was successfully submitted, false if the applicant already has an active application
     */
    public Boolean submitApplication(Applicant applicant, Project project) {

        // Check if the applicant already has an active application
        if (applicant.getAppliedProject() != null) {
            return false; // Already has an application
        }

        // Create new application
        ProjectApplication application = new ProjectApplication(applicant, project);

        // Set the application for the applicant
        applicant.setAppliedProject(application);

        // Add to the project's applications
        project.addApplication(application);

        // Store in the map
        applications.put(applicant.getNric(), application);

        return true;
    }

    /**
     * Processes an application by checking flat availability and updating the application status.
     * If units of the requested flat type are available, the application is marked as successful
     * and the available units count is decreased.
     *
     * @param application The application to process
     * @return true if the application was processed successfully with available units, false otherwise
     */
    public boolean processApplication(ProjectApplication application) {
        if (application == null || !applications.containsValue(application)) {
            return false;
        }

        // Check if there are available units of the requested flat type
        FlatType requestedType = application.getSelectedFlatType();
        Project project = application.getProject();

        int availableUnits = project.getFlatTypeUnits().getOrDefault(requestedType, 0);

        if (availableUnits > 0) {
            // Update application status
            application.setStatus(ApplicationStatus.SUCCESSFUL);

            // Reduce available units
            project.updateFlatAvailability(requestedType, availableUnits - 1);

            return true;
        } else {
            // No available units
            application.setStatus(ApplicationStatus.UNSUCCESSFUL);
            return false;
        }
    }

    /**
     * Updates the status of an application.
     *
     * @param application The application to update
     * @param status The new status to set
     * @return true if the status was updated successfully, false if the application is invalid
     */
    public boolean updateApplicationStatus(ProjectApplication application, ApplicationStatus status) {
        if (application == null || !applications.containsValue(application)) {
            return false;
        }

        application.setStatus(status);
        return true;
    }

    /**
     * Retrieves an application by the applicant's NRIC.
     *
     * @param nric The NRIC of the applicant
     * @return The ProjectApplication object if found, null otherwise
     */
    public ProjectApplication getApplicationByApplicantNRIC(String nric) {
        return applications.get(nric);
    }

    /**
     * Retrieves all applications for a specific project.
     *
     * @param project The project to filter applications for
     * @return A list of applications for the specified project
     */
    public List<ProjectApplication> getApplicationsByProject(Project project) {
        List<ProjectApplication> projectApplications = new ArrayList<>();

        for (ProjectApplication application : applications.values()) {
            if (application.getProject().equals(project)) {
                projectApplications.add(application);
            }
        }

        return projectApplications;
    }

    /**
     * Retrieves all applications in the system.
     *
     * @return A list of all applications
     */
    public List<ProjectApplication> getAllApplications() {
        return new ArrayList<>(applications.values());
    }

    /**
     * Submits a withdrawal request for an application.
     * Changes the withdrawal status to "PENDING" for manager approval.
     *
     * @param application The application to withdraw
     * @return true if the withdrawal request was submitted successfully, false if the application is invalid
     */
    public boolean requestWithdrawal(ProjectApplication application) {
        if (application == null || !applications.containsValue(application)) {
            return false;
        }

        // Update application withdrawal status
        application.setWithdrawalStatus("PENDING");

        return true;
    }

    /**
     * Approves a withdrawal request for an application.
     * If the application was successful and a flat was allocated, the flat is returned to the available pool.
     *
     * @param application The application with the withdrawal request
     * @return true if the withdrawal was approved successfully, false if the application is invalid or not in a PENDING withdrawal state
     */
    public boolean approveWithdrawal(ProjectApplication application) {
        if (application == null || !applications.containsValue(application) ||
                !"PENDING".equals(application.getWithdrawalStatus())) {
            return false;
        }

        // Update application withdrawal status
        application.setWithdrawalStatus("APPROVED");

        // Remove the application from the applicant
        application.getApplicant().setAppliedProject(null);

        // If the application was successful, increase the available units
        if (application.getStatus() == ApplicationStatus.SUCCESSFUL) {
            FlatType flatType = application.getSelectedFlatType();
            Project project = application.getProject();

            int currentUnits = project.getFlatTypeUnits().getOrDefault(flatType, 0);
            project.updateFlatAvailability(flatType, currentUnits + 1);
        }

        return true;
    }

    /**
     * Rejects a withdrawal request for an application.
     *
     * @param application The application with the withdrawal request
     * @return true if the withdrawal was rejected successfully, false if the application is invalid or not in a PENDING withdrawal state
     */
    public boolean rejectWithdrawal(ProjectApplication application) {
        if (application == null || !applications.containsValue(application) ||
                !"PENDING".equals(application.getWithdrawalStatus())) {
            return false;
        }

        // Update application withdrawal status
        application.setWithdrawalStatus("REJECTED");

        return true;
    }

    /**
     * Gets the map of all applications indexed by applicant NRIC.
     *
     * @return The map of NRIC to ProjectApplication
     */
    public Map<String, ProjectApplication> getApplications() {
        return applications;
    }

    /**
     * Sets the applications from a list of ProjectApplication objects.
     * Clears the existing map and rebuilds it using the applicant NRIC as keys.
     *
     * @param applicationList The list of applications to set
     */
    public void setApplications(List<ProjectApplication> applicationList) {
        this.applications.clear();

        for (ProjectApplication application : applicationList) {
            this.applications.put(application.getApplicant().getNric(), application);
        }
    }
}