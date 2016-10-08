package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Message;

public interface MessageDao extends PagingAndSortingRepository<Message, Integer> {

    public Page<Message> findByToUserId(Integer toUserId, Pageable pageable);

    public Iterable<Message> findByToUserId(Integer toUserId, Sort sort);

    public Page<Message> findByStatusAndToUserIdIsNull(
            Byte status, Pageable pageable);

    public Iterable<Message> findByStatusAndToUserIdIsNull(
            Byte status, Sort sort);
}
