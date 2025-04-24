package bto.Entities;

import bto.Enums.*;
import bto.EntitiesProjectRelated.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Map;

/**
 * Represents an HDB manager in the BTO system.
 * <p>
 * The HDBManager class extends the User class and represents staff members who manage
 * BTO projects. Managers can create, edit, and delete projects, approve or reject
 * officer registrations and applications, manage project visibility, and generate reports.
 */
public class HDBManager extends User implements ManagerOperations {
    /** List of projects managed by this HDB manager. */
    private List<Project> managedProjects;

    /**
     * Default constructor for creating an HDBManager.
     * Initializes an empty list of managed projects.
     */
    public HDBManager() {
        super();
        this.managedProjects = new ArrayList<>();
    }

    /**
     * Creates an HDBManager with specified personal information.
     *
     * @param nric The manager's National Registration Identity Card number
     * @param password The manager's password for system access
     * @param age The manager's age
     * @param maritalStatus The manager's marital status
     * @param name The manager's name
     */
    public HDBManager(String nric, String password, int age, MaritalStatus maritalStatus, String name) {
        super(nric, password, age, maritalStatus, name);
        this.managedProjects = new ArrayList<>();
    }

    /**
     * Creates a new BTO project with the provided details.
     * <p>
     * Checks for overlapping application periods with existing projects managed
     * by this manager before creating a new project.
     *
     * @param projectDetails The details of the project to create
     * @return The created project, or null if creation failed
     */
    public Project createProject(Project projectDetails) {
        // Check if already managing a project with overlapping application period
        Date newOpenDate = projectDetails.getApplicationOpenDate();
        Date newCloseDate = projectDetails.getApplicationCloseDate();

        for (Project existingProject : managedProjects) {
            Date existingOpenDate = existingProject.getApplicationOpenDate();
            Date existingCloseDate = existingProject.getApplicationCloseDate();

            // Check for overlap in application periods
            if ((newOpenDate.before(existingCloseDate) && newCloseDate.after(existingOpenDate)) ||
                    (existingOpenDate.before(newCloseDate) && existingCloseDate.after(newOpenDate))) {
                return null; // Overlapping application period
            }
        }

        // Validate project details
        if (projectDetails == null ||
                projectDetails.getProjectName() == null ||
                projectDetails.getProjectName().isEmpty()) {
            return null;
        }

        // Set the manager in charge to this manager
        projectDetails.setManagerInCharge(this);

        // Add to managed projects
        managedProjects.add(projectDetails);

        return projectDetails;
    }

    /**
     * Edits a specific attribute of an existing project.
     *
     * @param project The project to edit
     * @param attribute The name of the attribute to edit
     * @param newValue The new value for the attribute
     * @return true if the edit was successful, false otherwise
     */
    public boolean editProject(Project project, String attribute, Object newValue) {
        // Check if project belongs to this manager
        if (project == null || !project.getManagerInCharge().equals(this)) {
            return false;
        }

        // Update specific attribute based on input
        switch (attribute.toLowerCase()) {
            case "availablehdbofficerunits":
                int slots = ((Integer) newValue).intValue();
                project.setTotalOfficerSlots(slots);
                return true;

            case "flattypeunits":
                Map<FlatType, Integer> flatTypeUnits = (Map<FlatType, Integer>) newValue;
                project.setFlatTypeUnits(flatTypeUnits);
                return true;

            case "projectname":
                project.setProjectName((String) newValue);
                return true;

            case "neighborhood":
                project.setNeighborhood((String) newValue);
                return true;

            case "applicationopendate":
                project.setApplicationOpenDate((Date) newValue);
                return true;

            case "applicationclosedate":
                project.setApplicationCloseDate((Date) newValue);
                return true;

            default:
                return false;
        }
    }

    /**
     * Deletes an existing project managed by this manager.
     * <p>
     * Also removes all references to this project from registrations,
     * applications, and enquiries.
     *
     * @param project The project to delete
     * @return true if the deletion was successful, false otherwise
     */
    public boolean deleteProject(Project project) {
        // Check if project belongs to this manager
        if (project == null || !project.getManagerInCharge().equals(this)) {
            return false;
        }

        // Remove from managed projects
        boolean removed = managedProjects.remove(project);

        // Nullify any references to this project if needed
        if (removed) {
            // Clear officer registrations
            for (OfficerRegistration registration : project.getOfficerRegistrations()) {
                registration.setProject(null);
            }

            // Clear applications
            for (ProjectApplication application : project.getApplications()) {
                application.setProject(null);
                application.getApplicant().setAppliedProject(null);
            }

            // Clear enquiries
            for (Enquiry enquiry : project.getEnquiries()) {
                enquiry.setProject(null);
            }
        }

        return removed;
    }

