package bto.Entities;
import bto.Entities.*;
import bto.Enums.*;
import bto.EntitiesProjectRelated.*;
import bto.Controllers.*;

public interface ManagerOperations {
    Project createProject(Project projectDetails);
    boolean editProject(Project project, String attribute, Object newValue);
    boolean deleteProject(Project project);
    boolean toggleProjectVisibility(Project project);
    boolean approveRegistration(OfficerRegistration registration);
    boolean approveApplication(ProjectApplication application);
    boolean rejectApplication(ProjectApplication application);
    boolean approveWithdrawal(ProjectApplication application);
    boolean rejectWithdrawal(ProjectApplication application);
    Report generateReport(FilterCriteria criteria);
}