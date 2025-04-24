package bto.Entities;

//Interfaces for Dependency Inversion and Interface Segregation
public interface UserNeeds {
	String displayProfile();
	boolean changePassword(String oldPassword, String newPassword);
}