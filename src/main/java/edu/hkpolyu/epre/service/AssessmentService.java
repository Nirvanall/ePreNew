package edu.hkpolyu.epre.service;

import java.util.List;
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

    public Assessment saveAssessment(Assessment assessment) {
        return assessmentDao.save(assessment);
    }

    public Page<Assessment> listAssessment(int page, int size) {
        return assessmentDao.findByStatusAndToUserIdIsNull(
                Assessment.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
}
