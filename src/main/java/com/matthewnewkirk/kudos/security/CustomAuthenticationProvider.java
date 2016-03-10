package com.matthewnewkirk.kudos.security;

import java.util.ArrayList;
import java.util.List;

import com.matthewnewkirk.kudos.containers.KudoUser;
import com.matthewnewkirk.kudos.db.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Matt Newkirk 3/9/2016
 */
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  UserService userService;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    final String name = authentication.getName();
    final String password = authentication.getCredentials().toString();
    KudoUser kudoUser = userService.findUserByUsernameAndPassword(name, password);
    if (kudoUser != null) {
      final List<GrantedAuthority> grantedAuths = new ArrayList<>();
      grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
      final UserDetails principal = new User(name, password, grantedAuths);
      final Authentication auth = new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
      return auth;
    } else {
      return null;
    }
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
