package com.ryan.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Iterator;

public class CustomAccessDecisionManager implements AccessDecisionManager {
	
	private static final Logger log = LoggerFactory.getLogger(CustomAccessDecisionManager.class);

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (configAttributes == null) {
			return;
		}

		Iterator<ConfigAttribute> iterator = configAttributes.iterator();
		while (iterator.hasNext()) {
			ConfigAttribute configAttribute = iterator.next();
			String needRole = configAttribute.getAttribute();
			log.info("need role is :" + needRole);
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				if (needRole.equals(ga.getAuthority())) {
					return;
				}
			}
		}
		throw new AccessDeniedException("Cannot Access!");
	}

	@Override
	public boolean supports(ConfigAttribute configAttribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}

}
