package com.shawn.server.provider.manage.service;

import com.shawn.server.core.shiro.AccountManager;
import com.shawn.server.core.shiro.AuthenticationUser;

public class AccountManagerImpl implements AccountManager{

	@Override
	public AuthenticationUser findByUsername(String username) {
		AuthenticationUser user = new AuthenticationUser();
		user.setUsername(username);
		user.setState(0);
		return user;
	}

}
