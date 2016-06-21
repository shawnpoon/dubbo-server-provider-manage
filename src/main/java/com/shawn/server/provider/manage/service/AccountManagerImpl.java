package com.shawn.server.provider.manage.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import com.alibaba.dubbo.config.annotation.Service;
import com.shawn.server.core.shiro.AccountManager;
import com.shawn.server.core.shiro.AuthenticationUser;

@Service
public class AccountManagerImpl implements AccountManager<AuthenticationUser> {

	private static final String CURRENT_USER = "user";

	@Override
	public AuthenticationUser findByUsername(String username) {
		AuthenticationUser user = new AuthenticationUser();
		user.setUsername(username);
		user.setPassword("1234");
		user.setState(0);
		return user;
	}

	@Override
	public boolean login(String username, String password) throws UnknownAccountException, ExcessiveAttemptsException,
			LockedAccountException, IncorrectCredentialsException {
		try {
			UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			Subject subject = SecurityUtils.getSubject();
			token.setRememberMe(false);
			subject.login(token);
			if (subject.isAuthenticated()) {
				subject.getSession().setAttribute(CURRENT_USER, findByUsername(username));
				return true;
			} else {
				return false;
			}
		} catch (UnknownAccountException e) {
			// 账户不存在
			throw new UnknownAccountException();
		} catch (ExcessiveAttemptsException e) {
			// 登录失败次数过多
			throw new ExcessiveAttemptsException();
		} catch (LockedAccountException e) {
			// 帐号已被锁定
			throw new LockedAccountException();
		} catch (IncorrectCredentialsException e) {
			// 登录密码错误
			throw new IncorrectCredentialsException();
		}
	}

}
