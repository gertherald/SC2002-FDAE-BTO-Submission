package bto.Controllers;

import bto.EntitiesProjectRelated.*;
import bto.Interfaces.*;
import bto.Entities.*;
import bto.Enums.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ProjectController class manages BTO (Build-To-Order) housing projects in the system.
 * It provides functionality for creating, retrieving, filtering, editing, and deleting projects,
 * as well as managing project visibility and flat availability.
 * <p>
 * This controller maintains a map of all projects indexed by their names for efficient retrieval
 * and provides methods to filter projects based on various criteria.
 */
public class ProjectController implements IProjectController {
    private Map<String, Project> projects; // Map of project name to Project

    /**
     * Default constructor for ProjectController.
     * Initializes an empty map to store projects.
     */
    public ProjectController() {
        projects = new HashMap<>();
    }

    /**
     * Filters projects based on specified criteria.
     * Projects that match all criteria in the FilterCriteria object are included in the result.
     *
     * @param criteria The criteria to filter projects by
     * @return A list of projects that match all the specified criteria
     */
    public List<Project> filterProjects(FilterCriteria criteria) {
        List<Project> filtered = new ArrayList<>();

        for (Project project : projects.values()) {
            boolean match = true;

            for (Map.Entry<String, Object> entry : criteria.getCriteria().entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (key.equals("isVisible") && (boolean) value != project.isVisible()) {
                    match = false;
                    break;
                }

                // Add more filter criteria as needed
            }

            if (match) {
                filtered.add(project);
            }
        }

        return filtered;
    }

    /**
     * Retrieves a project by its name.
     *
     * @param projectName The name of the project to retrieve
     * @return The Project object if found, null otherwise
     */
    public Project getProjectByName(String projectName) {
        return projects.get(projectName);
    }

