package edu.hkpolyu.epre.service;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.MessageDao;
import edu.hkpolyu.epre.model.Message;

@Component
public class MessageService {

    private MessageDao messageDao;
    @Autowired
    public void setMessageDao(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    public Message getMessageById(Integer announcementId) {
        return messageDao.findOne(announcementId);
    }

    public Message saveMessage(Message announcement) {
        return messageDao.save(announcement);
    }

    public Page<Message> listMessageByFromUserId(
			Integer fromUserId, int page, int size) {
        return messageDao.findByFromUser_IdAndStatus(fromUserId,
                Message.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
	
	public Page<Message> listMessageByToUserId(
			Integer toUserId, int page, int size) {
        return messageDao.findByToUser_IdAndStatus(toUserId,
                Message.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
}
