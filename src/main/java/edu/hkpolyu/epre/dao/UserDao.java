package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import edu.hkpolyu.epre.model.User;

public interface UserDao extends PagingAndSortingRepository<User, Integer> {

    public Iterable<User> findByUserName(String userName);
}
