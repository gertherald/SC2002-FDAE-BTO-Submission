package bto.Controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import bto.Enums.*;
import bto.EntitiesProjectRelated.*;
import bto.Entities.*;
import bto.Interfaces.*;

/**
 * The BookingController class manages the flat booking process for BTO (Build-To-Order) housing projects.
 * It provides functionality for creating, processing, and tracking bookings, as well as generating
 * receipts for approved bookings.
 * <p>
 * This controller maintains several data structures to track bookings, rejected bookings, and receipts,
 * serving as a central management point for all booking-related operations in the system.
 */
public class BookingController implements IBookingController {
    private Map<String, FlatBooking> bookings; // Simulate a database of bookings
    private Map<String, String> rejectedBookings; // Track rejected bookings and reasons
    private ReceiptGenerator receiptGenerator; // Generates receipt
    private Map<String, Receipt> receipts; // Map that stores database of receipts with Applicant's NRIC as keys

    /**
     * Default constructor for BookingController.
     * Initializes all required data structures for tracking bookings, rejections, and receipts.
     */
    public BookingController() {
        bookings = new HashMap<>();
        rejectedBookings = new HashMap<>();
        receiptGenerator = new ReceiptGenerator();
        receipts = new HashMap<>();
    }

    /**
     * Creates a new flat booking in the system.
     * Used primarily in the officer interface.
     *
     * @param booking The booking to create
     * @return true if the booking was created successfully, false if the booking is invalid or the applicant already has a booking
     */
    public boolean createBooking(FlatBooking booking) {
        if (booking == null || booking.getApplicant() == null) {
            return false;
        }

        // Check if the applicant already has a booking
        String nric = booking.getApplicant().getNric();
        if (bookings.containsKey(nric)) {
            return false; // Applicant already has a booking
        }

        // Set status to approved automatically when created by officer
        booking.setBookingStatus(FlatBooking.STATUS_APPROVED);

        // Store the booking
        bookings.put(nric, booking);

        // Set the booking for the applicant
        booking.getApplicant().setBookedFlat(booking);

        // Update application status if applicable
        ProjectApplication application = booking.getApplicant().getAppliedProject();
        if (application != null && application.getStatus() == ApplicationStatus.SUCCESSFUL) {
            application.setStatus(ApplicationStatus.BOOKED);
        }

        return true;
    }

    /**
     * Processes a booking for an applicant in a specific project for a selected flat type.
     * This method is preserved for compatibility with older code.
     *
     * @param applicant The applicant requesting the booking
     * @param project The project to book a flat in
     * @param flatType The type of flat to book
     * @return The created FlatBooking if successful, null otherwise
     */
    public FlatBooking processBooking(Applicant applicant, Project project, FlatType flatType) {
        // Check if the applicant has a successful application
        ProjectApplication application = applicant.getAppliedProject();

        if (application == null || application.getStatus() != ApplicationStatus.SUCCESSFUL) {
            return null; // No successful application
        }

        // Book a specific flat of the selected type
        int flatId = project.bookFlat(flatType);

        if (flatId == -1) {
            return null; // No available flats of the selected type
        }

        // Create a new booking with the specific flat ID
        FlatBooking booking = new FlatBooking(applicant, project, flatType, flatId);
        booking.setBookingStatus(FlatBooking.STATUS_APPROVED); // Approve immediately

        // Set the booking for the applicant
        applicant.setBookedFlat(booking);

        // Update application status
        application.setStatus(ApplicationStatus.BOOKED);

        // Store in the database
        bookings.put(applicant.getNric(), booking);

        return booking;
    }

    /**
     * Creates a booking from a project application, processed by a specific HDB officer.
     * This is an overloaded version of createBooking used in the officer interface.
     *
     * @param application The project application to create a booking for
     * @param officer The HDB officer processing the booking
     * @return true if the booking was created successfully, false otherwise
     */
    public boolean createBooking(ProjectApplication application, HDBOfficer officer) {
        if (application == null || officer == null) {
            return false;
        }

        Applicant applicant = application.getApplicant();
        Project project = application.getProject();
        FlatType flatType = application.getSelectedFlatType();

        if (applicant == null || project == null || flatType == null) {
            return false;
        }

        // Book a specific flat of the selected type
        int flatId = project.bookFlat(flatType);

        if (flatId == -1) {
            return false; // No available flats of the selected type
        }

        // Create a new booking with the specific flat ID
        FlatBooking booking = new FlatBooking(applicant, project, flatType, flatId);
        booking.setBookingStatus(FlatBooking.STATUS_APPROVED); // Approve immediately
        booking.setProcessedByOfficer(officer);

        // Set the booking for the applicant
        applicant.setBookedFlat(booking);

        // Update application status
        application.setStatus(ApplicationStatus.BOOKED);

        // Store in the database
        bookings.put(applicant.getNric(), booking);

        return true;
    }

