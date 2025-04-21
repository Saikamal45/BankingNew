package com.banking.userservice.serviceImplementationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.banking.userservice.entity.Role;
import com.banking.userservice.entity.User;
import com.banking.userservice.exception.UserNotFound;
import com.banking.userservice.repo.UserRepository;
import com.banking.userservice.serviceImplementation.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userServiceImpl;
	
	private User mockUser;
	
	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		Role role=new Role("Customer");
		Set<Role> roles=new HashSet<Role>();
		roles.add(role);
		mockUser=new User(1,"sai","kamal","abc@gmail","abc123","7894561230",roles);
	}
	
	@Test
	void deleteProfileSuccessTest() {
		int id=1;
		when(userRepository.existsById(id)).thenReturn(true);
		
		String deleteProfile = userServiceImpl.deleteProfile(id);
		verify(userRepository,times(1)).deleteById(id);
		assertEquals("User Deleted Successfully...........", deleteProfile);
	}
	
	@Test
	void deleteProfileFailureTest() {
		when(userRepository.existsById(mockUser.getId())).thenReturn(false);
		assertThrows(RuntimeException.class, ()->{
			userServiceImpl.deleteProfile(mockUser.getId());
		});
		
//		assertEquals("User with ID 99 not found", Exception);
	}
	
	@Test
	void getUserByIdSuccessTest() throws UserNotFound {
		// ARRANGE
		when(userRepository.existsById(mockUser.getId())).thenReturn(true);
		when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
		
		//ACT
		Optional<User> userById = userServiceImpl.getUserById(mockUser.getId());
		
		//ASSERT
		verify(userRepository,times(1)).existsById(mockUser.getId());
		verify(userRepository,times(1)).findById(mockUser.getId());
		assertEquals(mockUser.getId(),userById.get().getId());
	}
	
	@Test
	void getUserByIdFailureTest() {
		when(userRepository.existsById(mockUser.getId())).thenReturn(false);
		
		
		assertThrows(RuntimeException.class, ()->{
			userServiceImpl.getUserById(mockUser.getId());
		});
		
		verify(userRepository,times(1)).existsById(mockUser.getId());
		verify(userRepository,never()).findById(mockUser.getId());
	}
	
	@Test
	void updateProfileSuccessTest() throws UserNotFound {
		Role role=new Role("Customer");
		Set<Role> roles=new HashSet<Role>();
		roles.add(role);
		User updatedUser=new User(1,"hiii","kamal","abc@gmail","abc123","7894561230",roles);
		when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
		when(userRepository.save(any(User.class))).thenReturn(updatedUser);
		
		User updateProfile = userServiceImpl.updateProfile(mockUser.getId(), updatedUser);
		
		verify(userRepository,times(1)).findById(mockUser.getId());
		verify(userRepository,times(1)).save(any(User.class));
		
		assertEquals("hiii", updateProfile.getFirstName());
			
	}
	
	@Test
	void updateProfileFailureTest() throws UserNotFound {
		Role role=new Role("Customer");
		Set<Role> roles=new HashSet<Role>();
		roles.add(role);
		User updatedUser=new User(1,"hiii","kamal","abc@gmail","abc123","7894561230",roles);
		when(userRepository.findById(mockUser.getId())).thenReturn(Optional.empty());
		
		assertThrows(UserNotFound.class, ()->{
			userServiceImpl.updateProfile(mockUser.getId(), updatedUser);
		});
		
		verify(userRepository,times(1)).findById(mockUser.getId());
		 verify(userRepository,never()).save(any(User.class));
	}
}
