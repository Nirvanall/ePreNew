package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Comment;

public interface CommentDao extends PagingAndSortingRepository<Comment, Integer> {

    public Page<Comment> findByVideo_IdAndStatus(
            Integer videoId, Byte status, Pageable pageable);

    public Iterable<Comment> findByVideo_IdAndStatus(
            Integer videoId, Byte status, Sort sort);
}
