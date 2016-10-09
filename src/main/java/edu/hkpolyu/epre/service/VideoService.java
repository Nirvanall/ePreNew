package edu.hkpolyu.epre.service;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.VideoDao;
import edu.hkpolyu.epre.model.Video;
import edu.hkpolyu.epre.model.Assessment;

@Component
public class VideoService {

    private VideoDao videoDao;
    @Autowired
    public void setVideoDao(VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    public Video getVideoById(Integer videoId) {
        return videoDao.findOne(videoId);
    }

    public Video saveVideo(Video video) {
        return videoDao.save(video);
    }

    public Page<Video> listVideoOfUser(Integer ownerId, int page, int size) {
        return videoDao.findByOwner_IdAndStatus(ownerId,
                Video.STATUS_NORMAL, new PageRequest(page - 1, size));
    }

    public Page<Video> listVideoByAssessor(Integer viewerId, int page, int size) {
        return videoDao.findByAssessments_Viewer_IdAndAssessments_StatusAndStatus(
                viewerId, Assessment.STATUS_NORMAL, Video.STATUS_NORMAL,
                new PageRequest(page - 1, size));
    }
}
