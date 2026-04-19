package com.ranum.adventure.bna.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ranum.adventure.bna.dto.UserDto;
import com.ranum.adventure.bna.entities.Role;
import com.ranum.adventure.bna.entities.User;


public interface UserService {

    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
    
    User loadUserByUsername(String username);
    
    Set<SimpleGrantedAuthority> getAuthorities(Set<Role> roles);
    
    Long getUserIdByUsername(String username);
    
    Long getUserById(Long userId);
    
//    User findUserAndPassword(String username, String password);
    
//    public User loadUserByUsername(String username);
    
//    User findByUsername(String email);
    
    User findByEmail(String email);
    
//    void changePassword(String email, String newPassword);
    
    //change password tanpa dto
    void changePassword(User user, String newPassword);
    
    //change password dengan dto
//    void changePassword(ChangePasswordUserDto changePasswordUserDto);
}
