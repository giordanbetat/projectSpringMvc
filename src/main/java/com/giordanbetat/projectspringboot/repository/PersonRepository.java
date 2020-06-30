package com.giordanbetat.projectspringboot.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectspringboot.model.Person;

@Repository
@Transactional
public interface PersonRepository extends JpaRepository<Person, Long> {

	@Query("select p from Person p where p.name like %?1%")
	List<Person> findPersonByName(String name);

	@Query("select p from Person p where p.genre like %?1%")
	List<Person> findPersonByGenre(String genre);

	@Query("select p from Person p where p.name like %?1% and p.genre = ?2")
	List<Person> findPersonByNameAndGenre(String name, String genre);

	default Page<Person> findPersonByNamePage(String name, Pageable pageable) {

		Person person = new Person();
		person.setName(name);

		ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("name",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Person> example = Example.of(person, matcher);

		Page<Person> persons = findAll(example, pageable);

		return persons;

	}

	default Page<Person> findPersonByGenrePage(String genre, Pageable pageable) {

		Person person = new Person();
		person.setGenre(genre);

		ExampleMatcher matcher = ExampleMatcher.matchingAny().withMatcher("genre",
				ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Person> example = Example.of(person, matcher);

		Page<Person> persons = findAll(example, pageable);

		return persons;

	}

	default Page<Person> findPersonByNameAndGenrePage(String name, String genre, Pageable pageable) {

		Person person = new Person();
		person.setName(name);
		person.setGenre(genre);

		ExampleMatcher matcher = ExampleMatcher.matchingAny()
				.withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("genre", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

		Example<Person> example = Example.of(person, matcher);

		Page<Person> persons = findAll(example, pageable);

		return persons;

	}

}
