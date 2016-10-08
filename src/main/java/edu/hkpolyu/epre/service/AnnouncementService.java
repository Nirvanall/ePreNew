package edu.hkpolyu.epre.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.MessageDao;
import edu.hkpolyu.epre.model.Message;

public class AnnouncementService {

    @Autowired
    private MessageDao messageDao;

    public Page<Message> findAnnouncement(int page, int count) {
        return messageDao.findByToUserIdIsNull(new PageRequest(page, count));
    }
}
