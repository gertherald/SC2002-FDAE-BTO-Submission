package bto.Entities;

import java.util.ArrayList;
import java.util.List;
import bto.Enums.*;
import bto.EntitiesProjectRelated.*;

/**
 * Represents an HDB officer in the BTO system.
 * <p>
 * The HDBOfficer class extends the User class and represents staff members who process
 * applications, respond to enquiries, and handle flat bookings. HDB officers have a dual role
 * as they can also apply for flats as applicants through their embedded applicant role.
 */
public class HDBOfficer extends User {
	/** List of projects assigned to this officer. */
	private List<Project> assignedProjects;

	/** List of registration requests made by this officer for projects. */
	private List<OfficerRegistration> registrations;

	/** Embedded applicant role to allow officers to apply for flats. */
	private Applicant applicantRole; // Composition - HDBOfficer has an Applicant role

	/**
	 * Default constructor for creating an HDBOfficer.
	 * Initializes empty lists for assigned projects and registrations,
	 * and creates a default applicant role.
	 */
	public HDBOfficer() {
		super();
		assignedProjects = new ArrayList<>();
		registrations = new ArrayList<>();
		// Create the applicant role with the same user details
		applicantRole = new Applicant();
	}

	/**
	 * Creates an HDBOfficer with specified personal information.
	 * Also initializes the embedded applicant role with the same information.
	 *
	 * @param nric The officer's National Registration Identity Card number
	 * @param password The officer's password for system access
	 * @param age The officer's age
	 * @param maritalStatus The officer's marital status
	 * @param name The officer's name
	 */
	public HDBOfficer(String nric, String password, int age, MaritalStatus maritalStatus, String name) {
		super(nric, password, age, maritalStatus, name);
		assignedProjects = new ArrayList<>();
		registrations = new ArrayList<>();
		// Create the applicant role with the same user details
		applicantRole = new Applicant(nric, password, age, maritalStatus, name);
	}

	/**
	 * Registers the officer for a project.
	 * <p>
	 * Officers cannot register for projects that they have applied for as an applicant.
	 *
	 * @param project The project to register for
	 * @return true if registration was successful, false otherwise
	 */
	public boolean registerForProject(Project project) {
		// Check if the officer has applied for this project as an applicant
		if (applicantRole.getAppliedProject() != null &&
				applicantRole.getAppliedProject().getProject().equals(project)) {
			// Cannot register for a project that the officer has applied for
			return false;
		}

		// Implementation for registering for a project
		return false; // Placeholder
	}

	/**
	 * Retrieves the registration status for all projects registered for.
	 *
	 * @return A string representation of all registration statuses
	 */
	public String viewRegistrationStatus() {
		// Implementation to view registration status
		return ""; // Placeholder
	}

	/**
	 * Updates the availability of a specific flat type in assigned projects.
	 *
	 * @param flatType The flat type to update availability for
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateFlatAvailability(FlatType flatType) {
		// Implementation to update flat availability
		return false; // Placeholder
	}

	/**
	 * Retrieves an application by applicant NRIC.
	 * <p>
	 * Officers cannot retrieve their own applications.
	 *
	 * @param nric The NRIC of the applicant
	 * @return The application associated with the NRIC, or null if not found or not allowed
	 */
	public ProjectApplication retrieveApplication(String nric) {
		// Check if the officer is trying to access their own application
		if (nric.equals(this.getNric())) {
			// Not allowed to process their own application
			return null;
		}

		// Implementation to retrieve application by NRIC
		return null; // Placeholder
	}

	/**
	 * Updates the booking status for an application with a specified flat type.
	 * <p>
	 * Officers cannot update their own applications.
	 *
	 * @param application The application to update
	 * @param flatType The flat type for the booking
	 * @return true if the update was successful, false otherwise
	 */
	public boolean updateBookingStatus(ProjectApplication application, FlatType flatType) {
		// Check if the application belongs to the officer
		if (application.getApplicant().getNric().equals(this.getNric())) {
			// Not allowed to update their own application
			return false;
		}

		// Implementation to update booking status
		return false; // Placeholder
	}

	/**
	 * Generates a receipt for a booking application.
	 * <p>
	 * Officers cannot generate receipts for their own applications.
	 *
	 * @param application The application to generate a receipt for
	 * @return The receipt as a string, or null if not allowed
	 */
	public String generateReceipt(ProjectApplication application) {
		// Check if the application belongs to the officer
		if (application.getApplicant().getNric().equals(this.getNric())) {
			// Not allowed to generate receipt for their own application
			return null;
		}

		// Implementation to generate receipt
		return ""; // Placeholder
	}

	/**
	 * Responds to an enquiry from an applicant.
	 * <p>
	 * Officers cannot respond to their own enquiries.
	 *
	 * @param enquiry The enquiry to respond to
	 * @return true if the response was recorded successfully, false otherwise
	 */
	public boolean respondToEnquiry(Enquiry enquiry) {
		// Check if the enquiry belongs to the officer
		if (enquiry.getApplicant().getNric().equals(this.getNric())) {
			// Not allowed to respond to their own enquiry
			return false;
		}

		// Implementation to respond to enquiry
		return false; // Placeholder
	}

