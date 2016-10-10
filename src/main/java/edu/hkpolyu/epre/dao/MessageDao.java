package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Message;

public interface MessageDao extends PagingAndSortingRepository<Message, Integer> {

    public Page<Message> findByToUser_IdAndStatus(
            Integer toUserId, Byte status, Pageable pageable);

    public Iterable<Message> findByToUser_IdAndStatus(
            Integer toUserId, Byte status, Sort sort);

    public Page<Message> findByFromUser_IdAndStatus(
            Integer fromUserId, Byte status, Pageable pageable);

    public Iterable<Message> findByFromUser_IdAndStatus(
            Integer fromUserId, Byte status, Sort sort);

    public Page<Message> findByToUserIsNullAndStatus(
            Byte status, Pageable pageable);

    public Iterable<Message> findByToUserIsNullAndStatus(
            Byte status, Sort sort);
}
