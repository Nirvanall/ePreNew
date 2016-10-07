package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import edu.hkpolyu.epre.model.Message;

public interface MessageDao extends PagingAndSortingRepository<Message, Integer> {

    public Page<Message> findByToUser(Integer toUserId, Pageable pagable);

    public Iterable<Message> findByToUser(Integer toUserId, Sort sort);
}
