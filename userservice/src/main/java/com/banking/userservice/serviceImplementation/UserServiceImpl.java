package com.banking.userservice.serviceImplementation;


import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.banking.userservice.Service.UserService;
import com.banking.userservice.entity.Role;
import com.banking.userservice.entity.User;
import com.banking.userservice.exception.RoleNotFound;
import com.banking.userservice.exception.UserNotFound;
import com.banking.userservice.feignClient.NotificationClient;
import com.banking.userservice.repo.RoleRepository;
import com.banking.userservice.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NotificationClient notificationClient;
	
	private BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder(12);
	
	@Override
	public void addRoles() {
		Role admin=new Role();
		admin.setRoleName("Admin");
		roleRepository.save(admin);
		
		Role customer=new Role();
		customer.setRoleName("Customer");
		roleRepository.save(customer);
	}

	@Override
	public User addUser(String role, User user) throws RoleNotFound {
		Role role2 = roleRepository.findById(role).orElseThrow(()-> new RoleNotFound("Role Not Found with role :"+role));
		Set<Role> roles=new HashSet<Role>();
		roles.add(role2);
		user.setRoles(roles);
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		notificationClient.accountCreationMail(user.getEmail());
		return userRepository.save(user);
	}

	@Override
	public User getUser(String email) throws UserNotFound {
		User user=userRepository.findByEmail(email);
		if(user==null) {
			throw new UserNotFound("User Not Found with user name :"+email);
		}
		return user;
	}

	@Override
	public User updateProfile(int id, User user) throws UserNotFound {
		User updatedUser = userRepository.findById(id).
				orElseThrow(()-> new UserNotFound("UserNotFound with Id :"+id));
	updatedUser.setEmail(user.getEmail());
	updatedUser.setFirstName(user.getFirstName());
    updatedUser.setLastName(user.getLastName());
	updatedUser.setPassword(user.getPassword());
	updatedUser.setPhoneNumber(user.getPhoneNumber());
		return userRepository.save(updatedUser);
	}

	@Override
	public String deleteProfile(int id) {
	    if (!userRepository.existsById(id)) {
	        throw new RuntimeException("User with ID " + id + " not found");
	    }
	    userRepository.deleteById(id);
	    return "User Deleted Successfully...........";
	}

	@Override
	public Optional<User> getUserById(int id) throws UserNotFound {
		if (!userRepository.existsById(id)) {
	        throw new RuntimeException("User with ID " + id + " not found");
	    }
		Optional<User> byId = userRepository.findById(id);
		return byId;
	}

}
