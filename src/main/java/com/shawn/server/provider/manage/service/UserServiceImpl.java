package com.shawn.server.provider.manage.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.shawn.server.entity.pojo.User;
import com.shawn.server.provider.manage.dao.redis.UserRedisDao;
//import com.shawn.server.provider.manage.dao.redis.UserRedisDao;
import com.shawn.server.web.api.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRedisDao userRedisDao;

	@Override
	public User login(String username, String password) {
		User user = new User();
		user.setUserId(1);
		user.setUsername(username);
		return user;
	}

	@Override
	public User getUser(Integer userId) {
		User user = userRedisDao.get(userId);
		return user;
	}

	@Override
	public User createUser(String username, String password) {
		User user = new User();
		user.setUserId(userRedisDao.nextSequence());
		user.setUsername(username);
		user.setPassword(password);
		userRedisDao.add(user);
		return user;
	}

}
