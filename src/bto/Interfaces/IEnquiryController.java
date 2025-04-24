package bto.Interfaces;

import bto.Entities.*;
import bto.EntitiesProjectRelated.*;
import java.util.List;

/**
 * Interface defining the contract for managing enquiries in the BTO housing system.
 */
public interface IEnquiryController {
    
    /**
     * Creates a new enquiry from an applicant regarding a project.
     *
     * @param applicant The applicant submitting the enquiry
     * @param project The project the enquiry is about (can be null for general enquiries)
     * @param content The content of the enquiry
     * @return The created Enquiry object
     */
    Enquiry createEnquiry(Applicant applicant, Project project, String content);
    
    /**
     * Retrieves all enquiries related to a specific project.
     *
     * @param project The project to get enquiries for
     * @return A list of enquiries for the specified project
     */
    List<Enquiry> getEnquiriesByProject(Project project);
    
    /**
     * Retrieves all enquiries submitted by a specific applicant.
     *
     * @param applicant The applicant to get enquiries for
     * @return A list of enquiries submitted by the specified applicant
     */
    List<Enquiry> getEnquiriesByApplicant(Applicant applicant);
    
    /**
     * Edits the content of an existing enquiry.
     *
     * @param enquiry The enquiry to edit
     * @param newContent The new content for the enquiry
     * @return true if the edit was successful, false otherwise
     */
    boolean editEnquiry(Enquiry enquiry, String newContent);
    
    /**
     * Deletes an existing enquiry.
     *
     * @param enquiry The enquiry to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean deleteEnquiry(Enquiry enquiry);
    
    /**
     * Responds to an enquiry.
     *
     * @param enquiry The enquiry to respond to
     * @param response The response text
     * @param respondedBy The user who is responding
     * @return true if the response was recorded successfully, false otherwise
     */
    boolean respondToEnquiry(Enquiry enquiry, String response, User respondedBy);
    
    /**
     * Retrieves all enquiries in the system.
     *
     * @return A list of all enquiries
     */
    List<Enquiry> getAllEnquiries();
    
    /**
     * Retrieves all pending (unanswered) enquiries in the system.
     *
     * @return A list of enquiries that have not yet been responded to
     */
    List<Enquiry> getPendingEnquiries();
    
    /**
     * Finds an enquiry by its ID.
     *
     * @param enquiryId The ID of the enquiry to find
     * @return The Enquiry object if found, null otherwise
     */
    Enquiry getEnquiryById(int enquiryId);
    
    /**
     * Sets the list of enquiries from an external source.
     *
     * @param enquiries The list of enquiries to set
     */
    void setEnquiries(List<Enquiry> enquiries);
}
