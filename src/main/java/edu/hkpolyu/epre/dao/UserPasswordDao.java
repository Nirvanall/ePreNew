package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.UserPassword;

public interface UserPasswordDao extends CrudRepository<UserPassword, Integer> {

}
