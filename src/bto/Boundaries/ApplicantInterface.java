package bto.Boundaries;

import java.util.Scanner;
import java.util.Set;

import bto.Controllers.*;
import bto.Entities.*;
import bto.Interfaces.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import bto.EntitiesProjectRelated.*;
import bto.Enums.*;

public class ApplicantInterface {

    /** Scanner for reading user input */
    private final Scanner scanner;

    /** Controller for authentication operations */
    private final IAuthController authController;

    /** Controller for project-related operations */
    private final IProjectController projectController;

    /** Controller for application-related operations */
    private final IApplicationController applicationController;

    /** Controller for enquiry-related operations */
    private final IEnquiryController enquiryController;

    /** Controller for report generation */
    private final IReportController reportController;

    /** Controller for registration-related operations */
    private final IRegistrationController registrationController;

    /** Controller for withdrawal-related operations */
    private final IWithdrawalController withdrawalController;

    /** Controller for booking-related operations */
    private final IBookingController bookingController;

    /** Generator for receipts */
    private final IReceiptGenerator receiptGenerator;

    /** Reference to the main user interface */
    private final UserInterface userInterface;

    /** The currently logged-in applicant */
    private Applicant currentApplicant; // This shouldn't be final as it can change

    /**
     * Constructs an ApplicantInterface with references to all required controllers.
     *
     * @param scanner Scanner for reading user input
     * @param authController Controller for authentication operations
     * @param projectController Controller for project-related operations
     * @param applicationController Controller for application-related operations
     * @param enquiryController Controller for enquiry-related operations
     * @param reportController Controller for report generation
     * @param registrationController Controller for registration-related operations
     * @param withdrawalController Controller for withdrawal-related operations
     * @param bookingController Controller for booking-related operations
     * @param receiptGenerator Generator for receipts
     * @param userInterface Reference to the main user interface
     */
    public ApplicantInterface(Scanner scanner, IAuthController authController, IProjectController projectController,
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
     * Sets the current logged-in applicant.
     *
     * @param applicant The applicant who is currently logged in
     */
    public void setCurrentApplicant(Applicant applicant) {
        this.currentApplicant = applicant;
    }

    /**
     * Gets the current logged-in applicant.
     *
     * @return The currently logged-in applicant
     */
    public Applicant getCurrentApplicant() {
        return this.currentApplicant;
    }

    /**
     * Helper method for getting validated integer input between a minimum and maximum value.
     *
     * @param prompt The message to display to the user
     * @param min The minimum acceptable value
     * @param max The maximum acceptable value
     * @return The validated integer input from the user
     */
    private int getValidIntegerInput(String prompt, int min, int max) {
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

    /**
     * Helper method for getting validated integer input with only a minimum bound.
     *
     * @param prompt The message to display to the user
     * @param min The minimum acceptable value
     * @return The validated integer input from the user
     */
    private int getValidIntegerInput(String prompt, int min) {
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
     * Displays the main menu for applicants and handles user selections.
     *
     * @param applicant The applicant who is currently logged in
     */
    public void displayApplicantMenu(Applicant applicant) {
        setCurrentApplicant(applicant);

        while (true) {
            try {

                System.out.println("\n======== HDB APPLICANT PORTAL ========");
                System.out.println("Welcome, " + applicant.getName() + " (" + applicant.getNric() + ")");
                System.out.println("========================================");

                System.out.println("\n=== APPLICANT MENU ===");
                System.out.println("1. View Projects");
                System.out.println("2. View Application Status");
                System.out.println("3. Submit Enquiry");
                System.out.println("4. View Enquiries");
                System.out.println("5. Edit Enquiry");
                System.out.println("6. Delete Enquiry");
                System.out.println("7. Request Withdrawal");
                System.out.println("8. Change Password");
                System.out.println("9. Display Profile");
                System.out.println("0. Logout");

                int choice = getValidIntegerInput("Enter your choice: ", 0, 9);

                switch(choice) {
                    case 1:
                    	List<Project> visibleProjects = displayProjects();
                        if (visibleProjects != null && !visibleProjects.isEmpty()) {
                            Project selectedProject = displayProjectDetails(visibleProjects);
                            if (selectedProject != null) {
                                applicationConfirmation(selectedProject);
                            }
                        }
                        break;
                    case 2:
                        viewApplicationStatus();
                        break;
                    case 3:
                        submitEnquiry();
                        break;
                    case 4:
                        viewEnquiries();
                        break;
                    case 5:
                        editEnquiryFromMenu();
                        break;
                    case 6:
                        deleteEnquiryFromMenu();
                        break;
                    case 7:
                        withdrawalRequest();
                        break;
                    case 8:
                        changePasswordInterface();
                        break;
                    case 9:
                        displayProfileInterface();
                        break;
                    case 0:
                        userInterface.displayLoginMenu();
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Displays a list of all visible projects available to the applicant.
     * Provides filtering and sorting options.
     *
     * @return A list of visible projects after any applied filters/sorting
     */
    private List<Project> displayProjects() {
        System.out.println("\n=== AVAILABLE PROJECTS ===");

        // Get visible projects using the existing method in ProjectController
        List<Project> visibleProjects = projectController.getVisibleProjectsForApplicant(currentApplicant);

        if (visibleProjects.isEmpty()) {
            System.out.println("No available projects at the moment.");
            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return visibleProjects;
        }

        // Ask if the user wants to apply filters
        System.out.println("\nDo you want to filter or sort the projects?");
        System.out.println("1. Yes");
        System.out.println("2. No (Show all projects)");
        
        int filterChoice = getValidIntegerInput("Enter your choice: ", 1, 2);
        
        if (filterChoice == 1) {
            // Apply filters
            visibleProjects = applyProjectFilters(visibleProjects);
        }

        // Display the filtered/sorted projects
        displayProjectsList(visibleProjects);

        return visibleProjects;
    }

    /**
     * Handles the filtering and sorting options for projects.
     *
     * @param projects List of projects to filter or sort
     * @return Filtered or sorted list of projects
     */
    private List<Project> applyProjectFilters(List<Project> projects) {
        System.out.println("\n=== FILTER OPTIONS ===");
        System.out.println("1. Filter by Neighborhood/Location");
        System.out.println("2. Filter by Flat Type");
        System.out.println("3. Sort Alphabetically");
        System.out.println("4. Return to Projects List");
        
        int option = getValidIntegerInput("Select a filter option: ", 1, 4);
        
        switch (option) {
            case 1:
                // Filter by neighborhood
                return filterByNeighborhood(projects);
            case 2:
                // Filter by flat type
                return filterByFlatType(projects);
            case 3:
                // Sort alphabetically
                return sortProjectsAlphabetically(projects);
            case 4:
                // Return without filtering
                return projects;
            default:
                return projects;
        }
    }

    /**
     * Filters projects by neighborhood.
     *
     * @param projects List of projects to filter
     * @return Filtered list of projects
     */
    private List<Project> filterByNeighborhood(List<Project> projects) {
        // Extract unique neighborhoods from the projects
        Set<String> neighborhoods = new HashSet<>();
        for (Project project : projects) {
            neighborhoods.add(project.getNeighborhood());
        }
        
        // Convert to a sorted list for display
        List<String> neighborhoodList = new ArrayList<>(neighborhoods);
        Collections.sort(neighborhoodList);
        
        System.out.println("\n=== FILTER BY NEIGHBORHOOD ===");
        for (int i = 0; i < neighborhoodList.size(); i++) {
            System.out.println((i+1) + ". " + neighborhoodList.get(i));
        }
        System.out.println((neighborhoodList.size() + 1) + ". Show All Neighborhoods");
        
        int choice = getValidIntegerInput("Select a neighborhood: ", 1, neighborhoodList.size() + 1);
        
        if (choice <= neighborhoodList.size()) {
            String selectedNeighborhood = neighborhoodList.get(choice - 1);
            
            // Use ProjectController to filter the projects
            List<Project> filteredProjects = projectController.filterProjectsByNeighborhood(projects, selectedNeighborhood);
            
            System.out.println("\nShowing projects in " + selectedNeighborhood);
            return filteredProjects;
        } else {
            // Show all neighborhoods
            return projects;
        }
    }

    /**
     * Filters projects by flat type.
     *
     * @param projects List of projects to filter
     * @return Filtered list of projects
     */
    private List<Project> filterByFlatType(List<Project> projects) {
        System.out.println("\n=== FILTER BY FLAT TYPE ===");
        System.out.println("1. 2-Room Flats");
        System.out.println("2. 3-Room Flats");
        System.out.println("3. Show All Flat Types");
        
        int choice = getValidIntegerInput("Select a flat type: ", 1, 3);
        
        if (choice == 1 || choice == 2) {
            FlatType selectedType = (choice == 1) ? FlatType.TWO_ROOM : FlatType.THREE_ROOM;
            
            // Use ProjectController to filter by flat type
            List<Project> filteredProjects = projectController.filterProjectsByFlatType(projects, selectedType, currentApplicant);
            
            System.out.println("\nShowing projects with available " + selectedType + " flats");
            return filteredProjects;
        } else {
            // Show all flat types
            return projects;
        }
    }

    /**
     * Sorts projects alphabetically.
     *
     * @param projects List of projects to sort
     * @return Sorted list of projects
     */
    private List<Project> sortProjectsAlphabetically(List<Project> projects) {
        System.out.println("\n=== SORT ALPHABETICALLY ===");
        System.out.println("1. Sort by Project Name (A-Z)");
        System.out.println("2. Sort by Project Name (Z-A)");
        System.out.println("3. Sort by Neighborhood (A-Z)");
        System.out.println("4. Sort by Neighborhood (Z-A)");
        System.out.println("5. No Sorting");
        
        int choice = getValidIntegerInput("Select a sorting option: ", 1, 5);
        
        switch (choice) {
            case 1:
                // Use ProjectController to sort by name (A-Z)
                System.out.println("\nProjects sorted by name (A-Z)");
                return projectController.sortProjectsByName(projects, true);
            case 2:
                // Sort by project name (Z-A)
                System.out.println("\nProjects sorted by name (Z-A)");
                return projectController.sortProjectsByName(projects, false);
            case 3:
                // Sort by neighborhood (A-Z)
                System.out.println("\nProjects sorted by neighborhood (A-Z)");
                return projectController.sortProjectsByNeighborhood(projects, true);
            case 4:
                // Sort by neighborhood (Z-A)
                System.out.println("\nProjects sorted by neighborhood (Z-A)");
                return projectController.sortProjectsByNeighborhood(projects, false);
            case 5:
            default:
                // No sorting
                return projects;
        }
    }

    /**
     * Displays a formatted list of projects.
     *
     * @param projects List of projects to display
     */
    private void displayProjectsList(List<Project> projects) {
        if (projects.isEmpty()) {
            System.out.println("No projects match your filter criteria.");
        } else {
            System.out.println("\nAvailable Projects:");
            System.out.println("ID      Project Name             Neighborhood           Application Period");
            System.out.println("--------------------------------------------------------------------------------");
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (int i = 0; i < projects.size(); i++) {
                Project project = projects.get(i);
                String openDate = dateFormat.format(project.getApplicationOpenDate());
                String closeDate = dateFormat.format(project.getApplicationCloseDate());

                System.out.printf("%-7d %-25s %-20s %-12s - %-12s%n",
                        i + 1,
                        project.getProjectName(),
                        project.getNeighborhood(),
                        openDate,
                        closeDate
                );
            }
        }
    }

    /**
     * Displays detailed information about a selected project.
     *
     * @param visibleProjects List of projects to choose from
     * @return The selected project, or null if no project was selected
     */
    private Project displayProjectDetails(List<Project> visibleProjects) {
        if (visibleProjects.isEmpty()) {
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return null;
        }

        int projectChoice = getValidIntegerInput("Enter the project number to view details (or 0 to go back): ", 0, visibleProjects.size());

        if (projectChoice == 0) {
            // Return to previous menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            displayApplicantMenu(currentApplicant);
            return null;
        }

        // Get the selected project (subtract 1 because list is 0-indexed)
        Project selectedProject = visibleProjects.get(projectChoice - 1);

        // Display detailed project information
        System.out.println("\n=== PROJECT DETAILS ===");
        System.out.println("Project Name: " + selectedProject.getProjectName());
        System.out.println("Neighborhood: " + selectedProject.getNeighborhood());

        // Format dates for display
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Application Open Date: " + dateFormat.format(selectedProject.getApplicationOpenDate()));
        System.out.println("Application Close Date: " + dateFormat.format(selectedProject.getApplicationCloseDate()));

        // Manager in Charge
        System.out.println("Manager in Charge: " + selectedProject.getManagerInCharge().getName());

        // Display Flat Types and Availability
        System.out.println("\nFlat Types and Availability:");
        System.out.println("Flat Type                Total Units           Available Units");
        System.out.println("----------------------------------------------------------------------");
        Map<FlatType, Integer> flatTypeUnits = selectedProject.getFlatTypeUnits();
        ProjectFlats projectFlats = selectedProject.getProjectFlats();

        for (FlatType flatType : flatTypeUnits.keySet()) {
            int totalUnits = flatTypeUnits.get(flatType);
            int availableUnits = projectFlats.getAvailableFlatCount(flatType);

            System.out.printf("%-25s %10d %20d%n",
                    flatType.name(),
                    totalUnits,
                    availableUnits
            );
        }

        // Check Eligibility for the Current Applicant
        List<FlatType> eligibleTypes = selectedProject.getEligibleFlatTypes(currentApplicant);
        System.out.println("\nYour Eligible Flat Types:");
        if (eligibleTypes.isEmpty()) {
            System.out.println("  No flat types currently available for your profile.");
        } else {
            for (FlatType type : eligibleTypes) {
                System.out.println("  - " + type.name());
            }
        }
        return selectedProject;
    }

    /**
     * Handles the confirmation process for applying to a project.
     *
     * @param selectedProject The project to potentially apply for
     */
    private void applicationConfirmation(Project selectedProject) {
        if (selectedProject == null) {
            return;
        }

        System.out.println("\nOptions:");
        System.out.println("1) Apply");
        System.out.println("0) Go Back");

        int choice = getValidIntegerInput("Enter your choice: ", 0, 1);

        switch (choice) {
            case 1:
                // Attempt to submit application
                Boolean applicationResult = applicationController.submitApplication(currentApplicant, selectedProject);

                if (applicationResult) {
                    System.out.println("\n=== APPLICATION SUBMITTED ===");
                    System.out.println("Your application is pending.");
                    System.out.println("Project: " + selectedProject.getProjectName());
                    System.out.println("Status: Pending");

                    // Prompt to continue
                    System.out.println("\nPress Enter to continue...");
                    scanner.nextLine();

                    // Return to projects list
                    displayProjects();
                } else {
                    System.out.println("Failed to submit application. You may already have an active application.");

                    // Wait for user input before returning to menu
                    System.out.println("\nPress Enter to return to the main menu...");
                    scanner.nextLine();

                    displayProjects();
                }
                break;

            case 0:
                // Go back to projects list
                // Wait for user input before returning to menu
                System.out.println("\nPress Enter to return to the main menu...");
                scanner.nextLine();

                displayProjects();
                break;
        }
    }

    /**
     * Displays the current status of the applicant's application and any withdrawal history.
     */
    private void viewApplicationStatus() {
        System.out.println("\n=== APPLICATION STATUS ===");

        // Get the current applicant's application
        ProjectApplication application = applicationController.getApplicationByApplicantNRIC(currentApplicant.getNric());

        // Get the applicant's withdrawal history
        List<Withdrawal> withdrawals = new ArrayList<>();
        for (Withdrawal withdrawal : withdrawalController.getAllWithdrawals()) {
            if (withdrawal.getApplicant().getNric().equals(currentApplicant.getNric())) {
                withdrawals.add(withdrawal);
            }
        }

        // Check if the applicant has an active application
        if (application == null) {
            System.out.println("You currently have no active applications.");

            // If there are approved withdrawals, show them
            if (!withdrawals.isEmpty()) {
                System.out.println("\n=== WITHDRAWAL HISTORY ===");
                for (Withdrawal withdrawal : withdrawals) {
                    ProjectApplication withdrawnApp = withdrawal.getApplication();
                    if (withdrawnApp != null) {
                        System.out.println("Project: " + withdrawnApp.getProject().getProjectName());
                        System.out.println("Withdrawal Status: " + withdrawal.getStatus());
                        System.out.println("Request Date: " + withdrawal.getRequestDate());

                        // Show original application status before withdrawal
                        System.out.println("Original Application Status: " + withdrawnApp.getStatus());

                        // Show selected flat type if any
                        if (withdrawnApp.getSelectedFlatType() != null) {
                            System.out.println("Selected Flat Type: " + withdrawnApp.getSelectedFlatType());
                        } else {
                            System.out.println("Selected Flat Type: Not chosen");
                        }
                        System.out.println("---------------------");
                    }
                }
            }

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
        } else {
            // Display application details
            Project project = application.getProject();

            System.out.println("Project Name: " + project.getProjectName());
            System.out.println("Neighborhood: " + project.getNeighborhood());
            System.out.println("Status: " + application.getStatus().toString());

            // If a flat type has been selected, show it. Otherwise, show "Not yet chosen"
            FlatType selectedFlatType = application.getSelectedFlatType();
            if (selectedFlatType != null) {
                System.out.println("Selected Flat Type: " + selectedFlatType.toString());
            } else {
                System.out.println("Selected Flat Type: Not yet chosen");
            }

            // If there's a withdrawal request, show its status
            String withdrawalStatus = application.getWithdrawalStatus();
            if (withdrawalStatus != null) {
                System.out.println("Withdrawal Status: " + withdrawalStatus);
            }

            // Flag to track if project visibility is off
            boolean projectVisibilityOff = false;
            
            // Check if project visibility has been turned off
            if (!project.isVisible()) {
                System.out.println("\nNOTICE: This project's visibility has been turned off by the management.");
                System.out.println("It is recommended that you withdraw your application from this project to apply for a new one.");
                projectVisibilityOff = true;
            }

            // Show options based on application status
            System.out.println("\nOptions:");

            int maxChoice = 0;

            if (application.getStatus() == ApplicationStatus.SUCCESSFUL && selectedFlatType == null) {
                // For successful applications with no flat type selected yet, show option to select flat type
                System.out.println("1. Select Flat Type");
                maxChoice = 1;
            } else if (application.getStatus() == ApplicationStatus.BOOKED) {
                // For booked applications, show option to view receipt
                System.out.println("1. View Booking Receipt");
                maxChoice = 1;
            }

            // Show withdrawal history if exists (for pending or rejected withdrawals)
            if (!withdrawals.isEmpty()) {
                System.out.println("2. View Withdrawal History");
                maxChoice = 2;
            }
            
            // Add withdrawal option if project visibility is off and no withdrawal request exists
            if (projectVisibilityOff && withdrawalStatus == null) {
                System.out.println("3. Request Withdrawal");
                maxChoice = 3;
            }

            System.out.println("0. Back to Main Menu");

            int choice = getValidIntegerInput("\nEnter your choice: ", 0, maxChoice);

            if (choice == 1) {
                if (application.getStatus() == ApplicationStatus.SUCCESSFUL && selectedFlatType == null) {
                    // Select flat type
                    selectFlatType(application, project);
                } else if (application.getStatus() == ApplicationStatus.BOOKED) {
                    // View booking receipt
                    viewBookingReceipt(currentApplicant);
                }
            } else if (choice == 2 && !withdrawals.isEmpty()) {
                // View withdrawal history
                viewWithdrawalHistory(withdrawals);
            } else if (choice == 3 && projectVisibilityOff && withdrawalStatus == null) {
                // Request withdrawal directly
                withdrawalRequest();
            } else if (choice == 0) {
                // Return to main menu
                System.out.println("\nPress Enter to return to the main menu...");
                scanner.nextLine();

                displayApplicantMenu(currentApplicant);
            }
        }
    }

    /**
     * Displays the withdrawal history for an applicant.
     *
     * @param withdrawals List of withdrawals to display
     */
    private void viewWithdrawalHistory(List<Withdrawal> withdrawals) {
        System.out.println("\n=== WITHDRAWAL HISTORY ===");

        for (int i = 0; i < withdrawals.size(); i++) {
            Withdrawal withdrawal = withdrawals.get(i);
            ProjectApplication withdrawnApp = withdrawal.getApplication();

            System.out.println((i+1) + ". Project: " + withdrawnApp.getProject().getProjectName());
            System.out.println("   Withdrawal Status: " + withdrawal.getStatus());
            System.out.println("   Request Date: " + withdrawal.getRequestDate());
            System.out.println("   Original Application Status: " + withdrawnApp.getStatus());

            // Show selected flat type if any
            if (withdrawnApp.getSelectedFlatType() != null) {
                System.out.println("   Selected Flat Type: " + withdrawnApp.getSelectedFlatType());
            } else {
                System.out.println("   Selected Flat Type: Not chosen");
            }
            System.out.println("---------------------");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the application status...");
        scanner.nextLine();
    }

    /**
     * Handles the selection of a flat type for a successful application.
     *
     * @param application The application to update with the selected flat type
     * @param project The project associated with the application
     */
    private void selectFlatType(ProjectApplication application, Project project) {
        System.out.println("\n=== SELECT FLAT TYPE ===");

        // Get eligible flat types for the applicant
        List<FlatType> eligibleTypes = project.getEligibleFlatTypes(currentApplicant);

        if (eligibleTypes.isEmpty()) {
            System.out.println("You are not eligible for any flat types in this project.");
        } else {
            System.out.println("Available and Eligible Flat Types:");
            System.out.println("ID      Flat Type                Available Units");
            System.out.println("-------------------------------------------------------");
            ProjectFlats projectFlats = project.getProjectFlats();
            List<FlatType> availableTypes = new ArrayList<>();

            for (int i = 0; i < eligibleTypes.size(); i++) {
                FlatType flatType = eligibleTypes.get(i);
                int availableUnits = projectFlats.getAvailableFlatCount(flatType);

                if (availableUnits > 0) {
                    availableTypes.add(flatType);
                    System.out.printf("%-7d %-25s %20d%n",
                            availableTypes.size(),
                            flatType.toString(),
                            availableUnits
                    );
                }
            }

            if (availableTypes.isEmpty()) {
                System.out.println("No eligible flat types currently have available units.");
            } else {
                System.out.println("\nOptions:");
                System.out.println("1. Select a Flat Type");
                System.out.println("0. Cancel");

                int choice = getValidIntegerInput("\nEnter your choice: ", 0, 1);

                if (choice == 1) {
                    int flatTypeId = getValidIntegerInput("Enter the ID of the flat type you want to select: ", 1, availableTypes.size());
                    FlatType selectedType = availableTypes.get(flatTypeId - 1);

                    // Update the application with the selected flat type
                    application.setSelectedFlatType(selectedType);

                    System.out.println("\nFlat type " + selectedType.toString() + " has been selected.");
                    System.out.println("Your selection has been submitted and is pending officer approval.");
                    System.out.println("You will be notified once your booking is confirmed.");
                }
            }
        }

        // Wait for user input before returning
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
        displayApplicantMenu(currentApplicant);
    }

    /**
     * Provides interface for changing the applicant's password.
     */
    private void changePasswordInterface() {
        System.out.println("\n=== CHANGE PASSWORD ===");

        // Ask for old password
        System.out.print("Enter your current password: ");
        String oldPassword = scanner.nextLine();

        // Validate old password
        if (!currentApplicant.getPassword().equals(oldPassword)) {
            System.out.println("Incorrect current password. Password change canceled.");
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            displayApplicantMenu(currentApplicant);
            return;
        }

        // Ask for new password
        String newPassword;
        while (true) {
            System.out.print("Enter new password: ");
            newPassword = scanner.nextLine();

            // Validate new password using the AuthController
            if (authController.validatePassword(newPassword)) {
                break;  // If valid, break out of the loop
            } else {
                System.out.println("Password must be at least 8 characters. Please try again.");
            }
        }

        // Confirm new password
        System.out.print("Confirm new password: ");
        String confirmPassword = scanner.nextLine();

        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords do not match. Password change canceled.");
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            displayApplicantMenu(currentApplicant);
            return;
        }

        // Change the password
        boolean success = currentApplicant.changePassword(oldPassword, newPassword);

        if (success) {
            System.out.println("Password changed successfully. Please log in again.");
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

            // Return to login menu
            userInterface.displayLoginMenu();
        } else {
            System.out.println("Failed to change password. Please try again later.");
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            displayApplicantMenu(currentApplicant);
        }
    }

    /**
     * Displays the profile information for the current applicant.
     */
    private void displayProfileInterface() {
        // Call the polymorphic displayProfile method
        System.out.println(currentApplicant.displayProfile());

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();

        // Return to the applicant menu
        displayApplicantMenu(currentApplicant);
    }

    /**
     * Displays the booking receipt for an applicant.
     *
     * @param applicant The applicant whose receipt to display
     */
    private void viewBookingReceipt(Applicant applicant) {

        // Check if a receipt exists in the BookingController
        if (bookingController.hasReceipt(applicant.getNric())) {
            // Retrieve the receipt from BookingController
            Receipt receipt = bookingController.getReceiptForApplicant(applicant.getNric());

            // Display the receipt content
            System.out.println(receipt.getContent());
        } else {
            System.out.println("No receipt available. Please contact an HDB Officer to generate your receipt.");
        }

        System.out.println("\nNote: To save this receipt, you can copy and paste the text above.");

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();

        // Return to the applicant menu
        displayApplicantMenu(applicant);
    }

    /**
     * Handles the submission of a new enquiry.
     */
    private void submitEnquiry() {
        System.out.println("\n======== SUBMIT ENQUIRY ========");

        // Ask if enquiry is about a specific project
        System.out.println("Is this enquiry about a specific project?");
        System.out.println("1. Yes");
        System.out.println("2. No (General Enquiry)");

        System.out.print("\nEnter your choice: ");
        int projectChoice = getValidIntegerInput("Enter your choice: ", 1, 2);

        Project selectedProject = null;

        if (projectChoice == 1) {
            // Get visible projects
            List<Project> visibleProjects = projectController.getVisibleProjectsForApplicant(currentApplicant);

            if (visibleProjects.isEmpty()) {
                System.out.println("No projects available. Submitting as a general enquiry.");
            } else {
                System.out.println("\nSelect Project:");
                for (int i = 0; i < visibleProjects.size(); i++) {
                    System.out.println((i+1) + ". " + visibleProjects.get(i).getProjectName());
                }

                int projectNum = getValidIntegerInput("\nEnter project number (or 0 for general enquiry): ", 0, visibleProjects.size() );

                if (projectNum > 0 && projectNum <= visibleProjects.size()) {
                    selectedProject = visibleProjects.get(projectNum - 1);
                    System.out.println("Selected project: " + selectedProject.getProjectName());
                } else {
                    System.out.println("No project selected. Submitting as a general enquiry.");
                }
            }
        }

        // Get enquiry content
        System.out.println("\nEnter your enquiry:");
        String enquiryContent = scanner.nextLine();

        if (enquiryContent.trim().isEmpty()) {
            System.out.println("Enquiry content cannot be empty. Operation cancelled.");
        } else {
            // Create the enquiry
            Enquiry newEnquiry = enquiryController.createEnquiry(currentApplicant, selectedProject, enquiryContent);

            if (newEnquiry != null) {
                System.out.println("\nEnquiry submitted successfully!");
                System.out.println("You will be notified when a response is available.");
            } else {
                System.out.println("\nFailed to submit enquiry. Please try again later.");
            }
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }

    /**
     * Displays all enquiries submitted by the current applicant and provides options
     * to view, edit, or delete them.
     */
    private void viewEnquiries() {
        System.out.println("\n======== MY ENQUIRIES ========");

        // Get the applicant's enquiries
        List<Enquiry> myEnquiries = enquiryController.getEnquiriesByApplicant(currentApplicant);

        if (myEnquiries.isEmpty()) {
            System.out.println("You have not submitted any enquiries.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
        } else {
            System.out.println("Your Enquiries:");
            System.out.println("ID\tProject\t\tSubmission Date\t\tStatus\t\tEnquiry Content");
            System.out.println("------------------------------------------------------------------------------------------");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < myEnquiries.size(); i++) {
                Enquiry enquiry = myEnquiries.get(i);
                String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "General";
                String status = enquiry.isResponded() ? "Responded" : "Pending";

                System.out.printf("%-3d %-20s %-12s %-12s %s%n",
                        i + 1,
                        projectName,
                        dateFormat.format(enquiry.getSubmissionDate()),
                        status,
                        enquiry.getEnquiryContent().length() > 30 ?
                                enquiry.getEnquiryContent().substring(0, 27) + "..." :
                                enquiry.getEnquiryContent()
                );
            }

            System.out.println("\nOptions:");
            System.out.println("1. View Enquiry Details");
            System.out.println("2. Edit Enquiry");
            System.out.println("3. Delete Enquiry");
            System.out.println("0. Back to Main Menu");

            int choice = getValidIntegerInput("\nEnter your choice: ", 0, 3);

            switch (choice) {
                case 1:
                    System.out.print("Enter enquiry ID to view details: ");
                    int viewId = Integer.parseInt(scanner.nextLine());

                    if (viewId > 0 && viewId <= myEnquiries.size()) {
                        viewEnquiryDetails(myEnquiries.get(viewId - 1));
                    } else {
                        System.out.println("Invalid enquiry ID.");
                    }
                    viewEnquiries(); // Return to list
                    break;

                case 2:
                    System.out.print("Enter enquiry ID to edit: ");
                    int editId = Integer.parseInt(scanner.nextLine());

                    if (editId > 0 && editId <= myEnquiries.size()) {
                        editEnquiry(myEnquiries.get(editId - 1));
                    } else {
                        System.out.println("Invalid enquiry ID.");
                        viewEnquiries();
                    }
                    break;

                case 3:
                    System.out.print("Enter enquiry ID to delete: ");
                    int deleteId = Integer.parseInt(scanner.nextLine());

                    if (deleteId > 0 && deleteId <= myEnquiries.size()) {
                        deleteEnquiry(myEnquiries.get(deleteId - 1));
                    } else {
                        System.out.println("Invalid enquiry ID.");
                        viewEnquiries();
                    }
                    break;

                case 0:
                    // Wait for user input before returning to menu
                    System.out.println("\nPress Enter to return to the main menu...");
                    scanner.nextLine();
                    break;

                default:
                    System.out.println("Invalid choice.");
                    viewEnquiries();
                    break;
            }
        }
    }

    /**
     * Displays detailed information for a specific enquiry.
     *
     * @param enquiry The enquiry to view details for
     */
    private void viewEnquiryDetails(Enquiry enquiry) {
        System.out.println("\n======== ENQUIRY DETAILS ========");

        String projectName = enquiry.getProject() != null ?
                enquiry.getProject().getProjectName() : "General Enquiry";
        System.out.println("Project: " + projectName);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Submission Date: " + dateFormat.format(enquiry.getSubmissionDate()));

        System.out.println("\nEnquiry Content:");
        System.out.println(enquiry.getEnquiryContent());

        if (enquiry.getResponse() != null && !enquiry.getResponse().isEmpty()) {
            System.out.println("\nResponse:");
            System.out.println(enquiry.getResponse());
        } else {
            System.out.println("\nStatus: Pending Response");
        }

        // Wait for user input before returning
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    /**
     * Provides an interface for editing an existing enquiry.
     *
     * @param enquiry The enquiry to be edited
     */
    private void editEnquiry(Enquiry enquiry) {
        System.out.println("\n======== EDIT ENQUIRY ========");

        // Check if enquiry already has a response
        if (enquiry.isResponded()) {
            System.out.println("You cannot edit an enquiry that has already been responded to.");

            // Wait for user input before returning
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();

            // Return to view enquiries
            viewEnquiries();
            return;
        }

        // Display current enquiry
        System.out.println("Current Enquiry Content:");
        System.out.println(enquiry.getEnquiryContent());

        // Get new content
        System.out.println("\nEnter new enquiry content (or leave empty to cancel):");
        String newContent = scanner.nextLine();

        if (newContent.trim().isEmpty()) {
            System.out.println("Edit cancelled.");
        } else {
            // Update the enquiry
            boolean success = enquiryController.editEnquiry(enquiry, newContent);

            if (success) {
                System.out.println("\nEnquiry updated successfully!");
            } else {
                System.out.println("\nFailed to update enquiry. Please try again later.");
            }
        }

        // Wait for user input before returning
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();

        // Return to view enquiries
        viewEnquiries();
    }

    /**
     * Provides an interface for deleting an existing enquiry.
     *
     * @param enquiry The enquiry to be deleted
     */
    private void deleteEnquiry(Enquiry enquiry) {
        System.out.println("\n======== DELETE ENQUIRY ========");

        // Display enquiry to be deleted
        System.out.println("Enquiry to be deleted:");
        System.out.println("Project: " + (enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "General"));
        System.out.println("Submission Date: " + new SimpleDateFormat("dd/MM/yyyy").format(enquiry.getSubmissionDate()));
        System.out.println("Content: " + enquiry.getEnquiryContent());

        // Confirmation
        System.out.print("\nAre you sure you want to delete this enquiry? (Y/N): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("Y")) {
            // Delete the enquiry
            boolean success = enquiryController.deleteEnquiry(enquiry);

            if (success) {
                System.out.println("\nEnquiry deleted successfully!");
            } else {
                System.out.println("\nFailed to delete enquiry. Please try again later.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }

        // Wait for user input before returning
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();

        // Return to view enquiries
        viewEnquiries();
    }

    /**
     * Provides a menu-driven interface for editing enquiries.
     */
    private void editEnquiryFromMenu() {
        System.out.println("\n======== EDIT ENQUIRY ========");

        // Get the applicant's enquiries
        List<Enquiry> myEnquiries = enquiryController.getEnquiriesByApplicant(currentApplicant);

        if (myEnquiries.isEmpty()) {
            System.out.println("You have not submitted any enquiries.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
        } else {
            // Display the list of enquiries
            System.out.println("Your Enquiries:");
            System.out.println("ID\tProject\t\tSubmission Date\t\tStatus\t\tEnquiry Content");
            System.out.println("------------------------------------------------------------------------------------------");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            // Only show enquiries that can be edited (haven't been responded to)
            List<Enquiry> editableEnquiries = new ArrayList<>();

            for (Enquiry enquiry : myEnquiries) {
                if (enquiry.getResponse() == null) {
                    editableEnquiries.add(enquiry);

                    String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "General";

                    System.out.printf("%-3d %-20s %-12s %-12s %s%n",
                            editableEnquiries.size(),
                            projectName,
                            dateFormat.format(enquiry.getSubmissionDate()),
                            "Pending",
                            enquiry.getEnquiryContent().length() > 30 ?
                                    enquiry.getEnquiryContent().substring(0, 27) + "..." :
                                    enquiry.getEnquiryContent()
                    );
                }
            }

            if (editableEnquiries.isEmpty()) {
                System.out.println("You don't have any enquiries that can be edited. Only pending enquiries can be edited.");

                // Wait for user input before returning to menu
                System.out.println("\nPress Enter to return to the main menu...");
                scanner.nextLine();
            } else {
                try {
                    int editId = getValidIntegerInput("\nEnter enquiry ID to edit (or 0 to cancel): ", 0, editableEnquiries.size());

                    if (editId > 0 && editId <= editableEnquiries.size()) {
                        editEnquiry(editableEnquiries.get(editId - 1));
                    } else if (editId != 0) {
                        System.out.println("Invalid enquiry ID.");

                        // Wait for user input before returning to menu
                        System.out.println("\nPress Enter to return to the main menu...");
                        scanner.nextLine();
                    } else {
                        // User chose to cancel
                        // Wait for user input before returning to menu
                        System.out.println("\nPress Enter to return to the main menu...");
                        scanner.nextLine();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");

                    // Wait for user input before returning to menu
                    System.out.println("\nPress Enter to return to the main menu...");
                    scanner.nextLine();
                }
            }
        }
    }

    /**
     * Provides a menu-driven interface for deleting enquiries.
     */
    private void deleteEnquiryFromMenu() {
        System.out.println("\n======== DELETE ENQUIRY ========");

        // Get the applicant's enquiries
        List<Enquiry> myEnquiries = enquiryController.getEnquiriesByApplicant(currentApplicant);

        if (myEnquiries.isEmpty()) {
            System.out.println("You have not submitted any enquiries.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
        } else {
            // Display the list of enquiries
            System.out.println("Your Enquiries:");
            System.out.println("ID\tProject\t\tSubmission Date\t\tStatus\t\tEnquiry Content");
            System.out.println("------------------------------------------------------------------------------------------");

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

            for (int i = 0; i < myEnquiries.size(); i++) {
                Enquiry enquiry = myEnquiries.get(i);
                String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "General";
                String status = enquiry.getResponse() != null ? "Responded" : "Pending";

                System.out.printf("%-3d %-20s %-12s %-12s %s%n",
                        i + 1,
                        projectName,
                        dateFormat.format(enquiry.getSubmissionDate()),
                        status,
                        enquiry.getEnquiryContent().length() > 30 ?
                                enquiry.getEnquiryContent().substring(0, 27) + "..." :
                                enquiry.getEnquiryContent()
                );
            }

            int deleteId = getValidIntegerInput("\nEnter enquiry ID to delete (or 0 to cancel): ", 0, myEnquiries.size());

            if (deleteId > 0 && deleteId <= myEnquiries.size()) {
                deleteEnquiry(myEnquiries.get(deleteId - 1));
            } else if (deleteId != 0) {
                System.out.println("Invalid enquiry ID.");

                // Wait for user input before returning to menu
                System.out.println("\nPress Enter to return to the main menu...");
                scanner.nextLine();
            } else {
                // User chose to cancel
                // Wait for user input before returning to menu
                System.out.println("\nPress Enter to return to the main menu...");
                scanner.nextLine();
            }
        }
    }

    /**
     * Handles the withdrawal request process for an applicant's application.
     * Allows submission of withdrawal requests with user confirmation.
     */
    private void withdrawalRequest() {
        System.out.println("\n=== WITHDRAWAL REQUEST ===");

        // Get the current applicant's application
        ProjectApplication application = applicationController.getApplicationByApplicantNRIC(currentApplicant.getNric());

        // Check if the applicant has an application
        if (!currentApplicant.requestWithdrawal() || application == null) {
            System.out.println("You currently have no active applications to withdraw.");

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Check if there's already a withdrawal request
        if (application.getWithdrawalStatus() != null) {
            System.out.println("You already have a withdrawal request with status: " + application.getWithdrawalStatus());

            // Wait for user input before returning to menu
            System.out.println("\nPress Enter to return to the main menu...");
            scanner.nextLine();
            return;
        }

        // Display application details
        Project project = application.getProject();
        System.out.println("You are about to request withdrawal for:");
        System.out.println("Project: " + project.getProjectName());
        System.out.println("Status: " + application.getStatus().toString());

        // Ask for confirmation
        System.out.println("\nWarning: Withdrawal requests are subject to approval and may incur penalties.");
        System.out.println("Do you want to proceed?");
        System.out.println("1. Yes, submit withdrawal request");
        System.out.println("0. No, cancel and return to menu");

        int choice = getValidIntegerInput("\nEnter your choice: ", 0, 1);

        if (choice == 1) {
            // Submit withdrawal request through controller
            Withdrawal withdrawal = withdrawalController.submitWithdrawal(currentApplicant, application);

            if (withdrawal != null) {
                System.out.println("\nWithdrawal request submitted successfully.");
                System.out.println("Your request is now pending approval from an HDB Manager.");
                System.out.println("You will be notified once your request has been processed.");
            } else {
                System.out.println("\nFailed to submit withdrawal request. Please try again later.");
            }
        } else if (choice != 0) {
            System.out.println("Invalid choice.");
        }

        // Wait for user input before returning to menu
        System.out.println("\nPress Enter to return to the main menu...");
        scanner.nextLine();
    }
}