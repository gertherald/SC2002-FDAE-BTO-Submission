
# BTO Management System

## Overview
The BTO (Build-To-Order) Management System is a comprehensive Java application designed to streamline the management of public housing projects in Singapore. It facilitates the entire lifecycle of BTO projects, from creation and management to application processing and flat booking, serving various stakeholders in the housing ecosystem.

## System Architecture

### Design Patterns
The system implements several design patterns:
- **MVC (Model-View-Controller)**: Separates data (entities), user interface (boundaries), and control logic
- **Interface Segregation**: Using interfaces like `UserNeeds` and `ManagerOperations`
- **Dependency Inversion**: High-level modules depend on abstractions (interfaces) rather than concrete implementations
- **Composition**: Using composition for role-based functionality (e.g., HDBOfficer contains an Applicant role)
- **Singleton**: Key controllers are initialized once and shared across interfaces
- **Facade**: Boundary classes provide simplified interfaces to complex subsystems
- **Repository Pattern**: Controllers act as repositories for their respective entity types

### Component Structure

#### Boundary Classes (View)
- `UserInterface`: Main interface handling authentication and navigation
- `ApplicantInterface`: Interface for applicant-specific functionalities
- `OfficerInterface`: Interface for HDB officer-specific functionalities
- `ManagerInterface`: Interface for HDB manager-specific functionalities

#### Controller Classes and Interfaces
- `AuthController` implements `IAuthController`: Manages user authentication and account management
- `ProjectController` implements `IProjectController`: Handles project creation, modification, and listing
- `ApplicationController` implements `IApplicationController`: Processes applications and status updates
- `BookingController` implements `IBookingController`: Manages flat bookings and receipt generation
- `EnquiryController` implements `IEnquiryController`: Handles user enquiries and responses
- `WithdrawalController` implements `IWithdrawalController`: Manages application withdrawal requests
- `RegistrationController` implements `IRegistrationController`: Handles officer registration for projects
- `ReportController` implements `IReportController`: Generates reports based on various criteria
- `FileManager`: Manages data persistence through text files
- `ReceiptGenerator` implements `IReceiptGenerator`: Creates formatted receipts for flat bookings

#### Entity Classes
- User hierarchy:
  - `User`: Base class for all users with common authentication and profile functionality
  - `Applicant`: Citizens who can apply for BTO flats
  - `HDBOfficer`: Staff who manage applications (also has Applicant role through composition)
  - `HDBManager`: Administrators who oversee projects
- Project-related:
  - `Project`: Represents a BTO housing project with flat types, application periods, and visibility
  - `ProjectFlats`: Manages flat availability within projects, tracks individual flat units
  - `ProjectApplication`: Represents an application for a project, tracks status and flat type selection
  - `FlatBooking`: Represents a successful flat booking with specific flat ID and status
  - `Receipt`: Represents a receipt for a booking with formatting and content generation
  - `Enquiry`: Represents a user enquiry about a project, tracks responses and respondents
  - `OfficerRegistration`: Represents an officer's request to be assigned to a project
  - `Withdrawal`: Represents a request to withdraw an application with status tracking
  - `Report`: Represents a generated report of flat bookings
  - `FilterCriteria`: Used to specify filtering criteria for reports

#### Enumerations
- `ApplicationStatus`: Defines the possible statuses of an application (PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED)
- `FlatType`: Defines the available types of flats (TWO_ROOM, THREE_ROOM)
- `MaritalStatus`: Defines possible marital statuses (SINGLE, MARRIED)

#### Interfaces
- `UserNeeds`: Defines common functionality for all users (profile display, password change)
- `ManagerOperations`: Defines operations specific to managers (project creation, approval workflows)
- Controller interfaces for dependency injection and abstraction:
  - `IAuthController`, `IProjectController`, `IApplicationController`
  - `IBookingController`, `IEnquiryController`, `IWithdrawalController`
  - `IRegistrationController`, `IReportController`, `IReceiptGenerator`

## Features

### Multi-User System
The application supports three distinct user roles:
- **Applicants**: Citizens who can browse and apply for BTO projects
- **HDB Officers**: Staff who manage flat bookings and handle applicant enquiries 
  - Officers also have an embedded Applicant role, allowing them to apply for flats
  - Officers cannot process their own applications or enquiries
- **HDB Managers**: Administrators who oversee projects and approve applications

### Project Management
- Create, edit, and delete housing projects
- Configure flat types (2-Room, 3-Room) and availability
- Set application periods with opening and closing dates
- Toggle project visibility to applicants
- Assign officers to projects through a registration system
- Track individual flat units and their availability status

### Application Processing
- Apply for available projects based on eligibility criteria
- Process and approve/reject applications
- Select specific flat types for successful applications
- Book specific flat units with unique IDs
- Generate detailed receipts for bookings
- Request withdrawal from applications
- Track application status through the entire workflow

