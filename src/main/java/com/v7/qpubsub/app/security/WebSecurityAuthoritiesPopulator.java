package com.v7.qpubsub.app.security;

import java.util.Collection;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Component
public class WebSecurityAuthoritiesPopulator implements LdapAuthoritiesPopulator {

	@Override
	public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations arg0, String arg1) {
		return Lists.newArrayList(new SimpleGrantedAuthority("READONLY"));
	}

}
