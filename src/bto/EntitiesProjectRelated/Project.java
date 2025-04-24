package bto.EntitiesProjectRelated;

import bto.Enums.*;
import bto.Entities.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Project class represents a Build-To-Order (BTO) housing project.
 * It contains information about the project's details, available flat types,
 * application period, and manages the relationships with officers, applicants,
 * and their respective registrations and applications.
 * <p>
 * This class also handles flat availability tracking and eligibility checking for applicants.
 */
public class Project {
    /** The name of the housing project */
    private String projectName;

    /** The neighborhood or area where the project is located */
    private String neighborhood;

    /**
     * Tracks the total number of housing units allocated for each flat type.
     * Maps flat types to their respective unit counts within this project.
     */
    private Map<FlatType, Integer> flatTypeUnits;

    /**
     * Indicates whether the project is visible to applicants.
     * Projects may be hidden during preparation or after completion.
     */
    private boolean isVisible;

    /** The date when applications for this project open */
    private Date applicationOpenDate;

    /** The date when applications for this project close */
    private Date applicationCloseDate;

    /** The HDB manager responsible for overseeing this project */
    private HDBManager managerInCharge;

    /** The number of HDB officer slots currently available for this project */
    private int availableHDBOfficerSlots;

    /** List of all enquiries related to this project */
    private List<Enquiry> enquiries;

    /** List of all applications submitted for this project */
    private List<ProjectApplication> applications;

    /** List of all officer registrations assigned to this project */
    private List<OfficerRegistration> officerRegistrations;

    /** The total number of officer slots allocated for this project */
    private int totalOfficerSlots;

    /**
     * Manages the collection of individual flat units within this project.
     * Contains detailed information about each flat including status and attributes.
     */
    private ProjectFlats projectFlats;

    /**
     * Default constructor for Project.
     * Initializes all collections, sets visibility to false, and creates a new ProjectFlats instance.
     */
    public Project() {
        flatTypeUnits = new HashMap<>();
        enquiries = new ArrayList<>();
        applications = new ArrayList<>();
        officerRegistrations = new ArrayList<>();
        isVisible = false;
        projectFlats = new ProjectFlats(this);
    }

    /**
     * Parameterized constructor for Project.
     * Creates a project with the specified name, neighborhood, and manager.
     *
     * @param projectName The name of the project
     * @param neighborhood The neighborhood where the project is located
     * @param managerInCharge The HDB manager responsible for this project
     */
    public Project(String projectName, String neighborhood, HDBManager managerInCharge) {
        this();
        this.projectName = projectName;
        this.neighborhood = neighborhood;
        this.managerInCharge = managerInCharge;
    }

    /**
     * Initializes the flat units for this project.
     * This method should be called after setting the flatTypeUnits map.
     */
    public void initializeProjectFlats() {
        projectFlats.initializeFlats(flatTypeUnits);
    }

    /**
     * Updates the count of available flats for a specific flat type.
     * This method is called by ProjectFlats when a flat is booked or released.
     *
     * @param flatType The type of flat
     * @param newCount The new count of available flats
     * @return true if the update was successful, false otherwise
     */
    public boolean updateFlatTypeCount(FlatType flatType, int newCount) {
        if (newCount < 0) {
            return false;
        }

        flatTypeUnits.put(flatType, newCount);
        return true;
    }

    /**
     * Books a flat of the specified type for an applicant.
     *
     * @param flatType The type of flat to book
     * @return The ID of the booked flat, or -1 if no flat of that type is available
     */
    public int bookFlat(FlatType flatType) {
        return projectFlats.bookFlat(flatType);
    }

    /**
     * Releases a previously booked flat.
     *
     * @param flatId The ID of the flat to release
     * @return true if the flat was successfully released, false otherwise
     */
    public boolean releaseFlat(int flatId) {
        return projectFlats.releaseFlat(flatId);
    }

    /**
     * Gets the number of available flats of the specified type.
     *
     * @param flatType The type of flat
     * @return The number of available flats of the specified type
     */
    public int getAvailableFlatCount(FlatType flatType) {
        return projectFlats.getAvailableFlatCount(flatType);
    }

