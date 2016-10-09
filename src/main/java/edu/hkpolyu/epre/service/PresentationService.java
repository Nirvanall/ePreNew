package edu.hkpolyu.epre.service;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.PresentationDao;
import edu.hkpolyu.epre.model.Presentation;

@Component
public class PresentationService {

    private PresentationDao presentationDao;
    @Autowired
    public void setPresentationDao(PresentationDao presentationDao) {
        this.presentationDao = presentationDao;
    }

    public Presentation getPresentationById(Integer presentationId) {
        return presentationDao.findOne(presentationId);
    }

    public Presentation savePresentation(Presentation presentation) {
        return presentationDao.save(presentation);
    }

    public Page<Presentation> listPresentation(int page, int size) {
        return presentationDao.findByStatusAndToUserIdIsNull(
                Presentation.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
}