	/**
	 * Displays the officer's profile information including personal details, assigned projects,
	 * and any active applications made through the applicant role.
	 *
	 * @return A formatted string containing the officer's profile information
	 */
	@Override
	public String displayProfile() {
		StringBuilder profile = new StringBuilder();
		profile.append("\n=== OFFICER PROFILE ===\n");
		profile.append("Name: ").append(getName()).append("\n");
		profile.append("NRIC: ").append(getNric()).append("\n");
		profile.append("Age: ").append(getAge()).append("\n");
		profile.append("Marital Status: ").append(getMaritalStatus()).append("\n");

		// Display assigned projects if any
		List<Project> assignedProjects = getAssignedProjects();
		if (assignedProjects.isEmpty()) {
			profile.append("Assigned Projects: None\n");
		} else {
			profile.append("Assigned Projects:\n");
			for (Project project : assignedProjects) {
				profile.append(" - ").append(project.getProjectName()).append("\n");
			}
		}

		// Get the officer's application as an applicant
		ProjectApplication application = getApplicantRole().getAppliedProject();

		// Display applied project information if it exists
		if (application == null) {
			profile.append("Project Applied: None\n");
		} else {
			Project project = application.getProject();
			ApplicationStatus status = application.getStatus();

			// Get the flat type, or "Not yet chosen" if null
			String flatTypeStr = "Not yet chosen";
			if (application.getSelectedFlatType() != null) {
				flatTypeStr = application.getSelectedFlatType().toString();
			}

			profile.append("Project Applied: ").append(project.getProjectName())
					.append(" (").append(status.toString())
					.append(", ").append(flatTypeStr).append(")\n");
		}

		return profile.toString();
	}

	/**
	 * Gets detailed information about all projects assigned to the officer.
	 *
	 * @return List of assigned projects with details
	 */
	public List<Project> getAssignedProjectDetails() {
		return assignedProjects;
	}

	/**
	 * Applies for a project through the officer's applicant role.
	 * <p>
	 * Officers cannot apply for projects they are assigned to.
	 *
	 * @param project The project to apply for
	 * @return true if the application was successful, false otherwise
	 */
	public boolean applyForProject(Project project) {
		// Check if the officer is assigned to this project
		for (Project assignedProject : assignedProjects) {
			if (assignedProject.equals(project)) {
				// Cannot apply for a project that the officer is assigned to
				return false;
			}
		}

		// Delegate to the applicant role
		return applicantRole.applyForProject(project);
	}

	/**
	 * Views the current application status through the officer's applicant role.
	 *
	 * @return The current application status, or null if no application exists
	 */
	public ApplicationStatus viewApplicationStatus() {
		// Delegate to the applicant role
		return applicantRole.viewApplicationStatus();
	}

	/**
	 * Requests a withdrawal of an application through the officer's applicant role.
	 *
	 * @return true if the withdrawal request was successful, false otherwise
	 */
	public boolean requestWithdrawal() {
		// Delegate to the applicant role
		return applicantRole.requestWithdrawal();
	}

	/**
	 * Gets the list of projects assigned to this officer.
	 *
	 * @return The list of assigned projects
	 */
	public List<Project> getAssignedProjects() {
		return assignedProjects;
	}

	/**
	 * Sets the list of projects assigned to this officer.
	 *
	 * @param assignedProjects The list of projects to assign
	 */
	public void setAssignedProjects(List<Project> assignedProjects) {
		this.assignedProjects = assignedProjects;
	}

	/**
	 * Gets the list of registration requests made by this officer.
	 *
	 * @return The list of officer registrations
	 */
	public List<OfficerRegistration> getRegistrations() {
		return registrations;
	}

	/**
	 * Sets the list of registration requests for this officer.
	 *
	 * @param registrations The list of officer registrations to set
	 */
	public void setRegistrations(List<OfficerRegistration> registrations) {
		this.registrations = registrations;
	}

	/**
	 * Gets the embedded applicant role of this officer.
	 *
	 * @return The applicant role associated with this officer
	 */
	public Applicant getApplicantRole() {
		return applicantRole;
	}

	/**
	 * Sets the embedded applicant role for this officer.
	 *
	 * @param applicantRole The applicant role to set
	 */
	public void setApplicantRole(Applicant applicantRole) {
		this.applicantRole = applicantRole;
	}

	/**
	 * Adds a project to the officer's list of assigned projects if it's not already in the list.
	 *
	 * @param project The project to add to the officer's assigned projects
	 */
	public void addAssignedProject(Project project) {
		if (!assignedProjects.contains(project)) {
			assignedProjects.add(project);
		}
	}

	/**
	 * Adds a registration to the officer's list of registrations if it's not already in the list.
	 *
	 * @param registration The officer registration to add
	 */
	public void addRegistration(OfficerRegistration registration) {
		if (!registrations.contains(registration)) {
			registrations.add(registration);
		}
	}
}