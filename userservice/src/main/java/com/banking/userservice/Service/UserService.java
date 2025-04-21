package com.banking.userservice.Service;

import java.util.Optional;
import java.util.UUID;

import com.banking.userservice.entity.User;
import com.banking.userservice.exception.RoleNotFound;
import com.banking.userservice.exception.UserNotFound;

public interface UserService {

	void addRoles();
	
	User addUser(String role,User user) throws RoleNotFound;
	
	User getUser(String email) throws UserNotFound;
	
	User updateProfile(int id,User user) throws UserNotFound;
	
	String deleteProfile(int id) ;
	
	Optional<User> getUserById(int id)throws UserNotFound;
}
