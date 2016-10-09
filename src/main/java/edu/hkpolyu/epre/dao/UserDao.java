package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.User;

public interface UserDao extends PagingAndSortingRepository<User, Integer> {

    public Iterable<User> findByUserName(String userName);

    public Iterable<User> findByUserNameAndPassword_Password(
            String userName, String password);

    public Page<User> findByStatus(Byte status, Pageable pageable);

    public Iterable<User> findByStatus(Byte status, Sort sort);
}
