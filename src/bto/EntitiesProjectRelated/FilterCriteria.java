package bto.EntitiesProjectRelated;

import bto.Entities.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * The FilterCriteria class provides a flexible way to define and apply filtering criteria
 * for BTO housing entities like projects, applications, and bookings.
 * <p>
 * It stores key-value pairs representing various filtering conditions that can be
 * used by controllers and report generators to filter collections of objects.
 */
public class FilterCriteria {
    /**
     * Stores the filter criteria as key-value pairs.
     * Keys represent the filter field names, and values represent the filter conditions.
     * Values can be of different types (strings, numbers, dates, etc.) depending on the field.
     */
    private Map<String, Object> criteria;

    /**
     * Default constructor for FilterCriteria.
     * Initializes an empty criteria map.
     */
    public FilterCriteria() {
        this.criteria = new HashMap<>();
    }

    /**
     * Adds a criterion to the filter.
     *
     * @param key The name of the criterion (e.g., "project", "flatType", "maritalStatus")
     * @param value The value to filter by (e.g., a Project object, FlatType enum, MaritalStatus enum)
     * @return true indicating the criterion was added successfully
     */
    public boolean addCriterion(String key, Object value) {
        criteria.put(key, value);
        return true;
    }

    /**
     * Removes a criterion from the filter.
     *
     * @param key The name of the criterion to remove
     * @return true if the criterion was removed successfully, false if it didn't exist
     */
    public boolean removeCriterion(String key) {
        if (criteria.containsKey(key)) {
            criteria.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Filters a list of projects to find those that belong to a specific HDB manager.
     *
     * @param allProjects The complete list of projects to filter
     * @param currentManager The HDB manager whose projects should be included in the result
     * @return A filtered list containing only projects managed by the specified manager
     */
    public List<Project> ownProjects(List<Project> allProjects, HDBManager currentManager) {
        List<Project> ownedProjects = new ArrayList<>();

        // Loop through all projects and check if their managerInCharge matches the currentManager
        for (Project project : allProjects) {
            // Here, we're assuming that the project has a 'managerInCharge' field of type 'HDBManager'
            if (project.getManagerInCharge().equals(currentManager)) {
                ownedProjects.add(project);
            }
        }

        return ownedProjects;
    }

    /**
     * Gets the map of all criteria currently defined in this filter.
     *
     * @return A map where keys are criterion names and values are the filter values
     */
    public Map<String, Object> getCriteria() {
        return criteria;
    }

    /**
     * Checks if an object matches all the criteria defined in this filter.
     * This is a placeholder method that currently always returns false.
     *
     * @param obj The object to check against the criteria
     * @return true if the object matches all criteria, false otherwise
     */
    public boolean matches(Object obj) {
        // Implementation to check if object matches criteria
        return false; // Placeholder
    }

    /**
     * Sets the criteria map to a new map.
     * This replaces any existing criteria.
     *
     * @param criteria The new criteria map to set
     */
    public void setCriteria(Map<String, Object> criteria) {
        this.criteria = criteria;
    }
}