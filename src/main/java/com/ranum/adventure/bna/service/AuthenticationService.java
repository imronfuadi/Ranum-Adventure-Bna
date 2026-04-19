package com.ranum.adventure.bna.service;

import com.ranum.adventure.bna.entities.User;

public interface AuthenticationService {

	public User findUserAndPassword(String username, String password);
}