    /**
     * Toggles the visibility of a project.
     * <p>
     * When a project is visible, applicants can see and apply for it.
     *
     * @param project The project whose visibility to toggle
     * @return true if the visibility was toggled successfully, false otherwise
     */
    public boolean toggleProjectVisibility(Project project) {
        // Check if project belongs to this manager
        if (project == null || !project.getManagerInCharge().equals(this)) {
            return false;
        }

        // Toggle visibility
        project.setVisible(!project.isVisible());

        return true;
    }

    /**
     * Approves an officer registration for a project.
     * <p>
     * Checks if there are available officer slots in the project before approval.
     *
     * @param registration The officer registration to approve
     * @return true if the approval was successful, false otherwise
     */
    public boolean approveRegistration(OfficerRegistration registration) {
        // Check if registration is valid and belongs to a project managed by this manager
        if (registration == null ||
                registration.getProject() == null ||
                !registration.getProject().getManagerInCharge().equals(this)) {
            return false;
        }

        // Check if there are available officer slots
        if (registration.getProject().getAvailableHDBOfficerSlots() <= 0) {
            return false; // No available slots
        }

        // Update registration status
        registration.setRegistrationStatus("APPROVED");

        // Add the project to the officer's assigned projects
        registration.getHdbOfficer().addAssignedProject(registration.getProject());

        return true;
    }

    /**
     * Approves an application for a project.
     * <p>
     * Checks if there are available units of eligible flat types before approval.
     *
     * @param application The application to approve
     * @return true if the approval was successful, false otherwise
     */
    public boolean approveApplication(ProjectApplication application) {
        // Check if application is valid and belongs to a project managed by this manager
        if (application == null ||
                application.getProject() == null ||
                !application.getProject().getManagerInCharge().equals(this)) {
            return false;
        }

        // Get the applicant and project
        Applicant applicant = application.getApplicant();
        Project project = application.getProject();

        // Get eligible flat types for this applicant
        List<FlatType> eligibleTypes = project.getEligibleFlatTypes(applicant);

        // Check if there are available units for any eligible flat type
        boolean unitsAvailable = false;
        for (FlatType flatType : eligibleTypes) {
            if (project.getAvailableFlatCount(flatType) > 0) {
                unitsAvailable = true;
                break;
            }
        }

        if (unitsAvailable) {
            // Update application status
            application.setStatus(ApplicationStatus.SUCCESSFUL);
            return true;
        } else {
            // No available units for any eligible flat type
            application.setStatus(ApplicationStatus.UNSUCCESSFUL);
            return false;
        }
    }

    /**
     * Rejects an application for a project.
     *
     * @param application The application to reject
     * @return true if the rejection was successful, false otherwise
     */
    public boolean rejectApplication(ProjectApplication application) {
        // Check if application is valid and belongs to a project managed by this manager
        if (application == null ||
                application.getProject() == null ||
                !application.getProject().getManagerInCharge().equals(this)) {
            return false;
        }

        // Update application status
        application.setStatus(ApplicationStatus.UNSUCCESSFUL);

        return true;
    }

    /**
     * Approves a withdrawal request for an application.
     * <p>
     * If the application status is BOOKED, releases the flat back into the available pool.
     *
     * @param application The application with a pending withdrawal
     * @return true if the approval was successful, false otherwise
     */
    public boolean approveWithdrawal(ProjectApplication application) {
        // Check if application is valid and belongs to a project managed by this manager
        if (application == null ||
                application.getProject() == null ||
                !application.getProject().getManagerInCharge().equals(this) ||
                !"PENDING".equals(application.getWithdrawalStatus())) {
            return false;
        }

        // Update application withdrawal status
        application.setWithdrawalStatus("APPROVED");

        // Remove the application from the applicant
        application.getApplicant().setAppliedProject(null);

        // If the application status is BOOKED, release the flat
        if (application.getStatus() == ApplicationStatus.BOOKED) {
            FlatType flatType = application.getSelectedFlatType();
            Project project = application.getProject();

            // In a real implementation, we would have a flatId stored in the application
            // and would call project.releaseFlat(flatId)

            // For now, we'll just increment the available units
            int currentUnits = project.getFlatTypeUnits().getOrDefault(flatType, 0);
            project.updateFlatAvailability(flatType, currentUnits + 1);
        }

        return true;
    }

    /**
     * Rejects a withdrawal request for an application.
     *
     * @param application The application with a pending withdrawal
     * @return true if the rejection was successful, false otherwise
     */
    public boolean rejectWithdrawal(ProjectApplication application) {
        // Check if application is valid and belongs to a project managed by this manager
        if (application == null ||
                application.getProject() == null ||
                !application.getProject().getManagerInCharge().equals(this) ||
                !"PENDING".equals(application.getWithdrawalStatus())) {
            return false;
        }

        // Update application withdrawal status
        application.setWithdrawalStatus("REJECTED");

        return true;
    }