    /**
     * Determines which flat types a user is eligible to apply for based on their age and marital status.
     * Singles age 35 and above are eligible for 2-Room flats only.
     * Married couples age 21 and above are eligible for all flat types.
     *
     * @param user The user to check eligibility for
     * @return A list of FlatType objects that the user is eligible to apply for
     */
    public List<FlatType> getEligibleFlatTypes(User user) {
        List<FlatType> eligibleTypes = new ArrayList<>();

        // Implementation for eligibility check based on user type, age, marital status
        // Singles >= 35 years old are eligible for 2-Room flats only
        // Married couples >= 21 years old are eligible for all flat types
        if (user.getMaritalStatus() == MaritalStatus.SINGLE && user.getAge() >= 35) {
            eligibleTypes.add(FlatType.TWO_ROOM);
        } else if (user.getMaritalStatus() == MaritalStatus.MARRIED && user.getAge() >= 21) {
            eligibleTypes.add(FlatType.TWO_ROOM);
            eligibleTypes.add(FlatType.THREE_ROOM);
        }

        return eligibleTypes;
    }

    /**
     * Gets a formatted string containing details about this project.
     * Includes project name, neighborhood, application period, and available flat types.
     *
     * @return A formatted string of project details
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Project Name: ").append(projectName).append("\n");
        details.append("Neighborhood: ").append(neighborhood).append("\n");
        details.append("Application Period: ").append(applicationOpenDate).append(" to ").append(applicationCloseDate).append("\n");
        details.append("Available Flat Types:\n");

        for (Map.Entry<FlatType, Integer> entry : flatTypeUnits.entrySet()) {
            details.append("- ").append(entry.getKey()).append(": ").append(entry.getValue()).append(" units\n");
        }

        return details.toString();
    }

    /**
     * Updates the flat availability for a specific flat type directly.
     * This method is kept for backward compatibility but now delegates to ProjectFlats.
     *
     * @param flatType The type of flat
     * @param quantity The new quantity of available flats
     * @return true if the update was successful, false otherwise
     */
    public boolean updateFlatAvailability(FlatType flatType, int quantity) {
        if (quantity < 0) {
            return false;
        }

        flatTypeUnits.put(flatType, quantity);

        // Re-initialize project flats with the updated quantities
        Map<FlatType, Integer> updatedFlats = new HashMap<>(flatTypeUnits);
        projectFlats = new ProjectFlats(this);
        projectFlats.initializeFlats(updatedFlats);

        return true;
    }

    /**
     * Toggles the visibility of this project.
     * Visible projects can be seen and applied to by eligible applicants.
     *
     * @param visible The new visibility status to set
     * @return true indicating the operation was successful
     */
    public boolean toggleVisibility(boolean visible) {
        this.isVisible = visible;
        return true;
    }

    /**
     * Gets a list of all enquiries related to this project.
     * Alias for getEnquiries() for backward compatibility.
     *
     * @return List of Enquiry objects for this project
     */
    public List<Enquiry> getProjectEnquiries() {
        return enquiries;
    }

    /**
     * Gets a list of all HDB officers with approved registrations for this project.
     *
     * @return List of HDBOfficer objects with approved registrations
     */
    public List<HDBOfficer> getApprovedOfficers() {
        List<HDBOfficer> approvedOfficers = new ArrayList<>();

        for (OfficerRegistration registration : officerRegistrations) {
            if (registration.getRegistrationStatus().equals("APPROVED")) {
                approvedOfficers.add(registration.getHdbOfficer());
            }
        }

        return approvedOfficers;
    }

    /**
     * Gets the name of this project.
     *
     * @return The project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Sets the name of this project.
     *
     * @param projectName The project name to set
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Gets the neighborhood where this project is located.
     *
     * @return The neighborhood
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Sets the neighborhood where this project is located.
     *
     * @param neighborhood The neighborhood to set
     */
    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    /**
     * Gets the map of flat types to the number of units available for each type.
     *
     * @return A map where keys are FlatType objects and values are the number of units
     */
    public Map<FlatType, Integer> getFlatTypeUnits() {
        return flatTypeUnits;
    }

    /**
     * Sets the map of flat types to the number of units available for each type.
     * Also initializes the project flats with the new units.
     *
     * @param flatTypeUnits The map of flat types to units to set
     */
    public void setFlatTypeUnits(Map<FlatType, Integer> flatTypeUnits) {
        this.flatTypeUnits = flatTypeUnits;
        initializeProjectFlats(); // Initialize the project flats with the new units
    }

    /**
     * Checks if this project is visible to applicants.
     *
     * @return true if the project is visible, false otherwise
     */
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * Sets the visibility of this project.
     *
     * @param visible The visibility status to set
     */
    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    /**
     * Gets the date when applications for this project open.
     *
     * @return The application open date
     */
    public Date getApplicationOpenDate() {
        return applicationOpenDate;
    }

