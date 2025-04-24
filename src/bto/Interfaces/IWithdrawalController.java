package bto.Interfaces;

import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import java.util.List;

/**
 * Interface defining the contract for managing withdrawal requests from applicants.
 */
public interface IWithdrawalController {
    
    /**
     * Submits a new withdrawal request for an applicant.
     *
     * @param applicant The applicant requesting the withdrawal
     * @param application The project application to be withdrawn
     * @return The created Withdrawal object, or null if the withdrawal could not be created
     */
    Withdrawal submitWithdrawal(Applicant applicant, ProjectApplication application);
    
    /**
     * Approves a withdrawal request.
     *
     * @param withdrawal The withdrawal to be approved
     * @return true if the approval was successful, false otherwise
     */
    boolean approveWithdrawal(Withdrawal withdrawal);
    
    /**
     * Rejects a withdrawal request.
     *
     * @param withdrawal The withdrawal to be rejected
     * @return true if the rejection was successful, false otherwise
     */
    boolean rejectWithdrawal(Withdrawal withdrawal);
    
    /**
     * Retrieves a withdrawal by the applicant.
     *
     * @param applicant The applicant whose withdrawal is to be retrieved
     * @return The Withdrawal object associated with the applicant, or null if not found
     */
    Withdrawal getWithdrawalByApplicant(Applicant applicant);
    
    /**
     * Adds a withdrawal to the system.
     *
     * @param withdrawal The withdrawal to be added
     */
    void addWithdrawal(Withdrawal withdrawal);
    
    /**
     * Notifies an applicant about their withdrawal status.
     *
     * @param withdrawal The withdrawal containing the notification information
     */
    void notifyApplicantStatus(Withdrawal withdrawal);
    
    /**
     * Gets all withdrawals in the system.
     * 
     * @return List of all withdrawals
     */
    List<Withdrawal> getAllWithdrawals();
    
    /**
     * Updates the internal collection of withdrawals from a loaded list.
     *
     * @param loadedWithdrawals The list of withdrawals loaded from file
     */
    void setWithdrawals(List<Withdrawal> loadedWithdrawals);
}