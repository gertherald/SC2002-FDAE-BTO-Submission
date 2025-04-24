package bto.Controllers;

import java.text.SimpleDateFormat;
import bto.Interfaces.*;
import java.util.Date;
import bto.Enums.*;
import bto.EntitiesProjectRelated.*;
import bto.Entities.*;

/**
 * The ReceiptGenerator class creates formatted receipts for BTO flat bookings.
 * It provides methods to generate receipts from both FlatBooking objects and
 * ProjectApplication objects, creating a standardized receipt format that includes
 * applicant details, project information, and booking specifics.
 */
public class ReceiptGenerator implements IReceiptGenerator {
    /**
     * Default constructor for ReceiptGenerator.
     */
    public ReceiptGenerator() {
    }

    /**
     * Generates a receipt for a flat booking.
     *
     * @param booking The FlatBooking object containing all booking details
     * @return A formatted receipt as a string, or null if the booking is invalid
     */
    public String generateReceipt(FlatBooking booking) {
        if (booking == null) {
            return null;
        }

        return formatReceipt(booking);
    }

    /**
     * Generates a receipt from a project application.
     * Creates a temporary FlatBooking object to use the same formatting logic.
     *
     * @param application The ProjectApplication to generate a receipt for
     * @return true if the receipt was generated successfully, false if the application is invalid
     */
    public boolean generateReceipt(ProjectApplication application) {
        if (application == null || application.getApplicant() == null ||
                application.getProject() == null || application.getSelectedFlatType() == null) {
            return false;
        }

        // Create a temporary booking object for receipt generation
        FlatBooking tempBooking = new FlatBooking(
                application.getApplicant(),
                application.getProject(),
                application.getSelectedFlatType(),
                0  // We don't have a flat ID yet
        );

        String receiptContent = formatReceipt(tempBooking);
        return receiptContent != null && !receiptContent.isEmpty();
    }

    /**
     * Formats the receipt using information from a FlatBooking object.
     * This is an enhanced version of the receipt formatter that includes more detailed information.
     *
     * @param booking The FlatBooking object containing the details to include in the receipt
     * @return A formatted receipt as a string
     */
    private String formatReceipt(FlatBooking booking) {
        User user = booking.getApplicant();
        Project project = booking.getProject();
        FlatType flatType = booking.getFlatType();
        int flatId = booking.getFlatId();
        Date bookingDate = booking.getBookingDate();

        StringBuilder receipt = new StringBuilder();

        // Format current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());
        String bookingDateStr = dateFormat.format(bookingDate);

        receipt.append("======== BOOKING RECEIPT ========\n");
        receipt.append("Receipt Date: ").append(currentDate).append("\n");
        receipt.append("Booking Date: ").append(bookingDateStr).append("\n\n");

        receipt.append("Applicant Information:\n");
        receipt.append("---------------------\n");
        receipt.append("Name: ").append(user.getName()).append("\n");
        receipt.append("NRIC: ").append(user.getNric()).append("\n");
        receipt.append("Age: ").append(user.getAge()).append("\n");
        receipt.append("Marital Status: ").append(user.getMaritalStatus()).append("\n\n");

        receipt.append("Project Information:\n");
        receipt.append("-------------------\n");
        receipt.append("Project Name: ").append(project.getProjectName()).append("\n");
        receipt.append("Neighborhood: ").append(project.getNeighborhood()).append("\n");
        receipt.append("Flat Type: ").append(flatType).append("\n");

        // Processing Officer Details
        HDBOfficer officer = booking.getProcessedByOfficer();
        if (officer != null) {
            receipt.append("Processed By: ").append(officer.getName()).append("\n");
            receipt.append("Officer ID: ").append(officer.getNric()).append("\n\n");
        }

        if (flatId > 0) {
            receipt.append("Flat ID: ").append(flatId).append("\n\n");
        } else {
            receipt.append("\n");
        }

        receipt.append("This receipt confirms your booking of the above flat unit. ");
        receipt.append("Please retain this document for your records.\n\n");

        receipt.append("Important Information:\n");
        receipt.append("--------------------\n");
        receipt.append("1. Further instructions regarding payment will be sent to you separately.\n");
        receipt.append("2. For enquiries, please contact HDB at 1800-123-4567.\n");
        receipt.append("3. Please quote your NRIC and Flat ID in all communications.\n\n");

        receipt.append("Thank you for choosing HDB.");

        return receipt.toString();
    }

    /**
     * Formats a simple receipt using basic user, project, and flat type information.
     * This method is maintained for backward compatibility with older code.
     *
     * @param user The User (applicant) object
     * @param project The Project object
     * @param flatType The type of flat being booked
     * @return A formatted receipt as a string
     */
    public String formatReceipt(User user, Project project, FlatType flatType) {
        StringBuilder receipt = new StringBuilder();

        // Format current date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());

        receipt.append("RECEIPT\n");
        receipt.append("=======\n\n");
        receipt.append("Date: ").append(currentDate).append("\n\n");

        receipt.append("Applicant Information:\n");
        receipt.append("---------------------\n");
        receipt.append("NRIC: ").append(user.getNric()).append("\n");
        receipt.append("Age: ").append(user.getAge()).append("\n");
        receipt.append("Marital Status: ").append(user.getMaritalStatus()).append("\n\n");

        receipt.append("Project Information:\n");
        receipt.append("-------------------\n");
        receipt.append("Project Name: ").append(project.getProjectName()).append("\n");
        receipt.append("Neighborhood: ").append(project.getNeighborhood()).append("\n");
        receipt.append("Flat Type: ").append(flatType).append("\n\n");

        receipt.append("Thank you for your booking. Please keep this receipt for your records.");

        return receipt.toString();
    }
}