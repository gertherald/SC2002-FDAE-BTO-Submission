package bto.Boundaries;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bto.Controllers.*;
import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import bto.Enums.*;
import bto.Interfaces.*;

public class ManagerInterface {

    /** Scanner for reading user input */
    private final Scanner scanner;

    // Controllers (same instance shared across all interfaces)
    /** Authentication controller for managing user authentication */
    private final IAuthController authController;

    /** Controller for managing project-related operations */
    private final IProjectController projectController;

    /** Controller for managing application processes */
    private final IApplicationController applicationController;

    /** Controller for handling enquiries */
    private final IEnquiryController enquiryController;

    /** Controller for generating reports */
    private final IReportController reportController;

    /** Controller for user registration processes */
    private final IRegistrationController registrationController;

    /** Controller for managing withdrawal requests */
    private final IWithdrawalController withdrawalController;

    /** Controller for booking-related operations */
    private final IBookingController bookingController;

    /** Generator for creating receipts */
    private final IReceiptGenerator receiptGenerator;

    /** Reference to the main user interface */
    private final UserInterface userInterface;

    // Constructor takes in controllers
    /**
     * Constructor initializes all controllers and dependencies.
     *
     * @param scanner Input scanner for user interactions
     * @param authController Controller for authentication processes
     * @param projectController Controller for project-related operations
     * @param applicationController Controller for managing applications
     * @param enquiryController Controller for handling enquiries
     * @param reportController Controller for generating reports
     * @param registrationController Controller for user registration
     * @param withdrawalController Controller for managing withdrawals
     * @param bookingController Controller for booking operations
     * @param receiptGenerator Generator for creating receipts
     * @param userInterface Reference to the main user interface
     */
    public ManagerInterface(Scanner scanner, IAuthController authController, IProjectController projectController,
                            IApplicationController applicationController, IEnquiryController enquiryController,
                            IReportController reportController, IRegistrationController registrationController,
                            IWithdrawalController withdrawalController, IBookingController bookingController,
                            IReceiptGenerator receiptGenerator, UserInterface userInterface) {
        this.scanner = scanner;
        this.authController = authController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.enquiryController = enquiryController;
        this.reportController = reportController;
        this.registrationController = registrationController;
        this.withdrawalController = withdrawalController;
        this.bookingController = bookingController;
        this.receiptGenerator = receiptGenerator;
        this.userInterface = userInterface;
    }

