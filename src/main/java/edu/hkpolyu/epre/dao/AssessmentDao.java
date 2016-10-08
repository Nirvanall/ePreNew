package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Assessment;

public interface AssessmentDao extends PagingAndSortingRepository<Assessment, Integer> {

    public Page<Assessment> findByToUserId(Integer toUserId, Pageable pageable);

    public Iterable<Assessment> findByToUserId(Integer toUserId, Sort sort);

    public Page<Assessment> findByStatusAndToUserIdIsNull(
            Byte status, Pageable pageable);

    public Iterable<Assessment> findByStatusAndToUserIdIsNull(
            Byte status, Sort sort);
}
