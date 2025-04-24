package bto.Interfaces;

import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import bto.Enums.*;
import java.util.List;

/**
 * Interface defining the contract for managing flat bookings for BTO housing projects.
 */
public interface IBookingController {
    
    /**
     * Creates a new flat booking in the system.
     *
     * @param booking The booking to create
     * @return true if the booking was created successfully, false otherwise
     */
    boolean createBooking(FlatBooking booking);
    
    /**
     * Processes a booking for an applicant in a specific project for a selected flat type.
     *
     * @param applicant The applicant requesting the booking
     * @param project The project to book a flat in
     * @param flatType The type of flat to book
     * @return The created FlatBooking if successful, null otherwise
     */
    FlatBooking processBooking(Applicant applicant, Project project, FlatType flatType);
    
    /**
     * Creates a booking from a project application, processed by a specific HDB officer.
     *
     * @param application The project application to create a booking for
     * @param officer The HDB officer processing the booking
     * @return true if the booking was created successfully, false otherwise
     */
    boolean createBooking(ProjectApplication application, HDBOfficer officer);
    
    /**
     * Rejects a booking request for a project application with a specified reason.
     *
     * @param application The project application to reject
     * @param rejectionReason The reason for rejecting the booking
     * @return true if the rejection was processed successfully, false otherwise
     */
    boolean rejectBooking(ProjectApplication application, String rejectionReason);
    
    /**
     * Checks if a project application has an approved booking.
     *
     * @param application The project application to check
     * @return true if the application has an approved booking, false otherwise
     */
    boolean hasApprovedBooking(ProjectApplication application);
    
    /**
     * Gets the booking status for a project application.
     *
     * @param application The project application to get the booking status for
     * @return The booking status, or null if no booking exists
     */
    String getBookingStatus(ProjectApplication application);
    
    /**
     * Gets the booking associated with a project application.
     *
     * @param application The project application to get the booking for
     * @return The FlatBooking object if found, null otherwise
     */
    FlatBooking getBookingForApplication(ProjectApplication application);
    
    /**
     * Gets a list of all bookings in the system.
     *
     * @return A list of all FlatBooking objects
     */
    List<FlatBooking> getAllBookings();
    
    /**
     * Gets a list of all pending bookings in the system.
     *
     * @return A list of FlatBooking objects with pending status
     */
    List<FlatBooking> getPendingBookings();
    
    /**
     * Gets a list of all approved bookings in the system.
     *
     * @return A list of FlatBooking objects with approved status
     */
    List<FlatBooking> getApprovedBookings();
    
    /**
     * Gets a list of all rejected bookings in the system.
     *
     * @return A list of FlatBooking objects with rejected status
     */
    List<FlatBooking> getRejectedBookings();
    
    /**
     * Generates and stores a receipt for a booking.
     *
     * @param booking The booking to generate a receipt for
     * @return The generated Receipt object if successful, null otherwise
     */
    Receipt generateAndStoreReceipt(FlatBooking booking);
    
    /**
     * Checks if a receipt exists for an applicant with the specified NRIC.
     *
     * @param nric The NRIC of the applicant to check
     * @return true if a receipt exists for the applicant, false otherwise
     */
    boolean hasReceipt(String nric);
    
    /**
     * Gets the receipt for an applicant with the specified NRIC.
     *
     * @param nric The NRIC of the applicant
     * @return The Receipt object if found, null otherwise
     */
    Receipt getReceiptForApplicant(String nric);
    
    /**
     * Updates the availability of a flat type in a project.
     *
     * @param project The project to update
     * @param flatType The flat type to update availability for
     * @return true if the update was successful, false otherwise
     */
    boolean updateFlatAvailability(Project project, FlatType flatType);
    
    /**
     * Generates a receipt for a project application.
     *
     * @param application The project application to generate a receipt for
     * @return true if the receipt was generated successfully, false otherwise
     */
    boolean generateReceipt(ProjectApplication application);
    
    /**
     * Sets the bookings from a list of FlatBooking objects.
     *
     * @param bookingsList The list of bookings to set
     */
    void setBookings(List<FlatBooking> bookingsList);
    
    /**
     * Sets the receipts from a list of Receipt objects.
     *
     * @param receiptsList The list of receipts to set
     */
    void setReceipts(List<Receipt> receiptsList);
    
    /**
     * Gets a list of all receipts in the system.
     *
     * @return A list of all Receipt objects
     */
    List<Receipt> getAllReceipts();
}