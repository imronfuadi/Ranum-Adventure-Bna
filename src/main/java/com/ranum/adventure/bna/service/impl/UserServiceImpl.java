package com.ranum.adventure.bna.service.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ranum.adventure.bna.dto.UserDto;
import com.ranum.adventure.bna.entities.Role;
import com.ranum.adventure.bna.entities.User;
import com.ranum.adventure.bna.repository.RoleRepository;
import com.ranum.adventure.bna.repository.UserRepository;
import com.ranum.adventure.bna.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public void saveUser(UserDto userDto) {
		User user = new User();
		user.setName(userDto.getFirstName() + " " + userDto.getLastName());
		user.setEmail(userDto.getEmail());
		// encrypt the password using spring security
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));

		Role role = roleRepository.findByName("ROLE_ADMIN");
		if (role == null) {
			role = checkRoleExist();
		}
		user.setRoles(Arrays.asList(role));
		userRepository.save(user);
	}

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<UserDto> findAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map((user) -> mapToUserDto(user)).collect(Collectors.toList());
	}

	private UserDto mapToUserDto(User user) {
		UserDto userDto = new UserDto();
		String[] str = user.getName().split(" ");
		userDto.setFirstName(str[0]);
		userDto.setLastName(str[1]);
		userDto.setEmail(user.getEmail());
		return userDto;
	}

	private Role checkRoleExist() {
		Role role = new Role();
		role.setName("ROLE_ADMIN");
		return roleRepository.save(role);
	}

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
//		 User user = userRepository.findByEmail(username);
//	        if (user == null) {
//	            throw new UsernameNotFoundException("User not found with username: " + username);
//	        }
//	        return new org.springframework.security.core.user.User(
//	            user.getName(), user.getPassword(), getAuthorities(user.getRoles()));
		return null;
	}

	@Override
	public Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
		}
		return authorities;
	}

	@Override
	public Long getUserIdByUsername(String username) {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		return user.getId();
	}

	@Override
	public Long getUserById(Long userId) {
		// TODO Auto-generated method stub
		return userRepository.getUserById(userId);
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return userRepository.findByEmail(email);
	}

//	@Override
//	public void changePassword(String email, String newPassword) {
//		User existingUser = userRepository.findByEmail(email);
//		if (existingUser != null) {
//			existingUser.setPassword(passwordEncoder.encode(newPassword));
//			userRepository.save(existingUser);
//		}
//	}

//	@Override
//	public void changePassword(User user, String newPassword) {
//	    user.setPassword(newPassword); // Perubahan di sini: Tetapkan kata sandi baru yang sudah di-hash
//	    userRepository.save(user);
//	    System.out.println("Password changed successfully!");
//	    System.out.println(newPassword);
//	}
	
	//change password tanpa dto
	@Override
	@Transactional
	public void changePassword(User user, String newPassword) {
		User existingUser = userRepository.findByEmail(user.getEmail());
		if (existingUser != null) {
			existingUser.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(existingUser);
			System.out.println("Password changed successfully!");
			System.out.println(passwordEncoder.encode(newPassword));
		} else {
			System.out.println("User not found!");
		}
	}
	
	//change password dengan dto
//	@Override
//	@Transactional
//	public void changePassword(ChangePasswordUserDto changePasswordUserDto) {
//	    User existingUser = userRepository.findByEmail(changePasswordUserDto.getEmail());
//	    System.out.println(changePasswordUserDto.getEmail());
//	    if (existingUser != null) {
//	        existingUser.setPassword(passwordEncoder.encode(changePasswordUserDto.getPassword()));
//	        userRepository.save(existingUser);
//	        System.out.println("Password changed successfully!");
//	    } else {
//	        System.out.println("User not found!");
//	    }
//	}

	
	

}
