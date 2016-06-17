package com.shawn.server.provider.manage.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.shawn.server.entity.pojo.User;
import com.shawn.server.web.api.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User login(String username, String password) {
		User user = new User();
		user.setUserId(1);
		user.setUsername(username);
		return user;
	}

	@Override
	public User getUser(Integer userId) {
		User user = new User();
		user.setUserId(userId);
		user.setUsername("uHaha");
		return user;
	}

}
