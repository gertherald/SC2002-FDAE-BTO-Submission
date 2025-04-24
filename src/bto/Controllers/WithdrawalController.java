package bto.Controllers;

import bto.Entities.*;
import bto.Interfaces.*;
import bto.EntitiesProjectRelated.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class responsible for managing withdrawal requests from applicants.
 * <p>
 * This class handles the creation, approval, rejection, and management of
 * withdrawal applications within the BTO system.
 */
public class WithdrawalController implements IWithdrawalController {
    /** Map to store withdrawals with applicant NRIC as the key. Simulates a database. */
    private Map<String, Withdrawal> withdrawals; // Simulate a database of withdrawals

    /**
     * Constructs a new WithdrawalController with an empty withdrawals map.
     */
    public WithdrawalController() {
        withdrawals = new HashMap<>();
    }

    // Methods
    /**
     * Submits a new withdrawal request for an applicant.
     * <p>
     * Checks if the applicant has a valid application and if a withdrawal
     * has not already been submitted.
     *
     * @param applicant The applicant requesting the withdrawal
     * @param application The project application to be withdrawn
     * @return The created Withdrawal object, or null if the withdrawal could not be created
     */
    public Withdrawal submitWithdrawal(Applicant applicant, ProjectApplication application) {
        // Check if the applicant has an application
        if (application == null || application.getApplicant() != applicant) {
            return null; // No matching application
        }

        // Check if the application already has a withdrawal
        if (application.getWithdrawalStatus() != null) {
            return null; // Already has a withdrawal
        }

        // Create a new withdrawal
        Withdrawal withdrawal = new Withdrawal(applicant, application);

        // Update application withdrawal status
        application.setWithdrawalStatus("PENDING");

        // Store in the database
        withdrawals.put(applicant.getNric(), withdrawal);

        return withdrawal;
    }

    /**
     * Approves a withdrawal request.
     * <p>
     * Updates the withdrawal status, the application status, and removes the
     * application from the applicant's record.
     *
     * @param withdrawal The withdrawal to be approved
     * @return true if the approval was successful, false otherwise
     */
    public boolean approveWithdrawal(Withdrawal withdrawal) {
        if (withdrawal == null) {
            return false;
        }

        // Update withdrawal status
        withdrawal.setStatus("APPROVED");

        // Update application withdrawal status
        withdrawal.getApplication().setWithdrawalStatus("APPROVED");

        // Remove the application from the applicant
        withdrawal.getApplicant().setAppliedProject(null);

        // Notify the applicant
        notifyApplicantStatus(withdrawal);

        return true;
    }

    /**
     * Rejects a withdrawal request.
     * <p>
     * Updates the withdrawal status and the application status.
     *
     * @param withdrawal The withdrawal to be rejected
     * @return true if the rejection was successful, false otherwise
     */
    public boolean rejectWithdrawal(Withdrawal withdrawal) {
        if (withdrawal == null) {
            return false;
        }

        // Update withdrawal status
        withdrawal.setStatus("REJECTED");

        // Update application withdrawal status
        withdrawal.getApplication().setWithdrawalStatus("REJECTED");

        // Notify the applicant
        notifyApplicantStatus(withdrawal);

        return true;
    }

    /**
     * Retrieves a withdrawal by the applicant.
     *
     * @param applicant The applicant whose withdrawal is to be retrieved
     * @return The Withdrawal object associated with the applicant, or null if not found
     */
    public Withdrawal getWithdrawalByApplicant(Applicant applicant) {
        if (applicant == null) {
            return null;
        }
        return withdrawals.get(applicant.getNric());
    }

    /**
     * Adds a withdrawal to the system.
     *
     * @param withdrawal The withdrawal to be added
     */
    public void addWithdrawal(Withdrawal withdrawal) {
        if (withdrawal != null && withdrawal.getApplicant() != null) {
            withdrawals.put(withdrawal.getApplicant().getNric(), withdrawal);
        }
    }

    /**
     * Notifies an applicant about their withdrawal status.
     * <p>
     * This is a placeholder implementation that currently outputs to the console.
     * In a production environment, this would send notifications via email, SMS, or other means.
     *
     * @param withdrawal The withdrawal containing the notification information
     */
    public void notifyApplicantStatus(Withdrawal withdrawal) {
        // Implementation to notify the applicant about the withdrawal status
        // This could be via email, SMS, or other means

        // Placeholder
        System.out.println("Notification sent to " + withdrawal.getApplicant().getNric() +
                " regarding withdrawal status: " + withdrawal.getStatus());
    }

    /**
     * Gets all withdrawals in the system
     * @return List of all withdrawals
     */
    public List<Withdrawal> getAllWithdrawals() {
        return new ArrayList<>(withdrawals.values());
    }

    /**
     * Updates the internal collection of withdrawals from a loaded list
     * Used during system initialization when loading from files
     *
     * @param loadedWithdrawals The list of withdrawals loaded from file
     */
    public void setWithdrawals(List<Withdrawal> loadedWithdrawals) {
        withdrawals.clear();
        for (Withdrawal withdrawal : loadedWithdrawals) {
            if (withdrawal.getApplicant() != null) {
                withdrawals.put(withdrawal.getApplicant().getNric(), withdrawal);
            }
        }
        System.out.println("WithdrawalController initialized with " + withdrawals.size() + " withdrawals.");
    }
}