package edu.hkpolyu.epre.service;

import java.util.ArrayList;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    public Page<Comment> listCommentByVideoId(Integer videoId) {
		ArrayList<Sort.Order> orders = new ArrayList<Sort.Order>();
		orders.add(new Sort.Order(Sort.Direction.ASC, "playtime"));
		orders.add(new Sort.Order(Sort.Direction.ASC, "id"));
        return commentDao.findByVideo_IdAndStatus(videoId,
                Comment.STATUS_NORMAL, new Sort(orders));
    }
}