    /**
     * Displays the main menu for an HDB Manager user.
     * Provides access to various management functions like project creation,
     * application processing, reporting, etc.
     *
     * @param manager The currently logged-in HDB Manager
     */
    public void displayManagerMenu(HDBManager manager) {
        while (true) {
            System.out.println("\n======== HDB MANAGER PORTAL ========");
            System.out.println("Welcome, " + manager.getName() + " (" + manager.getNric() + ")");
            System.out.println("========================================");
            System.out.println("\n=== HDB MANAGER MENU ===");
            System.out.println("1. Create Project");
            System.out.println("2. Edit Project");
            System.out.println("3. Delete Project");
            System.out.println("4. Toggle Project Visibility");
            System.out.println("5. View All Projects");
            System.out.println("6. View My Projects");
            System.out.println("7. View HDB Officer Registrations");
            System.out.println("8. Approve/Reject HDB Officer Registration");
            System.out.println("9. Approve/Reject Application");
            System.out.println("10. Approve/Reject Withdrawal");
            System.out.println("11. Generate Report");
            System.out.println("12. View All Enquiries");
            System.out.println("13. Respond to Enquiry");
            System.out.println("14. Display Profile");
            System.out.println("15. Change Password");
            System.out.println("0. Logout");

            while (true) {
                System.out.print("Enter your choice: ");
                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty. Please enter a valid number.");
                    continue;
                }

                try {
                    int choice = Integer.parseInt(input);

                    switch (choice) {
                        case 1:
                            createProject(manager);
                            break;
                        case 2:
                            editProject(manager);
                            break;
                        case 3:
                            deleteProject(manager);
                            break;
                        case 4:
                            toggleProjectVisibility(manager);
                            break;
                        case 5:
                            viewAllProjects();
                            break;
                        case 6:
                            viewMyProjects(manager);
                            break;
                        case 7:
                            viewOfficerRegistrations(manager);
                            break;
                        case 8:
                            approveRejectOfficerRegistration(manager);
                            break;
                        case 9:
                            approveRejectApplication(manager);
                            break;
                        case 10:
                            approveRejectWithdrawal(manager);
                            break;
                        case 11:
                            generateReport(manager);
                            break;
                        case 12:
                            viewAllEnquiries();
                            break;
                        case 13:
                            respondToEnquiry(manager);
                            break;
                        case 14:
                            displayManagerProfile(manager);
                        case 15:
                            changePassword(manager);
                            break;
                        case 0:
                            showMessage("Logging out...");
                            userInterface.displayLoginMenu();
                            return;
                        default:
                            showMessage("Invalid choice. Please try again.");
                            continue;
                    }
                    break; // Exit the loop if a valid choice was processed
                } catch (NumberFormatException e) {
                    showMessage("Invalid input. Please enter a number.");
                }
            }
        }
    }

    /**
     * Allows the manager to create a new project with detailed specifications.
     *
     * @param manager The HDB Manager creating the project
     */
    private void createProject(HDBManager manager) {
        showMessage("\n=== CREATE NEW PROJECT ===");

        // Get project details
        String projectName = getInput("Enter Project Name: ");
        String neighborhood = getInput("Enter Neighborhood (e.g., Yishun, Boon Lay): ");

        // Create project with basic details
        Project project = new Project(projectName, neighborhood, manager);

        // Create a map to store flat types and their quantities
        Map<FlatType, Integer> flatTypeUnits = new HashMap<>();

        // Get flat type units
        int twoRoomUnits = getIntegerInput("Enter number of 2-Room units: ", 0);
        int threeRoomUnits = getIntegerInput("Enter number of 3-Room units: ", 0);

        // Add to map
        flatTypeUnits.put(FlatType.TWO_ROOM, twoRoomUnits);
        flatTypeUnits.put(FlatType.THREE_ROOM, threeRoomUnits);

        // Set flat type units to project
        project.setFlatTypeUnits(flatTypeUnits);

        // Get application dates
        Date openDate = null;
        while (openDate == null) {
            String openDateStr = getInput("Enter Application Opening Date (DD/MM/YYYY): ");
            openDate = parseDate(openDateStr);
        }

        Date closeDate = null;
        while (closeDate == null) {
            String closeDateStr = getInput("Enter Application Closing Date (DD/MM/YYYY): ");
            closeDate = parseDate(closeDateStr);
        }

        // Set application dates
        project.setApplicationOpenDate(openDate);
        project.setApplicationCloseDate(closeDate);

        // Get HDB Officer slots
        int officerSlots = getIntegerInput("Enter number of HDB Officer slots (max 10): ", 1, 10);

        // Set officer slots
        project.setAvailableHDBOfficerSlots(officerSlots);

        // Set visibility (initially hidden)
        project.setVisible(false);

        // Use the manager's createProject method to check for overlapping application periods
        Project createdProject = manager.createProject(project);

        if (createdProject != null) {
            // If successful, add to the project controller
            projectController.createProject(createdProject);
            showMessage("Project created successfully!");
        } else {
            showMessage("Failed to create project. The application period may overlap with another project you're managing, or the project name may already exist.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Provides an interface for editing an existing project's details.
     *
     * @param manager The HDB Manager editing the project
     */
    private void editProject(HDBManager manager) {
        showMessage("\n=== EDIT PROJECT ===");

        // Get the manager's projects
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        if (managerProjects.isEmpty()) {
            showMessage("You don't have any projects to edit.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display the manager's projects
        showMessage("Your Projects:");
        for (int i = 0; i < managerProjects.size(); i++) {
            Project project = managerProjects.get(i);
            System.out.println((i+1) + ". " + project.getProjectName() + " - " +
                    (project.isVisible() ? "Visible" : "Hidden"));
        }

        // Get user selection
        int selection = getIntegerInput("Select a project to edit (0 to cancel): ", 0, managerProjects.size());

        if (selection == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        Project selectedProject = managerProjects.get(selection - 1);

        // Display edit options
        showMessage("\nEdit Options:");
        System.out.println("1. Project Name");
        System.out.println("2. Neighborhood");
        System.out.println("3. Application Opening Date");
        System.out.println("4. Application Closing Date");
        System.out.println("5. Flat Type Units");
        System.out.println("6. HDB Officer Slots");
        System.out.println("0. Cancel");

        int editOption = getIntegerInput("Select what to edit: ", 0, 6);

        if (editOption == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Handle edit based on selected option
        switch (editOption) {
            case 1:
                String newName = getInput("Enter new Project Name [" + selectedProject.getProjectName() + "]: ");
                if (!newName.trim().isEmpty()) {
                    if (manager.editProject(selectedProject, "projectname", newName)) {
                        showMessage("Project name updated successfully!");
                    } else {
                        showMessage("Failed to update project name.");
                    }
                }
                break;

            case 2:
                String newNeighborhood = getInput("Enter new Neighborhood [" + selectedProject.getNeighborhood() + "]: ");
                if (!newNeighborhood.trim().isEmpty()) {
                    if (manager.editProject(selectedProject, "neighborhood", newNeighborhood)) {
                        showMessage("Project neighborhood updated successfully!");
                    } else {
                        showMessage("Failed to update project neighborhood.");
                    }
                }
                break;

            case 3:
                Date newOpenDate = null;
                while (newOpenDate == null) {
                    String dateStr = getInput("Enter new Application Opening Date (DD/MM/YYYY) [" +
                            selectedProject.getApplicationOpenDate() + "]: ");
                    if (dateStr.trim().isEmpty()) {
                        break; // User didn't want to change
                    }
                    newOpenDate = parseDate(dateStr);
                }

                if (newOpenDate != null) {
                    if (manager.editProject(selectedProject, "applicationopendate", newOpenDate)) {
                        showMessage("Application opening date updated successfully!");
                    } else {
                        showMessage("Failed to update application opening date.");
                    }
                }
                break;

            case 4:
                Date newCloseDate = null;
                while (newCloseDate == null) {
                    String dateStr = getInput("Enter new Application Closing Date (DD/MM/YYYY) [" +
                            selectedProject.getApplicationCloseDate() + "]: ");
                    if (dateStr.trim().isEmpty()) {
                        break; // User didn't want to change
                    }
                    newCloseDate = parseDate(dateStr);
                }

                if (newCloseDate != null) {
                    if (manager.editProject(selectedProject, "applicationclosedate", newCloseDate)) {
                        showMessage("Application closing date updated successfully!");
                    } else {
                        showMessage("Failed to update application closing date.");
                    }
                }
                break;

            case 5:
                // Get current flat type units
                Map<FlatType, Integer> currentUnits = selectedProject.getFlatTypeUnits();
                int currentTwoRoomUnits = currentUnits.getOrDefault(FlatType.TWO_ROOM, 0);
                int currentThreeRoomUnits = currentUnits.getOrDefault(FlatType.THREE_ROOM, 0);

                String newTwoRoomUnitsStr = getInput("Enter new number of 2-Room units [" + currentTwoRoomUnits + "]: ");
                String newThreeRoomUnitsStr = getInput("Enter new number of 3-Room units [" + currentThreeRoomUnits + "]: ");

                // Create updated map if either input is non-empty
                if (!newTwoRoomUnitsStr.trim().isEmpty() || !newThreeRoomUnitsStr.trim().isEmpty()) {
                    Map<FlatType, Integer> newUnits = new HashMap<>(currentUnits);

                    if (!newTwoRoomUnitsStr.trim().isEmpty()) {
                        try {
                            int newTwoRoomUnits = Integer.parseInt(newTwoRoomUnitsStr);
                            if (newTwoRoomUnits < 0) {
                                showMessage("Number of units cannot be negative. Using current value.");
                            } else {
                                newUnits.put(FlatType.TWO_ROOM, newTwoRoomUnits);
                            }
                        } catch (NumberFormatException e) {
                            showMessage("Invalid input for 2-Room units. Using current value.");
                        }
                    }

                    if (!newThreeRoomUnitsStr.trim().isEmpty()) {
                        try {
                            int newThreeRoomUnits = Integer.parseInt(newThreeRoomUnitsStr);
                            if (newThreeRoomUnits < 0) {
                                showMessage("Number of units cannot be negative. Using current value.");
                            } else {
                                newUnits.put(FlatType.THREE_ROOM, newThreeRoomUnits);
                            }
                        } catch (NumberFormatException e) {
                            showMessage("Invalid input for 3-Room units. Using current value.");
                        }
                    }

                    if (manager.editProject(selectedProject, "flattypeunits", newUnits)) {
                        showMessage("Flat type units updated successfully!");
                    } else {
                        showMessage("Failed to update flat type units.");
                    }
                }
                break;

            case 6:
                int currentSlots = selectedProject.getTotalOfficerSlots();
                int newSlots = getIntegerInput("Enter new number of HDB Officer slots (max 10) [" + currentSlots + "]: ", 1, 10);

                if (manager.editProject(selectedProject, "availablehdbofficerunits", newSlots)) {
                    showMessage("HDB Officer slots updated successfully!");
                } else {
                    showMessage("Failed to update HDB Officer slots.");
                }
                break;

            default:
                showMessage("Invalid option. Operation cancelled.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Helper method to parse date string in DD/MM/YYYY format to Date object
    /**
     * Parses a date string in DD/MM/YYYY format to a Date object.
     *
     * @param dateStr The date string to parse
     * @return A Date object representing the parsed date, or null if parsing fails
     */
    private Date parseDate(String dateStr) {
        try {
            String[] parts = dateStr.split("/");
            if (parts.length != 3) {
                showMessage("Invalid date format. Please use DD/MM/YYYY format.");
                return null;
            }

            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]) - 1; // Month is 0-based in Calendar
            int year = Integer.parseInt(parts[2]);

            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.set(year, month, day, 0, 0, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);

            return calendar.getTime();
        } catch (NumberFormatException e) {
            showMessage("Error parsing date. Please use DD/MM/YYYY format with numbers only.");
            return null;
        }
    }

    /**
     * Deletes a project that belongs to the manager
     *
     * @param manager The HDBManager who owns the project to delete
     */
    private void deleteProject(HDBManager manager) {
        showMessage("\n=== DELETE PROJECT ===");

        // Get the manager's projects
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        if (managerProjects.isEmpty()) {
            showMessage("You don't have any projects to delete.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display the manager's projects
        showMessage("Your Projects:");
        for (int i = 0; i < managerProjects.size(); i++) {
            Project project = managerProjects.get(i);
            System.out.println((i+1) + ". " + project.getProjectName() + " - " +
                    (project.isVisible() ? "Visible" : "Hidden"));
        }

        // Get user selection
        int selection = getIntegerInput("Select a project to delete (0 to cancel): ", 0, managerProjects.size());

        if (selection == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        Project selectedProject = managerProjects.get(selection - 1);

        // Confirm deletion
        String confirm = getInput("Are you sure you want to delete " + selectedProject.getProjectName() + "? (Y/N): ");

        if (confirm.equalsIgnoreCase("Y")) {
            // First delete from manager's list
            manager.removeManagedProject(selectedProject);

            // Then delete from the controller
            if (projectController.deleteProject(selectedProject)) {
                showMessage("Project deleted successfully!");
            } else {
                // If controller deletion fails, add it back to manager's list
                manager.addManagedProject(selectedProject);
                showMessage("Failed to delete project.");
            }
        } else {
            showMessage("Operation cancelled.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Provides an interface to toggle the visibility of a project.
     *
     * @param manager The HDB Manager toggling project visibility
     */
    private void toggleProjectVisibility(HDBManager manager) {
        showMessage("\n=== TOGGLE PROJECT VISIBILITY ===");

        // Get the manager's projects
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        if (managerProjects.isEmpty()) {
            showMessage("You don't have any projects to toggle visibility.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display the manager's projects
        showMessage("Your Projects:");
        for (int i = 0; i < managerProjects.size(); i++) {
            Project project = managerProjects.get(i);
            System.out.println((i+1) + ". " + project.getProjectName() + " - " +
                    (project.isVisible() ? "Visible" : "Hidden"));
        }

        // Get user selection
        int selection = getIntegerInput("Select a project to toggle visibility (0 to cancel): ", 0, managerProjects.size());

        if (selection == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        Project selectedProject = managerProjects.get(selection - 1);

        // Toggle visibility
        boolean newVisibility = !selectedProject.isVisible();
        if (projectController.toggleVisibility(selectedProject, newVisibility)) {
            showMessage("Project visibility updated to: " + (newVisibility ? "Visible" : "Hidden"));
        } else {
            showMessage("Failed to update project visibility.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays details of all projects in the system.
     */
    private void viewAllProjects() {
        showMessage("\n=== ALL PROJECTS ===");

        List<Project> allProjects = projectController.getAllProjects();

        if (allProjects.isEmpty()) {
            showMessage("No projects found.");
        } else {
            for (Project project : allProjects) {
                displayProjectDetails(project);
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays details of projects managed by the current manager.
     *
     * @param manager The HDB Manager viewing their projects
     */
    private void viewMyProjects(HDBManager manager) {
        showMessage("\n=== MY PROJECTS ===");

        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        if (managerProjects.isEmpty()) {
            showMessage("You don't have any projects.");
        } else {
            for (Project project : managerProjects) {
                displayProjectDetails(project);
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays detailed information about a specific project.
     *
     * @param project The project to display details for
     */
    private void displayProjectDetails(Project project) {
        System.out.println("\nProject Name: " + project.getProjectName());
        System.out.println("Neighborhood: " + project.getNeighborhood());
        System.out.println("Visibility: " + (project.isVisible() ? "Visible" : "Hidden"));
        System.out.println("Manager: " + project.getManagerInCharge().getName() + " (" + project.getManagerInCharge().getNric() + ")");
        System.out.println("Application Period: " + project.getApplicationOpenDate() + " to " + project.getApplicationCloseDate());

        System.out.println("Flat Types:");
        Map<FlatType, Integer> flatTypeUnits = project.getFlatTypeUnits();
        for (Map.Entry<FlatType, Integer> entry : flatTypeUnits.entrySet()) {
            System.out.println("- " + entry.getKey() + ": " + entry.getValue() + " units");
        }

        System.out.println("Officer Slots: " + project.getAvailableHDBOfficerSlots() + " available");

        // Display assigned officers
        List<HDBOfficer> approvedOfficers = project.getApprovedOfficers();
        System.out.println("\nAssigned Officers:");
        if (approvedOfficers.isEmpty()) {
            System.out.println("- No officers assigned yet");
        } else {
            for (HDBOfficer officer : approvedOfficers) {
                System.out.println("- " + officer.getName() + " (" + officer.getNric() + ")");
            }
        }

        System.out.println("----------------------------------------");
    }

    /**
     * Displays all officer registrations for projects managed by this manager
     *
     * @param manager The HDBManager whose project registrations to view
     */
    private void viewOfficerRegistrations(HDBManager manager) {
        showMessage("\n=== HDB OFFICER REGISTRATIONS ===");

        // Get all registrations
        List<OfficerRegistration> allRegistrations = registrationController.getAllRegistrations();

        if (allRegistrations == null || allRegistrations.isEmpty()) {
            showMessage("No officer registrations found.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Filter registrations for this manager's projects
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        List<OfficerRegistration> managerRegistrations = new ArrayList<>();
        for (OfficerRegistration registration : allRegistrations) {
            if (managerProjects.contains(registration.getProject())) {
                managerRegistrations.add(registration);
            }
        }

        if (managerRegistrations.isEmpty()) {
            showMessage("No officer registrations found for your projects.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        showMessage("Pending Registrations:");
        int pendingCount = 0;
        for (OfficerRegistration registration : managerRegistrations) {
            if (registration.getRegistrationStatus().equals("PENDING")) {
                pendingCount++;
                displayRegistrationDetails(registration);
            }
        }

        if (pendingCount == 0) {
            showMessage("No pending registrations.");
        }

        showMessage("\nApproved Registrations:");
        int approvedCount = 0;
        for (OfficerRegistration registration : managerRegistrations) {
            if (registration.getRegistrationStatus().equals("APPROVED")) {
                approvedCount++;
                displayRegistrationDetails(registration);
            }
        }

        if (approvedCount == 0) {
            showMessage("No approved registrations.");
        }

        showMessage("\nRejected Registrations:");
        int rejectedCount = 0;
        for (OfficerRegistration registration : managerRegistrations) {
            if (registration.getRegistrationStatus().equals("REJECTED")) {
                rejectedCount++;
                displayRegistrationDetails(registration);
            }
        }

        if (rejectedCount == 0) {
            showMessage("No rejected registrations.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays details of a specific officer registration.
     *
     * @param registration The officer registration to display
     */
    private void displayRegistrationDetails(OfficerRegistration registration) {
        System.out.println("\nOfficer: " + registration.getHdbOfficer().getName() + " (" + registration.getHdbOfficer().getNric() + ")");
        System.out.println("Project: " + registration.getProject().getProjectName());
        System.out.println("Status: " + registration.getRegistrationStatus());
        System.out.println("----------------------------------------");
    }

    /**
     * Provides an interface for approving or rejecting officer registrations.
     *
     * @param manager The HDB Manager processing registrations
     */
    private void approveRejectOfficerRegistration(HDBManager manager) {
        showMessage("\n=== APPROVE/REJECT OFFICER REGISTRATION ===");

        // Get pending registrations for manager's projects
        List<OfficerRegistration> pendingRegistrations = new ArrayList<>();
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        for (OfficerRegistration registration : registrationController.getAllRegistrations()) {
            if (registration.getRegistrationStatus().equals("PENDING") &&
                    managerProjects.contains(registration.getProject())) {
                pendingRegistrations.add(registration);
            }
        }

        if (pendingRegistrations.isEmpty()) {
            showMessage("No pending officer registrations for your projects.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display pending registrations
        showMessage("Pending Registrations:");
        for (int i = 0; i < pendingRegistrations.size(); i++) {
            OfficerRegistration registration = pendingRegistrations.get(i);
            System.out.println((i+1) + ". Officer: " + registration.getHdbOfficer().getName() +
                    " - Project: " + registration.getProject().getProjectName());
        }

        // Get user selection
        int selection = getIntegerInput("Select a registration to process (0 to cancel): ", 0, pendingRegistrations.size());

        if (selection == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        OfficerRegistration selectedRegistration = pendingRegistrations.get(selection - 1);

        // Approve or reject
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        int actionChoice = getIntegerInput("Enter your choice: ", 1, 2);

        if (actionChoice == 1) {
            if (manager.approveRegistration(selectedRegistration)) {
                showMessage("Registration approved successfully!");

                // Update project's remaining officer slots
                registrationController.updateProjectSlots(selectedRegistration.getProject());

                // Get all other pending registrations for this officer
                HDBOfficer approvedOfficer = selectedRegistration.getHdbOfficer();
                Project approvedProject = selectedRegistration.getProject();

                // Check for overlapping application periods with other pending registrations
                for (OfficerRegistration otherReg : registrationController.getAllRegistrations()) {
                    // Skip if not the same officer or if already processed (not pending)
                    if (!otherReg.getHdbOfficer().getNric().equals(approvedOfficer.getNric()) ||
                            !otherReg.getRegistrationStatus().equals("PENDING") ||
                            otherReg == selectedRegistration) {
                        continue;
                    }

                    Project otherProject = otherReg.getProject();

                    // Check if application periods overlap
                    boolean periodsOverlap = datesOverlap(
                            approvedProject.getApplicationOpenDate(),
                            approvedProject.getApplicationCloseDate(),
                            otherProject.getApplicationOpenDate(),
                            otherProject.getApplicationCloseDate()
                    );

                    if (periodsOverlap) {
                        // Auto-reject this registration due to overlap
                        registrationController.rejectRegistration(otherReg);
                        showMessage("Note: Registration for " + otherProject.getProjectName() +
                                " was automatically rejected due to overlapping application periods.");
                    }
                }
            } else {
                showMessage("Failed to approve registration. All officer slots may be filled.");
            }
        } else if (actionChoice == 2) {
            if (registrationController.rejectRegistration(selectedRegistration)) {
                showMessage("Registration rejected successfully!");
            } else {
                showMessage("Failed to reject registration.");
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Checks if two date ranges overlap
     *
     * @param start1 Start date of first range
     * @param end1 End date of first range
     * @param start2 Start date of second range
     * @param end2 End date of second range
     * @return true if the ranges overlap, false otherwise
     */
    private boolean datesOverlap(Date start1, Date end1, Date start2, Date end2) {
        // Two date ranges overlap if start of one is before end of the other, for both directions
        return start1.before(end2) && start2.before(end1);
    }

    /**
     * Provides an interface for approving or rejecting project applications.
     *
     * @param manager The HDB Manager processing applications
     */
    private void approveRejectApplication(HDBManager manager) {
        showMessage("\n=== APPROVE/REJECT APPLICATION ===");

        // Get pending applications for manager's projects
        List<ProjectApplication> pendingApplications = new ArrayList<>();
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        for (ProjectApplication application : applicationController.getAllApplications()) {
            if (application.getStatus() == ApplicationStatus.PENDING &&
                    managerProjects.contains(application.getProject())) {
                pendingApplications.add(application);
            }
        }

        if (pendingApplications.isEmpty()) {
            showMessage("No pending applications for your projects.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display pending applications
        showMessage("Pending Applications:");
        for (int i = 0; i < pendingApplications.size(); i++) {
            ProjectApplication application = pendingApplications.get(i);
            System.out.println((i+1) + ". Applicant: " + application.getApplicant().getName() +
                    " - Project: " + application.getProject().getProjectName());
        }

        // Get user selection
        int selection = getIntegerInput("Select an application to process (0 to cancel): ", 0, pendingApplications.size());

        if (selection == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        ProjectApplication selectedApplication = pendingApplications.get(selection - 1);

        // Approve or reject
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        int actionChoice = getIntegerInput("Enter your choice: ", 1, 2);

        if (actionChoice == 1) {
            if (manager.approveApplication(selectedApplication)) {
                showMessage("Application approved successfully!");
            } else {
                showMessage("Failed to approve application. No available units for any eligible flat type.");
            }
        } else if (actionChoice == 2) {
            if (manager.rejectApplication(selectedApplication)) {
                showMessage("Application rejected successfully!");
            } else {
                showMessage("Failed to reject application.");
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Provides an interface for approving or rejecting withdrawal requests.
     *
     * @param manager The HDB Manager processing withdrawals
     */
    private void approveRejectWithdrawal(HDBManager manager) {
        showMessage("\n=== APPROVE/REJECT WITHDRAWAL ===");

        // Get pending withdrawals for manager's projects
        List<ProjectApplication> pendingWithdrawals = new ArrayList<>();
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        for (ProjectApplication application : applicationController.getAllApplications()) {
            if ("PENDING".equals(application.getWithdrawalStatus()) &&
                    managerProjects.contains(application.getProject())) {
                pendingWithdrawals.add(application);
            }
        }

        if (pendingWithdrawals.isEmpty()) {
            showMessage("No pending withdrawals for your projects.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display pending withdrawals
        showMessage("Pending Withdrawals:");
        for (int i = 0; i < pendingWithdrawals.size(); i++) {
            ProjectApplication application = pendingWithdrawals.get(i);
            System.out.println((i+1) + ". Applicant: " + application.getApplicant().getName() +
                    " - Project: " + application.getProject().getProjectName() +
                    " - Status: " + application.getStatus());
        }

        // Get user selection with proper validation
        int selection = getIntegerInput("Select a withdrawal to process (0 to cancel): ", 0, pendingWithdrawals.size());

        if (selection == 0) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        ProjectApplication selectedWithdrawal = pendingWithdrawals.get(selection - 1);
        Applicant applicant = selectedWithdrawal.getApplicant();

        // Get the Withdrawal object from WithdrawalController
        Withdrawal withdrawal = withdrawalController.getWithdrawalByApplicant(applicant);

        if (withdrawal == null) {
            showMessage("No withdrawal record found. Creating one now...");
            withdrawal = new Withdrawal(applicant, selectedWithdrawal);
            withdrawalController.addWithdrawal(withdrawal);
        }

        // Approve or reject
        System.out.println("1. Approve");
        System.out.println("2. Reject");
        int actionChoice = getIntegerInput("Enter your choice: ", 1, 2);

        if (actionChoice == 1) {
            if (withdrawalController.approveWithdrawal(withdrawal)) {
                showMessage("Withdrawal approved successfully!");
            } else {
                showMessage("Failed to approve withdrawal.");
            }
        } else if (actionChoice == 2) {
            if (withdrawalController.rejectWithdrawal(withdrawal)) {
                showMessage("Withdrawal rejected successfully!");
            } else {
                showMessage("Failed to reject withdrawal.");
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Generates a report based on user-selected filter criteria.
     *
     * @param manager The HDB Manager generating the report
     */
    private void generateReport(HDBManager manager) {
        showMessage("\n=== GENERATE REPORT ===");

        // Get filter criteria
        showMessage("Select Filter Criteria:");
        System.out.println("1. All Applicants");
        System.out.println("2. Married Applicants");
        System.out.println("3. Single Applicants");
        System.out.println("4. Applicants by Flat Type");
        System.out.println("5. Applicants by Project");
        System.out.println("6. Applicants by Age Range");

        int filterChoice = getIntegerInput("Enter your choice: ", 1, 6);

        // Create filter criteria
        FilterCriteria criteria = new FilterCriteria();

        switch (filterChoice) {
            case 1:
                // All applicants, no specific filter
                showMessage("Generating report for all applicants.");
                break;
            case 2:
                criteria.addCriterion("maritalStatus", MaritalStatus.MARRIED);
                showMessage("Generating report for married applicants.");
                break;
            case 3:
                criteria.addCriterion("maritalStatus", MaritalStatus.SINGLE);
                showMessage("Generating report for single applicants.");
                break;
            case 4:
                System.out.println("Select Flat Type:");
                System.out.println("1. 2-Room");
                System.out.println("2. 3-Room");
                int flatTypeChoice = getIntegerInput("Enter your choice: ", 1, 2);

                if (flatTypeChoice == 1) {
                    criteria.addCriterion("flatType", FlatType.TWO_ROOM);
                    showMessage("Generating report for 2-Room flat applications.");
                } else {
                    criteria.addCriterion("flatType", FlatType.THREE_ROOM);
                    showMessage("Generating report for 3-Room flat applications.");
                }
                break;
            case 5:
                // Get the manager's projects
                List<Project> projects = projectController.getAllProjects();
                List<Project> managerProjects = manager.ownProjects(projects, manager);

                if (managerProjects.isEmpty()) {
                    showMessage("You don't have any projects. Generating report for all projects.");
                } else {
                    // Display the manager's projects
                    showMessage("Your Projects:");
                    for (int i = 0; i < managerProjects.size(); i++) {
                        Project project = managerProjects.get(i);
                        System.out.println((i+1) + ". " + project.getProjectName());
                    }

                    int projectChoice = getIntegerInput("Select a project (0 for all): ", 0, managerProjects.size());

                    if (projectChoice > 0) {
                        Project selectedProject = managerProjects.get(projectChoice - 1);
                        criteria.addCriterion("project", selectedProject);
                        showMessage("Generating report for project: " + selectedProject.getProjectName());
                    } else {
                        showMessage("Generating report for all projects.");
                    }
                }
                break;
            case 6:
                int minAge = getIntegerInput("Enter minimum age: ", 0);
                int maxAge = getIntegerInput("Enter maximum age: ", minAge);

                criteria.addCriterion("minAge", minAge);
                criteria.addCriterion("maxAge", maxAge);
                showMessage("Generating report for applicants aged " + minAge + " to " + maxAge);
                break;
        }

        // Generate the report (no need to store it permanently)
        Report report = reportController.generateReport("BTO Flat Booking Report", criteria, manager.getManagedProjects());

        if (report != null) {
            // Display the report
            showMessage("\n=== REPORT ===");
            String formattedReport = reportController.getFormattedReport(report);
            showMessage(formattedReport);

            // Replace the save report logic with a simple confirmation
            showMessage("Report successfully generated.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays all enquiries in the system.
     */
    private void viewAllEnquiries() {
        showMessage("\n=== ALL ENQUIRIES ===");

        List<Enquiry> enquiries = enquiryController.getAllEnquiries();

        if (enquiries.isEmpty()) {
            showMessage("No enquiries found.");
        } else {
            for (Enquiry enquiry : enquiries) {
                displayEnquiryDetails(enquiry);
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays detailed information about a specific enquiry.
     *
     * @param enquiry The enquiry to display details for
     */
    private void displayEnquiryDetails(Enquiry enquiry) {
        System.out.println("\nEnquiry ID: " + enquiry.getEnquiryId());
        System.out.println("From: " + enquiry.getApplicant().getName());
        System.out.println("Project: " + (enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "General"));
        System.out.println("Message: " + enquiry.getEnquiryContent());
        System.out.println("Status: " + (enquiry.isResponded() ? "Responded" : "Pending"));

        if (enquiry.isResponded()) {
            System.out.println("Response: " + enquiry.getResponse());
            System.out.println("Responded By: " + enquiry.getRespondedBy().getName());
            System.out.println("Response Date: " + enquiry.getResponseDate());
        }

        System.out.println("----------------------------------------");
    }

    /**
     * Provides an interface for responding to enquiries.
     *
     * @param manager The HDB Manager responding to enquiries
     */
    private void respondToEnquiry(HDBManager manager) {
        showMessage("\n=== RESPOND TO ENQUIRY ===");

        // Get pending enquiries for manager's projects
        List<Enquiry> pendingEnquiries = new ArrayList<>();
        List<Project> projects = projectController.getAllProjects();
        List<Project> managerProjects = manager.ownProjects(projects, manager);

        // Filter pending enquiries
        for (Enquiry enquiry : enquiryController.getAllEnquiries()) {
            if (!enquiry.isResponded() &&
                    (enquiry.getProject() == null || managerProjects.contains(enquiry.getProject()))) {
                pendingEnquiries.add(enquiry);
            }
        }

        // Check if there are any pending enquiries
        if (pendingEnquiries.isEmpty()) {
            showMessage("No pending enquiries for your projects.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display pending enquiries
        showMessage("Pending Enquiries:");
        for (int i = 0; i < pendingEnquiries.size(); i++) {
            Enquiry enquiry = pendingEnquiries.get(i);
            System.out.println((i+1) + ". From: " + enquiry.getSender().getName() +
                    " - Subject: " + enquiry.getSubject() +
                    " - Project: " + (enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "N/A"));
        }

        // Get user selection
        int selection = getIntegerInput("Select an enquiry to respond to (0 to cancel): ", 0, pendingEnquiries.size());

        // Validate selection
        if (selection == 0 || selection > pendingEnquiries.size()) {
            showMessage("Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Get selected enquiry
        Enquiry selectedEnquiry = pendingEnquiries.get(selection - 1);

        // Display enquiry details
        showMessage("\nEnquiry Details:");
        displayEnquiryDetails(selectedEnquiry);

        // Get response
        String response = getInput("Enter your response: ");

        // Validate and process response
        if (!response.trim().isEmpty()) {
            // Attempt to respond to the enquiry
            if (enquiryController.respondToEnquiry(selectedEnquiry, response, manager)) {
                // Update enquiry details
                selectedEnquiry.setResponse(response);
                selectedEnquiry.setRespondedBy(manager);
                selectedEnquiry.setResponseDate(new Date());
                selectedEnquiry.setResponded(true);

                showMessage("Response sent successfully!");
            } else {
                showMessage("Failed to send response.");
            }
        } else {
            showMessage("Response cannot be empty. Operation cancelled.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Provides an interface for changing the manager's password.
     *
     * @param manager The HDB Manager changing their password
     */
    private void changePassword(HDBManager manager) {
        showMessage("\n=== CHANGE PASSWORD ===");

        String currentPassword = getInput("Enter current password: ");

        // Verify current password
        if (!manager.getPassword().equals(currentPassword)) {
            showMessage("Incorrect password. Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        String newPassword = getInput("Enter new password: ");

        // Validate new password
        if (!authController.validatePassword(newPassword)) {
            showMessage("Password must be at least 8 characters. Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        String confirmPassword = getInput("Confirm new password: ");

        if (!newPassword.equals(confirmPassword)) {
            showMessage("Passwords do not match. Operation cancelled.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Update password
        manager.setPassword(newPassword);
        showMessage("Password changed successfully!");

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    // Helper method for getting integer input with validation
    /**
     * Validates and gets integer input within a specified range.
     *
     * @param prompt The message to display when requesting input
     * @param min The minimum acceptable value
     * @param max The maximum acceptable value
     * @return A valid integer within the specified range
     */
    private int getIntegerInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid number.");
                continue;
            }

            try {
                int value = Integer.parseInt(input);

                if (value < min || value > max) {
                    System.out.println("Please enter a number between " + min + " and " + max);
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    // Helper method for getting integer input with only minimum bound
    /**
     * Validates and gets integer input with only a minimum bound.
     *
     * @param prompt The message to display when requesting input
     * @param min The minimum acceptable value
     * @return A valid integer greater than or equal to the minimum
     */
    private int getIntegerInput(String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please enter a valid number.");
                continue;
            }

            try {
                int value = Integer.parseInt(input);

                if (value < min) {
                    System.out.println("Please enter a number of at least " + min);
                    continue;
                }

                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Displays the profile of a hotel database manager and provides navigation back to the manager menu.
     *
     * This method performs two main actions:
     * 1. Calls the manager's polymorphic displayProfile() method to show the manager's details
     * 2. Waits for user input before returning to the main manager menu
     *
     * @param manager The HDBManager whose profile is to be displayed
     */
    private void displayManagerProfile(HDBManager manager) {
        // Call the polymorphic displayProfile method
        System.out.println(manager.displayProfile());

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();

        // Return to the manager menu
        displayManagerMenu(manager);
    }

    /**
     * Displays a message to the console.
     *
     * @param message The message to display
     */
    public void showMessage(String message) {
        System.out.println(message);
    }

    /**
     * Prompts the user for input and returns the entered string.
     *
     * @param prompt The message to display when requesting input
     * @return The user's input
     */
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

}