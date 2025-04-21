package com.banking.transactionService.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements UserDetails {

	    private int id;
	    private String firstName;
	    private String lastName;
	    private String email;
	    private String phoneNumber;
	    private String password;
	    
		@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL,CascadeType.REMOVE})
	    @JoinTable(name="User_Roles", joinColumns = {@JoinColumn(name="User_Id")},inverseJoinColumns = {@JoinColumn(name="Role_Name")})
		private Set<Role> roles;

	    @Override
	    @JsonIgnore
	    public Collection<? extends GrantedAuthority> getAuthorities() {
	        Set<Authority> authorities=new HashSet<Authority>();
	        this.roles.forEach(userRole->{
	            authorities.add(new Authority("ROLE_"+userRole.getRoleName()));
	        });
	        return authorities;
	    }
		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return this.password;
		}
		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return this.email;
		}
   
	


}