### Enquiry System
- Submit enquiries about specific projects or general topics
- Track and respond to enquiries
- Edit and delete pending enquiries
- View response history with timestamps
- Track which staff member responded to each enquiry

### Reporting System
- Generate reports with customizable filter criteria
- Filter by project, marital status, age range, and flat type
- Format reports for easy reading and analysis
- Include booking statistics and demographic information

### Data Persistence
The system uses a file-based data persistence mechanism to store:
- User accounts (Applicants, Officers, Managers)
- Projects and their configurations (including flat types and availability)
- Applications and their statuses (PENDING, SUCCESSFUL, UNSUCCESSFUL, BOOKED)
- Flat bookings (with specific flat IDs) and receipts
- Enquiries and responses (with timestamps and responder information)
- Officer registrations (PENDING, APPROVED, REJECTED)
- Withdrawal requests (PENDING, APPROVED, REJECTED)

## Domain Rules

### Applicant Eligibility
- **Singles**: Must be at least 35 years old and can only apply for 2-Room flats
- **Married Couples**: Must be at least 21 years old and can apply for any flat type (2-Room or 3-Room)

### Project Application Rules
- Applicants can only have one active application at a time
- Applications must be submitted during the project's application period (from open date to close date)
- Applications go through a workflow: PENDING → SUCCESSFUL/UNSUCCESSFUL → BOOKED (if successful)

### Officer Registration Rules
- Officers cannot apply for projects they are assigned to
- Officers cannot have overlapping project assignments (based on application periods)
- Officers need manager approval before being assigned to projects
- Projects have a maximum number of officer slots that can be assigned

### Conflict of Interest Prevention
- Officers cannot process their own applications or enquiries
- Officers cannot respond to their own enquiries
- Managers can only manage projects they created
- Managers can only approve registrations for projects they manage

### Withdrawal Rules
- Applicants can request withdrawal of their application
- Withdrawal requests must be approved by the project manager
- If withdrawal is approved, the application is removed and the applicant can apply for other projects
- If the withdrawn application had a booking, the flat is returned to the available pool

## System Workflows

1. **User Hierarchy**
   - User (base class)
     - Applicant (can apply for flats)
     - HDBOfficer (manages applications, contains Applicant role)
     - HDBManager (manages projects)

2. **Projects and Applications**
   - Project ← managed by → HDBManager
   - Project ← applied to by → Applicant (via ProjectApplication)
   - Project ← assigned to → HDBOfficer (via OfficerRegistration)

3. **Bookings and Receipts**
   - ProjectApplication → leads to → FlatBooking (when successful)
   - FlatBooking → generates → Receipt
   - FlatBooking ← processed by → HDBOfficer

4. **Enquiries and Communication**
   - Enquiry ← submitted by → Applicant
   - Enquiry ← about → Project (optional)
   - Enquiry ← responded by → User (HDBOfficer or HDBManager)

5. **Withdrawal Process**
   - Withdrawal ← requested by → Applicant
   - Withdrawal ← for → ProjectApplication
   - Withdrawal ← processed by → HDBManager

6. **Application Status Workflow**
   ```
   PENDING → SUCCESSFUL/UNSUCCESSFUL → BOOKED
         ↓
   Withdrawal Request (PENDING/APPROVED/REJECTED)
   ```

This interconnected structure allows the system to maintain referential integrity and track the entire lifecycle of housing applications from submission to flat booking.# BTO Management System


### Applicant Workflow
1. Register and log in as an Applicant
2. Browse available projects with optional filtering by neighborhood or flat type
3. Submit an application for a desired project
4. Check application status
5. Select a flat type if application is successful
6. Submit enquiries if needed
7. Request withdrawal if necessary

### HDB Officer Workflow
1. Register and log in as an HDB Officer
2. Register for project assignments
3. View assigned projects
4. Process flat bookings for successful applications
5. Generate receipts for approved bookings
6. Respond to applicant enquiries
7. Apply for flats through embedded Applicant role (if eligible)

### HDB Manager Workflow
1. Register and log in as an HDB Manager
2. Create and manage BTO projects
3. Approve/reject officer registrations for projects
4. Approve/reject applications
5. Approve/reject withdrawal requests
6. Generate reports
7. Respond to enquiries

## Technical Implementation

### Data Storage
All data is stored in tab-delimited text files that maintain entity relationships through unique identifiers:

- **User Data**:
  - `Applicant List.txt`: Stores applicant personal information (name, NRIC, age, marital status, password)
  - `Officer List.txt`: Stores HDB officer personal information
  - `Manager List.txt`: Stores HDB manager personal information

