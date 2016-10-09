package edu.hkpolyu.epre.service;

import java.util.Iterator;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.UserDao;
import edu.hkpolyu.epre.model.User;
import edu.hkpolyu.epre.model.UserPassword;

@Component
public class UserService {

    private UserDao userDao;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUserById(Integer userId) {
        return userDao.findOne(userId);
    }

    public User getUserByUserName(String userName) {
        Iterator<User> iterator = userDao.findByUserName(
                userName).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public User getUserByUserNameAndPassword(String userName, String password) {
        Iterator<User> iterator = userDao.findByUserNameAndPassword_Password(
                userName, UserPassword.sha256(password)).iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public Page<User> listUser(int page, int size) {
        return userDao.findByStatus(
                User.STATUS_NORMAL, new PageRequest(page - 1, size));
    }
}