    /**
     * Sets the date when applications for this project open.
     *
     * @param applicationOpenDate The application open date to set
     */
    public void setApplicationOpenDate(Date applicationOpenDate) {
        this.applicationOpenDate = applicationOpenDate;
    }

    /**
     * Gets the date when applications for this project close.
     *
     * @return The application close date
     */
    public Date getApplicationCloseDate() {
        return applicationCloseDate;
    }

    /**
     * Sets the date when applications for this project close.
     *
     * @param applicationCloseDate The application close date to set
     */
    public void setApplicationCloseDate(Date applicationCloseDate) {
        this.applicationCloseDate = applicationCloseDate;
    }

    /**
     * Gets the HDB manager in charge of this project.
     *
     * @return The manager in charge
     */
    public HDBManager getManagerInCharge() {
        return managerInCharge;
    }

    /**
     * Sets the HDB manager in charge of this project.
     *
     * @param managerInCharge The manager in charge to set
     */
    public void setManagerInCharge(HDBManager managerInCharge) {
        this.managerInCharge = managerInCharge;
    }

    /**
     * Gets the number of available slots for HDB officers to register for this project.
     *
     * @return The number of available HDB officer slots
     */
    public int getAvailableHDBOfficerSlots() {
        return availableHDBOfficerSlots;
    }

    /**
     * Sets the number of available slots for HDB officers to register for this project.
     *
     * @param availableHDBOfficerSlots The number of available HDB officer slots to set
     */
    public void setAvailableHDBOfficerSlots(int availableHDBOfficerSlots) {
        this.availableHDBOfficerSlots = availableHDBOfficerSlots;
    }

    /**
     * Gets the list of enquiries related to this project.
     *
     * @return The list of enquiries
     */
    public List<Enquiry> getEnquiries() {
        return enquiries;
    }

    /**
     * Sets the list of enquiries related to this project.
     *
     * @param enquiries The list of enquiries to set
     */
    public void setEnquiries(List<Enquiry> enquiries) {
        this.enquiries = enquiries;
    }

    /**
     * Gets the list of applications for this project.
     *
     * @return The list of applications
     */
    public List<ProjectApplication> getApplications() {
        return applications;
    }

    /**
     * Sets the list of applications for this project.
     *
     * @param applications The list of applications to set
     */
    public void setApplications(List<ProjectApplication> applications) {
        this.applications = applications;
    }

    /**
     * Gets the list of officer registrations for this project.
     *
     * @return The list of officer registrations
     */
    public List<OfficerRegistration> getOfficerRegistrations() {
        return officerRegistrations;
    }

    /**
     * Sets the list of officer registrations for this project.
     *
     * @param officerRegistrations The list of officer registrations to set
     */
    public void setOfficerRegistrations(List<OfficerRegistration> officerRegistrations) {
        this.officerRegistrations = officerRegistrations;
    }

    /**
     * Gets the ProjectFlats object that manages individual flats for this project.
     *
     * @return The ProjectFlats object
     */
    public ProjectFlats getProjectFlats() {
        return projectFlats;
    }

    /**
     * Adds an enquiry to this project if it's not already in the list.
     *
     * @param enquiry The enquiry to add
     */
    public void addEnquiry(Enquiry enquiry) {
        if (!enquiries.contains(enquiry)) {
            enquiries.add(enquiry);
        }
    }

    /**
     * Adds an application to this project if it's not already in the list.
     *
     * @param application The application to add
     */
    public void addApplication(ProjectApplication application) {
        if (!applications.contains(application)) {
            applications.add(application);
        }
    }

    /**
     * Adds an officer registration to this project if it's not already in the list.
     *
     * @param registration The officer registration to add
     */
    public void addOfficerRegistration(OfficerRegistration registration) {
        if (!officerRegistrations.contains(registration)) {
            officerRegistrations.add(registration);
        }
    }

    /**
     * Gets the total number of slots available for HDB officers for this project.
     * This represents the maximum capacity, while availableHDBOfficerSlots represents the currently available slots.
     *
     * @return The total number of officer slots
     */
    public int getTotalOfficerSlots() {
        return totalOfficerSlots;
    }

    /**
     * Sets the total number of slots available for HDB officers for this project.
     *
     * @param totalSlots The total number of officer slots to set
     */
    public void setTotalOfficerSlots(int totalSlots) {
        this.totalOfficerSlots = totalSlots;
    }
}