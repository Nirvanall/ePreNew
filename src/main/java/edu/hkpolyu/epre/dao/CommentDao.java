package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Comment;

public interface CommentDao extends PagingAndSortingRepository<Comment, Integer> {

    public Page<Comment> findByToUserId(Integer toUserId, Pageable pageable);

    public Iterable<Comment> findByToUserId(Integer toUserId, Sort sort);

    public Page<Comment> findByStatusAndToUserIdIsNull(
            Byte status, Pageable pageable);

    public Iterable<Comment> findByStatusAndToUserIdIsNull(
            Byte status, Sort sort);
}
