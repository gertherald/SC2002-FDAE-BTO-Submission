package bto.Controllers;

import bto.EntitiesProjectRelated.*;
import bto.Entities.*;
import bto.Enums.*;
import bto.Interfaces.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 * The ReportController class manages the generation and formatting of reports for the BTO housing system.
 * It provides functionality to create reports with various filter criteria, collect booking data from projects,
 * and format the reports in a human-readable format.
 * <p>
 * This controller applies filters based on project, marital status, age range, and flat type
 * to generate customized reports for HDB managers.
 */
public class ReportController implements IReportController{
    // No need for scanner or manager instance variables since menu handling
    // is now fully delegated to ManagerInterface

    /**
     * Default constructor for ReportController.
     * No initialization needed as reports are generated on demand.
     */
    public ReportController() {
        // No need to maintain a list of reports
    }

    // Core functionality methods

    /**
     * Generates a report based on the provided report type, filter criteria, and projects.
     *
     * @param reportType The type/title of the report
     * @param criteria The filter criteria to apply to the report
     * @param projects The list of projects to include in the report
     * @return A Report object containing the filtered bookings
     */
    public Report generateReport(String reportType, FilterCriteria criteria, List<Project> projects) {
        // Create a new report
        Report report = new Report(reportType, criteria);

        // Collect all relevant bookings from projects
        collectBookings(report, projects, criteria);

        return report;
    }

    /**
     * Collects booking information from projects based on filter criteria and adds them to the report.
     * This method applies filters for project, marital status, age range, and flat type.
     * It ensures each applicant is only included once in the report.
     *
     * @param report The report to add bookings to
     * @param projects The list of projects to collect bookings from
     * @param criteria The filter criteria to apply when collecting bookings
     */
    private void collectBookings(Report report, List<Project> projects, FilterCriteria criteria) {
        // Use a set to track applicants who already have a booking added to the report
        Set<String> addedApplicantNrics = new HashSet<>();

        for (Project project : projects) {
            // Check if project filter is applied
            if (criteria != null && criteria.getCriteria().containsKey("project") &&
                    !criteria.getCriteria().get("project").equals(project)) {
                continue;
            }

            // Get applications with BOOKED status
            for (ProjectApplication application : project.getApplications()) {
                if (application.getStatus() == ApplicationStatus.BOOKED) {
                    Applicant applicant = application.getApplicant();

                    // Skip if this applicant already has a booking in the report
                    if (addedApplicantNrics.contains(applicant.getNric())) {
                        continue;
                    }

                    // Apply marital status filter
                    if (criteria != null && criteria.getCriteria().containsKey("maritalStatus") &&
                            !criteria.getCriteria().get("maritalStatus").equals(applicant.getMaritalStatus())) {
                        continue;
                    }

                    // Apply age filter
                    if (criteria != null && criteria.getCriteria().containsKey("minAge") &&
                            applicant.getAge() < (int)criteria.getCriteria().get("minAge")) {
                        continue;
                    }
                    if (criteria != null && criteria.getCriteria().containsKey("maxAge") &&
                            applicant.getAge() > (int)criteria.getCriteria().get("maxAge")) {
                        continue;
                    }

                    // Apply flat type filter
                    if (criteria != null && criteria.getCriteria().containsKey("flatType") &&
                            !criteria.getCriteria().get("flatType").equals(application.getSelectedFlatType())) {
                        continue;
                    }

                    // Check if applicant already has a booking (from database)
                    FlatBooking existingBooking = applicant.getBookedFlat();

                    if (existingBooking != null) {
                        // Use existing booking
                        report.addBooking(existingBooking);
                    } else {
                        // Create a new temporary booking from the application data
                        FlatBooking booking = new FlatBooking(
                                applicant,
                                project,
                                application.getSelectedFlatType(),
                                0  // Default value for flat ID
                        );
                        report.addBooking(booking);
                    }

                    // Track that this applicant is added
                    addedApplicantNrics.add(applicant.getNric());
                }
            }
        }
    }

    /**
     * Formats a report into a human-readable string format.
     * The formatted report includes a header with the report title and generation date,
     * the filter criteria applied, and a table of booking information.
     *
     * @param report The report to format
     * @return A formatted string representation of the report
     */
    public String getFormattedReport(Report report) {
        if (report == null) {
            return "Invalid report.";
        }

        StringBuilder formattedReport = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Report header
        formattedReport.append("=== " + report.getReportType() + " ===\n");
        formattedReport.append("Generated On: " + dateFormat.format(report.getGenerationDate()) + "\n\n");

        // Filter criteria applied
        formattedReport.append("Filter Criteria:\n");
        if (report.getFilters() != null && !report.getFilters().getCriteria().isEmpty()) {
            for (Map.Entry<String, Object> entry : report.getFilters().getCriteria().entrySet()) {
                formattedReport.append("- " + entry.getKey() + ": ");

                if (entry.getValue() instanceof Project) {
                    formattedReport.append(((Project) entry.getValue()).getProjectName());
                } else if (entry.getValue() instanceof MaritalStatus) {
                    formattedReport.append(entry.getValue().toString());
                } else if (entry.getValue() instanceof FlatType) {
                    formattedReport.append(entry.getValue().toString());
                } else {
                    formattedReport.append(entry.getValue().toString());
                }

                formattedReport.append("\n");
            }
        } else {
            formattedReport.append("- No filters applied (all records)\n");
        }
        formattedReport.append("\n");

        // Booking information
        List<FlatBooking> bookings = report.getBookings();
        if (bookings.isEmpty()) {
            formattedReport.append("No applicants found matching the criteria.\n");
            formattedReport.append("This could be because:\n");
            formattedReport.append("- No applications have been approved and booked\n");
            formattedReport.append("- The applied filters are too restrictive\n");
            formattedReport.append("- No data is available in the system yet\n");
        } else {
            // Include applicant name in the report
            formattedReport.append(String.format("%-15s %-15s %-20s %-5s %-15s\n",
                    "Applicant Name", "Flat Type", "Project Name", "Age", "Marital Status"));
            formattedReport.append("-------------------------------------------------------------------------\n");

            for (FlatBooking booking : bookings) {
                Applicant applicant = booking.getApplicant();
                formattedReport.append(String.format("%-15s %-15s %-20s %-5d %-15s\n",
                        applicant.getName(),
                        booking.getFlatType(),
                        booking.getProject().getProjectName(),
                        applicant.getAge(),
                        applicant.getMaritalStatus()
                ));
            }

            formattedReport.append("\nTotal Bookings: " + bookings.size());
        }

        return formattedReport.toString();
    }

    /**
     * Returns an empty list of reports.
     * This is a stub method that satisfies interface requirements but does not maintain report history.
     * Reports are generated on demand and not stored persistently.
     *
     * @return An empty list of Report objects
     */
    public List<Report> getAllReports() {
        return new ArrayList<>();
    }
}