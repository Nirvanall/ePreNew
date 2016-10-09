package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Message;

public interface MessageDao extends PagingAndSortingRepository<Message, Integer> {

    public Page<Message> findByToUserIdAndStatus(
            Integer toUserId, Byte status, Pageable pageable);

    public Iterable<Message> findByToUserIdAndStatus(
            Integer toUserId, Byte status, Sort sort);

    public Page<Message> findByToUserIdIsNullAndStatus(
            Byte status, Pageable pageable);

    public Iterable<Message> findByToUserIdIsNullAndStatus(
            Byte status, Sort sort);
}
