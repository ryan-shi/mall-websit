package com.ryan.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import com.ryan.service.RoleService;

import java.util.*;

public class CustomSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private RoleService roleService;

	public CustomSecurityMetadataSource() {
	}

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		String url = ((FilterInvocation) object).getRequestUrl();
	
		Set<String> needRoles=roleService.findDistinctNeedRoles(url);
		Collection<ConfigAttribute> cas = new ArrayList<ConfigAttribute>();
		for (String needRole : needRoles) {
			ConfigAttribute ca = new SecurityConfig(needRole);
			cas.add(ca);
		}
		return cas;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}
}
