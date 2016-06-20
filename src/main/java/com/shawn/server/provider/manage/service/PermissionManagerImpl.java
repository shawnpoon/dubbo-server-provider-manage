package com.shawn.server.provider.manage.service;

import java.util.HashSet;
import java.util.Set;

import com.alibaba.dubbo.config.annotation.Service;
import com.shawn.server.core.shiro.PermissionManager;

@Service
public class PermissionManagerImpl implements PermissionManager {

	@Override
	public Set<String> getRoles(String username) {
		Set<String> set = new HashSet<>();
		return set;
	}

	@Override
	public Set<String> getStringPermissions(String username) {
		Set<String> set = new HashSet<>();
		return set;
	}

}
