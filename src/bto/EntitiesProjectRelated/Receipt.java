package bto.EntitiesProjectRelated;

import java.util.Date;
import java.text.SimpleDateFormat;

/**
 * The Receipt class represents a booking receipt for a BTO flat.
 * It contains details about the booking transaction including the applicant,
 * officer who processed the booking, project information, flat details,
 * and the date when the receipt was generated.
 * <p>
 * The class also provides functionality to generate a formatted receipt content
 * that can be displayed or printed for the applicant.
 */
public class Receipt {
	/** The NRIC (National Registration Identity Card) number of the applicant */
	private String applicantNric;

	/** The NRIC (National Registration Identity Card) number of the HDB officer handling this receipt */
	private String officerNric;

	/** The name of the housing project related to this receipt */
	private String projectName;

	/** The type of flat (e.g., 2-room, 3-room, 4-room) associated with this receipt */
	private String flatType;

	/** The specific flat unit identifier within the project */
	private int flatId;

	/** The date when the receipt was issued or generated */
	private Date receiptDate;

	/**
	 * The content or details of the receipt.
	 * Contains transaction information, payment details, or other relevant receipt data.
	 */
	private String content;

	/**
	 * Default constructor for Receipt.
	 * Initializes the receipt date to the current date.
	 */
	public Receipt() {
		this.receiptDate = new Date();
	}

	/**
	 * Parameterized constructor for Receipt.
	 * Creates a receipt with the specified details and initializes the receipt date to the current date.
	 * Automatically generates the content for the receipt.
	 *
	 * @param applicantNric The NRIC of the applicant who booked the flat
	 * @param officerNric The NRIC of the officer who processed the booking
	 * @param projectName The name of the project where the flat is located
	 * @param flatType The type of flat that was booked
	 * @param flatId The ID of the specific flat that was booked
	 */
	public Receipt(String applicantNric, String officerNric, String projectName, String flatType, int flatId) {
		this.applicantNric = applicantNric;
		this.officerNric = officerNric;
		this.projectName = projectName;
		this.flatType = flatType;
		this.flatId = flatId;
		this.receiptDate = new Date();

		// Automatically generate content when creating the receipt
		generateContent();
	}

	/**
	 * Gets the NRIC of the applicant who booked the flat.
	 *
	 * @return The applicant's NRIC
	 */
	public String getApplicantNric() {
		return applicantNric;
	}

	/**
	 * Sets the NRIC of the applicant who booked the flat.
	 *
	 * @param applicantNric The applicant's NRIC to set
	 */
	public void setApplicantNric(String applicantNric) {
		this.applicantNric = applicantNric;
	}

	/**
	 * Gets the NRIC of the officer who processed the booking.
	 *
	 * @return The officer's NRIC
	 */
	public String getOfficerNric() {
		return officerNric;
	}

	/**
	 * Sets the NRIC of the officer who processed the booking.
	 *
	 * @param officerNric The officer's NRIC to set
	 */
	public void setOfficerNric(String officerNric) {
		this.officerNric = officerNric;
	}

	/**
	 * Gets the name of the project where the flat is located.
	 *
	 * @return The project name
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Sets the name of the project where the flat is located.
	 *
	 * @param projectName The project name to set
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * Gets the type of flat that was booked.
	 *
	 * @return The flat type
	 */
	public String getFlatType() {
		return flatType;
	}

	/**
	 * Sets the type of flat that was booked.
	 *
	 * @param flatType The flat type to set
	 */
	public void setFlatType(String flatType) {
		this.flatType = flatType;
	}

	/**
	 * Gets the ID of the specific flat that was booked.
	 *
	 * @return The flat ID
	 */
	public int getFlatId() {
		return flatId;
	}

	/**
	 * Sets the ID of the specific flat that was booked.
	 *
	 * @param flatId The flat ID to set
	 */
	public void setFlatId(int flatId) {
		this.flatId = flatId;
	}

	/**
	 * Gets the date when this receipt was generated.
	 *
	 * @return The receipt date
	 */
	public Date getReceiptDate() {
		return receiptDate;
	}

	/**
	 * Sets the date when this receipt was generated.
	 *
	 * @param receiptDate The receipt date to set
	 */
	public void setReceiptDate(Date receiptDate) {
		this.receiptDate = receiptDate;
	}

	/**
	 * Gets the content of this receipt in a formatted string.
	 *
	 * @return The formatted receipt content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Sets the content of this receipt.
	 *
	 * @param content The receipt content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * Generates the content for this receipt in a formatted, human-readable form.
	 * The content includes applicant details, project information, flat details,
	 * processing officer information, and additional important notes.
	 * <p>
	 * This method is called automatically by the parameterized constructor,
	 * but can also be called manually to regenerate the content if any details change.
	 */
	public void generateContent() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		StringBuilder printableReceipt = new StringBuilder();

		printableReceipt.append("HDB BOOKING RECEIPT\n");
		printableReceipt.append("====================\n");
		printableReceipt.append("Receipt Date: ").append(dateFormat.format(receiptDate)).append("\n\n");

		printableReceipt.append("Applicant Details:\n");
		printableReceipt.append("NRIC: ").append(applicantNric).append("\n\n");

		printableReceipt.append("Project Details:\n");
		printableReceipt.append("Project: ").append(projectName).append("\n");
		printableReceipt.append("Flat Type: ").append(flatType).append("\n");
		printableReceipt.append("Flat ID: ").append(flatId).append("\n\n");

		printableReceipt.append("Processed By:\n");
		printableReceipt.append("Officer NRIC: ").append(officerNric).append("\n\n");

		printableReceipt.append("Important Information:\n");
		printableReceipt.append("1. Please retain this receipt for your records.\n");
		printableReceipt.append("2. For enquiries, contact HDB Customer Service.\n");

		// Set the generated content
		this.content = printableReceipt.toString();
	}
}