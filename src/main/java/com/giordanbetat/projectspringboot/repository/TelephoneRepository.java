package com.giordanbetat.projectspringboot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectspringboot.model.Telephone;

@Repository
@Transactional
public interface TelephoneRepository extends CrudRepository<Telephone, Long>{

	@Query("select t from Telephone t where t.person.id = ?1")
	public List<Telephone> getTelephones(Long idPerson);
	
}
