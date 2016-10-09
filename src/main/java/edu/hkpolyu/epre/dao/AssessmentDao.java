package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Assessment;

public interface AssessmentDao extends CrudRepository<Assessment, Integer> {

    public Iterable<Assessment> findByVideo_IdAndViewer_IdAndStatus(
            Integer videoId, Integer viewId, Byte status);
}
