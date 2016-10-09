package edu.hkpolyu.epre.service;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.MessageDao;
import edu.hkpolyu.epre.model.Message;

@Component
public class AnnouncementService {

    private MessageDao messageDao;
    @Autowired
    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public Message getAnnouncementById(Integer announcementId) {
        return messageDao.findOne(announcementId);
    }

    public Message saveAnnouncement(Message announcement) {
        return messageDao.save(announcement);
    }

    public Page<Message> listAnnouncement(int page, int size) {
        return messageDao.findByToUserIdIsNullAndStatus(
                Message.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
}
