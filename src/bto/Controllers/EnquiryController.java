package bto.Controllers;

import bto.Entities.*;
import bto.Interfaces.*;
import bto.EntitiesProjectRelated.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The EnquiryController class manages enquiries in the BTO housing system.
 * It provides functionality for creating, retrieving, editing, deleting, and responding to
 * enquiries submitted by applicants regarding projects or general information.
 * <p>
 * This controller maintains collections of enquiries for efficient retrieval by
 * applicant, project, or enquiry ID, and tracks which enquiries have been responded to.
 */
public class EnquiryController implements IEnquiryController{
    private Map<String, List<Enquiry>> enquiriesByApplicant;
    private List<Enquiry> allEnquiries;
    private int nextEnquiryId = 1;

    /**
     * Default constructor for EnquiryController.
     * Initializes the collections used to store and track enquiries.
     */
    public EnquiryController() {
        enquiriesByApplicant = new HashMap<>();
        allEnquiries = new ArrayList<>();
    }

    /**
     * Creates a new enquiry from an applicant regarding a project.
     * The enquiry is added to the controller's collections and to the project's list of enquiries.
     *
     * @param applicant The applicant submitting the enquiry
     * @param project The project the enquiry is about (can be null for general enquiries)
     * @param content The content of the enquiry
     * @return The created Enquiry object
     */
    public Enquiry createEnquiry(Applicant applicant, Project project, String content) {
        Enquiry enquiry = new Enquiry(applicant, project, content);
        enquiry.setEnquiryId(nextEnquiryId++);

        // Add to project's enquiries if project is not null
        if (project != null) {
            project.addEnquiry(enquiry);
        }

        // Add to our collections
        allEnquiries.add(enquiry);

        if (!enquiriesByApplicant.containsKey(applicant.getNric())) {
            enquiriesByApplicant.put(applicant.getNric(), new ArrayList<>());
        }
        enquiriesByApplicant.get(applicant.getNric()).add(enquiry);

        return enquiry;
    }

    /**
     * Retrieves all enquiries related to a specific project.
     *
     * @param project The project to get enquiries for
     * @return A list of enquiries for the specified project, or an empty list if the project is null
     */
    public List<Enquiry> getEnquiriesByProject(Project project) {
        if (project == null) return new ArrayList<>();
        return project.getEnquiries();
    }

    /**
     * Retrieves all enquiries submitted by a specific applicant.
     *
     * @param applicant The applicant to get enquiries for
     * @return A list of enquiries submitted by the specified applicant, or an empty list if the applicant is null
     */
    public List<Enquiry> getEnquiriesByApplicant(Applicant applicant) {
        if (applicant == null) return new ArrayList<>();
        return enquiriesByApplicant.getOrDefault(applicant.getNric(), new ArrayList<>());
    }

    /**
     * Edits the content of an existing enquiry.
     * Enquiries can only be edited if they have not yet been responded to.
     *
     * @param enquiry The enquiry to edit
     * @param newContent The new content for the enquiry
     * @return true if the edit was successful, false if the enquiry is invalid, not found, or already responded to
     */
    public boolean editEnquiry(Enquiry enquiry, String newContent) {
        if (enquiry == null || !allEnquiries.contains(enquiry)) {
            return false;
        }

        // Can't edit if already responded to
        if (enquiry.isResponded()) {
            return false;
        }

        // Update enquiry content
        enquiry.setEnquiryContent(newContent);

        return true;
    }

    /**
     * Deletes an existing enquiry.
     * The enquiry is removed from the controller's collections and from the project's list of enquiries.
     *
     * @param enquiry The enquiry to delete
     * @return true if the deletion was successful, false if the enquiry is invalid or not found
     */
    public boolean deleteEnquiry(Enquiry enquiry) {
        if (enquiry == null || !allEnquiries.contains(enquiry)) {
            return false;
        }

        // Remove from the project's enquiries if project is not null
        if (enquiry.getProject() != null) {
            enquiry.getProject().getEnquiries().remove(enquiry);
        }

        // Remove from our collections
        allEnquiries.remove(enquiry);

        Applicant applicant = enquiry.getApplicant();
        if (enquiriesByApplicant.containsKey(applicant.getNric())) {
            enquiriesByApplicant.get(applicant.getNric()).remove(enquiry);
            return true;
        }

        return false;
    }

    /**
     * Responds to an enquiry.
     * Sets the response text and the user who responded.
     *
     * @param enquiry The enquiry to respond to
     * @param response The response text
     * @param respondedBy The user (officer or manager) who is responding
     * @return true if the response was recorded successfully, false if the enquiry is invalid or not found
     */
    public boolean respondToEnquiry(Enquiry enquiry, String response, User respondedBy) {
        if (enquiry == null || !allEnquiries.contains(enquiry)) {
            return false;
        }

        // Set the response and respondent
        enquiry.setResponse(response);
        enquiry.setRespondedBy(respondedBy);

        return true;
    }

    /**
     * Retrieves all enquiries in the system.
     *
     * @return A list of all enquiries
     */
    public List<Enquiry> getAllEnquiries() {
        return allEnquiries;
    }

    /**
     * Retrieves all pending (unanswered) enquiries in the system.
     *
     * @return A list of enquiries that have not yet been responded to
     */
    public List<Enquiry> getPendingEnquiries() {
        List<Enquiry> pendingEnquiries = new ArrayList<>();

        for (Enquiry enquiry : allEnquiries) {
            if (!enquiry.isResponded()) {
                pendingEnquiries.add(enquiry);
            }
        }

        return pendingEnquiries;
    }

    /**
     * Finds an enquiry by its ID.
     *
     * @param enquiryId The ID of the enquiry to find
     * @return The Enquiry object if found, null otherwise
     */
    public Enquiry getEnquiryById(int enquiryId) {
        for (Enquiry enquiry : allEnquiries) {
            if (enquiry.getEnquiryId() == enquiryId) {
                return enquiry;
            }
        }
        return null;
    }

    /**
     * Sets the list of enquiries from an external source (e.g., for loading from persistence).
     * Clears existing collections and rebuilds them using the provided enquiries.
     * Updates the next enquiry ID to avoid conflicts.
     *
     * @param enquiries The list of enquiries to set
     */
    public void setEnquiries(List<Enquiry> enquiries) {
        this.allEnquiries.clear();
        this.enquiriesByApplicant.clear();

        for (Enquiry enquiry : enquiries) {
            // Ensure enquiry has an ID
            if (enquiry.getEnquiryId() >= nextEnquiryId) {
                nextEnquiryId = enquiry.getEnquiryId() + 1;
            }

            this.allEnquiries.add(enquiry);

            Applicant applicant = enquiry.getApplicant();
            if (!this.enquiriesByApplicant.containsKey(applicant.getNric())) {
                this.enquiriesByApplicant.put(applicant.getNric(), new ArrayList<>());
            }
            this.enquiriesByApplicant.get(applicant.getNric()).add(enquiry);
        }
    }
}