    /**
     * Filters all projects to find those managed by the specified manager.
     *
     * @param allProjects List of all projects in the system
     * @param currentManager The manager whose projects to filter for
     * @return List of projects managed by the specified manager
     */
    public List<Project> ownProjects(List<Project> allProjects, HDBManager currentManager) {
        List<Project> ownedProjects = new ArrayList<>();

        // Loop through all projects and check if their managerInCharge matches the currentManager
        for (Project project : allProjects) {
            // Here, we're assuming that the project has a 'managerInCharge' field of type 'HDBManager'
            if (project.getManagerInCharge().equals(currentManager)) {
                ownedProjects.add(project);
            }
        }

        return ownedProjects;
    }

    /**
     * Generates a report of flat bookings based on the provided filter criteria.
     *
     * @param criteria The filter criteria to apply to the report
     * @return A report containing filtered booking information
     */
    public Report generateReport(FilterCriteria criteria) {
        Report report = new Report("BTO Flat Booking Report", criteria);
        // Collect all bookings from managed projects
        for (Project project : this.managedProjects) {
            // Get applications with BOOKED status
            for (ProjectApplication application : project.getApplications()) {
                if (application.getStatus() == ApplicationStatus.BOOKED) {
                    // Create a booking from the application data
                    FlatBooking booking = new FlatBooking(
                            application.getApplicant(),
                            project,
                            application.getSelectedFlatType(),
                            0  // Default value for flat ID
                    );
                    report.addBooking(booking);
                }
            }
        }

        return report;
    }

    /**
     * Retrieves all enquiries in the system.
     *
     * @return A list of all enquiries
     */
    public List<Enquiry> viewAllEnquiries() {
        // This would typically involve querying a database or repository
        // For simplicity, we're returning an empty list
        return new ArrayList<>();
    }

    /**
     * Responds to an enquiry.
     * <p>
     * Checks if the enquiry is valid and related to a project managed by this manager.
     *
     * @param enquiry The enquiry to respond to
     * @return true if the response was recorded successfully, false otherwise
     */
    public boolean respondToEnquiry(Enquiry enquiry) {
        // Check if enquiry is valid
        if (enquiry == null || enquiry.isResponded()) {
            return false;
        }

        // If enquiry is related to a project, check if it belongs to this manager
        if (enquiry.getProject() != null && !enquiry.getProject().getManagerInCharge().equals(this)) {
            return false;
        }

        // Mark as responded (actual response text is set by the caller)
        enquiry.setResponded(true);
        enquiry.setRespondedBy(this);
        enquiry.setResponseDate(new Date());

        return true;
    }

    /**
     * Displays the manager's profile information including personal details and managed projects.
     *
     * @return A formatted string containing the manager's profile information
     */
    @Override
    public String displayProfile() {
        StringBuilder profile = new StringBuilder();
        profile.append("\n=== MANAGER PROFILE ===\n");
        profile.append("Name: ").append(getName()).append("\n");
        profile.append("NRIC: ").append(getNric()).append("\n");
        profile.append("Age: ").append(getAge()).append("\n");
        profile.append("Marital Status: ").append(getMaritalStatus()).append("\n");

        // Display managed projects if any
        List<Project> managedProjects = getManagedProjects();
        if (managedProjects.isEmpty()) {
            profile.append("Managed Projects: None\n");
        } else {
            profile.append("Managed Projects:\n");
            for (Project project : managedProjects) {
                profile.append(" - ").append(project.getProjectName());

                // Add visibility status
                profile.append(" (").append(project.isVisible() ? "Visible" : "Hidden").append(")\n");

                // Add application period
                profile.append("   Application Period: ")
                        .append(project.getApplicationOpenDate())
                        .append(" to ")
                        .append(project.getApplicationCloseDate())
                        .append("\n");

                // Add number of applications
                int applicationCount = project.getApplications().size();
                profile.append("   Total Applications: ").append(applicationCount).append("\n");
            }
        }

        return profile.toString();
    }

    /**
     * Gets the list of projects managed by this manager.
     *
     * @return The list of managed projects
     */
    public List<Project> getManagedProjects() {
        return managedProjects;
    }

    /**
     * Adds a project to the list of projects managed by this manager.
     *
     * @param project The project to add
     */
    public void addManagedProject(Project project) {
        if (project != null && !managedProjects.contains(project)) {
            managedProjects.add(project);
        }
    }

    /**
     * Removes a project from the list of projects managed by this manager.
     *
     * @param project The project to remove
     */
    public void removeManagedProject(Project project) {
        managedProjects.remove(project);
    }

    /**
     * Gets the first managed project, if any.
     * <p>
     * This is a legacy method maintained for backward compatibility.
     *
     * @return The first managed project, or null if none exist
     */
    public Project getManagedProject() {
        return managedProjects.isEmpty() ? null : managedProjects.get(0);
    }

    /**
     * Sets the managed project list to contain only the specified project.
     * <p>
     * This is a legacy method maintained for backward compatibility.
     *
     * @param project The project to set as the managed project
     */
    public void setManagedProject(Project project) {
        if (project == null) {
            managedProjects.clear();
        } else if (!managedProjects.contains(project)) {
            managedProjects.add(project);
        }
    }
}