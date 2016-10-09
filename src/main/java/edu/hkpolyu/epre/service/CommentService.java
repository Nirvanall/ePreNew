package edu.hkpolyu.epre.service;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.CommentDao;
import edu.hkpolyu.epre.model.Comment;

@Component
public class CommentService {

    private CommentDao commentDao;
    @Autowired
    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    public Comment getCommentById(Integer commentId) {
        return commentDao.findOne(commentId);
    }

    public Comment saveComment(Comment comment) {
        return commentDao.save(comment);
    }

    public Page<Comment> listComment(int page, int size) {
        return commentDao.findByStatusAndToUserIdIsNull(
                Comment.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
}
