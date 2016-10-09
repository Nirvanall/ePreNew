package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Video;

public interface VideoDao extends PagingAndSortingRepository<Video, Integer> {

    public Page<Video> findByOwner_IdAndStatus(
            Integer ownerId, Byte status, Pageable pageable);

    public Iterable<Video> findByOwner_IdAndStatus(
            Integer ownerId, Byte status, Sort sort);

    public Page<Video> findByAssessments_Viewer_IdAndAssessments_StatusAndStatus(
            Integer viewerId, Byte assessmentStatus,
            Byte videoStatus, Pageable pageable);

    public Iterable<Video> findByAssessments_Viewer_IdAndAssessments_StatusAndStatus(
            Integer viewerId, Byte assessmentStatus,
            Byte videoStatus, Sort sort);
}