- **Project Data**:
  - `Project List.txt`: Stores project details (name, neighborhood, flat types, units, prices, application dates, manager, officer slots)

- **Application Data**:
  - `Application List.txt`: Stores application details (applicant NRIC, project name, status, withdrawal status, selected flat type)
  - `Booking List.txt`: Stores booking information (applicant NRIC, project name, flat type, flat ID, booking date, status)
  - `Receipt List.txt`: Stores receipt information (applicant NRIC, project name, officer NRIC, flat type, flat ID, receipt date, content)
  - `Withdrawal List.txt`: Stores withdrawal requests (applicant NRIC, project name, status, request date)

- **Officer Assignment Data**:
  - `Officer Registration List.txt`: Stores officer registrations (officer NRIC, project name, registration status)

- **Communication Data**:
  - `Enquiry List.txt`: Stores enquiries (applicant NRIC, project name, content, ID, response, responder NRIC, dates)

### Entity Relationships
The system maintains relationships between entities through reference IDs:
- Projects reference their manager by NRIC
- Applications reference both applicant and project
- Bookings reference applicant, project, and processing officer
- Enquiries reference applicant, project, and responder
- Officer registrations reference both officer and project

### System Startup & Shutdown
- On startup, the `BTOManagementSystem.initialize()` method:
  1. Initializes all controllers
  2. Creates a FileManager
  3. Loads all data from text files using the FileManager
  4. Establishes relationships between entities
  5. Initializes the user interface

- During operation, all operations are performed in memory using the loaded object graphs

- On shutdown, a Runtime hook calls `BTOManagementSystem.saveData()` which:
  1. Collects all entities from their respective controllers
  2. Uses the FileManager to save each entity type to its respective file
  3. Ensures all relationship data is properly preserved

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- A Java IDE such as Eclipse, IntelliJ IDEA, or NetBeans

### Project Structure
```
bto-management-system/
├── src/
│   └── bto/
│       ├── Boundaries/         # User interfaces
│       │   ├── UserInterface.java
│       │   ├── ApplicantInterface.java
│       │   ├── OfficerInterface.java
│       │   └── ManagerInterface.java
│       ├── Controllers/        # Business logic
│       │   ├── AuthController.java
│       │   ├── ProjectController.java
│       │   ├── ApplicationController.java
│       │   ├── BookingController.java
│       │   ├── EnquiryController.java
│       │   ├── WithdrawalController.java
│       │   ├── RegistrationController.java
│       │   ├── ReportController.java
│       │   ├── FileManager.java
│       │   └── ReceiptGenerator.java
│       ├── Entities/           # User entities
│       │   ├── User.java
│       │   ├── Applicant.java
│       │   ├── HDBOfficer.java
│       │   ├── HDBManager.java
│       │   └── UserNeeds.java
│       ├── EntitiesProjectRelated/ # Project entities
│       │   ├── Project.java
│       │   ├── ProjectFlats.java
│       │   ├── ProjectApplication.java
│       │   ├── FlatBooking.java
│       │   ├── Receipt.java
│       │   ├── Enquiry.java
│       │   ├── OfficerRegistration.java
│       │   ├── Withdrawal.java
│       │   ├── Report.java
│       │   └── FilterCriteria.java
│       ├── Enums/             # Enumeration types
│       │   ├── ApplicationStatus.java
│       │   ├── FlatType.java
│       │   └── MaritalStatus.java
│       ├── Interfaces/        # Controller interfaces
│       │   ├── IAuthController.java
│       │   ├── IProjectController.java
│       │   └── ...
│       └── data/              # Data storage
│           ├── Applicant List.txt
│           ├── Officer List.txt
│           ├── Manager List.txt
│           ├── Project List.txt
│           ├── Application List.txt
│           ├── Enquiry List.txt
│           ├── Officer Registration List.txt
│           ├── Booking List.txt
│           ├── Receipt List.txt
│           └── Withdrawal List.txt
```

### Installation
1. Clone the repository or download the source code
2. Open the project in your preferred IDE
3. Ensure the project structure includes the `src` and `data` directories
4. Compile and run the `BTOManagementSystem` class in the src/bto/Boundaries directory which contains the main method

### Initial Setup
- The system comes with sample data to demonstrate functionality:
  - Sample users (applicants, officers, managers)
  - Sample projects with different neighborhoods and flat types
  - Sample applications in various states
  - Sample bookings and receipts
  - Sample enquiries and responses
- You can register new users of any type through the login menu
- Default password for sample users is "password"

### System Requirements
- Memory: Minimum 1GB RAM
- Storage: At least 100MB free disk space
- Display: Console-based interface (minimum 80x24 characters)

## Usage Examples

