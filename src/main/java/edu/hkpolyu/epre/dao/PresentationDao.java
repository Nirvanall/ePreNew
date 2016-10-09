package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Presentation;

public interface PresentationDao extends PagingAndSortingRepository<Presentation, Integer> {

    public Page<Presentation> findByStatus(Byte status, Pageable pageable);

    public Iterable<Presentation> findByStatus(Byte status, Sort sort);

    public Page<Presentation> findByDepartment_IdAndStatus(
            Integer departmentId, Byte status, Pageable pageable);

    public Iterable<Presentation> findByDepartment_IdAndStatus(
            Integer departmentId, Byte status, Sort sort);
}
