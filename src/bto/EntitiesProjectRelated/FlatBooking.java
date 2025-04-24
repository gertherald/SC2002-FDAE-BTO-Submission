package bto.EntitiesProjectRelated;

import java.util.Date;
import bto.Entities.*;
import bto.Enums.*;

/**
 * The FlatBooking class represents a booking made by an applicant for a specific flat in a BTO project.
 * It tracks booking details such as the applicant, project, flat type, flat ID, booking date, status,
 * and processing officer information.
 * <p>
 * Bookings can have three status types: PENDING, APPROVED, or REJECTED.
 */
public class FlatBooking {
    /** The applicant who submitted this application */
    private Applicant applicant;

    /** The BTO project the application is for */
    private Project project;

    /** The type of flat (e.g., 2-room, 3-room, 4-room) the applicant is applying for */
    private FlatType flatType;

    /**
     * The specific flat unit identifier within the project.
     * Used to track the exact flat unit that has been allocated or selected.
     */
    private int flatId;

    /** The date when the booking appointment was scheduled or completed */
    private Date bookingDate;

    /**
     * The current status of the booking process.
     * May include values like "Pending", "Confirmed", "Rejected", "Cancelled", etc.
     */
    private String bookingStatus;

    /**
     * The reason provided if an application is rejected.
     * Contains explanation for unsuccessful applications to provide feedback to applicants.
     */
    private String rejectionReason;

    /** The HDB officer who processed or is handling this application */
    private HDBOfficer processedByOfficer;

    /**
     * Constant representing an approved booking status.
     */
    public static final String STATUS_APPROVED = "APPROVED";

    /**
     * Constant representing a rejected booking status.
     */
    public static final String STATUS_REJECTED = "REJECTED";

    /**
     * Constant representing a pending booking status.
     */
    public static final String STATUS_PENDING = "PENDING";

    /**
     * Default constructor for FlatBooking.
     * Initializes a new booking with the current date and a PENDING status.
     */
    public FlatBooking() {
        this.bookingDate = new Date(); // Sets current date as booking date
        this.bookingStatus = STATUS_PENDING; // Default status is pending
    }

    /**
     * Parameterized constructor for FlatBooking.
     * Creates a booking for a specific applicant, project, flat type, and flat ID.
     * The booking date is set to the current date and status to PENDING.
     *
     * @param applicant The applicant making the booking
     * @param project The project where the flat is located
     * @param flatType The type of flat being booked
     * @param flatId The specific ID of the flat being booked
     */
    public FlatBooking(Applicant applicant, Project project, FlatType flatType, int flatId) {
        this();
        this.applicant = applicant;
        this.project = project;
        this.flatType = flatType;
        this.flatId = flatId;
    }

    /**
     * Generates a formatted string containing all booking details.
     * If the booking is rejected, includes the rejection reason.
     *
     * @return A formatted string of booking details
     */
    public String getBookingDetails() {
        StringBuilder details = new StringBuilder();
        details.append("Booking Information\n");
        details.append("===================\n");
        details.append("Applicant: ").append(applicant.getName()).append(" (").append(applicant.getNric()).append(")\n");
        details.append("Project: ").append(project.getProjectName()).append("\n");
        details.append("Flat Type: ").append(flatType).append("\n");
        details.append("Flat ID: ").append(flatId).append("\n");
        details.append("Booking Date: ").append(bookingDate).append("\n");
        details.append("Status: ").append(bookingStatus).append("\n");

        if (STATUS_REJECTED.equals(bookingStatus) && rejectionReason != null) {
            details.append("Rejection Reason: ").append(rejectionReason).append("\n");
        }

        return details.toString();
    }

    /**
     * Generates a receipt for the booking.
     * Currently returns the same information as getBookingDetails().
     *
     * @return A formatted receipt for the booking
     */
    public String generateReceipt() {
        return getBookingDetails();
    }

    /**
     * Approves this booking.
     * Changes the booking status to APPROVED.
     */
    public void approve() {
        this.bookingStatus = STATUS_APPROVED;
    }

    /**
     * Rejects this booking with a specified reason.
     * Changes the booking status to REJECTED and records the rejection reason.
     *
     * @param reason The reason for rejection
     */
    public void reject(String reason) {
        this.bookingStatus = STATUS_REJECTED;
        this.rejectionReason = reason;
    }

    /**
     * Checks if the booking is approved.
     *
     * @return true if the booking status is APPROVED, false otherwise
     */
    public boolean isApproved() {
        return STATUS_APPROVED.equals(bookingStatus);
    }

    /**
     * Checks if the booking is rejected.
     *
     * @return true if the booking status is REJECTED, false otherwise
     */
    public boolean isRejected() {
        return STATUS_REJECTED.equals(bookingStatus);
    }

    /**
     * Checks if the booking is pending.
     *
     * @return true if the booking status is PENDING, false otherwise
     */
    public boolean isPending() {
        return STATUS_PENDING.equals(bookingStatus);
    }

    /**
     * Gets the applicant who made this booking.
     *
     * @return The applicant who made the booking
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Sets the applicant for this booking.
     *
     * @param applicant The applicant to set
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Gets the project where the booked flat is located.
     *
     * @return The project associated with the booking
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project for this booking.
     *
     * @param project The project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the type of flat being booked.
     *
     * @return The flat type of the booking
     */
    public FlatType getFlatType() {
        return flatType;
    }

    /**
     * Sets the type of flat for this booking.
     *
     * @param flatType The flat type to set
     */
    public void setFlatType(FlatType flatType) {
        this.flatType = flatType;
    }

    /**
     * Gets the specific ID of the flat being booked.
     *
     * @return The flat ID
     */
    public int getFlatId() {
        return flatId;
    }

    /**
     * Sets the specific ID of the flat for this booking.
     *
     * @param flatId The flat ID to set
     */
    public void setFlatId(int flatId) {
        this.flatId = flatId;
    }

    /**
     * Gets the date when this booking was made.
     *
     * @return The booking date
     */
    public Date getBookingDate() {
        return bookingDate;
    }

    /**
     * Sets the date when this booking was made.
     *
     * @param bookingDate The booking date to set
     */
    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * Gets the current status of this booking.
     *
     * @return The booking status (PENDING, APPROVED, or REJECTED)
     */
    public String getBookingStatus() {
        return bookingStatus;
    }

    /**
     * Sets the status of this booking.
     *
     * @param bookingStatus The booking status to set
     */
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    /**
     * Gets the reason why this booking was rejected, if applicable.
     *
     * @return The rejection reason, or null if the booking is not rejected
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Sets the reason why this booking was rejected.
     *
     * @param rejectionReason The rejection reason to set
     */
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    /**
     * Gets the HDB officer who processed this booking.
     *
     * @return The HDB officer who processed the booking
     */
    public HDBOfficer getProcessedByOfficer() {
        return processedByOfficer;
    }

    /**
     * Sets the HDB officer who processed this booking.
     *
     * @param officer The HDB officer to set
     */
    public void setProcessedByOfficer(HDBOfficer officer) {
        this.processedByOfficer = officer;
    }
}