### Creating a New Project (Manager)
1. Log in as a manager (e.g., username: `S5678901G`, password: `password`)
2. Select "Create Project" from the manager menu
3. Enter project details:
   - Project Name: e.g., "Woodland Horizon"
   - Neighborhood: e.g., "Woodlands"
   - Number of 2-Room units: e.g., 50
   - Number of 3-Room units: e.g., 30
   - Application Opening Date: in DD/MM/YYYY format
   - Application Closing Date: in DD/MM/YYYY format
   - Number of HDB Officer slots: e.g., 5
4. The project is initially created with visibility set to "hidden"
5. Select "Toggle Project Visibility" to make it visible to applicants

### Officer Registration & Assignment
1. Log in as an HDB officer (e.g., username: `T1234567G`, password: `password`)
2. Select "Register for Project Assignment" from the officer menu
3. View the list of available projects and select one to register for
4. The registration status will be set to "PENDING"
5. Log in as a manager who owns the project
6. Select "Approve/Reject HDB Officer Registration"
7. View the list of pending registrations and approve or reject as needed

### Applying for a Project (Applicant)
1. Log in as an applicant (e.g., username: `S1234567A`, password: `password`)
2. Select "View Projects" from the applicant menu
3. Browse available projects:
   - Filter by neighborhood if desired
   - Filter by flat type if desired
   - Sort alphabetically if desired
4. Select a project and choose "Apply"
5. Application status will be initially set to "PENDING"

### Processing Applications (Manager)
1. Log in as a manager
2. Select "Approve/Reject Application" from the manager menu
3. View pending applications for your projects
4. Select an application to process
5. Choose to approve or reject the application
6. If approved, the application status changes to "SUCCESSFUL"
7. The applicant can then select a specific flat type from their eligible options

### Booking a Flat (Officer)
1. Log in as an officer assigned to the project
2. Select "Manage Flat Bookings" -> "View Pending Flat Booking Requests"
3. View pending flat booking requests for assigned projects
4. Select a request to process
5. View the flat availability for the requested type
6. Choose to approve or reject the booking
7. If approved:
   - A specific flat ID is assigned
   - The flat is marked as unavailable
   - The application status changes to "BOOKED"
8. Select "Generate Receipt for Flat Type Booking" to create a receipt

### Handling Enquiries
1. Log in as an applicant
2. Select "Submit Enquiry" from the menu
3. Choose whether it's a general enquiry or about a specific project
4. Enter your enquiry content
5. Log in as an officer or manager
6. Select "Respond to Enquiries" from the menu
7. View the list of pending enquiries
8. Select an enquiry to respond to
9. Enter your response
10. The applicant can view the response by selecting "View Enquiries"

### Withdrawal Process
1. Log in as an applicant with an active application
2. Select "Request Withdrawal" from the menu
3. Confirm your intention to withdraw the application
4. Log in as the project manager
5. Select "Approve/Reject Withdrawal" from the menu
6. Process the withdrawal request
7. If approved, the application is removed and the applicant can apply for other projects

## Future Enhancements

### Short-term Improvements
1. **Graphical User Interface (GUI)**
   - Implement a JavaFX or Swing-based interface to replace the console UI
   - Create intuitive forms for project creation, application submission, and booking management
   - Add visual dashboards for managers to monitor project statistics

2. **Enhanced Security**
   - Implement password hashing and encryption 
   - Add role-based access control with more granular permissions
   - Include session management and timeout functionality

3. **Notification System**
   - Implement email notifications for status updates
   - Add SMS notifications for critical events like application approval
   - Create an in-system notification center for users

### Mid-term Improvements
1. **Database Integration**
   - Replace file-based storage with a relational database (MySQL, PostgreSQL)
   - Implement proper data modeling with foreign key constraints
   - Add transaction support for critical operations

2. **Expanded Flat Types**
   - Add support for more flat types (4-Room, 5-Room, Executive)
   - Implement pricing models based on location and flat type
   - Add floor plans and unit-specific details

3. **Application Enhancements**
   - Implement a waitlist system for popular projects
   - Add priority schemes for first-time applicants
   - Support for joint applications (e.g., family members applying together)

### Long-term Vision
1. **Web-based Platform**
   - Migrate to a web-based architecture with RESTful APIs
   - Create mobile applications for applicants
   - Implement real-time updates and notifications

2. **Integration with Government Systems**
   - Connect with national identity verification systems
   - Interface with banking systems for deposit and payment processing
   - Integrate with property registration systems for automatic deed transfer

3. **Advanced Analytics**
   - Implement predictive models for housing demand
   - Add visualization tools for demographic analysis
   - Create recommendation systems for applicants based on preferences and eligibility

These enhancements would transform the system from a standalone application to a comprehensive housing management platform that serves all stakeholders in the public housing ecosystem.## Entity Relationships

The system is built on a rich network of relationships between entities:
