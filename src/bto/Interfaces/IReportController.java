package bto.Interfaces;

import bto.EntitiesProjectRelated.*;
import bto.Entities.*;
import java.util.List;

/**
 * Interface defining the contract for generating and formatting reports for the BTO housing system.
 */
public interface IReportController {
    
    /**
     * Generates a report based on the provided report type, filter criteria, and projects.
     *
     * @param reportType The type/title of the report
     * @param criteria The filter criteria to apply to the report
     * @param projects The list of projects to include in the report
     * @return A Report object containing the filtered bookings
     */
    Report generateReport(String reportType, FilterCriteria criteria, List<Project> projects);
    
    /**
     * Formats a report into a human-readable string format.
     *
     * @param report The report to format
     * @return A formatted string representation of the report
     */
    String getFormattedReport(Report report);
    
    /**
     * Returns all reports in the system.
     *
     * @return A list of Report objects
     */
    List<Report> getAllReports();
}