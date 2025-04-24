package bto.Interfaces;

import bto.EntitiesProjectRelated.*;
import bto.Entities.*;
import java.util.List;

/**
 * Interface defining the contract for managing officer registrations for BTO housing projects.
 */
public interface IRegistrationController {
    
    /**
     * Registers an officer for a project.
     *
     * @param officer The HDBOfficer to register
     * @param project The Project to register for
     * @return The created OfficerRegistration object, or null if no slots available
     */
    OfficerRegistration registerOfficer(HDBOfficer officer, Project project);
    
    /**
     * Approves an officer registration for a project.
     *
     * @param registration The OfficerRegistration to approve
     * @return true if successful, false if the registration is invalid
     */
    boolean approveRegistration(OfficerRegistration registration);
    
    /**
     * Rejects an officer registration for a project.
     *
     * @param registration The OfficerRegistration to reject
     * @return true if successful, false if the registration is invalid
     */
    boolean rejectRegistration(OfficerRegistration registration);
    
    /**
     * Retrieves all registrations for a specific officer.
     *
     * @param officer The HDBOfficer whose registrations to view
     * @return List of OfficerRegistration objects for the specified officer
     */
    List<OfficerRegistration> viewRegistrationStatus(HDBOfficer officer);
    
    /**
     * Retrieves all officer registrations in the system.
     *
     * @return List of all OfficerRegistration objects
     */
    List<OfficerRegistration> getAllRegistrations();
    
    /**
     * Updates the available officer slots for a project.
     *
     * @param project The Project to update
     * @return true if slots were updated, false if no slots available
     */
    boolean updateProjectSlots(Project project);
    
    /**
     * Sets the registrations from a list of OfficerRegistration objects.
     *
     * @param registrationList The list of OfficerRegistration objects to set
     */
    void setRegistrations(List<OfficerRegistration> registrationList);
}
