package bto.Interfaces;

import bto.EntitiesProjectRelated.*;
import bto.Entities.*;
import bto.Enums.*;
import java.util.List;
import java.util.Map;

/**
 * Interface defining the contract for managing BTO housing projects.
 */
public interface IProjectController {

    /**
     * Filters projects based on specified criteria.
     *
     * @param criteria The criteria to filter projects by
     * @return A list of projects that match all the specified criteria
     */
    List<Project> filterProjects(FilterCriteria criteria);

    /**
     * Retrieves a project by its name.
     *
     * @param projectName The name of the project to retrieve
     * @return The Project object if found, null otherwise
     */
    Project getProjectByName(String projectName);

    /**
     * Retrieves all projects in the system.
     *
     * @return A list of all Project objects
     */
    List<Project> getAllProjects();

    /**
     * Creates a new project in the system.
     *
     * @param project The project to create
     * @return The created Project object if successful, null otherwise
     */
    Project createProject(Project project);

    /**
     * Edits an existing project with the specified changes.
     *
     * @param project The project to edit
     * @param changes A map of field names to new values
     * @return true if the edit was successful, false otherwise
     */
    boolean editProject(Project project, Map<String, Object> changes);

    /**
     * Deletes a project from the system.
     *
     * @param project The project to delete
     * @return true if the deletion was successful, false otherwise
     */
    boolean deleteProject(Project project);

    /**
     * Toggles the visibility of a project.
     *
     * @param project The project to toggle visibility for
     * @param visible The new visibility status to set
     * @return true if the visibility was set successfully, false otherwise
     */
    boolean toggleVisibility(Project project, boolean visible);

    /**
     * Retrieves all visible projects that the specified user is eligible to apply for.
     *
     * @param user The user to check eligibility for
     * @return A list of visible projects that the user is eligible to apply for
     */
    List<Project> getVisibleProjectsForApplicant(User user);

    /**
     * Retrieves all flat types that a user is eligible to apply for in a specific project.
     *
     * @param project The project to check flat type eligibility for
     * @param user The user to check eligibility for
     * @return A list of FlatType objects that the user is eligible to apply for
     */
    List<FlatType> getEligibleFlatTypes(Project project, User user);

    /**
     * Updates the availability of a specific flat type in a project.
     *
     * @param project The project to update flat availability for
     * @param flatType The type of flat to update
     * @param quantity The new quantity of available flats
     * @return true if the update was successful, false otherwise
     */
    boolean updateFlatAvailability(Project project, FlatType flatType, int quantity);

    /**
     * Gets the map of all projects indexed by project name.
     *
     * @return The map of project name to Project objects
     */
    Map<String, Project> getProjects();

    /**
     * Sets the projects from a list of Project objects.
     *
     * @param projectList The list of projects to set
     */
    void setProjects(List<Project> projectList);
    
    /**
     * Filters projects by neighborhood.
     *
     * @param projects List of projects to filter
     * @param neighborhood The neighborhood to filter by
     * @return A list of projects in the specified neighborhood
     */
    List<Project> filterProjectsByNeighborhood(List<Project> projects, String neighborhood);

    /**
     * Filters projects by flat type, only including projects where the flat type is available.
     *
     * @param projects List of projects to filter
     * @param flatType The flat type to filter by
     * @param user The user to check eligibility for
     * @return A list of projects with the specified flat type available
     */
    List<Project> filterProjectsByFlatType(List<Project> projects, FlatType flatType, User user);

    /**
     * Sorts projects alphabetically by project name.
     *
     * @param projects List of projects to sort
     * @param ascending If true, sort from A to Z, otherwise Z to A
     * @return A sorted list of projects
     */
    List<Project> sortProjectsByName(List<Project> projects, boolean ascending);

    /**
     * Sorts projects alphabetically by neighborhood.
     *
     * @param projects List of projects to sort
     * @param ascending If true, sort from A to Z, otherwise Z to A
     * @return A sorted list of projects
     */
    List<Project> sortProjectsByNeighborhood(List<Project> projects, boolean ascending);
}