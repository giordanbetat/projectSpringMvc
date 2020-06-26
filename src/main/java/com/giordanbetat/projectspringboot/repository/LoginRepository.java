package com.giordanbetat.projectspringboot.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectspringboot.model.Login;

@Repository
@Transactional
public interface LoginRepository extends CrudRepository<Login, Long> {

	@Query("select l from Login l where l.login = ?1")
	Login findUserByLogin(String login);
	
}
