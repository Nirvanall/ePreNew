package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Presentation;

public interface PresentationDao extends PagingAndSortingRepository<Presentation, Integer> {

    public Page<Presentation> findByStatus(Pageable pageable);

    public Iterable<Presentation> findByStatus(Sort sort);
}
