package bto.Controllers;

import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import bto.Enums.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages file operations for the BTO system including loading and saving data.
 * <p>
 * This class handles persistence operations for all entity types in the system,
 * reading data from and writing data to text files. It provides methods for loading
 * and saving users, projects, applications, enquiries, bookings, and other entities.
 */
public class FileManager {
	/** Path to the file containing applicant data */
	private static final String APPLICANT_FILE = "./src/bto/data/Applicant List.txt";

	/** Path to the file containing project data */
	private static final String PROJECT_FILE = "./src/bto/data/Project List.txt";

	/** Path to the file containing officer data */
	private static final String OFFICER_FILE = "./src/bto/data/Officer List.txt";

	/** Path to the file containing manager data */
	private static final String MANAGER_FILE = "./src/bto/data/Manager List.txt";

	/** Path to the file containing officer registration data */
	private static final String OFFICER_REGISTRATION_FILE = "./src/bto/data/Officer Registration List.txt";

	/** Path to the file containing enquiry data */
	private static final String ENQUIRY_FILE = "./src/bto/data/Enquiry List.txt";

	/** Path to the file containing application data */
	private static final String APPLICATION_FILE = "./src/bto/data/Application List.txt";

	/** Path to the file containing booking data */
	private static final String BOOKING_FILE = "./src/bto/data/Booking List.txt";

	/** Path to the file containing receipt data */
	private static final String RECEIPT_FILE = "./src/bto/data/Receipt List.txt";

	/** Path to the file containing withdrawal data */
	private static final String WITHDRAWAL_FILE = "./src/bto/data/Withdrawal List.txt";

	/** Date format used for reading and writing dates to files */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");

	/**
	 * Loads all users from files into a combined list.
	 *
	 * @return A list containing all applicants, officers, and managers
	 */
	public List<User> loadAllUsers() {
		List<User> allUsers = new ArrayList<>();

		// Load users of each type
		List<Applicant> applicants = loadApplicants();
		List<HDBOfficer> officers = loadOfficers();
		List<HDBManager> managers = loadManagers();

		// Add all users to the combined list
		allUsers.addAll(applicants);
		allUsers.addAll(officers);
		allUsers.addAll(managers);

		return allUsers;
	}

