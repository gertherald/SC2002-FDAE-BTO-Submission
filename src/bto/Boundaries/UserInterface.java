package bto.Boundaries;

import java.util.Scanner;
import bto.Controllers.*;
import bto.Entities.*;
import bto.Enums.*;
import bto.Interfaces.*;

public class UserInterface {
    /** Input scanner for reading user inputs */
    private final Scanner scanner;

    /** Authentication controller for managing user login and registration */
    private final IAuthController authController;
    /** Controller for managing project-related operations */
    private final IProjectController projectController;
    /** Controller for managing application processes */
    private final IApplicationController applicationController;
    /** Controller for handling user enquiries */
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

    /** Interface for applicant-specific interactions */
    private final ApplicantInterface applicantInterface;
    /** Interface for HDB officer-specific interactions */
    private final OfficerInterface officerInterface;
    /** Interface for HDB manager-specific interactions */
    private final ManagerInterface managerInterface;

    /**
     * Constructor initializes all controllers and user interfaces.
     *
     * @param authController Controller for authentication processes
     * @param projectController Controller for project-related operations
     * @param applicationController Controller for managing applications
     * @param enquiryController Controller for handling enquiries
     * @param registrationController Controller for user registration
     * @param withdrawalController Controller for withdrawal requests
     * @param bookingController Controller for booking operations
     * @param receiptGenerator Generator for creating receipts
     * @param reportController Controller for generating reports
     */
    public UserInterface(IAuthController authController, IProjectController projectController,
                         IApplicationController applicationController, IEnquiryController enquiryController,
                         IRegistrationController registrationController,
                         IWithdrawalController withdrawalController, IBookingController bookingController,
                         IReceiptGenerator receiptGenerator, IReportController reportController) {
        this.scanner = new Scanner(System.in);

        // Use the provided controllers
        this.authController = authController;
        this.projectController = projectController;
        this.applicationController = applicationController;
        this.enquiryController = enquiryController;
        this.reportController = reportController;
        this.registrationController = registrationController;
        this.withdrawalController = withdrawalController;
        this.bookingController = bookingController;
        this.receiptGenerator = receiptGenerator;

        // Pass controllers to boundary classes (interfaces)
        this.applicantInterface = new ApplicantInterface(scanner, this.authController, this.projectController,
                this.applicationController, this.enquiryController,
                this.reportController, this.registrationController,
                this.withdrawalController, this.bookingController,
                this.receiptGenerator, this);
        this.officerInterface = new OfficerInterface(scanner, this.authController, this.projectController,
                this.applicationController, this.enquiryController,
                this.reportController, this.registrationController,
                this.withdrawalController, this.bookingController,
                this.receiptGenerator, this);
        this.managerInterface = new ManagerInterface(scanner, this.authController, this.projectController,
                this.applicationController, this.enquiryController,
                this.reportController, this.registrationController,
                this.withdrawalController, this.bookingController,
                this.receiptGenerator, this);
    }

