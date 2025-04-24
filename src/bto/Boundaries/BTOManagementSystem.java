package bto.Boundaries;

import bto.Controllers.*;
import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import bto.Enums.*;
import bto.Interfaces.*;
import java.util.List;
import java.util.ArrayList;

public class BTOManagementSystem {
    /** User interface for system interactions */
    private static UserInterface ui;

    /** File manager for handling data persistence */
    private static FileManager fileManager;

    // Controller declarations for various system functionalities
    /** Controller for user authentication */
    private static IAuthController authController;

    /** Controller for project-related operations */
    private static IProjectController projectController;

    /** Controller for managing project applications */
    private static IApplicationController applicationController;

    /** Controller for handling enquiries */
    private static IEnquiryController enquiryController;

    /** Controller for generating reports */
    private static IReportController reportController;

    /** Controller for user registration processes */
    private static IRegistrationController registrationController;

    /** Controller for managing withdrawal requests */
    private static IWithdrawalController withdrawalController;

    /** Controller for booking-related operations */
    private static IBookingController bookingController;

    /** Generator for creating receipts */
    private static IReceiptGenerator receiptGenerator;
    /**
     * Main method to launch the BTO Management System.
     * Sets up a shutdown hook to save data and initializes the system.
     *
     * @param args Command-line arguments (not used)
     */
    public static void main(String[] args) {

        // Add shutdown hook to save data
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveData();
            System.out.println("System data saved successfully.");
        }));

        initialize();
        ui.displayLoginMenu();
    }

    /**
     * Initializes all system controllers, file manager, and loads existing data.
     * Creates the user interface after data loading.
     */
    public static void initialize() {
        // Initialize only the core controllers needed for data management
        authController = new AuthController();
        projectController = new ProjectController();
        applicationController = new ApplicationController();
        enquiryController = new EnquiryController();
        registrationController = new RegistrationController();
        reportController = new ReportController();
        withdrawalController = new WithdrawalController();
        bookingController = new BookingController();
        receiptGenerator = new ReceiptGenerator();

        // Initialize file manager
        fileManager = new FileManager();

        // Load data
        loadData();

        // Initialize UI after loading data
        ui = new UserInterface(authController, projectController, applicationController, enquiryController,
                registrationController, withdrawalController, bookingController,
                receiptGenerator, reportController);
    }

    /**
     * Loads system data from persistent storage.
     * Initializes controllers with loaded data and performs post-loading setup.
     */
    public static void loadData() {
        try {
            // Load users first
            List<Applicant> applicants = fileManager.loadApplicants();
            List<HDBOfficer> officers = fileManager.loadOfficers();
            List<HDBManager> managers = fileManager.loadManagers();

            // Create a combined list of all users for enquiry responder lookup
            List<User> allUsers = new ArrayList<>();
            allUsers.addAll(applicants);
            allUsers.addAll(officers);
            allUsers.addAll(managers);

            // Initialize controllers with users
            initializeAuthController(applicants, officers, managers);

            // Load projects (passing officers and managers)
            List<Project> projects = fileManager.loadProjects(officers, managers);
            initializeProjectController(projects);

            // Load applications and enquiries (passing all users for responder lookup)
            List<ProjectApplication> applications = fileManager.loadApplications(applicants, projects);
            List<Enquiry> enquiries = fileManager.loadEnquiries(applicants, projects, allUsers);

            // Load additional entities
            List<OfficerRegistration> registrations = fileManager.loadOfficerRegistrations(officers, projects);
            // Load withdrawals (new addition)
            List<Withdrawal> withdrawals = fileManager.loadWithdrawals(applicants, applications);
            // Updated to pass officers list to loadBookings
            List<FlatBooking> bookings = fileManager.loadBookings(applicants, projects, officers);
            List<Receipt> receipts = fileManager.loadReceipts(applicants, projects);

            // Print loading status
            printLoadingStatus(
                    applicants, officers, managers,
                    projects, applications, enquiries,
                    registrations, withdrawals, bookings, receipts
            );

            // Post-loading setup
            postLoadSetup(
                    applicants, officers, managers,
                    projects, applications, enquiries,
                    withdrawals, bookings, receipts
            );

            // Initialize controllers
            initializeApplicationController(applications);
            initializeEnquiryController(enquiries);
            initializeRegistrationController(registrations);
            initializeWithdrawalController(withdrawals);
            initializeBookingController(bookings, receipts);

            // Initialize report controller (no need to load reports from file)
            initializeReportController();

            System.out.println("All data loaded and initialized successfully.");
        } catch (Exception e) {
            System.err.println("Error during load data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prints a summary of loaded entities to the console.
     * Provides visibility into the number of entities loaded during initialization.
     *
     * @param applicants List of loaded applicants
     * @param officers List of loaded HDB officers
     * @param managers List of loaded HDB managers
     * @param projects List of loaded projects
     * @param applications List of loaded project applications
     * @param enquiries List of loaded enquiries
     * @param registrations List of loaded officer registrations
     * @param bookings List of loaded flat bookings
     * @param receipts List of loaded receipts
     */
    private static void printLoadingStatus(
            List<Applicant> applicants,
            List<HDBOfficer> officers,
            List<HDBManager> managers,
            List<Project> projects,
            List<ProjectApplication> applications,
            List<Enquiry> enquiries,
            List<OfficerRegistration> registrations,
            List<Withdrawal> withdrawals,
            List<FlatBooking> bookings,
            List<Receipt> receipts
    ) {
        System.out.println("========== System Initialization ==========");
        System.out.println("Loaded " + applicants.size() + " applicants");
        System.out.println("Loaded " + officers.size() + " officers");
        System.out.println("Loaded " + managers.size() + " managers");
        System.out.println("Loaded " + projects.size() + " projects");
        System.out.println("Loaded " + applications.size() + " applications");
        System.out.println("Loaded " + enquiries.size() + " enquiries");
        System.out.println("Loaded " + registrations.size() + " officer registrations");
        System.out.println("Loaded " + withdrawals.size() + " withdrawal requests");
        System.out.println("Loaded " + bookings.size() + " flat bookings");
        System.out.println("Loaded " + receipts.size() + " receipts");
        System.out.println("===========================================");
    }

    /**
     * Performs post-loading setup to establish relationships between loaded entities.
     * Links applications to applicants, bookings to applicants,
     * and enquiries to projects.
     *
     * @param applicants List of loaded applicants
     * @param officers List of loaded HDB officers
     * @param managers List of loaded HDB managers
     * @param projects List of loaded projects
     * @param applications List of loaded project applications
     * @param enquiries List of loaded enquiries
     * @param bookings List of loaded flat bookings
     * @param receipts List of loaded receipts
     */
    private static void postLoadSetup(
            List<Applicant> applicants,
            List<HDBOfficer> officers,
            List<HDBManager> managers,
            List<Project> projects,
            List<ProjectApplication> applications,
            List<Enquiry> enquiries,
            List<Withdrawal> withdrawals,
            List<FlatBooking> bookings,
            List<Receipt> receipts
    ) {
        // Link applications back to applicants (only if withdrawal status is not APPROVED)
        for (Applicant applicant : applicants) {
            // Clear any existing applied project first
            applicant.setAppliedProject(null);

            for (ProjectApplication app : applications) {
                if (app.getApplicant().getNric().equals(applicant.getNric()) &&
                        !"APPROVED".equals(app.getWithdrawalStatus())) {
                    applicant.setAppliedProject(app);
                    break;
                }
            }
        }

        // Link bookings back to applicants
        for (Applicant applicant : applicants) {
            for (FlatBooking booking : bookings) {
                if (booking.getApplicant().getNric().equals(applicant.getNric())) {
                    applicant.setBookedFlat(booking);
                    break;
                }
            }
        }

        // Link enquiries to projects and applicants
        for (Project project : projects) {
            for (Enquiry enquiry : enquiries) {
                if (enquiry.getProject() != null &&
                        enquiry.getProject().getProjectName().equals(project.getProjectName())) {
                    project.addEnquiry(enquiry);
                }
            }
        }

        // Link withdrawals to applications and make sure approved withdrawals clear applicant's applied project
        for (Withdrawal withdrawal : withdrawals) {
            // Ensure the application has the correct withdrawal status
            ProjectApplication app = withdrawal.getApplication();
            if (app != null) {
                app.setWithdrawalStatus(withdrawal.getStatus());

                // If withdrawal is approved, ensure the applicant's applied project is cleared
                if ("APPROVED".equals(withdrawal.getStatus())) {
                    withdrawal.getApplicant().setAppliedProject(null);
                }
            }
        }

        // Reinitialize project flats
        for (Project project : projects) {
            project.initializeProjectFlats();
        }
    }

    /**
     * Saves all system data to persistent storage.
     * Collects data from various controllers and uses file manager to save entities.
     */
    public static void saveData() {
        try {
            // Get data from controllers
            List<Applicant> applicants = authController.getAllApplicants();
            List<HDBOfficer> officers = authController.getAllOfficers();
            List<HDBManager> managers = authController.getAllManagers();

            List<Project> projects = projectController.getAllProjects();
            List<ProjectApplication> applications = applicationController.getAllApplications();
            List<Enquiry> enquiries = enquiryController.getAllEnquiries();

            // Get additional entities
            List<OfficerRegistration> registrations = registrationController.getAllRegistrations();
            List<Withdrawal> withdrawals = withdrawalController.getAllWithdrawals();
            List<FlatBooking> bookings = bookingController.getAllBookings();
            List<Receipt> receipts = bookingController.getAllReceipts();

            // Print summary of data being saved
            System.out.println("========== Saving System Data ==========");
            System.out.println("Saving " + applicants.size() + " applicants");
            System.out.println("Saving " + officers.size() + " officers");
            System.out.println("Saving " + managers.size() + " managers");
            System.out.println("Saving " + projects.size() + " projects");
            System.out.println("Saving " + applications.size() + " applications");
            System.out.println("Saving " + enquiries.size() + " enquiries");
            System.out.println("Saving " + registrations.size() + " officer registrations");
            System.out.println("Saving " + withdrawals.size() + " withdrawal requests");
            System.out.println("Saving " + bookings.size() + " flat bookings");
            System.out.println("Saving " + receipts.size() + " receipts");
            System.out.println("========================================");

            // Save existing entities
            fileManager.saveApplicants(applicants);
            fileManager.saveOfficers(officers);
            fileManager.saveManagers(managers);
            fileManager.saveProjects(projects);
            fileManager.saveApplications(applications);
            fileManager.saveEnquiries(enquiries);

            // Save additional entities
            fileManager.saveOfficerRegistrations(registrations);
            fileManager.saveWithdrawals(withdrawals);
            fileManager.saveBookings(bookings);
            fileManager.saveReceipts(receipts);

            System.out.println("Data saved successfully.");
        } catch (Exception e) {
            System.err.println("Error during save data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Update the initialization method for BookingController
    /**
     * Initializes the BookingController with loaded bookings and receipts.
     *
     * @param bookings List of flat bookings to set in the controller
     * @param receipts List of receipts to set in the controller
     */
    private static void initializeBookingController(List<FlatBooking> bookings, List<Receipt> receipts) {
        bookingController.setBookings(bookings);
        bookingController.setReceipts(receipts);
    }

    // Add a new initialization method for WithdrawalController
    /**
     * Initializes the withdrawal controller with a list of withdrawals.
     *
     * @param withdrawals The list of withdrawal objects to be set in the withdrawal controller
     */
    private static void initializeWithdrawalController(List<Withdrawal> withdrawals) {
        withdrawalController.setWithdrawals(withdrawals);
    }

    // Initialization methods for controllers
    /**
     * Initializes the AuthController by adding all loaded users.
     *
     * @param applicants List of applicants to add to the auth controller
     * @param officers List of HDB officers to add to the auth controller
     * @param managers List of HDB managers to add to the auth controller
     */
    private static void initializeAuthController(List<Applicant> applicants, List<HDBOfficer> officers, List<HDBManager> managers) {
        // Add all users to the auth controller
        for (Applicant applicant : applicants) {
            authController.addUser(applicant);
        }

        for (HDBOfficer officer : officers) {
            authController.addUser(officer);
        }

        for (HDBManager manager : managers) {
            authController.addUser(manager);
        }
    }

    /**
     * Initializes the ProjectController with loaded projects.
     *
     * @param projects List of projects to set in the controller
     */
    private static void initializeProjectController(List<Project> projects) {
        projectController.setProjects(projects);
    }

    /**
     * Initializes the ApplicationController with loaded applications.
     *
     * @param applications List of project applications to set in the controller
     */
    private static void initializeApplicationController(List<ProjectApplication> applications) {
        applicationController.setApplications(applications);
    }

    /**
     * Initializes the EnquiryController with loaded enquiries.
     *
     * @param enquiries List of enquiries to set in the controller
     */
    private static void initializeEnquiryController(List<Enquiry> enquiries) {
        enquiryController.setEnquiries(enquiries);
    }

    /**
     * Initializes the RegistrationController with loaded officer registrations.
     *
     * @param registrations List of officer registrations to set in the controller
     */
    private static void initializeRegistrationController(List<OfficerRegistration> registrations) {
        registrationController.setRegistrations(registrations);
    }

    /**
     * Initializes the ReportController.
     * Currently a no-op method as reports are generated on-demand.
     */
    private static void initializeReportController() {
        // No need to load reports from file as they are generated on-demand
    }
}