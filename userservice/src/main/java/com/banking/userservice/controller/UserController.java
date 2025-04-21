package com.banking.userservice.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banking.userservice.Service.UserService;
import com.banking.userservice.entity.User;
import com.banking.userservice.exception.RoleNotFound;
import com.banking.userservice.exception.UserNotFound;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostConstruct
	public void addRoles() {
		userService.addRoles();
	}
	
	@PostMapping("/addUser")
	public ResponseEntity<User> createUser(@RequestParam String role,@RequestBody User user) throws RoleNotFound{
		User createUser = userService.addUser(role, user);
		return new ResponseEntity<User>(createUser,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','Customer')")
	@GetMapping("/getUser")
	public ResponseEntity<User> getUserByEmail(@RequestParam String email) throws UserNotFound{
		User user = userService.getUser(email);
		return new ResponseEntity<User>(user,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','Customer')")
	@PutMapping("/updateProfile")
	public ResponseEntity<User> updateProfile(@RequestParam int id,@RequestBody User user)throws UserNotFound{
		User updateProfile = userService.updateProfile(id, user);
		return new ResponseEntity<User>(updateProfile,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','Customer')")
	@DeleteMapping("/deleteProfile")
	public ResponseEntity<String> deleteProfile(@RequestParam int id) {
	String deleteProfile = userService.deleteProfile(id);
	return new ResponseEntity<String>(deleteProfile,HttpStatus.OK);
	}
	
	@PreAuthorize("hasAnyRole('Admin','Customer')")
	@GetMapping("/getUserById/{id}")
	public ResponseEntity<Optional<User>> getUserById(@PathVariable int id)throws UserNotFound{
		Optional<User> userById = userService.getUserById(id);
		return new ResponseEntity<Optional<User>>(userById,HttpStatus.OK);
	}
}