    /**
     * Handles the sign-up process for a new user.
     * Collects and validates user information including NRIC, password,
     * age, marital status, and name.
     *
     * @return A new User object with the collected information
     */
    private User signUp() {
        String newnric = "";
        String newpassword = "";
        int newage = 0;
        MaritalStatus maritalStatus = null;
        String newname = "";

        while (true) {
            System.out.print("Enter NRIC: ");
            newnric = scanner.nextLine();
            if (authController.validateNRIC(newnric)) {
                if (!authController.getUsers().containsKey(newnric)) {
                    break;  // If valid, break out of the loop
                }
                else {
                    System.out.println("NRIC is already registered on another User");
                }
            } else {
                System.out.println("Invalid NRIC format. Please try again.");
            }
        }

        while (true) {
            System.out.print("Enter Password: ");
            newpassword = scanner.nextLine();
            if (authController.validatePassword(newpassword)) {
                break;  // If valid, break out of the loop
            } else {
                System.out.println("Password must be at least 8 characters. Please try again.");
            }
        }

        while (true) {
            try {
                System.out.print("Enter Age: ");
                String ageInput = scanner.nextLine().trim();

                if (ageInput.isEmpty()) {
                    System.out.println("Age cannot be empty. Please enter a valid age.");
                    continue;
                }

                newage = Integer.parseInt(ageInput);
                break;  // If parsing succeeds, break out of the loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid age format. Please enter a number.");
            }
        }

        while (true) {
            try {
                System.out.println("Enter Marital Status:");
                System.out.println("1) Single");
                System.out.println("2) Married");
                System.out.print("Your choice: ");

                String maritalInput = scanner.nextLine().trim();

                if (maritalInput.isEmpty()) {
                    System.out.println("Please select a valid option.");
                    continue;
                }

                int newmarital = Integer.parseInt(maritalInput);
                if (newmarital == 1) {
                    maritalStatus = MaritalStatus.SINGLE;
                    break;
                } else if (newmarital == 2) {
                    maritalStatus = MaritalStatus.MARRIED;
                    break;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number (1 or 2).");
            }
        }

        while (true) {
            System.out.print("Enter Name: ");
            newname = scanner.nextLine();
            if (authController.validateName(newname)) {
                break;  // If valid, break out of the loop
            } else {
                System.out.println("Name requirements: non-empty and contains only letters, spaces, and hyphens. Please try again.");
            }
        }

        // Create a temporary User object with the collected information
        return new User(newnric, newpassword, newage, maritalStatus, newname);
    }

    /**
     * Displays the login menu with options to login, sign up, or exit.
     * Handles user authentication and role-based registration.
     */
    public void displayLoginMenu() {
        while (true) {
            System.out.println("=== BTO MANAGEMENT SYSTEM ===");
            System.out.println("1. Login");
            System.out.println("2. Sign Up");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Please enter a valid option.");
                continue;
            }
            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        System.out.print("Enter NRIC: ");
                        String nric = scanner.nextLine();
                        System.out.print("Enter Password: ");
                        String password = scanner.nextLine();

                        User user = authController.loginUser(nric, password);
                        if (user != null) {
                            displayUserMenu(user);
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                            continue;
                        }
                        break;
                    case 2:
                        while (true) {
                            try {
                                System.out.println("Pick a Role:");
                                System.out.println("1) Applicant");
                                System.out.println("2) HDB Officer");
                                System.out.println("3) HDB Manager");
                                System.out.println("4) Go Back");
                                System.out.print("Enter your choice: ");

                                String roleInput = scanner.nextLine().trim();

                                if (roleInput.isEmpty()) {
                                    System.out.println("Please enter a valid option.");
                                    continue;
                                }

                                int rolechoice = Integer.parseInt(roleInput);

                                switch(rolechoice) {
                                    case 1:
                                        User applicantData = signUp();
                                        Applicant applicant = new Applicant(
                                                applicantData.getNric(),
                                                applicantData.getPassword(),
                                                applicantData.getAge(),
                                                applicantData.getMaritalStatus(),
                                                applicantData.getName()
                                        );
                                        System.out.println("Applicant registered successfully!");
                                        if (authController.addUser(applicant)) {
                                            User newuser = authController.loginUser(applicantData.getNric(), applicantData.getPassword());
                                            if (newuser != null) {
                                                displayUserMenu(newuser);
                                            } else {
                                                System.out.println("Invalid credentials. Please try again.");
                                                // Continue in the login menu
                                            }
                                        }
                                        break;

                                    case 2:
                                        User officerData = signUp();
                                        HDBOfficer officer = new HDBOfficer(
                                                officerData.getNric(),
                                                officerData.getPassword(),
                                                officerData.getAge(),
                                                officerData.getMaritalStatus(),
                                                officerData.getName()
                                        );
                                        System.out.println("HDB Officer registered successfully!");
                                        if (authController.addUser(officer)) {
                                            User newuser = authController.loginUser(officerData.getNric(), officerData.getPassword());
                                            if (newuser != null) {
                                                displayUserMenu(newuser);
                                            } else {
                                                System.out.println("Invalid credentials. Please try again.");
                                                // Continue in the login menu
                                            }
                                        }
                                        break;

                                    case 3:
                                        User managerData = signUp();
                                        HDBManager manager = new HDBManager(
                                                managerData.getNric(),
                                                managerData.getPassword(),
                                                managerData.getAge(),
                                                managerData.getMaritalStatus(),
                                                managerData.getName()
                                        );
                                        System.out.println("HDB Manager registered successfully!");
                                        if (authController.addUser(manager)) {
                                            User newuser = authController.loginUser(managerData.getNric(), managerData.getPassword());
                                            if (newuser != null) {
                                                displayUserMenu(newuser);
                                            } else {
                                                System.out.println("Invalid credentials. Please try again.");
                                                // Continue in the login menu
                                            }
                                        }
                                        break;

                                    case 4:
                                        System.out.println("Back to Log in Menu.");
                                        displayLoginMenu();
                                        return;

                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                        continue; // This will continue the loop and prompt the user again.
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number.");
                            } catch (Exception e) {
                                System.out.println("An error occurred: " + e.getMessage());
                                System.out.println("Please try again.");
                            }
                        }
                    case 0:
                        System.out.println("Thank you for using BTO Management System.");
                        System.exit(0);
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
            } catch (NumberFormatException e) {
                // Catch exception if input is not a valid number
                System.out.println("Invalid input. Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again.");
            }
        }
    }

    /**
     * Displays the appropriate user menu based on the user's role.
     *
     * @param user The logged-in user whose menu will be displayed
     */
    public void displayUserMenu(User user) {
        if (user instanceof Applicant) {
            applicantInterface.displayApplicantMenu((Applicant) user);
        } else if (user instanceof HDBOfficer) {
            officerInterface.displayOfficerMenu((HDBOfficer) user);
        } else if (user instanceof HDBManager) {
            managerInterface.displayManagerMenu((HDBManager) user);
        }
    }
}