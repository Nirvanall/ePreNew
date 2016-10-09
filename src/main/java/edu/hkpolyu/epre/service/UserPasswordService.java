package edu.hkpolyu.epre.service;

import java.util.Iterator;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import edu.hkpolyu.epre.dao.UserPasswordDao;
import edu.hkpolyu.epre.model.UserPassword;

@Component
public class UserPasswordService {

    private UserPasswordDao passwordDao;
    @Autowired
    public void setUserPasswordDao(UserDao passwordDao) {
        this.passwordDao = passwordDao;
    }

    public boolean isPasswordCorrect(Integer userId, String password) {
        UserPassword passwordHash = passwordDao.findOne(userId);
        return UserPassword.sha256(password) == passwordHash.getPassword();
    }

    public UserPassword savePassword(Integer userId, String password) {
        UserPassword passwordHash = passwordDao.findOne(userId);
        passwordHash.setPassword(password);
        return passwordDao.save(passwordHash);
    }
}
