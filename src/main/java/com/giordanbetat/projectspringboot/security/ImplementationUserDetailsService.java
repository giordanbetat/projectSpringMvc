package com.giordanbetat.projectspringboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectspringboot.model.Login;
import com.giordanbetat.projectspringboot.repository.LoginRepository;

@Service
@Transactional
public class ImplementationUserDetailsService implements UserDetailsService {

	@Autowired
	private LoginRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Login login = repository.findUserByLogin(username);

		if (login == null) {
			throw new UsernameNotFoundException("Usuario não localizado");
		}

		return new User(login.getLogin(), login.getPassword(), login.isEnabled(), true, true, true,
				login.getAuthorities());
	}

}
