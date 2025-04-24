package bto.EntitiesProjectRelated;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a report for flat bookings with configurable filtering and tracking.
 */
public class Report {
    /** Type of the report being generated */
    private String reportType;

    /** List of flat bookings included in the report */
    private List<FlatBooking> bookings;

    /** Filtering criteria applied to the report */
    private FilterCriteria filters;

    /** Date when the report was generated */
    private Date generationDate;

    /**
     * Default constructor initializes an empty list of bookings
     * and sets the generation date to the current time.
     */
    public Report() {
        this.bookings = new ArrayList<>();
        this.generationDate = new Date();
    }

    /**
     * Parameterized constructor creates a report with a specific type and filter criteria.
     *
     * @param reportType The type of report to be generated
     * @param filters The filtering criteria to be applied to the report
     */
    public Report(String reportType, FilterCriteria filters) {
        this();
        this.reportType = reportType;
        this.filters = filters;
    }

    /** Gets the type of the report */
    public String getReportType() {
        return reportType;
    }

    /**
     * Sets the type of the report.
     *
     * @param reportType The report type to be set
     */
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    /** Gets the list of flat bookings in the report */
    public List<FlatBooking> getBookings() {
        return bookings;
    }

    /**
     * Sets the list of flat bookings for the report.
     *
     * @param bookings The list of bookings to be set
     */
    public void setBookings(List<FlatBooking> bookings) {
        this.bookings = bookings;
    }

    /** Gets the filtering criteria applied to the report */
    public FilterCriteria getFilters() {
        return filters;
    }

    /**
     * Sets the filtering criteria for the report.
     *
     * @param filters The filter criteria to be applied
     */
    public void setFilters(FilterCriteria filters) {
        this.filters = filters;
    }

    /** Gets the date when the report was generated */
    public Date getGenerationDate() {
        return generationDate;
    }

    /**
     * Sets the generation date of the report.
     *
     * @param generationDate The date when the report was generated
     */
    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    /**
     * Adds a new booking to the report if it's not already present.
     *
     * @param booking The flat booking to be added to the report
     */
    public void addBooking(FlatBooking booking) {
        if (!bookings.contains(booking)) {
            bookings.add(booking);
        }
    }

    /**
     * Provides a string representation of the report.
     *
     * @return A string describing the report's key details
     */
    @Override
    public String toString() {
        return "Report [type=" + reportType + ", filters=" + filters + ", bookings=" + bookings.size() + "]";
    }
}