    /**
     * Rejects a booking request for a project application with a specified reason.
     * Creates a rejected booking record or updates an existing booking to rejected status.
     *
     * @param application The project application to reject
     * @param rejectionReason The reason for rejecting the booking
     * @return true if the rejection was processed successfully, false otherwise
     */
    public boolean rejectBooking(ProjectApplication application, String rejectionReason) {
        if (application == null || application.getApplicant() == null) {
            return false;
        }

        String nric = application.getApplicant().getNric();

        // Store rejection reason
        rejectedBookings.put(nric, rejectionReason);

        // Check if there's an existing booking
        FlatBooking existingBooking = bookings.get(nric);

        if (existingBooking != null) {
            // Update existing booking with rejection
            existingBooking.setBookingStatus(FlatBooking.STATUS_REJECTED);
            existingBooking.setRejectionReason(rejectionReason);
        } else {
            // Create a rejected booking record
            FlatBooking booking = new FlatBooking();
            booking.setApplicant(application.getApplicant());
            booking.setProject(application.getProject());
            booking.setFlatType(application.getSelectedFlatType());
            booking.setBookingStatus(FlatBooking.STATUS_REJECTED);
            booking.setRejectionReason(rejectionReason);

            // Store in the database
            bookings.put(nric, booking);

            // Set the booking for the applicant
            application.getApplicant().setBookedFlat(booking);
        }

        return true;
    }

    /**
     * Checks if a project application has an approved booking.
     *
     * @param application The project application to check
     * @return true if the application has an approved booking, false otherwise
     */
    public boolean hasApprovedBooking(ProjectApplication application) {
        if (application == null || application.getApplicant() == null) {
            return false;
        }

        String nric = application.getApplicant().getNric();
        FlatBooking booking = bookings.get(nric);

        return booking != null && booking.isApproved();
    }

    /**
     * Gets the booking status for a project application.
     *
     * @param application The project application to get the booking status for
     * @return The booking status, or null if no booking exists
     */
    public String getBookingStatus(ProjectApplication application) {
        if (application == null || application.getApplicant() == null) {
            return null;
        }

        String nric = application.getApplicant().getNric();
        FlatBooking booking = bookings.get(nric);

        return booking != null ? booking.getBookingStatus() : null;
    }

    /**
     * Gets the booking associated with a project application.
     *
     * @param application The project application to get the booking for
     * @return The FlatBooking object if found, null otherwise
     */
    public FlatBooking getBookingForApplication(ProjectApplication application) {
        if (application == null || application.getApplicant() == null) {
            return null;
        }

        String nric = application.getApplicant().getNric();
        return bookings.get(nric);
    }

    /**
     * Gets a list of all bookings in the system.
     *
     * @return A list of all FlatBooking objects
     */
    public List<FlatBooking> getAllBookings() {
        return new ArrayList<>(bookings.values());
    }

    /**
     * Gets a list of all pending bookings in the system.
     *
     * @return A list of FlatBooking objects with pending status
     */
    public List<FlatBooking> getPendingBookings() {
        List<FlatBooking> pendingBookings = new ArrayList<>();

        for (FlatBooking booking : bookings.values()) {
            if (booking.isPending()) {
                pendingBookings.add(booking);
            }
        }

        return pendingBookings;
    }

    /**
     * Gets a list of all approved bookings in the system.
     *
     * @return A list of FlatBooking objects with approved status
     */
    public List<FlatBooking> getApprovedBookings() {
        List<FlatBooking> approvedBookings = new ArrayList<>();

        for (FlatBooking booking : bookings.values()) {
            if (booking.isApproved()) {
                approvedBookings.add(booking);
            }
        }

        return approvedBookings;
    }

