package edu.hkpolyu.epre.service;

import java.util.Iterator;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.AssessmentDao;
import edu.hkpolyu.epre.model.Assessment;

@Component
public class AssessmentService {

    private AssessmentDao assessmentDao;
    @Autowired
    public void setAssessmentDao(AssessmentDao assessmentDao) {
        this.assessmentDao = assessmentDao;
    }

    public Assessment getAssessmentById(Integer assessmentId) {
        return assessmentDao.findOne(assessmentId);
    }

    public Assessment getAssessmentByVideoIdAndViewerId(
            Integer videoId, Integer viewerId) {
        Iterator<Assessment> iterator =
                assessmentDao.findByVideo_IdAndViewer_IdAndStatus(
                        videoId, viewerId, Assessment.STATUS_NORMAL).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public Assessment saveAssessment(Assessment assessment) {
        return assessmentDao.save(assessment);
    }
}
