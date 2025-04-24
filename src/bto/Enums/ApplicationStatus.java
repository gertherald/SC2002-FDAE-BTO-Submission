package bto.Enums;

/**
 * Represents the possible statuses of an application in the system.
 * <p>
 * This enum defines the various states that an application can be in
 * during its lifecycle.
 */
public enum ApplicationStatus {
    /** Indicates that the application is currently under review. */
    PENDING,

    /** Indicates that the application has been approved. */
    SUCCESSFUL,

    /** Indicates that the application has been rejected. */
    UNSUCCESSFUL,

    /** Indicates that the application has been confirmed and scheduled. */
    BOOKED
}