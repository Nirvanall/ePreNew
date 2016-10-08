package edu.hkpolyu.epre.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import edu.hkpolyu.epre.model.Video;

public interface VideoDao extends PagingAndSortingRepository<Video, Integer> {

    public Page<Video> findByStatusAndOwnerId(
            Byte status, Integer ownerId, Pageable pageable);

    public Iterable<Video> findByStatusAndOwnerId(
            Byte status, Integer ownerId, Sort sort);
}
