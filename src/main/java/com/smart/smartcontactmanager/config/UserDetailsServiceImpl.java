package com.smart.smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smart.smartcontactmanager.dao.UserRepository;
import com.smart.smartcontactmanager.entities.User;
import com.smart.smartcontactmanager.helper.Message;

import jakarta.servlet.http.HttpSession;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException  {
		
		User user= userRepository.getUserByUserName(username);
		
		if (user==null) {
			throw new UsernameNotFoundException("Could not found user !!");
			
		}
		
		CustomUsersDetails customUsersDetails = new CustomUsersDetails(user);
		
		
		return customUsersDetails;
	}
	
}