    /**
     * Retrieves all projects in the system.
     *
     * @return A list of all Project objects
     */
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects.values());
    }

    /**
     * Creates a new project in the system.
     * The project is only created if it has a unique name.
     *
     * @param project The project to create
     * @return The created Project object if successful, null if the project is invalid or a project with the same name already exists
     */
    public Project createProject(Project project) {
        if (project != null && !projects.containsKey(project.getProjectName())) {
            projects.put(project.getProjectName(), project);
            return project;
        }
        return null;
    }

    /**
     * Edits an existing project with the specified changes.
     * Supports changing neighborhood, visibility, and application dates.
     *
     * @param project The project to edit
     * @param changes A map of field names to new values
     * @return true if the edit was successful, false if the project is invalid or not found
     */
    public boolean editProject(Project project, Map<String, Object> changes) {
        if (project != null && projects.containsKey(project.getProjectName())) {
            // Apply changes to project
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                switch (key) {
                    case "neighborhood":
                        project.setNeighborhood((String) value);
                        break;
                    case "isVisible":
                        project.setVisible((boolean) value);
                        break;
                    case "applicationOpenDate":
                        project.setApplicationOpenDate((java.util.Date) value);
                        break;
                    case "applicationCloseDate":
                        project.setApplicationCloseDate((java.util.Date) value);
                        break;
                    // Add more fields as needed
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Deletes a project from the system.
     *
     * @param project The project to delete
     * @return true if the deletion was successful, false if the project is invalid or not found
     */
    public boolean deleteProject(Project project) {
        if (project != null && projects.containsKey(project.getProjectName())) {
            projects.remove(project.getProjectName());
            return true;
        }

        return false;
    }

    /**
     * Toggles the visibility of a project.
     * Visible projects can be seen and applied to by eligible applicants.
     *
     * @param project The project to toggle visibility for
     * @param visible The new visibility status to set
     * @return true if the visibility was set successfully, false if the project is invalid or not found
     */
    public boolean toggleVisibility(Project project, boolean visible) {
        if (project != null && projects.containsKey(project.getProjectName())) {
            project.setVisible(visible);
            return true;
        }

        return false;
    }

    /**
     * Retrieves all visible projects that the specified user is eligible to apply for.
     * A project is considered eligible if the user qualifies for at least one flat type in the project.
     *
     * @param user The user to check eligibility for
     * @return A list of visible projects that the user is eligible to apply for
     */
    public List<Project> getVisibleProjectsForApplicant(User user) {
        List<Project> visibleProjects = new ArrayList<>();

        for (Project project : projects.values()) {
            // First check if project is visible (toggled "on" by managers)
            if (!project.isVisible()) {
                continue;
            }

            // Then check if user is eligible for any flat type in the project
            List<FlatType> eligibleTypes = project.getEligibleFlatTypes(user);
            if (!eligibleTypes.isEmpty()) {
                visibleProjects.add(project);
            }
        }

        return visibleProjects;
    }

    /**
     * Retrieves all flat types that a user is eligible to apply for in a specific project.
     * The eligibility is determined based on the user's age, marital status, and other factors.
     *
     * @param project The project to check flat type eligibility for
     * @param user The user to check eligibility for
     * @return A list of FlatType objects that the user is eligible to apply for
     */
    public List<FlatType> getEligibleFlatTypes(Project project, User user) {
        if (project != null && user != null) {
            return project.getEligibleFlatTypes(user);
        }

        return new ArrayList<>();
    }

    /**
     * Updates the availability of a specific flat type in a project.
     *
     * @param project The project to update flat availability for
     * @param flatType The type of flat to update
     * @param quantity The new quantity of available flats
     * @return true if the update was successful, false if the project is invalid or not found
     */
    public boolean updateFlatAvailability(Project project, FlatType flatType, int quantity) {
        if (project != null && projects.containsKey(project.getProjectName())) {
            return project.updateFlatAvailability(flatType, quantity);
        }

        return false;
    }

    /**
     * Gets the map of all projects indexed by project name.
     *
     * @return The map of project name to Project objects
     */
    public Map<String, Project> getProjects() {
        return projects;
    }

    /**
     * Sets the projects map from a list of Project objects.
     * Clears the existing map and rebuilds it using the project names as keys.
     *
     * @param projectList The list of projects to set
     */
    public void setProjects(List<Project> projectList) {
        this.projects.clear();

        for (Project project : projectList) {
            this.projects.put(project.getProjectName(), project);
        }
    }
    
    /**
     * Filters projects by neighborhood.
     *
     * @param projects List of projects to filter
     * @param neighborhood The neighborhood to filter by
     * @return A list of projects in the specified neighborhood
     */
    public List<Project> filterProjectsByNeighborhood(List<Project> projects, String neighborhood) {
        List<Project> filtered = new ArrayList<>();
        
        for (Project project : projects) {
            if (project.getNeighborhood().equals(neighborhood)) {
                filtered.add(project);
            }
        }
        
        return filtered;
    }

    /**
     * Filters projects by flat type, only including projects where the flat type is available.
     *
     * @param projects List of projects to filter
     * @param flatType The flat type to filter by
     * @param user The user to check eligibility for
     * @return A list of projects with the specified flat type available
     */
    public List<Project> filterProjectsByFlatType(List<Project> projects, FlatType flatType, User user) {
        List<Project> filtered = new ArrayList<>();
        
        for (Project project : projects) {
            Map<FlatType, Integer> flatTypeUnits = project.getFlatTypeUnits();
            
            if (flatTypeUnits.containsKey(flatType) && 
                flatTypeUnits.get(flatType) > 0 && 
                project.getAvailableFlatCount(flatType) > 0) {
                
                // Check if the user is eligible for this flat type
                List<FlatType> eligibleTypes = project.getEligibleFlatTypes(user);
                if (eligibleTypes.contains(flatType)) {
                    filtered.add(project);
                }
            }
        }
        
        return filtered;
    }

    /**
     * Sorts projects alphabetically by project name.
     *
     * @param projects List of projects to sort
     * @param ascending If true, sort from A to Z, otherwise Z to A
     * @return A sorted list of projects
     */
    public List<Project> sortProjectsByName(List<Project> projects, boolean ascending) {
        List<Project> sorted = new ArrayList<>(projects);
        
        if (ascending) {
            Collections.sort(sorted, Comparator.comparing(Project::getProjectName));
        } else {
            Collections.sort(sorted, Comparator.comparing(Project::getProjectName).reversed());
        }
        
        return sorted;
    }

    /**
     * Sorts projects alphabetically by neighborhood.
     *
     * @param projects List of projects to sort
     * @param ascending If true, sort from A to Z, otherwise Z to A
     * @return A sorted list of projects
     */
    public List<Project> sortProjectsByNeighborhood(List<Project> projects, boolean ascending) {
        List<Project> sorted = new ArrayList<>(projects);
        
        if (ascending) {
            Collections.sort(sorted, Comparator.comparing(Project::getNeighborhood));
        } else {
            Collections.sort(sorted, Comparator.comparing(Project::getNeighborhood).reversed());
        }
        
        return sorted;
    }
}