package com.banking.transactionService.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.banking.transactionService.dto.UserDto;
import com.banking.transactionService.feignClient.UserClient;



@Service
public class JwtServiceImpl implements UserDetailsService{

	@Autowired
	@Lazy
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserClient userClient;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserDto user = userClient.getUserByEmail(email);
		if (user == null) {
			System.out.println("User Not Found");
			throw new UsernameNotFoundException("user not found");
		}

		return user;
	}
}