	/**
	 * Loads applicant data from file.
	 *
	 * @return A list of all applicants found in the data file
	 */
	public List<Applicant> loadApplicants() {
		List<Applicant> applicants = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(APPLICANT_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 5) continue;

				String name = parts[0].trim();
				String nric = parts[1].trim();
				int age = Integer.parseInt(parts[2].trim());
				MaritalStatus maritalStatus = convertToMaritalStatus(parts[3].trim());
				String password = parts[4].trim();

				Applicant applicant = new Applicant(nric, password, age, maritalStatus, name);

				applicants.add(applicant);
			}

		} catch (IOException e) {
			System.out.println("Warning: Failed to load applicants data. " + e.getMessage());
		}

		return applicants;
	}

	/**
	 * Loads HDB officer data from file.
	 *
	 * @return A list of all HDB officers found in the data file
	 */
	public List<HDBOfficer> loadOfficers() {
		List<HDBOfficer> officers = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(OFFICER_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 5) continue;

				String name = parts[0].trim();
				String nric = parts[1].trim();
				int age = Integer.parseInt(parts[2].trim());
				MaritalStatus maritalStatus = convertToMaritalStatus(parts[3].trim());
				String password = parts[4].trim();

				HDBOfficer officer = new HDBOfficer(nric, password, age, maritalStatus, name);

				officers.add(officer);
			}

		} catch (IOException e) {
			System.out.println("Warning: Failed to load officers data. " + e.getMessage());
		}

		return officers;
	}

	/**
	 * Loads HDB manager data from file.
	 *
	 * @return A list of all HDB managers found in the data file
	 */
	public List<HDBManager> loadManagers() {
		List<HDBManager> managers = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(MANAGER_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 5) continue;

				String name = parts[0].trim();
				String nric = parts[1].trim();
				int age = Integer.parseInt(parts[2].trim());
				MaritalStatus maritalStatus = convertToMaritalStatus(parts[3].trim());
				String password = parts[4].trim();

				HDBManager manager = new HDBManager(nric, password, age, maritalStatus, name);

				managers.add(manager);
			}

		} catch (IOException e) {
			System.out.println("Warning: Failed to load managers data. " + e.getMessage());
		}

		return managers;
	}

	/**
	 * Loads project data from file and establishes relationships with managers and officers.
	 *
	 * @param loadedOfficers The list of HDB officers to associate with projects
	 * @param loadedManagers The list of HDB managers to associate with projects
	 * @return A list of all projects with their associated managers and officers
	 */
	public List<Project> loadProjects(List<HDBOfficer> loadedOfficers, List<HDBManager> loadedManagers) {
		List<Project> projects = new ArrayList<>();
		Map<String, HDBManager> managerMap = createManagerMap(loadedManagers);
		Map<String, HDBOfficer> officerMap = createOfficerMap(loadedOfficers);

		try (BufferedReader reader = new BufferedReader(new FileReader(PROJECT_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 12) continue;

				String projectName = parts[0].trim();
				String neighborhood = parts[1].trim();

				// Parse flat types and units
				FlatType type1 = convertToFlatType(parts[2].trim());
				int units1 = Integer.parseInt(parts[3].trim());
				int price1 = Integer.parseInt(parts[4].trim());

				FlatType type2 = convertToFlatType(parts[5].trim());
				int units2 = Integer.parseInt(parts[6].trim());
				int price2 = Integer.parseInt(parts[7].trim());

				// Parse dates
				Date openDate = null;
				Date closeDate = null;
				try {
					openDate = DATE_FORMAT.parse(parts[8].trim());
					closeDate = DATE_FORMAT.parse(parts[9].trim());
				} catch (ParseException e) {
					System.out.println("Warning: Failed to parse date for project " + projectName + ". " + e.getMessage());
					continue;
				}

				// Parse manager and officer assignments
				String managerName = parts[10].trim();
				int totalOfficerSlots = Integer.parseInt(parts[11].trim());

				String officersString = parts.length > 12 ? parts[12].trim() : "";
				String[] officerNames = officersString.replace("\"", "").split(",");

				// Create the project
				Project project = new Project();
				project.setProjectName(projectName);
				project.setNeighborhood(neighborhood);
				project.setApplicationOpenDate(openDate);
				project.setApplicationCloseDate(closeDate);
				project.setTotalOfficerSlots(totalOfficerSlots);
				project.setAvailableHDBOfficerSlots(calculateAvailableSlots(totalOfficerSlots,officerNames, officerMap));


				// Set flat type units
				Map<FlatType, Integer> flatTypeUnits = new HashMap<>();
				flatTypeUnits.put(type1, units1);
				flatTypeUnits.put(type2, units2);
				project.setFlatTypeUnits(flatTypeUnits);

				// Set visibility (default to false for now)
				project.setVisible(true);

				// Set manager
				HDBManager manager = findManagerByName(managerName, managerMap);
				if (manager != null) {
					project.setManagerInCharge(manager);

					// Also set this project as the managed project for the manager
					manager.setManagedProject(project);
				}

				// Assign officers
				for (String officerName : officerNames) {
					if (officerName.trim().isEmpty()) continue;

					HDBOfficer officer = findOfficerByName(officerName.trim(), officerMap);
					if (officer != null) {
						// Add project to officer's assigned projects
						officer.addAssignedProject(project);

						// Create officer registration (with APPROVED status)
						OfficerRegistration registration = new OfficerRegistration(officer, project);
						registration.setRegistrationStatus("APPROVED");

						// Add registration to both officer and project
						officer.addRegistration(registration);
						project.addOfficerRegistration(registration);

					}
				}

				projects.add(project);
			}

		} catch (IOException e) {
			System.out.println("Warning: Failed to load projects data. " + e.getMessage());
		}

		return projects;
	}

	/**
	 * Calculates the available officer slots for a project.
	 *
	 * @param totalOfficerSlots The total number of officer slots for the project
	 * @param officerNames Array of officer names assigned to the project
	 * @param officerMap Map of officers for lookup
	 * @return The number of available officer slots
	 */
	private int calculateAvailableSlots(int totalOfficerSlots,String[] officerNames, Map<String, HDBOfficer> officerMap) {
		int assignedOfficers = 0;
		for (String officerName : officerNames) {
			if (officerName.trim().isEmpty()) continue;

			HDBOfficer officer = findOfficerByName(officerName.trim(), officerMap);
			if (officer != null) {
				assignedOfficers++;
			}
		}

		return Math.max(0, totalOfficerSlots - assignedOfficers);
	}

	/**
	 * Loads application data from file and establishes relationships with applicants and projects.
	 *
	 * @param applicants The list of applicants to associate with applications
	 * @param projects The list of projects to associate with applications
	 * @return A list of all project applications
	 */
	public List<ProjectApplication> loadApplications(List<Applicant> applicants, List<Project> projects) {
		List<ProjectApplication> applications = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(APPLICATION_FILE))) {
			String line;
			// Skip header
			reader.readLine();

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length < 5) continue;

				// Find matching applicant and project
				Applicant applicant = findApplicantByNRIC(parts[0], applicants);
				Project project = findProjectByName(parts[1], projects);

				if (applicant != null && project != null) {
					ProjectApplication application = new ProjectApplication(applicant, project);

					// Set status
					application.setStatus(ApplicationStatus.valueOf(parts[2]));

					// Set withdrawal status
					if (!"N/A".equals(parts[3])) {
						application.setWithdrawalStatus(parts[3]);
					}

					// Set selected flat type
					if (!"N/A".equals(parts[4])) {
						application.setSelectedFlatType(FlatType.valueOf(parts[4]));
					}

					applications.add(application);

					// Register the application with the project
					project.addApplication(application);

					// Only set appliedProject for the applicant if withdrawal status is not APPROVED
					if (!"APPROVED".equals(application.getWithdrawalStatus())) {
						applicant.setAppliedProject(application);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading applications: " + e.getMessage());
		}

		return applications;
	}

	/**
	 * Loads enquiry data from file and establishes relationships with applicants, projects, and users.
	 *
	 * @param applicants The list of applicants who may have submitted enquiries
	 * @param projects The list of projects that enquiries may be about
	 * @param allUsers The list of all users for finding responders
	 * @return A list of all enquiries
	 */
	public List<Enquiry> loadEnquiries(List<Applicant> applicants, List<Project> projects, List<User> allUsers) {
		List<Enquiry> enquiries = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(ENQUIRY_FILE))) {
			String line;
			// Skip header
			reader.readLine();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length < 7) continue; // Need at least 7 fields with the new format

				// Find matching applicant and project
				Applicant applicant = findApplicantByNRIC(parts[0], applicants);
				Project project = "N/A".equals(parts[1]) ? null : findProjectByName(parts[1], projects);

				if (applicant != null) {
					// Create enquiry
					Enquiry enquiry = new Enquiry(applicant, project, parts[2]);

					// Set enquiry ID
					enquiry.setEnquiryId(Integer.parseInt(parts[3]));

					// Set response if exists
					if (!"N/A".equals(parts[4])) {
						enquiry.setResponse(parts[4]);

						// Find responder
						if (!"N/A".equals(parts[5])) {
							User responder = findUserByNRIC(parts[5], allUsers);
							if (responder != null) {
								enquiry.setRespondedBy(responder);
							}
						}

						// Set response date
						if (!"N/A".equals(parts[6])) {
							try {
								enquiry.setResponseDate(dateFormat.parse(parts[6]));
							} catch (ParseException e) {
								// Use current date if parsing fails
								enquiry.setResponseDate(new Date());
							}
						}
					}

					// Set submission date
					try {
						if (parts.length > 7 && !"N/A".equals(parts[7])) {
							enquiry.setSubmissionDate(dateFormat.parse(parts[7]));
						} else {
							enquiry.setSubmissionDate(new Date());
						}
					} catch (ParseException e) {
						// Use current date if parsing fails
						enquiry.setSubmissionDate(new Date());
					}

					enquiries.add(enquiry);
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading enquiries: " + e.getMessage());
		}

		return enquiries;
	}

	/**
	 * Finds a user by their NRIC across all user types.
	 *
	 * @param nric The NRIC to search for
	 * @param allUsers The list of all users to search through
	 * @return The user with the matching NRIC, or null if not found
	 */
	private User findUserByNRIC(String nric, List<User> allUsers) {
		return allUsers.stream()
				.filter(u -> u.getNric().equals(nric))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Finds an applicant by their NRIC.
	 *
	 * @param nric The NRIC to search for
	 * @param applicants The list of applicants to search through
	 * @return The applicant with the matching NRIC, or null if not found
	 */
	private Applicant findApplicantByNRIC(String nric, List<Applicant> applicants) {
		return applicants.stream()
				.filter(a -> a.getNric().equals(nric))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Finds a project by its name.
	 *
	 * @param projectName The project name to search for
	 * @param projects The list of projects to search through
	 * @return The project with the matching name, or null if not found
	 */
	private Project findProjectByName(String projectName, List<Project> projects) {
		return projects.stream()
				.filter(p -> p.getProjectName().equals(projectName))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Loads officer registration data from file and establishes relationships with officers and projects.
	 *
	 * @param officers The list of officers to associate with registrations
	 * @param projects The list of projects to associate with registrations
	 * @return A list of all officer registrations
	 */
	public List<OfficerRegistration> loadOfficerRegistrations(
			List<HDBOfficer> officers,
			List<Project> projects
	) {
		List<OfficerRegistration> registrations = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(OFFICER_REGISTRATION_FILE))) {
			// Skip header
			reader.readLine();

			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split("\t");
				if (parts.length < 3) continue;

				// Find matching officer and project
				HDBOfficer officer = findOfficerByNRIC(parts[0], officers);
				Project project = findProjectByName(parts[1], projects);

				if (officer != null && project != null) {
					// Check if a registration already exists for this officer and project
					boolean registrationExists = false;

					// Look through the project's registrations to see if this officer already has one
					for (OfficerRegistration existingReg : project.getOfficerRegistrations()) {
						if (existingReg.getHdbOfficer() != null &&
								existingReg.getHdbOfficer().getNric().equals(officer.getNric())) {
							// Registration already exists, add it to our return list
							registrations.add(existingReg);
							registrationExists = true;
							break;
						}
					}

					// Only create a new registration if one doesn't already exist
					if (!registrationExists) {
						OfficerRegistration registration = new OfficerRegistration(officer, project);
						registration.setRegistrationStatus(parts[2]);

						registrations.add(registration);

						// Link registration to officer and project
						officer.addRegistration(registration);
						project.addOfficerRegistration(registration);
					}
				}
			}
		} catch (IOException e) {
			System.out.println("Error loading officer registrations: " + e.getMessage());
		}

		return registrations;
	}

	/**
	 * Finds an HDB officer by their NRIC.
	 *
	 * @param nric The NRIC to search for
	 * @param officers The list of officers to search through
	 * @return The officer with the matching NRIC, or null if not found
	 */
	private HDBOfficer findOfficerByNRIC(String nric, List<HDBOfficer> officers) {
		return officers.stream()
				.filter(officer -> officer.getNric().equals(nric))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Loads flat bookings from file
	 *
	 * @param applicants List of loaded applicants for reference linking
	 * @param projects List of loaded projects for reference linking
	 * @return List of all bookings loaded from file
	 */
	public List<FlatBooking> loadBookings(List<Applicant> applicants, List<Project> projects, List<HDBOfficer> officers) {
		List<FlatBooking> bookings = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(BOOKING_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 6) continue; // Need at least 6 fields with status

				// Find matching applicant and project
				Applicant applicant = findApplicantByNRIC(parts[0], applicants);
				Project project = findProjectByName(parts[1], projects);

				if (applicant != null && project != null) {
					// Create booking
					FlatBooking booking = new FlatBooking();
					booking.setApplicant(applicant);
					booking.setProject(project);
					booking.setFlatType(FlatType.valueOf(parts[2]));
					booking.setFlatId(Integer.parseInt(parts[3]));

					// Parse booking date
					try {
						booking.setBookingDate(dateFormat.parse(parts[4]));
					} catch (ParseException e) {
						// Use current date if parsing fails
						booking.setBookingDate(new Date());
					}

					// Set booking status
					booking.setBookingStatus(parts[5]);

					// Set rejection reason if available
					if (parts.length > 6 && !parts[6].equals("N/A")) {
						booking.setRejectionReason(parts[6]);
					}

					// Set processing officer if available (new field)
					if (parts.length > 7 && !parts[7].equals("N/A")) {
						HDBOfficer processedByOfficer = findOfficerByNRIC(parts[7], officers);
						if (processedByOfficer != null) {
							booking.setProcessedByOfficer(processedByOfficer);
						}
					}

					// Link the booking to the applicant
					applicant.setBookedFlat(booking);

					// Update application status if this is an approved booking
					if (booking.isApproved()) {
						ProjectApplication application = applicant.getAppliedProject();
						if (application != null && application.getStatus() == ApplicationStatus.SUCCESSFUL) {
							application.setStatus(ApplicationStatus.BOOKED);
						}
					}

					bookings.add(booking);
				}
			}

			System.out.println("Successfully loaded " + bookings.size() + " bookings from file.");
		} catch (IOException e) {
			System.out.println("Warning: Failed to load bookings data. " + e.getMessage());
		}

		return bookings;
	}

	/**
	 * Loads receipts from file
	 *
	 * @param applicants List of loaded applicants for reference linking
	 * @param projects List of loaded projects for reference linking
	 * @return List of all receipts loaded from file
	 */
	public List<Receipt> loadReceipts(List<Applicant> applicants, List<Project> projects) {
		List<Receipt> receipts = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(RECEIPT_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 6) continue; // Ensure we have all required fields

				// Find matching applicant and project
				Applicant applicant = findApplicantByNRIC(parts[0], applicants);
				Project project = findProjectByName(parts[1], projects);

				if (applicant != null && project != null) {
					// Create receipt
					Receipt receipt = new Receipt(
							parts[0], // Applicant NRIC
							parts[2], // Officer NRIC
							parts[1], // Project Name
							parts[3], // Flat Type
							Integer.parseInt(parts[4]) // Flat ID
					);

					// Parse receipt date
					try {
						receipt.setReceiptDate(dateFormat.parse(parts[5]));
					} catch (ParseException e) {
						// Use current date if parsing fails
						receipt.setReceiptDate(new Date());
					}

					// Set content of the receipt if available and convert placeholders back to newlines
					if (parts.length > 6) {
						receipt.setContent(parts[6].replace("||", "\n"));
					}

					receipts.add(receipt);
				}
			}

			System.out.println("Successfully loaded " + receipts.size() + " receipts from file.");
		} catch (IOException e) {
			System.out.println("Warning: Failed to load receipts data. " + e.getMessage());
		}

		return receipts;
	}

	/**
	 * Loads withdrawal requests from file
	 *
	 * @param applicants List of loaded applicants for reference linking
	 * @param applications List of loaded applications for reference linking
	 * @return List of all withdrawals loaded from file
	 */
	public List<Withdrawal> loadWithdrawals(List<Applicant> applicants, List<ProjectApplication> applications) {
		List<Withdrawal> withdrawals = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(WITHDRAWAL_FILE))) {
			String line;
			// Skip header line
			reader.readLine();

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			while ((line = reader.readLine()) != null) {
				if (line.trim().isEmpty()) continue;

				String[] parts = line.split("\\t");
				if (parts.length < 4) continue; // Need at least 4 fields

				// Find matching applicant and application
				String applicantNRIC = parts[0].trim();
				String projectName = parts[1].trim();

				Applicant applicant = findApplicantByNRIC(applicantNRIC, applicants);
				ProjectApplication application = null;

				// Find matching application
				if (applicant != null) {
					for (ProjectApplication app : applications) {
						if (app.getApplicant().getNric().equals(applicantNRIC) &&
								app.getProject().getProjectName().equals(projectName)) {
							application = app;
							break;
						}
					}

					if (application != null) {
						// Create withdrawal
						Withdrawal withdrawal = new Withdrawal(applicant, application);

						// Set status
						String status = parts[2];
						withdrawal.setStatus(status);

						// Set request date
						try {
							withdrawal.setRequestDate(dateFormat.parse(parts[3]));
						} catch (ParseException e) {
							// Use current date if parsing fails
							withdrawal.setRequestDate(new Date());
						}

						// Update application withdrawal status to match
						application.setWithdrawalStatus(status);

						// If withdrawal is APPROVED, clear the applicant's applied project
						if ("APPROVED".equals(status)) {
							applicant.setAppliedProject(null);
						}

						withdrawals.add(withdrawal);
					}
				}
			}

			System.out.println("Successfully loaded " + withdrawals.size() + " withdrawals from file.");
		} catch (IOException e) {
			System.out.println("Warning: Failed to load withdrawals data. " + e.getMessage());
		}

		return withdrawals;
	}

	/**
	 * Saves the list of applicants to file.
	 *
	 * @param applicants The list of applicants to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveApplicants(List<Applicant> applicants) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICANT_FILE))) {
			// Write header
			writer.write("Name\tNRIC\tAge\tMarital Status\tPassword");
			writer.newLine();

			// Write data
			for (Applicant applicant : applicants) {
				writer.write(String.format("%s\t%s\t%d\t%s\t%s",
						applicant.getName(),
						applicant.getNric(),
						applicant.getAge(),
						applicant.getMaritalStatus().toString(),
						applicant.getPassword()));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save applicants data. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the list of HDB officers to file.
	 *
	 * @param officers The list of officers to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveOfficers(List<HDBOfficer> officers) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(OFFICER_FILE))) {
			// Write header
			writer.write("Name\tNRIC\tAge\tMarital Status\tPassword");
			writer.newLine();

			// Write data
			for (HDBOfficer officer : officers) {
				writer.write(String.format("%s\t%s\t%d\t%s\t%s",
						officer.getName(),
						officer.getNric(),
						officer.getAge(),
						officer.getMaritalStatus().toString(),
						officer.getPassword()));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save officers data. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the list of HDB managers to file.
	 *
	 * @param managers The list of managers to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveManagers(List<HDBManager> managers) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(MANAGER_FILE))) {
			// Write header
			writer.write("Name\tNRIC\tAge\tMarital Status\tPassword");
			writer.newLine();

			// Write data
			for (HDBManager manager : managers) {
				writer.write(String.format("%s\t%s\t%d\t%s\t%s",
						manager.getName(),
						manager.getNric(),
						manager.getAge(),
						manager.getMaritalStatus().toString(),
						manager.getPassword()));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save managers data. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the list of projects to file, including their flat types, units, dates, and assigned staff.
	 *
	 * @param projects The list of projects to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveProjects(List<Project> projects) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROJECT_FILE))) {
			// Write header
			writer.write("Project Name\tNeighborhood\tType 1\tNumber of units for Type 1\tSelling price for Type 1\tType 2\tNumber of units for Type 2\tSelling price for Type 2\tApplication opening date\tApplication closing date\tManager\tTotal Officer Slot\tAssigned Officers");
			writer.newLine();

			for (Project project : projects) {
				// Extract flat types and units
				Map<FlatType, Integer> flatTypeUnits = project.getFlatTypeUnits();
				FlatType[] flatTypes = flatTypeUnits.keySet().toArray(new FlatType[0]);

				FlatType type1 = flatTypes.length > 0 ? flatTypes[0] : FlatType.TWO_ROOM;
				int units1 = flatTypeUnits.getOrDefault(type1, 0);
				int price1 = 0; // Placeholder

				FlatType type2 = flatTypes.length > 1 ? flatTypes[1] : FlatType.THREE_ROOM;
				int units2 = flatTypeUnits.getOrDefault(type2, 0);
				int price2 = 0; // Placeholder

				// Get manager name
				String managerName = "Unknown";
				if (project.getManagerInCharge() != null) {
					managerName = project.getManagerInCharge().getName();
				}

				// Count actually assigned officers with APPROVED status
				int assignedOfficerCount = 0;
				StringBuilder officerNames = new StringBuilder();
				boolean first = true;

				for (OfficerRegistration reg : project.getOfficerRegistrations()) {
					if ("APPROVED".equals(reg.getRegistrationStatus())) {
						if (!first) {
							officerNames.append(",");
						}
						String current_name = reg.getHdbOfficer().getName();
						if (officerNames.toString().contains(current_name)) {
							continue;
						}
						officerNames.append(current_name);
						assignedOfficerCount++;
						first = false;
					}
				}

				// Calculate total officer slots (original max slots)
				int totalOfficerSlots = project.getAvailableHDBOfficerSlots() + assignedOfficerCount;

				// Format and write the project data
				// Save the TOTAL officer slots
				writer.write(String.format("%s\t%s\t%s\t%d\t%d\t%s\t%d\t%d\t%s\t%s\t%s\t%d\t\"%s\"",
						project.getProjectName(),
						project.getNeighborhood(),
						formatFlatType(type1),
						units1,
						price1,
						formatFlatType(type2),
						units2,
						price2,
						DATE_FORMAT.format(project.getApplicationOpenDate()),
						DATE_FORMAT.format(project.getApplicationCloseDate()),
						managerName,
						project.getTotalOfficerSlots(), // New method to get total slots
						officerNames.toString()));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save projects data. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the list of applications to file.
	 *
	 * @param applications The list of applications to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveApplications(List<ProjectApplication> applications) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPLICATION_FILE))) {
			// Write header
			writer.write("Applicant NRIC\tProject Name\tStatus\tWithdrawal Status\tSelected Flat Type");
			writer.newLine();

			// Write data
			for (ProjectApplication app : applications) {
				writer.write(String.format("%s\t%s\t%s\t%s\t%s",
						app.getApplicant().getNric(),
						app.getProject().getProjectName(),
						app.getStatus().toString(),
						app.getWithdrawalStatus() != null ? app.getWithdrawalStatus() : "N/A",
						app.getSelectedFlatType() != null ? app.getSelectedFlatType().toString() : "N/A"
				));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error saving applications: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the list of enquiries to file.
	 *
	 * @param enquiries The list of enquiries to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveEnquiries(List<Enquiry> enquiries) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENQUIRY_FILE))) {
			// Write header
			writer.write("Applicant NRIC\tProject Name\tEnquiry Content\tEnquiry ID\tResponse\tResponder NRIC\tResponse Date\tSubmission Date");
			writer.newLine();

			// Write data
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for (Enquiry enquiry : enquiries) {
				String projectName = enquiry.getProject() != null ? enquiry.getProject().getProjectName() : "N/A";
				String response = enquiry.getResponse() != null ? enquiry.getResponse() : "N/A";
				String responderNRIC = enquiry.getRespondedBy() != null ? enquiry.getRespondedBy().getNric() : "N/A";
				String responseDate = enquiry.getResponseDate() != null ? dateFormat.format(enquiry.getResponseDate()) : "N/A";
				String submissionDate = enquiry.getSubmissionDate() != null ? dateFormat.format(enquiry.getSubmissionDate()) : dateFormat.format(new Date());

				writer.write(String.format("%s\t%s\t%s\t%d\t%s\t%s\t%s\t%s",
						enquiry.getApplicant().getNric(),
						projectName,
						enquiry.getEnquiryContent(),
						enquiry.getEnquiryId(),
						response,
						responderNRIC,
						responseDate,
						submissionDate
				));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error saving enquiries: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves the list of officer registrations to file.
	 *
	 * @param registrations The list of officer registrations to save
	 * @return true if the save was successful, false otherwise
	 */
	public boolean saveOfficerRegistrations(List<OfficerRegistration> registrations) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(OFFICER_REGISTRATION_FILE))) {
			// Write header
			writer.write("Officer NRIC\tProject Name\tRegistration Status");
			writer.newLine();

			// Write data
			for (OfficerRegistration registration : registrations) {
				writer.write(String.format("%s\t%s\t%s",
						registration.getHdbOfficer().getNric(),
						registration.getProject().getProjectName(),
						registration.getRegistrationStatus()
				));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error saving officer registrations: " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves flat bookings to file
	 *
	 * @param bookings List of bookings to save
	 * @return true if successful, false otherwise
	 */
	public boolean saveBookings(List<FlatBooking> bookings) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(BOOKING_FILE))) {
			// Write header
			writer.write("Applicant NRIC\tProject Name\tFlat Type\tFlat ID\tBooking Date\tBooking Status\tRejection Reason\tProcessed By Officer NRIC");
			writer.newLine();

			// Write data
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for (FlatBooking booking : bookings) {
				String rejectionReason = booking.getRejectionReason() != null ? booking.getRejectionReason() : "N/A";
				String officerNRIC = booking.getProcessedByOfficer() != null ? booking.getProcessedByOfficer().getNric() : "N/A";

				writer.write(String.format("%s\t%s\t%s\t%d\t%s\t%s\t%s\t%s",
						booking.getApplicant().getNric(),
						booking.getProject().getProjectName(),
						booking.getFlatType().toString(),
						booking.getFlatId(),
						dateFormat.format(booking.getBookingDate()),
						booking.getBookingStatus(),
						rejectionReason,
						officerNRIC
				));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save bookings data. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Saves receipts to file
	 *
	 * @param receipts List of receipts to save
	 * @return true if successful, false otherwise
	 */
	public boolean saveReceipts(List<Receipt> receipts) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(RECEIPT_FILE))) {
			// Write header
			writer.write("Applicant NRIC\tProject Name\tOfficer NRIC\tFlat Type\tFlat ID\tReceipt Date\tReceipt Content");
			writer.newLine();

			// Write data
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for (Receipt receipt : receipts) {
				writer.write(String.format("%s\t%s\t%s\t%s\t%d\t%s\t%s",
						receipt.getApplicantNric(),
						receipt.getProjectName(),
						receipt.getOfficerNric(),
						receipt.getFlatType(),
						receipt.getFlatId(),
						dateFormat.format(receipt.getReceiptDate()),
						// Change is here - replace newlines with a placeholder
						receipt.getContent() != null ? receipt.getContent().replace("\n", "||") : "N/A"
				));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save receipts data. " + e.getMessage());
			return false;
		}
	}

	/**
	 * Converts a string representation of marital status to the corresponding enum value.
	 *
	 * @param status The string representation of the marital status
	 * @return The corresponding MaritalStatus enum value
	 */
	private MaritalStatus convertToMaritalStatus(String status) {
		if (status.equalsIgnoreCase("Married")) {
			return MaritalStatus.MARRIED;
		} else {
			return MaritalStatus.SINGLE;
		}
	}

	/**
	 * Converts a string representation of flat type to the corresponding enum value.
	 *
	 * @param type The string representation of the flat type
	 * @return The corresponding FlatType enum value
	 */
	private FlatType convertToFlatType(String type) {
		if (type.contains("2-Room") || type.equalsIgnoreCase("TWO_ROOM")) {
			return FlatType.TWO_ROOM;
		} else {
			return FlatType.THREE_ROOM;
		}
	}

	/**
	 * Formats a FlatType enum value to a user-friendly string.
	 *
	 * @param type The FlatType enum value to format
	 * @return A formatted string representation of the flat type
	 */
	private String formatFlatType(FlatType type) {
		switch (type) {
			case TWO_ROOM:
				return "2-Room";
			case THREE_ROOM:
				return "3-Room";
			default:
				return type.toString();
		}
	}

	/**
	 * Creates a map of HDB managers indexed by both name and NRIC for easy lookup.
	 *
	 * @param managers The list of managers to map
	 * @return A map with manager names and NRICs as keys and the corresponding manager objects as values
	 */
	private Map<String, HDBManager> createManagerMap(List<HDBManager> managers) {
		Map<String, HDBManager> map = new HashMap<>();

		for (HDBManager manager : managers) {
			// Use name as the key
			map.put(manager.getName(), manager);
			// Also map by NRIC for backup lookup
			map.put(manager.getNric(), manager);
		}

		return map;
	}

	/**
	 * Creates a map of HDB officers indexed by both name and NRIC for easy lookup.
	 *
	 * @param officers The list of officers to map
	 * @return A map with officer names and NRICs as keys and the corresponding officer objects as values
	 */
	private Map<String, HDBOfficer> createOfficerMap(List<HDBOfficer> officers) {
		Map<String, HDBOfficer> map = new HashMap<>();

		for (HDBOfficer officer : officers) {
			// Use name as the key
			map.put(officer.getName(), officer);
			// Also map by NRIC for backup lookup
			map.put(officer.getNric(), officer);
		}

		return map;
	}

	/**
	 * Finds a manager by name in the manager map, with fallback logic if the exact name is not found.
	 *
	 * @param name The name of the manager to find
	 * @param managerMap The map of managers to search in
	 * @return The found manager, or the first available manager if the name is not found
	 */
	private HDBManager findManagerByName(String name, Map<String, HDBManager> managerMap) {
		// Try to find by name
		HDBManager manager = managerMap.get(name);

		// If not found and we have managers, return the first one
		if (manager == null && !managerMap.isEmpty()) {
			// Get any manager that isn't keyed by an NRIC (which starts with S or T)
			for (Map.Entry<String, HDBManager> entry : managerMap.entrySet()) {
				if (!entry.getKey().matches("^[ST].*")) {
					return entry.getValue();
				}
			}

			// If all else fails, return any manager
			return managerMap.values().iterator().next();
		}
		return manager;
	}

	/**
	 * Finds an officer by name in the officer map, with fallback logic if the exact name is not found.
	 *
	 * @param name The name of the officer to find
	 * @param officerMap The map of officers to search in
	 * @return The found officer, or the first available officer if the name is not found
	 */
	private HDBOfficer findOfficerByName(String name, Map<String, HDBOfficer> officerMap) {
		// Try to find by name
		HDBOfficer officer = officerMap.get(name);

		// If not found and we have officers, return the first one
		if (officer == null && !officerMap.isEmpty()) {
			// Get any officer that isn't keyed by an NRIC (which starts with S or T)
			for (Map.Entry<String, HDBOfficer> entry : officerMap.entrySet()) {
				if (!entry.getKey().matches("^[ST].*")) {
					return entry.getValue();
				}
			}

			// If all else fails, return any officer
			return officerMap.values().iterator().next();
		}

		return officer;
	}

	/**
	 * Saves withdrawal requests to file
	 *
	 * @param withdrawals List of withdrawals to save
	 * @return true if successful, false otherwise
	 */
	public boolean saveWithdrawals(List<Withdrawal> withdrawals) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(WITHDRAWAL_FILE))) {
			// Write header
			writer.write("Applicant NRIC\tProject Name\tStatus\tRequest Date");
			writer.newLine();

			// Write data
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			for (Withdrawal withdrawal : withdrawals) {
				writer.write(String.format("%s\t%s\t%s\t%s",
						withdrawal.getApplicant().getNric(),
						withdrawal.getApplication().getProject().getProjectName(),
						withdrawal.getStatus(),
						dateFormat.format(withdrawal.getRequestDate())
				));
				writer.newLine();
			}

			return true;
		} catch (IOException e) {
			System.out.println("Error: Failed to save withdrawals data. " + e.getMessage());
			return false;
		}
	}
}
