package com.giordanbetat.projectspringboot.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.giordanbetat.projectspringboot.model.Profession;

@Transactional
@Repository
public interface ProfessionRepository extends CrudRepository<Profession, Long>{

}
