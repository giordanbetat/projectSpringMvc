package com.giordanbetat.projectspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectspringboot.model.Person;

@Repository
@Transactional
public interface PersonRepository extends CrudRepository<Person, Long> {

	@Query("select p from Person p where p.name like %?1%")
	List<Person> findPersonByName(String name);
	
	@Query("select p from Person p where p.genre like %?1%")
	List<Person> findPersonByGenre(String genre);

	@Query("select p from Person p where p.name like %?1% and p.genre = ?2")
	List<Person> findPersonByNameAndGenre(String name, String genre);

}