    /**
     * Gets a list of all rejected bookings in the system.
     *
     * @return A list of FlatBooking objects with rejected status
     */
    public List<FlatBooking> getRejectedBookings() {
        List<FlatBooking> rejectedBookings = new ArrayList<>();

        for (FlatBooking booking : bookings.values()) {
            if (booking.isRejected()) {
                rejectedBookings.add(booking);
            }
        }

        return rejectedBookings;
    }

    /**
     * Generates and stores a receipt for a booking.
     * If a receipt already exists for the applicant, returns the existing receipt.
     *
     * @param booking The booking to generate a receipt for
     * @return The generated Receipt object if successful, null otherwise
     */
    public Receipt generateAndStoreReceipt(FlatBooking booking) {
        if (booking == null || booking.getApplicant() == null || booking.getProcessedByOfficer() == null) {
            return null;
        }

        String applicantNric = booking.getApplicant().getNric();

        // Check if a receipt already exists for this applicant
        if (receipts.containsKey(applicantNric)) {
            // Return the existing receipt instead of creating a new one
            return receipts.get(applicantNric);
        }

        // Create a new receipt
        String officerNric = booking.getProcessedByOfficer().getNric();
        String projectName = booking.getProject().getProjectName();
        String flatType = booking.getFlatType().toString();
        int flatId = booking.getFlatId();

        Receipt receipt = new Receipt(applicantNric, officerNric, projectName, flatType, flatId);
        String formattedReceipt = receiptGenerator.generateReceipt(booking);
        receipt.setContent(formattedReceipt);

        receipts.put(applicantNric, receipt);

        return receipt;
    }

    /**
     * Checks if a receipt exists for an applicant with the specified NRIC.
     *
     * @param nric The NRIC of the applicant to check
     * @return true if a receipt exists for the applicant, false otherwise
     */
    public boolean hasReceipt(String nric) {
        return receipts.containsKey(nric);
    }

    /**
     * Gets the receipt for an applicant with the specified NRIC.
     *
     * @param nric The NRIC of the applicant
     * @return The Receipt object if found, null otherwise
     */
    public Receipt getReceiptForApplicant(String nric) {
        return receipts.get(nric);
    }

    /**
     * Updates the availability of a flat type in a project by decrementing the available units count.
     *
     * @param project The project to update
     * @param flatType The flat type to update availability for
     * @return true if the update was successful (units available), false otherwise
     */
    public boolean updateFlatAvailability(Project project, FlatType flatType) {
        int availableUnits = project.getFlatTypeUnits().getOrDefault(flatType, 0);

        if (availableUnits > 0) {
            project.updateFlatAvailability(flatType, availableUnits - 1);
            return true;
        }

        return false;
    }

    /**
     * Generates a receipt for a project application.
     * This method retrieves the booking associated with the application and generates a receipt.
     *
     * @param application The project application to generate a receipt for
     * @return true if the receipt was generated successfully, false otherwise
     */
    public boolean generateReceipt(ProjectApplication application) {
        if (application == null || application.getApplicant() == null) {
            return false;
        }

        String nric = application.getApplicant().getNric();
        FlatBooking booking = bookings.get(nric);

        if (booking == null) {
            return false;
        }

        // Generate receipt
        String receipt = receiptGenerator.generateReceipt(booking);
        return receipt != null && !receipt.isEmpty();
    }

    /**
     * Sets the bookings map from a list of FlatBooking objects.
     * Clears the existing map and rebuilds it using the applicant NRIC as keys.
     *
     * @param bookingsList The list of bookings to set
     */
    public void setBookings(List<FlatBooking> bookingsList) {
        this.bookings.clear();
        for (FlatBooking booking : bookingsList) {
            this.bookings.put(booking.getApplicant().getNric(), booking);
        }
    }

    /**
     * Sets the receipts map from a list of Receipt objects.
     * Clears the existing map and rebuilds it using the applicant NRIC as keys.
     *
     * @param receiptsList The list of receipts to set
     */
    public void setReceipts(List<Receipt> receiptsList) {
        this.receipts.clear();
        for (Receipt receipt : receiptsList) {
            this.receipts.put(receipt.getApplicantNric(), receipt);
        }
    }

    /**
     * Gets a list of all receipts in the system.
     *
     * @return A list of all Receipt objects
     */
    public List<Receipt> getAllReceipts() {
        return new ArrayList<>(receipts.values());
    }
}