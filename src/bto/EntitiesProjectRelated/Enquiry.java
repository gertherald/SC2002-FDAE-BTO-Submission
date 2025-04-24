package bto.EntitiesProjectRelated;

import bto.Entities.Applicant;
import bto.Entities.User;
import java.util.Date;

/**
 * The Enquiry class represents a question or request for information submitted by an applicant.
 * Enquiries can be related to a specific project or can be general enquiries about the BTO system.
 * They track submission and response details including dates and respondent information.
 */
public class Enquiry {
    /** Unique identifier for the enquiry */
    private int enquiryId;

    /** The applicant who submitted this enquiry */
    private Applicant applicant;

    /**
     * The specific project this enquiry relates to.
     * Can be null for general enquiries not tied to a specific project.
     */
    private Project project;

    /** The content/question of the enquiry submitted by the applicant */
    private String enquiryContent;

    /** The response provided to the enquiry */
    private String response;

    /** The date and time when the enquiry was submitted */
    private Date submissionDate;

    /** The user/staff member who responded to this enquiry */
    private User respondedBy;

    /** The date and time when the response was provided */
    private Date responseDate;

    /**
     * Flag indicating whether the enquiry has been responded to.
     * True if a response has been provided, false otherwise.
     */
    private boolean isResponded;

    /**
     * Constructor for creating a new enquiry.
     * Sets the submission date to the current date and marks the enquiry as not responded.
     *
     * @param applicant The applicant who submitted the enquiry
     * @param project The project the enquiry is about (can be null for general enquiries)
     * @param enquiryContent The content of the enquiry
     */
    public Enquiry(Applicant applicant, Project project, String enquiryContent) {
        this.applicant = applicant;
        this.project = project;
        this.enquiryContent = enquiryContent;
        this.submissionDate = new Date(); // Set current date
        this.isResponded = false;
    }

    /**
     * Gets the unique identifier for this enquiry.
     *
     * @return The enquiry ID
     */
    public int getEnquiryId() {
        return enquiryId;
    }

    /**
     * Sets the unique identifier for this enquiry.
     *
     * @param enquiryId The enquiry ID to set
     */
    public void setEnquiryId(int enquiryId) {
        this.enquiryId = enquiryId;
    }

    /**
     * Gets the applicant who submitted this enquiry.
     *
     * @return The applicant who submitted the enquiry
     */
    public Applicant getApplicant() {
        return applicant;
    }

    /**
     * Sets the applicant who submitted this enquiry.
     *
     * @param applicant The applicant to set
     */
    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    /**
     * Gets the project this enquiry is about.
     * Can be null for general enquiries not specific to any project.
     *
     * @return The project associated with the enquiry, or null if it's a general enquiry
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets the project this enquiry is about.
     * Can be set to null for general enquiries.
     *
     * @param project The project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets the content of the enquiry.
     *
     * @return The enquiry content
     */
    public String getEnquiryContent() {
        return enquiryContent;
    }

    /**
     * Sets the content of the enquiry.
     *
     * @param enquiryContent The enquiry content to set
     */
    public void setEnquiryContent(String enquiryContent) {
        this.enquiryContent = enquiryContent;
    }

    /**
     * Gets the response to this enquiry.
     *
     * @return The response text, or null if the enquiry has not been responded to
     */
    public String getResponse() {
        return response;
    }

    /**
     * Sets the response to this enquiry.
     * Also updates the isResponded flag and sets the response date to the current date
     * if a non-empty response is provided.
     *
     * @param response The response text to set
     */
    public void setResponse(String response) {
        this.response = response;
        this.isResponded = (response != null && !response.isEmpty());
        if (this.isResponded) {
            this.responseDate = new Date();
        }
    }

    /**
     * Gets the date when the enquiry was submitted.
     *
     * @return The submission date
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Sets the date when the enquiry was submitted.
     *
     * @param submissionDate The submission date to set
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Gets the user (officer or manager) who responded to this enquiry.
     *
     * @return The user who responded, or null if the enquiry has not been responded to
     */
    public User getRespondedBy() {
        return respondedBy;
    }

    /**
     * Sets the user (officer or manager) who responded to this enquiry.
     *
     * @param respondedBy The user who responded
     */
    public void setRespondedBy(User respondedBy) {
        this.respondedBy = respondedBy;
    }

    /**
     * Gets the date when a response was provided.
     *
     * @return The response date, or null if the enquiry has not been responded to
     */
    public Date getResponseDate() {
        return responseDate;
    }

    /**
     * Sets the date when a response was provided.
     *
     * @param responseDate The response date to set
     */
    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }

    /**
     * Checks if this enquiry has been responded to.
     * Returns true if either the isResponded flag is set to true or if a non-empty response exists.
     *
     * @return true if the enquiry has been responded to, false otherwise
     */
    public boolean isResponded() {
        return isResponded || (response != null && !response.isEmpty());
    }

    /**
     * Sets the responded status of this enquiry.
     *
     * @param bool The responded status to set
     */
    public void setResponded(boolean bool) {
        this.isResponded = bool;
    }

    /**
     * Gets the enquiry content.
     * This method is provided for backward compatibility with code that expects a getMessage() method.
     *
     * @return The enquiry content
     */
    public String getMessage() {
        return enquiryContent;
    }

    /**
     * Gets a subject line for this enquiry.
     * For project-specific enquiries, includes the project name.
     * For general enquiries, returns "Enquiry about General Topics".
     *
     * @return A subject line for the enquiry
     */
    public String getSubject() {
        return "Enquiry about " + (project != null ? project.getProjectName() : "General Topics");
    }

    /**
     * Gets the applicant who submitted this enquiry as a User object.
     * This method is provided for backward compatibility with ManagerInterface.
     *
     * @return The applicant who submitted the enquiry as a User object
     */
    public User getSender() {
        // For backward compatibility with ManagerInterface
        return applicant;
    }
}