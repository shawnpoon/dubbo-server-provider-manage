package com.shawn.server.provider.manage.service;

import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.shawn.server.core.shiro.AccountManager;
import com.shawn.server.core.shiro.AuthenticationUser;
import com.shawn.server.core.shiro.PermissionManager;
import com.shawn.server.entity.pojo.User;
import com.shawn.server.provider.manage.dao.redis.UserRedisDao;
//import com.shawn.server.provider.manage.dao.redis.UserRedisDao;
import com.shawn.server.web.api.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private PermissionManager permissionManager;
	@Autowired
	private AccountManager<AuthenticationUser> accountManager;
	@Autowired
	private UserRedisDao userRedisDao;

	@Override
	public User login(String username, String password) {
		try {
			boolean isLogin = accountManager.login(username, password);
		} catch (UnknownAccountException e) {
			// 账户不存在
		} catch (ExcessiveAttemptsException e) {
			// 登录失败次数过多
		} catch (LockedAccountException e) {
			// 帐号已被锁定
		} catch (IncorrectCredentialsException e) {
			// 登录密码错误
		} catch (Exception e) {

		}

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
