package bto.Interfaces;

import bto.Enums.*;
import bto.EntitiesProjectRelated.*;
import bto.Entities.*;

/**
 * Interface defining the contract for generating receipts for BTO flat bookings.
 */
public interface IReceiptGenerator {
    
    /**
     * Generates a receipt for a flat booking.
     *
     * @param booking The FlatBooking object containing all booking details
     * @return A formatted receipt as a string, or null if the booking is invalid
     */
    String generateReceipt(FlatBooking booking);
    
    /**
     * Generates a receipt from a project application.
     *
     * @param application The ProjectApplication to generate a receipt for
     * @return true if the receipt was generated successfully, false if the application is invalid
     */
    boolean generateReceipt(ProjectApplication application);
    
    /**
     * Formats a simple receipt using basic user, project, and flat type information.
     *
     * @param user The User (applicant) object
     * @param project The Project object
     * @param flatType The type of flat being booked
     * @return A formatted receipt as a string
     */
    String formatReceipt(User user, Project project, FlatType flatType);
}